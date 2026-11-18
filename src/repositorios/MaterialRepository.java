package repositorios;

import modelos.DetalleTrabajo.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Materiales usados en cada orden sobre materiales.txt (RF-15, RF-16).
 * Formato: codigoOrden|descripcion|costoUnitario|cantidad
 *
 * Lo usa DetalleTrabajoRepository (composición), porque el material pertenece al detalle del trabajo.
 */
public class MaterialRepository extends BaseRepository {

    public MaterialRepository() {
        super("materiales.txt", "|");
    }

    public void guardar(String codigoOrden, List<Material> materiales) {
        ArrayList<String> lineas = new ArrayList<>();
        for (Material m : materiales) {
            lineas.add(unir(codigoOrden, m.getDescripcion(), m.getCostoUnitario(), m.getCantidad()));
        }
        reemplazarGrupo(codigoOrden, lineas);
    }

    public ArrayList<Material> cargar(String codigoOrden) {
        ArrayList<Material> materiales = new ArrayList<>();
        for (String linea : lineasDe(codigoOrden)) {
            String[] c = separar(linea);
            materiales.add(new Material(c[1], Double.parseDouble(c[2]), Double.parseDouble(c[3])));
        }
        return materiales;
    }

    public void eliminar(String codigoOrden) {
        eliminarPorClave(codigoOrden);
    }
}
