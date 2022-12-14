package me.MrRafter.hispanarky.patches.randomspawn;

import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.patches.Manager;
import me.MrRafter.hispanarky.patches.Utils;
import me.MrRafter.hispanarky.patches.randomspawn.listeners.RespawnListener;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class RandomSpawnManager extends Manager {

    private int range;
    private String world;
    private List<Material> ignored;
    private ConfigurationSection config;

    public RandomSpawnManager() {
        super("RandomSpawn");
    }

    public String getWorld() {
        return world;
    }

    @Override
    public void init(Main plugin) {
        config = plugin.getModuleConfig(this);
        plugin.registerListener(new RespawnListener(this));
        setVars();
    }

    @Override
    public void destruct(Main plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
        setVars();
    }

    private List<Material> parseIgnored() {
        List<String> strList = config.getStringList("Blocks");
        List<Material> output = new ArrayList<>();
        for (String strMat : strList) {
            try {
                Material material = Material.valueOf(strMat.toUpperCase());
                output.add(material);
            } catch (EnumConstantNotPresentException e) {
                Utils.log("&3Unknown block&r&a " + strMat + "&r&3 in blocks section of the config", this);
            }
        }
        return output;
    }

    private void setVars() {
        range = config.getInt("Range");
        world = config.getString("World");
        ignored = parseIgnored();
    }

    public List<Material> getIgnored() {
        return ignored;
    }

    public int getRange() {
        return range;
    }
}