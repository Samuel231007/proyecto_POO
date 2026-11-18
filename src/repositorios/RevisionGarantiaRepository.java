package repositorios;

import modelos.Garantia;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Revisiones por garantía de cada orden sobre revisiones_garantia.txt (RF-18).
 * Formato: codigoOrden|fecha|problema
 *
 * Lo usa GarantiaRepository (composición). Al leer se vuelve a llamar a solicitarRevision:
 * como la fecha es la misma que se guardó, la garantía sigue siendo vigente para esa fecha.
 */
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
