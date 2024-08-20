package io.github.svegon.mclientapi.mixin;

import io.github.svegon.mclientapi.mixininterface.MClientAPIMinecraftServer;
import kotlin.Pair;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Recorder;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.PlayerSaveHandler;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements CommandOutput,
        AutoCloseable, MClientAPIMinecraftServer {
    @Mutable
    @Shadow @Final protected LevelStorage.Session session;
    @Mutable
    @Shadow @Final protected PlayerSaveHandler saveHandler;
    @Shadow @Final private List<Runnable> serverGuiTickables;
    @Shadow private Recorder recorder;
    @Shadow private Profiler profiler;
    @Shadow @Nullable private ServerMetadata.@Nullable Favicon favicon;
    @Shadow @Final private Random random;
    @Shadow private int ticksUntilAutosave;
    @Shadow private long lastOverloadWarningNanos;
    @Shadow private long lastPlayerSampleUpdate;
    @Shadow private long prevFullTickLogTime;
    @Shadow private long tasksStartTime;
    @Shadow private long waitTime;
    @Shadow private long tickEndTimeNanos;
    @Shadow private boolean waitingForNextTick;
    @Shadow private float averageTickTime;
    @Shadow private @Nullable String serverId;
    @Shadow private MinecraftServer.ResourceManagerHolder resourceManagerHolder;

    private MinecraftServerMixin(String string) {
        super(string);
    }

    @Override
    public LevelStorage.@NotNull Session getSession() {
        return session;
    }

    @Override
    public void setSession(LevelStorage.Session session) {
        this.session = session;
    }

    @Override
    public @NotNull PlayerSaveHandler getSaveHandler() {
        return saveHandler;
    }

    @Override
    public void setSaveHandler(PlayerSaveHandler saveHandler) {
        this.saveHandler = saveHandler;
    }

    @Override
    public @NotNull List<Runnable> getServerGuiTickables() {
        return serverGuiTickables;
    }

    @Override
    public @NotNull Recorder getRecorder() {
        return recorder;
    }

    @Override
    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
        profiler = recorder.getProfiler();
    }

    @Override
    public ServerMetadata.@Nullable Favicon getFavicon() {
        return favicon;
    }

    @Override
    public void setFavicon(ServerMetadata.@Nullable Favicon favicon) {
        this.favicon = favicon;
    }

    @Override
    public @NotNull Random getRandom() {
        return random;
    }

    @Override
    public int getTicksUntilAutosave() {
        return ticksUntilAutosave;
    }

    @Override
    public void setTicksUntilAutosave(int ticksUntilAutosave) {
        this.ticksUntilAutosave = ticksUntilAutosave;
    }

    @Override
    public long getLastOverloadWarningNanos() {
        return lastOverloadWarningNanos;
    }

    @Override
    public void setLastOverloadWarningNanos(long lastOverloadWarningNanos) {
        this.lastOverloadWarningNanos = lastOverloadWarningNanos;
    }

    @Override
    public long getLastPlayerSampleUpdate() {
        return lastPlayerSampleUpdate;
    }

    @Override
    public void setLastPlayerSampleUpdate(long lastPlayerSampleUpdate) {
        this.lastPlayerSampleUpdate = lastPlayerSampleUpdate;
    }

    @Override
    public long getPrevFullTickLogTime() {
        return prevFullTickLogTime;
    }

    @Override
    public void setPrevFullTickLogTime(long prevFullTickLogTime) {
        this.prevFullTickLogTime = prevFullTickLogTime;
    }

    @Override
    public long getTasksStartTime() {
        return tasksStartTime;
    }

    @Override
    public void setTasksStartTime(long tasksStartTime) {
        this.tasksStartTime = tasksStartTime;
    }

    @Override
    public long getWaitTime() {
        return waitTime;
    }

    @Override
    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public long getTickEndTimeNanos() {
        return tickEndTimeNanos;
    }

    @Override
    public void setTickEndTimeNanos(long tickEndTimeNanos) {
        this.tickEndTimeNanos = tickEndTimeNanos;
    }

    @Override
    public boolean getWaitingForNextTick() {
        return waitingForNextTick;
    }

    @Override
    public void setWaitingForNextTick(boolean waitingForNextTick) {
        this.waitingForNextTick = waitingForNextTick;
    }

    @Override
    public void setAverageTickTime(float time) {
        averageTickTime = time;
    }

    @Override
    public @Nullable String getServerId() {
        return serverId;
    }

    @Override
    public void setServerId(@Nullable String s) {
        serverId = s;
    }

    @Override
    public @NotNull Pair<LifecycledResourceManager, DataPackContents> getResourceManagerHolder() {
        return new Pair<>(resourceManagerHolder.resourceManager(), resourceManagerHolder.dataPackContents());
    }

    @Override
    public void setResourceManagerHolder(@NotNull Pair<? extends LifecycledResourceManager,
            ? extends DataPackContents> pair) {
        resourceManagerHolder = MClientAPIMinecraftServer.getRESOURSE_MANAGER_HOLDER_CONSTRUCTOR()
                .call(pair.getFirst(), pair.getSecond());
    }
}
