import java.time.LocalDate;

public class Transferencia extends Pago {

    private String referenciaBancaria; // identifica la transferencia en el banco

    public Transferencia(double monto, LocalDate fecha, String referenciaBancaria) {
        super(monto, fecha); // Pago valida y guarda monto y fecha
        this.referenciaBancaria = // la referencia no puede ser nula ni vacía
                validarTextoObligatorio(referenciaBancaria, "La referencia bancaria");
    }

    public String getReferenciaBancaria() {
        return referenciaBancaria;
    }

    public void setReferenciaBancaria(String referenciaBancaria) {
        this.referenciaBancaria = // misma validación al modificarla
                validarTextoObligatorio(referenciaBancaria, "La referencia bancaria");
    }

    @Override
    public String getMetodoPago() {
        return "Transferencia";
    }

    @Override
    public String getDetalle() {
        return "Ref. bancaria: " + referenciaBancaria;
    }
}
