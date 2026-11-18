package modelos;

import interfaces.IImprimible;

/** Tipo de servicio (plomería, electricidad...) con su factor de tarifa, duración y garantía. RF-05 y RF-07. */
public class Servicio implements IImprimible {
    public static final double FACTOR_NORMAL = 1.0;   // factor de un servicio sin recargo
    private final String tipo;
    private final double factorTarifa;          // 1.0 = tarifa normal
    private final double duracionEstimadaHoras;
    private final int diasGarantia;

    public Servicio(String tipo, double factorTarifa, double duracionEstimadaHoras, int diasGarantia) {
        this.tipo = Validar.texto(tipo, "tipo de servicio");
        this.factorTarifa = Validar.positivo(factorTarifa, "factor de tarifa");
        this.duracionEstimadaHoras = Validar.positivo(duracionEstimadaHoras, "duración estimada");
        if (diasGarantia < 0) { throw new IllegalArgumentException("Los días de garantía no pueden ser negativos."); }
        this.diasGarantia = diasGarantia;
    }

    public String getTipo() { return tipo; }
    public double getFactorTarifa() { return factorTarifa; }
    public double getDuracionEstimadaHoras() { return duracionEstimadaHoras; }
    public int getDiasGarantia() { return diasGarantia; }

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
