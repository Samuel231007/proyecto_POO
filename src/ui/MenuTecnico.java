package ui;

import modelos.DetalleTrabajo;
import modelos.OrdenServicio;
import modelos.Tecnico;
import servicios.TecnicoServicio;

import java.util.List;

public class MenuTecnico {
    private final Tecnico tecnico;
    private final TecnicoServicio servicio;
    private final Consola consola;

    public MenuTecnico(Tecnico tecnico, TecnicoServicio servicio, Consola consola) {
        this.tecnico = tecnico;
        this.servicio = servicio;
        this.consola = consola;
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("TÉCNICO: " + tecnico.getNombre());
            System.out.println("1. Ver mis datos y horario");
            System.out.println("2. Registrar horario disponible (RF-13)");
            System.out.println("3. Registrar bloqueo de indisponibilidad (RF-13)");
            System.out.println("4. Quitar un bloqueo");
            System.out.println("5. Ver mis órdenes activas (RF-09)");
            System.out.println("6. Iniciar trabajo de una orden (RF-06)");
            System.out.println("7. Registrar trabajo realizado y materiales (RF-15)");
            System.out.println("8. Finalizar orden y ver costo final (RF-16)");
            System.out.println("9. Cambiar contraseña");
            System.out.println("0. Cerrar sesión");
            opcion = consola.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1: tecnico.imprimir(); break;
                case 2: Consola.intentar(() -> servicio.registrarDisponibilidad(tecnico, consola.leerFranja())); break;
                case 3: Consola.intentar(() -> servicio.registrarIndisponibilidad(tecnico, consola.leerFranja())); break;
                case 4: Consola.intentar(() -> servicio.quitarIndisponibilidad(tecnico, consola.leerFranja())); break;
                case 5: verOrdenesActivas(); break;
                case 6: Consola.intentar(() -> servicio.iniciarTrabajo(tecnico, consola.leerTexto("Código de la orden: "))); break;
                case 7: Consola.intentar(this::registrarTrabajo); break;
                case 8: Consola.intentar(() -> System.out.println(servicio.finalizarOrden(tecnico, consola.leerTexto("Código de la orden: ")))); break;
                case 9: Consola.intentar(() -> servicio.cambiarContrasenia(tecnico, consola.leerTexto("Contraseña actual: "), consola.leerTexto("Contraseña nueva: "))); break;
                case 0: tecnico.cerrarSesion(); break;
                default: System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private void verOrdenesActivas() {
        List<OrdenServicio> ordenes = servicio.consultarOrdenesActivas(tecnico);
        if (ordenes.isEmpty()) { System.out.println("No tiene órdenes activas."); }
        for (OrdenServicio o : ordenes) { o.imprimir(); }
    }

    // RF-15: se arma el detalle completo y luego se registra de una vez (RNF-03).
    private void registrarTrabajo() {
        String codigo = consola.leerTexto("Código de la orden: ");
        servicio.buscarOrdenAsignada(tecnico, codigo);   // revisa antes de pedir todos los datos
        DetalleTrabajo detalle = new DetalleTrabajo();
        detalle.setDescripcion(consola.leerTexto("Descripción del trabajo realizado: "));
        detalle.setHorasTrabajo(consola.leerDouble("Horas reales trabajadas: "));
        System.out.println("Actividades realizadas (Enter vacío para terminar):");
        String actividad;
        while (!(actividad = consola.leerTexto("  - ")).isEmpty()) { detalle.agregarActividad(actividad); }
        System.out.println("Materiales usados (Enter vacío en la descripción para terminar):");
        String material;
        while (!(material = consola.leerTexto("  Material: ")).isEmpty()) {
            double costo = consola.leerDouble("  Costo unitario: ");
            double cantidad = consola.leerDouble("  Cantidad: ");
            detalle.registrarMaterial(new DetalleTrabajo.Material(material, costo, cantidad));
        }
        servicio.registrarTrabajo(tecnico, codigo, detalle);
    }
}
