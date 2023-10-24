package com.github.svegon.utils.math;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.chars.CharPredicate;

import java.security.SecureRandom;
import java.util.List;

public class ExtendedRandom extends SecureRandom {
    public char nextChar() {
        return (char) next(Character.SIZE);
    }

    public char nextChar(char maxCodePoint) {
        return (char) (next(Character.SIZE) % maxCodePoint);
    }

    public String nextString(final int length) {
        char[] charArray = new char[length];

        for (int i = 0; i < length; ++i) {
            charArray[i] = nextChar();
        }

        return new String(charArray);
    }

    public String nextString(final int length, final CharPredicate filter) {
        Preconditions.checkNotNull(filter);
        char[] charArray = new char[length];

        for (int i = 0; i < length; ++i) {
            char chr = nextChar();

            while (!filter.test(chr)) {
                chr = nextChar();
            }

            charArray[i] = chr;
        }

        return new String(charArray);
    }

    public boolean nextBoolean(int in) {
        return nextInt(in) == 0;
    }

    public boolean nextBoolean(double chance) {
        return nextGaussian() < chance;
    }

    public <E> E randElement(List<E> range) {
        return range.get(nextInt(range.size()));
    }

    public <E> E randElement(E[] array) {
        return array[nextInt(array.length)];
    }

    static {
        System.setProperty("java.util.secureRandomSeed", "true");
    }
}
