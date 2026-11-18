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

/**
 * Pagos de cada orden sobre pagos.txt (RF-17, RNF-05).
 * Formato: codigoOrden|metodo|monto|fecha|estado|referencia
 *
 * RNF-05: de la tarjeta solo se guarda la referencia de autorización; el número y el código de seguridad
 * nunca llegan al sistema, así que tampoco al archivo.
 *
 * POLIMORFISMO: al guardar se usa getMetodoPago() y getReferencia() sin saber el tipo de pago;
 * al leer se crea el objeto del tipo correcto (Efectivo, Tarjeta o Transferencia) en una variable de tipo Pago.
 */
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
