package io.github.svegon.capi.block_search;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.lang.ref.Cleaner;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

public class BlockSearchManager implements AutoCloseable {
    static final Cleaner CLEANER = Cleaner.create();
    static final AtomicInteger UNIQUE_ID_SUPPLIER = new AtomicInteger();

    final Set<BlockPos> results = Sets.newConcurrentHashSet();
    private final Map<ChunkPos, ChunkSearcher> searchers = Maps.newConcurrentMap();
    private final Set<BiConsumer<BlockSearchManager, Set<BlockPos>>> matchingBlocksListeners =
            Sets.newConcurrentHashSet();
    private final Predicate<? super BlockPos.Mutable> blockCondition;
    private final IntSupplier limit;
    private final int minHeight;
    private final int maxHeight;
    private final Cleaner.Cleanable cleanable;
    private ForkJoinPool pool = standardPool();

    public BlockSearchManager(Predicate<? super BlockPos.Mutable> blockCondition, IntSupplier limit,
                              int minHeight, int maxHeight) {
        this.blockCondition = blockCondition;
        this.limit = limit;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.cleanable = CLEANER.register(this, new Finalizer(results, searchers, pool));
    }

    public BlockSearchManager(Predicate<? super BlockPos.Mutable> blockCondition, int minHeight, int maxHeight) {
        this(blockCondition, new IntSupplier() {
            // approximately amount of BlockPos instances we can fit into the memory
            final int constant = (int) (Runtime.getRuntime().freeMemory()) / 72;

            @Override
            public int getAsInt() {
                return constant;
            }
        }, minHeight, maxHeight);
    }

    @Override
    public void close() throws Exception {
        cleanable.clean();
    }

    public void start() {
        pool = standardPool();
        pool.submit(this::completeSearchListener);
    }

    public void stop() {
        pool.shutdownNow();
        searchers.clear();
    }

    public void addChunk(ChunkPos chunk) {
        ChunkSearcher searcher = new ChunkSearcher(this, chunk);
        ChunkSearcher oldSearcher = searchers.put(chunk, searcher);

        if (pool.isTerminated()) {
            searchers.clear();
            start();
        }

        searcher.startSearching(pool);

        if (oldSearcher != null) {
            oldSearcher.cancelSearching();
        }
    }

    public void addChunks(Collection<ChunkPos> chunks) {
        chunks.parallelStream().forEach(this::addChunk);
    }

    public void addChunks(ChunkPos... chunks) {
        addChunks(Arrays.asList(chunks));
    }

    public void removeChunk(ChunkPos chunk) {
        ChunkSearcher searcher = searchers.remove(chunk);

        if (searcher != null) {
            searcher.cancelSearching();
        }
    }

    public void removeChunks(Collection<ChunkPos> chunks) {
        chunks.parallelStream().forEach(this::removeChunk);
    }

    public void removeChunks(ChunkPos... chunks) {
        removeChunks(Arrays.asList(chunks));
    }

    public boolean addListener(BiConsumer<BlockSearchManager, Set<BlockPos>> listener) {
        return matchingBlocksListeners.add(Preconditions.checkNotNull(listener));
    }

    public boolean removeListener(BiConsumer<BlockSearchManager, Set<BlockPos>> listener) {
        return matchingBlocksListeners.remove(listener);
    }

    private void completeSearchListener() {
        if (pool.awaitQuiescence(Long.MAX_VALUE, TimeUnit.DAYS)) {
            for (BiConsumer<BlockSearchManager, Set<BlockPos>> listener : matchingBlocksListeners) {
                listener.accept(this, Collections.unmodifiableSet(results));
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public ForkJoinTask<VertexBuffer> compileSideVertexes() {
        return standardPool().submit(BlockVertexCompiler.sideVertices(results));
    }

    @Environment(EnvType.CLIENT)
    public ForkJoinTask<VertexBuffer> compileOutlineVertexes() {
        return standardPool().submit(BlockVertexCompiler.outlineVertices(results));
    }

    public static List<BlockPos> search(BlockPos from, BlockPos to, Predicate<BlockPos.Mutable> blockCondition,
                                        Predicate<BlockPos.Mutable> stopCondition) {
        LinkedList<BlockPos> list = Lists.newLinkedList();

        search(from, to, blockCondition, stopCondition, list);

        return list;
    }

    @SuppressWarnings("unchecked")
    public static void search(BlockPos from, BlockPos to, Predicate<? super BlockPos.Mutable> blockCondition,
                              Predicate<? super BlockPos.Mutable> stopCondition, Collection<BlockPos> resultSet) {
        for (BlockPos.Mutable pos : ((Iterable<BlockPos.Mutable>) (Object) BlockPos.iterate(from, to))) {
            if (stopCondition.test(pos)) {
                return;
            }

            if (blockCondition.test(pos)) {
                resultSet.add(pos.toImmutable());
            }
        }
    }

    public Predicate<? super BlockPos.Mutable> getBlockCondition() {
        return blockCondition;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public static ForkJoinPool standardPool() {
        return new ForkJoinPool(Math.min(Runtime.getRuntime().availableProcessors(), 0x7fff), pool -> {
            ForkJoinWorkerThread t = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);

            t.setPriority(Thread.MIN_PRIORITY);

            return t;
            }, (t, e) -> e.printStackTrace(), true,0, 0x7fff, 1,
                null, 20, TimeUnit.MILLISECONDS);
    }

    public IntSupplier getLimit() {
        return limit;
    }

    public boolean limitReached() {
        return results.size() >= limit.getAsInt();
    }

    private record Finalizer(Set<BlockPos> results, Map<ChunkPos, ChunkSearcher> searchers,
                             ForkJoinPool pool) implements Runnable {
            @Override
            public void run() {
                pool.shutdownNow();
                searchers.clear();
                results.clear();

                Thread t = new Thread(System::gc, "Garbage-collecting-thread-" + UNIQUE_ID_SUPPLIER.get());
                t.setUncaughtExceptionHandler((th, e) -> {});
                t.setPriority(Thread.MIN_PRIORITY);
                t.setDaemon(true);
                t.start();
            }
        }
}
