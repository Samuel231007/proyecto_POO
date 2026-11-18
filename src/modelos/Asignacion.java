package modelos;

import enums.EstadoOrden;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Asigna (y reasigna) un técnico a una orden. Cada orden tiene su propia Asignacion.
 * RF-04: solo acepta técnicos de la especialidad y zona de la solicitud.
 * RF-13: no asigna técnicos con un bloqueo en el horario de la solicitud.
 * RF-14 y RNF-04: conserva los técnicos anteriores.
 */
public class Asignacion {

    // Clase anidada estática: un movimiento (asignación o reasignación) con su fecha y responsable (RNF-04).
    // Se guarda como objeto y no como texto para poder escribirlo y leerlo del archivo asignaciones.txt (RNF-01).
    public static class Movimiento {
        private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        private final LocalDateTime fecha;
        private final Tecnico tecnicoAnterior;   // null si fue la primera asignación
        private final Tecnico tecnicoNuevo;
        private final String responsable;

        private Movimiento(LocalDateTime fecha, Tecnico tecnicoAnterior, Tecnico tecnicoNuevo, String responsable) {
            this.fecha = fecha;
            this.tecnicoAnterior = tecnicoAnterior;
            this.tecnicoNuevo = tecnicoNuevo;
            this.responsable = responsable;
        }

        public LocalDateTime getFecha() { return fecha; }
        public Tecnico getTecnicoAnterior() { return tecnicoAnterior; }
        public Tecnico getTecnicoNuevo() { return tecnicoNuevo; }
        public String getResponsable() { return responsable; }

        @Override
        public String toString() {
            String cambio = tecnicoAnterior == null ? "Asignado a " + tecnicoNuevo.getNombre()
                    : "Reasignada de " + tecnicoAnterior.getNombre() + " a " + tecnicoNuevo.getNombre();
            return fecha.format(FORMATO) + " | " + cambio + " | por " + responsable;
        }
    }

    private Tecnico tecnico;                             // técnico actual (null al inicio)
    private final OrdenServicio orden;
    private final List<Tecnico> historialAsignaciones;   // técnicos anteriores
    private final List<Movimiento> movimientos = new ArrayList<>();   // RNF-04: fecha, técnicos y responsable de cada cambio

    public Asignacion(OrdenServicio orden) {
        Validar.objeto(orden, "orden");
        this.orden = orden;
        this.historialAsignaciones = new ArrayList<>();
    }

    public boolean asignarTecnico(Tecnico nuevo, String responsable) {
        if (!sePuedeUsar(nuevo)) { return false; }
        if (estaAsignada()) {
            System.out.println("La orden ya tiene un técnico asignado. Use reasignarTecnico.");
            return false;
        }
        orden.asignarTecnico(nuevo, responsable);   // primero la orden (puede rechazar): RNF-03
        movimientos.add(new Movimiento(LocalDateTime.now(), null, nuevo, responsable.trim()));
        tecnico = nuevo;
        System.out.println("Técnico " + nuevo.getNombre() + " asignado a la orden " + orden.getCodigo() + ".");
        return true;
    }

    // RF-14: solo antes de iniciar el trabajo (la orden rechaza la reasignación si está EN_PROCESO).
    public boolean reasignarTecnico(Tecnico nuevo, String responsable) {
        if (!sePuedeUsar(nuevo)) { return false; }
        if (!estaAsignada()) {
            System.out.println("La orden no tiene técnico asignado. Use asignarTecnico.");
            return false;
        }
        if (tecnico == nuevo) {
            System.out.println("Ese técnico ya está asignado a la orden.");
            return false;
        }
        if (orden.getEstado() != EstadoOrden.ASIGNADA) {
            System.out.println("Solo se puede reasignar antes de iniciar el trabajo (la orden está " + orden.getEstado() + ").");
            return false;
        }
        orden.asignarTecnico(nuevo, responsable);
        historialAsignaciones.add(tecnico);   // se guarda el anterior solo si la orden aceptó
        movimientos.add(new Movimiento(LocalDateTime.now(), tecnico, nuevo, responsable.trim()));
        System.out.println("Orden " + orden.getCodigo() + " reasignada a " + nuevo.getNombre() + " por " + responsable + ".");
        tecnico = nuevo;
        return true;
    }

    // Revisa que el técnico exista, sea de la especialidad y zona de la solicitud y esté libre en ese horario.
    private boolean sePuedeUsar(Tecnico t) {
        if (t == null) {
            System.out.println("Debe indicar un técnico válido.");
            return false;
        }
        Solicitud s = orden.getSolicitud();
        if (!t.puedeAtender(s.getTipoProblema(), s.getUbicacion())) {
            System.out.println("El técnico " + t.getNombre() + " no es de esa especialidad o zona.");
            return false;
        }
        if (!t.estaDisponible(s.getHorarioDisponible())) {
            System.out.println("El técnico " + t.getNombre() + " no está disponible en el horario " + s.getHorarioDisponible() + ".");
            return false;
        }
        return true;
    }

    // RNF-01: lo usa AsignacionRepository al leer el archivo. Repite el movimiento sin volver a validar ni avisar.
    public void restaurarMovimiento(LocalDateTime fecha, Tecnico anterior, Tecnico nuevo, String responsable) {
        Validar.objeto(fecha, "fecha");
        Validar.objeto(nuevo, "técnico");
        movimientos.add(new Movimiento(fecha, anterior, nuevo, Validar.texto(responsable, "responsable")));
        if (anterior != null) { historialAsignaciones.add(anterior); }
        tecnico = nuevo;
        nuevo.restaurarSolicitud(orden.getSolicitud());
    }

    public boolean estaAsignada() { return tecnico != null; }
    public Tecnico getTecnico() { return tecnico; }
    public List<Tecnico> getHistorialAsignaciones() { return new ArrayList<>(historialAsignaciones); }
    public List<Movimiento> getMovimientos() { return new ArrayList<>(movimientos); }

    // Igual que antes: el registro en texto, uno por cambio.
    public List<String> getRegistroCambios() {
        List<String> registro = new ArrayList<>();
        for (Movimiento m : movimientos) { registro.add(m.toString()); }
        return registro;
    }
}
