package repositorios;

import interfaces.IRepositorioDependiente;
import modelos.Calificacion;
import modelos.OrdenServicio;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Calificación de cada orden sobre calificaciones.txt (RF-10).
 * Formato: codigoOrden|estrellas|comentario|fecha
 */
public class CalificacionRepository extends BaseRepository implements IRepositorioDependiente<OrdenServicio> {

    public CalificacionRepository() {
        super("calificaciones.txt", "|");
    }

    @Override
    public void guardarDe(OrdenServicio orden) {
        Calificacion c = orden.getCalificacion();
        ArrayList<String> lineas = new ArrayList<>();
        if (c != null) {
            lineas.add(unir(orden.getCodigo(), c.getEstrellas(), c.getComentario(), c.getFecha()));
        }
        reemplazarGrupo(orden.getCodigo(), lineas);
    }

    @Override
    public void cargarEn(OrdenServicio orden) {
        String linea = buscarLinea(orden.getCodigo());
        if (linea == null) { return; }
        String[] c = separar(linea);
        // calificar() revisa que la orden esté FINALIZADA y sin calificación: al cargar siempre se cumple.
        orden.calificar(new Calificacion(Integer.parseInt(c[1]), c[2], LocalDateTime.parse(c[3])));
    }

    @Override
    public void eliminarDe(String codigoOrden) {
        eliminarPorClave(codigoOrden);
    }
}
