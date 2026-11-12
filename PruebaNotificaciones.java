/**
 * Pruebas sencillas de Notificacion, Notificable y los canales WhatsApp y Correo (se ejecuta con main).
 * Parte de Juan David Lara.
 * No necesita otras clases del proyecto: se compila solo con las cuatro clases de notificaciones.
 * Apoya RF-19 (aviso al cambiar el estado), RNF-04 (el registro no se altera) y RNF-06 (mensajes claros).
 *
 * Cómo ejecutarla:
 *   javac Notificable.java Notificacion.java NotificacionWhatsApp.java NotificacionCorreo.java PruebaNotificaciones.java
 *   java PruebaNotificaciones
 */
public class PruebaNotificaciones {

    private static int correctas = 0;
    private static int fallidas = 0;

    // Muestra si una comprobación salió bien o mal y lleva la cuenta.
    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            correctas++;
            System.out.println("OK     - " + descripcion);
        } else {
            fallidas++;
            System.out.println("FALLÓ  - " + descripcion);
        }
    }

    // Crea una notificación de prueba con los datos del cliente que se le indiquen.
    private static Notificacion crearAviso(String celular, String correo) {
        return new Notificacion("OS-001", "1012345678", "Ana", celular, correo,
                "Hola Ana, tu orden OS-001 cambió de PENDIENTE a ASIGNADA.");
    }

    public static void main(String[] args) {
        probarNotificacion();
        probarWhatsApp();
        probarCorreo();
        probarPolimorfismo();
        System.out.println();
        System.out.println("Resultado: " + correctas + " pruebas correctas y " + fallidas + " con fallas.");
    }

    // ---------- Notificacion (RF-19, RNF-04 y RNF-06) ----------
    private static void probarNotificacion() {
        System.out.println("=== Notificacion ===");

        Notificacion aviso = crearAviso("3001234567", "ana@correo.com");
        verificar("Un aviso nuevo queda PENDIENTE",
                aviso.getEstadoEnvio() == Notificacion.EstadoEnvio.PENDIENTE && !aviso.fueEnviada());
        verificar("Un aviso nuevo todavía no tiene canal", aviso.getCanal().equals(""));

        // Atributo y método estáticos: el contador lo comparten todos los avisos.
        int antes = Notificacion.getTotalNotificaciones();
        Notificacion otro = crearAviso("3001234567", "ana@correo.com");
        verificar("El contador estático sube de a uno", Notificacion.getTotalNotificaciones() == antes + 1);
        verificar("El id del aviso es el número del contador", otro.getId() == Notificacion.getTotalNotificaciones());

        // Método estático que arma el mensaje en lenguaje claro (RNF-06).
        String mensaje = Notificacion.crearMensajeCambioEstado("Ana", "OS-001", "PENDIENTE", "ASIGNADA");
        verificar("Arma el mensaje del cambio de estado",
                mensaje.equals("Hola Ana, tu orden OS-001 cambió de PENDIENTE a ASIGNADA."));

        Notificacion conEspacios = new Notificacion("  OS-002  ", "1012345678", "Ana", "3001234567",
                "ana@correo.com", "Mensaje");
        verificar("Guarda el código de la orden sin espacios sobrantes",
                conEspacios.getCodigoOrden().equals("OS-002"));

        // Datos obligatorios: deben lanzar IllegalArgumentException.
        boolean huboError = false;
        try {
            new Notificacion("", "1012345678", "Ana", "3001234567", "ana@correo.com", "Mensaje");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite un código de orden vacío", huboError);

        huboError = false;
        try {
            new Notificacion("OS-001", "  ", "Ana", "3001234567", "ana@correo.com", "Mensaje");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite un documento de cliente vacío", huboError);

        huboError = false;
        try {
            new Notificacion("OS-001", "1012345678", null, "3001234567", "ana@correo.com", "Mensaje");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite un nombre de cliente nulo", huboError);

        huboError = false;
        try {
            new Notificacion("OS-001", "1012345678", "Ana", "3001234567", "ana@correo.com", "");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite un mensaje vacío", huboError);

        // RNF-04: el registro no se altera después de procesarlo.
        Notificacion procesada = crearAviso("3001234567", "ana@correo.com");
        procesada.marcarEnviada("WhatsApp", "Enviado");
        huboError = false;
        try {
            procesada.marcarFallida("Correo", "Otro intento");
        } catch (IllegalStateException e) {
            huboError = true;
        }
        verificar("Una notificación ya procesada no se puede modificar", huboError);
        verificar("Tras el intento, conserva su resultado original",
                procesada.fueEnviada() && procesada.getCanal().equals("WhatsApp"));
    }

    // ---------- NotificacionWhatsApp (RF-19 y RNF-06) ----------
    private static void probarWhatsApp() {
        System.out.println();
        System.out.println("=== NotificacionWhatsApp ===");

        NotificacionWhatsApp whatsapp = new NotificacionWhatsApp();
        verificar("El nombre del canal es WhatsApp", whatsapp.getNombreCanal().equals("WhatsApp"));

        Notificacion valida = crearAviso("3001234567", "ana@correo.com");
        boolean resultado = whatsapp.enviar(valida);
        verificar("Con un celular válido devuelve true", resultado);
        verificar("Con un celular válido queda ENVIADA por WhatsApp",
                valida.getEstadoEnvio() == Notificacion.EstadoEnvio.ENVIADA
                        && valida.getCanal().equals("WhatsApp"));
        verificar("El detalle indica a qué número se envió",
                valida.getDetalle().equals("Enviado por WhatsApp al 3001234567."));

        // Celulares inválidos: debe fallar y explicar la causa.
        String[] celularesInvalidos = {"123", "2001234567", "30012345ab", "300123456789", ""};
        for (int i = 0; i < celularesInvalidos.length; i++) {
            Notificacion aviso = crearAviso(celularesInvalidos[i], "ana@correo.com");
            boolean envio = whatsapp.enviar(aviso);
            verificar("Rechaza el celular \"" + celularesInvalidos[i] + "\" y queda FALLIDA",
                    !envio && aviso.getEstadoEnvio() == Notificacion.EstadoEnvio.FALLIDA);
        }

        Notificacion sinCelular = crearAviso(null, "ana@correo.com");
        verificar("Un celular nulo falla sin lanzar error", !whatsapp.enviar(sinCelular));
        verificar("El detalle de la falla explica la causa en lenguaje claro (RNF-06)",
                sinCelular.getDetalle().contains("no es un celular válido"));

        // RNF-04: un aviso ya enviado no se puede enviar otra vez.
        boolean huboError = false;
        try {
            whatsapp.enviar(valida);
        } catch (IllegalStateException e) {
            huboError = true;
        }
        verificar("No permite enviar dos veces el mismo aviso", huboError);
    }

    // ---------- NotificacionCorreo (RF-19 y RNF-06) ----------
    private static void probarCorreo() {
        System.out.println();
        System.out.println("=== NotificacionCorreo ===");

        NotificacionCorreo correo = new NotificacionCorreo();
        verificar("El nombre del canal es Correo", correo.getNombreCanal().equals("Correo"));

        Notificacion valida = crearAviso("3001234567", "ana@correo.com");
        boolean resultado = correo.enviar(valida);
        verificar("Con un correo válido devuelve true", resultado);
        verificar("Con un correo válido queda ENVIADA por Correo",
                valida.getEstadoEnvio() == Notificacion.EstadoEnvio.ENVIADA
                        && valida.getCanal().equals("Correo"));
        verificar("El detalle incluye el correo y el asunto con el código de la orden",
                valida.getDetalle().contains("ana@correo.com")
                        && valida.getDetalle().contains("Actualización de tu orden OS-001"));

        // Correos inválidos: debe fallar y explicar la causa.
        String[] correosInvalidos = {"anacorreo.com", "ana@correo", "ana@correo.", "@correo.com", "ana@.com"};
        for (int i = 0; i < correosInvalidos.length; i++) {
            Notificacion aviso = crearAviso("3001234567", correosInvalidos[i]);
            boolean envio = correo.enviar(aviso);
            verificar("Rechaza el correo \"" + correosInvalidos[i] + "\" y queda FALLIDA",
                    !envio && aviso.getEstadoEnvio() == Notificacion.EstadoEnvio.FALLIDA);
        }

        Notificacion sinCorreo = crearAviso("3001234567", null);
        verificar("Un correo nulo falla sin lanzar error", !correo.enviar(sinCorreo));
        verificar("El detalle de la falla explica la causa en lenguaje claro (RNF-06)",
                sinCorreo.getDetalle().contains("no es válida"));
    }

    // ---------- Polimorfismo con Notificable (RF-19) ----------
    private static void probarPolimorfismo() {
        System.out.println();
        System.out.println("=== Polimorfismo con Notificable ===");

        // Quien envía el aviso solo conoce la interfaz, no el canal concreto.
        Notificable[] canales = {new NotificacionWhatsApp(), new NotificacionCorreo()};
        for (int i = 0; i < canales.length; i++) {
            Notificacion aviso = crearAviso("3001234567", "ana@correo.com");
            boolean envio = canales[i].enviar(aviso);
            verificar("El canal " + canales[i].getNombreCanal() + " envía por la misma interfaz",
                    envio && aviso.getCanal().equals(canales[i].getNombreCanal()));
        }

        // Cada canal revisa solo su dato: el celular malo no impide el correo y al revés.
        Notificacion paraWhatsApp = crearAviso("123", "ana@correo.com");
        Notificacion paraCorreo = crearAviso("123", "ana@correo.com");
        verificar("Con celular inválido, WhatsApp falla", !canales[0].enviar(paraWhatsApp));
        verificar("Con el mismo cliente, el correo sí se envía", canales[1].enviar(paraCorreo));
    }
}
