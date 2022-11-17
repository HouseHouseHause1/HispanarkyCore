package me.MrRafter.hispanarky.patches.antiillegal.events;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.patches.antiillegal.util.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class InventoryOpen implements Listener {
    ItemUtils utils = new ItemUtils();

    @EventHandler
    @AntiIllegal(EventName = "InventoryCloseEvent")
    public void onInventoryClose(InventoryOpenEvent event) {
        if (Main.getPlugin().getConfig().getBoolean("AntiIllegal.InventoryOpen-Enabled")) {
            Inventory inv = event.getInventory();
            utils.deleteIllegals(inv);
        }
    }
}