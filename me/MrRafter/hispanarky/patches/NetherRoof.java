package me.MrRafter.hispanarky.patches;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@RequiredArgsConstructor
public class NetherRoof implements Listener {
    private final Main plugin;

    @EventHandler
    public void onTeleport(PlayerTeleportEvent evt) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("PatchNetherRoof")) {
            if (evt.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                if (!evt.getPlayer().hasPermission("hispanarkycore.netherroofbypass")) {
                    if (evt.getFrom().getY() < 128 && evt.getTo().getY() >= 128) {
                        evt.getPlayer().teleport(evt.getFrom());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("PatchNetherRoof")) {
            if (!evt.getPlayer().hasPermission("hispanarkycore.netherroofbypass") && evt.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                if (evt.getTo().getY() > 127 || evt.getPlayer().getLocation().getY() > 127) {

                    evt.getPlayer().teleport(new Location(evt.getPlayer().getWorld(), evt.getPlayer().getLocation().getX(), 120, evt.getPlayer().getLocation().getZ()));

                    if (evt.getPlayer().isInsideVehicle()) {
                        evt.getPlayer().leaveVehicle();
                    }

                    if (evt.getPlayer().isGliding()) {
                        evt.getPlayer().setGliding(false);
                    }
                }
            }

        }
    }
}
