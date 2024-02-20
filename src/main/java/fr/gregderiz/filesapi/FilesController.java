package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class FilesController {
    private static final Map<File, Set<File>> folders = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void setupFolder(Plugin plugin, String directoryName) {
        File directory = new File(plugin.getDataFolder() + File.separator + directoryName);
        if (!directory.exists()) directory.mkdir();

        File[] files = directory.listFiles();
        if (files == null) {
            Bukkit.getLogger().severe("Files you want to access is null.");
            return;
        }

        folders.put(directory, new HashSet<>(Arrays.asList(files)));
    }

    public static Set<File> getFilesFromDirectoryName(String directoryName) {
        Optional<File> optionalDirectory = getFolders().keySet().stream().filter(file -> {
            String name = file.getName().substring(0, file.getName().lastIndexOf("."));
            return name.equalsIgnoreCase(directoryName);
        }).findAny();

        if (!optionalDirectory.isPresent()) {
            Bukkit.getLogger().severe("There are no directories with that name.");
            return null;
        }

        return folders.get(optionalDirectory.get());
    }

    public static Map<File, Set<File>> getFolders() {
        return folders;
    }

    public static void addFileInFolder(File directory, File file) {
        if (directory == null || folders.get(directory) == null) {
            Bukkit.getLogger().severe("The directory is null.");
            return;
        }

        if (file == null) {
            Bukkit.getLogger().severe("The file you want to add is null.");
            return;
        }

        if (folders.get(directory).contains(file)) return;
        folders.get(directory).add(file);
    }

    public static void removeFileFromFolder(File directory, File file) {
        if (directory == null || folders.get(directory) == null) {
            Bukkit.getLogger().severe("The directory is null.");
            return;
        }

        if (file == null) {
            Bukkit.getLogger().severe("The file you want to add is null.");
            return;
        }

        if (!folders.get(directory).contains(file)) return;
        folders.get(directory).remove(file);
    }
}
