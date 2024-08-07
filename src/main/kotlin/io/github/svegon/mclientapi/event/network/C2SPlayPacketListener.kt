package io.github.svegon.capi.event.network

import io.github.svegon.capi.event.ListenerCollectionFactory
import io.github.svegon.capi.event.ListenerSet
import io.github.svegon.capi.event.network.C2SPlayPacketListener
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.listener.ServerPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.common.*
import net.minecraft.network.packet.c2s.play.*
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SPlayPacketListener : ServerPlayPacketListener {
    fun apply(packet: Packet<ServerPlayPacketListener?>, info: CallbackInfo?) {
        packet.apply(this)
    }

    override fun isConnectionOpen(): Boolean {
        return false
    }

    override fun onPlayerSession(packet: PlayerSessionC2SPacket) {
    }

    override fun onHandSwing(packet: HandSwingC2SPacket) {
    }

    override fun onChatMessage(packet: ChatMessageC2SPacket) {
    }

    override fun onCommandExecution(packet: CommandExecutionC2SPacket) {
    }

    override fun onMessageAcknowledgment(packet: MessageAcknowledgmentC2SPacket) {
    }

    override fun onClientStatus(packet: ClientStatusC2SPacket) {
    }

    override fun onButtonClick(packet: ButtonClickC2SPacket) {
    }

    override fun onClickSlot(packet: ClickSlotC2SPacket) {
    }

    override fun onCraftRequest(packet: CraftRequestC2SPacket) {
    }

    override fun onCloseHandledScreen(packet: CloseHandledScreenC2SPacket) {
    }

    override fun onCustomPayload(packet: CustomPayloadC2SPacket) {
    }

    override fun onPlayerInteractEntity(packet: PlayerInteractEntityC2SPacket) {
    }

    override fun onKeepAlive(packet: KeepAliveC2SPacket) {
    }

    override fun onPlayerMove(packet: PlayerMoveC2SPacket) {
    }

    override fun onUpdatePlayerAbilities(packet: UpdatePlayerAbilitiesC2SPacket) {
    }

    override fun onPlayerAction(packet: PlayerActionC2SPacket) {
    }

    override fun onClientCommand(packet: ClientCommandC2SPacket) {
    }

    override fun onPlayerInput(packet: PlayerInputC2SPacket) {
    }

    override fun onUpdateSelectedSlot(packet: UpdateSelectedSlotC2SPacket) {
    }

    override fun onCreativeInventoryAction(packet: CreativeInventoryActionC2SPacket) {
    }

    override fun onUpdateSign(packet: UpdateSignC2SPacket) {
    }

    override fun onPlayerInteractBlock(packet: PlayerInteractBlockC2SPacket) {
    }

    override fun onPlayerInteractItem(packet: PlayerInteractItemC2SPacket) {
    }

    override fun onSpectatorTeleport(packet: SpectatorTeleportC2SPacket) {
    }

    override fun onResourcePackStatus(packet: ResourcePackStatusC2SPacket) {
    }

    override fun onBoatPaddleState(packet: BoatPaddleStateC2SPacket) {
    }

    override fun onVehicleMove(packet: VehicleMoveC2SPacket) {
    }

    override fun onTeleportConfirm(packet: TeleportConfirmC2SPacket) {
    }

    override fun onRecipeBookData(packet: RecipeBookDataC2SPacket) {
    }

    override fun onRecipeCategoryOptions(packet: RecipeCategoryOptionsC2SPacket) {
    }

    override fun onAdvancementTab(packet: AdvancementTabC2SPacket) {
    }

    override fun onRequestCommandCompletions(packet: RequestCommandCompletionsC2SPacket) {
    }

    override fun onUpdateCommandBlock(packet: UpdateCommandBlockC2SPacket) {
    }

    override fun onUpdateCommandBlockMinecart(packet: UpdateCommandBlockMinecartC2SPacket) {
    }

    override fun onPickFromInventory(packet: PickFromInventoryC2SPacket) {
    }

    override fun onRenameItem(packet: RenameItemC2SPacket) {
    }

    override fun onUpdateBeacon(packet: UpdateBeaconC2SPacket) {
    }

    override fun onUpdateStructureBlock(packet: UpdateStructureBlockC2SPacket) {
    }

    override fun onSelectMerchantTrade(packet: SelectMerchantTradeC2SPacket) {
    }

    override fun onBookUpdate(packet: BookUpdateC2SPacket) {
    }

    override fun onQueryEntityNbt(packet: QueryEntityNbtC2SPacket) {
    }

    override fun onQueryBlockNbt(packet: QueryBlockNbtC2SPacket) {
    }

    override fun onUpdateJigsaw(packet: UpdateJigsawC2SPacket) {
    }

    override fun onJigsawGenerating(packet: JigsawGeneratingC2SPacket) {
    }

    override fun onUpdateDifficulty(packet: UpdateDifficultyC2SPacket) {
    }

    override fun onUpdateDifficultyLock(packet: UpdateDifficultyLockC2SPacket) {
    }

    fun onDisconnected(reason: Text?) {
    }

    override fun onQueryPing(packet: QueryPingC2SPacket) {
    }

    override fun onPong(packet: CommonPongC2SPacket) {
    }

    override fun onClientOptions(packet: ClientOptionsC2SPacket) {
    }

    override fun onAcknowledgeReconfiguration(packet: AcknowledgeReconfigurationC2SPacket) {
    }

    override fun onAcknowledgeChunks(packet: AcknowledgeChunksC2SPacket) {
    }

    companion object {
        @JvmField
        val CLIENT_PACKET_SENT_EVENT: Event<C2SPlayPacketListener> = EventFactory.createArrayBacked(
            C2SPlayPacketListener::class.java
        ) { listeners: Array<C2SPlayPacketListener> ->
            object : C2SPlayPacketListener {
                override fun apply(packet: Packet<ServerPlayPacketListener?>, info: CallbackInfo) {
                    for (listener in listeners) {
                        listener.apply(packet, info)

                        if (info.isCancelled) {
                            return@createArrayBacked
                        }
                    }
                }
            }
        }
        val LISTENER_LIST: io.github.svegon.capi.event.ListenerList<C2SPlayPacketListener> =
            ListenerCollectionFactory.Companion.listenersVector<Any>(
                Function<List<Any>, Any> { l: List<Any> ->
                    object : C2SPlayPacketListener {
                        override fun apply(packet: Packet<ServerPlayPacketListener?>, info: CallbackInfo) {
                            for (listener in l) {
                                listener.apply(packet, info)

                                if (info.isCancelled) {
                                    return@listenersVector
                                }
                            }
                        }
                    }
                })
        val LISTENER_SET: ListenerSet<C2SPlayPacketListener> =
            ListenerCollectionFactory.Companion.listenersLinkedSet<Any>(
                Function<Set<Any>, Any> { s: Set<Any> ->
                    object : C2SPlayPacketListener {
                        override fun apply(packet: Packet<ServerPlayPacketListener?>, info: CallbackInfo) {
                            for (listener in s) {
                                listener.apply(packet, info)

                                if (info.isCancelled) {
                                    return@listenersLinkedSet
                                }
                            }
                        }
                    }
                })
    }
}
