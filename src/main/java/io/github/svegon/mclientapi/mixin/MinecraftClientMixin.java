package io.github.svegon.capi.mixin;

import io.github.svegon.capi.event.input.CrosshairTargetTypeCallback;
import io.github.svegon.capi.event.render.EnableAmbientOcclusionCallback;
import io.github.svegon.capi.event.render.RenderListener;
import io.github.svegon.capi.event.world.ClientWorldLifecycleEvents;
import io.github.svegon.capi.mixininterface.IMinecraftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.toast.TutorialToast;
import net.minecraft.client.util.WindowProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.profiler.TickTimeTracker;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler,
        IMinecraftClient {
    private MinecraftClientMixin(String string) {
        super(string);
    }

    @Shadow
    @Final
    private WindowProvider windowProvider;
    @Shadow
    @Final
    @Mutable
    private RenderTickCounter renderTickCounter;
    @Shadow
    @Final
    @Mutable
    private SearchManager searchManager;
    @Shadow
    private int itemUseCooldown;
    @Shadow
    private float pausedTickDelta;
    @Shadow
    private long lastMetricsSampleTime;
    @Shadow
    private long nextDebugInfoUpdateTime;
    @Shadow
    @Nullable
    private TutorialToast socialInteractionsToast;
    @Mutable
    @Shadow
    @Final
    private TickTimeTracker tickTimeTracker;

    @Shadow @Nullable public ClientWorld world;

    @Shadow protected abstract boolean doAttack();

    @Shadow protected abstract void doItemPick();

    @Shadow protected abstract void doItemUse();

    @Shadow protected abstract void handleBlockBreaking(boolean bl);

    @Shadow public int attackCooldown;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;" +
            "setPhase(Ljava/lang/String;)V", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    private void onRender(boolean tick, CallbackInfo info) {
        RenderListener.EVENT.invoker().onRender(tick, info);
    }

    @Redirect(method = "handleBlockBreaking", target = @Desc(owner = HitResult.class, value = "getType",
            ret = HitResult.Type.class), at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
    private HitResult.Type handleBlockBreakingGetCrosshairTargetType(HitResult hitResult) {
        return CrosshairTargetTypeCallback.BLOCK_BREAK.invoker().getCrosshairTargetType(hitResult);
    }

    @Redirect(method = "doAttack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"),
            target = @Desc(owner = HitResult.class, value = "getType", ret = HitResult.Type.class))
    private HitResult.Type attackGetCrosshairTargetType(HitResult hitResult) {
        return CrosshairTargetTypeCallback.ATTACK.invoker().getCrosshairTargetType(hitResult);
    }

    @Redirect(method = "doItemUse", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"),
            target = @Desc(owner = HitResult.class, value = "getType", ret = HitResult.Type.class))
    private HitResult.Type itemUseGetCrosshairTargetType(HitResult hitResult) {
        return CrosshairTargetTypeCallback.ITEM_USE.invoker().getCrosshairTargetType(hitResult);
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"), cancellable = true)
    private void onDisconnect(Screen screen, CallbackInfo callback) {
        if (world != null) {
            ClientWorldLifecycleEvents.LEAVE_WORLD.invoker().onWorldLeave((MinecraftClient) (Object) this, screen);
        }
    }

    @Inject(method = "isAmbientOcclusionEnabled", at = @At("RETURN"), cancellable = true)
    private static void onIsAmbientOcclusionEnabled(CallbackInfoReturnable<Boolean> callback) {
        EnableAmbientOcclusionCallback.EVENT.invoker().isAmbientOcclusionEnabled(callback);
    }

    @Redirect(method = "doItemPick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"),
            target = @Desc(owner = HitResult.class, value = "getType", ret = HitResult.Type.class))
    private HitResult.Type itemPickGetCrosshairTargetType(HitResult hitResult) {
        return CrosshairTargetTypeCallback.ITEM_PICK.invoker().getCrosshairTargetType(hitResult);
    }

    @Override
    public int getItemUseCooldown() {
        return itemUseCooldown;
    }

    @Override
    public void setItemUseCooldown(int itemUseCooldown) {
        this.itemUseCooldown = itemUseCooldown;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(int cooldown) {
        attackCooldown = cooldown;
    }

    @Override
    public float getPausedTickDelta() {
        return pausedTickDelta;
    }

    @Override
    public long lastMetricsSampleTime() {
        return lastMetricsSampleTime;
    }

    @Override
    public long nextDebugInfoUpdateTime() {
        return nextDebugInfoUpdateTime;
    }

    @Override
    public TutorialToast getSocialInteractionsToast() {
        return socialInteractionsToast;
    }

    @Override
    public void setSocialInteractionsToast(TutorialToast toast) {
        socialInteractionsToast = toast;
    }

    @Override
    public TickTimeTracker getTickTimeTracker() {
        return tickTimeTracker;
    }

    @Override
    public void attack() {
        doAttack();
    }

    @Override
    public void pickItem() {
        doItemPick();
    }

    @Override
    public void useItem() {
        doItemUse();
    }

    @Override
    public void progressBlockBreaking() {
        handleBlockBreaking(true);
    }

    @Override
    public WindowProvider getWindowProvider() {
        return windowProvider;
    }

    @Override
    public RenderTickCounter getRenderTickCounter() {
        return renderTickCounter;
    }

    @Override
    public void setRenderTickCounter(RenderTickCounter renderTickCounter) {
        this.renderTickCounter = renderTickCounter;
    }

    @Override
    public SearchManager getSearchManager() {
        return searchManager;
    }

    @Override
    public void setSearchManager(SearchManager searchManager) {
        this.searchManager = searchManager;
    }
}
