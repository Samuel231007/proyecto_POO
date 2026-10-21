import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Orden de servicio: reúne el servicio pedido, el técnico, el estado, los costos,
 * la garantía y los pagos. Implementa Pagable (decisión 7).
 * Contiene la clase anidada estática CambioEstado (decisión 9).
 * Apoya RF-05, RF-06, RF-16, RF-18, RF-17, RF-19 y RNF-04.
 */
public class OrdenServicio implements Pagable {

    // Estados posibles de la orden (RF-06).
    public enum Estado {
        PENDIENTE,
        ASIGNADA,
        EN_PROCESO,
        FINALIZADA,
        CANCELADA
    }

    /**
     * Clase anidada estática: guarda un cambio de estado de la orden (RNF-04).
     * Es estática porque no necesita acceder a la orden que la contiene.
     * Es de solo lectura: se crea una vez y no se modifica.
     */
    public static class CambioEstado {
        private final Estado estadoAnterior;
        private final Estado estadoNuevo;
        private final LocalDateTime fecha;
        private final String responsable;

        // Privado: solo OrdenServicio puede crear cambios, así el historial no se falsifica.
        private CambioEstado(Estado estadoAnterior, Estado estadoNuevo, String responsable) {
            this.estadoAnterior = estadoAnterior;
            this.estadoNuevo = estadoNuevo;
            this.fecha = LocalDateTime.now();
            this.responsable = responsable;
        }

        public Estado getEstadoAnterior() {
            return estadoAnterior;
        }

        public Estado getEstadoNuevo() {
            return estadoNuevo;
        }

        public LocalDateTime getFecha() {
            return fecha;
        }

        public String getResponsable() {
            return responsable;
        }

        @Override
        public String toString() {
            return estadoAnterior + " -> " + estadoNuevo + " (" + responsable + ")";
        }
    }

    // Atributo estático: lo comparten todas las órdenes y sirve para numerarlas.
    private static int consecutivo = 0;

    private final String codigo;
    private final String documentoCliente;
    private final Servicio servicio;
    private final LocalDateTime fechaCreacion;
    private String documentoTecnico;   // Vacío hasta que se asigna un técnico.
    private double tarifaBaseTecnico;  // Tarifa base del técnico asignado.
    private Estado estado;
    private double costoEstimado;
    private double costoFinal;
    private Garantia garantia;         // Se crea al finalizar la orden.
    private final List<CambioEstado> cambios;
    private final List<Pago> pagos;

    // Toda orden nueva queda pendiente (RF-03).
    public OrdenServicio(String codigo, String documentoCliente, Servicio servicio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la orden es obligatorio.");
        }
        if (documentoCliente == null || documentoCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El documento del cliente es obligatorio.");
        }
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio es obligatorio.");
        }
        this.codigo = codigo.trim();
        this.documentoCliente = documentoCliente.trim();
        this.servicio = servicio;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = Estado.PENDIENTE;
        this.cambios = new ArrayList<>();
        this.pagos = new ArrayList<>();
    }

    /**
     * Método estático: genera el siguiente código de orden (OS-001, OS-002...).
     * Es estático porque no depende de una orden en particular.
     */
    public static String generarCodigo() {
        consecutivo++;
        return String.format("OS-%03d", consecutivo);
    }

    /** Método estático: cuántos códigos de orden se han generado. */
    public static int getTotalOrdenes() {
        return consecutivo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public String getDocumentoTecnico() {
        return documentoTecnico;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public double getCostoEstimado() {
        return costoEstimado;
    }

    public double getCostoFinal() {
        return costoFinal;
    }

    public Garantia getGarantia() {
        return garantia;
    }

    public List<CambioEstado> getCambios() {
        return Collections.unmodifiableList(cambios);
    }

    public List<Pago> getPagos() {
        return Collections.unmodifiableList(pagos);
    }

    // ---------- RF-06: cambios de estado ----------

    // Asigna el técnico y pasa la orden a ASIGNADA.
    public void asignarTecnico(String documentoTecnico, double tarifaBaseTecnico, String responsable) {
        if (documentoTecnico == null || documentoTecnico.trim().isEmpty()) {
            throw new IllegalArgumentException("El documento del técnico es obligatorio.");
        }
        if (tarifaBaseTecnico <= 0) {
            throw new IllegalArgumentException("La tarifa base del técnico debe ser mayor a 0.");
        }
        cambiarEstado(Estado.ASIGNADA, responsable);
        this.documentoTecnico = documentoTecnico.trim();
        this.tarifaBaseTecnico = tarifaBaseTecnico;
    }

    public void iniciarTrabajo(String responsable) {
        cambiarEstado(Estado.EN_PROCESO, responsable);
    }

    // Al finalizar se crea la garantía del servicio con los días de su tipo (RF-07).
    public void finalizar(String responsable) {
        cambiarEstado(Estado.FINALIZADA, responsable);
        garantia = new Garantia(LocalDate.now(), servicio.getDiasGarantia(),
                "Fallas del servicio de " + servicio.getTipo());
    }

    public void cancelar(String responsable) {
        cambiarEstado(Estado.CANCELADA, responsable);
    }

    // Único lugar donde cambia el estado: valida la transición y deja el registro.
    private void cambiarEstado(Estado nuevo, String responsable) {
        if (responsable == null || responsable.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar quién hace el cambio.");
        }
        if (nuevo == Estado.FINALIZADA && documentoTecnico == null) {
            throw new IllegalStateException("No se puede finalizar una orden que no ha sido asignada.");
        }
        if (!transicionPermitida(estado, nuevo)) {
            throw new IllegalStateException("No se puede pasar de " + estado + " a " + nuevo + ".");
        }
        cambios.add(new CambioEstado(estado, nuevo, responsable.trim()));
        estado = nuevo;
    }

    private boolean transicionPermitida(Estado actual, Estado nuevo) {
        switch (actual) {
            case PENDIENTE:
                return nuevo == Estado.ASIGNADA || nuevo == Estado.CANCELADA;
            case ASIGNADA:
                return nuevo == Estado.EN_PROCESO || nuevo == Estado.CANCELADA;
            case EN_PROCESO:
                return nuevo == Estado.FINALIZADA;
            default:
                return false; // FINALIZADA y CANCELADA son finales.
        }
    }

    // ---------- RF-05 y RF-16: costos ----------

    // Estimado = mano de obra con la duración estimada + materiales estimados.
    public double calcularCostoEstimado(double tarifaBaseTecnico, double costoMateriales) {
        validarMateriales(costoMateriales);
        costoEstimado = servicio.calcularManoObra(servicio.getDuracionEstimadaHoras(), tarifaBaseTecnico)
                + costoMateriales;
        return costoEstimado;
    }

    // Final = mano de obra con las horas reales + materiales usados.
    public double calcularCostoFinal(double horasReales, double costoMateriales) {
        if (documentoTecnico == null) {
            throw new IllegalStateException("La orden no tiene técnico asignado.");
        }
        validarMateriales(costoMateriales);
        costoFinal = servicio.calcularManoObra(horasReales, tarifaBaseTecnico) + costoMateriales;
        return costoFinal;
    }

    // Diferencia entre final y estimado: positivo = salió más caro de lo estimado.
    public double compararConEstimado() {
        if (costoEstimado <= 0 || costoFinal <= 0) {
            throw new IllegalStateException("Calcule primero el costo estimado y el final.");
        }
        return costoFinal - costoEstimado;
    }

    private void validarMateriales(double costoMateriales) {
        if (costoMateriales < 0) {
            throw new IllegalArgumentException("El costo de materiales no puede ser negativo.");
        }
    }

    // ---------- RF-19: aviso del último cambio de estado ----------

    // Crea la notificación del último cambio de estado; el canal (Notificable) la envía después.
    public Notificacion crearAvisoUltimoCambio(String nombreCliente, String telefonoCliente, String correoCliente) {
        if (cambios.isEmpty()) {
            throw new IllegalStateException("La orden todavía no ha cambiado de estado.");
        }
        CambioEstado ultimo = cambios.get(cambios.size() - 1);
        String mensaje = Notificacion.crearMensajeCambioEstado(nombreCliente, codigo,
                ultimo.getEstadoAnterior().name(), ultimo.getEstadoNuevo().name());
        return new Notificacion(codigo, documentoCliente, nombreCliente, telefonoCliente, correoCliente, mensaje);
    }

    // ---------- RF-18: revisión por garantía ----------

    public void solicitarRevisionGarantia(LocalDate fecha, String problema) {
        if (garantia == null) {
            throw new IllegalStateException("La orden aún no tiene garantía.");
        }
        garantia.solicitarRevision(fecha, problema);
    }

    // ---------- Pagable (RF-17) ----------

    @Override
    public void registrarPago(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago es obligatorio.");
        }
        pagos.add(pago);
    }

    // Saldo = costo final (o el estimado si aún no hay final) menos los pagos confirmados.
    @Override
    public double getSaldoPendiente() {
        double total = costoFinal > 0 ? costoFinal : costoEstimado;
        double pagado = 0;
        for (Pago p : pagos) {
            if (p.estaConfirmado()) {
                pagado += p.getMonto();
            }
        }
        return total - pagado;
    }

    @Override
    public String toString() {
        return "Orden " + codigo + " [" + servicio.getTipo() + ", " + estado + "]";
    }
}
