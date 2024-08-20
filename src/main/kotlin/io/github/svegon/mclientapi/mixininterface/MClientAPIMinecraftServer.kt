package io.github.svegon.mclientapi.mixininterface

import net.minecraft.resource.LifecycledResourceManager
import net.minecraft.server.DataPackContents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.ServerMetadata.Favicon
import net.minecraft.util.math.random.Random
import net.minecraft.util.profiler.Recorder
import net.minecraft.world.PlayerSaveHandler
import net.minecraft.world.level.storage.LevelStorage
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.isAccessible

interface MClientAPIMinecraftServer {
    var session: LevelStorage.Session
    var saveHandler: PlayerSaveHandler
    val serverGuiTickables: List<Runnable>
    var recorder: Recorder
    var favicon: Favicon?
    val random: Random
    var ticksUntilAutosave: Int
    var lastOverloadWarningNanos: Long
    var lastPlayerSampleUpdate: Long
    var prevFullTickLogTime: Long
    var tasksStartTime: Long
    var waitTime: Long
    var tickEndTimeNanos: Long
    var waitingForNextTick: Boolean
    fun setAverageTickTime(time: Float)
    var serverId: String?
    var resourceManagerHolder: Pair<LifecycledResourceManager, DataPackContents>

    companion object {
        @JvmStatic
        val RESOURSE_MANAGER_HOLDER_CONSTRUCTOR: KFunction<MinecraftServer.ResourceManagerHolder>
        = MinecraftServer.ResourceManagerHolder::class.constructors.firstOrNull()!!

        init {
            RESOURSE_MANAGER_HOLDER_CONSTRUCTOR.isAccessible = true
        }
    }
}
