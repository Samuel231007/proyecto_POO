import java.time.LocalDate;

public class Efectivo extends Pago {

    public Efectivo(double monto, LocalDate fecha) {
        super(monto, fecha); // Pago valida y guarda monto y fecha
    }

    @Override
    public String getMetodoPago() {
        return "Efectivo";
    }

    @Override
    public String getDetalle() {
        return "Sin referencia"; // el efectivo no tiene referencia
    }
}
