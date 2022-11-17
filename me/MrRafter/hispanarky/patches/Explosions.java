package me.MrRafter.hispanarky.patches;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.utils.TPS;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class Explosions implements Listener {
    private final Main plugin;

    public Explosions(Main plugin) {
        this.plugin = plugin;
    }

    public void onPrime(ExplosionPrimeEvent e) {
        if (plugin.getConfig().getBoolean("explosions.enabled")) {
            e.setCancelled(true);
        }
    }

    public void onExplode(EntityExplodeEvent e) {
        if (plugin.getConfig().getBoolean("explosions.disableattps")) {
            double tps = TPS.getTPS();
            if (tps >= plugin.getConfig().getInt("explosions.tps")) {
                e.setCancelled(true);
            }
        }
        if (plugin.getConfig().getBoolean("explosions.enabled")) {
            e.setCancelled(true);
        }
    }
}
