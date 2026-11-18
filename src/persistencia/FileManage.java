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

/**
 * Capa de persistencia: lee y escribe líneas de texto en los archivos de src/files/ (RNF-01).
 * Es la misma idea del FileManage del proyecto base, con tres mejoras:
 *  1. Usa UTF-8 para que las tildes y la ñ no se dañen (Gómez, Plomería, Cajicá...).
 *  2. Si el archivo no existe, devuelve una lista vacía y lo crea al escribir (no hay que crearlo a mano).
 *  3. RNF-03: al reescribir el archivo completo, primero escribe un archivo temporal y luego lo reemplaza;
 *     si algo falla a mitad de camino, el archivo anterior queda intacto.
 */
public class FileManage {

    // Carpeta de los archivos. Igual que en el proyecto base: se ejecuta desde la raíz del proyecto.
    private static final String CARPETA = "src/files/";

    // OOP: Encapsulación — el nombre del archivo es privado
    private final String nameFile;

    public FileManage(String nameFile) {
        this.nameFile = nameFile;
    }

    public File getFile() {
        return new File(CARPETA + this.nameFile);
    }

    public String getNameFile() { return nameFile; }

    /** Lee todas las líneas no vacías del archivo. Si el archivo no existe devuelve una lista vacía. */
    public ArrayList<String> getDataOfFile() {
        ArrayList<String> lines = new ArrayList<>();
        File file = this.getFile();
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Solo agrega líneas no vacías para evitar problemas al separar los campos
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo " + nameFile + ": " + e.getMessage());
        }
        return lines;
    }

    /** Agrega una línea al final del archivo (modo append). */
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

    /** Reemplaza la primera línea igual a oldLine por newLine. */
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

    /** Elimina la primera línea igual a line. */
    public void deleteFile(String line) {
        ArrayList<String> lines = this.getDataOfFile();
        if (lines.remove(line)) {
            this.writeAllLinesInFile(lines);
        }
    }

    /** Reescribe el archivo completo con las líneas dadas (RNF-03: primero en un temporal). */
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
