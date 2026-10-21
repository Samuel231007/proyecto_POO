/**
 * Contrato que cumple todo canal capaz de enviar una notificación al cliente.
 * NotificacionWhatsApp y NotificacionCorreo implementan esta interfaz,
 * así quien envía el aviso no necesita saber por qué canal sale (polimorfismo).
 * Apoya RF-19.
 */
public interface Notificable {

    /** Devuelve el nombre del canal, por ejemplo "WhatsApp" o "Correo". */
    String getNombreCanal();

    /**
     * Envía (de forma simulada) la notificación por este canal.
     * Deja la notificación marcada como ENVIADA o FALLIDA y devuelve true si se pudo enviar.
     */
    boolean enviar(Notificacion notificacion);
}
