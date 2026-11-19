package modelos;

import interfaces.IImprimible;

import java.util.ArrayList;
import java.util.List;

public class Tecnico extends Usuario implements IImprimible {
    private String especialidad;
    private String zonaCobertura;
    private double tarifaBase;
    private DisponibilidadTecnico horarioDisponible;
    private List<Solicitud> solicitudes;

    public Tecnico(String nombre, String documento, String correo, String telefono,
                   String contrasenia, String especialidad, String zonaCobertura, double tarifaBase) {
        this(nombre, documento, correo, telefono, contrasenia, especialidad, zonaCobertura, tarifaBase, false);
    }

    // RNF-01: constructor para el repositorio (la contraseña llega ya cifrada desde el archivo).
    public Tecnico(String nombre, String documento, String correo, String telefono, String contrasenia,
                   String especialidad, String zonaCobertura, double tarifaBase, boolean contraseniaYaCifrada) {
        super(nombre, documento, correo, telefono, contrasenia, contraseniaYaCifrada);
        this.especialidad = Validar.texto(especialidad, "especialidad");
        this.zonaCobertura = Validar.texto(zonaCobertura, "zona de cobertura");
        this.tarifaBase = Validar.positivo(tarifaBase, "tarifa base");
        this.solicitudes = new ArrayList<>();
        this.horarioDisponible = new DisponibilidadTecnico(this);
    }

    public void recibirSolicitud(Solicitud solicitud) {
        if (!solicitudes.contains(solicitud)) { solicitudes.add(solicitud); }
        System.out.println("El técnico " + getNombre() + " recibió la solicitud: " + solicitud.getTipoProblema());
    }

    // RNF-01: al cargar del archivo se vuelve a asociar la solicitud sin mostrar mensajes.
    public void restaurarSolicitud(Solicitud solicitud) {
        if (solicitud != null && !solicitudes.contains(solicitud)) { solicitudes.add(solicitud); }
    }

    // RF-04: atiende si coinciden la especialidad y la zona.
    public boolean puedeAtender(String tipoServicio, String zona) {
        return especialidad.equalsIgnoreCase(tipoServicio.trim()) && zonaCobertura.equalsIgnoreCase(zona.trim());
    }

    public boolean estaDisponible(DisponibilidadTecnico.FranjaHoraria franja) {
        return horarioDisponible.estaDisponible(franja);
    }

    public void registrarDisponibilidad(DisponibilidadTecnico.FranjaHoraria franja) { horarioDisponible.registrarDisponibilidad(franja); }
    public boolean registrarIndisponibilidad(DisponibilidadTecnico.FranjaHoraria franja) { return horarioDisponible.registrarIndisponibilidad(franja); }

    // RF-15: solo el técnico asignado registra el trabajo de la orden (RNF-02).
    public void registrarTrabajo(OrdenServicio orden, DetalleTrabajo detalle) {
        Validar.objeto(orden, "orden");
        if (orden.getTecnico() != this) { throw new IllegalArgumentException("Solo el técnico asignado puede registrar el trabajo."); }
        orden.registrarDetalle(detalle);
        System.out.println("Trabajo registrado en la orden " + orden.getCodigo() + ".");
    }

    public String getEspecialidad() { return especialidad; }
    public String getZonaCobertura() { return zonaCobertura; }
    public double getTarifaBase() { return tarifaBase; }
    public List<Solicitud> getSolicitudes() { return new ArrayList<>(solicitudes); }
    public DisponibilidadTecnico getHorarioDisponible() { return horarioDisponible; }

    @Override
    public String getRol() { return "Técnico"; }

    @Override
    public void imprimir() {
        System.out.println("Nombre: " + getNombre() + "\nDocumento: " + getDocumento() + "\nTeléfono: " + getTelefono()
                + "\nCorreo: " + getCorreo() + "\nEspecialidad: " + especialidad + "\nZona: " + zonaCobertura
                + "\nTarifa base por hora: " + String.format("$%,.0f", tarifaBase));
        System.out.println("Horario: " + horarioDisponible.getFranjasActuales());
        System.out.println("Bloqueos: " + horarioDisponible.getBloqueosIndisponibles());
    }

    @Override
    public String aLinea() {
        return String.format("%-12s %-22s %-14s %-12s %10s/h", getDocumento(), getNombre(), especialidad,
                zonaCobertura, String.format("$%,.0f", tarifaBase));
    }
}
