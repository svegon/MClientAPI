package io.github.svegon.capi.block_search;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ChunkSearcher {
    private final Collection<BlockPos> intermediateResult = Lists.newLinkedList();
    private final BlockSearchManager manager;
    private final ChunkPos chunk;
    private ChunkSearcher.Status status = Status.IDLE;
    private Future<?> future;

    public ChunkSearcher(BlockSearchManager manager, ChunkPos chunk) {
        this.manager = manager;
        this.chunk = chunk;
    }

    public void startSearching(ExecutorService pool) {
        if(status != Status.IDLE) {
            throw new IllegalStateException();
        }

        status = Status.SEARCHING;
        future = pool.submit(this::searchNow);
    }

    private void searchNow() {
        Status status0 = status;

        if (status0 == Status.INTERRUPTED) {
            return;
        }

        if (status0 != Status.SEARCHING) {
            throw new IllegalStateException();
        }

        // clear out chunk
        manager.results.removeIf(pos -> pos.getX() >> 4 == chunk.x && pos.getZ() >> 4 == chunk.z);

        BlockSearchManager.search(new BlockPos(chunk.getStartX(), manager.getMinHeight(), chunk.getStartZ()),
                new BlockPos(chunk.getEndX(), manager.getMaxHeight(), chunk.getEndZ()), manager.getBlockCondition(),
                (pos) -> status == Status.INTERRUPTED || manager.limitReached(),
                intermediateResult);

        if (status != Status.INTERRUPTED) {
            status = Status.DONE;
            manager.results.addAll(intermediateResult);
        }
    }

    public void cancelSearching() {
        new Thread(this::cancelNow, "ChunkSearcher-canceller-#"
                + BlockSearchManager.UNIQUE_ID_SUPPLIER.getAndIncrement()).start();
    }

    private void cancelNow() {
        if (future != null) {
            try {
                status = Status.INTERRUPTED;
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        status = Status.IDLE;
    }

    public boolean terminated() {
        return status == Status.INTERRUPTED || status == Status.DONE;
    }

    public enum Status {
        IDLE,
        SEARCHING,
        INTERRUPTED,
        DONE
    }
}
