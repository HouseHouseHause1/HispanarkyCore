package me.MrRafter.hispanarky.events;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.commands.ToggleJoinMessages;
import me.MrRafter.hispanarky.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {
    private final Main plugin;

    public LeaveEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(Utils.getConfig().getBoolean("join-messages.enabled")){
            e.setQuitMessage("");
            sendLeaveMessage(e.getPlayer());
        }
    }

    public static void sendLeaveMessage(Player quit){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(!ToggleJoinMessages.muted.contains(p)){
                Utils.sendRawMessage(p, Utils.getConfig().getString("join-messages.leave").replaceAll("%player%", quit.getName()));
            }
        }
    }
}
