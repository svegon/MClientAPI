package com.github.svegon.capi.mixininterface;

import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.search.SearchManager;
import net.minecraft.client.toast.TutorialToast;
import net.minecraft.client.util.WindowProvider;
import net.minecraft.util.profiler.TickTimeTracker;

public interface IMinecraftClient {
   WindowProvider getWindowProvider();

   RenderTickCounter getRenderTickCounter();

   void setRenderTickCounter(RenderTickCounter renderTickCounter);

   SearchManager getSearchManager();

   void setSearchManager(SearchManager searchManager);

   int getItemUseCooldown();

   void setItemUseCooldown(int cooldown);

   int getAttackCooldown();

   void setAttackCooldown(int cooldown);

   float getPausedTickDelta();

   long lastMetricsSampleTime();

   long nextDebugInfoUpdateTime();

   TutorialToast getSocialInteractionsToast();

   void setSocialInteractionsToast(TutorialToast toast);

   TickTimeTracker getTickTimeTracker();

   void attack();

   void pickItem();

   void useItem();

   void progressBlockBreaking();
}
