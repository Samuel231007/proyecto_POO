package modelos;

import interfaces.Notificable;

/** Canal de correo (envío simulado): revisa el correo del cliente y deja el resultado en la notificación. */
public class NotificacionCorreo implements Notificable {

    @Override
    public String getNombreCanal() { return "Correo"; }

    @Override
    public boolean enviar(Notificacion notificacion) {
        String correo = notificacion.getCorreoCliente();
        if (!esCorreoValido(correo)) {
            notificacion.marcarFallida(getNombreCanal(), "No se pudo enviar el correo: la dirección \"" + correo
                    + "\" no es válida (debe ser como usuario@dominio.com).");
            return false;
        }
        notificacion.marcarEnviada(getNombreCanal(), "Enviado al correo " + correo
                + " con el asunto \"Actualización de tu orden " + notificacion.getCodigoOrden() + "\".");
        return true;
    }

    // Válido si tiene una @ y después de ella un punto.
    private boolean esCorreoValido(String correo) {
        if (correo == null) { return false; }
        int arroba = correo.indexOf('@');
        return arroba > 0 && correo.indexOf('.', arroba) > arroba + 1 && !correo.endsWith(".");
    }
}
