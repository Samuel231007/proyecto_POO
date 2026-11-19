package enums;

public enum EstadoPago {
    PENDIENTE("Pendiente"),
    CONFIRMADO("Confirmado"),
    RECHAZADO("Rechazado");

    private final String descripcion;

    EstadoPago(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcion() { return descripcion; }
}
