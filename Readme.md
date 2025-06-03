# 🌌 Gateway to Eternity - Random Gate Spawning

This mod adds **random spawning mechanics** for gates from the [Gateways to Eternity](https://www.curseforge.com/minecraft/mc-mods/gateways-to-eternity) mod.  
It is designed to add suspense and excitement by making powerful gateways appear unexpectedly during gameplay.

---

## 🧱 Requirements

This mod depends on the following mods to function:

- [FTB Chunks](https://www.curseforge.com/minecraft/mc-mods/ftb-chunks)
- [FTB Library](https://www.curseforge.com/minecraft/mc-mods/ftb-library)
- [FTB Teams](https://www.curseforge.com/minecraft/mc-mods/ftb-teams)
- [Gateways to Eternity](https://www.curseforge.com/minecraft/mc-mods/gateways-to-eternity)

> Optional:
- [JourneyMap](https://www.curseforge.com/minecraft/mc-mods/journeymap) – adds automatic waypoints for spawned gates

---

## ⚙️ How It Works

- Every **Minecraft day at sunrise** (`time == 0`), the mod evaluates whether to spawn a gateway.
- The mod selects a **random player in the Overworld** and obtains their current position.
- From the player's position, the mod randomly chooses a **surface block** between **50 to 300 blocks away** as the potential spawn location for the gate.
- The actual spawning happens at a **random time during the day**, not necessarily exactly at sunrise.
- The **spawn chance increases** gradually with each missed attempt:
    - 20% → 40% → 60% → 80%
    - After a successful spawn, the chance resets to 20%.
- Once a gate has spawned:
    - It **prevents another spawn the next day** to avoid over-spawning.
- A **notification** is shown when a gate is about to appear.
- If **JourneyMap** is installed:
    - A **waypoint** is automatically created at the gate's location.
- A **beam of light** marks the spawn position using **FTB Map**.
- The chunk where the gate spawns is **force-loaded** using **FTB Chunks**, ensuring proper gate generation.

---

## ⚙️ Config Options
🧪 **Debug Mode**  
Disable `debug_mode` in the config to remove debug logs.

💬 **Notes**  
It does **not** modify the Gateways to Eternity mod itself — only adds random spawning behavior externally.