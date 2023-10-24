package com.github.svegon.capi.event.network;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public final class PlayerPositionSyncEvents {
    private PlayerPositionSyncEvents() {
        throw new UnsupportedOperationException();
    }

    public static final Event<BeforeVehiclePacketsSend> BEFORE_VEHICLE_PACKETS_SEND_EVENT =
            EventFactory.createArrayBacked(BeforeVehiclePacketsSend.class, (player, callback) -> {},
                    listeners -> (player, callback) -> {
                        for (BeforeVehiclePacketsSend listener : listeners) {
                            listener.beforeVehiclePacketsSend(player, callback);

                            if (callback.isCancelled()) {
                                return;
                            }
                        }
                    });

    public static final Event<AfterVehiclePacketsSend> AFTER_VEHICLE_PACKETS_SEND_EVENT =
            EventFactory.createArrayBacked(AfterVehiclePacketsSend.class, (player) -> {},
                    listeners -> (player) -> {
                        for (AfterVehiclePacketsSend listener : listeners) {
                            listener.afterVehiclePacketsSend(player);
                        }
                    });

    public static final Event<BeforeMovementPacketsSend> BEFORE_MOVEMENT_PACKETS_SEND_EVENT =
            EventFactory.createArrayBacked(BeforeMovementPacketsSend.class, (player, callback) -> {},
                    listeners -> (player, callback) -> {
                        for (BeforeMovementPacketsSend listener : listeners) {
                            listener.beforePacketsSend(player, callback);

                            if (callback.isCancelled()) {
                                return;
                            }
                        }
                    });

    public static final Event<AfterMovementPacketsSend> AFTER_MOVEMENT_PACKETS_SEND_EVENT =
            EventFactory.createArrayBacked(AfterMovementPacketsSend.class, (player) -> {}, listeners -> (player) -> {
                        for (AfterMovementPacketsSend listener : listeners) {
                            listener.afterPacketsSend(player);
                        }
                    });

    public interface BeforeVehiclePacketsSend {
        void beforeVehiclePacketsSend(ClientPlayerEntity player, CallbackInfo callback);
    }

    public interface AfterVehiclePacketsSend {
        void afterVehiclePacketsSend(ClientPlayerEntity player);
    }

    public interface BeforeMovementPacketsSend {
        void beforePacketsSend(ClientPlayerEntity player, CallbackInfo callback);
    }

    public interface AfterMovementPacketsSend {
        void afterPacketsSend(ClientPlayerEntity player);
    }
}
