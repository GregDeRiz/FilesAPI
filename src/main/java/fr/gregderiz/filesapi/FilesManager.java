package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public final class FilesManager {
    private static FilesManager instance = null;
    private final FilesController filesController;
    private final FilesProperty filesProperty;

    private FilesManager() {
        this.filesController = new FilesController(this);
        this.filesProperty = new FilesProperty(this);
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

        Optional<Set<File>> optionalFiles = this.filesController.getFilesFromDirectory(directory);
        if (!optionalFiles.isPresent()) {
            Bukkit.getLogger().warning("Directory named " + directory.getName() + " was not found");
            return null;
        }

        return optionalFiles.get();
    }

    public Optional<File> findDirectoryByName(String name) {
        return this.filesController.getFolders().keySet().stream()
                .filter(directory -> directory.getName().equalsIgnoreCase(name)).findAny();
    }

    public Optional<File> findDirectoryByPath(File path) {
        return this.filesController.getFolders().keySet().stream()
                .filter(directory -> directory.equals(path)).findAny();
    }

    public Optional<File> findFileByName(File directory, String name) {
        if (!this.filesController.getFolders().containsKey(directory)) return Optional.empty();

        Set<File> files = getFilesList(directory);
        return (files == null) ? Optional.empty() : files.stream()
                .filter(file -> !FilesChecker.isDirectory(file))
                .filter(file -> getNameWithoutExtension(file.getName()).equalsIgnoreCase(name)).findAny();
    }

    public void destroy() {
        this.getFilesController().getFolders().clear();
    }

    public String getNameWithoutExtension(String file) {
        return file.substring(0, file.lastIndexOf("."));
    }

    public FilesController getFilesController() {
        return this.filesController;
    }

    public FilesProperty getFilesProperty() { return this.filesProperty; }
}
