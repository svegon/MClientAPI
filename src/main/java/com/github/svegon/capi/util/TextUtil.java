package com.github.svegon.capi.util;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public final class TextUtil {
	private TextUtil() {
		throw new UnsupportedOperationException();
	}

	public static final Text ADD = Text.translatable("gui.add");
	public static final Text CONFIRM_QUESTION = Text.translatable("gui.confirmMedium");
	public static final Text REMOVE = Text.translatable("gui.remove");

	public static List<Text> parseMultilineTranslation(String keyStart) {
		return parseMultilineTranslation(keyStart, ObjectArrays.EMPTY_ARRAY);
	}

	public static List<Text> parseMultilineTranslation(String keyStart, Object... args) {
		List<Text> translation = new ArrayList<>();
		String key;

		for (int i = 0; I18n.hasTranslation(key = keyStart + i); ++i) {
			translation.add(Text.translatable(key, args));
		}

		return translation;
	}
}
