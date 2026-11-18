package modelos;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Garantía de un servicio terminado: vigencia y revisiones. RF-07 y RF-18. */
public class Garantia {
    private final LocalDate fechaInicio;
    private final int diasVigencia;
    private final String cobertura;
    private final List<String> revisiones = new ArrayList<>();

    public Garantia(LocalDate fechaInicio, int diasVigencia, String cobertura) {
        Validar.objeto(fechaInicio, "fecha de inicio");
        if (diasVigencia < 0) { throw new IllegalArgumentException("Los días de vigencia no pueden ser negativos."); }
        this.fechaInicio = fechaInicio;
        this.diasVigencia = diasVigencia;
        this.cobertura = Validar.texto(cobertura, "cobertura");
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public int getDiasVigencia() { return diasVigencia; }
    public LocalDate getFechaFin() { return fechaInicio.plusDays(diasVigencia); }

    // Vigente desde la fecha de inicio hasta la fecha de fin (incluida).
    public boolean estaVigente(LocalDate fecha) {
        Validar.objeto(fecha, "fecha");
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(getFechaFin());
    }

    // RF-18: solo se registra la revisión si la garantía está vigente.
    public void solicitarRevision(LocalDate fecha, String problema) {
        Validar.texto(problema, "problema");
        if (!estaVigente(fecha)) { throw new IllegalStateException("La garantía no está vigente el " + fecha + " (venció el " + getFechaFin() + ")."); }
        revisiones.add(fecha + " - " + problema.trim());
    }

    public List<String> getRevisiones() { return new ArrayList<>(revisiones); }   // copia
    public String getCobertura() { return cobertura; }
}
