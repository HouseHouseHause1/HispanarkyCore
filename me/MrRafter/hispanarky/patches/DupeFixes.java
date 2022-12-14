package me.MrRafter.hispanarky.patches;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class DupeFixes implements Listener {
    private final Main plugin;

    @EventHandler
    private void onEntityPortalEvent(EntityPortalEvent evt) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("DonkeysFromGoingThroughPortalsPatch")) {
            Entity entity = evt.getEntity();
            if (entity instanceof ChestedHorse) {
                if (((ChestedHorse) entity).isCarryingChest()) {
                    evt.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent evt) {
        if (plugin.getConfig().getBoolean("EndPortalDupePatch")) {
            if (evt.getEntity() instanceof LivingEntity) {
                if ((evt.getEntity() instanceof ChestedHorse && ((ChestedHorse) evt.getEntity()).isCarryingChest()) || ((LivingEntity) evt.getEntity()).getCanPickupItems()) {
                    if ((evt.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || evt.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                            && evt.getEntity().getWorld().getEnvironment().equals(World.Environment.THE_END)
                            && Math.round(evt.getEntity().getLocation().getX()) == 100
                            && Math.round(evt.getEntity().getLocation().getZ()) == 0
                            && (((LivingEntity) evt.getEntity()).getHealth() - evt.getDamage()) <= 0) {
                        evt.getEntity().remove();
                    }
                }
            }
        }
    }

    @EventHandler
    private void onEntityInteract(PlayerInteractAtEntityEvent evt) {
        FileConfiguration config = plugin.getConfig();

        if (config.getBoolean("DisableChestsOnDonkeys")) {
            if (evt.getRightClicked() instanceof ChestedHorse) {
                evt.setCancelled(true);
                (new BukkitRunnable() {
                    public void run() {
                        if (evt.getPlayer().getInventory().getItemInMainHand().getType() == Material.CHEST) {
                            ((ChestedHorse) evt.getRightClicked()).setCarryingChest(false);
                        }
                        ((ChestedHorse) evt.getRightClicked()).setCarryingChest(false);
                    }
                }).runTaskLater(plugin, 2L);
            }
        }
    }
}
