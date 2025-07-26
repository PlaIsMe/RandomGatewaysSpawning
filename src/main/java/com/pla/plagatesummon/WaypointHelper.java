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
        if (dimension == null) {
            LOGGER.warn("PlaGateSummon: No current map dimension available, createWaypoint will be skipped");
            return;
        }
        if (!dimension.dimension.equals(Level.OVERWORLD)) {
            return;
        }

        WaypointManager waypointManager = dimension.getWaypointManager();
        CustomWaypoint waypoint = new CustomWaypoint(dimension, pos.getX(), pos.getY(), pos.getZ());
        waypoint.update();
        waypoint.setName(name);
        waypoint.setColor(hexColor);
        waypointManager.add(waypoint);
    }

    public static void removeWaypoint(String name) {
        MapDimension dimension = MapDimension.getCurrent();
        if (dimension == null) {
            LOGGER.warn("PlaGateSummon: No current map dimension available, removeWaypoint will be skipped");
            return;
        }
        if (!dimension.dimension.equals(Level.OVERWORLD)) {
            return;
        }

        WaypointManager waypointManager = dimension.getWaypointManager();
        waypointManager.removeIf(waypoint -> waypoint.name.equals(name));
    }
}
