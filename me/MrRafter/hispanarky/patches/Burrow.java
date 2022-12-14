package me.MrRafter.hispanarky.patches;

import me.MrRafter.hispanarky.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Burrow implements Listener {
    private final Main plugin;

    public Burrow(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onMove(PlayerMoveEvent evt) {
        Location l = evt.getPlayer().getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        double yy = l.getY();
        int z = l.getBlockZ();
        if (plugin.getConfig().getBoolean("AntiBurrow") && evt.getPlayer().getGameMode() != GameMode.SPECTATOR && (plugin.getConfig().getBoolean("PreventBurrowIfBlockAbove") || evt.getPlayer().getLocation().getWorld().getBlockAt(x, y + 1, z).getType().equals(Material.AIR))) {
            Material b = evt.getPlayer().getLocation().getWorld().getBlockAt(x, y, z).getType();
            Block bb = evt.getPlayer().getLocation().getWorld().getBlockAt(x, y, z);
            if (!b.equals(Material.AIR) && !b.equals(Material.SOUL_SAND) && b.isOccluding() && !isGravityBlock(b)) {
                if (!b.equals(Material.DOUBLE_STEP) || !plugin.getConfig().getBoolean("AllowSlabsInBurrow")) {
                    evt.getPlayer().damage(plugin.getConfig().getInt("BurrowDamageWhenMoving"));
                    if (plugin.getConfig().getBoolean("TeleportBurrow")) {
                        evt.getPlayer().teleport(new Location(l.getWorld(), x, y + 1, z));
                    }
                }
            }

            switch (b) {
                case ENDER_CHEST:
                case SOUL_SAND: {
                    if (yy - y < 0.875) {
                        evt.getPlayer().damage(plugin.getConfig().getInt("BurrowDamageWhenMoving"));
                        if (plugin.getConfig().getBoolean("TeleportBurrow")) {
                            evt.getPlayer().teleport(new Location(l.getWorld(), x, y + 1, z));
                        }
                    }
                    break;
                }
                case ENCHANTMENT_TABLE: {
                    if (yy - y < 0.75) {
                        evt.getPlayer().damage(plugin.getConfig().getInt("BurrowDamageWhenMoving"));
                        if (plugin.getConfig().getBoolean("TeleportBurrow")) {
                            evt.getPlayer().teleport(new Location(l.getWorld(), x, y + 1, z));
                        }
                    }
                    break;
                }
                case ANVIL: {
                    evt.getPlayer().damage(plugin.getConfig().getInt("BurrowDamageWhenMoving"));
                    if (plugin.getConfig().getBoolean("TeleportBurrow") && !plugin.getConfig().getBoolean("BreakAnvilInsteadOfTeleport")) {
                        evt.getPlayer().teleport(new Location(l.getWorld(), x, y + 1, z));
                    }
                    if (plugin.getConfig().getBoolean("BreakAnvilInsteadOfTeleport")) {
                        bb.breakNaturally();
                    }
                    break;
                }
                case BEDROCK:
                case BEACON: {
                    evt.getPlayer().damage(plugin.getConfig().getInt("BurrowDamageWhenMoving"));
                    if (plugin.getConfig().getBoolean("TeleportBurrow")) {
                        evt.getPlayer().teleport(new Location(l.getWorld(), x, y + 1, z));
                    }
                    break;
                }

            }
        }
    }

    private Boolean isGravityBlock(Material b) {
        switch (b) {
            case SAND:
            case GRAVEL:
                return true;
            default:
                return false;
        }
    }
}
