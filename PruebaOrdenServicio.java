import java.time.LocalDate;

// Prueba sencilla de OrdenServicio, Servicio y Garantia (se ejecuta con main).
public class PruebaOrdenServicio {

    public static void main(String[] args) {
        Servicio plomeria = new Servicio("Plomería", 1.2, 2, 30);
        OrdenServicio orden = new OrdenServicio(OrdenServicio.generarCodigo(), "1012345678", plomeria);

        // RF-05: estimado = 2 h x 50000 x 1.2 + 30000 = 150000
        System.out.println("Estimado: " + orden.calcularCostoEstimado(50000, 30000));

        // RF-06: una orden sin asignar no se puede finalizar.
        try {
            orden.finalizar("Administrador");
        } catch (IllegalStateException e) {
            System.out.println("Correcto, error esperado: " + e.getMessage());
        }

        orden.asignarTecnico("79888777", 50000, "Administrador");
        orden.iniciarTrabajo("Técnico");

        // RF-16: final = 3 h x 50000 x 1.2 + 40000 = 220000
        System.out.println("Final: " + orden.calcularCostoFinal(3, 40000));
        System.out.println("Diferencia con el estimado: " + orden.compararConEstimado());

        orden.finalizar("Técnico");
        Garantia garantia = orden.getGarantia();
        System.out.println("La garantía termina el " + garantia.getFechaFin());

        // RF-18: revisión dentro y fuera de la vigencia.
        orden.solicitarRevisionGarantia(LocalDate.now().plusDays(5), "La llave vuelve a gotear");
        try {
            orden.solicitarRevisionGarantia(LocalDate.now().plusDays(60), "Otro problema");
        } catch (IllegalStateException e) {
            System.out.println("Correcto, error esperado: " + e.getMessage());
        }
        System.out.println("Revisiones: " + orden.getGarantia().getRevisiones());

        // Pagable: un pago confirmado reduce el saldo.
        Pago pago = new Efectivo(100000, LocalDate.now());
        orden.registrarPago(pago);
        System.out.println("Saldo con pago pendiente: " + orden.getSaldoPendiente());
        pago.confirmarPago();
        System.out.println("Saldo con pago confirmado: " + orden.getSaldoPendiente());

        // RF-19: aviso del último cambio, enviado por un canal (polimorfismo con Notificable).
        Notificacion aviso = orden.crearAvisoUltimoCambio("Ana", "3001234567", "ana@correo.com");
        Notificable canal = new NotificacionWhatsApp();
        canal.enviar(aviso);
        System.out.println("Órdenes creadas: " + OrdenServicio.getTotalOrdenes());

        // RNF-04: historial de cambios.
        for (OrdenServicio.CambioEstado c : orden.getCambios()) {
            System.out.println("Cambio: " + c.getEstadoAnterior() + " -> " + c.getEstadoNuevo() + " (" + c.getResponsable() + ")");
        }
    }
}
