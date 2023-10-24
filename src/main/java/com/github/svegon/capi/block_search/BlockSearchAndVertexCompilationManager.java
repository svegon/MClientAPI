package com.github.svegon.capi.block_search;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class BlockSearchAndVertexCompilationManager extends BlockSearchManager {
    private VertexBuffer sideVertexes;
    private ForkJoinTask<VertexBuffer> sideVertexesTask;
    private VertexBuffer outlineVertexes;
    private ForkJoinTask<VertexBuffer> outlineVertexesTask;
    private boolean vertexesUpToDate;

    public BlockSearchAndVertexCompilationManager(Predicate<? super BlockPos.Mutable> blockCondition,
                                                  IntSupplier limit, int minHeight, int maxHeight) {
        super(blockCondition, limit, minHeight, maxHeight);
        addListener((self, result) -> vertexesUpToDate = true);
    }

    public BlockSearchAndVertexCompilationManager(Predicate<? super BlockPos.Mutable> blockCondition,
                                                  int minHeight, int maxHeight) {
        this(blockCondition, new IntSupplier() {
            // approximately amount of BlockPos instances we can fit into the memory
            final int constant = (int) (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) / 72;

            @Override
            public int getAsInt() {
                return constant;
            }
        }, minHeight, maxHeight);
    }

    @Override
    public void addChunk(ChunkPos chunk) {
        super.addChunk(chunk);
        vertexesUpToDate = false;
    }

    @Override
    public void removeChunk(ChunkPos chunk) {
        super.removeChunk(chunk);
        vertexesUpToDate = false;
    }

    @Override
    public ForkJoinTask<VertexBuffer> compileSideVertexes() {
        if (sideVertexesTask == null) {
            return sideVertexesTask = super.compileSideVertexes();
        }

        if (sideVertexesTask.isCompletedNormally()) {
            sideVertexes = sideVertexesTask.getRawResult();
        }

        if (!vertexesUpToDate && sideVertexesTask.isDone()) {
            return sideVertexesTask = super.compileSideVertexes();
        }

        return sideVertexesTask;
    }

    @Override
    public ForkJoinTask<VertexBuffer> compileOutlineVertexes() {
        if (outlineVertexesTask == null) {
            return outlineVertexesTask = super.compileOutlineVertexes();
        }

        if (outlineVertexesTask.isCompletedNormally()) {
            outlineVertexes = outlineVertexesTask.getRawResult();
        }

        if (!vertexesUpToDate && outlineVertexesTask.isDone()) {
            return outlineVertexesTask = super.compileOutlineVertexes();
        }

        return outlineVertexesTask;
    }

    @Nullable
    public VertexBuffer getSideVertexes() {
        ForkJoinTask<VertexBuffer> task = compileSideVertexes();

        if (vertexesUpToDate) {
            return sideVertexes;
        }

        return task.getRawResult();
    }

    @Nullable
    public VertexBuffer getOutlineVertexes() {
        ForkJoinTask<VertexBuffer> task = compileOutlineVertexes();

        if (vertexesUpToDate) {
            return outlineVertexes;
        }

        return task.getRawResult();
    }
}
