package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public final class FilesManager {
    private static FilesManager instance = null;
    private final FilesController filesController;

    private FilesManager() {
        this.filesController = new FilesController(this);
    }

    public static FilesManager getFileManager() {
        return (instance == null) ? instance = new FilesManager() : instance;
    }

    @SuppressWarnings("all")
    public File loadFile(File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) parent.mkdirs();

        if (!FilesChecker.isDirectory(file)) {
            if (!file.exists()) file.mkdirs();
            this.filesController.addFolder(file);
            return file;
        }

        if (!file.exists()) {
            try { file.createNewFile(); }
            catch (IOException e) { Bukkit.getLogger().severe("Error when creating file " + file.getName() + " by exception: " + e); }
        }

        this.filesController.addFolder(file.getParentFile());
        return file;
    }

    public Set<File> getFilesList(File directory) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return null;
        if (!this.filesController.getFolders().containsKey(directory)) return null;

        Optional<Set<File>> optionalFiles = this.filesController.getFilesFromDirectory(directory);
        if (!optionalFiles.isPresent()) {
            Bukkit.getLogger().warning("Directory named " + directory.getName() + " was not found");
            return null;
        }

        return optionalFiles.get();
    }

    public String getNameWithoutExtension(String file) {
        return file.substring(0, file.lastIndexOf("."));
    }

    public FilesController getFilesController() {
        return this.filesController;
    }
}
