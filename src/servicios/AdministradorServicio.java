package servicios;

import enums.EstadoOrden;
import interfaces.IServicio;
import modelos.Administrador;
import modelos.Cliente;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Servicio;
import modelos.Tecnico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Acciones del administrador. Cada método usa los modelos y luego guarda el cambio con el gestor (RNF-01).
public class AdministradorServicio implements IServicio<Administrador> {
    private final GestorPlataforma gestor;

    public AdministradorServicio(GestorPlataforma gestor) {
        this.gestor = gestor;
    }

    @Override
    public ArrayList<Administrador> getDataList() { return gestor.getAdministradores(); }

    @Override
    public ArrayList<Administrador> buscarPorNombre(String nombre) {
        ArrayList<Administrador> resultado = new ArrayList<>();
        for (Administrador a : gestor.getAdministradores()) {
            if (a.getNombre().toLowerCase().contains(nombre.toLowerCase().trim())) { resultado.add(a); }
        }
        return resultado;
    }

    @Override
    public Administrador buscarPorId(String documento) { return gestor.buscarAdministrador(documento); }

    @Override
    public boolean existeEntidad(String documento) { return buscarPorId(documento) != null; }

    @Override
    public void mostrarTodos() {
        for (Administrador a : getDataList()) {
            System.out.println(a.getDocumento() + " | " + a.getNombre() + " | " + a.getAreaResponsable());
        }
    }

    public void registrarAdministrador(Administrador administrador) { gestor.registrarAdministrador(administrador); }

    public Administrador iniciarSesion(String documento, String contrasenia) {
        Administrador admin = gestor.buscarAdministrador(documento);
        if (admin == null) {
            System.out.println("No existe un administrador con el documento " + documento + ".");
            return null;
        }
        return admin.iniciarSesion(contrasenia) ? admin : null;
    }

    // RF-01: el gestor rechaza documentos repetidos.
    public void registrarTecnico(Administrador admin, Tecnico tecnico) {
        gestor.registrarTecnico(tecnico);
        System.out.println("Técnico " + tecnico.getNombre() + " registrado en la plataforma por " + admin.getNombre() + ".");
    }

    // RF-05 y RF-07: agrega un tipo de servicio al catálogo.
    public void registrarServicio(Servicio servicio) {
        gestor.registrarServicio(servicio);
        System.out.println("Servicio " + servicio.getTipo() + " agregado al catálogo.");
    }

    public void mostrarServicios() {
        if (gestor.getServicios().isEmpty()) { System.out.println("No hay servicios registrados."); }
        for (Servicio s : gestor.getServicios()) { s.imprimir(); }
    }

    public OrdenServicio buscarOrden(String codigo) {
        OrdenServicio orden = gestor.buscarOrden(codigo);
        if (orden == null) { throw new IllegalArgumentException("No existe la orden " + codigo + "."); }
        return orden;
    }

    public List<OrdenServicio> getOrdenes() { return gestor.getOrdenes(); }

    // RF-04: técnicos de la especialidad y zona de la orden, libres en su horario.
    public List<Tecnico> buscarTecnicosDisponibles(String codigo) {
        return gestor.buscarTecnicosDisponibles(buscarOrden(codigo).getSolicitud());
    }

    // RF-04 y RF-05: asigna el técnico y calcula el costo estimado con su tarifa y los materiales estimados.
    public boolean asignarTecnico(Administrador admin, String codigo, String documentoTecnico, double materialesEstimados) {
        OrdenServicio orden = buscarOrden(codigo);
        Tecnico tecnico = gestor.buscarTecnico(documentoTecnico);
        if (materialesEstimados < 0) { throw new IllegalArgumentException("El costo de materiales no puede ser negativo."); }
        boolean asignado = orden.getAsignacion().asignarTecnico(tecnico, admin.getNombre());
        if (asignado) {
            double estimado = orden.calcularCostoEstimado(tecnico.getTarifaBase(), materialesEstimados);
            System.out.println("Costo estimado: " + String.format("$%,.0f", estimado));
            gestor.guardarOrden(orden);
        }
        return asignado;
    }

    // RF-14: cambia el técnico antes de iniciar el trabajo.
    public boolean reasignarOrden(Administrador admin, String codigo, String documentoTecnico) {
        OrdenServicio orden = buscarOrden(codigo);
        boolean reasignada = admin.reasignarOrden(orden, gestor.buscarTecnico(documentoTecnico));
        if (reasignada) { gestor.guardarOrden(orden); }
        return reasignada;
    }

    // RF-06: el administrador puede cancelar una orden PENDIENTE o ASIGNADA.
    public void cancelarOrden(Administrador admin, String codigo, String motivo) {
        OrdenServicio orden = buscarOrden(codigo);
        orden.cancelar(admin.getNombre(), motivo);
        gestor.guardarOrden(orden);
        System.out.println("Orden " + orden.getCodigo() + " cancelada.");
    }

    // RF-20: un filtro en null no se aplica.
    public List<OrdenServicio> filtrarOrdenes(EstadoOrden estado, LocalDate fecha, Cliente cliente, Tecnico tecnico) {
        return gestor.filtrarOrdenes(estado, fecha, cliente, tecnico);
    }

    // RF-17: el administrador revisa los pagos de una orden.
    public List<Pago> consultarPagos(Administrador admin, String codigo) {
        return buscarOrden(codigo).getPagos(admin);
    }

    // numeroPago empieza en 1 (como se muestra en la lista).
    public void confirmarPago(Administrador admin, String codigo, int numeroPago) {
        OrdenServicio orden = buscarOrden(codigo);
        obtenerPago(admin, orden, numeroPago).confirmarPago();
        gestor.guardarOrden(orden);
    }

    public void rechazarPago(Administrador admin, String codigo, int numeroPago) {
        OrdenServicio orden = buscarOrden(codigo);
        obtenerPago(admin, orden, numeroPago).rechazarPago();
        gestor.guardarOrden(orden);
    }

    private Pago obtenerPago(Administrador admin, OrdenServicio orden, int numeroPago) {
        List<Pago> pagos = orden.getPagos(admin);
        if (numeroPago < 1 || numeroPago > pagos.size()) {
            throw new IllegalArgumentException("La orden " + orden.getCodigo() + " no tiene el pago número " + numeroPago + ".");
        }
        return pagos.get(numeroPago - 1);
    }

    // RNF-04: cambios de estado, asignaciones y avisos de una orden.
    public void mostrarTrazabilidad(String codigo) {
        OrdenServicio orden = buscarOrden(codigo);
        System.out.println("Cambios de estado de " + orden.getCodigo() + ":");
        for (OrdenServicio.CambioEstado c : orden.getCambios()) { System.out.println("  - " + c); }
        System.out.println("Asignaciones:");
        for (String r : orden.getAsignacion().getRegistroCambios()) { System.out.println("  - " + r); }
        System.out.println("Avisos al cliente:");
        for (modelos.Notificacion n : orden.getNotificaciones()) { System.out.println("  - " + n); }
    }
}
