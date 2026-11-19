package servicios;

import interfaces.IServicio;
import interfaces.Notificable;
import modelos.Cliente;
import modelos.DisponibilidadTecnico.FranjaHoraria;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Servicio;
import modelos.Solicitud;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Acciones del cliente. Cada método usa los modelos y luego guarda el cambio con el gestor (RNF-01).
public class ClienteServicio implements IServicio<Cliente> {
    private final GestorPlataforma gestor;

    public ClienteServicio(GestorPlataforma gestor) {
        this.gestor = gestor;
    }

    @Override
    public ArrayList<Cliente> getDataList() { return gestor.getClientes(); }

    @Override
    public ArrayList<Cliente> buscarPorNombre(String nombre) {
        ArrayList<Cliente> resultado = new ArrayList<>();
        for (Cliente c : gestor.getClientes()) {
            if (c.getNombre().toLowerCase().contains(nombre.toLowerCase().trim())) { resultado.add(c); }
        }
        return resultado;
    }

    @Override
    public Cliente buscarPorId(String documento) { return gestor.buscarCliente(documento); }

    @Override
    public boolean existeEntidad(String documento) { return buscarPorId(documento) != null; }

    @Override
    public void mostrarTodos() {
        ArrayList<Cliente> todos = getDataList();
        if (todos.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println(String.format("%-12s %-22s %-12s %-26s %-12s %s", "Documento", "Nombre", "Teléfono", "Correo", "Ubicación", "Avisos"));
        System.out.println("─".repeat(100));
        for (Cliente c : todos) { System.out.println(c.aLinea()); }
        System.out.println("Total: " + todos.size() + " cliente(s)");
    }

    public void registrarCliente(Cliente cliente) { gestor.registrarCliente(cliente); }

    // RNF-02: devuelve el cliente solo si la contraseña es correcta; si no, null.
    public Cliente iniciarSesion(String documento, String contrasenia) {
        Cliente cliente = gestor.buscarCliente(documento);
        if (cliente == null) {
            System.out.println("No existe un cliente con el documento " + documento + ".");
            return null;
        }
        return cliente.iniciarSesion(contrasenia) ? cliente : null;
    }

    // RF-11: actualiza los datos de contacto y la ubicación.
    public void actualizarDatos(Cliente cliente, String nombre, String telefono, String correo, String ubicacion) {
        cliente.actualizarDatos(nombre, telefono, correo, ubicacion);
        gestor.guardarCliente(cliente);
    }

    // RF-19: cambia el canal por el que le llegan los avisos.
    public void cambiarCanalNotificacion(Cliente cliente, Notificable canal) {
        cliente.setCanalNotificacion(canal);
        gestor.guardarCliente(cliente);
    }

    public void cambiarContrasenia(Cliente cliente, String actual, String nueva) {
        cliente.cambiarContrasenia(actual, nueva);
        gestor.guardarCliente(cliente);
    }

    // RF-03: primero se revisa que el servicio exista, para no gastar un ID de solicitud si no se puede atender.
    public OrdenServicio solicitarServicio(Cliente cliente, String tipoProblema, String ubicacion,
                                           String descripcion, FranjaHoraria horario) {
        Servicio servicio = gestor.buscarServicio(tipoProblema);
        if (servicio == null) {
            throw new IllegalArgumentException("La plataforma no ofrece el servicio \"" + tipoProblema + "\". Consulte la lista de servicios.");
        }
        Solicitud solicitud = new Solicitud(servicio.getTipo(), ubicacion, descripcion, horario);
        cliente.solicitarServicio(solicitud);
        OrdenServicio orden = new OrdenServicio(OrdenServicio.generarCodigo(), cliente, solicitud, servicio);
        gestor.registrarOrden(orden);
        return orden;
    }

    public List<Servicio> getServiciosDisponibles() { return gestor.getServicios(); }

    // RF-08: todas las órdenes del cliente.
    public List<OrdenServicio> consultarHistorial(Cliente cliente) {
        return gestor.consultarHistorialCliente(cliente);
    }

    // RNF-02: busca una orden y verifica que sea del cliente.
    public OrdenServicio buscarOrdenPropia(Cliente cliente, String codigo) {
        OrdenServicio orden = gestor.buscarOrden(codigo);
        if (orden == null) { throw new IllegalArgumentException("No existe la orden " + codigo + "."); }
        cliente.verificarPropia(orden);
        return orden;
    }

    // RF-12: devuelve false si la orden ya tiene técnico.
    public boolean cancelarSolicitud(Cliente cliente, String codigo, String motivo) {
        OrdenServicio orden = buscarOrdenPropia(cliente, codigo);
        boolean cancelada = cliente.cancelarSolicitud(orden, motivo);
        if (cancelada) { gestor.guardarOrden(orden); }
        return cancelada;
    }

    // RF-10: califica una orden propia finalizada.
    public void calificarServicio(Cliente cliente, String codigo, int estrellas, String comentario) {
        OrdenServicio orden = buscarOrdenPropia(cliente, codigo);
        cliente.calificarServicio(orden, estrellas, comentario);
        gestor.guardarOrden(orden);
    }

    // RF-18: pide una revisión por garantía.
    public void solicitarRevisionGarantia(Cliente cliente, String codigo, LocalDate fecha, String problema) {
        OrdenServicio orden = buscarOrdenPropia(cliente, codigo);
        cliente.solicitarRevisionGarantia(orden, fecha, problema);
        gestor.guardarOrden(orden);
    }

    // El cliente registra el pago; queda PENDIENTE hasta que el administrador lo confirme.
    public void registrarPago(Cliente cliente, String codigo, Pago pago) {
        OrdenServicio orden = buscarOrdenPropia(cliente, codigo);
        orden.registrarPago(pago);
        gestor.guardarOrden(orden);
    }

    // RNF-05: el cliente solo ve los pagos de sus órdenes.
    public List<Pago> consultarPagos(Cliente cliente, String codigo) {
        return buscarOrdenPropia(cliente, codigo).getPagos(cliente);
    }
}
