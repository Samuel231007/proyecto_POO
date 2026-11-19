package repositorios;

import modelos.Garantia;

import java.time.LocalDate;
import java.util.ArrayList;

// Formato: codigoOrden|fecha|problema
public class RevisionGarantiaRepository extends BaseRepository {
    public RevisionGarantiaRepository() {
        super("revisiones_garantia.txt", "|");
    }

    public void guardar(String codigoOrden, Garantia garantia) {
        ArrayList<String> lineas = new ArrayList<>();
        for (String revision : garantia.getRevisiones()) {
            // Garantia guarda cada revisión como "fecha - problema"
            String[] partes = revision.split(" - ", 2);
            lineas.add(unir(codigoOrden, partes[0], partes.length > 1 ? partes[1] : ""));
        }
        reemplazarGrupo(codigoOrden, lineas);
    }

    // Vuelve a registrar cada revisión con su fecha original; la garantía sigue vigente para esa fecha.
    public void cargarEn(String codigoOrden, Garantia garantia) {
        for (String linea : lineasDe(codigoOrden)) {
            String[] c = separar(linea);
            garantia.solicitarRevision(LocalDate.parse(c[1]), c[2]);
        }
    }

    public void eliminar(String codigoOrden) {
        eliminarPorClave(codigoOrden);
    }
}
