package repositorios;

import persistencia.FileManage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

// Todas las líneas de los archivos empiezan con su clave (documento, código de la orden o id).
public abstract class BaseRepository {
    protected FileManage fileManage;
    protected String delimiter;

    public BaseRepository(String nameFile, String delimiter) {
        this.fileManage = new FileManage(nameFile);
        this.delimiter = delimiter;
    }

    // split con -1 conserva los campos vacíos; StringTokenizer los saltaría y los datos quedarían corridos.
    protected String[] separar(String linea) {
        return linea.split(Pattern.quote(delimiter), -1);
    }

    protected String unir(Object... campos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) { sb.append(delimiter); }
            Object campo = campos[i];
            if (campo instanceof LocalDateTime) { campo = ((LocalDateTime) campo).truncatedTo(ChronoUnit.SECONDS); }
            sb.append(campo == null ? "" : String.valueOf(campo));
        }
        return sb.toString();
    }

    protected String claveDe(String linea) {
        return separar(linea)[0];
    }

    protected String buscarLinea(String clave) {
        for (String linea : fileManage.getDataOfFile()) {
            if (claveDe(linea).equals(clave)) { return linea; }
        }
        return null;
    }

    protected void guardarLinea(String clave, String nuevaLinea) {
        ArrayList<String> lineas = fileManage.getDataOfFile();
        for (int i = 0; i < lineas.size(); i++) {
            if (claveDe(lineas.get(i)).equals(clave)) {
                lineas.set(i, nuevaLinea);
                fileManage.writeAllLinesInFile(lineas);
                return;
            }
        }
        fileManage.writeFile(nuevaLinea);
    }

    protected void eliminarPorClave(String clave) {
        reemplazarGrupo(clave, new ArrayList<>());
    }

    protected ArrayList<String> lineasDe(String clave) {
        ArrayList<String> resultado = new ArrayList<>();
        for (String linea : fileManage.getDataOfFile()) {
            if (claveDe(linea).equals(clave)) { resultado.add(linea); }
        }
        return resultado;
    }

    // Quita las líneas de esa clave y escribe las nuevas en una sola escritura (RNF-03), sin dejar duplicados.
    protected void reemplazarGrupo(String clave, List<String> nuevasLineas) {
        ArrayList<String> lineas = new ArrayList<>();
        for (String linea : fileManage.getDataOfFile()) {
            if (!claveDe(linea).equals(clave)) { lineas.add(linea); }
        }
        lineas.addAll(nuevasLineas);
        fileManage.writeAllLinesInFile(lineas);
    }

    protected static String vacioANull(String valor) {
        return (valor == null || valor.isEmpty()) ? null : valor;
    }
}
