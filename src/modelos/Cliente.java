package modelos;

import enums.EstadoOrden;
import interfaces.IImprimible;
import interfaces.Notificable;

import java.time.LocalDate;

public class Cliente extends Usuario implements IImprimible {
    private String ubicacion;
    private Notificable canalNotificacion = new NotificacionWhatsApp();   // RF-19: canal elegido por el cliente

    public Cliente(String ubicacion, String nombre, String documento, String telefono, String correo, String contrasenia) {
        super(nombre, documento, correo, telefono, contrasenia);
        this.ubicacion = Validar.texto(ubicacion, "ubicación");
    }

    // RNF-01: constructor para el repositorio. La contraseña llega cifrada y el canal ya elegido.
    public Cliente(String ubicacion, String nombre, String documento, String telefono, String correo,
                   String contrasenia, boolean contraseniaYaCifrada, Notificable canal) {
        super(nombre, documento, correo, telefono, contrasenia, contraseniaYaCifrada);
        this.ubicacion = Validar.texto(ubicacion, "ubicación");
        Validar.objeto(canal, "canal de notificación");
        this.canalNotificacion = canal;
    }

    // RF-11: actualiza contacto y ubicación. Valida todo antes de cambiar cualquier dato (RNF-03).
    // El documento no se cambia porque identifica al cliente.
    public void actualizarDatos(String nombre, String telefono, String correo, String ubicacion) {
        Validar.texto(nombre, "nombre");
        Validar.texto(telefono, "teléfono");
        Validar.texto(correo, "correo");
        Validar.texto(ubicacion, "ubicación");
        setNombre(nombre);
        setTelefono(telefono);
        setCorreo(correo);
        this.ubicacion = ubicacion.trim();
        System.out.println("Datos actualizados correctamente.");
    }

    // RF-19: el cliente elige si le llegan los avisos por WhatsApp o por correo.
    public void setCanalNotificacion(Notificable canal) {
        Validar.objeto(canal, "canal de notificación");
        this.canalNotificacion = canal;
        System.out.println("Los avisos le llegarán por " + canal.getNombreCanal() + ".");
    }

    public Notificable getCanalNotificacion() { return canalNotificacion; }

    // RF-03: solicitar un servicio.
    public void solicitarServicio(Solicitud solicitud) {
        Validar.objeto(solicitud, "solicitud");
        solicitud.crear();
        System.out.println("Solicitud enviada correctamente.");
    }

    // RF-12: solo se cancela mientras esté pendiente, es decir, sin técnico asignado.
    public boolean cancelarSolicitud(OrdenServicio orden, String motivo) {
        verificarPropia(orden);
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            System.out.println("No se puede cancelar la solicitud: la orden ya está " + orden.getEstado() + ".");
            return false;
        }
        orden.cancelar(getNombre(), motivo);
        System.out.println("Solicitud cancelada correctamente.");
        return true;
    }

    // RF-10: calificar una orden propia que ya esté finalizada.
    public void calificarServicio(OrdenServicio orden, int estrellas, String comentario) {
        verificarPropia(orden);
        orden.calificar(new Calificacion(estrellas, comentario));
        System.out.println("Gracias por calificar el servicio con " + estrellas + " estrella(s).");
    }

    // RF-18: pedir una revisión por garantía de una orden propia.
    public void solicitarRevisionGarantia(OrdenServicio orden, LocalDate fecha, String problema) {
        verificarPropia(orden);
        orden.solicitarRevisionGarantia(fecha, problema);
        System.out.println("Revisión por garantía registrada para la orden " + orden.getCodigo() + ".");
    }

    // RNF-02: un cliente solo actúa sobre sus propias órdenes.
    public void verificarPropia(OrdenServicio orden) {
        Validar.objeto(orden, "orden");
        if (orden.getCliente() != this) { throw new IllegalArgumentException("Solo puede gestionar sus propias órdenes."); }
    }

    public String consultarDatos() {
        return "Nombre: " + getNombre() + "\nDocumento: " + getDocumento() + "\nTeléfono: " + getTelefono()
                + "\nCorreo: " + getCorreo() + "\nUbicación: " + ubicacion + "\nAvisos por: " + canalNotificacion.getNombreCanal();
    }

    public String getUbicacion() { return ubicacion; }

    @Override
    public String getRol() { return "Cliente"; }

    @Override
    public void imprimir() { System.out.println(consultarDatos()); }

    @Override
    public String aLinea() {
        return String.format("%-12s %-22s %-12s %-26s %-12s %s", getDocumento(), getNombre(), getTelefono(),
                getCorreo(), ubicacion, canalNotificacion.getNombreCanal());
    }
}
