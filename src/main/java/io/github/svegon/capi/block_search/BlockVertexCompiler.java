package io.github.svegon.capi.block_search;

import io.github.svegon.capi.util.RenderUtil;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public final class BlockVertexCompiler {
    private BlockVertexCompiler() {
        throw new UnsupportedOperationException();
    }

    public static Callable<VertexBuffer> sideVertices(final Collection<BlockPos> blocks) {
        return () -> {
            final Iterator<BufferBuilder.BuiltBuffer> partialResults = blocks.parallelStream()
                    .map((pos) -> getSideVertices(newQuadsBuilder(), pos, blocks).end()).iterator();

            partialResults.hasNext();

            return CompletableFuture.supplyAsync(() -> {
                VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                buffer.bind();

                if (partialResults.hasNext()) {
                    do {
                        buffer.upload(partialResults.next());
                    } while (partialResults.hasNext());
                } else {
                    buffer.upload(newQuadsBuilder().end());
                }

                VertexBuffer.unbind();

                return buffer;
            }, RenderUtil.RENDER_THREAD_EXECUTOR).join();
        };
    }

    public static Callable<VertexBuffer> outlineVertices(final Collection<BlockPos> blocks) {
        return () -> {
            final Iterator<BufferBuilder.BuiltBuffer> partialResults = blocks.parallelStream()
                    .map((pos) -> getOutlineVertices(newLinesBuilder(), pos, blocks).end()).iterator();

            partialResults.hasNext();

            return CompletableFuture.supplyAsync(() -> {
                VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                buffer.bind();

                if (partialResults.hasNext()) {
                    do {
                        buffer.upload(partialResults.next());
                    } while (partialResults.hasNext());
                } else {
                    buffer.upload(newLinesBuilder().end());
                }

                return buffer;
            }, RenderUtil.RENDER_THREAD_EXECUTOR).join();
        };
    }

    public static BufferBuilder getSideVertices(BufferBuilder buffer, BlockPos pos,
                                                Collection<BlockPos> matchingBlocks) {
        if(!matchingBlocks.contains(pos.down())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
        }

        if(!matchingBlocks.contains(pos.up())) {
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
        }

        if(!matchingBlocks.contains(pos.north())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
        }

        if(!matchingBlocks.contains(pos.east())) {
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
        }

        if(!matchingBlocks.contains(pos.south())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
        }

        if(!matchingBlocks.contains(pos.west())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
        }

        return buffer;
    }

    public static BufferBuilder getOutlineVertices(BufferBuilder buffer, BlockPos pos,
                                                Collection<BlockPos> matchingBlocks) {
        if(!matchingBlocks.contains(pos.down())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
        }

        if(!matchingBlocks.contains(pos.up())) {
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
        }

        if(!matchingBlocks.contains(pos.north())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
        }

        if(!matchingBlocks.contains(pos.east())) {
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ()).next();
        }

        if(!matchingBlocks.contains(pos.south())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
        }

        if(!matchingBlocks.contains(pos.west())) {
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ() + 1).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY() + 1, pos.getZ()).next();
            buffer.vertex(pos.getX(), pos.getY(), pos.getZ()).next();
        }

        return buffer;
    }

    public static BufferBuilder newLinesBuilder() {
        BufferBuilder bufferBuilder = new BufferBuilder(24);
        bufferBuilder.begin(VertexFormat.DrawMode.LINES, VertexFormats.POSITION);
        return bufferBuilder;
    }

    public static BufferBuilder newQuadsBuilder() {
        BufferBuilder bufferBuilder = new BufferBuilder(24);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        return bufferBuilder;
    }
}
