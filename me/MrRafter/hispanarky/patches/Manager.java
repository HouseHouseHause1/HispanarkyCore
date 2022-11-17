package me.MrRafter.hispanarky.patches;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.MrRafter.hispanarky.Main;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

@RequiredArgsConstructor
@Getter
public abstract class Manager {

    @NonNull
    private final String name;

    protected File dataFolder;

    public abstract void init(Main plugin);

    public abstract void destruct(Main plugin);

    public abstract void reloadConfig(ConfigurationSection config);

    public File getDataFolder() {
        if (dataFolder == null) dataFolder = new File(Main.getInstance().getDataFolder(), getName());
        if (!dataFolder.exists()) dataFolder.mkdirs();
        return dataFolder;
    }
}
