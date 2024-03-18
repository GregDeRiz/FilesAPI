package fr.gregderiz.filesapi;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class FilesPlugin extends JavaPlugin {
    private FilesManager filesManager;

    @Override
    public void onEnable() {
        this.filesManager = FilesManager.getFileManager();
        File[] dataFolder = getDataFolder().listFiles();
        if (dataFolder == null) return;

        for (File file : dataFolder) this.filesManager.loadFile(file);
    }

    @Override
    public void onDisable() {
        if (this.filesManager != null) this.filesManager.destroy();
    }
}
