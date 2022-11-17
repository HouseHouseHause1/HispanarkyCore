package me.MrRafter.hispanarky.misc;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.awt.*;

@RequiredArgsConstructor
public class CommandPreProcess implements Listener {
    private final Main plugin;

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent evt) {
        FileConfiguration config = plugin.getConfig();

        String msg = evt.getMessage().toLowerCase();
        if (config.getBoolean("AllowOPStobypass")) {
            if (config.getBoolean("CommandWhitelist") && !config.getList("CommandsWhitelisted").contains(msg.split(" ")[0])) {
                if (!evt.getPlayer().isOp()) {
                    if (evt.getPlayer().getLocale().startsWith("es")) {
                        evt.getPlayer().sendMessage(ChatColor.DARK_RED + "Mal comando. Tipo /help para todos los comandos.");
                    } else {
                        evt.getPlayer().sendMessage(ChatColor.DARK_RED + "Bad command. Type /help for all commands.");
                    }
                    evt.setCancelled(true);
                }
            }
        } else {
            if (config.getBoolean("CommandWhitelist") && !config.getList("CommandsWhitelisted").contains(msg.split(" ")[0])) {
                if (evt.getPlayer().getLocale().startsWith("es")) {
                    evt.getPlayer().sendMessage(ChatColor.DARK_RED + "Mal comando. Tipo /help para todos los comandos.");
                } else {
                    evt.getPlayer().sendMessage(ChatColor.DARK_RED + "Bad command. Type /help for all commands.");
                }

                evt.setCancelled(true);
            }
        }
    }
}
