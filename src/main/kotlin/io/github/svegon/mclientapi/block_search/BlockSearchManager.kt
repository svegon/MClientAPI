package io.github.svegon.mclientapi.block_search

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import java.lang.ref.Cleaner
import java.util.*
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiConsumer
import java.util.function.IntSupplier
import java.util.function.Predicate
import kotlin.math.min

open class BlockSearchManager(
    val blockCondition: Predicate<in BlockPos.Mutable>, private val limit: IntSupplier,
    val minHeight: Int, val maxHeight: Int
) : AutoCloseable {
    val results: MutableSet<BlockPos> = Sets.newConcurrentHashSet()
    private val searchers: MutableMap<ChunkPos, ChunkSearcher> = Maps.newConcurrentMap()
    private val matchingBlocksListeners: MutableSet<BiConsumer<BlockSearchManager, Set<BlockPos>>> =
        Sets.newConcurrentHashSet()
    private val cleanable: Cleaner.Cleanable
    private var pool = standardPool()

    init {
        this.cleanable = CLEANER.register(this, Finalizer(results, searchers, pool))
    }

    constructor(blockCondition: Predicate<in BlockPos.Mutable?>, minHeight: Int, maxHeight: Int) : this(
        blockCondition,
        object : IntSupplier {
            // approximately amount of BlockPos instances we can fit into the memory
            val constant: Int = Runtime.getRuntime().freeMemory().toInt() / 72

            override fun getAsInt(): Int {
                return constant
            }
        },
        minHeight,
        maxHeight
    )

    @Throws(Exception::class)
    override fun close() {
        cleanable.clean()
    }

    fun start() {
        pool = standardPool()
        pool.submit { this.completeSearchListener() }
    }

    fun stop() {
        pool.shutdownNow()
        searchers.clear()
    }

    open fun addChunk(chunk: ChunkPos) {
        val searcher: ChunkSearcher = ChunkSearcher(this, chunk)

        if (pool.isTerminated) {
            searchers.clear()
            start()
        }

        searcher.startSearching(pool)

        searchers.put(chunk, searcher)?.cancelSearching()
    }

    fun addChunks(chunks: Collection<ChunkPos>) {
        chunks.parallelStream().forEach { chunk: ChunkPos -> this.addChunk(chunk) }
    }

    fun addChunks(vararg chunks: ChunkPos) {
        addChunks(listOf(*chunks))
    }

    open fun removeChunk(chunk: ChunkPos) {
        searchers.remove(chunk)?.cancelSearching()
    }

    fun removeChunks(chunks: Collection<ChunkPos>) {
        chunks.parallelStream().forEach { chunk: ChunkPos -> this.removeChunk(chunk) }
    }

    fun removeChunks(vararg chunks: ChunkPos) {
        removeChunks(listOf(*chunks))
    }

    fun addListener(listener: BiConsumer<BlockSearchManager, Set<BlockPos>>): Boolean {
        return matchingBlocksListeners.add(listener)
    }

    fun removeListener(listener: BiConsumer<BlockSearchManager, Set<BlockPos>>): Boolean {
        return matchingBlocksListeners.remove(listener)
    }

    private fun completeSearchListener() {
        if (pool.awaitQuiescence(Long.MAX_VALUE, TimeUnit.DAYS)) {
            for (listener in matchingBlocksListeners) {
                listener.accept(this, Collections.unmodifiableSet(results))
            }
        }
    }

    fun limitReached(): Boolean {
        return results.size >= limit.asInt
    }

    private class Finalizer(
        val results: MutableSet<BlockPos>, val searchers: MutableMap<ChunkPos, ChunkSearcher>,
        val pool: ForkJoinPool
    ) : Runnable {
        override fun run() {
            pool.shutdownNow()
            searchers.clear()
            results.clear()

            val t = Thread({ System.gc() }, "Garbage-collecting-thread-" + UNIQUE_ID_SUPPLIER.get())
            t.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { th: Thread, e: Throwable -> }
            t.priority = Thread.MIN_PRIORITY
            t.isDaemon = true
            t.start()
        }
    }

    companion object {
        val CLEANER: Cleaner = Cleaner.create()
        val UNIQUE_ID_SUPPLIER: AtomicInteger = AtomicInteger()

        fun search(
            from: BlockPos, to: BlockPos, blockCondition: Predicate<in BlockPos.Mutable>,
            stopCondition: Predicate<in BlockPos.Mutable>
        ): List<BlockPos> {
            val list: LinkedList<BlockPos> = Lists.newLinkedList<BlockPos>()

            search(from, to, blockCondition, stopCondition, list)

            return list
        }

        fun search(
            from: BlockPos, to: BlockPos, blockCondition: Predicate<in BlockPos.Mutable>,
            stopCondition: Predicate<in BlockPos.Mutable>, resultSet: MutableCollection<BlockPos>
        ) {
            for (pos in (BlockPos.iterate(from, to) as Iterable<BlockPos.Mutable>)) {
                if (stopCondition.test(pos)) {
                    return
                }

                if (blockCondition.test(pos)) {
                    resultSet.add(pos.toImmutable())
                }
            }
        }

        fun standardPool(): ForkJoinPool {
            return ForkJoinPool(
                min(
                    Runtime.getRuntime().availableProcessors(),
                    0x7fff
                ), { pool: ForkJoinPool? ->
                    val t = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool)
                    t.priority = Thread.MIN_PRIORITY
                    t
                }, { t: Thread, e: Throwable -> e.printStackTrace() }, true, 0,
                0x7fff, 1, null, 20, TimeUnit.MILLISECONDS
            )
        }
    }
}
