package modelos;

import enums.EstadoOrden;
import interfaces.IImprimible;
import interfaces.Notificable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Clase central: une cliente, solicitud, servicio y técnico, y maneja estado, costos, garantía, calificación, pagos y avisos.
public class OrdenServicio implements IImprimible {
    // Constructor privado y solo getters: el historial de cambios no se puede falsificar (RNF-04).
    public static class CambioEstado {
        private final EstadoOrden estadoAnterior;
        private final EstadoOrden estadoNuevo;
        private final LocalDateTime fecha;
        private final String responsable;

        private CambioEstado(EstadoOrden estadoAnterior, EstadoOrden estadoNuevo, String responsable) {
            this(estadoAnterior, estadoNuevo, LocalDateTime.now(), responsable);
        }

        // RNF-01: se usa al leer el archivo, con la fecha original del cambio.
        private CambioEstado(EstadoOrden estadoAnterior, EstadoOrden estadoNuevo, LocalDateTime fecha, String responsable) {
            this.estadoAnterior = estadoAnterior;
            this.estadoNuevo = estadoNuevo;
            this.fecha = fecha;
            this.responsable = responsable;
        }

        public EstadoOrden getEstadoAnterior() { return estadoAnterior; }
        public EstadoOrden getEstadoNuevo() { return estadoNuevo; }
        public LocalDateTime getFecha() { return fecha; }
        public String getResponsable() { return responsable; }

        @Override
        public String toString() { return fecha.toLocalDate() + " " + estadoAnterior + " -> " + estadoNuevo + " (por " + responsable + ")"; }
    }

    private static int consecutivo = 0;

    private final String codigo;
    private final Cliente cliente;
    private final Solicitud solicitud;
    private final Servicio servicio;
    private final Asignacion asignacion;
    private final LocalDateTime fechaCreacion;
    private Tecnico tecnico;              // null hasta que se asigna
    private EstadoOrden estado = EstadoOrden.PENDIENTE;
    private double costoMaterialesEstimado, costoEstimado;
    private double costoManoObraFinal, costoMaterialesFinal, costoFinal;
    private DetalleTrabajo detalle;
    private Garantia garantia;
    private Calificacion calificacion;
    private final List<CambioEstado> cambios = new ArrayList<>();
    private final List<Pago> pagos = new ArrayList<>();
    private final List<Notificacion> notificaciones = new ArrayList<>();

    public OrdenServicio(String codigo, Cliente cliente, Solicitud solicitud, Servicio servicio) {
        Validar.objeto(cliente, "cliente");
        Validar.objeto(solicitud, "solicitud");
        Validar.objeto(servicio, "servicio");
        if (solicitud.getEstado() != EstadoOrden.PENDIENTE) { throw new IllegalStateException("La solicitud " + solicitud.getIdSolicitud() + " ya no está pendiente."); }
        if (!servicio.getTipo().equalsIgnoreCase(solicitud.getTipoProblema())) {
            throw new IllegalArgumentException("El servicio (" + servicio.getTipo() + ") no coincide con el tipo de problema de la solicitud (" + solicitud.getTipoProblema() + ").");
        }
        this.codigo = Validar.texto(codigo, "código de la orden");
        this.cliente = cliente;
        this.solicitud = solicitud;
        this.servicio = servicio;
        this.fechaCreacion = LocalDateTime.now();
        this.asignacion = new Asignacion(this);
    }

    // RNF-01 (sobrecarga de constructores): lo usa OrdenServicioRepository para reconstruir la orden tal como quedó guardada.
    // No vuelve a validar las reglas de negocio ni envía avisos: esos datos ya se validaron cuando se crearon.
    public OrdenServicio(String codigo, Cliente cliente, Solicitud solicitud, Servicio servicio, LocalDateTime fechaCreacion,
                         Tecnico tecnico, EstadoOrden estado, double costoMaterialesEstimado, double costoEstimado,
                         double costoManoObraFinal, double costoMaterialesFinal, double costoFinal) {
        Validar.objeto(cliente, "cliente");
        Validar.objeto(solicitud, "solicitud");
        Validar.objeto(servicio, "servicio");
        Validar.objeto(fechaCreacion, "fecha de creación");
        Validar.objeto(estado, "estado");
        this.codigo = Validar.texto(codigo, "código de la orden");
        this.cliente = cliente;
        this.solicitud = solicitud;
        this.servicio = servicio;
        this.fechaCreacion = fechaCreacion;
        this.tecnico = tecnico;
        this.estado = estado;
        this.costoMaterialesEstimado = costoMaterialesEstimado;
        this.costoEstimado = costoEstimado;
        this.costoManoObraFinal = costoManoObraFinal;
        this.costoMaterialesFinal = costoMaterialesFinal;
        this.costoFinal = costoFinal;
        this.asignacion = new Asignacion(this);
        actualizarConsecutivo(this.codigo);
    }

    // Devuelve el siguiente código de orden: OS-001, OS-002...
    public static String generarCodigo() {
        consecutivo++;
        String ceros = consecutivo < 10 ? "00" : consecutivo < 100 ? "0" : "";
        return "OS-" + ceros + consecutivo;
    }

    public static int getTotalOrdenes() { return consecutivo; }

    // RNF-01: si se cargó la orden OS-007, la siguiente nueva será OS-008 (no se repiten códigos).
    private static void actualizarConsecutivo(String codigo) {
        try {
            int numero = Integer.parseInt(codigo.substring(codigo.indexOf('-') + 1));
            if (numero > consecutivo) { consecutivo = numero; }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // Código con otro formato: no afecta el consecutivo.
        }
    }

    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public String getDocumentoCliente() { return cliente.getDocumento(); }
    public Solicitud getSolicitud() { return solicitud; }
    public Servicio getServicio() { return servicio; }
    public Asignacion getAsignacion() { return asignacion; }
    public Tecnico getTecnico() { return tecnico; }
    public String getDocumentoTecnico() { return tecnico == null ? null : tecnico.getDocumento(); }
    public EstadoOrden getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public DetalleTrabajo getDetalle() { return detalle; }
    public Garantia getGarantia() { return garantia; }
    public Calificacion getCalificacion() { return calificacion; }
    public double getCostoEstimado() { return costoEstimado; }
    public double getCostoMaterialesEstimado() { return costoMaterialesEstimado; }
    public double getCostoManoObraFinal() { return costoManoObraFinal; }
    public double getCostoMaterialesFinal() { return costoMaterialesFinal; }
    public double getCostoFinal() { return costoFinal; }
    public List<CambioEstado> getCambios() { return new ArrayList<>(cambios); }
    public List<Notificacion> getNotificaciones() { return new ArrayList<>(notificaciones); }

    // RF-09: activa = no ha terminado (ni finalizada ni cancelada).
    public boolean estaActiva() { return estado != EstadoOrden.FINALIZADA && estado != EstadoOrden.CANCELADA; }

    // Sin "public": solo Asignacion la llama, después de revisar especialidad, zona y horario (RF-04 y RF-13).
    // Si ya estaba ASIGNADA es una reasignación (RF-14): sigue ASIGNADA y solo cambia el técnico.
    void asignarTecnico(Tecnico nuevo, String responsable) {
        Validar.objeto(nuevo, "técnico");
        Validar.texto(responsable, "responsable");
        if (estado != EstadoOrden.ASIGNADA) { cambiarEstado(EstadoOrden.ASIGNADA, responsable); }
        tecnico = nuevo;
        nuevo.recibirSolicitud(solicitud);
        // Si ya había estimado, se recalcula con la tarifa del técnico nuevo.
        if (costoEstimado > 0) { calcularCostoEstimado(nuevo.getTarifaBase(), costoMaterialesEstimado); }
    }

    // RF-06: ASIGNADA -> EN_PROCESO.
    public void iniciarTrabajo(String responsable) { cambiarEstado(EstadoOrden.EN_PROCESO, responsable); }

    // RF-07 y RF-16: al finalizar se calcula el costo final y se crea la garantía.
    // RNF-03: primero se calcula todo; si algo falla, la orden queda exactamente igual.
    public void finalizar(String responsable) {
        if (tecnico == null) { throw new IllegalStateException("No se puede finalizar una orden que no ha sido asignada."); }
        if (estado != EstadoOrden.EN_PROCESO) { throw new IllegalStateException("No se puede pasar de " + estado + " a FINALIZADA."); }
        if (detalle == null) { throw new IllegalStateException("Registre primero el detalle del trabajo (actividades, horas y materiales)."); }
        Validar.texto(responsable, "responsable");
        double manoObra = servicio.calcularManoObra(detalle.getHorasTrabajo(), tecnico.getTarifaBase());
        double materiales = detalle.calcularCostoMateriales();
        Garantia nueva = new Garantia(LocalDate.now(), servicio.getDiasGarantia(), "Fallas del servicio de " + servicio.getTipo());

        costoManoObraFinal = manoObra;
        costoMaterialesFinal = materiales;
        costoFinal = manoObra + materiales;
        garantia = nueva;
        cambiarEstado(EstadoOrden.FINALIZADA, responsable);
    }

    // RF-06 y RF-12: cancela la orden y guarda el motivo en la solicitud.
    public void cancelar(String responsable, String motivo) {
        String motivoLimpio = Validar.texto(motivo, "motivo de cancelación");
        cambiarEstado(EstadoOrden.CANCELADA, responsable);
        solicitud.registrarMotivoCancelacion(motivoLimpio);
    }

    // Único lugar donde cambia el estado: valida la transición, deja el registro y avisa al cliente.
    private void cambiarEstado(EstadoOrden nuevo, String responsable) {
        Validar.texto(responsable, "responsable");
        if (nuevo == EstadoOrden.FINALIZADA && tecnico == null) {
            throw new IllegalStateException("No se puede finalizar una orden que no ha sido asignada.");
        }
        if (!transicionPermitida(estado, nuevo)) {
            throw new IllegalStateException("No se puede pasar de " + estado + " a " + nuevo + ".");
        }
        CambioEstado cambio = new CambioEstado(estado, nuevo, responsable.trim());
        cambios.add(cambio);
        estado = nuevo;
        solicitud.actualizarEstado(nuevo);   // la solicitud siempre refleja el estado de su orden
        notificarCliente(cambio);
    }

    private boolean transicionPermitida(EstadoOrden actual, EstadoOrden nuevo) {
        switch (actual) {
            case PENDIENTE: return nuevo == EstadoOrden.ASIGNADA || nuevo == EstadoOrden.CANCELADA;
            case ASIGNADA: return nuevo == EstadoOrden.EN_PROCESO || nuevo == EstadoOrden.CANCELADA;
            case EN_PROCESO: return nuevo == EstadoOrden.FINALIZADA;
            default: return false;   // FINALIZADA y CANCELADA son finales
        }
    }

    // Si el envío falla, la notificación queda FALLIDA con su causa, pero el cambio de estado sí se conserva.
    private void notificarCliente(CambioEstado cambio) {
        Notificable canal = cliente.getCanalNotificacion();
        String mensaje = Notificacion.crearMensajeCambioEstado(cliente.getNombre(), codigo,
                cambio.getEstadoAnterior().name(), cambio.getEstadoNuevo().name());
        Notificacion aviso = new Notificacion(codigo, cliente.getDocumento(), cliente.getNombre(),
                cliente.getTelefono(), cliente.getCorreo(), mensaje);
        canal.enviar(aviso);
        notificaciones.add(aviso);
        System.out.println("  Aviso al cliente -> " + aviso);
    }

    // Sin "public": lo registra el técnico asignado con Tecnico.registrarTrabajo.
    void registrarDetalle(DetalleTrabajo nuevo) {
        Validar.objeto(nuevo, "detalle del trabajo");
        if (estado != EstadoOrden.EN_PROCESO) { throw new IllegalStateException("Solo se registra el trabajo de una orden EN_PROCESO (está " + estado + ")."); }
        nuevo.validarCompleto();
        detalle = nuevo;
    }

    // Estimado = mano de obra con la duración estimada + materiales estimados.
    public double calcularCostoEstimado(double tarifaBaseTecnico, double costoMateriales) {
        if (costoMateriales < 0) { throw new IllegalArgumentException("El costo de materiales no puede ser negativo."); }
        double manoObra = servicio.calcularManoObra(servicio.getDuracionEstimadaHoras(), tarifaBaseTecnico);
        costoMaterialesEstimado = costoMateriales;
        costoEstimado = manoObra + costoMateriales;
        return costoEstimado;
    }

    // Positivo = salió más caro de lo estimado.
    public double compararConEstimado() {
        if (costoEstimado <= 0 || costoFinal <= 0) { throw new IllegalStateException("Calcule primero el costo estimado y finalice la orden."); }
        return costoFinal - costoEstimado;
    }

    public String getDesgloseCosto() {
        if (costoFinal <= 0) { throw new IllegalStateException("La orden " + codigo + " aún no tiene costo final."); }
        String texto = "Costo final de la orden " + codigo + ":"
                + "\n  Mano de obra: " + detalle.getHorasTrabajo() + " h x " + dinero(tecnico.getTarifaBase())
                + " x factor " + servicio.getFactorTarifa() + " = " + dinero(costoManoObraFinal)
                + "\n  Materiales: " + dinero(costoMaterialesFinal)
                + "\n  TOTAL: " + dinero(costoFinal);
        if (costoEstimado > 0) {
            double diferencia = compararConEstimado();
            texto += "\n  Estimado inicial: " + dinero(costoEstimado) + " | Diferencia: "
                    + (diferencia >= 0 ? "+" : "-") + dinero(Math.abs(diferencia))
                    + (diferencia > 0 ? " (más caro)" : diferencia < 0 ? " (más barato)" : " (igual)");
        }
        return texto;
    }

    private static String dinero(double valor) { return String.format("$%,.0f", valor); }

    // RF-10: solo una orden finalizada y una sola vez.
    public void calificar(Calificacion nueva) {
        Validar.objeto(nueva, "calificación");
        if (estado != EstadoOrden.FINALIZADA) { throw new IllegalStateException("Solo se puede calificar una orden finalizada (está " + estado + ")."); }
        if (calificacion != null) { throw new IllegalStateException("Esta orden ya fue calificada."); }
        calificacion = nueva;
    }

    // RF-18: la orden debe estar finalizada (tener garantía).
    public void solicitarRevisionGarantia(LocalDate fecha, String problema) {
        if (garantia == null) { throw new IllegalStateException("La orden aún no tiene garantía: debe estar finalizada."); }
        garantia.solicitarRevision(fecha, problema);
    }

    // RF-17: el pago queda PENDIENTE hasta que el administrador lo confirme.
    public void registrarPago(Pago pago) {
        Validar.objeto(pago, "pago");
        if (estado == EstadoOrden.CANCELADA) { throw new IllegalStateException("No se pueden registrar pagos en una orden cancelada."); }
        double saldo = getSaldoPendiente();
        if (saldo <= 0) { throw new IllegalStateException("La orden no tiene saldo por pagar."); }
        // Los pagos que aún no se confirman también cuentan: así no se puede pagar dos veces lo mismo.
        double enRevision = 0;
        for (Pago p : pagos) { if (p.estaPendiente()) { enRevision += p.getMonto(); } }
        double disponible = saldo - enRevision;
        if (disponible <= 0) {
            throw new IllegalStateException("Ya hay pagos por " + dinero(enRevision) + " esperando confirmación que cubren el saldo. Espere a que el administrador los revise.");
        }
        if (pago.getMonto() > disponible) {
            throw new IllegalArgumentException("El pago (" + dinero(pago.getMonto()) + ") supera lo que falta por pagar (" + dinero(disponible)
                    + (enRevision > 0 ? ", descontando " + dinero(enRevision) + " en revisión" : "") + ").");
        }
        pagos.add(pago);
        System.out.println("Pago registrado: " + pago);
    }

    // Saldo = costo final (o el estimado si aún no hay final) menos los pagos confirmados.
    public double getSaldoPendiente() {
        double saldo = costoFinal > 0 ? costoFinal : costoEstimado;
        for (Pago p : pagos) {
            if (p.estaConfirmado()) { saldo -= p.getMonto(); }
        }
        return saldo;
    }

    // RNF-02 y RNF-05: los pagos solo los ve el administrador o el cliente dueño de la orden.
    public List<Pago> getPagos(Usuario quienConsulta) {
        if (!(quienConsulta instanceof Administrador) && quienConsulta != cliente) {
            throw new SecurityException("No tiene permiso para ver los pagos de la orden " + codigo + ".");
        }
        return new ArrayList<>(pagos);
    }

    // Métodos para los repositorios (RNF-01): no piden un Usuario porque no son consultas de una persona.

    public List<Pago> getPagosParaGuardar() { return new ArrayList<>(pagos); }

    // Los métodos restaurar... devuelven a la orden lo que estaba guardado, sin validar de nuevo ni enviar avisos.
    public void restaurarCambio(EstadoOrden anterior, EstadoOrden nuevo, LocalDateTime fecha, String responsable) {
        cambios.add(new CambioEstado(anterior, nuevo, fecha, Validar.texto(responsable, "responsable")));
    }

    public void restaurarDetalle(DetalleTrabajo guardado) {
        Validar.objeto(guardado, "detalle del trabajo");
        guardado.validarCompleto();
        detalle = guardado;
    }

    public void restaurarGarantia(Garantia guardada) {
        Validar.objeto(guardada, "garantía");
        garantia = guardada;
    }

    public void restaurarPago(Pago guardado) {
        Validar.objeto(guardado, "pago");
        pagos.add(guardado);
    }

    public void restaurarNotificacion(Notificacion guardada) {
        Validar.objeto(guardada, "notificación");
        notificaciones.add(guardada);
    }

    @Override
    public void imprimir() {
        System.out.println(this);
        System.out.println("  Solicitud #" + solicitud.getIdSolicitud() + ": " + solicitud.getDescripcion()
                + " | " + solicitud.getUbicacion() + " | " + solicitud.getHorarioDisponible());
        if (costoEstimado > 0) { System.out.println("  Costo estimado: " + dinero(costoEstimado)); }
        if (costoFinal > 0) { System.out.println("  " + getDesgloseCosto().replace("\n", "\n  ")); }
        if (estado != EstadoOrden.CANCELADA && (costoFinal > 0 || costoEstimado > 0)) {
            System.out.println("  Saldo pendiente: " + dinero(getSaldoPendiente()));
        }
        if (estado == EstadoOrden.CANCELADA) { System.out.println("  Motivo de cancelación: " + solicitud.getMotivoCancelacion()); }
        if (garantia != null) { System.out.println("  Garantía vigente hasta: " + garantia.getFechaFin() + " | Revisiones: " + garantia.getRevisiones()); }
        if (calificacion != null) { System.out.println("  Calificación: " + calificacion); }
    }

    @Override
    public String aLinea() { return toString(); }

    @Override
    public String toString() {
        return codigo + " | " + servicio.getTipo() + " | Cliente: " + cliente.getNombre()
                + " | Técnico: " + (tecnico == null ? "sin asignar" : tecnico.getNombre()) + " | " + estado
                + " | Creada: " + fechaCreacion.toLocalDate();
    }
}
