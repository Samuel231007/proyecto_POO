package enums;

// Resultado del envío de un aviso al cliente (RF-19).
public enum EstadoEnvio {
    PENDIENTE("Pendiente"),
    ENVIADA("Enviada"),
    FALLIDA("Fallida");

    private final String descripcion;

    EstadoEnvio(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcion() { return descripcion; }
}
