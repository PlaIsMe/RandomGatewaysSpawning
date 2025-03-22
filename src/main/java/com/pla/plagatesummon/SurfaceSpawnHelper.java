package com.pla.plagatesummon;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

public class SurfaceSpawnHelper {
    private static final Random random = new Random();

    public static BlockPos findRandomSurfacePos(ServerLevel world, BlockPos playerPos, int minRange, int maxRange) {
        Random random = new Random();

        int offsetX = minRange + random.nextInt(maxRange - minRange) * (random.nextBoolean() ? 1 : -1);
        int offsetZ = minRange + random.nextInt(maxRange - minRange) * (random.nextBoolean() ? 1 : -1);

        int x = playerPos.getX() + offsetX;
        int z = playerPos.getZ() + offsetZ;

        BlockPos surfacePos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z));

        if (!isChunkLoaded(world, surfacePos)) {
            return new BlockPos(x, 0, z);
        }

        return moveToSurface(world, surfacePos);
    }

    public static boolean isChunkLoaded(ServerLevel world, BlockPos surfacePos) {
        return surfacePos.getY() > world.getMinBuildHeight();
    }

    public static BlockPos moveToSurface (ServerLevel world, BlockPos surfacePos) {
        while (!world.canSeeSky(surfacePos) && surfacePos.getY() < world.getMaxBuildHeight()) {
            surfacePos = surfacePos.above();
        }

        // If surface is too high (not valid), move down to the last solid block
        while (world.getBlockState(surfacePos.below()).isAir() && surfacePos.getY() > world.getMinBuildHeight()) {
            surfacePos = surfacePos.below();
        }

        return  surfacePos;
    }
}
