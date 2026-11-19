package interfaces;

import modelos.Notificacion;

public interface Notificable {
    String getNombreCanal();

    // Envía (simulado) la notificación; la deja ENVIADA o FALLIDA y devuelve true si se pudo enviar.
    boolean enviar(Notificacion notificacion);
}
