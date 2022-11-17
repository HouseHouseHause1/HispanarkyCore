package me.MrRafter.hispanarky.patches;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

@RequiredArgsConstructor
public class BowBomb implements Listener {
    private final Main plugin;

    @EventHandler
    public void onArrow(ProjectileLaunchEvent evt) {
        if (plugin.getConfig().getBoolean("AntiBowBomb")) {
            if (evt.getEntity() instanceof Arrow || evt.getEntity() instanceof SpectralArrow) {
                if (evt.getEntity().getVelocity().lengthSquared() > plugin.getConfig().getInt("MaxBowSquared")) {
                    evt.setCancelled(true);
                }
            }
        }
    }
}
