package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;

import java.io.File;
import java.util.*;

public class FilesController {
    private static final Map<File, Set<File>> folders = new HashMap<>();

    public static Map<File, Set<File>> getFolders() {
        return folders;
    }

    public static Optional<File> findDirectoryByName(String directoryName) {
        return getFolders().keySet().stream().filter(file -> {
            String name = file.getName().substring(0, file.getName().lastIndexOf("."));
            return name.equalsIgnoreCase(directoryName);
        }).findAny();
    }

    public static Set<File> getFilesFromDirectory(File directory) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return null;
        return getFolders().get(directory);
    }

    public static void addFileToFolder(File directory, File file) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return;
        if (FilesChecker.checkIfFileIsNull(file)) return;

        Set<File> files = getFolders().get(directory);
        if (files.contains(file)) return;
        files.add(file);
    }

    public static void removeFileFromFolder(File directory, File file, boolean canDelete) {
        if (FilesChecker.checkIfDirectoryIsNull(directory)) return;
        if (FilesChecker.checkIfFileIsNull(file)) return;

        Set<File> files = getFolders().get(directory);
        if (!files.contains(file)) return;
        files.remove(file);
        if (canDelete) FilesProperty.delete(file);
    }

    public static void addFolder(File directory) {
        if (getFolders().containsKey(directory)) return;

        File[] files = directory.listFiles();
        if (files == null) {
            Bukkit.getLogger().severe("Files you want to access is null.");
            return;
        }

        folders.put(directory, new HashSet<>(Arrays.asList(files)));
    }

    public static void removeFolder(File directory, boolean canDelete) {
        if (!getFolders().containsKey(directory)) return;
        folders.remove(directory);
        if (canDelete) FilesProperty.delete(directory);
    }
}
