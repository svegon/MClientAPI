package com.github.svegon.capi.util;

import com.github.svegon.utils.multithreading.ReentrantThreadExecutor;
import com.mojang.blaze3d.systems.RenderCall;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class RenderUtil {
    private RenderUtil() {
        throw new UnsupportedOperationException();
    }

    public static final ReentrantThreadExecutor<Runnable> RENDER_THREAD_EXECUTOR = new RenderThreadExecutor();

    public static int color4FToI(float[] a) {
        return (int) (a[0] * 255F) << 24 | (int) (a[1] * 255F) << 16 | (int) (a[2] * 255F) << 8 | (int) (a[3] * 255F);
    }

    public static float[] colorITo4F(int color) {
        return new float[]{(color >> 24) / 255F, ((color >> 16) & 255) / 255F, ((color >> 8) & 255) / 255F,
                (color & 255) / 255F};
    }

    public static void setShaderColor(final float @NotNull ... components) {
        if (components.length < 4) {
            RenderSystem.setShaderColor(components[0], components[1], components[2], 1);
        } else {
            RenderSystem.setShaderColor(components[0], components[1], components[2], components[3]);
        }
    }

    public static void setShaderColor(int red, int green, int blue, int alpha) {
        RenderSystem.setShaderColor(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void setShaderColor(int color) {
        setShaderColor(ColorHelper.Argb.getRed(color), ColorHelper.Argb.getGreen(color),
                ColorHelper.Argb.getBlue(color), ColorHelper.Argb.getAlpha(color));
    }

    public static void setShaderRed(float red) {
        RenderSystem.getShaderColor()[0] = red;
    }

    public static void setShaderGreen(float green) {
        RenderSystem.getShaderColor()[1] = green;
    }

    public static void setShaderBlue(float blue) {
        RenderSystem.getShaderColor()[2] = blue;
    }

    public static void setShaderAlpha(float alpha) {
        RenderSystem.getShaderColor()[3] = alpha;
    }

    public static void setShaderRed(int red) {
        setShaderRed(red / 255F);
    }

    public static void setShaderGreen(int green) {
        setShaderGreen(green / 255F);
    }

    public static void setShaderBlue(int blue) {
        setShaderBlue(blue / 255F);
    }

    public static void setShaderAlpha(int alpha) {
        setShaderAlpha(alpha / 255F);
    }

    public static void resetShaderColor() {
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
    public static void rect(VertexConsumer vertexConsumer, float x1, float y1, float x2, float y2) {
        zRect(vertexConsumer, 0, x1, y1, x2, y2);
    }

    public static void rect(VertexConsumer vertexConsumer, Matrix4f matrix, float x1, float y1, float x2, float y2) {
        zRect(vertexConsumer, matrix, 0, x1, y1, x2, y2);
    }

    public static void rect(VertexConsumer vertexConsumer, Rectangle rect) {
        rect(vertexConsumer, rect.x, rect.y, rect.x + rect.width, rect.y - rect.height);
    }

    public static void rect(VertexConsumer vertexConsumer, Matrix4f matrix, Rectangle rect) {
        rect(vertexConsumer, matrix, rect.x, rect.y, rect.x + rect.width, rect.y - rect.height);
    }

    public static void xRect(VertexConsumer vertexConsumer, float x, float y1, float z1, float y2, float z2) {
        vertexConsumer.vertex(x, y1, z1).next();
        vertexConsumer.vertex(x, y1, z2).next();
        vertexConsumer.vertex(x, y2, z2).next();
        vertexConsumer.vertex(x, y2, z1).next();
    }

    public static void xRect(VertexConsumer vertexConsumer, Matrix4f matrix, float x, float y1, float z1, float y2,
                             float z2) {
        vertexConsumer.vertex(matrix, x, y1, z1).next();
        vertexConsumer.vertex(matrix, x, y1, z2).next();
        vertexConsumer.vertex(matrix, x, y2, z2).next();
        vertexConsumer.vertex(matrix, x, y2, z1).next();
    }

    public static void xRect(VertexConsumer vertexConsumer, double x, double y1, double z1, double y2, double z2) {
        xRect(vertexConsumer, (float) x, (float) y1, (float) z1, (float) y2, (float) z2);
    }

    public static void xRect(VertexConsumer vertexConsumer, Matrix4f matrix, double x, double y1, double z1, double y2,
                             double z2) {
        xRect(vertexConsumer, matrix, (float) x, (float) y1, (float) z1, (float) y2, (float) z2);
    }

    public static void yRect(VertexConsumer vertexConsumer, float y, float x1, float z1, float x2, float z2) {
        vertexConsumer.vertex(x1, y, z1).next();
        vertexConsumer.vertex(x1, y, z2).next();
        vertexConsumer.vertex(x2, y, z2).next();
        vertexConsumer.vertex(x2, y, z1).next();
    }

    public static void yRect(VertexConsumer vertexConsumer, Matrix4f matrix, float y, float x1, float z1, float x2,
                             float z2) {
        vertexConsumer.vertex(matrix, x1, y, z1).next();
        vertexConsumer.vertex(matrix, x1, y, z2).next();
        vertexConsumer.vertex(matrix, x2, y, z2).next();
        vertexConsumer.vertex(matrix, x2, y, z1).next();
    }

    public static void yRect(VertexConsumer vertexConsumer, double x, double y1, double z1, double y2, double z2) {
        yRect(vertexConsumer, (float) x, (float) y1, (float) z1, (float) y2, (float) z2);
    }

    public static void yRect(VertexConsumer vertexConsumer, Matrix4f matrix, double x, double y1, double z1, double y2,
                             double z2) {
        yRect(vertexConsumer, matrix, (float) x, (float) y1, (float) z1, (float) y2, (float) z2);
    }

    public static void zRect(VertexConsumer vertexConsumer, float z, float x1, float y1, float x2, float y2) {
        vertexConsumer.vertex(x1, y1, z).next();
        vertexConsumer.vertex(x1, y2, z).next();
        vertexConsumer.vertex(x2, y2, z).next();
        vertexConsumer.vertex(x2, y1, z).next();
    }

    public static void zRect(VertexConsumer vertexConsumer, Matrix4f matrix, float z, float x1, float y1, float x2,
                             float y2) {
        vertexConsumer.vertex(matrix, x1, y1, z).next();
        vertexConsumer.vertex(matrix, x1, y2, z).next();
        vertexConsumer.vertex(matrix, x2, y2, z).next();
        vertexConsumer.vertex(matrix, x2, y1, z).next();
    }

    public static void zRect(VertexConsumer vertexConsumer, double x, double y1, double z1, double y2, double z2) {
        zRect(vertexConsumer, (float) x, (float) y1, (float) z1, (float) y2, (float) z2);
    }

    public static void zRect(VertexConsumer vertexConsumer, Matrix4f matrix, double x, double y1, double z1, double y2,
                             double z2) {
        zRect(vertexConsumer, matrix, (float) x, (float) y1, (float) z1, (float) y2, (float) z2);
    }

    public static void zRectLines(VertexConsumer vertexConsumer, float z, float x1, float y1, float x2, float y2) {
        vertexConsumer.vertex(x1, y1, z).next();
        vertexConsumer.vertex(x2, y1, z).next();
        vertexConsumer.vertex(x2, y1, z).next();
        vertexConsumer.vertex(x2, y2, z).next();
        vertexConsumer.vertex(x2, y2, z).next();
        vertexConsumer.vertex(x1, y2, z).next();
        vertexConsumer.vertex(x1, y2, z).next();
        vertexConsumer.vertex(x1, y1, z).next();
    }

    public static void zRectLines(VertexConsumer vertexConsumer, Matrix4f matrix, float z, float x1, float y1, float x2,
                             float y2) {
        vertexConsumer.vertex(matrix, x1, y1, z).next();
        vertexConsumer.vertex(matrix, x2, y1, z).next();
        vertexConsumer.vertex(matrix, x2, y1, z).next();
        vertexConsumer.vertex(matrix, x2, y2, z).next();
        vertexConsumer.vertex(matrix, x2, y2, z).next();
        vertexConsumer.vertex(matrix, x1, y2, z).next();
        vertexConsumer.vertex(matrix, x1, y2, z).next();
        vertexConsumer.vertex(matrix, x1, y1, z).next();
    }

    public static void block(VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2) {
        xRect(vertexConsumer, x1, y1, z1, y2, z2);
        xRect(vertexConsumer, x2, y1, z1, y2, z2);

        yRect(vertexConsumer, y1, x1, z1, x2, z2);
        yRect(vertexConsumer, y2, x1, z1, x2, z2);

        zRect(vertexConsumer, z1, x1, y1, x2, y2);
        zRect(vertexConsumer, z2, x1, y1, x2, y2);
    }

    public static void block(VertexConsumer vertexConsumer, double x1, double y1, double z1,
                             double x2, double y2, double z2) {
        block(vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
    }

    public static void block(VertexConsumer vertexConsumer, Box box) {
        block(vertexConsumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public static void block(VertexConsumer vertexConsumer, Matrix4f matrix, float x1, float y1, float z1, float x2,
                             float y2, float z2) {
        xRect(vertexConsumer, matrix, x1, y1, z1, y2, z2);
        xRect(vertexConsumer, matrix, x2, y1, z1, y2, z2);

        yRect(vertexConsumer, matrix, y1, x1, z1, x2, z2);
        yRect(vertexConsumer, matrix, y2, x1, z1, x2, z2);

        zRect(vertexConsumer, matrix, z1, x1, y1, x2, y2);
        zRect(vertexConsumer, matrix, z2, x1, y1, x2, y2);
    }

    public static void block(VertexConsumer vertexConsumer, Matrix4f matrix, double x1, double y1, double z1,
                             double x2, double y2, double z2) {
        block(vertexConsumer, matrix, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
    }

    public static void block(VertexConsumer vertexConsumer, Matrix4f matrix, Box box) {
        block(vertexConsumer, matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    /**
     * consumes vertexes for LINES for outlining the given box
     *
     * @param vertexConsumer
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     */
    public static void blockOutline(VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2,
                                    float z2) {
        zRectLines(vertexConsumer, z1, x1, y1, x2, y2);
        zRectLines(vertexConsumer, z2, x1, y1, x2, y2);

        vertexConsumer.vertex(x1, y1, z1).next();
        vertexConsumer.vertex(x1, y1, z2).next();

        vertexConsumer.vertex(x2, y1, z1).next();
        vertexConsumer.vertex(x2, y1, z2).next();

        vertexConsumer.vertex(x2, y2, z1).next();
        vertexConsumer.vertex(x2, y2, z2).next();

        vertexConsumer.vertex(x1, y2, z1).next();
        vertexConsumer.vertex(x1, y2, z2).next();
    }

    public static void blockOutline(VertexConsumer vertexConsumer, double x1, double y1, double z1, double x2, double y2,
                                    double z2) {
        blockOutline(vertexConsumer, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
    }

    public static void blockOutline(VertexConsumer vertexConsumer, Box box) {
        blockOutline(vertexConsumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public static void blockOutline(VertexConsumer vertexConsumer, Matrix4f matrix, float x1, float y1, float z1,
                                    float x2, float y2, float z2) {
        zRectLines(vertexConsumer, matrix, z1, x1, y1, x2, y2);
        zRectLines(vertexConsumer, matrix, z2, x1, y1, x2, y2);

        vertexConsumer.vertex(matrix, x1, y1, z1).next();
        vertexConsumer.vertex(matrix, x1, y1, z2).next();

        vertexConsumer.vertex(matrix, x2, y1, z1).next();
        vertexConsumer.vertex(matrix, x2, y1, z2).next();

        vertexConsumer.vertex(matrix, x2, y2, z1).next();
        vertexConsumer.vertex(matrix, x2, y2, z2).next();

        vertexConsumer.vertex(matrix, x1, y2, z1).next();
        vertexConsumer.vertex(matrix, x1, y2, z2).next();
    }

    public static void blockOutline(VertexConsumer vertexConsumer, Matrix4f matrix, double x1, double y1, double z1,
                                    double x2, double y2, double z2) {
        blockOutline(vertexConsumer, matrix, (float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
    }

    public static void blockOutline(VertexConsumer vertexConsumer, Matrix4f matrix, Box box) {
        blockOutline(vertexConsumer, matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public static void drawWrapped(DrawContext context, TextRenderer tr, List<Text> texts, int x, int y,
                                   int width, int color) {
        for (Text text : texts) {
            for (OrderedText orderedText : tr.wrapLines(text, width)) {
                context.drawText(tr, orderedText, x, y, color, false);
                y += tr.fontHeight;
            }
        }
    }

    /**
     * Use RENDER_THREAD_EXECUTOR.getThread() to get the thread if needed.
     * @return
     */
    private static Thread getRenderThread() {
        if (RenderSystem.isOnRenderThread()) {
            return Thread.currentThread();
        }

        try {
            Field renderThreadField = RenderSystem.class.getDeclaredField("renderThread");
            Thread renderThread;

            renderThreadField.setAccessible(true);

            while ((renderThread = (Thread) renderThreadField.get(null)) == null) {
                Thread.yield();
            }

            return renderThread;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final class RenderThreadExecutor extends ReentrantThreadExecutor<Runnable> {
        private final Thread thread = getRenderThread();

        public RenderThreadExecutor() {
            super("render-executor");
        }

        @Override
        public Thread getThread() {
            return thread;
        }

        @Override
        protected Runnable createTask(Runnable runnable) {
            return runnable;
        }

        @Override
        public void send(Runnable runnable) {
            super.send(runnable);
            RenderSystem.recordRenderCall(this::runTasks);
        }
    }
}
