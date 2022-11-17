package me.MrRafter.hispanarky.events.vanish;

import me.MrRafter.hispanarky.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {
    Main vanish;

    public PlayerEvents(Main vanish) {
        this.vanish = vanish;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (Player vanishedPlayer : vanish.gamemodelist.keySet()) {
            player.hidePlayer(vanish, vanishedPlayer);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (vanish.isVanished(player)) {
            event.setQuitMessage(null);

            vanish.vanishPlayer.unvanishPlayer(player);
        }
    }
}
