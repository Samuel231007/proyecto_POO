package enums;

public enum EstadoOrden {
    PENDIENTE("Pendiente"),
    ASIGNADA("Asignada"),
    EN_PROCESO("En proceso"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoOrden(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcion() { return descripcion; }
}
