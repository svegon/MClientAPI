package io.github.svegon.mclientapi.event.network.packet_direct

import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.listener.PacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.common.*
import net.minecraft.network.packet.s2c.play.*
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket
import net.minecraft.util.hit.HitResult
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.Function

interface S2CPlayPacketListener : S2CPacketListener, ClientPlayPacketListener {
    override fun onPingResult(packet: PingResultS2CPacket) {}

    override fun onCookieRequest(packet: CookieRequestS2CPacket) {}

    override fun onKeepAlive(packet: KeepAliveS2CPacket) {}

    override fun onPing(packet: CommonPingS2CPacket) {}

    override fun onCustomPayload(packet: CustomPayloadS2CPacket) {}

    override fun onDisconnect(packet: DisconnectS2CPacket) {}

    override fun onResourcePackSend(packet: ResourcePackSendS2CPacket) {}

    override fun onResourcePackRemove(packet: ResourcePackRemoveS2CPacket) {}

    override fun onSynchronizeTags(packet: SynchronizeTagsS2CPacket) {}

    override fun onStoreCookie(packet: StoreCookieS2CPacket) {}

    override fun onServerTransfer(packet: ServerTransferS2CPacket) {}

    override fun onCustomReportDetails(packet: CustomReportDetailsS2CPacket) {}

    override fun onServerLinks(packet: ServerLinksS2CPacket) {}

    override fun onEntitySpawn(packet: EntitySpawnS2CPacket) {}

    override fun onExperienceOrbSpawn(packet: ExperienceOrbSpawnS2CPacket) {}

    override fun onScoreboardObjectiveUpdate(packet: ScoreboardObjectiveUpdateS2CPacket) {}

    override fun onEntityAnimation(packet: EntityAnimationS2CPacket) {}

    override fun onDamageTilt(packet: DamageTiltS2CPacket) {}

    override fun onStatistics(packet: StatisticsS2CPacket) {}

    override fun onUnlockRecipes(packet: ChangeUnlockedRecipesS2CPacket) {}

    override fun onBlockBreakingProgress(packet: BlockBreakingProgressS2CPacket) {}

    override fun onSignEditorOpen(packet: SignEditorOpenS2CPacket) {}

    override fun onBlockEntityUpdate(packet: BlockEntityUpdateS2CPacket) {}

    override fun onBlockEvent(packet: BlockEventS2CPacket) {}

    override fun onBlockUpdate(packet: BlockUpdateS2CPacket) {}

    override fun onGameMessage(packet: GameMessageS2CPacket) {}

    override fun onChatMessage(packet: ChatMessageS2CPacket) {}

    override fun onProfilelessChatMessage(packet: ProfilelessChatMessageS2CPacket) {}

    override fun onRemoveMessage(packet: RemoveMessageS2CPacket) {}

    override fun onChunkDeltaUpdate(packet: ChunkDeltaUpdateS2CPacket) {}

    override fun onMapUpdate(packet: MapUpdateS2CPacket) {}

    override fun onCloseScreen(packet: CloseScreenS2CPacket) {}

    override fun onInventory(packet: InventoryS2CPacket) {}

    override fun onOpenHorseScreen(packet: OpenHorseScreenS2CPacket) {}

    override fun onScreenHandlerPropertyUpdate(packet: ScreenHandlerPropertyUpdateS2CPacket) {}

    override fun onScreenHandlerSlotUpdate(packet: ScreenHandlerSlotUpdateS2CPacket) {}

    override fun onEntityStatus(packet: EntityStatusS2CPacket) {}

    override fun onEntityAttach(packet: EntityAttachS2CPacket) {}

    override fun onEntityPassengersSet(packet: EntityPassengersSetS2CPacket) {}

    override fun onExplosion(packet: ExplosionS2CPacket) {}

    override fun onGameStateChange(packet: GameStateChangeS2CPacket) {}

    override fun onChunkData(packet: ChunkDataS2CPacket) {}

    override fun onChunkBiomeData(packet: ChunkBiomeDataS2CPacket) {}

    override fun onUnloadChunk(packet: UnloadChunkS2CPacket) {}

    override fun onWorldEvent(packet: WorldEventS2CPacket) {}

    override fun onGameJoin(packet: GameJoinS2CPacket) {}

    override fun onEntity(packet: EntityS2CPacket) {}

    override fun onPlayerPositionLook(packet: PlayerPositionLookS2CPacket) {}

    override fun onParticle(packet: ParticleS2CPacket) {}

    override fun onPlayerAbilities(packet: PlayerAbilitiesS2CPacket) {}

    override fun onPlayerRemove(packet: PlayerRemoveS2CPacket) {}

    override fun onPlayerList(packet: PlayerListS2CPacket) {}

    override fun onEntitiesDestroy(packet: EntitiesDestroyS2CPacket) {}

    override fun onRemoveEntityStatusEffect(packet: RemoveEntityStatusEffectS2CPacket) {}

    override fun onPlayerRespawn(packet: PlayerRespawnS2CPacket) {}

    override fun onEntitySetHeadYaw(packet: EntitySetHeadYawS2CPacket) {}

    override fun onUpdateSelectedSlot(packet: UpdateSelectedSlotS2CPacket) {}

    override fun onScoreboardDisplay(packet: ScoreboardDisplayS2CPacket) {}

    override fun onEntityTrackerUpdate(packet: EntityTrackerUpdateS2CPacket) {}

    override fun onEntityVelocityUpdate(packet: EntityVelocityUpdateS2CPacket) {}

    override fun onEntityEquipmentUpdate(packet: EntityEquipmentUpdateS2CPacket) {}

    override fun onExperienceBarUpdate(packet: ExperienceBarUpdateS2CPacket) {}

    override fun onHealthUpdate(packet: HealthUpdateS2CPacket) {}

    override fun onTeam(packet: TeamS2CPacket) {}

    override fun onScoreboardScoreUpdate(packet: ScoreboardScoreUpdateS2CPacket) {}

    override fun onScoreboardScoreReset(packet: ScoreboardScoreResetS2CPacket) {}

    override fun onPlayerSpawnPosition(packet: PlayerSpawnPositionS2CPacket) {}

    override fun onWorldTimeUpdate(packet: WorldTimeUpdateS2CPacket) {}

    override fun onPlaySound(packet: PlaySoundS2CPacket) {}

    override fun onPlaySoundFromEntity(packet: PlaySoundFromEntityS2CPacket) {}

    override fun onItemPickupAnimation(packet: ItemPickupAnimationS2CPacket) {}

    override fun onEntityPosition(packet: EntityPositionS2CPacket) {}

    override fun onUpdateTickRate(packet: UpdateTickRateS2CPacket) {}

    override fun onTickStep(packet: TickStepS2CPacket) {}

    override fun onEntityAttributes(packet: EntityAttributesS2CPacket) {}

    override fun onEntityStatusEffect(packet: EntityStatusEffectS2CPacket) {}

    override fun onEndCombat(packet: EndCombatS2CPacket) {}

    override fun onEnterCombat(packet: EnterCombatS2CPacket) {}

    override fun onDeathMessage(packet: DeathMessageS2CPacket) {}

    override fun onDifficulty(packet: DifficultyS2CPacket) {}

    override fun onSetCameraEntity(packet: SetCameraEntityS2CPacket) {}

    override fun onWorldBorderInitialize(packet: WorldBorderInitializeS2CPacket) {}

    override fun onWorldBorderInterpolateSize(packet: WorldBorderInterpolateSizeS2CPacket) {}

    override fun onWorldBorderSizeChanged(packet: WorldBorderSizeChangedS2CPacket) {}

    override fun onWorldBorderWarningTimeChanged(packet: WorldBorderWarningTimeChangedS2CPacket) {}

    override fun onWorldBorderWarningBlocksChanged(packet: WorldBorderWarningBlocksChangedS2CPacket) {}

    override fun onWorldBorderCenterChanged(packet: WorldBorderCenterChangedS2CPacket) {}

    override fun onPlayerListHeader(packet: PlayerListHeaderS2CPacket) {}

    override fun onBossBar(packet: BossBarS2CPacket) {}

    override fun onCooldownUpdate(packet: CooldownUpdateS2CPacket) {}

    override fun onVehicleMove(packet: VehicleMoveS2CPacket) {}

    override fun onAdvancements(packet: AdvancementUpdateS2CPacket) {}

    override fun onSelectAdvancementTab(packet: SelectAdvancementTabS2CPacket) {}

    override fun onCraftFailedResponse(packet: CraftFailedResponseS2CPacket) {}

    override fun onCommandTree(packet: CommandTreeS2CPacket) {}

    override fun onStopSound(packet: StopSoundS2CPacket) {}

    override fun onCommandSuggestions(packet: CommandSuggestionsS2CPacket) {}

    override fun onSynchronizeRecipes(packet: SynchronizeRecipesS2CPacket) {}

    override fun onLookAt(packet: LookAtS2CPacket) {}

    override fun onNbtQueryResponse(packet: NbtQueryResponseS2CPacket) {}

    override fun onLightUpdate(packet: LightUpdateS2CPacket) {}

    override fun onOpenWrittenBook(packet: OpenWrittenBookS2CPacket) {}

    override fun onOpenScreen(packet: OpenScreenS2CPacket) {}

    override fun onSetTradeOffers(packet: SetTradeOffersS2CPacket) {}

    override fun onChunkLoadDistance(packet: ChunkLoadDistanceS2CPacket) {}

    override fun onSimulationDistance(packet: SimulationDistanceS2CPacket) {}

    override fun onChunkRenderDistanceCenter(packet: ChunkRenderDistanceCenterS2CPacket) {}

    override fun onPlayerActionResponse(packet: PlayerActionResponseS2CPacket) {}

    override fun onOverlayMessage(packet: OverlayMessageS2CPacket) {}

    override fun onSubtitle(packet: SubtitleS2CPacket) {}

    override fun onTitle(packet: TitleS2CPacket) {}

    override fun onTitleFade(packet: TitleFadeS2CPacket) {}

    override fun onTitleClear(packet: ClearTitleS2CPacket) {}

    override fun onServerMetadata(packet: ServerMetadataS2CPacket) {}

    override fun onChatSuggestions(packet: ChatSuggestionsS2CPacket) {}

    override fun onBundle(packet: BundleS2CPacket) {}

    override fun onEntityDamage(packet: EntityDamageS2CPacket) {}

    override fun onEnterReconfiguration(packet: EnterReconfigurationS2CPacket) {}

    override fun onStartChunkSend(packet: StartChunkSendS2CPacket) {}

    override fun onChunkSent(packet: ChunkSentS2CPacket) {}

    override fun onDebugSample(packet: DebugSampleS2CPacket) {}

    override fun onProjectilePower(packet: ProjectilePowerS2CPacket) {}

    object EmptyInvoker : S2CPlayPacketListener

    object InvokerFactory : Function<Array<S2CPlayPacketListener>, S2CPlayPacketListener> {
        override fun apply(listeners: Array<S2CPlayPacketListener>): S2CPlayPacketListener {
            return object : S2CPlayPacketListener {
                override fun intercept(packet: Packet<out PacketListener>, ci: CallbackInfo) {
                    for (listener in listeners) {
                        listener.intercept(packet, ci)

                        if (ci.isCancelled) {
                            return
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun emptyInvoker(): S2CPlayPacketListener {
            return EmptyInvoker
        }
        
        fun invokerFactory(): Function<Array<S2CPlayPacketListener>, S2CPlayPacketListener> {
            return InvokerFactory
        }
    }
}