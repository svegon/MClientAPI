package io.github.svegon.mclientapi.client.block_search

import io.github.svegon.mclientapi.block_search.BlockSearchManager
import io.github.svegon.mclientapi.client.block_search.BlockVertexCompiler.compileOutlineVertices
import io.github.svegon.mclientapi.client.block_search.BlockVertexCompiler.compileSideVertices
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import java.util.concurrent.ForkJoinTask
import java.util.function.IntSupplier
import java.util.function.Predicate

class BlockSearchAndVertexCompilationManager(
    blockCondition: (BlockPos.Mutable) -> Boolean,
    limit: () -> Int, minHeight: Int, maxHeight: Int
) : BlockSearchManager(blockCondition, limit, minHeight, maxHeight) {
    private var sideVertices: VertexBuffer? = null
    private var sideVertexesTask: ForkJoinTask<VertexBuffer>? = null
    private var outlineVertexes: VertexBuffer? = null
    private var outlineVerticesTask: ForkJoinTask<VertexBuffer>? = null
    private var vertexesUpToDate = false

    init {
        addListener { self, result -> vertexesUpToDate = true }
    }

    constructor(
        blockCondition: (BlockPos.Mutable) -> Boolean,
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
        vertexesUpToDate = false
    }

    override fun removeChunk(chunk: ChunkPos) {
        super.removeChunk(chunk)
        vertexesUpToDate = false
    }

    fun compileSideVertices(): ForkJoinTask<VertexBuffer> {
        if (sideVertexesTask == null) {
            return (this as BlockSearchManager).compileSideVertices().also { sideVertexesTask = it }
        }

        if (sideVertexesTask!!.isCompletedNormally) {
            sideVertices = sideVertexesTask!!.getRawResult()
        }

        if (!vertexesUpToDate && sideVertexesTask!!.isDone()) {
            return (this as BlockSearchManager).compileSideVertices().also { sideVertexesTask = it }
        }

        return sideVertexesTask!!
    }

    fun compileOutlineVertices(): ForkJoinTask<VertexBuffer> {
        if (outlineVerticesTask == null) {
            return (this as BlockSearchManager).compileOutlineVertices().also { outlineVerticesTask = it }
        }

        if (outlineVerticesTask!!.isCompletedNormally) {
            outlineVertexes = outlineVerticesTask!!.rawResult
        }

        if (!vertexesUpToDate && outlineVerticesTask!!.isDone) {
            return (this as BlockSearchManager).compileOutlineVertices().also { outlineVerticesTask = it }
        }

        return outlineVerticesTask!!
    }

    fun getSideVertexes(): VertexBuffer? {
        val task: ForkJoinTask<VertexBuffer> = compileSideVertices()

        if (vertexesUpToDate) {
            return sideVertices!!
        }

        return task.rawResult
    }

    fun getOutlineVertexes(): VertexBuffer? {
        val task: ForkJoinTask<VertexBuffer> = compileOutlineVertices()

        if (vertexesUpToDate) {
            return outlineVertexes
        }

        return task.rawResult
    }
}
