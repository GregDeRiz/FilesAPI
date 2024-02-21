package fr.gregderiz.filesapi;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Set;

@SuppressWarnings("all")
public class FileProperties {
    public static void create(Plugin plugin, String directoryName) {
        File directory = new File(plugin.getDataFolder() + File.separator + directoryName);
        if (!directory.exists()) directory.mkdir();

        FilesController.addFolder(directory);
    }

    public static void copy(File source, File destination) {
        if (source.isDirectory()) {
            if (!destination.exists()) destination.mkdir();

            String[] files = source.list();
            for (String file : files) {
                if (file.equals("uid.dat")) continue;

                File newSource = new File(source, file);
                File newDestination = new File(destination, file);
                copy(newSource, newDestination);
            }

            return;
        }

        try {
            InputStream inputStream = Files.newInputStream(source.toPath());
            OutputStream outputStream = Files.newOutputStream(destination.toPath());

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) outputStream.write(buffer, 0, length);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            Set<File> files = FilesController.getFilesFromDirectory(file);
            if (files == null) return;

            for (File child : files) delete(child);
        }

        file.delete();
    }
}
