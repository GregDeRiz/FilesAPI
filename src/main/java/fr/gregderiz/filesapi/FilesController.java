package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.*;

public class FilesController {
    private final Map<File, Set<File>> folders;
    private final FilesManager filesManager;
    private final FilesProperty filesProperty;

    public FilesController(FilesManager filesManager) {
        this.folders = new HashMap<>();
        this.filesManager = filesManager;
        this.filesProperty = filesManager.getFilesProperty();
    }

    public Map<File, Set<File>> getFolders() {
        return folders;
    }

    public Optional<Set<File>> getFilesFromDirectory(File directory) {
        return Optional.ofNullable(this.folders.get(directory));
    }

    public void addFileToFolder(File directory, File file) {
        if (FilesChecker.checkIfFileIsNull(file)) return;
        if (!this.folders.containsKey(directory)) return;

        Set<File> files = this.filesManager.getFilesList(directory);
        if (files == null) return;
        if (files.contains(file)) return;

        files.add(file);
    }

    public void removeFileFromFolder(File directory, File file) {
        if (FilesChecker.checkIfFileIsNull(file)) return;
        if (!this.folders.containsKey(directory)) return;

        Set<File> files = this.filesManager.getFilesList(directory);
        if (files == null) return;
        if (!files.contains(file)) return;

        files.remove(file);
        this.filesProperty.delete(file);
    }

    public void addFolder(File directory) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return;
        if (this.folders.containsKey(directory)) return;

        File[] files = directory.listFiles();
        if (files == null) {
            Bukkit.getLogger().severe("Files you want to access in directory " + directory.getName() + " are null.");
            return;
        }

        folders.put(directory, new HashSet<>(Arrays.asList(files)));
    }

    public void removeFolder(File directory) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return;
        if (!this.folders.containsKey(directory)) return;

        folders.remove(directory);
        this.filesProperty.delete(directory);
    }
}
