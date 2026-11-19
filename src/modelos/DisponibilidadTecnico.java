package modelos;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadTecnico {
    private Tecnico tecnico;
    private List<FranjaHoraria> franjasActuales;          // horarios en los que SÍ trabaja
    private List<FranjaHoraria> bloqueosIndisponibles;    // horarios en los que NO puede

    public DisponibilidadTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
        this.franjasActuales = new ArrayList<>();
        this.bloqueosIndisponibles = new ArrayList<>();
    }

    public void registrarDisponibilidad(FranjaHoraria franja) {
        Validar.objeto(franja, "franja horaria");
        franjasActuales.add(franja);
        System.out.println("Disponibilidad registrada: " + franja);
    }

    // RF-13: rechaza bloqueos que se crucen con otro ya registrado.
    public boolean registrarIndisponibilidad(FranjaHoraria franja) {
        Validar.objeto(franja, "franja horaria");
        for (FranjaHoraria bloqueo : bloqueosIndisponibles) {
            if (bloqueo.seCruzaCon(franja)) {
                System.out.println("No se registró el bloqueo: se cruza con otro ya registrado (" + bloqueo + ").");
                return false;
            }
        }
        bloqueosIndisponibles.add(franja);
        System.out.println("Bloqueo registrado: " + franja);
        return true;
    }

    // RNF-01: al leer el archivo se vuelven a poner las franjas sin mostrar mensajes.
    public void restaurar(FranjaHoraria franja, boolean esBloqueo) {
        Validar.objeto(franja, "franja horaria");
        if (esBloqueo) { bloqueosIndisponibles.add(franja); } else { franjasActuales.add(franja); }
    }

    public boolean quitarIndisponibilidad(FranjaHoraria franja) {
        for (int i = 0; i < bloqueosIndisponibles.size(); i++) {
            FranjaHoraria b = bloqueosIndisponibles.get(i);
            if (b.getDia() == franja.getDia() && b.getHoraInicio().equals(franja.getHoraInicio())
                    && b.getHoraFin().equals(franja.getHoraFin())) {
                bloqueosIndisponibles.remove(i);
                System.out.println("Bloqueo eliminado: " + b);
                return true;
            }
        }
        System.out.println("No se encontró ese bloqueo.");
        return false;
    }

    // Disponible si no choca con un bloqueo y cabe dentro de una franja en la que trabaja.
    public boolean estaDisponible(FranjaHoraria franja) {
        for (FranjaHoraria bloqueo : bloqueosIndisponibles) {
            if (bloqueo.seCruzaCon(franja)) { return false; }
        }
        for (FranjaHoraria h : franjasActuales) {
            if (h.getDia() == franja.getDia() && !franja.getHoraInicio().isBefore(h.getHoraInicio())
                    && !franja.getHoraFin().isAfter(h.getHoraFin())) {
                return true;
            }
        }
        return false;
    }

    public Tecnico getTecnico() { return tecnico; }
    public List<FranjaHoraria> getFranjasActuales() { return new ArrayList<>(franjasActuales); }
    public List<FranjaHoraria> getBloqueosIndisponibles() { return new ArrayList<>(bloqueosIndisponibles); }

    public static class FranjaHoraria {
        private final DayOfWeek dia;
        private final LocalTime horaInicio;
        private final LocalTime horaFin;

        public FranjaHoraria(DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin) {
            Validar.objeto(dia, "día");
            Validar.objeto(horaInicio, "hora de inicio");
            Validar.objeto(horaFin, "hora de fin");
            if (!horaInicio.isBefore(horaFin)) { throw new IllegalArgumentException("La hora de inicio debe ser antes de la hora de fin."); }
            this.dia = dia;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
        }

        public boolean seCruzaCon(FranjaHoraria otra) {
            return dia == otra.dia && horaInicio.isBefore(otra.horaFin) && otra.horaInicio.isBefore(horaFin);
        }

        public DayOfWeek getDia() { return dia; }
        public LocalTime getHoraInicio() { return horaInicio; }
        public LocalTime getHoraFin() { return horaFin; }

        @Override
        public String toString() { return dia + " " + horaInicio + " - " + horaFin; }
    }
}
