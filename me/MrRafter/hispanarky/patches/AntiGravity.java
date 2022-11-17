package me.MrRafter.hispanarky.patches;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.utils.TPS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Arrays;
import java.util.List;

public class AntiGravity implements Listener {
    private final Main plugin;

    public AntiGravity(Main plugin) {
        this.plugin = plugin;
    }

    private final List<Material> blocks = Arrays.asList(Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER);

    @EventHandler
    public void Liquid(BlockFromToEvent e) {
        if(plugin.getConfig().getBoolean("AntiGravity.enabled")) {
            Block block = e.getBlock();
            if(!blocks.contains(block.getType()))
                return;
            double tps = TPS.getTPS();
            if (tps <= plugin.getConfig().getInt("AntiGravity.disabletps")) {
                e.setCancelled(true);
            }
        }
    }
}
