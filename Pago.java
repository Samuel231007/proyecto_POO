import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa la información común de cualquier pago.
 * Las formas concretas de pago heredan de esta clase.
 */
public abstract class Pago {

    public enum EstadoPago {
        PENDIENTE,
        CONFIRMADO,
        RECHAZADO
    }

    private double monto;
    private LocalDateTime fecha;
    private EstadoPago estado;

    /** Crea un pago con fecha actual y estado pendiente. */
    protected Pago(double monto) {
        this(monto, LocalDateTime.now(), EstadoPago.PENDIENTE);
    }

    /** Crea un pago con sus datos iniciales. */
    protected Pago(double monto, LocalDateTime fecha, EstadoPago estado) {
        setMonto(monto);
        this.fecha = Objects.requireNonNull(fecha, "La fecha del pago es obligatoria.");
        this.estado = Objects.requireNonNull(estado, "El estado del pago es obligatorio.");
    }

    public double getMonto() {
        return monto;
    }

    public final void setMonto(double monto) {
        if (!Double.isFinite(monto) || monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser un número positivo y finito.");
        }
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = Objects.requireNonNull(fecha, "La fecha del pago es obligatoria.");
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public final void setEstado(EstadoPago estado) {
        this.estado = Objects.requireNonNull(estado, "El estado del pago es obligatorio.");
    }

    /** Devuelve el nombre del medio de pago concreto. */
    public abstract String getMetodoPago();

    /** Valida un texto obligatorio compartido por las subclases. */
    protected static String validarTextoNoVacio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " es obligatorio.");
        }
        return valor.trim();
    }
}
