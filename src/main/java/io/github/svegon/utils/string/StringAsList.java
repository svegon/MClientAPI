package io.github.svegon.utils.string;

import io.github.svegon.utils.fast.util.chars.ImprovedCharList;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.NotNull;

import java.util.RandomAccess;

@Immutable
public final class StringAsList extends ImprovedCharList implements RandomAccess {
    private final String string;
    private boolean initHash = true;
    private int hash;

    StringAsList(final String string) {
        this.string = string;
    }

    @Override
    public char getChar(final int index) {
        return string.charAt(index);
    }

    @Override
    public int size() {
        return string.length();
    }

    @Override
    public @NotNull StringAsList subList(final int from, final int to) {
        return new StringAsList(string.substring(from, to));
    }

    @Override
    public int hashCode() {
        if (initHash) {
            hash = super.hashCode();
            initHash = false;
        }

        return hash;
    }

    @Override
    public void getElements(final int from, final char[] a, final int offset, final int length) {
        string.getChars(from, from + length, a, offset);
    }

    @Override
    public @NotNull String toString() {
        return string;
    }
}
