package modelos;


import java.time.LocalDate;

public class Transferencia extends Pago {
    private final String referenciaBancaria;   // identifica la transferencia en el banco

    public Transferencia(double monto, LocalDate fecha, String referenciaBancaria) {
        super(monto, fecha);
        this.referenciaBancaria = Validar.texto(referenciaBancaria, "referencia bancaria");
    }

    public String getReferenciaBancaria() { return referenciaBancaria; }

    @Override
    public String getReferencia() { return referenciaBancaria; }

    @Override
    public String getMetodoPago() { return "Transferencia"; }

    @Override
    public String getDetalle() { return "Ref. bancaria: " + referenciaBancaria; }
}
