package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbchunks.data.ClaimedChunk;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkManager;
import dev.ftb.mods.ftbchunks.data.FTBChunksTeamData;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Objects;

import static dev.ftb.mods.ftbchunks.data.FTBChunksAPI.getManager;

public class ClaimChunkHelper {
    private static ClaimChunkHelper instance;
    private final ClaimedChunkManager claimedChunkManager;

    public ClaimChunkHelper(ClaimedChunkManager claimedChunkManager) throws CommandSyntaxException {
        this.claimedChunkManager = claimedChunkManager;
    }

    public static synchronized ClaimChunkHelper getInstance(MinecraftServer server) throws CommandSyntaxException {
        if (instance == null) {
            instance = new ClaimChunkHelper(getManager());
        }
        return instance;
    }

    public void claimChunk(ServerPlayer player, BlockPos pos) throws CommandSyntaxException {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = player.level.dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        FTBChunksTeamData teamData = claimedChunkManager.getData(player);

        ClaimedChunk claimedChunk = new ClaimedChunk(teamData, chunkDimPos);
        claimedChunk.setClaimedTime(System.currentTimeMillis());
        claimedChunkManager.registerClaim(chunkDimPos, claimedChunk);
        claimedChunk.sendUpdateToAll();
    }

    public void unClaimChunk(CommandSourceStack source, ServerPlayer pPlayer) {
        String unclaimCommand = "ftbchunks admin unclaim_everything";
        try {
            Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(unclaimCommand, source);
        } catch (CommandSyntaxException e) {
        }
    }
}
