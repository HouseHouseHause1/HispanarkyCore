package me.MrRafter.hispanarky.patches;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

@RequiredArgsConstructor
public class EndPortalPatch implements Listener {
    private final Main plugin;

    @EventHandler
    private void onBlockDispense(BlockDispenseEvent evt) {
        if (plugin.getConfig().getBoolean("DestroyingEndPortalsPatch")) {
            if (evt.getItem().getType().equals(Material.WATER_BUCKET) || evt.getItem().getType().equals(Material.LAVA_BUCKET)) {
                Block b = evt.getBlock();
                World w = evt.getBlock().getWorld();
                if (w.getBlockAt(b.getX(), b.getY() - 1, b.getZ()).getType() == Material.ENDER_PORTAL || w.getBlockAt(b.getX(), b.getY() + 1, b.getZ()).getType() == Material.ENDER_PORTAL) {
                    evt.setCancelled(true);
                    plugin.getLogger().info("Patch a dispenser from destroying a portal!");
                }
            }
        }
    }

    @EventHandler
    private void onPlayerBucketEvent(PlayerBucketEmptyEvent evt) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("DestroyingEndPortalsPatch")) {
            String playerName = evt.getPlayer().getName();
            Material type = evt.getBlockClicked().getType();
            BlockFace face = evt.getBlockFace();
            World world = evt.getBlockClicked().getWorld();

            final boolean isAround = face == BlockFace.NORTH || face == BlockFace.EAST || face == BlockFace.SOUTH || face == BlockFace.WEST;

            if (type == Material.BEDROCK && world.getEnvironment().equals(World.Environment.THE_END)) {
                if (isAround) {
                    evt.setCancelled(true);
                    plugin.getLogger().info("Patch " + playerName + " from destroying a portal!");
                }
            }

            if (type == Material.ENDER_PORTAL_FRAME) {
                if (isAround) {
                    evt.setCancelled(true);
                    plugin.getLogger().info("Patch " + playerName + " from destroying a portal!");
                }
            }

            if (face == BlockFace.UP || face == BlockFace.DOWN) {
                World w = evt.getPlayer().getWorld();
                Block b = evt.getBlockClicked();
                if (w.getBlockAt(b.getX(), b.getY() - 1, b.getZ()).getType() == Material.ENDER_PORTAL || w.getBlockAt(b.getX(), b.getY() + 1, b.getZ()).getType() == Material.ENDER_PORTAL) {
                    evt.setCancelled(true);
                    plugin.getLogger().info("Patch " + playerName + " from destroying a portal!");
                }
            }
        }
    }
}
