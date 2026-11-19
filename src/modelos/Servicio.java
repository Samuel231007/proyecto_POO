package modelos;

import interfaces.IImprimible;

// Tipo de servicio (Plomería, Electricidad...) con su factor de tarifa, duración estimada y días de garantía (RF-05, RF-07).
public class Servicio implements IImprimible {
    public static final double FACTOR_NORMAL = 1.0;
    private String tipo;
    private double factorTarifa;          // 1.0 = tarifa normal
    private double duracionEstimadaHoras;
    private int diasGarantia;

    // Decisión 18: los atributos son privados y los setters validan los datos (el constructor los usa).
    public Servicio(String tipo, double factorTarifa, double duracionEstimadaHoras, int diasGarantia) {
        setTipo(tipo);
        setFactorTarifa(factorTarifa);
        setDuracionEstimadaHoras(duracionEstimadaHoras);
        setDiasGarantia(diasGarantia);
    }

    public String getTipo() { return tipo; }
    public double getFactorTarifa() { return factorTarifa; }
    public double getDuracionEstimadaHoras() { return duracionEstimadaHoras; }
    public int getDiasGarantia() { return diasGarantia; }

    // Si el dato no es válido lanzan una excepción y el servicio conserva su valor anterior.
    public void setTipo(String tipo) { this.tipo = Validar.texto(tipo, "tipo de servicio"); }
    public void setFactorTarifa(double factorTarifa) { this.factorTarifa = Validar.positivo(factorTarifa, "factor de tarifa"); }
    public void setDuracionEstimadaHoras(double horas) { this.duracionEstimadaHoras = Validar.positivo(horas, "duración estimada"); }

    public void setDiasGarantia(int diasGarantia) {
        if (diasGarantia < 0) { throw new IllegalArgumentException("Los días de garantía no pueden ser negativos."); }
        this.diasGarantia = diasGarantia;
    }

    // RF-05: cada tipo aplica su propia tarifa. Mano de obra = horas x tarifa base del técnico x factor del tipo.
    public double calcularManoObra(double horas, double tarifaBaseTecnico) {
        Validar.positivo(horas, "horas");
        Validar.positivo(tarifaBaseTecnico, "tarifa base del técnico");
        return horas * tarifaBaseTecnico * factorTarifa;
    }

    @Override
    public void imprimir() { System.out.println(aLinea()); }

    @Override
    public String aLinea() {
        return String.format("%-14s factor %.2f | %.1f h estimadas | %d días de garantía", tipo, factorTarifa,
                duracionEstimadaHoras, diasGarantia);
    }
}
