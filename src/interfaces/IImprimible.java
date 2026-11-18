package interfaces;

/**
 * Contrato de presentación en consola (igual que en el proyecto base).
 * Lo implementan Cliente, Tecnico, Servicio, OrdenServicio y Pago.
 */
public interface IImprimible {

    /** Muestra la información completa del objeto en la consola. */
    void imprimir();

    /** Devuelve los datos principales en una sola línea, útil para listados. */
    String aLinea();
}
