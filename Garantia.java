import java.time.LocalDate;

/**
 * Garantía de un servicio ya realizado: desde cuándo empieza, cuántos meses dura y qué cubre.
 * La crea OrdenServicio cuando la orden se finaliza.
 * Apoya RF-07 y RF-18 (saber si un problema todavía está cubierto).
 */
public class Garantia {

    private final LocalDate fechaInicio; // Día en que se finalizó el servicio.
    private final int mesesVigencia;     // Duración de la garantía.
    private final String cobertura;      // Qué cubre la garantía.

    /** Crea la garantía validando que sus datos sean correctos. */
    public Garantia(LocalDate fechaInicio, int mesesVigencia, String cobertura) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }

        if (mesesVigencia <= 0) {
            throw new IllegalArgumentException("La vigencia debe ser de al menos 1 mes.");
        }

        if (cobertura == null || cobertura.trim().length() == 0) {
            throw new IllegalArgumentException("La cobertura no puede estar vacía.");
        }

        this.fechaInicio = fechaInicio;
        this.mesesVigencia = mesesVigencia;
        this.cobertura = cobertura.trim();
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public int getMesesVigencia() {
        return mesesVigencia;
    }

    public String getCobertura() {
        return cobertura;
    }

    /** Calcula el último día de la garantía: fecha de inicio + meses de vigencia. */
    public LocalDate getFechaFin() {
        return fechaInicio.plusMonths(mesesVigencia);
    }

    /** Indica si la garantía cubre una fecha dada (RF-18). */
    public boolean estaVigente(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha a consultar es obligatoria.");
        }

        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(getFechaFin());
    }

    @Override
    public String toString() {
        return "Garantía de " + mesesVigencia + " meses (del " + fechaInicio + " al "
                + getFechaFin() + "): " + cobertura;
    }
}
