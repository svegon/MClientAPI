package io.github.svegon.mclientapi.client.util

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import it.unimi.dsi.fastutil.objects.ObjectArrays
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.OrderedSubmitNodeCollector
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.phys.AABB
import org.joml.Matrix4f
import org.joml.Matrix4fc
import java.awt.Rectangle
import java.lang.reflect.Field

object RenderUtil {
    val ADD = Component.translatable("gui.add")
    val CONFIRM_QUESTION = Component.translatable("gui.confirmMedium")
    val REMOVE = Component.translatable("gui.remove")

    fun parseMultilineTranslation(keyStart: String): List<Component> {
        return parseMultilineTranslation(keyStart, *ObjectArrays.EMPTY_ARRAY)
    }

    fun parseMultilineTranslation(keyStart: String, vararg args: Any): List<Component> {
        val translation: MutableList<Component> = ArrayList()

        var i = 0
        while (true) {
            val key = keyStart + i

            if (!I18n.exists(key)) {
                break
            }

            translation.add(Component.translatable(key, *args))
            ++i
        }

        return translation
    }

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
        vertexConsumer.addVertex(x, y1, z1)
        vertexConsumer.addVertex(x, y1, z2)
        vertexConsumer.addVertex(x, y2, z2)
        vertexConsumer.addVertex(x, y2, z1)
    }

    fun xRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x: Float, y1: Float, z1: Float, y2: Float,
        z2: Float
    ) {
        vertexConsumer.addVertex(matrix, x, y1, z1)
        vertexConsumer.addVertex(matrix, x, y1, z2)
        vertexConsumer.addVertex(matrix, x, y2, z2)
        vertexConsumer.addVertex(matrix, x, y2, z1)
    }

    fun xRect(vertexConsumer: VertexConsumer, x: Double, y1: Double, z1: Double, y2: Double, z2: Double) {
        xRect(vertexConsumer, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun xRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x: Double, y1: Double, z1: Double, y2: Double,
        z2: Double
    ) {
        xRect(vertexConsumer, matrix, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun yRect(vertexConsumer: VertexConsumer, y: Float, x1: Float, z1: Float, x2: Float, z2: Float) {
        vertexConsumer.addVertex(x1, y, z1)
        vertexConsumer.addVertex(x1, y, z2)
        vertexConsumer.addVertex(x2, y, z2)
        vertexConsumer.addVertex(x2, y, z1)
    }

    fun yRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, y: Float, x1: Float, z1: Float, x2: Float,
        z2: Float
    ) {
        vertexConsumer.addVertex(matrix, x1, y, z1)
        vertexConsumer.addVertex(matrix, x1, y, z2)
        vertexConsumer.addVertex(matrix, x2, y, z2)
        vertexConsumer.addVertex(matrix, x2, y, z1)
    }

    fun yRect(vertexConsumer: VertexConsumer, x: Double, y1: Double, z1: Double, y2: Double, z2: Double) {
        yRect(vertexConsumer, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun yRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x: Double, y1: Double, z1: Double, y2: Double,
        z2: Double
    ) {
        yRect(vertexConsumer, matrix, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun zRect(vertexConsumer: VertexConsumer, z: Float, x1: Float, y1: Float, x2: Float, y2: Float) {
        vertexConsumer.addVertex(x1, y1, z)
        vertexConsumer.addVertex(x1, y2, z)
        vertexConsumer.addVertex(x2, y2, z)
        vertexConsumer.addVertex(x2, y1, z)
    }

    fun zRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, z: Float, x1: Float, y1: Float, x2: Float,
        y2: Float
    ) {
        vertexConsumer.addVertex(matrix, x1, y1, z)
        vertexConsumer.addVertex(matrix, x1, y2, z)
        vertexConsumer.addVertex(matrix, x2, y2, z)
        vertexConsumer.addVertex(matrix, x2, y1, z)
    }

    fun zRect(vertexConsumer: VertexConsumer, x: Double, y1: Double, z1: Double, y2: Double, z2: Double) {
        zRect(vertexConsumer, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun zRect(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x: Double, y1: Double, z1: Double, y2: Double,
        z2: Double
    ) {
        zRect(vertexConsumer, matrix, x.toFloat(), y1.toFloat(), z1.toFloat(), y2.toFloat(), z2.toFloat())
    }

    fun zRectLines(vertexConsumer: VertexConsumer, z: Float, x1: Float, y1: Float, x2: Float, y2: Float) {
        vertexConsumer.addVertex(x1, y1, z)
        vertexConsumer.addVertex(x2, y1, z)
        vertexConsumer.addVertex(x2, y1, z)
        vertexConsumer.addVertex(x2, y2, z)
        vertexConsumer.addVertex(x2, y2, z)
        vertexConsumer.addVertex(x1, y2, z)
        vertexConsumer.addVertex(x1, y2, z)
        vertexConsumer.addVertex(x1, y1, z)
    }

    fun zRectLines(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, z: Float, x1: Float, y1: Float, x2: Float,
        y2: Float
    ) {
        vertexConsumer.addVertex(matrix, x1, y1, z)
        vertexConsumer.addVertex(matrix, x2, y1, z)
        vertexConsumer.addVertex(matrix, x2, y1, z)
        vertexConsumer.addVertex(matrix, x2, y2, z)
        vertexConsumer.addVertex(matrix, x2, y2, z)
        vertexConsumer.addVertex(matrix, x1, y2, z)
        vertexConsumer.addVertex(matrix, x1, y2, z)
        vertexConsumer.addVertex(matrix, x1, y1, z)
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

    fun block(vertexConsumer: VertexConsumer, box: AABB) {
        block(vertexConsumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    fun block(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x1: Float, y1: Float, z1: Float, x2: Float,
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
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x1: Double, y1: Double, z1: Double,
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

    fun block(vertexConsumer: VertexConsumer, matrix: Matrix4fc, box: AABB) {
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

        vertexConsumer.addVertex(x1, y1, z1)
        vertexConsumer.addVertex(x1, y1, z2)

        vertexConsumer.addVertex(x2, y1, z1)
        vertexConsumer.addVertex(x2, y1, z2)

        vertexConsumer.addVertex(x2, y2, z1)
        vertexConsumer.addVertex(x2, y2, z2)

        vertexConsumer.addVertex(x1, y2, z1)
        vertexConsumer.addVertex(x1, y2, z2)
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

    fun blockOutline(vertexConsumer: VertexConsumer, box: AABB) {
        blockOutline(vertexConsumer, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ)
    }

    fun blockOutline(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x1: Float, y1: Float, z1: Float,
        x2: Float, y2: Float, z2: Float
    ) {
        zRectLines(vertexConsumer, matrix, z1, x1, y1, x2, y2)
        zRectLines(vertexConsumer, matrix, z2, x1, y1, x2, y2)

        vertexConsumer.addVertex(matrix, x1, y1, z1)
        vertexConsumer.addVertex(matrix, x1, y1, z2)

        vertexConsumer.addVertex(matrix, x2, y1, z1)
        vertexConsumer.addVertex(matrix, x2, y1, z2)

        vertexConsumer.addVertex(matrix, x2, y2, z1)
        vertexConsumer.addVertex(matrix, x2, y2, z2)

        vertexConsumer.addVertex(matrix, x1, y2, z1)
        vertexConsumer.addVertex(matrix, x1, y2, z2)
    }

    fun blockOutline(
        vertexConsumer: VertexConsumer, matrix: Matrix4fc, x1: Double, y1: Double, z1: Double,
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

    fun blockOutline(vertexConsumer: VertexConsumer, matrix: Matrix4fc, box: AABB) {
        blockOutline(vertexConsumer, matrix, box.minX, box.minY, box.minZ, box.maxX, box.maxY,
            box.maxZ)
    }

    fun drawWrapped(
        context: OrderedSubmitNodeCollector, matrices: PoseStack, font: Font, texts: List<FormattedText>,
        startX: Float, startY: Float, width: Int, color: Int
    ) {
        var x = startX
        var y = startY

        for (text in texts) {
            for (line in font.split(text, width)) {
                context.submitText(matrices, x, y, line, true,
                    Font.DisplayMode.NORMAL, 0, color, 0, 0)
                y += font.lineHeight
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
