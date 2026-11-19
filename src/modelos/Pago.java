package modelos;

import enums.EstadoPago;
import interfaces.IImprimible;

import java.time.LocalDate;

public abstract class Pago implements IImprimible {
    private final double monto;
    private final LocalDate fecha;
    private EstadoPago estado = EstadoPago.PENDIENTE;

    protected Pago(double monto, LocalDate fecha) {
        Validar.positivo(monto, "monto");
        Validar.objeto(fecha, "fecha");
        this.monto = monto;
        this.fecha = fecha;
    }

    public double getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }
    public EstadoPago getEstado() { return estado; }
    public boolean estaPendiente() { return estado == EstadoPago.PENDIENTE; }
    public boolean estaConfirmado() { return estado == EstadoPago.CONFIRMADO; }

    public void confirmarPago() { cambiarDesdePendiente("confirmar", EstadoPago.CONFIRMADO); }
    public void rechazarPago() { cambiarDesdePendiente("rechazar", EstadoPago.RECHAZADO); }

    private void cambiarDesdePendiente(String accion, EstadoPago nuevo) {
        if (estado != EstadoPago.PENDIENTE) {
            throw new IllegalStateException("No se puede " + accion + " un pago que ya está " + estado.name().toLowerCase() + ".");
        }
        estado = nuevo;
        System.out.println("Pago " + getMetodoPago().toLowerCase() + " de $" + monto + " " + nuevo.name().toLowerCase() + ".");
    }

    // RNF-01: el repositorio deja el pago en el estado que tenía guardado (sin mensajes). Solo desde PENDIENTE.
    public void restaurarEstado(EstadoPago guardado) {
        Validar.objeto(guardado, "estado del pago");
        if (estado != EstadoPago.PENDIENTE) { throw new IllegalStateException("El estado del pago ya fue definido."); }
        estado = guardado;
    }

    public abstract String getMetodoPago();
    public abstract String getDetalle();

    // Referencia que identifica el pago (vacía en efectivo). La sobrescriben Tarjeta y Transferencia.
    // RNF-05: en tarjeta es la referencia de autorización, nunca el número de la tarjeta.
    public String getReferencia() { return ""; }

    @Override
    public void imprimir() { System.out.println(this); }

    @Override
    public String aLinea() { return toString(); }

    @Override
    public String toString() { return getMetodoPago() + " , $" + monto + " , " + fecha + " , " + estado + " , " + getDetalle(); }
}
