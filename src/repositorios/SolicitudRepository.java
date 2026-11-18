package repositorios;

import enums.EstadoOrden;
import interfaces.IRepository;
import modelos.DisponibilidadTecnico.FranjaHoraria;
import modelos.Solicitud;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Solicitudes de servicio sobre solicitudes.txt (RF-03, RF-12, RNF-01).
 * Formato: id|tipoProblema|ubicacion|descripcion|dia|horaInicio|horaFin|fechaCreacion|estado|motivoCancelacion
 */
public class SolicitudRepository extends BaseRepository implements IRepository<Solicitud> {

    public SolicitudRepository() {
        super("solicitudes.txt", "|");
    }

    @Override
    public ArrayList<Solicitud> getDataList() {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        for (String linea : fileManage.getDataOfFile()) {
            String[] c = separar(linea);
            FranjaHoraria horario = new FranjaHoraria(DayOfWeek.valueOf(c[4]), LocalTime.parse(c[5]), LocalTime.parse(c[6]));
            // El constructor de reconstrucción también ajusta el consecutivo estático de Solicitud.
            solicitudes.add(new Solicitud(Integer.parseInt(c[0]), c[1], c[2], c[3], horario,
                    LocalDateTime.parse(c[7]), EstadoOrden.valueOf(c[8]), c[9]));
        }
        return solicitudes;
    }

    @Override
    public void insertData(Solicitud solicitud) {
        fileManage.writeFile(getDataLine(solicitud));
    }

    @Override
    public void updateData(Solicitud oldSolicitud, Solicitud newSolicitud) {
        guardarLinea(String.valueOf(oldSolicitud.getIdSolicitud()), getDataLine(newSolicitud));
    }

    @Override
    public void deleteData(Solicitud solicitud) {
        eliminarPorClave(String.valueOf(solicitud.getIdSolicitud()));
    }

    @Override
    public String getDataLine(Solicitud s) {
        FranjaHoraria h = s.getHorarioDisponible();
        return unir(s.getIdSolicitud(), s.getTipoProblema(), s.getUbicacion(), s.getDescripcion(),
                h.getDia().name(), h.getHoraInicio(), h.getHoraFin(), s.getFechaCreacion(),
                s.getEstado().name(), s.getMotivoCancelacion());
    }

    public Solicitud buscarPorId(int id) {
        for (Solicitud s : getDataList()) {
            if (s.getIdSolicitud() == id) { return s; }
        }
        return null;
    }
}
