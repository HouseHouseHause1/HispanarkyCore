package me.MrRafter.hispanarky.misc;

import java.util.Random;

import me.MrRafter.hispanarky.Main;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

public class Util
{
    private static Integer chance;
    
    public static void load() {
        Main.getPlugin().saveDefaultConfig();
        if (Main.getPlugin().getConfig().getBoolean("modification.dispenser")) {
            Bukkit.getServer().getPluginManager().registerEvents((Listener)new Dispenser(), (Plugin)Main.getPlugin());
        }
        if (Main.getPlugin().getConfig().getBoolean("modification.dropper")) {
            Bukkit.getServer().getPluginManager().registerEvents((Listener)new Dropper(), (Plugin)Main.getPlugin());
        }
        Util.chance = Main.getPlugin().getConfig().getInt("settings.chance");
    }
    
    public static Boolean chanceOf() {
        final Random r = new Random();
        final int g = r.nextInt(100);
        if (g <= Util.chance) {
            return true;
        }
        return false;
    }
}
