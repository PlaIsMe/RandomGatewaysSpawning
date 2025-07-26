package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbchunks.data.ClaimResult;
import dev.ftb.mods.ftbchunks.data.ClaimResults;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkManager;
import dev.ftb.mods.ftbchunks.data.FTBChunksTeamData;
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

    public void unClaimChunk(CommandSourceStack source, ServerPlayer pPlayer, BlockPos pos, String unClaimUUID) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = pPlayer.level.dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        UUID uuid = (!unClaimUUID.isEmpty() ? UUID.fromString(unClaimUUID) : pPlayer.getUUID());
        Team team = FTBTeamsAPI.getManager().getPlayerTeam(uuid);
        FTBChunksTeamData teamData = claimedChunkManager.getData(team);
        teamData.unclaim(source, chunkDimPos, false);
    }

    public void unForceLoadChunk(CommandSourceStack source, ServerPlayer pPlayer, BlockPos pos, String unClaimUUID) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = pPlayer.level.dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        UUID uuid = (!unClaimUUID.isEmpty() ? UUID.fromString(unClaimUUID) : pPlayer.getUUID());
        Team team = FTBTeamsAPI.getManager().getPlayerTeam(uuid);
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
        } else {
            if (!teamData.load(source, chunkDimPos, false).equals(ClaimResults.ALREADY_LOADED)) {
                gatewayEntity.getPersistentData().putString("CleanUpAction", "half");
            }
        }
    }
}
