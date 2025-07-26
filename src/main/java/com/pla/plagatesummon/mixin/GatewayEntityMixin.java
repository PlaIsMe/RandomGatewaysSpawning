package com.pla.plagatesummon.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pla.plagatesummon.ClaimChunkHelper;
import com.pla.plagatesummon.DailyGateSpawner;
import dev.shadowsoffire.gateways.entity.GatewayEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Objects;

import static com.pla.plagatesummon.WaypointHelper.removeWaypoint;

@Mixin(value = {GatewayEntity.class}, remap = false)
public class GatewayEntityMixin {
    private static final Logger LOGGER = LogManager.getLogger();

    private void cleanUp(GatewayEntity gatewayEntity) {
        CompoundTag tag = gatewayEntity.getPersistentData();
        if (tag.contains("GateSpawnPos")) {
            int[] posArr = tag.getIntArray("GateSpawnPos");
            BlockPos spawnPos = new BlockPos(posArr[0], posArr[1], posArr[2]);
            ClaimChunkHelper claimChunkHelper;
            try {
                claimChunkHelper = ClaimChunkHelper.getInstance(gatewayEntity.getServer());
                if (tag.contains("CleanUpAction") && tag.contains("UnClaimUUID")) {
                    switch (tag.getString("CleanUpAction")) {
                        case "full":
                            claimChunkHelper.unClaimChunk(DailyGateSpawner.source, spawnPos, tag.getUUID("UnClaimUUID"), gatewayEntity);
                            claimChunkHelper.unForceLoadChunk(DailyGateSpawner.source, spawnPos, tag.getUUID("UnClaimUUID"), gatewayEntity);
                            break;
                        case "half":
                            claimChunkHelper.unForceLoadChunk(DailyGateSpawner.source, spawnPos, tag.getUUID("UnClaimUUID"), gatewayEntity);
                            break;
                        default:
                            break;
                    }
                }

            } catch (CommandSyntaxException e) {
                LOGGER.error("PlaGateSummon: Failed to unclaim chunk on spawn");
            }
        }
        removeWaypoint(tag.getString("GateWaypointName"));
        String clearWaypoint = "jm waypoint delete \"" + tag.getString("GateWaypointName") + "\" @a";
        try {
            Objects.requireNonNull(DailyGateSpawner.randomPlayer.getServer()).getCommands().getDispatcher().execute(clearWaypoint, DailyGateSpawner.source);
        } catch (CommandSyntaxException e) {
            LOGGER.warn("PlaGateSummon: (Journey Map Compat) Failed to execute command {}, error {}", clearWaypoint, e);
        }
    }

    @Inject(method = "onGateCreated", at = @At("HEAD"))
    private void claimChunkOnSpawn(CallbackInfo ci) {
        GatewayEntity self = (GatewayEntity) (Object) this;
        CompoundTag tag = self.getPersistentData();
        if (tag.contains("GateSpawnPos")) {
            int[] posArr = tag.getIntArray("GateSpawnPos");
            BlockPos spawnPos = new BlockPos(posArr[0], posArr[1], posArr[2]);
            ClaimChunkHelper claimChunkHelper;
            try {
                claimChunkHelper = ClaimChunkHelper.getInstance(self.getServer());
                claimChunkHelper.processClaim(DailyGateSpawner.source, DailyGateSpawner.randomPlayer, spawnPos, self);
            } catch (CommandSyntaxException e) {
                LOGGER.error("PlaGateSummon: Failed to claim chunk on spawn");
            }
        }
    }

    @Inject(method = "completeGateway", at = @At("HEAD"))
    private void cleanupOnComplete(CallbackInfo ci) {
        GatewayEntity self = (GatewayEntity) (Object) this;
        cleanUp(self);
    }

    @Inject(method = "onFailure", at = @At("HEAD"))
    private void cleanupOnFailure(Collection<LivingEntity> remaining, GatewayEntity.FailureReason reason, CallbackInfo ci) {
        GatewayEntity self = (GatewayEntity) (Object) this;
        cleanUp(self);
    }
}
