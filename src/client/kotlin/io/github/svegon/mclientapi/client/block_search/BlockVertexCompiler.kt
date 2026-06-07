package io.github.svegon.mclientapi.client.block_search

import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import io.github.svegon.mclientapi.block_search.BlockSearchManager
import io.github.svegon.mclientapi.block_search.BlockSearchManager.Companion.standardPool
import net.minecraft.core.BlockPos
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinTask

object BlockVertexCompiler {
    private val tesselator = Tesselator.getInstance();

    fun sideVertices(blocks: Collection<BlockPos>): Callable<GpuBuffer> {
        return Callable<GpuBuffer> {
            val builder = tesselator.begin(VertexFormat.Mode.QUADS,
                    DefaultVertexFormat.POSITION_COLOR)

            for (pos in blocks) {
                getSideVertices(builder, pos, blocks)
            }

            RenderSystem.getDevice().createBuffer(null,
                GpuBuffer.USAGE_VERTEX or GpuBuffer.USAGE_COPY_DST, builder.buildOrThrow().vertexBuffer())
        }
    }

    fun outlineVertices(blocks: Collection<BlockPos>): Callable<GpuBuffer> {
        return Callable<GpuBuffer> {
            val builder = tesselator.begin(VertexFormat.Mode.LINES,
                DefaultVertexFormat.POSITION_COLOR)

            for (pos in blocks) {
                getOutlineVertices(builder, pos, blocks)
            }

            RenderSystem.getDevice().createBuffer(null,
                GpuBuffer.USAGE_VERTEX or GpuBuffer.USAGE_COPY_DST, builder.buildOrThrow().vertexBuffer())
        }
    }

    fun getSideVertices(
        buffer: BufferBuilder, pos: BlockPos,
        matchingBlocks: Collection<BlockPos>
    ): BufferBuilder {
        if (!matchingBlocks.contains(pos.below())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.above())) {
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.north())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.east())) {
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.south())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.west())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
        }

        return buffer
    }

    fun getOutlineVertices(
        buffer: BufferBuilder, pos: BlockPos,
        matchingBlocks: Collection<BlockPos>
    ): BufferBuilder {
        if (!matchingBlocks.contains(pos.below())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.above())) {
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.north())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.east())) {
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.south())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.west())) {
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.addVertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
        }

        return buffer
    }

    fun BlockSearchManager.compileOutlineVertices(): ForkJoinTask<GpuBuffer> {
        return standardPool().submit(outlineVertices(results))
    }

    fun BlockSearchManager.compileSideVertices(): ForkJoinTask<GpuBuffer> {
        return standardPool().submit(sideVertices(results))
    }
}
