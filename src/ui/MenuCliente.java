package ui;

import modelos.Cliente;
import modelos.DisponibilidadTecnico.FranjaHoraria;
import modelos.Efectivo;
import modelos.NotificacionCorreo;
import modelos.NotificacionWhatsApp;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Tarjeta;
import modelos.Transferencia;
import servicios.ClienteServicio;

import java.time.LocalDate;
import java.util.List;

public class MenuCliente {
    private final Cliente cliente;
    private final ClienteServicio servicio;
    private final Consola consola;

    public MenuCliente(Cliente cliente, ClienteServicio servicio, Consola consola) {
        this.cliente = cliente;
        this.servicio = servicio;
        this.consola = consola;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("CLIENTE: " + cliente.getNombre());
            System.out.println("1. Ver mis datos");
            System.out.println("2. Actualizar mis datos (RF-11)");
            System.out.println("3. Elegir canal de avisos: WhatsApp o correo (RF-19)");
            System.out.println("4. Solicitar un servicio (RF-03)");
            System.out.println("5. Ver mis órdenes (RF-08)");
            System.out.println("6. Ver detalle de una orden");
            System.out.println("7. Cancelar una solicitud pendiente (RF-12)");
            System.out.println("8. Calificar un servicio finalizado (RF-10)");
            System.out.println("9. Pedir revisión por garantía (RF-18)");
            System.out.println("10. Registrar un pago (RF-17)");
            System.out.println("11. Ver los pagos de una orden");
            System.out.println("12. Cambiar contraseña");
            System.out.println("0. Cerrar sesión");
            opcion = consola.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1: cliente.imprimir(); break;
                case 2: Consola.intentar(this::actualizarDatos); break;
                case 3: Consola.intentar(this::elegirCanal); break;
                case 4: Consola.intentar(this::solicitarServicio); break;
                case 5: verOrdenes(); break;
                case 6: Consola.intentar(this::verDetalle); break;
                case 7: Consola.intentar(this::cancelar); break;
                case 8: Consola.intentar(this::calificar); break;
                case 9: Consola.intentar(this::revisionGarantia); break;
                case 10: Consola.intentar(this::registrarPago); break;
                case 11: Consola.intentar(this::verPagos); break;
                case 12: Consola.intentar(this::cambiarContrasenia); break;
                case 0: cliente.cerrarSesion(); break;
                default: System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void actualizarDatos() {
        System.out.println("Deje vacío un campo para conservar el valor actual.");
        String nombre = valorOActual(consola.leerTexto("Nombre [" + cliente.getNombre() + "]: "), cliente.getNombre());
        String telefono = valorOActual(consola.leerTexto("Teléfono [" + cliente.getTelefono() + "]: "), cliente.getTelefono());
        String correo = valorOActual(consola.leerTexto("Correo [" + cliente.getCorreo() + "]: "), cliente.getCorreo());
        String ubicacion = valorOActual(consola.leerTexto("Ubicación [" + cliente.getUbicacion() + "]: "), cliente.getUbicacion());
        servicio.actualizarDatos(cliente, nombre, telefono, correo, ubicacion);
    }

    private void elegirCanal() {
        int canal = consola.leerEntero("1 = WhatsApp, 2 = Correo: ");
        if (canal == 1) { servicio.cambiarCanalNotificacion(cliente, new NotificacionWhatsApp()); }
        else if (canal == 2) { servicio.cambiarCanalNotificacion(cliente, new NotificacionCorreo()); }
        else { System.out.println("Opción no válida."); }
    }

    private void solicitarServicio() {
        System.out.println("Servicios disponibles: " + nombresServicios());
        String tipo = consola.leerTexto("Tipo de servicio: ");
        String ubicacion = valorOActual(consola.leerTexto("Ubicación [" + cliente.getUbicacion() + "]: "), cliente.getUbicacion());
        String descripcion = consola.leerTexto("Describa el problema: ");
        System.out.println("¿Cuándo puede recibir al técnico?");
        FranjaHoraria horario = consola.leerFranja();
        OrdenServicio orden = servicio.solicitarServicio(cliente, tipo, ubicacion, descripcion, horario);
        System.out.println("Su orden es la " + orden.getCodigo() + ". Le avisaremos cuando se asigne un técnico.");
    }

    private void verOrdenes() {
        List<OrdenServicio> ordenes = servicio.consultarHistorial(cliente);
        if (ordenes.isEmpty()) { System.out.println("Aún no tiene órdenes."); }
        for (OrdenServicio o : ordenes) { System.out.println("- " + o.aLinea()); }
    }

    private void verDetalle() {
        servicio.buscarOrdenPropia(cliente, consola.leerTexto("Código de la orden (ej. OS-001): ")).imprimir();
    }

    private void cancelar() {
        String codigo = consola.leerTexto("Código de la orden: ");
        String motivo = consola.leerTexto("Motivo de la cancelación: ");
        servicio.cancelarSolicitud(cliente, codigo, motivo);
    }

    private void calificar() {
        String codigo = consola.leerTexto("Código de la orden: ");
        int estrellas = consola.leerEntero("Estrellas (1 a 5): ");
        String comentario = consola.leerTexto("Comentario (opcional): ");
        servicio.calificarServicio(cliente, codigo, estrellas, comentario);
    }

    private void revisionGarantia() {
        String codigo = consola.leerTexto("Código de la orden: ");
        String problema = consola.leerTexto("¿Qué problema se presentó?: ");
        servicio.solicitarRevisionGarantia(cliente, codigo, LocalDate.now(), problema);
    }

    private void registrarPago() {
        String codigo = consola.leerTexto("Código de la orden: ");
        OrdenServicio orden = servicio.buscarOrdenPropia(cliente, codigo);
        System.out.println("Saldo pendiente: " + String.format("$%,.0f", orden.getSaldoPendiente()));
        double monto = consola.leerDouble("Monto a pagar: ");
        int metodo = consola.leerEntero("Método: 1 = Efectivo, 2 = Tarjeta, 3 = Transferencia: ");
        Pago pago;
        switch (metodo) {
            case 1: pago = new Efectivo(monto, LocalDate.now()); break;
            case 2:
                // RNF-05: no se pide el número de la tarjeta, solo la referencia que da el datáfono.
                pago = new Tarjeta(monto, LocalDate.now(), consola.leerTexto("Referencia de autorización del datáfono: "));
                break;
            case 3: pago = new Transferencia(monto, LocalDate.now(), consola.leerTexto("Referencia bancaria: ")); break;
            default: System.out.println("Método no válido."); return;
        }
        servicio.registrarPago(cliente, codigo, pago);
        System.out.println("El pago queda PENDIENTE hasta que el administrador lo confirme.");
    }

    private void verPagos() {
        List<Pago> pagos = servicio.consultarPagos(cliente, consola.leerTexto("Código de la orden: "));
        if (pagos.isEmpty()) { System.out.println("La orden no tiene pagos registrados."); }
        for (int i = 0; i < pagos.size(); i++) { System.out.println((i + 1) + ". " + pagos.get(i).aLinea()); }
    }

    private void cambiarContrasenia() {
        servicio.cambiarContrasenia(cliente, consola.leerTexto("Contraseña actual: "), consola.leerTexto("Contraseña nueva: "));
    }

    private String nombresServicios() {
        StringBuilder sb = new StringBuilder();
        for (modelos.Servicio s : servicio.getServiciosDisponibles()) {
            if (sb.length() > 0) { sb.append(", "); }
            sb.append(s.getTipo());
        }
        return sb.toString();
    }

    private static String valorOActual(String valor, String actual) {
        return valor.isEmpty() ? actual : valor;
    }
}
