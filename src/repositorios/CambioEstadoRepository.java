package repositorios;

import enums.EstadoOrden;
import interfaces.IRepositorioDependiente;
import modelos.OrdenServicio;
import modelos.OrdenServicio.CambioEstado;

import java.time.LocalDateTime;
import java.util.ArrayList;

// Formato: codigoOrden|estadoAnterior|estadoNuevo|fecha|responsable
public class CambioEstadoRepository extends BaseRepository implements IRepositorioDependiente<OrdenServicio> {
    public CambioEstadoRepository() {
        super("cambios_estado.txt", "|");
    }

    @Override
    public void guardarDe(OrdenServicio orden) {
        ArrayList<String> lineas = new ArrayList<>();
        for (CambioEstado c : orden.getCambios()) {
            lineas.add(unir(orden.getCodigo(), c.getEstadoAnterior().name(), c.getEstadoNuevo().name(),
                    c.getFecha(), c.getResponsable()));
        }
        reemplazarGrupo(orden.getCodigo(), lineas);
    }

    @Override
    public void cargarEn(OrdenServicio orden) {
        for (String linea : lineasDe(orden.getCodigo())) {
            String[] c = separar(linea);
            orden.restaurarCambio(EstadoOrden.valueOf(c[1]), EstadoOrden.valueOf(c[2]), LocalDateTime.parse(c[3]), c[4]);
        }
    }

    @Override
    public void eliminarDe(String codigoOrden) {
        eliminarPorClave(codigoOrden);
    }
}
