package me.MrRafter.hispanarky.misc;

import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class Chat implements Listener {
    private static final String httpRegexCheck = "(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?://(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
    private static final String regexCheck = "[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z()]{1,6}\\b([-a-zA-Z()@:%_+.~#?&/=]*)";
    private final Main plugin;
    private final Map<UUID, Integer> messagesPerTime = new HashMap<>();
    private final Set<String> antispam = new HashSet<>();
    private final Set<String> linkSpam = new HashSet<>();
    private final Set<String> words = new HashSet<>();

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();

        if (event.getMessage().contains("．")) {
            if (plugin.getConfig().getBoolean("PreventUnicodeDot")) {
                event.setCancelled(true);
            }
        }

        String msgLowercase = event.getMessage().toLowerCase();
        for (String s : plugin.getConfig().getStringList("BannedWords")) {
            if (msgLowercase.contains(s.toLowerCase())) {
                event.setCancelled(true);
                plugin.getLogger().info(ChatColor.GOLD + p.getName() + ChatColor.RED + " " + event.getMessage() + ChatColor.GOLD + " because words banned in config.yml");
            }
        }

        for (String s : plugin.getConfig().getStringList("BannedRegex")) {
            if (event.getMessage().matches(s)) {
                plugin.getLogger().info(ChatColor.GOLD + p.getName() + " prevented from saying " + ChatColor.RED + event.getMessage() + ChatColor.GOLD + " because regex banned in config.yml");
                event.setCancelled(true);
            }
        }

        if (handleChatSpam(event.getPlayer(), event.getMessage())) {
            event.setCancelled(true);
        }

        if (plugin.getConfig().getBoolean("Replace@")) {
            if (event.getMessage().contains("@")) {
                event.setMessage(event.getMessage().replaceAll("@", ""));
            }
        }

        if (plugin.getConfig().getBoolean("Preventlinks")) {
            /*
             code from John200410
            */
            //loop through each word

            final World playerWorld = p.getWorld();

            //check if player is in overworld
            if (!playerWorld.getEnvironment().equals(World.Environment.NORMAL)) {
                return;
            }
            final Location location = p.getLocation();

            //check distance from the world spawn
            if (location.distance(playerWorld.getSpawnLocation()) > 1000) {
                return;
            }

            //check if message contains a link
            final String msg = event.getMessage();

            //loop through each word
            for (String string : msg.split(" ")) {
                //check if it is a link
                if ((string.matches(regexCheck) || string.matches(httpRegexCheck)) && !p.isOp()) {
                    plugin.getLogger().info(ChatColor.GOLD +"Prevented " + p.getName() + " from sending " + ChatColor.RED + event.getMessage() + ChatColor.GOLD + " because link");
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean handleChatSpam(Player p, String m) {
        if (plugin.getConfig().getBoolean("PreventSpam")) {
            if (antispam.contains(p.getName())) {
                plugin.getLogger().info(ChatColor.GOLD + p.getName() + " FAILED Message slowmode: " + ChatColor.RED + m);
                return true;
            } else {
                antispam.add(p.getName());
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> antispam.remove(p.getName()), plugin.getConfig().getInt("AntiSpamTime") * 20L);

                if (words.contains(m)) {
                    plugin.getLogger().info(p.getName() + " FAILED message due to duplicate message: " + m);
                    return true;
                } else {
                    if (m.length() > 10) {
                        words.add(m);
                        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> words.remove(m), plugin.getConfig().getInt("AntiSpamWordTime") * 20L);
                    }
                    Integer playerMessagesPerTime = messagesPerTime.get(p.getUniqueId());
                    if (playerMessagesPerTime != null) {
                        if (playerMessagesPerTime > plugin.getConfig().getInt("AntiSpamMessagesPerTime")) {
                            plugin.getLogger().info(p.getName() + " FAILED to send message due to too many messages in time period: " + m);
                            return true;
                        } else {
                            messagesPerTime.merge(p.getUniqueId(), 1, Integer::sum);
                            Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> messagesPerTime.put(p.getUniqueId(), messagesPerTime.get(p.getUniqueId()) - 1), plugin.getConfig().getInt("AntiSpamCheckTime") * 20L);
                        }
                    } else {
                        messagesPerTime.put(p.getUniqueId(), 1);
                    }
                }

            }

            if (m.length() > plugin.getConfig().getInt("CharacterLimit")) {
                plugin.getLogger().info(p.getName() + " FAILED to send message due to character limit: " + m);
                return true;
            }
            AtomicBoolean c = new AtomicBoolean(false);
            for (String string : m.replace(">", "").split(" ")) {
                // Check if message matches regexes in config
                plugin.getConfig().getStringList("LinkRegex").forEach(b -> {
                    if (string.matches(b)) {
                        if (handleLinkSpam(p)) {
                            c.set(true);
                            plugin.getLogger().info("Prevented " + p.getName() + " from sending " + m + " because temp banned from sending links");
                        }
                    }
                });
            }
            return c.get();
        }
        return false;
    }

    private boolean handleLinkSpam(Player p) {
        if (linkSpam.contains(p.getName())) {
            return !plugin.getConfig().getBoolean("AllowLinksForOP") || !p.isOp();
        } else {
            if (plugin.getConfig().getBoolean("AllowLinksForOP") && p.isOp()) {
                return false;
            } else {
                linkSpam.add(p.getName());
                Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> linkSpam.remove(p.getName()), plugin.getConfig().getInt("AntiSpamLinkTime") * 20L);
            }
        }

        return false;
    }
}
