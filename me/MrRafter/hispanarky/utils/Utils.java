package me.MrRafter.hispanarky.utils;

import me.MrRafter.hispanarky.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static void sendMessage(Player p, String message){
        String prefix = getConfig().getString("global.prefix");
        if(Main.usingPAPI){
            String messagewoutpapi = ChatColor.translateAlternateColorCodes('&', prefix+message);
            String finalmessage = PlaceholderAPI.setPlaceholders(p, messagewoutpapi);
            p.sendMessage(finalmessage);
        }else{
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+message));
        }
    }

    public static int O = 1;

    public static void sendRawMessage(Player p, String message){
        if(Main.usingPAPI){
            String messagewoutpapi = ChatColor.translateAlternateColorCodes('&', message);
            String finalmessage = PlaceholderAPI.setPlaceholders(p, messagewoutpapi);
            p.sendMessage(finalmessage);
        }else{
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static FileConfiguration getConfig() {
        return Main.getPlugin().getConfig();
    }

    public static String getJoindate(Player p){
        if(p == null){
            return "&cError, cope!";
        }else{
            long offlineDate = ((OfflinePlayer) p).getFirstPlayed();
            Date date = new Date(offlineDate);
            String joinDate = new SimpleDateFormat("EEEE, d MMMM y - hh:mm:ss z")
                    .format(date);

            return joinDate;
        }
    }

    public static int getPing(Player p) {
        try {
            String a = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> b = Class.forName("org.bukkit.craftbukkit." + a + ".entity.CraftPlayer");
            Object c = b.getMethod("getHandle", new Class[0]).invoke(p);
            int ping = (Integer) c.getClass().getDeclaredField("ping").get(c);
            return ping;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return -2;
    }

    public static void sendMessage(String s) {
    }
}
