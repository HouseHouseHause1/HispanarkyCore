package me.MrRafter.hispanarky.patches;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

@RequiredArgsConstructor
public class InvalidNames implements Listener {
    private final Main plugin;

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent evt) {
        if (plugin.getConfig().getBoolean("PatchInvalidNames")) {
            if (!evt.getName().matches("[A-Za-z0-9_]+") || evt.getName().length() < 1 || evt.getName().length() > 16) {
                plugin.getLogger().warning("Invalid Username Join: " + evt.getName());
                evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Invalid Username");
            }
        }
    }
}
