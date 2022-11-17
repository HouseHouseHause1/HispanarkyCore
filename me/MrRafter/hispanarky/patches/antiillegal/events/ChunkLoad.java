package me.MrRafter.hispanarky.patches.antiillegal.events;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.patches.antiillegal.util.ItemUtils;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkLoad implements Listener {
    ItemUtils itemUtils = new ItemUtils();

    @EventHandler
    @AntiIllegal(EventName = "ChunkLoadEvent")
    public void onLoad(ChunkLoadEvent event) {
        if (Main.getPlugin().getConfig().getBoolean("AntiIllegal.ChunkLoad-Enabled")) {
            for (BlockState state : event.getChunk().getTileEntities()) {
                if (state instanceof Container) {
                    Container container = (Container) state;
                    itemUtils.deleteIllegals(container.getInventory());

                }
            }
        }
    }
}