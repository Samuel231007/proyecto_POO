package modelos;

public class Administrador extends Usuario {
    private String areaResponsable;

    public Administrador(String nombre, String documento, String telefono,
                         String contrasenia, String correo, String areaResponsable) {
        super(nombre, documento, correo, telefono, contrasenia);
        this.areaResponsable = Validar.texto(areaResponsable, "área responsable");
    }

    // RNF-01: constructor para el repositorio (la contraseña llega ya cifrada desde el archivo).
    public Administrador(String nombre, String documento, String telefono, String contrasenia,
                         String correo, String areaResponsable, boolean contraseniaYaCifrada) {
        super(nombre, documento, correo, telefono, contrasenia, contraseniaYaCifrada);
        this.areaResponsable = Validar.texto(areaResponsable, "área responsable");
    }

    // RF-14: el administrador reasigna la orden y queda como responsable del cambio (RNF-04).
    public boolean reasignarOrden(OrdenServicio orden, Tecnico nuevo) {
        return orden.getAsignacion().reasignarTecnico(nuevo, getNombre());
    }

    @Override
    public String getRol() { return "Administrador"; }

    public String getAreaResponsable() { return areaResponsable; }
    public void setAreaResponsable(String areaResponsable) { this.areaResponsable = Validar.texto(areaResponsable, "área responsable"); }
}
