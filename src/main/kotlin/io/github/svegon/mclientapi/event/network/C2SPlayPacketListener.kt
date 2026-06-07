package io.github.svegon.mclientapi.event.network

import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ServerboundClientInformationPacket
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket
import net.minecraft.network.protocol.common.ServerboundPongPacket
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket
import net.minecraft.network.protocol.game.ServerboundAttackPacket
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQueryPacket
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket
import net.minecraft.network.protocol.game.ServerboundChatAckPacket
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket
import net.minecraft.network.protocol.game.ServerboundChatPacket
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket
import net.minecraft.network.protocol.game.ServerboundChunkBatchReceivedPacket
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket
import net.minecraft.network.protocol.game.ServerboundConfigurationAcknowledgedPacket
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket
import net.minecraft.network.protocol.game.ServerboundContainerSlotStateChangedPacket
import net.minecraft.network.protocol.game.ServerboundDebugSubscriptionRequestPacket
import net.minecraft.network.protocol.game.ServerboundEditBookPacket
import net.minecraft.network.protocol.game.ServerboundEntityTagQueryPacket
import net.minecraft.network.protocol.game.ServerboundInteractPacket
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket
import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket
import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket
import net.minecraft.network.protocol.game.ServerboundPlayerLoadedPacket
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket
import net.minecraft.network.protocol.game.ServerboundSelectBundleItemPacket
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket
import net.minecraft.network.protocol.game.ServerboundSetGameRulePacket
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket
import net.minecraft.network.protocol.game.ServerboundSetTestBlockPacket
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket
import net.minecraft.network.protocol.game.ServerboundSpectateEntityPacket
import net.minecraft.network.protocol.game.ServerboundSwingPacket
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket
import net.minecraft.network.protocol.game.ServerboundTestInstanceBlockActionPacket
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.network.protocol.game.ServerboundUseItemPacket
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface C2SPlayPacketListener : C2SPacketListener, ServerGamePacketListener {
    override fun handleAnimate(packet: ServerboundSwingPacket) {}

    override fun handleChat(packet: ServerboundChatPacket) {}

    override fun handleChatCommand(packet: ServerboundChatCommandPacket) {}

    override fun handleSignedChatCommand(packet: ServerboundChatCommandSignedPacket) {}

    override fun handleChatAck(packet: ServerboundChatAckPacket) {}

    override fun handleClientCommand(packet: ServerboundClientCommandPacket) {}

    override fun handleContainerButtonClick(packet: ServerboundContainerButtonClickPacket) {}

    override fun handleContainerClick(packet: ServerboundContainerClickPacket) {}

    override fun handlePlaceRecipe(packet: ServerboundPlaceRecipePacket) {}

    override fun handleContainerClose(packet: ServerboundContainerClosePacket) {}

    override fun handleAttack(packet: ServerboundAttackPacket) {}

    override fun handleInteract(packet: ServerboundInteractPacket) {}

    override fun handleSpectateEntity(packet: ServerboundSpectateEntityPacket) {}

    override fun handleMovePlayer(packet: ServerboundMovePlayerPacket) {}

    override fun handlePlayerAbilities(packet: ServerboundPlayerAbilitiesPacket) {}

    override fun handlePlayerAction(packet: ServerboundPlayerActionPacket) {}

    override fun handlePlayerCommand(packet: ServerboundPlayerCommandPacket) {}

    override fun handlePlayerInput(packet: ServerboundPlayerInputPacket) {}

    override fun handleSetCarriedItem(packet: ServerboundSetCarriedItemPacket) {}

    override fun handleSetCreativeModeSlot(packet: ServerboundSetCreativeModeSlotPacket) {}

    override fun handleSignUpdate(packet: ServerboundSignUpdatePacket) {}

    override fun handleUseItemOn(packet: ServerboundUseItemOnPacket) {}

    override fun handleUseItem(packet: ServerboundUseItemPacket) {}

    override fun handleTeleportToEntityPacket(packet: ServerboundTeleportToEntityPacket) {}

    override fun handlePaddleBoat(packet: ServerboundPaddleBoatPacket) {}

    override fun handleMoveVehicle(packet: ServerboundMoveVehiclePacket) {}

    override fun handleAcceptTeleportPacket(packet: ServerboundAcceptTeleportationPacket) {}

    override fun handleAcceptPlayerLoad(packet: ServerboundPlayerLoadedPacket) {}

    override fun handleRecipeBookSeenRecipePacket(packet: ServerboundRecipeBookSeenRecipePacket) {}

    override fun handleBundleItemSelectedPacket(packet: ServerboundSelectBundleItemPacket) {}

    override fun handleRecipeBookChangeSettingsPacket(packet: ServerboundRecipeBookChangeSettingsPacket) {}

    override fun handleSeenAdvancements(packet: ServerboundSeenAdvancementsPacket) {}

    override fun handleCustomCommandSuggestions(packet: ServerboundCommandSuggestionPacket) {}

    override fun handleSetCommandBlock(packet: ServerboundSetCommandBlockPacket) {}

    override fun handleSetCommandMinecart(packet: ServerboundSetCommandMinecartPacket) {}

    override fun handlePickItemFromBlock(packet: ServerboundPickItemFromBlockPacket) {}

    override fun handlePickItemFromEntity(packet: ServerboundPickItemFromEntityPacket) {}

    override fun handleRenameItem(packet: ServerboundRenameItemPacket) {}

    override fun handleSetBeaconPacket(packet: ServerboundSetBeaconPacket) {}

    override fun handleSetGameRule(packet: ServerboundSetGameRulePacket) {}

    override fun handleSetStructureBlock(packet: ServerboundSetStructureBlockPacket) {}

    override fun handleSetTestBlock(packet: ServerboundSetTestBlockPacket) {}

    override fun handleTestInstanceBlockAction(packet: ServerboundTestInstanceBlockActionPacket) {}

    override fun handleSelectTrade(packet: ServerboundSelectTradePacket) {}

    override fun handleEditBook(packet: ServerboundEditBookPacket) {}

    override fun handleEntityTagQuery(packet: ServerboundEntityTagQueryPacket) {}

    override fun handleContainerSlotStateChanged(packet: ServerboundContainerSlotStateChangedPacket) {}

    override fun handleBlockEntityTagQuery(packet: ServerboundBlockEntityTagQueryPacket) {}

    override fun handleSetJigsawBlock(packet: ServerboundSetJigsawBlockPacket) {}

    override fun handleJigsawGenerate(packet: ServerboundJigsawGeneratePacket) {}

    override fun handleChangeDifficulty(packet: ServerboundChangeDifficultyPacket) {}

    override fun handleChangeGameMode(packet: ServerboundChangeGameModePacket) {}

    override fun handleLockDifficulty(packet: ServerboundLockDifficultyPacket) {}

    override fun handleChatSessionUpdate(packet: ServerboundChatSessionUpdatePacket) {}

    override fun handleConfigurationAcknowledged(packet: ServerboundConfigurationAcknowledgedPacket) {}

    override fun handleChunkBatchReceived(packet: ServerboundChunkBatchReceivedPacket) {}

    override fun handleDebugSubscriptionRequest(packet: ServerboundDebugSubscriptionRequestPacket) {}

    override fun handleClientTickEnd(packet: ServerboundClientTickEndPacket) {}

    override fun handleKeepAlive(packet: ServerboundKeepAlivePacket) {}

    override fun handlePong(serverboundPongPacket: ServerboundPongPacket) {}

    override fun handleCustomPayload(packet: ServerboundCustomPayloadPacket) {}

    override fun handleResourcePackResponse(packet: ServerboundResourcePackPacket) {}

    override fun handleClientInformation(packet: ServerboundClientInformationPacket) {}

    override fun handleCustomClickAction(packet: ServerboundCustomClickActionPacket) {}

    override fun handleCookieResponse(packet: ServerboundCookieResponsePacket) {}

    override fun handlePingRequest(packet: ServerboundPingRequestPacket) {}

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