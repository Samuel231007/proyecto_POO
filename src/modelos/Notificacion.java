package modelos;

import enums.EstadoEnvio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registro de un aviso al cliente cuando cambia el estado de su orden. No lo envía: lo hace un canal Notificable.
 * Apoya RF-19, RNF-04 y RNF-06.
 */
public class Notificacion {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static int totalNotificaciones = 0;   // atributo estático: numera las notificaciones

    private final int id;
    private final String codigoOrden, documentoCliente, nombreCliente, telefonoCliente, correoCliente, mensaje;
    private final LocalDateTime fecha;
    private String canal = "";
    private EstadoEnvio estadoEnvio = EstadoEnvio.PENDIENTE;
    private String detalle = "Aún no se ha enviado.";

    public Notificacion(String codigoOrden, String documentoCliente, String nombreCliente,
                        String telefonoCliente, String correoCliente, String mensaje) {
        this.codigoOrden = Validar.texto(codigoOrden, "código de la orden");
        this.documentoCliente = Validar.texto(documentoCliente, "documento del cliente");
        this.nombreCliente = Validar.texto(nombreCliente, "nombre del cliente");
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.mensaje = Validar.texto(mensaje, "mensaje");
        this.fecha = LocalDateTime.now();
        this.id = ++totalNotificaciones;
    }

    // RNF-01 (sobrecarga de constructores): el repositorio la reconstruye tal como quedó registrada.
    // Ajusta el contador para que las notificaciones nuevas no repitan un ID ya guardado.
    public Notificacion(int id, String codigoOrden, String documentoCliente, String nombreCliente,
                        String telefonoCliente, String correoCliente, String mensaje, LocalDateTime fecha,
                        String canal, EstadoEnvio estadoEnvio, String detalle) {
        this.codigoOrden = Validar.texto(codigoOrden, "código de la orden");
        this.documentoCliente = Validar.texto(documentoCliente, "documento del cliente");
        this.nombreCliente = Validar.texto(nombreCliente, "nombre del cliente");
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.mensaje = Validar.texto(mensaje, "mensaje");
        Validar.objeto(fecha, "fecha");
        Validar.objeto(estadoEnvio, "estado de envío");
        this.fecha = fecha;
        this.id = id;
        this.canal = canal == null ? "" : canal;
        this.estadoEnvio = estadoEnvio;
        this.detalle = detalle == null ? "" : detalle;
        if (id > totalNotificaciones) { totalNotificaciones = id; }
    }

    // Método estático: arma el texto del aviso en lenguaje claro (RNF-06).
    public static String crearMensajeCambioEstado(String nombreCliente, String codigoOrden,
                                                  String estadoAnterior, String estadoNuevo) {
        return "Hola " + nombreCliente + ", tu orden " + codigoOrden + " cambió de " + estadoAnterior + " a " + estadoNuevo + ".";
    }

    public static int getTotalNotificaciones() { return totalNotificaciones; }

    // El canal la marca una sola vez (RNF-04: el registro no se altera).
    public void marcarEnviada(String canal, String detalle) { procesar(canal, EstadoEnvio.ENVIADA, detalle); }
    public void marcarFallida(String canal, String causa) { procesar(canal, EstadoEnvio.FALLIDA, causa); }

    private void procesar(String canal, EstadoEnvio nuevo, String detalle) {
        if (estadoEnvio != EstadoEnvio.PENDIENTE) {
            throw new IllegalStateException("La notificación " + id + " ya fue procesada y no se puede modificar.");
        }
        this.canal = canal;
        this.estadoEnvio = nuevo;
        this.detalle = detalle;
    }

    public boolean fueEnviada() { return estadoEnvio == EstadoEnvio.ENVIADA; }

    public int getId() { return id; }
    public String getCodigoOrden() { return codigoOrden; }
    public String getDocumentoCliente() { return documentoCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public String getCorreoCliente() { return correoCliente; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public String getCanal() { return canal; }
    public EstadoEnvio getEstadoEnvio() { return estadoEnvio; }
    public String getDetalle() { return detalle; }

    @Override
    public String toString() {
        return "Notificación " + id + " | Orden " + codigoOrden + " | " + fecha.format(FORMATO_FECHA)
                + " | " + canal + " | " + estadoEnvio + " | " + detalle;
    }
}
