package io.github.svegon.capi.block_search

import com.google.common.collect.Lists
import io.github.svegon.capi.block_search.BlockSearchManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class ChunkSearcher(manager: BlockSearchManager, private val chunk: ChunkPos) {
    private val intermediateResult: MutableCollection<BlockPos> = Lists.newLinkedList()
    private val manager: BlockSearchManager = manager
    private var status = Status.IDLE
    private var future: Future<*>? = null

    fun startSearching(pool: ExecutorService) {
        check(status == Status.IDLE)

        status = Status.SEARCHING
        future = pool.submit { this.searchNow() }
    }

    private fun searchNow() {
        val status0 = status

        if (status0 == Status.INTERRUPTED) {
            return
        }

        check(status0 == Status.SEARCHING)

        // clear out chunk
        manager.results.removeIf { pos -> pos.getX() shr 4 === chunk.x && pos.getZ() shr 4 === chunk.z }

        BlockSearchManager.search(
            BlockPos(chunk.startX, manager.getMinHeight(), chunk.startZ),
            BlockPos(chunk.endX, manager.getMaxHeight(), chunk.endZ), manager.getBlockCondition(),
            { pos -> status == Status.INTERRUPTED || manager.limitReached() },
            intermediateResult
        )

        if (status != Status.INTERRUPTED) {
            status = Status.DONE
            manager.results.addAll(intermediateResult)
        }
    }

    fun cancelSearching() {
        Thread(
            { this.cancelNow() }, "ChunkSearcher-canceller-#"
                    + BlockSearchManager.UNIQUE_ID_SUPPLIER.getAndIncrement()
        ).start()
    }

    private fun cancelNow() {
        if (future != null) {
            try {
                status = Status.INTERRUPTED
                future!!.get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }

        status = Status.IDLE
    }

    fun terminated(): Boolean {
        return status == Status.INTERRUPTED || status == Status.DONE
    }

    enum class Status {
        IDLE,
        SEARCHING,
        INTERRUPTED,
        DONE
    }
}
