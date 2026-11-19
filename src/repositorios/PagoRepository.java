package repositorios;

import enums.EstadoPago;
import interfaces.IRepositorioDependiente;
import modelos.Efectivo;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Tarjeta;
import modelos.Transferencia;

import java.time.LocalDate;
import java.util.ArrayList;

// Formato: codigoOrden|metodo|monto|fecha|estado|referencia
public class PagoRepository extends BaseRepository implements IRepositorioDependiente<OrdenServicio> {
    public PagoRepository() {
        super("pagos.txt", "|");
    }

    @Override
    public void guardarDe(OrdenServicio orden) {
        ArrayList<String> lineas = new ArrayList<>();
        for (Pago p : orden.getPagosParaGuardar()) {
            lineas.add(unir(orden.getCodigo(), p.getMetodoPago(), p.getMonto(), p.getFecha(), p.getEstado().name(), p.getReferencia()));
        }
        reemplazarGrupo(orden.getCodigo(), lineas);
    }

    @Override
    public void cargarEn(OrdenServicio orden) {
        for (String linea : lineasDe(orden.getCodigo())) {
            String[] c = separar(linea);
            Pago pago = crearPago(c[1], Double.parseDouble(c[2]), LocalDate.parse(c[3]), c[5]);
            pago.restaurarEstado(EstadoPago.valueOf(c[4]));
            orden.restaurarPago(pago);
        }
    }

    @Override
    public void eliminarDe(String codigoOrden) {
        eliminarPorClave(codigoOrden);
    }

    private Pago crearPago(String metodo, double monto, LocalDate fecha, String referencia) {
        switch (metodo) {
            case "Tarjeta": return new Tarjeta(monto, fecha, referencia);
            case "Transferencia": return new Transferencia(monto, fecha, referencia);
            case "Efectivo": return new Efectivo(monto, fecha);
            default: throw new IllegalStateException("Método de pago desconocido en pagos.txt: " + metodo);
        }
    }
}
