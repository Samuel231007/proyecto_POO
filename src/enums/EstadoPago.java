package enums;

/** Estados de un pago (RF-17). Antes era Pago.Estado. */
public enum EstadoPago {
    PENDIENTE("Pendiente"),
    CONFIRMADO("Confirmado"),
    RECHAZADO("Rechazado");

    private final String descripcion;

    EstadoPago(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcion() { return descripcion; }
}
