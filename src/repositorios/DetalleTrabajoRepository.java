package repositorios;

import interfaces.IRepositorioDependiente;
import modelos.DetalleTrabajo;
import modelos.OrdenServicio;

import java.util.ArrayList;

/**
 * Detalle del trabajo de cada orden sobre detalles_trabajo.txt (RF-15, RF-16).
 * Formato: codigoOrden|descripcion|horasTrabajo|actividad1;actividad2;actividad3
 *
 * COMPOSICIÓN: TIENE un MaterialRepository, porque los materiales se guardan aparte en materiales.txt
 * (una orden puede tener muchos materiales).
 */
public class DetalleTrabajoRepository extends BaseRepository implements IRepositorioDependiente<OrdenServicio> {

    private static final String SEPARADOR_ACTIVIDADES = ";";
    private final MaterialRepository materialRepository;

    public DetalleTrabajoRepository() {
        super("detalles_trabajo.txt", "|");
        this.materialRepository = new MaterialRepository();
    }

    @Override
    public void guardarDe(OrdenServicio orden) {
        DetalleTrabajo d = orden.getDetalle();
        ArrayList<String> lineas = new ArrayList<>();
        if (d != null) {
            lineas.add(unir(orden.getCodigo(), d.getDescripcion(), d.getHorasTrabajo(),
                    String.join(SEPARADOR_ACTIVIDADES, d.getActividades())));
            materialRepository.guardar(orden.getCodigo(), d.getMateriales());
        } else {
            materialRepository.eliminar(orden.getCodigo());
        }
        reemplazarGrupo(orden.getCodigo(), lineas);
    }

    @Override
    public void cargarEn(OrdenServicio orden) {
        String linea = buscarLinea(orden.getCodigo());
        if (linea == null) { return; }   // la orden todavía no tiene detalle
        String[] c = separar(linea);
        DetalleTrabajo detalle = new DetalleTrabajo();
        detalle.setDescripcion(c[1]);
        detalle.setHorasTrabajo(Double.parseDouble(c[2]));
        if (!c[3].isEmpty()) {
            for (String actividad : c[3].split(SEPARADOR_ACTIVIDADES)) { detalle.agregarActividad(actividad); }
        }
        for (DetalleTrabajo.Material m : materialRepository.cargar(orden.getCodigo())) { detalle.registrarMaterial(m); }
        orden.restaurarDetalle(detalle);
    }

    @Override
    public void eliminarDe(String codigoOrden) {
        eliminarPorClave(codigoOrden);
        materialRepository.eliminar(codigoOrden);
    }
}
