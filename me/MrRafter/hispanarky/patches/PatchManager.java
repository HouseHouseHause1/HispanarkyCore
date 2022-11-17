package me.MrRafter.hispanarky.patches;

import lombok.Getter;
import me.MrRafter.hispanarky.Main;
import me.MrRafter.hispanarky.patches.*;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.block.EndGateway;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.Redstone;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PatchManager extends Manager {
    @Getter
    private Image image;
    private Main plugin;
    @Getter
    private ConfigurationSection config;

    public PatchManager() {
        super("Patches");
    }

    @Override
    public void init(Main plugin) {
        this.plugin = plugin;
        config = plugin.getModuleConfig(this);
        plugin.registerListener(new BoatFly());
        plugin.registerListener(new IllegalBlock(plugin.getConfig()));
        plugin.registerListener(new ProjectileVelocity());
        plugin.registerListener(new WitherLag());
    }

    @Override
    public void destruct(Main plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
        this.config = config;
    }
}
