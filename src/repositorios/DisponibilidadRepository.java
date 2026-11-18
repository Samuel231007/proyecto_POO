package repositorios;

import interfaces.IRepositorioDependiente;
import modelos.DisponibilidadTecnico;
import modelos.DisponibilidadTecnico.FranjaHoraria;
import modelos.Tecnico;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Horario y bloqueos de cada técnico sobre disponibilidades.txt (RF-13, RNF-01).
 * Formato: documentoTecnico|tipo|dia|horaInicio|horaFin
 *   tipo = DISPONIBLE (horario en que trabaja) o BLOQUEO (no puede recibir asignaciones)
 *   dia  = MONDAY, TUESDAY... (nombre del enum DayOfWeek)
 */
public class DisponibilidadRepository extends BaseRepository implements IRepositorioDependiente<Tecnico> {

    private static final String DISPONIBLE = "DISPONIBLE";
    private static final String BLOQUEO = "BLOQUEO";

    public DisponibilidadRepository() {
        super("disponibilidades.txt", "|");
    }

    @Override
    public void guardarDe(Tecnico tecnico) {
        DisponibilidadTecnico horario = tecnico.getHorarioDisponible();
        ArrayList<String> lineas = new ArrayList<>();
        for (FranjaHoraria f : horario.getFranjasActuales()) { lineas.add(linea(tecnico, DISPONIBLE, f)); }
        for (FranjaHoraria f : horario.getBloqueosIndisponibles()) { lineas.add(linea(tecnico, BLOQUEO, f)); }
        reemplazarGrupo(tecnico.getDocumento(), lineas);
    }

    @Override
    public void cargarEn(Tecnico tecnico) {
        for (String linea : lineasDe(tecnico.getDocumento())) {
            String[] c = separar(linea);
            FranjaHoraria franja = new FranjaHoraria(DayOfWeek.valueOf(c[2]), LocalTime.parse(c[3]), LocalTime.parse(c[4]));
            tecnico.getHorarioDisponible().restaurar(franja, BLOQUEO.equals(c[1]));
        }
    }

    @Override
    public void eliminarDe(String documentoTecnico) {
        eliminarPorClave(documentoTecnico);
    }

    private String linea(Tecnico tecnico, String tipo, FranjaHoraria f) {
        return unir(tecnico.getDocumento(), tipo, f.getDia().name(), f.getHoraInicio(), f.getHoraFin());
    }
}
