package io.github.svegon.mclientapi.event.network

import net.minecraft.network.listener.PacketListener
import net.minecraft.network.listener.ServerPlayPacketListener
import net.minecraft.network.listener.ServerQueryPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.c2s.common.*
import net.minecraft.network.packet.c2s.play.*
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SPlayPacketListener : C2SPacketListener, ServerPlayPacketListener {
    override fun onQueryPing(packet: QueryPingC2SPacket) {}

    override fun onCookieResponse(packet: CookieResponseC2SPacket) {}

    override fun onKeepAlive(packet: KeepAliveC2SPacket) {}

    override fun onPong(packet: CommonPongC2SPacket) {}

    override fun onCustomPayload(packet: CustomPayloadC2SPacket) {}

    override fun onResourcePackStatus(packet: ResourcePackStatusC2SPacket) {}

    override fun onClientOptions(packet: ClientOptionsC2SPacket) {}

    override fun onHandSwing(packet: HandSwingC2SPacket) {}

    override fun onChatMessage(packet: ChatMessageC2SPacket) {}

    override fun onCommandExecution(packet: CommandExecutionC2SPacket) {}

    override fun onChatCommandSigned(packet: ChatCommandSignedC2SPacket) {}

    override fun onMessageAcknowledgment(packet: MessageAcknowledgmentC2SPacket) {}

    override fun onClientStatus(packet: ClientStatusC2SPacket) {}

    override fun onButtonClick(packet: ButtonClickC2SPacket) {}

    override fun onClickSlot(packet: ClickSlotC2SPacket) {}

    override fun onCraftRequest(packet: CraftRequestC2SPacket) {}

    override fun onCloseHandledScreen(packet: CloseHandledScreenC2SPacket) {}

    override fun onPlayerInteractEntity(packet: PlayerInteractEntityC2SPacket) {}

    override fun onPlayerMove(packet: PlayerMoveC2SPacket) {}

    override fun onUpdatePlayerAbilities(packet: UpdatePlayerAbilitiesC2SPacket) {}

    override fun onPlayerAction(packet: PlayerActionC2SPacket) {}

    override fun onClientCommand(packet: ClientCommandC2SPacket) {}

    override fun onPlayerInput(packet: PlayerInputC2SPacket) {}

    override fun onUpdateSelectedSlot(packet: UpdateSelectedSlotC2SPacket) {}

    override fun onCreativeInventoryAction(packet: CreativeInventoryActionC2SPacket) {}

    override fun onUpdateSign(packet: UpdateSignC2SPacket) {}

    override fun onPlayerInteractBlock(packet: PlayerInteractBlockC2SPacket) {}

    override fun onPlayerInteractItem(packet: PlayerInteractItemC2SPacket) {}

    override fun onSpectatorTeleport(packet: SpectatorTeleportC2SPacket) {}

    override fun onBoatPaddleState(packet: BoatPaddleStateC2SPacket) {}

    override fun onVehicleMove(packet: VehicleMoveC2SPacket) {}

    override fun onTeleportConfirm(packet: TeleportConfirmC2SPacket) {}

    override fun onRecipeBookData(packet: RecipeBookDataC2SPacket) {}

    override fun onRecipeCategoryOptions(packet: RecipeCategoryOptionsC2SPacket) {}

    override fun onAdvancementTab(packet: AdvancementTabC2SPacket) {}

    override fun onRequestCommandCompletions(packet: RequestCommandCompletionsC2SPacket) {}

    override fun onUpdateCommandBlock(packet: UpdateCommandBlockC2SPacket) {}

    override fun onUpdateCommandBlockMinecart(packet: UpdateCommandBlockMinecartC2SPacket) {}

    override fun onPickFromInventory(packet: PickFromInventoryC2SPacket) {}

    override fun onRenameItem(packet: RenameItemC2SPacket) {}

    override fun onUpdateBeacon(packet: UpdateBeaconC2SPacket) {}

    override fun onUpdateStructureBlock(packet: UpdateStructureBlockC2SPacket) {}

    override fun onSelectMerchantTrade(packet: SelectMerchantTradeC2SPacket) {}

    override fun onBookUpdate(packet: BookUpdateC2SPacket) {}

    override fun onQueryEntityNbt(packet: QueryEntityNbtC2SPacket) {}

    override fun onSlotChangedState(packet: SlotChangedStateC2SPacket) {}

    override fun onQueryBlockNbt(packet: QueryBlockNbtC2SPacket) {}

    override fun onUpdateJigsaw(packet: UpdateJigsawC2SPacket) {}

    override fun onJigsawGenerating(packet: JigsawGeneratingC2SPacket) {}

    override fun onUpdateDifficulty(packet: UpdateDifficultyC2SPacket) {}

    override fun onUpdateDifficultyLock(packet: UpdateDifficultyLockC2SPacket) {}

    override fun onPlayerSession(packet: PlayerSessionC2SPacket) {}

    override fun onAcknowledgeReconfiguration(packet: AcknowledgeReconfigurationC2SPacket) {}

    override fun onAcknowledgeChunks(packet: AcknowledgeChunksC2SPacket) {}

    override fun onDebugSampleSubscription(packet: DebugSampleSubscriptionC2SPacket) {}

    object EmptyInvoker : C2SPlayPacketListener

    object InvokerFactory : Function<Array<C2SPlayPacketListener>, C2SPlayPacketListener> {
        override fun apply(listeners: Array<C2SPlayPacketListener>): C2SPlayPacketListener {
            return object : C2SPlayPacketListener {
                override fun intercept(packet: Packet<out PacketListener>, callback: CallbackInfo) {
                    for (listener in listeners) {
                        listener.intercept(packet, callback)

                        if (callback.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }
}