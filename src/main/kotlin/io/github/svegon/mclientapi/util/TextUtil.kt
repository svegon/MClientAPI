package io.github.svegon.capi.util

import it.unimi.dsi.fastutil.objects.ObjectArrays
import net.minecraft.client.resource.language.I18n
import net.minecraft.text.Text

class TextUtil private constructor() {
    init {
        throw UnsupportedOperationException()
    }

    companion object {
        val ADD: Text = Text.translatable("gui.add")
        val CONFIRM_QUESTION: Text = Text.translatable("gui.confirmMedium")
        val REMOVE: Text = Text.translatable("gui.remove")

        fun parseMultilineTranslation(keyStart: String): List<Text> {
            return parseMultilineTranslation(keyStart, *ObjectArrays.EMPTY_ARRAY)
        }

        fun parseMultilineTranslation(keyStart: String, vararg args: Any?): List<Text> {
            val translation: MutableList<Text> = ArrayList()
            var key: String?

            var i = 0
            while (I18n.hasTranslation((keyStart + i).also { key = it })) {
                translation.add(Text.translatable(key, *args))
                ++i
            }

            return translation
        }
    }
}
