package me.MrRafter.hispanarky.events;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Random;


public class MotdEvent implements Listener {
    private final Main plugin;

    public MotdEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPing(ServerListPingEvent e){
        if(Utils.getConfig().getBoolean("motd.enabled")){
            int max = Utils.getConfig().getStringList("motd.motd-list").size();
            int rand = new Random().nextInt(max);
            e.setMotd(Utils.getConfig().getStringList("motd.motd-list").get(rand));
        }
    }
}
