package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileBuilder {
    private final File file;
    private final FileConfiguration fileConfiguration;

    public FileBuilder(String path, String name) {
        this.file = new File(path, name + ".yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileBuilder(File path, String name) {
        this.file = new File(path, name + ".yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileBuilder(File file) {
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileBuilder fileClone() {
        return new FileBuilder(this.file);
    }

    public FileBuilder set(String name, Object data) {
        this.fileConfiguration.set(name, data);
        return this;
    }

    public FileBuilder section(String name, Map<String, Object> data) {
        ConfigurationSection section = this.fileConfiguration.createSection(name);
        data.forEach(section::set);
        return this;
    }

    public File build() {
        try { this.fileConfiguration.save(this.file); }
        catch (IOException e) { Bukkit.getLogger().severe("Error when saving file " + this.file.getName() + " by exception: " + e); }
        return this.file;
    }
}
