package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;

import java.io.File;

public class FilesChecker {
    public static boolean checkIfDirectoryIsNull(File directory) {
        if (directory == null) {
            Bukkit.getLogger().severe("The directory is null.");
            return true;
        }

        return false;
    }

    public static boolean checkIfFileIsNull(File file) {
        if (file == null) {
            Bukkit.getLogger().severe("The file you want to add is null.");
            return true;
        }

        return false;
    }

    public static boolean isFile(File file) {
        return file.getName().contains(".");
    }
}
