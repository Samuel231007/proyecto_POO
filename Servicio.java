/**
 * Representa un tipo de servicio técnico del hogar (plomería, electricidad, pintura...).
 * Cada tipo define su propia tarifa, su duración estimada y el tiempo de garantía.
 * Apoya RF-05, RF-07 y RF-16.
 */
public class Servicio {

    private String tipo;                  // Ej.: "Plomería", "Electricidad".
    private double tarifaPorHora;         // Cada tipo de servicio aplica su propia tarifa (RF-05).
    private double duracionEstimadaHoras; // Duración que se espera para el trabajo.
    private int garantiaMeses;            // Cada tipo define su tiempo de garantía (RF-07). 0 = sin garantía.

    /** Crea un servicio validando que los datos tengan sentido. */
    public Servicio(String tipo, double tarifaPorHora, double duracionEstimadaHoras, int garantiaMeses) {
        this.tipo = validarTextoObligatorio(tipo, "El tipo de servicio");

        if (tarifaPorHora <= 0) {
            throw new IllegalArgumentException("La tarifa por hora debe ser mayor a 0.");
        }

        if (duracionEstimadaHoras <= 0) {
            throw new IllegalArgumentException("La duración estimada debe ser mayor a 0.");
        }

        if (garantiaMeses < 0) {
            throw new IllegalArgumentException("Los meses de garantía no pueden ser negativos.");
        }

        this.tarifaPorHora = tarifaPorHora;
        this.duracionEstimadaHoras = duracionEstimadaHoras;
        this.garantiaMeses = garantiaMeses;
    }

    public String getTipo() {
        return tipo;
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public double getDuracionEstimadaHoras() {
        return duracionEstimadaHoras;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    /**
     * Calcula el costo con la fórmula: tarifa por hora x horas + materiales.
     * Sirve para el costo estimado (RF-05) y para el costo final (RF-16).
     */
    public double calcularCosto(double horas, double costoMateriales) {
        if (horas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser mayores a 0.");
        }

        if (costoMateriales < 0) {
            throw new IllegalArgumentException("El costo de los materiales no puede ser negativo.");
        }

        return tarifaPorHora * horas + costoMateriales;
    }

    @Override
    public String toString() {
        return tipo + " (tarifa " + tarifaPorHora + " por hora, " + duracionEstimadaHoras
                + " h estimadas, garantía " + garantiaMeses + " meses)";
    }

    // Rechaza textos nulos, vacíos o formados solo por espacios.
    private static String validarTextoObligatorio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().length() == 0) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacío.");
        }

        return valor.trim();
    }
}
