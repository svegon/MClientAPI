package io.github.svegon.mclientapi.client.util

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.VertexConsumer
import net.minecraft.text.Text
import net.minecraft.util.math.Box
import net.minecraft.util.math.ColorHelper
import org.joml.Matrix4f
import java.awt.Rectangle
import java.lang.reflect.Field

object RenderUtil {
    fun color4FToI(a: FloatArray): Int {
        return (a[0] * 255f).toInt() shl 24 or ((a[1] * 255f).toInt() shl 16) or ((a[2] * 255f).toInt() shl 8
        ) or ((a[3] * 255f).toInt())
    }

    fun colorITo4F(color: Int): FloatArray {
        return floatArrayOf(
            (color shr 24) / 255f, ((color shr 16) and 255) / 255f, ((color shr 8) and 255) / 255f,
            (color and 255) / 255f
        )
    }

    fun setShaderColor(vararg components: Float) {
        if (components.size < 4) {
            RenderSystem.setShaderColor(components[0], components[1], components[2], 1f)
        } else {
            RenderSystem.setShaderColor(components[0], components[1], components[2], components[3])
        }
    }

    fun setShaderColor(red: Int, green: Int, blue: Int, alpha: Int) {
        RenderSystem.setShaderColor(red / 255f, green / 255f, blue / 255f, alpha / 255f)
    }

    fun setShaderColor(color: Int) {
        setShaderColor(
            ColorHelper.Argb.getRed(color), ColorHelper.Argb.getGreen(color),
            ColorHelper.Argb.getBlue(color), ColorHelper.Argb.getAlpha(color)
        )
    }

    fun setShaderRed(red: Float) {
        RenderSystem.getShaderColor()[0] = red
    }

    fun setShaderGreen(green: Float) {
        RenderSystem.getShaderColor()[1] = green
    }

    fun setShaderBlue(blue: Float) {
        RenderSystem.getShaderColor()[2] = blue
    }

    fun setShaderAlpha(alpha: Float) {
        RenderSystem.getShaderColor()[3] = alpha
    }

    fun setShaderRed(red: Int) {
        setShaderRed(red / 255f)
    }

    fun setShaderGreen(green: Int) {
        setShaderGreen(green / 255f)
    }

    fun setShaderBlue(blue: Int) {
        setShaderBlue(blue / 255f)
    }

    fun setShaderAlpha(alpha: Int) {
        setShaderAlpha(alpha / 255f)
    }

    fun resetShaderColor() {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    }

    fun rect(vertexConsumer: VertexConsumer, x1: Float, y1: Float, x2: Float, y2: Float) {
        zRect(vertexConsumer, 0f, x1, y1, x2, y2)
    }

    fun rect(vertexConsumer: VertexConsumer, matrix: Matrix4f, x1: Float, y1: Float, x2: Float, y2: Float) {
        zRect(vertexConsumer, matrix, 0f, x1, y1, x2, y2)
    }

    fun rect(vertexConsumer: VertexConsumer, rect: Rectangle) {
        rect(
            vertexConsumer,
            rect.x.toFloat(),
            rect.y.toFloat(),
            (rect.x + rect.width).toFloat(),
            (rect.y - rect.height).toFloat()
        )
    }

    fun rect(vertexConsumer: VertexConsumer, matrix: Matrix4f, rect: Rectangle) {
        rect(
            vertexConsumer,
            matrix,
            rect.x.toFloat(),
            rect.y.toFloat(),
            (rect.x + rect.width).toFloat(),
            (rect.y - rect.height).toFloat()
        )
    }

    fun xRect(vertexConsumer: VertexConsumer, x: Float, y1: Float, z1: Float, y2: Float, z2: Float) {
        vertexConsumer.vertex(x, y1, z1)
        vertexConsumer.vertex(x, y1, z2)
        vertexConsumer.vertex(x, y2, z2)
        vertexConsumer.vertex(x, y2, z1)
    }

    fun xRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x: Float, y1: Float, z1: Float, y2: Float,
        z2: Float
    ) {
        vertexConsumer.vertex(matrix, x, y1, z1)
        vertexConsumer.vertex(matrix, x, y1, z2)
        vertexConsumer.vertex(matrix, x, y2, z2)
        vertexConsumer.vertex(matrix, x, y2, z1)
    }

    fun xRect(vertexConsumer: VertexConsumer, x: Double, y1: Double, z1: Double, y2: Double, z2: Double) {
        xRect(vertexConsumer, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun xRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x: Double, y1: Double, z1: Double, y2: Double,
        z2: Double
    ) {
        xRect(vertexConsumer, matrix, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun yRect(vertexConsumer: VertexConsumer, y: Float, x1: Float, z1: Float, x2: Float, z2: Float) {
        vertexConsumer.vertex(x1, y, z1)
        vertexConsumer.vertex(x1, y, z2)
        vertexConsumer.vertex(x2, y, z2)
        vertexConsumer.vertex(x2, y, z1)
    }

    fun yRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, y: Float, x1: Float, z1: Float, x2: Float,
        z2: Float
    ) {
        vertexConsumer.vertex(matrix, x1, y, z1)
        vertexConsumer.vertex(matrix, x1, y, z2)
        vertexConsumer.vertex(matrix, x2, y, z2)
        vertexConsumer.vertex(matrix, x2, y, z1)
    }

    fun yRect(vertexConsumer: VertexConsumer, x: Double, y1: Double, z1: Double, y2: Double, z2: Double) {
        yRect(vertexConsumer, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun yRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x: Double, y1: Double, z1: Double, y2: Double,
        z2: Double
    ) {
        yRect(vertexConsumer, matrix, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun zRect(vertexConsumer: VertexConsumer, z: Float, x1: Float, y1: Float, x2: Float, y2: Float) {
        vertexConsumer.vertex(x1, y1, z)
        vertexConsumer.vertex(x1, y2, z)
        vertexConsumer.vertex(x2, y2, z)
        vertexConsumer.vertex(x2, y1, z)
    }

    fun zRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, z: Float, x1: Float, y1: Float, x2: Float,
        y2: Float
    ) {
        vertexConsumer.vertex(matrix, x1, y1, z)
        vertexConsumer.vertex(matrix, x1, y2, z)
        vertexConsumer.vertex(matrix, x2, y2, z)
        vertexConsumer.vertex(matrix, x2, y1, z)
    }

    fun zRect(vertexConsumer: VertexConsumer, x: Double, y1: Double, z1: Double, y2: Double, z2: Double) {
        zRect(vertexConsumer, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun zRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x: Double, y1: Double, z1: Double, y2: Double,
        z2: Double
    ) {
        zRect(vertexConsumer, matrix, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun zRectLines(vertexConsumer: VertexConsumer, z: Float, x1: Float, y1: Float, x2: Float, y2: Float) {
        vertexConsumer.vertex(x1, y1, z)
        vertexConsumer.vertex(x2, y1, z)
        vertexConsumer.vertex(x2, y1, z)
        vertexConsumer.vertex(x2, y2, z)
        vertexConsumer.vertex(x2, y2, z)
        vertexConsumer.vertex(x1, y2, z)
        vertexConsumer.vertex(x1, y2, z)
        vertexConsumer.vertex(x1, y1, z)
    }

    fun zRectLines(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, z: Float, x1: Float, y1: Float, x2: Float,
        y2: Float
    ) {
        vertexConsumer.vertex(matrix, x1, y1, z)
        vertexConsumer.vertex(matrix, x2, y1, z)
        vertexConsumer.vertex(matrix, x2, y1, z)
        vertexConsumer.vertex(matrix, x2, y2, z)
        vertexConsumer.vertex(matrix, x2, y2, z)
        vertexConsumer.vertex(matrix, x1, y2, z)
        vertexConsumer.vertex(matrix, x1, y2, z)
        vertexConsumer.vertex(matrix, x1, y1, z)
    }

    fun block(vertexConsumer: VertexConsumer, x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float) {
        xRect(vertexConsumer, x1, y1, z1, y2, z2)
        xRect(vertexConsumer, x2, y1, z1, y2, z2)

        yRect(vertexConsumer, y1, x1, z1, x2, z2)
        yRect(vertexConsumer, y2, x1, z1, x2, z2)

        zRect(vertexConsumer, z1, x1, y1, x2, y2)
        zRect(vertexConsumer, z2, x1, y1, x2, y2)
    }

    fun block(
        vertexConsumer: VertexConsumer, x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double
    ) {
        block(vertexConsumer, x1.toFloat(), y1.toFloat(), z1.toFloat(), x2.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun block(vertexConsumer: VertexConsumer, box: Box) {
        block(vertexConsumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    fun block(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x1: Float, y1: Float, z1: Float, x2: Float,
        y2: Float, z2: Float
    ) {
        xRect(vertexConsumer, matrix, x1, y1, z1, y2, z2)
        xRect(vertexConsumer, matrix, x2, y1, z1, y2, z2)

        yRect(vertexConsumer, matrix, y1, x1, z1, x2, z2)
        yRect(vertexConsumer, matrix, y2, x1, z1, x2, z2)

        zRect(vertexConsumer, matrix, z1, x1, y1, x2, y2)
        zRect(vertexConsumer, matrix, z2, x1, y1, x2, y2)
    }

    fun block(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double
    ) {
        block(
            vertexConsumer,
            matrix,
            x1.toFloat(),
            y1.toFloat(),
            z1.toFloat(),
            x2.toFloat(),
            y2.toFloat(),
            z2.toFloat()
        )
    }

    fun block(vertexConsumer: VertexConsumer, matrix: Matrix4f?, box: Box) {
        block(vertexConsumer, matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
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
    fun blockOutline(
        vertexConsumer: VertexConsumer, x1: Float, y1: Float, z1: Float, x2: Float, y2: Float,
        z2: Float
    ) {
        zRectLines(vertexConsumer, z1, x1, y1, x2, y2)
        zRectLines(vertexConsumer, z2, x1, y1, x2, y2)

        vertexConsumer.vertex(x1, y1, z1)
        vertexConsumer.vertex(x1, y1, z2)

        vertexConsumer.vertex(x2, y1, z1)
        vertexConsumer.vertex(x2, y1, z2)

        vertexConsumer.vertex(x2, y2, z1)
        vertexConsumer.vertex(x2, y2, z2)

        vertexConsumer.vertex(x1, y2, z1)
        vertexConsumer.vertex(x1, y2, z2)
    }

    fun blockOutline(
        vertexConsumer: VertexConsumer, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double,
        z2: Double
    ) {
        blockOutline(
            vertexConsumer,
            x1.toFloat(),
            y1.toFloat(),
            z1.toFloat(),
            x2.toFloat(),
            y2.toFloat(),
            z2.toFloat()
        )
    }

    fun blockOutline(vertexConsumer: VertexConsumer, box: Box) {
        blockOutline(vertexConsumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    fun blockOutline(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float
    ) {
        zRectLines(vertexConsumer, matrix, z1, x1, y1, x2, y2)
        zRectLines(vertexConsumer, matrix, z2, x1, y1, x2, y2)

        vertexConsumer.vertex(matrix, x1, y1, z1)
        vertexConsumer.vertex(matrix, x1, y1, z2)

        vertexConsumer.vertex(matrix, x2, y1, z1)
        vertexConsumer.vertex(matrix, x2, y1, z2)

        vertexConsumer.vertex(matrix, x2, y2, z1)
        vertexConsumer.vertex(matrix, x2, y2, z2)

        vertexConsumer.vertex(matrix, x1, y2, z1)
        vertexConsumer.vertex(matrix, x1, y2, z2)
    }

    fun blockOutline(
        vertexConsumer: VertexConsumer, matrix: Matrix4f?, x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double
    ) {
        blockOutline(
            vertexConsumer,
            matrix,
            x1.toFloat(),
            y1.toFloat(),
            z1.toFloat(),
            x2.toFloat(),
            y2.toFloat(),
            z2.toFloat()
        )
    }

    fun blockOutline(vertexConsumer: VertexConsumer, matrix: Matrix4f?, box: Box) {
        blockOutline(vertexConsumer, matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    fun drawWrapped(
        context: DrawContext, tr: TextRenderer, texts: List<Text?>, x: Int, y: Int,
        width: Int, color: Int
    ) {
        var y = y
        for (text in texts) {
            for (orderedText in tr.wrapLines(text, width)) {
                context.drawText(tr, orderedText, x, y, color, false)
                y += tr.fontHeight
            }
        }
    }

    private val renderThread: Thread
        /**
         * Use RENDER_THREAD_EXECUTOR.getThread() to get the thread if needed.
         * @return
         */
        get() {
            if (RenderSystem.isOnRenderThread()) {
                return Thread.currentThread()
            }

            try {
                val renderThreadField: Field = RenderSystem::class.java.getDeclaredField("renderThread")
                var renderThread: Thread

                renderThreadField.isAccessible = true

                while (((renderThreadField[null] as Thread).also { renderThread = it }) == null) {
                    Thread.yield()
                }

                return renderThread
            } catch (e: NoSuchFieldException) {
                throw IllegalStateException(e)
            } catch (e: IllegalAccessException) {
                throw IllegalStateException(e)
            }
        }
}
