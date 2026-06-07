package io.github.svegon.mclientapi.client.block_search

import com.mojang.blaze3d.buffers.GpuBuffer
import io.github.svegon.mclientapi.block_search.BlockSearchManager
import io.github.svegon.mclientapi.client.block_search.BlockVertexCompiler.compileOutlineVertices
import io.github.svegon.mclientapi.client.block_search.BlockVertexCompiler.compileSideVertices
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import java.util.concurrent.ForkJoinTask

class BlockSearchAndVertexCompilationManager(
    blockCondition: (BlockPos.MutableBlockPos) -> Boolean,
    limit: () -> Int, minHeight: Int, maxHeight: Int
): BlockSearchManager(blockCondition, limit, minHeight, maxHeight) {
    private var sideVertices: GpuBuffer? = null
    private var sideVertexesTask: ForkJoinTask<GpuBuffer>? = null
    private var outlineVertexes: GpuBuffer? = null
    private var outlineVerticesTask: ForkJoinTask<GpuBuffer>? = null
    private var verticesUpToDate = false

    init {
        addListener { self, result -> verticesUpToDate = true }
    }

    constructor(
        blockCondition: (BlockPos.MutableBlockPos) -> Boolean,
        minHeight: Int, maxHeight: Int
    ) : this(blockCondition, object : () -> Int {
        // approximately amount of BlockPos instances we can fit into the memory
        val constant = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()).toInt() / 72

        override fun invoke(): Int {
            return constant
        }
    }, minHeight, maxHeight)

    override fun addChunk(chunk: ChunkPos) {
        super.addChunk(chunk)
        verticesUpToDate = false
    }

    override fun removeChunk(chunk: ChunkPos) {
        super.removeChunk(chunk)
        verticesUpToDate = false
    }

    fun compileSideVertices(): ForkJoinTask<GpuBuffer> {
        if (sideVertexesTask == null) {
            return (this as BlockSearchManager).compileSideVertices().also { sideVertexesTask = it }
        }

        if (sideVertexesTask!!.isCompletedNormally) {
            sideVertices = sideVertexesTask!!.getRawResult()
        }

        if (!verticesUpToDate && sideVertexesTask!!.isDone()) {
            return (this as BlockSearchManager).compileSideVertices().also { sideVertexesTask = it }
        }

        return sideVertexesTask!!
    }

    fun compileOutlineVertices(): ForkJoinTask<GpuBuffer> {
        if (outlineVerticesTask == null) {
            return (this as BlockSearchManager).compileOutlineVertices().also { outlineVerticesTask = it }
        }

        if (outlineVerticesTask!!.isCompletedNormally) {
            outlineVertexes = outlineVerticesTask!!.rawResult
        }

        if (!verticesUpToDate && outlineVerticesTask!!.isDone) {
            return (this as BlockSearchManager).compileOutlineVertices().also { outlineVerticesTask = it }
        }

        return outlineVerticesTask!!
    }

    fun getSideVertexes(): GpuBuffer? {
        val task: ForkJoinTask<GpuBuffer> = compileSideVertices()

        if (verticesUpToDate) {
            return sideVertices!!
        }

        return task.rawResult
    }

    fun getOutlineVertexes(): GpuBuffer? {
        val task: ForkJoinTask<GpuBuffer> = compileOutlineVertices()

        if (verticesUpToDate) {
            return outlineVertexes
        }

        return task.rawResult
    }
}
