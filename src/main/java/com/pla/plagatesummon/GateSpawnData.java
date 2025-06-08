package com.pla.plagatesummon;

import com.mojang.datafixers.DataFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;

public class GateSpawnData extends SavedData {
    private static final String DATA_NAME = "gate_spawn_data";

    public int nextSpawnTick = -1;
    public boolean shouldSpawnToday = false;
    public BlockPos spawnPos = null;
    public BlockPos oldSpawnPos = null;
    public boolean isPromptPlayer = false;
    public String mainMessage = "";
    public int hexColor = 0;
    public String randomGate = "";
    public String subMessage = "";
    public String waypointName = "";
    public boolean skippedToday = false;
    public int spawnChance = Config.SPAWN_RATE.get();
    public int dayPassed = 1;
    public String unClaimUUID = "";

    public GateSpawnData() {
    }

    public static final SavedData.Factory<GateSpawnData> FACTORY = new SavedData.Factory<>(
            GateSpawnData::new,
            GateSpawnData::load,
            DataFixTypes.LEVEL
    );

    public static GateSpawnData load(CompoundTag nbt, HolderLookup.Provider provider) {
        GateSpawnData data = new GateSpawnData();
        data.nextSpawnTick = nbt.getInt("NextSpawnTick");
        data.shouldSpawnToday = nbt.getBoolean("ShouldSpawnToday");
        if (nbt.contains("SpawnPos")) {
            int[] pos = nbt.getIntArray("SpawnPos");
            if (pos.length == 3) {
                data.spawnPos = new BlockPos(pos[0], pos[1], pos[2]);
            } else {
                data.spawnPos = null;
            }
        } else {
            data.spawnPos = null;
        }
        if (nbt.contains("OldSpawnPos")) {
            int[] pos = nbt.getIntArray("OldSpawnPos");
            if (pos.length == 3) {
                data.oldSpawnPos = new BlockPos(pos[0], pos[1], pos[2]);
            } else {
                data.oldSpawnPos = null;
            }
        } else {
            data.oldSpawnPos = null;
        }
        data.isPromptPlayer = nbt.getBoolean("IsPromptPlayer");
        data.mainMessage = nbt.getString("MainMessage");
        data.hexColor = nbt.getInt("HexColor");
        data.randomGate = nbt.getString("RandomGate");
        data.subMessage = nbt.getString("SubMessage");
        data.waypointName = nbt.getString("WaypointName");
        data.skippedToday = nbt.getBoolean("SkippedToday");
        data.spawnChance = nbt.getInt("SpawnChance");
        data.dayPassed = nbt.getInt("DayPassed");
        data.unClaimUUID = nbt.getString("UnClaimUUID");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putInt("NextSpawnTick", nextSpawnTick);
        nbt.putBoolean("ShouldSpawnToday", shouldSpawnToday);
        if (spawnPos != null) {
            nbt.putIntArray("SpawnPos", new int[]{spawnPos.getX(), spawnPos.getY(), spawnPos.getZ()});
        }
        if (oldSpawnPos != null) {
            nbt.putIntArray("OldSpawnPos", new int[]{oldSpawnPos.getX(), oldSpawnPos.getY(), oldSpawnPos.getZ()});
        }
        nbt.putBoolean("IsPromptPlayer", isPromptPlayer);
        nbt.putString("MainMessage", mainMessage);
        nbt.putInt("HexColor", hexColor);
        nbt.putString("RandomGate", randomGate);
        nbt.putString("SubMessage", subMessage);
        nbt.putString("WaypointName", waypointName);
        nbt.putBoolean("SkippedToday", skippedToday);
        nbt.putInt("SpawnChance", spawnChance);
        nbt.putInt("DayPassed", dayPassed);
        nbt.putString("UnClaimUUID", unClaimUUID);
        return nbt;
    }

    public static GateSpawnData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
    }
}
