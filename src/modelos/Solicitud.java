package modelos;

import enums.EstadoOrden;

import java.time.LocalDateTime;

public class Solicitud {
    private static int consecutivo = 1;

    private final int idSolicitud;
    private final String tipoProblema, ubicacion, descripcion;
    private final DisponibilidadTecnico.FranjaHoraria horarioDisponible;   // se compara con el horario del técnico (RF-04)
    private final LocalDateTime fechaCreacion;
    // Usa los mismos estados de OrdenServicio: así no hay dos listas de estados que se contradigan.
    private EstadoOrden estado;
    private String motivoCancelacion = "";

    // RF-03: se valida antes de gastar un ID; la solicitud nace PENDIENTE con fecha y hora.
    public Solicitud(String tipoProblema, String ubicacion, String descripcion, DisponibilidadTecnico.FranjaHoraria horarioDisponible) {
        this.tipoProblema = Validar.texto(tipoProblema, "tipo de problema");
        this.ubicacion = Validar.texto(ubicacion, "ubicación");
        this.descripcion = Validar.texto(descripcion, "descripción");
        Validar.objeto(horarioDisponible, "horario disponible");
        this.horarioDisponible = horarioDisponible;
        this.idSolicitud = consecutivo++;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
    }

    // RNF-01 (sobrecarga de constructores): lo usa el repositorio para reconstruir la solicitud tal como se guardó.
    // Ajusta el consecutivo para que las solicitudes nuevas no repitan un ID ya guardado.
    public Solicitud(int idSolicitud, String tipoProblema, String ubicacion, String descripcion,
                     DisponibilidadTecnico.FranjaHoraria horarioDisponible, LocalDateTime fechaCreacion,
                     EstadoOrden estado, String motivoCancelacion) {
        this.tipoProblema = Validar.texto(tipoProblema, "tipo de problema");
        this.ubicacion = Validar.texto(ubicacion, "ubicación");
        this.descripcion = Validar.texto(descripcion, "descripción");
        Validar.objeto(horarioDisponible, "horario disponible");
        Validar.objeto(fechaCreacion, "fecha de creación");
        Validar.objeto(estado, "estado");
        this.horarioDisponible = horarioDisponible;
        this.idSolicitud = idSolicitud;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.motivoCancelacion = motivoCancelacion == null ? "" : motivoCancelacion;
        if (idSolicitud >= consecutivo) { consecutivo = idSolicitud + 1; }
    }

    public void crear() { System.out.println("Solicitud creada con ID: " + idSolicitud + " (estado " + estado + ")."); }

    // Sin "public": solo OrdenServicio (mismo paquete) cambia el estado, para que solicitud y orden siempre coincidan.
    void actualizarEstado(EstadoOrden nuevo) { this.estado = nuevo; }
    void registrarMotivoCancelacion(String motivo) { this.motivoCancelacion = motivo; }

    public static int getConsecutivo() { return consecutivo; }

    public int getIdSolicitud() { return idSolicitud; }
    public String getTipoProblema() { return tipoProblema; }
    public String getUbicacion() { return ubicacion; }
    public String getDescripcion() { return descripcion; }
    public DisponibilidadTecnico.FranjaHoraria getHorarioDisponible() { return horarioDisponible; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public EstadoOrden getEstado() { return estado; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
}
