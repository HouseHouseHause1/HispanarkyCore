package me.MrRafter.hispanarky.patches;

import java.util.ArrayList;
import java.util.Iterator;

import me.MrRafter.hispanarky.Main;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class BlockListener implements Listener {
  private Main plugin;

  public BlockListener(Main plugin) {
    this.plugin = plugin;
  }

  private Main getPlugin() {
    return this.plugin;
  }

  @EventHandler
  public void onPistonRetrackt(BlockPistonRetractEvent event) {
    if (this.getPlugin().getConfig().getBoolean("armorstand.settings.block-stacking")) {
      int found = 0;
      ArrayList<Entity> remove = new ArrayList();
      Block blocks;
      Iterator var5;
      if (event.getBlocks().size() <= 0) {
        blocks = event.getBlock().getRelative(event.getDirection());
        Iterator var10 = blocks.getWorld().getNearbyEntities(blocks.getLocation(), 0.5D, 0.5D, 0.5D).iterator();

        while(var10.hasNext()) {
          Entity entity = (Entity)var10.next();
          if (entity instanceof ArmorStand) {
            ++found;
            remove.add(entity);
          }
        }
      } else {
        var5 = event.getBlocks().iterator();

        while(var5.hasNext()) {
          blocks = (Block)var5.next();
          Iterator var7 = blocks.getWorld().getNearbyEntities(blocks.getLocation(), 0.5D, 0.5D, 0.5D).iterator();

          while(var7.hasNext()) {
            Entity entity = (Entity)var7.next();
            if (entity instanceof ArmorStand) {
              ++found;
              remove.add(entity);
            }
          }
        }
      }

      if (found > 1 && !remove.isEmpty()) {
        var5 = remove.iterator();

        while(var5.hasNext()) {
          Entity tokill = (Entity)var5.next();
          tokill.remove();
        }
      }
    }

  }
}
