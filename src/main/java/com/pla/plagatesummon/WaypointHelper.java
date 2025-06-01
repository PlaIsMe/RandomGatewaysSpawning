package com.pla.plagatesummon;

import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.WaypointImpl;
import dev.ftb.mods.ftbchunks.client.map.WaypointManagerImpl;
import dev.ftb.mods.ftbchunks.client.map.WaypointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.util.Optional;


public class WaypointHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void createWaypoint(BlockPos pos, String name, int hexColor) {
        Optional<MapDimension> dimension = MapDimension.getCurrent();
        assert dimension.isPresent();
        if (!dimension.get().dimension.equals(Level.OVERWORLD)) {
            return;
        }

        WaypointManagerImpl waypointManager = dimension.get().getWaypointManager();
        WaypointImpl waypoint = new WaypointImpl(WaypointType.DEFAULT, dimension.get(), pos);
        waypoint.refreshIcon();
        waypoint.setName(name);
        waypoint.setColor(hexColor);
        waypointManager.add(waypoint);
    }

    public static void removeWaypoint(BlockPos pos, String name, int hexColor) {
        Optional<MapDimension> dimension = MapDimension.getCurrent();
        assert dimension.isPresent();
        if (!dimension.get().dimension.equals(Level.OVERWORLD)) {
            return;
        }

        WaypointManagerImpl waypointManager = dimension.get().getWaypointManager();

        boolean removed = waypointManager.removeIf(waypoint -> waypoint.getName().equals(name));
        if (removed) {
            LOGGER.info("PlaGateSummon: Removed waypoint with name: {}, hexColor: {}", name, hexColor);
        } else {
            LOGGER.warn("PlaGateSummon: No matching waypoint found for name: {}, hexColor: {}", name, hexColor);
        }
    }
}
