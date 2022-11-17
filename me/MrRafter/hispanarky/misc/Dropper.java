package me.MrRafter.hispanarky.misc;

import me.MrRafter.hispanarky.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockDispenseEvent;
import java.util.ArrayList;
import org.bukkit.Location;
import java.util.List;
import org.bukkit.event.Listener;

public class Dropper implements Listener
{
    private List<Location> cldown;
    
    public Dropper() {
        this.cldown = new ArrayList<Location>();
    }
    
    @EventHandler
    public void onDispenser(final BlockDispenseEvent e) {
        if (!(e.getBlock().getState() instanceof org.bukkit.block.Dropper)) {
            return;
        }
        final Location _l = e.getBlock().getLocation();
        if (this.cldown.contains(_l)) {
            return;
        }
        if (me.MrRafter.hispanarky.misc.Util.chanceOf()) {
            this.cldown.add(_l);
            e.getBlock().getWorld().dropItem(_l, e.getItem());
            Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) Main.getPlugin(), () -> this.cldown.remove(_l), 20L * Main.getPlugin().getConfig().getInt("settings.cooldown"));
        }
    }
}
