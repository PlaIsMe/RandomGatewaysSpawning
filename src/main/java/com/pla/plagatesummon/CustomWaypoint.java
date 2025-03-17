package com.pla.plagatesummon;

import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftbchunks.client.map.Waypoint;

public class CustomWaypoint extends Waypoint {

    public CustomWaypoint(MapDimension d, int x, int y, int z) {
        super(d, x, y, z);
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
