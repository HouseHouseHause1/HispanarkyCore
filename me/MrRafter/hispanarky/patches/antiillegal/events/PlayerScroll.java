package me.MrRafter.hispanarky.patches.antiillegal.events;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.patches.antiillegal.util.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerScroll implements Listener {
    ItemUtils itemUtils = new ItemUtils();

    @EventHandler
    public void onItemMove(PlayerItemHeldEvent event) {
        if (Main.getPlugin().getConfig().getBoolean("AntiIllegal.PlayerHotbarMove-Enabled")) {
            Player player = event.getPlayer();
            itemUtils.deleteIllegals(player.getInventory());
        }
    }
}
