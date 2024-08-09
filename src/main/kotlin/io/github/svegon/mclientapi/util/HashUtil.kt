package io.github.svegon.mclientapi.util

import it.unimi.dsi.fastutil.Hash
import it.unimi.dsi.fastutil.booleans.BooleanHash
import it.unimi.dsi.fastutil.bytes.ByteHash
import it.unimi.dsi.fastutil.chars.CharHash
import it.unimi.dsi.fastutil.doubles.DoubleHash
import it.unimi.dsi.fastutil.floats.FloatHash
import it.unimi.dsi.fastutil.ints.IntHash
import it.unimi.dsi.fastutil.ints.IntIterable
import it.unimi.dsi.fastutil.longs.LongHash
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap
import it.unimi.dsi.fastutil.shorts.ShortHash
import java.lang.ref.WeakReference
import java.util.*
import java.util.function.ToIntFunction

object HashUtil {
    object CaseIgnoringStrategy : Hash.Strategy<String?> {
        private val hashCodeCache: Object2IntOpenCustomHashMap<WeakReference<String>> = Object2IntOpenCustomHashMap(
            object : Hash.Strategy<WeakReference<String>> {
                override fun equals(a: WeakReference<String>, b: WeakReference<String>): Boolean {
                    val first = a.get()?: return false
                    val second = b.get()?: return false
                    return first === second;
                }

                override fun hashCode(o: WeakReference<String>): Int {
                    return System.identityHashCode(o.get())
                }
            }
        )

        override fun hashCode(o: String?): Int {
            return hashCodeCache.computeIfAbsent(WeakReference(o?: return 0),
                ToIntFunction {
                    val str = it.get()
                    val length = str!!.length
                    var h = 0

                    for (i in 0 until length) {
                        h = 31 * h + str[i].uppercaseChar().code
                    }
                    h
                })
        }

        override fun equals(a: String?, b: String?): Boolean {
            return a === b || (a != null && a.equals(b, ignoreCase = true))
        }
    }

    object ArrayRegardingStrategy : Hash.Strategy<Any?> {
        override fun hashCode(o: Any?): Int {
            if (o == null) {
                return 0
            }

            if (!o.javaClass.isArray) {
                return o.hashCode()
            }

            if (o is BooleanArray) {
                return o.contentHashCode()
            }

            if (o is ByteArray) {
                return o.contentHashCode()
            }

            if (o is CharArray) {
                return o.contentHashCode()
            }

            if (o is ShortArray) {
                return o.contentHashCode()
            }

            if (o is IntArray) {
                return o.contentHashCode()
            }

            if (o is LongArray) {
                return o.contentHashCode()
            }

            if (o is FloatArray) {
                return o.contentHashCode()
            }

            if (o is DoubleArray) {
                return o.contentHashCode()
            }

            return (o as Array<*>).contentDeepHashCode()
        }

        override fun equals(a: Any?, b: Any?): Boolean {
            if (a === b) {
                return true
            }

            if (a == null || b == null) {
                return false
            }

            if (!a.javaClass.isArray) {
                return a == b
            }

            if (a is BooleanArray) {
                return b is BooleanArray && a.contentEquals(b)
            }

            if (a is ByteArray) {
                return b is ByteArray && a.contentEquals(b)
            }

            if (a is CharArray) {
                return b is CharArray && a.contentEquals(b)
            }

            if (a is ShortArray) {
                return b is ShortArray && a.contentEquals(b)
            }

            if (a is IntArray) {
                return b is IntArray && a.contentEquals(b)
            }

            if (a is LongArray) {
                return b is LongArray && a.contentEquals(b)
            }

            if (a is FloatArray) {
                return b is FloatArray && a.contentEquals(b)
            }

            if (a is DoubleArray) {
                return b is DoubleArray && a.contentEquals(b)
            }

            return b.javaClass.isArray && !b.javaClass.componentType().isPrimitive
                    && (a as Array<*>).contentDeepEquals(b as Array<*>)
        }
    }

    object IdentityStrategy : Hash.Strategy<Any?> {
        override fun hashCode(o: Any?): Int {
            return System.identityHashCode(o)
        }

        override fun equals(a: Any?, b: Any?): Boolean {
            return a === b
        }
    }

    object DefaultStrategy : Hash.Strategy<Any?> {
        override fun hashCode(o: Any?): Int {
            return Objects.hashCode(o)
        }

        override fun equals(a: Any?, b: Any?): Boolean {
            return a == b
        }
    }

    object DEFAULT_BOOLEAN_STRATEGY : BooleanHash.Strategy {
        override fun hashCode(e: Boolean): Int {
            return java.lang.Boolean.hashCode(e)
        }

        override fun equals(a: Boolean, b: Boolean): Boolean {
            return a == b
        }
    }
    object DEFAULT_BYTE_STRATEGY : ByteHash.Strategy {
        override fun hashCode(e: Byte): Int {
            return java.lang.Byte.hashCode(e)
        }

        override fun equals(a: Byte, b: Byte): Boolean {
            return a == b
        }
    }
    object DefaultShortStrategy : ShortHash.Strategy {
        override fun hashCode(e: Short): Int {
            return java.lang.Short.hashCode(e)
        }

        override fun equals(a: Short, b: Short): Boolean {
            return a == b
        }
    }
    object DefaultIntStrategy : IntHash.Strategy {
        override fun hashCode(e: Int): Int {
            return Integer.hashCode(e)
        }

        override fun equals(a: Int, b: Int): Boolean {
            return a == b
        }
    }
    object DefaultLongStrategy : LongHash.Strategy {
        override fun hashCode(e: Long): Int {
            return java.lang.Long.hashCode(e)
        }

        override fun equals(a: Long, b: Long): Boolean {
            return a == b
        }
    }
    object DefaultCharStrategy : CharHash.Strategy {
        override fun hashCode(e: Char): Int {
            return Character.hashCode(e)
        }

        override fun equals(a: Char, b: Char): Boolean {
            return a == b
        }
    }
    object DefaultFloatStrategy : FloatHash.Strategy {
        override fun hashCode(e: Float): Int {
            return java.lang.Float.hashCode(e)
        }

        override fun equals(a: Float, b: Float): Boolean {
            return java.lang.Float.compare(a, b) == 0
        }
    }
    object DefaultDoubleStrategy : DoubleHash.Strategy {
        override fun hashCode(e: Double): Int {
            return e.hashCode()
        }

        override fun equals(a: Double, b: Double): Boolean {
            return a.compareTo(b) == 0
        }
    }

    /**
     * combines hash codes supplied by the given iterator with regard to their order
     *
     * @param hashIterator the iterator supplying the hash codes
     * @return resulting hash code
     */
    fun hashOrdered(hashIterator: Iterator<Int>): Int {
        var h = 0

        while (hashIterator.hasNext()) {
            h = h * 31 + hashIterator.next()
        }

        return h
    }

    fun hashOrdered(hashCollection: IntIterable): Int {
        return hashOrdered(hashCollection.iterator())
    }

    fun <K> Hash.Strategy<K>.hashOrdered(vararg values: K): Int {
        return hashOrdered(values.map { hashCode(it) }.iterator())
    }

    /**
     * combines hash codes supplied by the given iterator with regard to their order
     *
     * @param hashIterator the iterator supplying the hash codes
     * @return resulting hash code
     */
    fun hashUnordered(hashIterator: Iterator<Int>): Int {
        var h = 0

        while (hashIterator.hasNext()) {
            h = h xor hashIterator.next()
        }

        return h
    }

    fun hashUnordered(hashCollection: IntIterable): Int {
        return hashUnordered(hashCollection.iterator())
    }

    fun <K> Hash.Strategy<K>.hashUnordered(vararg values: K): Int {
        return hashUnordered(values.map { hashCode(it) }.iterator())
    }
}
