package enums;

/** Resultado del envío de una notificación (RF-19). Antes era Notificacion.EstadoEnvio. */
public enum EstadoEnvio {
    PENDIENTE("Pendiente"),
    ENVIADA("Enviada"),
    FALLIDA("Fallida");

    private final String descripcion;

    EstadoEnvio(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcion() { return descripcion; }
}
