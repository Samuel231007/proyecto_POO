/**
 * Canal de WhatsApp. Implementa Notificable.
 * El envío es simulado: revisa el celular del cliente y deja el resultado en la notificación.
 * Apoya RF-19 y RNF-06.
 */
public class NotificacionWhatsApp implements Notificable {

    private static final String NOMBRE_CANAL = "WhatsApp";

    @Override
    public String getNombreCanal() {
        return NOMBRE_CANAL;
    }

    @Override
    public boolean enviar(Notificacion notificacion) {
        String celular = notificacion.getTelefonoCliente();
        if (!esCelularValido(celular)) {
            notificacion.marcarFallida(NOMBRE_CANAL, "No se pudo enviar por WhatsApp: el número \""
                    + celular + "\" no es un celular válido (debe tener 10 dígitos y empezar por 3).");
            return false;
        }
        notificacion.marcarEnviada(NOMBRE_CANAL, "Enviado por WhatsApp al " + celular + ".");
        return true;
    }

    /** Un celular colombiano tiene 10 dígitos y empieza por 3. */
    private boolean esCelularValido(String celular) {
        if (celular == null || celular.length() != 10 || celular.charAt(0) != '3') {
            return false;
        }
        for (int i = 0; i < celular.length(); i++) {
            if (!Character.isDigit(celular.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
