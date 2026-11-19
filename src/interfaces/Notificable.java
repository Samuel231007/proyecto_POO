package interfaces;

import modelos.Notificacion;

// Canal por el que se avisa al cliente (RF-19). Lo implementan NotificacionWhatsApp y NotificacionCorreo.
public interface Notificable {
    String getNombreCanal();

    // Envía (simulado) la notificación; la deja ENVIADA o FALLIDA y devuelve true si se pudo enviar.
    boolean enviar(Notificacion notificacion);
}
