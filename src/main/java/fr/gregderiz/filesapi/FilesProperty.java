package fr.gregderiz.filesapi;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("all")
public class FilesProperty {
    private static final FilesManager filesManager = FilesManager.getFileManager();

    public static File create(File path, String name) {
        File file = new File(path, name);
        return filesManager.loadFile(file);
    }

    public static File create(FileBuilder fileBuilder) {
        return filesManager.loadFile(fileBuilder.build());
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
            Optional<Set<File>> optionalFiles = FilesController.getFilesFromDirectory(file);
            if (!optionalFiles.isPresent()) {
                Bukkit.getLogger().warning("Directory named " + file.getName() + " is empty");
                return;
            }

            Set<File> files = optionalFiles.get();
            for (File child : files) {
                delete(child);
                FilesController.removeFileFromFolder(file, child);
            }
        }

        file.delete();
    }
}
