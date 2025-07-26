package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbchunks.data.*;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import dev.ftb.mods.ftbteams.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.data.Team;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadows.gateways.entity.GatewayEntity;

import java.util.UUID;

import static dev.ftb.mods.ftbchunks.data.FTBChunksAPI.getManager;

public class ClaimChunkHelper {
    private static ClaimChunkHelper instance;
    private final ClaimedChunkManager claimedChunkManager;
    private static final Logger LOGGER = LogManager.getLogger();

    public ClaimChunkHelper(ClaimedChunkManager claimedChunkManager) {
        this.claimedChunkManager = claimedChunkManager;
    }

    public static ClaimChunkHelper getInstance(MinecraftServer server) throws CommandSyntaxException {
        if (instance == null) {
            instance = new ClaimChunkHelper(getManager());
        }
        return instance;
    }

    public void unClaimChunk(CommandSourceStack source, BlockPos pos, UUID teamUUID, GatewayEntity gatewayEntity) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = gatewayEntity.level.dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        Team team = FTBTeamsAPI.getManager().getPlayerTeam(teamUUID);
        FTBChunksTeamData teamData = claimedChunkManager.getData(team);
        teamData.unclaim(source, chunkDimPos, false);
    }

    public void unForceLoadChunk(CommandSourceStack source, BlockPos pos, UUID teamUUID, GatewayEntity gatewayEntity) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = gatewayEntity.level.dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        Team team = FTBTeamsAPI.getManager().getPlayerTeam(teamUUID);
        FTBChunksTeamData teamData = claimedChunkManager.getData(team);
        teamData.unload(source, chunkDimPos, false);
    }

    public void processClaim(CommandSourceStack source, ServerPlayer player, BlockPos pos, GatewayEntity gatewayEntity) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = player.level.dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        FTBChunksTeamData teamData = claimedChunkManager.getData(player);
        if (!teamData.claim(source, chunkDimPos, false).equals(ClaimResults.ALREADY_CLAIMED)) {
            teamData.load(source, chunkDimPos, false);
            gatewayEntity.getPersistentData().putString("CleanUpAction", "full");
            gatewayEntity.getPersistentData().putUUID("UnClaimUUID", teamData.getTeamId());
        } else {
            ClaimedChunk chunk = claimedChunkManager.getChunk(chunkDimPos);
            if (chunk != null) {
                FTBChunksTeamData forceLoadTeam = chunk.getTeamData();
                if (!forceLoadTeam.load(source, chunkDimPos, false).equals(ClaimResults.ALREADY_LOADED)) {
                    gatewayEntity.getPersistentData().putString("CleanUpAction", "half");
                    gatewayEntity.getPersistentData().putUUID("UnClaimUUID", forceLoadTeam.getTeamId());
                }
            }
        }
    }
}
