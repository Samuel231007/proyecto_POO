package repositorios;

import interfaces.IRepositorioDependiente;
import modelos.Asignacion.Movimiento;
import modelos.OrdenServicio;
import modelos.Tecnico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Asignaciones y reasignaciones de técnicos de cada orden sobre asignaciones.txt (RF-04, RF-14, RNF-04).
 * Formato: codigoOrden|fecha|documentoTecnicoAnterior|documentoTecnicoNuevo|responsable
 *   documentoTecnicoAnterior queda vacío en la primera asignación.
 *
 * El archivo solo guarda los DOCUMENTOS de los técnicos (no los objetos). Al leer, se buscan en la lista
 * de técnicos ya cargada, para que la orden y la asignación apunten al MISMO objeto Tecnico.
 */
public class AsignacionRepository extends BaseRepository implements IRepositorioDependiente<OrdenServicio> {

    private List<Tecnico> tecnicos = new ArrayList<>();   // técnicos ya cargados, para buscar por documento

    public AsignacionRepository() {
        super("asignaciones.txt", "|");
    }

    // Lo llama OrdenServicioRepository antes de cargar las órdenes.
    public void usarTecnicos(List<Tecnico> tecnicosCargados) {
        this.tecnicos = tecnicosCargados;
    }

    @Override
    public void guardarDe(OrdenServicio orden) {
        ArrayList<String> lineas = new ArrayList<>();
        for (Movimiento m : orden.getAsignacion().getMovimientos()) {
            String anterior = m.getTecnicoAnterior() == null ? "" : m.getTecnicoAnterior().getDocumento();
            lineas.add(unir(orden.getCodigo(), m.getFecha(), anterior, m.getTecnicoNuevo().getDocumento(), m.getResponsable()));
        }
        reemplazarGrupo(orden.getCodigo(), lineas);
    }

    @Override
    public void cargarEn(OrdenServicio orden) {
        for (String linea : lineasDe(orden.getCodigo())) {
            String[] c = separar(linea);
            Tecnico anterior = buscarTecnico(vacioANull(c[2]));
            Tecnico nuevo = buscarTecnico(c[3]);
            if (nuevo == null) {
                System.out.println("Aviso: en la orden " + orden.getCodigo() + " se omitió una asignación porque el técnico "
                        + c[3] + " ya no existe.");
                continue;
            }
            orden.getAsignacion().restaurarMovimiento(LocalDateTime.parse(c[1]), anterior, nuevo, c[4]);
        }
    }

    @Override
    public void eliminarDe(String codigoOrden) {
        eliminarPorClave(codigoOrden);
    }

    private Tecnico buscarTecnico(String documento) {
        if (documento == null) { return null; }
        for (Tecnico t : tecnicos) {
            if (t.getDocumento().equals(documento)) { return t; }
        }
        return null;
    }
}
