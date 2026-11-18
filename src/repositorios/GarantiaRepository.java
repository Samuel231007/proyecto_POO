package repositorios;

import interfaces.IRepositorioDependiente;
import modelos.Garantia;
import modelos.OrdenServicio;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Garantía de cada orden finalizada sobre garantias.txt (RF-07).
 * Formato: codigoOrden|fechaInicio|diasVigencia|cobertura
 *
 * COMPOSICIÓN: TIENE un RevisionGarantiaRepository para las revisiones (RF-18), que se guardan en otro archivo.
 */
public class GarantiaRepository extends BaseRepository implements IRepositorioDependiente<OrdenServicio> {

    private final RevisionGarantiaRepository revisionRepository;

    public GarantiaRepository() {
        super("garantias.txt", "|");
        this.revisionRepository = new RevisionGarantiaRepository();
    }

    @Override
    public void guardarDe(OrdenServicio orden) {
        Garantia g = orden.getGarantia();
        ArrayList<String> lineas = new ArrayList<>();
        if (g != null) {
            lineas.add(unir(orden.getCodigo(), g.getFechaInicio(), g.getDiasVigencia(), g.getCobertura()));
            revisionRepository.guardar(orden.getCodigo(), g);
        } else {
            revisionRepository.eliminar(orden.getCodigo());
        }
        reemplazarGrupo(orden.getCodigo(), lineas);
    }

    @Override
    public void cargarEn(OrdenServicio orden) {
        String linea = buscarLinea(orden.getCodigo());
        if (linea == null) { return; }   // la orden todavía no tiene garantía
        String[] c = separar(linea);
        Garantia garantia = new Garantia(LocalDate.parse(c[1]), Integer.parseInt(c[2]), c[3]);
        revisionRepository.cargarEn(orden.getCodigo(), garantia);
        orden.restaurarGarantia(garantia);
    }

    @Override
    public void eliminarDe(String codigoOrden) {
        eliminarPorClave(codigoOrden);
        revisionRepository.eliminar(codigoOrden);
    }
}
