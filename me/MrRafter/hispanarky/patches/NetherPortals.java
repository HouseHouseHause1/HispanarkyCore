package me.MrRafter.hispanarky.patches;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

@RequiredArgsConstructor
public class NetherPortals implements Listener {
    private final Main plugin;

    @EventHandler
    private void onPortal(EntityPortalEvent evt) {
        if (plugin.getConfig().getBoolean("PatchFireworksInPortals")) {
            if (evt.getEntity().getType().equals(EntityType.FIREWORK)) {
                evt.setCancelled(true);
            }
        }
        if (plugin.getConfig().getBoolean("PatchDropppedItemsInPortals")) {
            if (evt.getEntity().getType().equals(EntityType.DROPPED_ITEM)) {
                evt.setCancelled(true);
            }
        }
        if (plugin.getConfig().getBoolean("PatchAllEntitiesInPortals")) {
            evt.setCancelled(true);
        }
    }

    @EventHandler
    private void onPortal(PlayerPortalEvent evt) {
        if (plugin.getConfig().getBoolean("PatchPortalTraps")) {
            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location l = evt.getPlayer().getLocation();
                if (l.getWorld().getBlockAt(l).getType() == Material.PORTAL) {
                    evt.getPlayer().teleport(evt.getFrom());
                    evt.getPlayer().playSound(evt.getPlayer().getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0F, 1.0F);
                }
            }, 300L);
        }
    }
}
