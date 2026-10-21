import java.time.LocalDate;

// Clase abstracta que reúne los datos y operaciones comunes de los pagos.
public abstract class Pago {

    // Estados posibles para un pago.
    public enum Estado {
        PENDIENTE,
        CONFIRMADO,
        RECHAZADO
    }

    private final double monto;    // No cambia después de crear el pago.
    private final LocalDate fecha; // Fecha en que se registró el pago.
    private Estado estado;         // Cambia al confirmar o rechazar el pago.

    // Valida los datos comunes y deja todo pago nuevo como pendiente.
    protected Pago(double monto, LocalDate fecha) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }

        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }

        this.monto = monto;
        this.fecha = fecha;
        this.estado = Estado.PENDIENTE;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    // Confirma el pago solo si todavía está pendiente.
    public void confirmarPago() {
        exigirPendiente("confirmar");
        estado = Estado.CONFIRMADO;
    }

    // Rechaza el pago solo si todavía está pendiente.
    public void rechazarPago() {
        exigirPendiente("rechazar");
        estado = Estado.RECHAZADO;
    }

    public boolean estaPendiente() {
        return estado == Estado.PENDIENTE;
    }

    public boolean estaConfirmado() {
        return estado == Estado.CONFIRMADO;
    }

    // Cada tipo de pago indica su nombre y sus datos particulares.
    public abstract String getMetodoPago();

    public abstract String getDetalle();

    // Evita confirmar o rechazar un pago más de una vez.
    private void exigirPendiente(String accion) {
        if (estado != Estado.PENDIENTE) {
            throw new IllegalStateException(
                    "No se puede " + accion + " un pago que ya está "
                            + estado.name().toLowerCase() + "."
            );
        }
    }

    // Rechaza textos nulos, vacíos o formados solo por espacios.
    protected static String validarTextoObligatorio(
            String valor, String nombreCampo) {

        if (valor == null || valor.trim().length() == 0) {
            throw new IllegalArgumentException(
                    nombreCampo + " no puede estar vacío."
            );
        }

        return valor.trim();
    }

    // Devuelve un resumen del pago con los datos de su tipo específico.
    @Override
    public String toString() {
        return getMetodoPago() + " , $" + monto + " , " + fecha
                + " , " + estado + " , " + getDetalle();
    }
}