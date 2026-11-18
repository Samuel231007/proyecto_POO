package modelos;


/**
 * Hereda de Usuario. Reasigna órdenes (RF-14).
 * Registrar técnicos (RF-01) y filtrar órdenes (RF-20) ahora se hacen en servicios.AdministradorServicio,
 * porque la capa de modelos no debe conocer a la capa de servicios (arquitectura por capas del proyecto base).
 */
public class Administrador extends Usuario {
    private String areaResponsable;

    public Administrador(String nombre, String documento, String telefono,
                         String contrasenia, String correo, String areaResponsable) {
        super(nombre, documento, correo, telefono, contrasenia); // orden de Usuario: correo, telefono
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
