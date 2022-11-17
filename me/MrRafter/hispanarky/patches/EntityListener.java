package me.MrRafter.hispanarky.patches;

import me.MrRafter.hispanarky.Main;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityListener implements Listener {
    private Main plugin;

    public EntityListener(Main plugin) {
        this.plugin = plugin;
    }

    private Main getPlugin() {
        return this.plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.ARMOR_STAND) {
            ArmorStand stand = (ArmorStand)entity;
            if (this.getPlugin().getConfig().getBoolean("armorstand.settings.betterchairs")) {
                this.getPlugin().getServer().getScheduler().runTaskLater(this.plugin, () -> {
                    if (stand.isVisible()) {
                        entity.setGravity(false);
                    }

                }, 1L);
            } else {
                entity.setGravity(false);
            }
        }

    }
}
