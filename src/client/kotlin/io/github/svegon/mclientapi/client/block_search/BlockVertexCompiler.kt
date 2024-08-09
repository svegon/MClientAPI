package io.github.svegon.mclientapi.client.block_search

import io.github.svegon.mclientapi.block_search.BlockSearchManager
import io.github.svegon.mclientapi.block_search.BlockSearchManager.Companion.standardPool
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.BuiltBuffer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.BufferAllocator
import net.minecraft.util.math.BlockPos
import java.util.concurrent.Callable
import java.util.concurrent.ForkJoinTask

object BlockVertexCompiler {
    private val bufferAllocator = BufferAllocator(576)

    fun sideVertices(blocks: Collection<BlockPos>): Callable<VertexBuffer> {
        return Callable<VertexBuffer> {
            val partialResults: Iterator<BuiltBuffer> = blocks.parallelStream()
                .map { pos: BlockPos ->
                    getSideVertices(
                        newQuadsBuilder(),
                        pos,
                        blocks
                    ).end()
                }.iterator()
            partialResults.hasNext()
            val buffer = VertexBuffer(VertexBuffer.Usage.STATIC)
            buffer.bind()

            if (partialResults.hasNext()) {
                do {
                    buffer.upload(partialResults.next())
                } while (partialResults.hasNext())
            } else {
                buffer.upload(newQuadsBuilder().end())
            }

            VertexBuffer.unbind()
            buffer
        }
    }

    fun outlineVertices(blocks: Collection<BlockPos>): Callable<VertexBuffer> {
        return Callable<VertexBuffer> {
            val partialResults: Iterator<BuiltBuffer> = blocks.parallelStream()
                .map { pos: BlockPos ->
                    getOutlineVertices(
                        newLinesBuilder(),
                        pos,
                        blocks
                    ).end()
                }.iterator()
            partialResults.hasNext()
            val buffer = VertexBuffer(VertexBuffer.Usage.STATIC)
            buffer.bind()

            if (partialResults.hasNext()) {
                do {
                    buffer.upload(partialResults.next())
                } while (partialResults.hasNext())
            } else {
                buffer.upload(newLinesBuilder().end())
            }
            buffer
        }
    }

    fun getSideVertices(
        buffer: BufferBuilder, pos: BlockPos,
        matchingBlocks: Collection<BlockPos>
    ): BufferBuilder {
        if (!matchingBlocks.contains(pos.down())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.up())) {
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.north())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.east())) {
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.south())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.west())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
        }

        return buffer
    }

    fun getOutlineVertices(
        buffer: BufferBuilder, pos: BlockPos,
        matchingBlocks: Collection<BlockPos>
    ): BufferBuilder {
        if (!matchingBlocks.contains(pos.down())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.up())) {
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.north())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.east())) {
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z.toFloat())
        }

        if (!matchingBlocks.contains(pos.south())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x + 1f, pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
        }

        if (!matchingBlocks.contains(pos.west())) {
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z + 1f)
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y + 1f, pos.z.toFloat())
            buffer.vertex(pos.x.toFloat(), pos.y.toFloat(), pos.z.toFloat())
        }

        return buffer
    }

    fun BlockSearchManager.compileOutlineVertices(): ForkJoinTask<VertexBuffer> {
        return standardPool().submit(outlineVertices(results))
    }

    fun BlockSearchManager.compileSideVertices(): ForkJoinTask<VertexBuffer> {
        return standardPool().submit(sideVertices(results))
    }

    private fun newLinesBuilder(): BufferBuilder {
        return BufferBuilder(bufferAllocator, VertexFormat.DrawMode.LINES, VertexFormats.POSITION)
    }

    private fun newQuadsBuilder(): BufferBuilder {
        return BufferBuilder(bufferAllocator, VertexFormat.DrawMode.QUADS, VertexFormats.POSITION)
    }
}
