package io.github.svegon.capi.block_search

import net.minecraft.client.gl.VertexBuffer
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import java.util.function.Supplier

class BlockVertexCompiler private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        fun sideVertices(blocks: Collection<BlockPos>): Callable<VertexBuffer> {
            return Callable<VertexBuffer> {
                val partialResults: Iterator<BuiltBuffer> = blocks.parallelStream()
                    .map<Any>(Function<BlockPos, Any> { pos: BlockPos ->
                        getSideVertices(
                            newQuadsBuilder(),
                            pos,
                            blocks
                        ).end()
                    }).iterator()
                partialResults.hasNext()
                CompletableFuture.supplyAsync<Any>(Supplier<Any> {
                    val buffer: VertexBuffer = VertexBuffer(VertexBuffer.Usage.STATIC)
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
                }, RenderUtil.Companion.RENDER_THREAD_EXECUTOR).join()
            }
        }

        fun outlineVertices(blocks: Collection<BlockPos>): Callable<VertexBuffer> {
            return Callable<VertexBuffer> {
                val partialResults: Iterator<BuiltBuffer> = blocks.parallelStream()
                    .map<Any>(Function<BlockPos, Any> { pos: BlockPos ->
                        getOutlineVertices(
                            newLinesBuilder(),
                            pos,
                            blocks
                        ).end()
                    }).iterator()
                partialResults.hasNext()
                CompletableFuture.supplyAsync<Any>(Supplier<Any> {
                    val buffer: VertexBuffer = VertexBuffer(VertexBuffer.Usage.STATIC)
                    buffer.bind()

                    if (partialResults.hasNext()) {
                        do {
                            buffer.upload(partialResults.next())
                        } while (partialResults.hasNext())
                    } else {
                        buffer.upload(newLinesBuilder().end())
                    }
                    buffer
                }, RenderUtil.Companion.RENDER_THREAD_EXECUTOR).join()
            }
        }

        fun getSideVertices(
            buffer: BufferBuilder, pos: BlockPos,
            matchingBlocks: Collection<BlockPos>
        ): BufferBuilder {
            if (!matchingBlocks.contains(pos.down())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
            }

            if (!matchingBlocks.contains(pos.up())) {
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
            }

            if (!matchingBlocks.contains(pos.north())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
            }

            if (!matchingBlocks.contains(pos.east())) {
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
            }

            if (!matchingBlocks.contains(pos.south())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
            }

            if (!matchingBlocks.contains(pos.west())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
            }

            return buffer
        }

        fun getOutlineVertices(
            buffer: BufferBuilder, pos: BlockPos,
            matchingBlocks: Collection<BlockPos>
        ): BufferBuilder {
            if (!matchingBlocks.contains(pos.down())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
            }

            if (!matchingBlocks.contains(pos.up())) {
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
            }

            if (!matchingBlocks.contains(pos.north())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
            }

            if (!matchingBlocks.contains(pos.east())) {
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next()
            }

            if (!matchingBlocks.contains(pos.south())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
            }

            if (!matchingBlocks.contains(pos.west())) {
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next()
                buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next()
            }

            return buffer
        }

        fun newLinesBuilder(): BufferBuilder {
            val bufferBuilder: BufferBuilder = BufferBuilder(24)
            bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION)
            return bufferBuilder
        }

        fun newQuadsBuilder(): BufferBuilder {
            val bufferBuilder: BufferBuilder = BufferBuilder(24)
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION)
            return bufferBuilder
        }
    }
}
