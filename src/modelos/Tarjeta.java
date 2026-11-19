package modelos;

import java.time.LocalDate;

public class Tarjeta extends Pago {
    private final String referenciaAutorizacion;   // RNF-05: se guarda la referencia, nunca el número ni el código de la tarjeta

    public Tarjeta(double monto, LocalDate fecha, String referenciaAutorizacion) {
        super(monto, fecha);
        this.referenciaAutorizacion = Validar.texto(referenciaAutorizacion, "referencia de autorización");
    }

    public String getReferenciaAutorizacion() { return referenciaAutorizacion; }

    @Override
    public String getReferencia() { return referenciaAutorizacion; }

    @Override
    public String getMetodoPago() { return "Tarjeta"; }

    @Override
    public String getDetalle() { return "Ref. autorización: " + referenciaAutorizacion; }
}
