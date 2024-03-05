package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FileRead {
    private final FileConfiguration fileConfiguration;
    private final String fileName;

    public FileRead(File file) {
        this.fileName = FilesManager.getFileManager().getNameWithoutExtension(file.getName());
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public Object get(String name) {
        Object object = this.fileConfiguration.get(name);
        if (object == null) {
            Bukkit.getLogger().severe("Object " + name + " was not found in file " + this.fileName);
            return null;
        }

        return object;
    }

    public List<?> list(String name) {
        List<?> list = this.fileConfiguration.getList(name);
        if (list == null) {
            Bukkit.getLogger().severe("List " + name + " was not found in file " + this.fileName);
            return null;
        }

        return list;
    }

    public Map<String, Object> section(String name) {
        ConfigurationSection section = this.fileConfiguration.getConfigurationSection(name);
        if (section == null) {
            Bukkit.getLogger().severe("Section " + name + " was not found in file " + this.fileName);
            return null;
        }

        return section.getValues(false);
    }
}
