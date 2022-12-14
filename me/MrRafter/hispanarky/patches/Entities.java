package me.MrRafter.hispanarky.patches;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

@RequiredArgsConstructor
public class Entities implements Listener {
    private final Main plugin;

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (plugin.getConfig().getBoolean("DisableFish")) {
//            if (event.getEntity().getType().equals(EntityType.COD) || event.getEntity().getType().equals(EntityType.SALMON)) {
            // Have to use strings or else maven will get pissed at me
            if (event.getEntity().getName().equals("Cod") || event.getEntity().getName().equals("Salmon")) {
                event.setCancelled(true);
            }
        }
    }
}
