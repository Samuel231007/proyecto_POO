package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

// Lee y escribe las líneas de los archivos de src/files/ en UTF-8, para conservar tildes y ñ (RNF-01).
public class FileManage {
    // Carpeta de los archivos. Igual que en el proyecto base: se ejecuta desde la raíz del proyecto.
    private static final String CARPETA = "src/files/";

    private final String nameFile;

    public FileManage(String nameFile) {
        this.nameFile = nameFile;
    }

    public File getFile() {
        return new File(CARPETA + this.nameFile);
    }

    public String getNameFile() { return nameFile; }

    // Si el archivo no existe devuelve una lista vacía; se crea al escribir la primera línea.
    public ArrayList<String> getDataOfFile() {
        ArrayList<String> lines = new ArrayList<>();
        File file = this.getFile();
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo " + nameFile + ": " + e.getMessage());
        }
        return lines;
    }

    public void writeFile(String newLine) {
        crearCarpetaSiFalta();
        try (BufferedWriter writer = Files.newBufferedWriter(getFile().toPath(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(newLine);
            writer.newLine();
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo escribir en el archivo " + nameFile + ": " + e.getMessage());
        }
    }

    public void updateFile(String oldLine, String newLine) {
        ArrayList<String> lines = this.getDataOfFile();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(oldLine)) {
                lines.set(i, newLine);
                this.writeAllLinesInFile(lines);
                return;
            }
        }
    }

    public void deleteFile(String line) {
        ArrayList<String> lines = this.getDataOfFile();
        if (lines.remove(line)) {
            this.writeAllLinesInFile(lines);
        }
    }

    // RNF-03: escribe primero un archivo temporal y luego lo reemplaza; si algo falla, el archivo anterior queda intacto.
    public void writeAllLinesInFile(ArrayList<String> lines) {
        crearCarpetaSiFalta();
        File temporal = new File(getFile().getPath() + ".tmp");
        try (BufferedWriter writer = Files.newBufferedWriter(temporal.toPath(), StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            temporal.delete();
            throw new IllegalStateException("No se pudo guardar el archivo " + nameFile + ": " + e.getMessage());
        }
        try {
            Files.move(temporal.toPath(), getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            temporal.delete();
            throw new IllegalStateException("No se pudo guardar el archivo " + nameFile + ": " + e.getMessage());
        }
    }

    private void crearCarpetaSiFalta() {
        File carpeta = getFile().getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            carpeta.mkdirs();
        }
    }
}
