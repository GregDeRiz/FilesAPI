package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.*;

public class FilesController {
    private static final Map<File, Set<File>> folders = new HashMap<>();
    private static final FilesManager filesManager = FilesManager.getFileManager();

    public static Map<File, Set<File>> getFolders() {
        return folders;
    }

    public static Optional<File> findDirectoryByName(String name) {
        return getFolders().keySet().stream().filter(directory -> directory.getName().equalsIgnoreCase(name)).findAny();
    }

    public static Optional<File> findFileByName(File directory, String name) {
        Set<File> files = getFilesList(directory);
        return (files == null) ? Optional.empty() : files.stream().filter(file ->
                filesManager.getNameWithoutExtension(file.getName()).equalsIgnoreCase(name)).findAny();
    }

    public static Optional<Set<File>> getFilesFromDirectory(File directory) {
        return Optional.ofNullable(getFolders().get(directory));
    }

    public static void addFileToFolder(File directory, File file) {
        if (FilesChecker.checkIfFileIsNull(file)) return;

        Set<File> files = getFilesList(directory);
        if (files == null) return;
        if (files.contains(file)) return;

        files.add(file);
    }

    public static void removeFileFromFolder(File directory, File file) {
        if (FilesChecker.checkIfFileIsNull(file)) return;

        Set<File> files = getFilesList(directory);
        if (files == null) return;
        if (!files.contains(file)) return;

        files.remove(file);
        FilesProperty.delete(file);
    }

    public static void addFolder(File directory) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return;
        if (getFolders().containsKey(directory)) return;

        File[] files = directory.listFiles();
        if (files == null) {
            Bukkit.getLogger().severe("Files you want to access in directory " + directory.getName() + " are null.");
            return;
        }

        folders.put(directory, new HashSet<>(Arrays.asList(files)));
    }

    public static void removeFolder(File directory) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return;
        if (!getFolders().containsKey(directory)) return;

        folders.remove(directory);
        FilesProperty.delete(directory);
    }

    private static Set<File> getFilesList(File directory) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return null;
        if (!getFolders().containsKey(directory)) return null;

        Optional<Set<File>> optionalFiles = getFilesFromDirectory(directory);
        if (!optionalFiles.isPresent()) {
            Bukkit.getLogger().warning("Directory named " + directory.getName() + " was not found");
            return null;
        }

        return optionalFiles.get();
    }
}
