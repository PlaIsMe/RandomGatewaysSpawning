package com.pla.plagatesummon;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkImpl;
import dev.ftb.mods.ftbchunks.data.ChunkTeamDataImpl;
import dev.ftb.mods.ftbchunks.data.ClaimedChunkManagerImpl;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ClaimChunkHelper {
    private static ClaimChunkHelper instance;
    private final ClaimedChunkManagerImpl claimedChunkManager;

    public ClaimChunkHelper(ClaimedChunkManagerImpl claimedChunkManager) throws CommandSyntaxException {
        this.claimedChunkManager = claimedChunkManager;
    }

    public static synchronized ClaimChunkHelper getInstance(MinecraftServer server) throws CommandSyntaxException {
        if (instance == null) {
            instance = new ClaimChunkHelper((ClaimedChunkManagerImpl) FTBChunksAPI.api().getManager());
        }
        return instance;
    }

    public void claimChunk(ServerPlayer player, BlockPos pos) throws CommandSyntaxException {
        ChunkPos chunkPos = new ChunkPos(pos);
        ResourceKey<Level> dimension = player.level().dimension();
        ChunkDimPos chunkDimPos = new ChunkDimPos(dimension, chunkPos.x, chunkPos.z);

        ChunkTeamDataImpl teamData = claimedChunkManager.getOrCreateData(player);

        ClaimedChunkImpl claimedChunk = new ClaimedChunkImpl(teamData, chunkDimPos);
        claimedChunk.setClaimedTime(System.currentTimeMillis());
        claimedChunk.setForceLoadedTime(System.currentTimeMillis());
        claimedChunkManager.registerClaim(chunkDimPos, claimedChunk);
        claimedChunk.sendUpdateToAll();
    }

    public void unClaimChunk(CommandSourceStack source, ServerPlayer pPlayer) {
        // FIXME: Don't know how to unclaim a pos so let's unclaim everything
        String unclaimCommand = "ftbchunks admin unclaim_everything";
        try {
            Objects.requireNonNull(pPlayer.getServer()).getCommands().getDispatcher().execute(unclaimCommand, source);
        } catch (CommandSyntaxException e) {
        }
    }
}
