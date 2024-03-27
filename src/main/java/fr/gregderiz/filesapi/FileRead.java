package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FileRead {
    private final FileConfiguration fileConfiguration;
    private final File file;
    private final Set<String> sectionsName;

    public FileRead(File file) {
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        this.sectionsName = this.fileConfiguration.getKeys(true);
    }

    public Object get(String name) {
        String fileName = FilesManager.getFileManager().getNameWithoutExtension(this.file.getName());
        Optional<String> optionalSectionName = findSectionByName(name);
        if (!optionalSectionName.isPresent()) {
            Bukkit.getLogger().severe("Name " + name + " was not found in file " + fileName);
            return null;
        }

        Object object = this.fileConfiguration.get(optionalSectionName.get());
        if (object == null) {
            Bukkit.getLogger().severe("Object " + name + " was not found in file " + fileName);
            return null;
        }

        return object;
    }

    public List<?> list(String name) {
        String fileName = FilesManager.getFileManager().getNameWithoutExtension(this.file.getName());
        Optional<String> optionalSectionName = findSectionByName(name);
        if (!optionalSectionName.isPresent()) {
            Bukkit.getLogger().severe("Name " + name + " was not found in file " + fileName);
            return null;
        }

        List<?> list = this.fileConfiguration.getList(optionalSectionName.get());
        if (list == null) {
            Bukkit.getLogger().severe("List " + name + " was not found in file " + fileName);
            return null;
        }

        return list;
    }

    public Map<String, Object> section(String name) {
        String fileName = FilesManager.getFileManager().getNameWithoutExtension(this.file.getName());
        Optional<String> optionalSectionName = findSectionByName(name);
        if (!optionalSectionName.isPresent()) {
            Bukkit.getLogger().severe("Name " + name + " was not found in file " + fileName);
            return null;
        }

        ConfigurationSection section = this.fileConfiguration.getConfigurationSection(optionalSectionName.get());
        if (section == null) {
            Bukkit.getLogger().severe("Section " + name + " was not found in file " + fileName);
            return null;
        }

        return section.getValues(false);
    }

    public void override(String name, Object value) {
        String fileName = FilesManager.getFileManager().getNameWithoutExtension(this.file.getName());
        Optional<String> optionalSectionName = findSectionByName(name);
        if (!optionalSectionName.isPresent()) {
            Bukkit.getLogger().severe("Name " + name + " was not found in file " + fileName);
            return;
        }

        this.fileConfiguration.set(optionalSectionName.get(), value);
        try { this.fileConfiguration.save(this.file); }
        catch (IOException e) { Bukkit.getLogger().severe("Error when saving file " + this.file.getName() + " by exception: " + e); }
    }

    public void remove(String name) {
        String fileName = FilesManager.getFileManager().getNameWithoutExtension(this.file.getName());
        Optional<String> optionalSectionName = findSectionByName(name);
        if (!optionalSectionName.isPresent()) {
            Bukkit.getLogger().severe("Name " + name + " was not found in file " + fileName);
            return;
        }

        this.fileConfiguration.set(optionalSectionName.get(), null);
        try { this.fileConfiguration.save(this.file); }
        catch (IOException e) { Bukkit.getLogger().severe("Error when saving file " + this.file.getName() + " by exception: " + e); }
    }

    private Optional<String> findSectionByName(String name) {
        return this.sectionsName.stream().filter(sectionName -> sectionName.contains(name)).findFirst();
    }
}
