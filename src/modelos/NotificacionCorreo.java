/**
 * Canal de correo electrónico. Implementa Notificable.
 * El envío es simulado: revisa el correo del cliente y deja el resultado en la notificación.
 * Apoya RF-19 y RNF-06.
 */
public class NotificacionCorreo implements Notificable {

    private static final String NOMBRE_CANAL = "Correo";

    @Override
    public String getNombreCanal() {
        return NOMBRE_CANAL;
    }

    @Override
    public boolean enviar(Notificacion notificacion) {
        String correo = notificacion.getCorreoCliente();
        if (!esCorreoValido(correo)) {
            notificacion.marcarFallida(NOMBRE_CANAL, "No se pudo enviar el correo: la dirección \""
                    + correo + "\" no es válida (debe ser como usuario@dominio.com).");
            return false;
        }
        notificacion.marcarEnviada(NOMBRE_CANAL, "Enviado al correo " + correo
                + " con el asunto \"" + crearAsunto(notificacion) + "\".");
        return true;
    }

    /** El asunto del correo indica la orden a la que se refiere el aviso. */
    private String crearAsunto(Notificacion notificacion) {
        return "Actualización de tu orden " + notificacion.getCodigoOrden();
    }

    /** Validación sencilla: tiene una @ y después de ella un punto. */
    private boolean esCorreoValido(String correo) {
        if (correo == null) {
            return false;
        }
        int arroba = correo.indexOf('@');
        return arroba > 0 && correo.indexOf('.', arroba) > arroba + 1 && !correo.endsWith(".");
    }
}
