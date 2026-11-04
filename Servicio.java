/**
 * Tipo de servicio que ofrece la plataforma (electricidad, plomería, pintura...).
 * Cada tipo define su propia tarifa, su duración estimada y su tiempo de garantía.
 * Apoya RF-05 (precio estimado), RF-07 (garantía por tipo) y RF-16 (costo final).
 */
public class Servicio {

    // Constante estática: factor de una tarifa normal, sin recargo.
    public static final double FACTOR_NORMAL = 1.0;

    private String tipo;                  // Ejemplo: "Plomería".
    private double factorTarifa;          // Multiplicador propio del tipo (1.0 = tarifa normal).
    private double duracionEstimadaHoras; // Horas que se espera que tome el trabajo.
    private int diasGarantia;             // Días de garantía de este tipo de servicio.

    public Servicio(String tipo, double factorTarifa, double duracionEstimadaHoras, int diasGarantia) {
        setTipo(tipo);
        setFactorTarifa(factorTarifa);
        setDuracionEstimadaHoras(duracionEstimadaHoras);
        setDiasGarantia(diasGarantia);
    }

    public String getTipo() {
        return tipo;
    }

    public double getDuracionEstimadaHoras() {
        return duracionEstimadaHoras;
    }

    public int getDiasGarantia() {
        return diasGarantia;
    }

    // Los setters validan los datos para que el objeto nunca quede inválido.
    public void setTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de servicio no puede estar vacío.");
        }
        this.tipo = tipo.trim();
    }

    public void setFactorTarifa(double factorTarifa) {
        if (factorTarifa <= 0) {
            throw new IllegalArgumentException("El factor de tarifa debe ser mayor a 0.");
        }
        this.factorTarifa = factorTarifa;
    }

    public void setDuracionEstimadaHoras(double duracionEstimadaHoras) {
        if (duracionEstimadaHoras <= 0) {
            throw new IllegalArgumentException("La duración estimada debe ser mayor a 0.");
        }
        this.duracionEstimadaHoras = duracionEstimadaHoras;
    }

    public void setDiasGarantia(int diasGarantia) {
        if (diasGarantia < 0) {
            throw new IllegalArgumentException("Los días de garantía no pueden ser negativos.");
        }
        this.diasGarantia = diasGarantia;
    }

    // Costo de la mano de obra: horas x tarifa base del técnico x factor propio del tipo.
    public double calcularManoObra(double horas, double tarifaBaseTecnico) {
        if (horas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser mayores a 0.");
        }
        if (tarifaBaseTecnico <= 0) {
            throw new IllegalArgumentException("La tarifa base del técnico debe ser mayor a 0.");
        }
        return horas * tarifaBaseTecnico * factorTarifa;
    }

}
