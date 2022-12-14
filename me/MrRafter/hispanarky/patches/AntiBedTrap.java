package me.MrRafter.hispanarky.patches;

import me.MrRafter.hispanarky.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AntiBedTrap implements Listener {
    private final Main plugin;
    private final Map<UUID, AtomicInteger> deathCount = new HashMap<>();

    public AntiBedTrap(Main plugin) {
        this.plugin = plugin;

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, deathCount::clear, 0, 30 * 20L);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("AntiBedTrap"))
            return;

        if (event.getEntity().getBedSpawnLocation() != null) {
            deathCount.putIfAbsent(event.getEntity().getUniqueId(), new AtomicInteger());

            if (deathCount.get(event.getEntity().getUniqueId()).incrementAndGet() > plugin.getConfig().getInt("AntiBedTrapDeathAmount")) {
                event.getEntity().setBedSpawnLocation(null);
            }
        }
    }
}
