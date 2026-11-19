package ui;

import enums.EstadoOrden;
import modelos.Administrador;
import modelos.Cliente;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Servicio;
import modelos.Tecnico;
import servicios.AdministradorServicio;
import servicios.ClienteServicio;
import servicios.TecnicoServicio;

import java.time.LocalDate;
import java.util.List;

public class MenuAdministrador {
    private final Administrador admin;
    private final AdministradorServicio servicio;
    private final ClienteServicio clienteServicio;
    private final TecnicoServicio tecnicoServicio;
    private final Consola consola;

    public MenuAdministrador(Administrador admin, AdministradorServicio servicio, ClienteServicio clienteServicio,
                             TecnicoServicio tecnicoServicio, Consola consola) {
        this.admin = admin;
        this.servicio = servicio;
        this.clienteServicio = clienteServicio;
        this.tecnicoServicio = tecnicoServicio;
        this.consola = consola;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("ADMINISTRADOR: " + admin.getNombre());
            System.out.println("1. Registrar técnico (RF-01)");
            System.out.println("2. Ver técnicos");
            System.out.println("3. Ver clientes");
            System.out.println("4. Agregar tipo de servicio (RF-05, RF-07)");
            System.out.println("5. Ver servicios");
            System.out.println("6. Ver todas las órdenes");
            System.out.println("7. Asignar técnico a una orden pendiente (RF-04, RF-05)");
            System.out.println("8. Reasignar una orden (RF-14)");
            System.out.println("9. Cancelar una orden (RF-06)");
            System.out.println("10. Filtrar órdenes (RF-20)");
            System.out.println("11. Ver, confirmar o rechazar pagos (RF-17)");
            System.out.println("12. Ver trazabilidad de una orden (RNF-04)");
            System.out.println("0. Cerrar sesión");
            opcion = consola.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1: Consola.intentar(this::registrarTecnico); break;
                case 2: tecnicoServicio.mostrarTodos(); break;
                case 3: clienteServicio.mostrarTodos(); break;
                case 4: Consola.intentar(this::registrarServicio); break;
                case 5: servicio.mostrarServicios(); break;
                case 6: verOrdenes(servicio.getOrdenes()); break;
                case 7: Consola.intentar(this::asignarTecnico); break;
                case 8: Consola.intentar(this::reasignar); break;
                case 9: Consola.intentar(() -> servicio.cancelarOrden(admin, consola.leerTexto("Código de la orden: "), consola.leerTexto("Motivo: "))); break;
                case 10: Consola.intentar(this::filtrar); break;
                case 11: Consola.intentar(this::gestionarPagos); break;
                case 12: Consola.intentar(() -> servicio.mostrarTrazabilidad(consola.leerTexto("Código de la orden: "))); break;
                case 0: admin.cerrarSesion(); break;
                default: System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void registrarTecnico() {
        String nombre = consola.leerTexto("Nombre: ");
        String documento = consola.leerTexto("Documento: ");
        String correo = consola.leerTexto("Correo: ");
        String telefono = consola.leerTexto("Teléfono: ");
        String clave = consola.leerTexto("Contraseña inicial: ");
        String especialidad = consola.leerTexto("Especialidad (debe coincidir con un servicio, ej. Plomería): ");
        String zona = consola.leerTexto("Zona de cobertura (ej. Chía): ");
        double tarifa = consola.leerDouble("Tarifa base por hora: ");
        servicio.registrarTecnico(admin, new Tecnico(nombre, documento, correo, telefono, clave, especialidad, zona, tarifa));
        System.out.println("Recuerde que el técnico debe registrar su horario para recibir órdenes (RF-13).");
    }

    private void registrarServicio() {
        String tipo = consola.leerTexto("Tipo de servicio (ej. Cerrajería): ");
        double factor = consola.leerDouble("Factor de tarifa (1.0 = normal, 1.5 = 50% más): ");
        double horas = consola.leerDouble("Duración estimada en horas: ");
        int dias = consola.leerEntero("Días de garantía: ");
        servicio.registrarServicio(new Servicio(tipo, factor, horas, dias));
    }

    private void asignarTecnico() {
        String codigo = consola.leerTexto("Código de la orden: ");
        OrdenServicio orden = servicio.buscarOrden(codigo);
        orden.imprimir();
        List<Tecnico> disponibles = servicio.buscarTecnicosDisponibles(codigo);
        if (disponibles.isEmpty()) {
            System.out.println("No hay técnicos de " + orden.getServicio().getTipo() + " en " + orden.getSolicitud().getUbicacion()
                    + " libres en ese horario.");
            return;
        }
        System.out.println("Técnicos disponibles (RF-04):");
        for (Tecnico t : disponibles) { System.out.println("  " + t.aLinea()); }
        String documento = consola.leerTexto("Documento del técnico a asignar: ");
        double materiales = consola.leerDouble("Costo estimado de materiales: ");
        servicio.asignarTecnico(admin, codigo, documento, materiales);
    }

    private void reasignar() {
        String codigo = consola.leerTexto("Código de la orden: ");
        System.out.println("Técnicos disponibles (RF-04):");
        for (Tecnico t : servicio.buscarTecnicosDisponibles(codigo)) { System.out.println("  " + t.aLinea()); }
        servicio.reasignarOrden(admin, codigo, consola.leerTexto("Documento del nuevo técnico: "));
    }

    // RF-20: Enter vacío = no filtrar por ese campo.
    private void filtrar() {
        System.out.println("Deje vacío el filtro que no quiera usar.");
        String textoEstado = consola.leerTexto("Estado (PENDIENTE, ASIGNADA, EN_PROCESO, FINALIZADA, CANCELADA): ").toUpperCase();
        EstadoOrden estado = textoEstado.isEmpty() ? null : EstadoOrden.valueOf(textoEstado);
        LocalDate fecha = consola.leerFechaOpcional("Fecha de creación (AAAA-MM-DD): ");
        String docCliente = consola.leerTexto("Documento del cliente: ");
        Cliente cliente = null;
        if (!docCliente.isEmpty()) {
            cliente = clienteServicio.buscarPorId(docCliente);
            if (cliente == null) { System.out.println("No existe ese cliente."); return; }
        }
        String docTecnico = consola.leerTexto("Documento del técnico: ");
        Tecnico tecnico = null;
        if (!docTecnico.isEmpty()) {
            tecnico = tecnicoServicio.buscarPorId(docTecnico);
            if (tecnico == null) { System.out.println("No existe ese técnico."); return; }
        }
        verOrdenes(servicio.filtrarOrdenes(estado, fecha, cliente, tecnico));
    }

    private void gestionarPagos() {
        String codigo = consola.leerTexto("Código de la orden: ");
        List<Pago> pagos = servicio.consultarPagos(admin, codigo);
        if (pagos.isEmpty()) {
            System.out.println("La orden no tiene pagos registrados.");
            return;
        }
        for (int i = 0; i < pagos.size(); i++) { System.out.println((i + 1) + ". " + pagos.get(i).aLinea()); }
        System.out.println("Saldo pendiente: " + String.format("$%,.0f", servicio.buscarOrden(codigo).getSaldoPendiente()));
        int numero = consola.leerEntero("Número del pago a revisar (0 para volver): ");
        if (numero == 0) { return; }
        int accion = consola.leerEntero("1 = Confirmar, 2 = Rechazar: ");
        if (accion == 1) { servicio.confirmarPago(admin, codigo, numero); }
        else if (accion == 2) { servicio.rechazarPago(admin, codigo, numero); }
    }

    private void verOrdenes(List<OrdenServicio> ordenes) {
        if (ordenes.isEmpty()) { System.out.println("No hay órdenes que mostrar."); }
        for (OrdenServicio o : ordenes) { System.out.println("- " + o.aLinea()); }
        System.out.println("Total: " + ordenes.size() + " orden(es)");
    }
}
