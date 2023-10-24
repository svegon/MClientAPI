package com.github.svegon.utils.fast.util.chars.transform.chars;

import com.github.svegon.utils.fast.util.objects.transform.objects.L2LTransformingSet;
import com.github.svegon.utils.fast.util.chars.transform.TransformingChar2CharMap;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.chars.Char2CharMap;
import it.unimi.dsi.fastutil.chars.CharUnaryOperator;

import java.util.Set;

public class C2CKC2CVTranformingMap extends TransformingChar2CharMap<Character, Character, Char2CharMap> {
    private final CharUnaryOperator forwardingKeyTransformer;
    private final CharUnaryOperator backingKeyTransformer;
    private final CharUnaryOperator forwardingValueTransformer;
    private final CharUnaryOperator backingValueTransformer;
    private final ObjectSet<Entry> entrySet;

    @SuppressWarnings("unchecked")
    public C2CKC2CVTranformingMap(Char2CharMap map,
                                  CharUnaryOperator forwardingKeyTransformer,
                                  CharUnaryOperator backingKeyTransformer,
                                  CharUnaryOperator forwardingValueTransformer,
                                  CharUnaryOperator backingValueTransformer) {
        super(map);
        this.forwardingKeyTransformer = Preconditions.checkNotNull(forwardingKeyTransformer);
        this.backingKeyTransformer = Preconditions.checkNotNull(backingKeyTransformer);
        this.forwardingValueTransformer = Preconditions.checkNotNull(forwardingValueTransformer);
        this.backingValueTransformer = Preconditions.checkNotNull(backingValueTransformer);
        this.entrySet = (ObjectSet) new EntrySet(map.char2CharEntrySet(), forwardingKeyTransformer,
                forwardingValueTransformer, backingValueTransformer);
    }

    @Override
    public final ObjectSet<Entry> char2CharEntrySet() {
        return entrySet;
    }

    @Override
    public char put(char key, char value) {
        return forwardingValueTransformer.apply(m.put(backingKeyTransformer.apply(key),
                backingValueTransformer.apply(value)));
    }

    @Override
    public char remove(char key) {
        return forwardingValueTransformer.apply(m.remove(backingKeyTransformer.apply(key)));
    }

    @Override
    public char get(char key) {
        return forwardingValueTransformer.apply(m.get(backingKeyTransformer.apply(key)));
    }

    @Override
    public char defaultReturnValue() {
        return forwardingValueTransformer.apply(m.defaultReturnValue());
    }

    @Override
    public void defaultReturnValue(char rv) {
        m.defaultReturnValue(backingValueTransformer.apply(rv));
    }

    public CharUnaryOperator getForwardingKeyTransformer() {
        return forwardingKeyTransformer;
    }

    public CharUnaryOperator getBackingKeyTransformer() {
        return backingKeyTransformer;
    }

    public CharUnaryOperator getForwardingValueTransformer() {
        return forwardingValueTransformer;
    }

    public CharUnaryOperator getBackingValueTransformer() {
        return backingValueTransformer;
    }

    private static class EntrySet extends L2LTransformingSet<Entry, S2SKS2SVTransformingEntry> {
        public EntrySet(final Set<Entry> set,
                        final CharUnaryOperator forwardingKeyTransformer,
                        final CharUnaryOperator forwardingValueTransformer,
                        final CharUnaryOperator backingValueTransformer) {
            super(set, e -> new S2SKS2SVTransformingEntry(e, forwardingKeyTransformer, forwardingValueTransformer,
                    backingValueTransformer), e -> e.e);
        }
    }

    private static class S2SKS2SVTransformingEntry extends TransformingEntry<Character, Character, Entry> {
        private final CharUnaryOperator forwardingKeyTransformer;
        private final CharUnaryOperator forwardingValueTransformer;
        private final CharUnaryOperator backingValueTransformer;

        public S2SKS2SVTransformingEntry(final Entry e,
                                         final CharUnaryOperator forwardingKeyTransformer,
                                         final CharUnaryOperator forwardingValueTransformer,
                                         final CharUnaryOperator backingValueTransformer) {
            // no checks necessary
            super(e);
            this.forwardingKeyTransformer = forwardingKeyTransformer;
            this.forwardingValueTransformer = forwardingValueTransformer;
            this.backingValueTransformer = backingValueTransformer;
        }

        @Override
        public char getCharKey() {
            return forwardingKeyTransformer.apply(e.getCharKey());
        }

        @Override
        public char getCharValue() {
            return forwardingValueTransformer.apply(e.getCharKey());
        }

        @Override
        public char setValue(char value) {
            return forwardingValueTransformer.apply(e.setValue(backingValueTransformer.apply(value)));
        }
    }
}
