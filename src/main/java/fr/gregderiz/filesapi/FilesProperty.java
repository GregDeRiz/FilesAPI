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
    private final FilesManager filesManager;
    private final FilesController filesController;

    public FilesProperty(FilesManager filesManager) {
        this.filesManager = filesManager;
        this.filesController = filesManager.getFilesController();
    }

    public File create(File path, String name) {
        File file = new File(path, name);
        return this.filesManager.loadFile(file);
    }

    public File create(FileBuilder fileBuilder) {
        return this.filesManager.loadFile(fileBuilder.build());
    }

    public void copy(File source, File destination) {
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

    public void delete(File file) {
        if (file.isDirectory()) {
            Optional<Set<File>> optionalFiles = this.filesController.getFilesFromDirectory(file);
            if (!optionalFiles.isPresent()) {
                Bukkit.getLogger().warning("Directory named " + file.getName() + " is empty");
                return;
            }

            Set<File> files = optionalFiles.get();
            for (File child : files) {
                delete(child);
                this.filesController.removeFileFromFolder(file, child);
            }
        }

        file.delete();
    }
}
