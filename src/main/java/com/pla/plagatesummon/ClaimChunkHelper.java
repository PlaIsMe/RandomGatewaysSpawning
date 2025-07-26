package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbchunks.api.*;
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

    public void unClaimChunk(CommandSourceStack source, BlockPos pos, UUID teamUUID, GatewayEntity gatewayEntity) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = gatewayEntity.level().dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        Optional<Team> ftbTeam = FTBTeamsAPI.api().getManager().getTeamByID(teamUUID);
        ftbTeam.ifPresent(team -> {
            ChunkTeamData teamData = claimedChunkManager.getOrCreateData(team);
            teamData.unclaim(source, chunkDimPos, false);
        });
    }

    public void unForceLoadChunk(CommandSourceStack source, BlockPos pos, UUID teamUUID, GatewayEntity gatewayEntity) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = gatewayEntity.level().dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        Optional<Team> ftbTeam = FTBTeamsAPI.api().getManager().getTeamByID(teamUUID);
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
            gatewayEntity.getPersistentData().putUUID("UnClaimUUID", teamData.getTeam().getTeamId());
        } else {
            ClaimedChunk chunk = claimedChunkManager.getChunk(chunkDimPos);
            if (chunk != null) {
                ChunkTeamData forceLoadTeam = chunk.getTeamData();
                if (!forceLoadTeam.forceLoad(source, chunkDimPos, false).equals(ClaimResult.StandardProblem.ALREADY_LOADED)) {
                    gatewayEntity.getPersistentData().putString("CleanUpAction", "half");
                    gatewayEntity.getPersistentData().putUUID("UnClaimUUID", forceLoadTeam.getTeam().getTeamId());
                }
            }
        }
    }
}
