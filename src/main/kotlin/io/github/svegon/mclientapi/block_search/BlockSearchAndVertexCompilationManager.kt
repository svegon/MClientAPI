package io.github.svegon.capi.block_search

import net.fabricmc.api.Environment
import net.minecraft.client.gl.VertexBuffer
import java.util.concurrent.ForkJoinTask
import java.util.function.IntSupplier
import java.util.function.Predicate

@Environment(EnvType.CLIENT)
class BlockSearchAndVertexCompilationManager(
    blockCondition: Predicate<in BlockPos.Mutable?>,
    limit: IntSupplier, minHeight: Int, maxHeight: Int
) : BlockSearchManager(blockCondition, limit, minHeight, maxHeight) {
    private var sideVertexes: VertexBuffer? = null
    private var sideVertexesTask: ForkJoinTask<VertexBuffer>? = null
    private var outlineVertexes: VertexBuffer? = null
    private var outlineVertexesTask: ForkJoinTask<VertexBuffer>? = null
    private var vertexesUpToDate = false

    init {
        addListener { self, result -> vertexesUpToDate = true }
    }

    constructor(
        blockCondition: Predicate<in BlockPos.Mutable?>,
        minHeight: Int, maxHeight: Int
    ) : this(blockCondition, object : IntSupplier {
        // approximately amount of BlockPos instances we can fit into the memory
        val constant: Int = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()).toInt() / 72

        override fun getAsInt(): Int {
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

    override fun compileSideVertexes(): ForkJoinTask<VertexBuffer> {
        if (sideVertexesTask == null) {
            return super.compileSideVertexes().also { sideVertexesTask = it }
        }

        if (sideVertexesTask!!.isCompletedNormally()) {
            sideVertexes = sideVertexesTask!!.getRawResult()
        }

        if (!vertexesUpToDate && sideVertexesTask!!.isDone()) {
            return super.compileSideVertexes().also { sideVertexesTask = it }
        }

        return sideVertexesTask
    }

    override fun compileOutlineVertexes(): ForkJoinTask<VertexBuffer> {
        if (outlineVertexesTask == null) {
            return super.compileOutlineVertexes().also { outlineVertexesTask = it }
        }

        if (outlineVertexesTask!!.isCompletedNormally()) {
            outlineVertexes = outlineVertexesTask!!.getRawResult()
        }

        if (!vertexesUpToDate && outlineVertexesTask!!.isDone()) {
            return super.compileOutlineVertexes().also { outlineVertexesTask = it }
        }

        return outlineVertexesTask
    }

    fun getSideVertexes(): VertexBuffer? {
        val task: ForkJoinTask<VertexBuffer> = compileSideVertexes()

        if (vertexesUpToDate) {
            return sideVertexes
        }

        return task.getRawResult()
    }

    fun getOutlineVertexes(): VertexBuffer? {
        val task: ForkJoinTask<VertexBuffer> = compileOutlineVertexes()

        if (vertexesUpToDate) {
            return outlineVertexes
        }

        return task.getRawResult()
    }
}
