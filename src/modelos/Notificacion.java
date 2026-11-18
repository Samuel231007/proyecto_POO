import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registro de un aviso enviado al cliente cuando cambia el estado de su orden.
 * Guarda el contenido, el destinatario, la orden, el canal y el resultado del envío.
 * No envía el mensaje por sí misma: eso lo hace un canal que implementa Notificable.
 * Apoya RF-19, RNF-04 y RNF-06.
 */
public class Notificacion {

    /** Estados posibles del envío (clase anidada de tipo enum). */
    public enum EstadoEnvio {
        PENDIENTE,
        ENVIADA,
        FALLIDA
    }

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Atributo estático: lo comparten todas las notificaciones y sirve para numerarlas.
    private static int totalNotificaciones = 0;

    private int id;
    private String codigoOrden;
    private String documentoCliente;
    private String nombreCliente;
    private String telefonoCliente;
    private String correoCliente;
    private String mensaje;
    private LocalDateTime fecha;
    private String canal;
    private EstadoEnvio estadoEnvio;
    private String detalle;

    /** Crea un aviso nuevo en estado PENDIENTE con la fecha y hora actuales. */
    public Notificacion(String codigoOrden, String documentoCliente, String nombreCliente,
                        String telefonoCliente, String correoCliente, String mensaje) {
        this.codigoOrden = validarTextoObligatorio(codigoOrden, "El código de la orden");
        this.documentoCliente = validarTextoObligatorio(documentoCliente, "El documento del cliente");
        this.nombreCliente = validarTextoObligatorio(nombreCliente, "El nombre del cliente");
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.mensaje = validarTextoObligatorio(mensaje, "El mensaje");
        this.fecha = LocalDateTime.now();
        this.canal = "";
        this.estadoEnvio = EstadoEnvio.PENDIENTE;
        this.detalle = "Aún no se ha enviado.";
        totalNotificaciones++;
        this.id = totalNotificaciones;
    }

    /**
     * Método estático: arma el texto del aviso de cambio de estado en lenguaje claro (RNF-06).
     * Es estático porque no necesita los datos de una notificación en particular.
     */
    public static String crearMensajeCambioEstado(String nombreCliente, String codigoOrden,
                                                  String estadoAnterior, String estadoNuevo) {
        return "Hola " + nombreCliente + ", tu orden " + codigoOrden + " cambió de "
                + estadoAnterior + " a " + estadoNuevo + ".";
    }

    /** Método estático: cuántas notificaciones se han creado en total. */
    public static int getTotalNotificaciones() {
        return totalNotificaciones;
    }

    /** El canal la marca como enviada. Solo se puede hacer una vez (RNF-04: el registro no se altera). */
    public void marcarEnviada(String canal, String detalle) {
        verificarPendiente();
        this.canal = canal;
        this.estadoEnvio = EstadoEnvio.ENVIADA;
        this.detalle = detalle;
    }

    /** El canal la marca como fallida y guarda la causa para mostrarla al usuario (RNF-06). */
    public void marcarFallida(String canal, String causa) {
        verificarPendiente();
        this.canal = canal;
        this.estadoEnvio = EstadoEnvio.FALLIDA;
        this.detalle = causa;
    }

    public boolean fueEnviada() {
        return estadoEnvio == EstadoEnvio.ENVIADA;
    }

    public int getId() {
        return id;
    }

    public String getCodigoOrden() {
        return codigoOrden;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getCanal() {
        return canal;
    }

    public EstadoEnvio getEstadoEnvio() {
        return estadoEnvio;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        return "Notificación " + id + " | Orden " + codigoOrden + " | " + fecha.format(FORMATO_FECHA)
                + " | " + canal + " | " + estadoEnvio + " | " + detalle;
    }

    private void verificarPendiente() {
        if (estadoEnvio != EstadoEnvio.PENDIENTE) {
            throw new IllegalStateException("La notificación " + id + " ya fue procesada y no se puede modificar.");
        }
    }

    private static String validarTextoObligatorio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " es obligatorio.");
        }
        return valor.trim();
    }
}
