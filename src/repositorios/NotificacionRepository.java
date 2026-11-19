package repositorios;

import enums.EstadoEnvio;
import interfaces.IRepositorioDependiente;
import modelos.Notificacion;
import modelos.OrdenServicio;

import java.time.LocalDateTime;
import java.util.ArrayList;

// Formato: codigoOrden|id|documentoCliente|nombreCliente|telefono|correo|mensaje|fecha|canal|estadoEnvio|detalle
public class NotificacionRepository extends BaseRepository implements IRepositorioDependiente<OrdenServicio> {
    public NotificacionRepository() {
        super("notificaciones.txt", "|");
    }

    @Override
    public void guardarDe(OrdenServicio orden) {
        ArrayList<String> lineas = new ArrayList<>();
        for (Notificacion n : orden.getNotificaciones()) {
            lineas.add(unir(orden.getCodigo(), n.getId(), n.getDocumentoCliente(), n.getNombreCliente(),
                    n.getTelefonoCliente(), n.getCorreoCliente(), n.getMensaje(), n.getFecha(),
                    n.getCanal(), n.getEstadoEnvio().name(), n.getDetalle()));
        }
        reemplazarGrupo(orden.getCodigo(), lineas);
    }

    @Override
    public void cargarEn(OrdenServicio orden) {
        for (String linea : lineasDe(orden.getCodigo())) {
            String[] c = separar(linea);
            orden.restaurarNotificacion(new Notificacion(Integer.parseInt(c[1]), c[0], c[2], c[3], c[4], c[5], c[6],
                    LocalDateTime.parse(c[7]), c[8], EstadoEnvio.valueOf(c[9]), c[10]));
        }
    }

    @Override
    public void eliminarDe(String codigoOrden) {
        eliminarPorClave(codigoOrden);
    }
}
