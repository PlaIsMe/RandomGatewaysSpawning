package com.pla.plagatesummon;

import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.Waypoint;
import dev.ftb.mods.ftbchunks.client.map.WaypointManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public class WaypointHelper {
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

    public static void removeWaypoint(BlockPos pos) {
        MapDimension dimension = MapDimension.getCurrent();
        assert dimension != null;
        if (!dimension.dimension.equals(Level.OVERWORLD)) {
            return;
        }
        WaypointManager waypointManager = dimension.getWaypointManager();
        Waypoint waypoint = new Waypoint(dimension, pos.getX(), pos.getY(), pos.getZ());
        waypointManager.remove(waypoint);
    }
}
