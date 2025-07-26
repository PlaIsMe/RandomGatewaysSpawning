package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbchunks.api.ChunkTeamData;
import dev.ftb.mods.ftbchunks.api.ClaimResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunkManager;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.shadowsoffire.gateways.entity.GatewayEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;

public class ClaimChunkHelper {
    private static ClaimChunkHelper instance;
    private final ClaimedChunkManager claimedChunkManager;
    private static final Logger LOGGER = LogManager.getLogger();

    public ClaimChunkHelper(ClaimedChunkManager claimedChunkManager) {
        this.claimedChunkManager = claimedChunkManager;
    }

    public static ClaimChunkHelper getInstance(MinecraftServer server) throws CommandSyntaxException {
        if (instance == null) {
            instance = new ClaimChunkHelper(FTBChunksAPI.api().getManager());
        }
        return instance;
    }

    public void unClaimChunk(CommandSourceStack source, ServerPlayer player, BlockPos pos, String unClaimUUID) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = player.level().dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        UUID uuid = (!unClaimUUID.isEmpty() ? UUID.fromString(unClaimUUID) : player.getUUID());
        Optional<Team> ftbTeam = FTBTeamsAPI.api().getManager().getTeamForPlayerID(uuid);
        ftbTeam.ifPresent(team -> {
            ChunkTeamData teamData = claimedChunkManager.getOrCreateData(team);
            teamData.unclaim(source, chunkDimPos, false);
        });
    }

    public void unForceLoadChunk(CommandSourceStack source, ServerPlayer player, BlockPos pos, String unClaimUUID) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = player.level().dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        UUID uuid = (!unClaimUUID.isEmpty() ? UUID.fromString(unClaimUUID) : player.getUUID());
        Optional<Team> ftbTeam = FTBTeamsAPI.api().getManager().getTeamForPlayerID(uuid);
        ftbTeam.ifPresent(team -> {
            ChunkTeamData teamData = claimedChunkManager.getOrCreateData(team);
            teamData.unForceLoad(source, chunkDimPos, false);
        });
    }

    public void processClaim(CommandSourceStack source, ServerPlayer player, BlockPos pos, GatewayEntity gatewayEntity) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = player.level().dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        ChunkTeamData teamData = claimedChunkManager.getOrCreateData(player);
        if (!teamData.claim(source, chunkDimPos, false).equals(ClaimResult.StandardProblem.ALREADY_CLAIMED)) {
            teamData.forceLoad(source, chunkDimPos, false);
            gatewayEntity.getPersistentData().putString("CleanUpAction", "full");
        } else {
            if (!teamData.forceLoad(source, chunkDimPos, false).equals(ClaimResult.StandardProblem.ALREADY_LOADED)) {
                gatewayEntity.getPersistentData().putString("CleanUpAction", "half");
            }
        }
    }
}
