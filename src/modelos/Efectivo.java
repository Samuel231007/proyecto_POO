package modelos;

import java.time.LocalDate;

// Pago en efectivo: no tiene referencia.
public class Efectivo extends Pago {
    public Efectivo(double monto, LocalDate fecha) { super(monto, fecha); }

    @Override
    public String getMetodoPago() { return "Efectivo"; }

    @Override
    public String getDetalle() { return "Sin referencia"; }
}
