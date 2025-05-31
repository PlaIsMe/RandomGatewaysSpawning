# 🌌 Gateway to Eternity - Random Gate Spawning

This mod adds **random spawning mechanics** for gates from the [Gateways to Eternity](https://www.curseforge.com/minecraft/mc-mods/gateways-to-eternity) mod.  
It is designed to add suspense and excitement by making powerful gateways appear unexpectedly during gameplay.

---

## 🧱 Requirements

This mod depends on the following mods to function:

- [FTB Chunks](https://www.curseforge.com/minecraft/mc-mods/ftb-chunks)
- [FTB Library](https://www.curseforge.com/minecraft/mc-mods/ftb-library)
- [FTB Teams](https://www.curseforge.com/minecraft/mc-mods/ftb-teams)
- [FTB Ranks](https://www.curseforge.com/minecraft/mc-mods/ftb-ranks)
- [Architectury API](https://www.curseforge.com/minecraft/mc-mods/architectury-api)
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

### 🔄 `auto_claim` (default: `true`)

- When enabled, the mod automatically claims and force-loads the chunk where the gate spawns using **FTB Chunks**.
- This ensures the gate is kept active even if no players are nearby.

⚠️ **Warning:** In some cases, automatically force-loading chunks may cause temporary lag or game freezes on weaker servers.  
If this happens, you can **disable** the option in the config:

```toml
auto_claim = false
```
**But keep in mind:**

If `auto_claim` is disabled, the spawned gate may unload if no players are nearby, which can cause problems with gate functionality or rendering.

⚠️ **Caution: `auto_unclaim` Clears All Claimed Chunks**  
By default, this mod clears *all* of your claimed chunks when unclaiming, because currently it does not support unclaiming a specific chunk only.

If you want to prevent this, you can disable the automatic unclaiming in the config:

```toml
auto_unclaim = false
```
If you disable `auto_unclaim`, you must manually unclaim the spawned gate's chunk later if needed (using FTB Chunks UI or commands).

🧪 **Debug Mode**  
Disable `debug_mode` in the config to remove debug logs.

💬 **Notes**  
It does **not** modify the Gateways to Eternity mod itself — only adds random spawning behavior externally.