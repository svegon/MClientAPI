package io.github.svegon.mclientapi.util.collections.chars

import io.github.svegon.utils.ConditionUtil
import io.github.svegon.utils.collections.ArrayUtil
import io.github.svegon.utils.fast.util.chars.OnNextComputeCharIterator
import io.github.svegon.utils.math.MathUtil
import it.unimi.dsi.fastutil.bytes.ByteArrays
import it.unimi.dsi.fastutil.bytes.ByteListIterator
import it.unimi.dsi.fastutil.chars.*
import it.unimi.dsi.fastutil.chars.CharIterator
import oshi.annotation.concurrent.NotThreadSafe

@NotThreadSafe
class CharCompressedTableSet : AbstractCharSet {
    private var table: ByteArray

    constructor() {
        table = ByteArrays.DEFAULT_EMPTY_ARRAY
    }

    constructor(expectedMaxKey: Char) {
        table = if (expectedMaxKey.code == 0) ByteArrays.EMPTY_ARRAY else ByteArray(
            if ((expectedMaxKey.code ushr 3)
                + (expectedMaxKey.code and 7) != 0
            ) 1 else 0
        )
    }

    constructor(elements: CharIterable) : this(elements.iterator())

    constructor(elements: CharIterator) : this(16.toChar()) {
        while (elements.hasNext()) {
            add(elements.nextChar())
        }
    }

    override fun iterator(): CharIterator {
        return object : OnNextComputeCharIterator() {
            val it = table.asList().listIterator()

            override fun computeNext(): Char {
                var b: Byte

                while (it.hasNext()) {
                    if ((it.next().also { b = it }).toInt() != 0) {
                        return ((it.nextIndex() shl 3) or MathUtil.highestOneBit(b).toInt()).toChar()
                    }
                }

                finish()
                return 0.toChar()
            }
        }
    }

    override val size: Int
        get() = table.asList().parallelStream().mapToInt { i: Byte ->
            Integer.bitCount(i.toInt())
        }.sum()

    override fun add(k: Char): Boolean {
        val index = k.code ushr 3
        val mod = k.code and 7
        table = ByteArrays.ensureCapacity(table, index + 1)
        val prev = table[k.code].toInt()
        val new = table[index].toInt() or (1 shl mod)
        table[index] = new.toByte()
        return prev != new
    }

    override fun contains(k: Char): Boolean {
        val index = k.code ushr 3

        if (index >= table.size) {
            return false
        }

        return ConditionUtil.hasFlag(table[index].toInt(), k.code and 7)
    }

    override fun remove(k: Char): Boolean {
        val index = k.code ushr 3

        if (index >= table.size) {
            return false
        }

        return table[index] != ((1 shl (k.code and 7)).inv().let { table[index] =
            (table[index].toInt() and it).toByte(); table[index] })
    }

    override fun removeAll(c: CharCollection): Boolean {
        var modified = false

        for (s in c) {
            modified = modified or remove(s)
        }

        return modified
    }

    override fun retainAll(c: CharCollection): Boolean {
        return removeIf(CharPredicate { e: Char -> !c.contains(e) })
    }
}
