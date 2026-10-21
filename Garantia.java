import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Garantía de un servicio terminado: tiene fecha de inicio, periodo de vigencia y cobertura.
 * Mientras esté vigente, el cliente puede pedir una revisión por un problema cubierto.
 * Apoya RF-07 (registrar la garantía) y RF-18 (solicitud de revisión).
 */
public class Garantia {

    private final LocalDate fechaInicio; // No cambia después de crear la garantía.
    private final int diasVigencia;      // Periodo de vigencia en días.
    private final String cobertura;      // Qué problemas cubre.
    private final List<String> revisiones; // Revisiones solicitadas durante la vigencia.

    public Garantia(LocalDate fechaInicio, int diasVigencia, String cobertura) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }
        if (diasVigencia < 0) {
            throw new IllegalArgumentException("Los días de vigencia no pueden ser negativos.");
        }
        if (cobertura == null || cobertura.trim().isEmpty()) {
            throw new IllegalArgumentException("La cobertura no puede estar vacía.");
        }
        this.fechaInicio = fechaInicio;
        this.diasVigencia = diasVigencia;
        this.cobertura = cobertura.trim();
        this.revisiones = new ArrayList<>();
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public int getDiasVigencia() {
        return diasVigencia;
    }

    public String getCobertura() {
        return cobertura;
    }

    public LocalDate getFechaFin() {
        return fechaInicio.plusDays(diasVigencia);
    }

    // Está vigente desde la fecha de inicio hasta la fecha de fin (incluida).
    public boolean estaVigente(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(getFechaFin());
    }

    // RF-18: registra una revisión solo si la garantía está vigente.
    public void solicitarRevision(LocalDate fecha, String problema) {
        if (problema == null || problema.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe describir el problema.");
        }
        if (!estaVigente(fecha)) {
            throw new IllegalStateException("La garantía no está vigente el " + fecha + ".");
        }
        revisiones.add(fecha + " - " + problema.trim());
    }

    // Devuelve una copia de solo lectura para que nadie modifique la lista desde afuera.
    public List<String> getRevisiones() {
        return Collections.unmodifiableList(revisiones);
    }

    @Override
    public String toString() {
        return "Garantía de " + diasVigencia + " días (hasta " + getFechaFin() + "): " + cobertura;
    }
}
