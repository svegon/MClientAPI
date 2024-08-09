package io.github.svegon.utils.fast.util.bytes.transform.chars;

import io.github.svegon.utils.fast.util.bytes.transform.TransformingByteSequentialList;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.chars.Char2ByteFunction;
import it.unimi.dsi.fastutil.chars.CharList;
import org.jetbrains.annotations.NotNull;

public class C2BTransformingList extends TransformingByteSequentialList<Character, CharList> {
    private final Char2ByteFunction transformer;

    public C2BTransformingList(CharList list, Char2ByteFunction transformer) {
        super(list);
        this.transformer = Preconditions.checkNotNull(transformer);
    }

    @Override
    public @NotNull ByteListIterator listIterator(int index) {
        return new C2BTransformingListIterator(list.listIterator(index), transformer);
    }

    @Override
    public boolean removeIf(final BytePredicate filter) {
        Preconditions.checkNotNull(filter);
        return list.removeIf((e) -> filter.test(transformer.get(e)));
    }

    @Override
    public byte removeByte(int index) {
        return transformer.get(list.removeChar(index));
    }
}
