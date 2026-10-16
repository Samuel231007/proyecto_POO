import java.time.LocalDate;

public class Tarjeta extends Pago {

    private String referenciaAutorizacion; // se guarda la referencia, nunca el número de la tarjeta

    public Tarjeta(double monto, LocalDate fecha, String referenciaAutorizacion) {
        super(monto, fecha); // Pago valida y guarda monto y fecha
        this.referenciaAutorizacion = // la referencia no puede ser nula ni vacía
                validarTextoObligatorio(referenciaAutorizacion, "La referencia de autorización");
    }

    public String getReferenciaAutorizacion() {
        return referenciaAutorizacion;
    }

    public void setReferenciaAutorizacion(String referenciaAutorizacion) {
        this.referenciaAutorizacion = // misma validación al modificarla
                validarTextoObligatorio(referenciaAutorizacion, "La referencia de autorización");
    }

    @Override
    public String getMetodoPago() {
        return "Tarjeta";
    }

    @Override
    public String getDetalle() {
        return "Ref. autorización: " + referenciaAutorizacion;
    }
}
