package io.github.svegon.mclientapi.client.mixin;

import com.mojang.blaze3d.platform.WindowEventHandler;
import io.github.svegon.mclientapi.client.event.input.CrosshairTargetTypeCallback;
import io.github.svegon.mclientapi.client.event.render.RenderListener;
import io.github.svegon.mclientapi.client.event.world.ClientWorldLifecycleEvents;
import io.github.svegon.mclientapi.client.mixinterface.IMinecraftClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler,
        IMinecraftClient {
    @Shadow private @Final DeltaTracker.Timer deltaTracker;
    @Shadow private int rightClickDelay;
    @Shadow @Nullable private TutorialToast socialInteractionsToast;

    @Shadow private boolean startAttack() { throw new AssertionError();}

    @Shadow private void pickBlockOrEntity() { throw new AssertionError();}

    @Shadow private void startUseItem() { throw new AssertionError();}

    @Shadow private void continueAttack(final boolean down) { throw new AssertionError();}

    @Shadow @Nullable public ClientLevel level;

    @Inject(method = "renderFrame", at = @At("HEAD"), cancellable = true)
    private void onRender(boolean advanceGameTime, CallbackInfo info) {
        RenderListener.Companion.getEVENT().invoker().onRender((Minecraft) (Object) this, advanceGameTime, info);
    }

    @Redirect(method = "startUseItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"),
            target = @Desc(owner = HitResult.class, value = "getType", ret = HitResult.Type.class))
    private HitResult.Type itemUseGetCrosshairTargetType(HitResult hitResult) {
        return CrosshairTargetTypeCallback.Companion.getITEM_USE().invoker().getCrosshairTargetType((Minecraft)
                (Object) this, hitResult);
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screens/Screen;ZZ)V", at = @At("HEAD"))
    private void onDisconnect(Screen screen, boolean keepResourcePacks, boolean stopSound, CallbackInfo ci) {
        if (level != null) {
            ClientWorldLifecycleEvents.INSTANCE.getLEAVE_WORLD().invoker().onWorldLeave((Minecraft) (Object) this,
                    screen, keepResourcePacks, stopSound, ci);
        }
    }

    @Override
    public int getMClientAPI$itemUseCooldown() {
        return rightClickDelay;
    }

    @Override
    public void setMClientAPI$itemUseCooldown(int rightClickDelay) {
        this.rightClickDelay = rightClickDelay;
    }

    @Override
    public @Nullable TutorialToast getMClientAPI$socialInteractionsToast() {
        return socialInteractionsToast;
    }

    @Override
    public void setMClientAPI$socialInteractionsToast(@Nullable TutorialToast tutorialToast) {
        socialInteractionsToast = tutorialToast;
    }

    @Override
    public DeltaTracker.@NotNull Timer getMClientAPI$tickTimer() {
        return deltaTracker;
    }

    @Override
    public void mClientAPI$attack() {
        startAttack();
    }

    @Override
    public void mClientAPI$pickItem() {
        pickBlockOrEntity();
    }

    @Override
    public void mClientAPI$useItem() {
        startUseItem();
    }

    @Override
    public void mClientAPI$progressBlockBreaking() {
        continueAttack(true);
    }

    private MinecraftMixin(String name, boolean propagatesCrashes) {
        super(name, propagatesCrashes);
    }
}
