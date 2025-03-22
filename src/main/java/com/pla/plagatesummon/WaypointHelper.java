package com.pla.plagatesummon;

import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.WaypointManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;


public class WaypointHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void createWaypoint(BlockPos pos, String name, int hexColor) {
        MapDimension dimension = MapDimension.getCurrent();
        assert dimension != null;
        if (!dimension.dimension.equals(Level.OVERWORLD)) {
            return;
        }

        WaypointManager waypointManager = dimension.getWaypointManager();
        CustomWaypoint waypoint = new CustomWaypoint(dimension, pos.getX(), pos.getY(), pos.getZ());
        waypoint.setName(name);
        waypoint.setColor(hexColor);
        waypointManager.add(waypoint);
    }

    public static void removeWaypoint(BlockPos pos, String name, int hexColor) {
        MapDimension dimension = MapDimension.getCurrent();
        assert dimension != null;
        if (!dimension.dimension.equals(Level.OVERWORLD)) {
            return;
        }

        WaypointManager waypointManager = dimension.getWaypointManager();

        boolean removed = waypointManager.removeIf(waypoint -> waypoint.name.equals(name));
        if (removed) {
            LOGGER.info("PlaGateSummon: Removed waypoint with name: {}, hexColor: {}", name, hexColor);
        } else {
            LOGGER.warn("PlaGateSummon: No matching waypoint found for name: {}, hexColor: {}", name, hexColor);
        }
    }
}
