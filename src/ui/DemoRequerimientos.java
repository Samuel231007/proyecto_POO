package ui;

import enums.EstadoOrden;
import modelos.Administrador;
import modelos.Cliente;
import modelos.DetalleTrabajo;
import modelos.DisponibilidadTecnico;
import modelos.NotificacionCorreo;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Servicio;
import modelos.Tarjeta;
import modelos.Tecnico;
import modelos.Transferencia;
import servicios.AdministradorServicio;
import servicios.ClienteServicio;
import servicios.GestorPlataforma;
import servicios.TecnicoServicio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

// Recorre los requerimientos en MODO MEMORIA: no modifica los archivos .txt.
public class DemoRequerimientos {
    public static void ejecutarDemo() {
        GestorPlataforma gestor = new GestorPlataforma(false);
        AdministradorServicio adminServicio = new AdministradorServicio(gestor);
        ClienteServicio clienteServicio = new ClienteServicio(gestor);
        TecnicoServicio tecnicoServicio = new TecnicoServicio(gestor);

        titulo("DEMO EN MEMORIA: no se modifica ningún archivo .txt");

        titulo("RF-01 y RF-02: registro de usuarios");
        Administrador admin = new Administrador("Samuel Rusinque", "1001", "3101112233", "admin123", "samuel@plataforma.co", "Operaciones");
        adminServicio.registrarAdministrador(admin);
        Servicio plomeria = new Servicio("Plomería", 1.2, 2, 30);
        adminServicio.registrarServicio(plomeria);
        Tecnico juanDavid = new Tecnico("Juan David Lara", "2001", "juandavid@correo.co", "3201234567", "clave1", "Plomería", "Chía", 50000);
        Tecnico pedro = new Tecnico("Pedro Ruiz", "2002", "pedro@correo.co", "3207654321", "clave2", "Plomería", "Chía", 60000);
        adminServicio.registrarTecnico(admin, juanDavid);
        adminServicio.registrarTecnico(admin, pedro);
        intentar("Registrar un técnico con documento repetido",
                () -> adminServicio.registrarTecnico(admin, new Tecnico("Otro", "2001", "o@c.co", "3000000000", "x", "Plomería", "Chía", 1)));
        intentar("Registrar un cliente sin ubicación",
                () -> new Cliente(" ", "Ana", "3001", "3001112233", "ana@correo.co", "x"));
        intentar("Registrar un cliente con '|' en el nombre (dañaría el archivo)",
                () -> new Cliente("Chía", "Ana|Pérez", "3009", "3001112233", "ana@correo.co", "x"));
        Cliente valeria = new Cliente("Chía", "Valeria Morales Quintero", "3001", "3009998877", "valeria.morales@correo.co", "miClave");
        clienteServicio.registrarCliente(valeria);

        titulo("RNF-02 y RNF-05: inicio de sesión con contraseña cifrada");
        clienteServicio.iniciarSesion("3001", "otraClave");
        clienteServicio.iniciarSesion("3001", "miClave");

        titulo("RF-11: actualizar datos de contacto y ubicación");
        clienteServicio.actualizarDatos(valeria, "Valeria Morales Quintero", "3009998877", "vmorales@correo.co", "Cajicá");
        clienteServicio.actualizarDatos(valeria, "Valeria Morales Quintero", "3009998877", "valeria.morales@correo.co", "Chía");
        System.out.println(valeria.consultarDatos());

        titulo("RF-13: horarios y bloqueos de los técnicos");
        tecnicoServicio.registrarDisponibilidad(juanDavid, franja(DayOfWeek.MONDAY, 7, 17));
        tecnicoServicio.registrarDisponibilidad(pedro, franja(DayOfWeek.MONDAY, 7, 17));
        tecnicoServicio.registrarIndisponibilidad(juanDavid, franja(DayOfWeek.MONDAY, 13, 15));
        tecnicoServicio.registrarIndisponibilidad(juanDavid, franja(DayOfWeek.MONDAY, 14, 16));   // se cruza: se rechaza

        titulo("RF-03: el cliente solicita un servicio");
        OrdenServicio orden = clienteServicio.solicitarServicio(valeria, "Plomería", "Chía", "Fuga en el lavamanos", franja(DayOfWeek.MONDAY, 8, 10));
        String codigo = orden.getCodigo();
        intentar("Solicitar un servicio que no existe", () -> clienteServicio.solicitarServicio(valeria, "Jardinería", "Chía", "Podar", franja(DayOfWeek.MONDAY, 8, 10)));

        titulo("RF-04: técnicos disponibles para la solicitud");
        for (Tecnico t : adminServicio.buscarTecnicosDisponibles(codigo)) { System.out.println("- " + t.getNombre()); }
        intentar("Finalizar una orden sin técnico (RF-06)", () -> orden.finalizar(admin.getNombre()));

        titulo("RF-05, RF-06 y RF-19: asignación, costo estimado y aviso automático al cliente");
        adminServicio.asignarTecnico(admin, codigo, "2001", 20000);

        titulo("RF-12: cancelar cuando ya tiene técnico (se rechaza)");
        clienteServicio.cancelarSolicitud(valeria, codigo, "Ya lo arreglé");

        titulo("RF-14: el administrador reasigna la orden");
        adminServicio.reasignarOrden(admin, codigo, "2002");
        System.out.println("Técnicos anteriores: " + orden.getAsignacion().getHistorialAsignaciones().size()
                + " | Nuevo estimado con la tarifa de Pedro: $" + orden.getCostoEstimado());

        titulo("RF-09: órdenes activas de cada técnico");
        System.out.println("Pedro: " + tecnicoServicio.consultarOrdenesActivas(pedro).size() + " | Juan David: " + tecnicoServicio.consultarOrdenesActivas(juanDavid).size());

        titulo("RF-15 y RF-16: trabajo realizado, costo final y comparación");
        tecnicoServicio.iniciarTrabajo(pedro, codigo);
        DetalleTrabajo detalle = new DetalleTrabajo();
        detalle.setDescripcion("Cambio de sifón y empaques");
        detalle.setHorasTrabajo(3);
        detalle.agregarActividad("Desmontar sifón");
        detalle.agregarActividad("Instalar sifón nuevo");
        detalle.registrarMaterial(new DetalleTrabajo.Material("Sifón PVC", 15000, 1));
        detalle.registrarMaterial(new DetalleTrabajo.Material("Empaque", 2500, 4));
        intentar("Que Juan David registre el trabajo de una orden que no es suya (RNF-02)", () -> tecnicoServicio.registrarTrabajo(juanDavid, codigo, detalle));
        tecnicoServicio.registrarTrabajo(pedro, codigo, detalle);
        System.out.println(tecnicoServicio.finalizarOrden(pedro, codigo));

        titulo("RF-07 y RF-18: garantía y revisión");
        System.out.println("Garantía vigente hasta: " + orden.getGarantia().getFechaFin());
        clienteServicio.solicitarRevisionGarantia(valeria, codigo, LocalDate.now().plusDays(10), "Volvió a gotear");
        intentar("Revisión con la garantía vencida", () -> clienteServicio.solicitarRevisionGarantia(valeria, codigo, LocalDate.now().plusDays(60), "Gotea"));

        titulo("RF-10: calificación");
        clienteServicio.calificarServicio(valeria, codigo, 5, "Muy puntual");
        intentar("Calificar dos veces", () -> clienteServicio.calificarServicio(valeria, codigo, 3, ""));

        titulo("RF-17: pagos");
        clienteServicio.registrarPago(valeria, codigo, new Transferencia(50000, LocalDate.now(), "TRX-7781"));
        adminServicio.confirmarPago(admin, codigo, 1);
        Pago resto = new Tarjeta(orden.getSaldoPendiente(), LocalDate.now(), "AUT-55120");
        clienteServicio.registrarPago(valeria, codigo, resto);
        adminServicio.rechazarPago(admin, codigo, 2);
        System.out.println("Saldo pendiente: $" + orden.getSaldoPendiente());
        intentar("Que un técnico vea los pagos (RNF-05)", () -> orden.getPagos(pedro));
        for (Pago p : adminServicio.consultarPagos(admin, codigo)) { System.out.println("- " + p); }

        titulo("RF-12 y RF-19: otra solicitud cancelada a tiempo, aviso por correo");
        clienteServicio.cambiarCanalNotificacion(valeria, new NotificacionCorreo());
        OrdenServicio orden2 = clienteServicio.solicitarServicio(valeria, "Plomería", "Chía", "Revisar tanque", franja(DayOfWeek.MONDAY, 15, 16));
        clienteServicio.cancelarSolicitud(valeria, orden2.getCodigo(), "Ya no lo necesito");
        System.out.println("Estado de la solicitud: " + orden2.getSolicitud().getEstado() + " | Motivo: " + orden2.getSolicitud().getMotivoCancelacion());

        titulo("RF-08 y RF-20: historial del cliente y filtros");
        System.out.println("Historial de Valeria: " + clienteServicio.consultarHistorial(valeria).size() + " orden(es)");
        for (OrdenServicio o : adminServicio.filtrarOrdenes(EstadoOrden.FINALIZADA, LocalDate.now(), valeria, pedro)) {
            System.out.println("- " + o);
        }

        titulo("RNF-04: trazabilidad de la orden " + codigo);
        adminServicio.mostrarTrazabilidad(codigo);

        titulo("Fin de la demo: estos datos estaban solo en memoria y se descartan");
    }

    private static DisponibilidadTecnico.FranjaHoraria franja(DayOfWeek dia, int desde, int hasta) {
        return new DisponibilidadTecnico.FranjaHoraria(dia, LocalTime.of(desde, 0), LocalTime.of(hasta, 0));
    }

    private static void titulo(String texto) { System.out.println("\n=== " + texto + " ==="); }

    // Ejecuta una acción que debe fallar y muestra el mensaje de error (RNF-06).
    private static void intentar(String descripcion, Runnable accion) {
        try {
            accion.run();
            System.out.println("[!] " + descripcion + ": se permitió.");
        } catch (RuntimeException e) {
            System.out.println("[Rechazado] " + descripcion + ": " + e.getMessage());
        }
    }
}
