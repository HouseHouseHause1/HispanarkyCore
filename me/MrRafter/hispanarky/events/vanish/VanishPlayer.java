package me.MrRafter.hispanarky.events.vanish;

import me.MrRafter.hispanarky.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class VanishPlayer {
    Main Vanish;

    public VanishPlayer(Main Vanish) {
        this.Vanish = Vanish;
    }

    public void vanishPlayer(Player vanishplayer) {
        Vanish.gamemodelist.put(vanishplayer, vanishplayer.getGameMode());
        vanishplayer.setGameMode(GameMode.CREATIVE);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Vanish.getConfig().getString("quitmessage").replaceAll("%player%", vanishplayer.getDisplayName())));

        Vanish.taskidlist.put(vanishplayer, Bukkit.getScheduler().scheduleSyncRepeatingTask(Vanish, () -> {
            TextComponent actionbar = new TextComponent("You are vanished at the moment!");
            actionbar.setColor(ChatColor.YELLOW);

            vanishplayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionbar);
        }, 0, 20));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != vanishplayer) {
                player.hidePlayer(Vanish, vanishplayer);
            }
        }
    }

    public void unvanishPlayer(Player unvanishplayer) {
        unvanishplayer.setGameMode(Vanish.gamemodelist.get(unvanishplayer));
        Vanish.gamemodelist.remove(unvanishplayer);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Vanish.getConfig().getString("joinmessage").replaceAll("%player%", unvanishplayer.getDisplayName())));

        Bukkit.getScheduler().cancelTask(Vanish.taskidlist.get(unvanishplayer));
        Vanish.taskidlist.remove(unvanishplayer);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != unvanishplayer) {
                player.showPlayer(Vanish, unvanishplayer);
            }
        }
    }
}