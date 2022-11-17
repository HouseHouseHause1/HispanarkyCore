package me.MrRafter.hispanarky.events;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerInteractWithEntityEvent implements Listener {
    private final Main plugin;

    public PlayerInteractWithEntityEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e){
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();
        if(Utils.getConfig().getBoolean("salc1-dupe.enabled")){
            if(p.getInventory().getItemInMainHand().getType() == Material.CHEST){
                if(entity instanceof Donkey || entity instanceof Llama){
                    e.setCancelled(true);
                    ChestedHorse donkey = (ChestedHorse) entity;
                    for(ItemStack i : donkey.getInventory().getContents()){
                        if(i != null){
                            if(i.getType() != Material.SADDLE){
                                donkey.getWorld().dropItem(donkey.getLocation(), i);
                            }
                        }
                    }
                    donkey.setCarryingChest(false);
                }
            }
        }
    }

}