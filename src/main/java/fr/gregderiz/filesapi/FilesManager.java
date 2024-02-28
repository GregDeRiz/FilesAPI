package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class FilesManager {
    public static FilesManager instance = null;

    private FilesManager() {

    }

    public static FilesManager getFileManager() {
        return (instance == null) ? instance = new FilesManager() : instance;
    }

    @SuppressWarnings("all")
    public File loadFile(File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) parent.mkdirs();

        if (!FilesChecker.checkIfDirectory(file)) {
            if (!file.exists()) file.mkdirs();
            FilesController.addFolder(file);
            return file;
        }

        if (!file.exists()) {
            try { file.createNewFile(); }
            catch (IOException e) { Bukkit.getLogger().severe("Error when creating file " + file.getName() + " by exception: " + e); }
        }

        FilesController.addFolder(file.getParentFile());
        return file;
    }

    public Map<String, Object> getDataOfFile(File file) {
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> fileData = new HashMap<>();

        for (String fileKey : fileConfiguration.getKeys(false)) {
            if (fileConfiguration.isConfigurationSection(fileKey)) {
                ConfigurationSection dataSection = fileConfiguration.getConfigurationSection(fileKey);
                assert dataSection != null;

                fileData.put(fileKey, dataSection.getValues(false));
            }
            if (!fileData.containsKey(fileKey)) fileData.put(fileKey, fileConfiguration.get(fileKey));
        }

        return fileData;
    }

    public String getNameWithoutExtension(String file) {
        return file.substring(0, file.lastIndexOf("."));
    }
}
