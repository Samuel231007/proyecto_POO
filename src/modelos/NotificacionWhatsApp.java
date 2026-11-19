package modelos;

import interfaces.Notificable;

// Canal de WhatsApp (envío simulado): revisa el celular y deja el resultado en la notificación.
public class NotificacionWhatsApp implements Notificable {
    @Override
    public String getNombreCanal() { return "WhatsApp"; }

    @Override
    public boolean enviar(Notificacion notificacion) {
        String celular = notificacion.getTelefonoCliente();
        if (!esCelularValido(celular)) {
            notificacion.marcarFallida(getNombreCanal(), "No se pudo enviar por WhatsApp: el número \"" + celular
                    + "\" no es un celular válido (debe tener 10 dígitos y empezar por 3).");
            return false;
        }
        notificacion.marcarEnviada(getNombreCanal(), "Enviado por WhatsApp al " + celular + ".");
        return true;
    }

    // Un celular colombiano tiene 10 dígitos y empieza por 3.
    private boolean esCelularValido(String celular) {
        if (celular == null || celular.length() != 10 || celular.charAt(0) != '3') { return false; }
        for (int i = 0; i < celular.length(); i++) {
            if (!Character.isDigit(celular.charAt(i))) { return false; }
        }
        return true;
    }
}
