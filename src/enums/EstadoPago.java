package enums;

// Estados de un pago (RF-17): todo pago empieza PENDIENTE y el administrador lo confirma o rechaza.
public enum EstadoPago {
    PENDIENTE("Pendiente"),
    CONFIRMADO("Confirmado"),
    RECHAZADO("Rechazado");

    private final String descripcion;

    EstadoPago(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcion() { return descripcion; }
}
