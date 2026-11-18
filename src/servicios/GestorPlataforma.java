package servicios;

import enums.EstadoOrden;
import modelos.Administrador;
import modelos.Cliente;
import modelos.OrdenServicio;
import modelos.Servicio;
import modelos.Solicitud;
import modelos.Tecnico;
import repositorios.AdministradorRepository;
import repositorios.ClienteRepository;
import repositorios.OrdenServicioRepository;
import repositorios.ServicioRepository;
import repositorios.SolicitudRepository;
import repositorios.TecnicoRepository;
import modelos.Validar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda las listas de administradores, técnicos, clientes, servicios y órdenes, responde las consultas
 * y las guarda en los archivos .txt a través de los repositorios.
 * Apoya RF-01, RF-02, RF-04, RF-08, RF-09, RF-20, RNF-01, RNF-02 y RNF-03.
 *
 * MODO DUAL (igual que EstudianteServicio en el proyecto base):
 *   new GestorPlataforma()       → MODO ARCHIVO: carga los .txt al iniciar y guarda cada cambio (RNF-01).
 *   new GestorPlataforma(false)  → MODO MEMORIA: solo ArrayList; al cerrar el programa los datos se pierden.
 *
 * Los datos se cargan UNA vez en las listas y todas las órdenes apuntan a esos mismos objetos.
 * Por eso, después de cada operación, los servicios llaman a guardarOrden / guardarCliente / guardarTecnico.
 */
public class GestorPlataforma {

    private final boolean usarArchivos;

    private final ArrayList<Administrador> administradores = new ArrayList<>();
    private final ArrayList<Tecnico> tecnicos = new ArrayList<>();
    private final ArrayList<Cliente> clientes = new ArrayList<>();
    private final ArrayList<Servicio> servicios = new ArrayList<>();
    private final ArrayList<OrdenServicio> ordenes = new ArrayList<>();

    // OOP: COMPOSICIÓN — el gestor TIENE los repositorios
    private final AdministradorRepository administradorRepository = new AdministradorRepository();
    private final ClienteRepository clienteRepository = new ClienteRepository();
    private final TecnicoRepository tecnicoRepository = new TecnicoRepository();
    private final ServicioRepository servicioRepository = new ServicioRepository();
    private final SolicitudRepository solicitudRepository = new SolicitudRepository();
    private final OrdenServicioRepository ordenRepository =
            new OrdenServicioRepository(clienteRepository, tecnicoRepository, servicioRepository, solicitudRepository);

    /** MODO ARCHIVO. */
    public GestorPlataforma() {
        this(true);
    }

    /** Sobrecarga de constructores: true = archivos .txt, false = solo memoria. */
    public GestorPlataforma(boolean usarArchivos) {
        this.usarArchivos = usarArchivos;
        if (usarArchivos) { cargarDatos(); }
    }

    public boolean isUsandoArchivos() { return usarArchivos; }

    // ---------- RNF-01: cargar los datos al iniciar ----------

    private void cargarDatos() {
        administradores.addAll(administradorRepository.getDataList());
        servicios.addAll(servicioRepository.getDataList());
        clientes.addAll(clienteRepository.getDataList());
        tecnicos.addAll(tecnicoRepository.getDataList());
        // Las órdenes se arman con los MISMOS objetos de las listas de arriba.
        ordenes.addAll(ordenRepository.getDataList(clientes, tecnicos, servicios));
        System.out.println("Datos cargados de src/files/: " + administradores.size() + " administrador(es), "
                + tecnicos.size() + " técnico(s), " + clientes.size() + " cliente(s), "
                + servicios.size() + " servicio(s) y " + ordenes.size() + " orden(es).");
    }

    // ---------- Registros (RNF-03: primero se guarda en el archivo; si falla, la lista no cambia) ----------

    public void registrarAdministrador(Administrador administrador) {
        Validar.objeto(administrador, "administrador");
        if (buscarAdministrador(administrador.getDocumento()) != null) {
            throw new IllegalStateException("Ya existe un administrador con el documento " + administrador.getDocumento() + ".");
        }
        if (usarArchivos) { administradorRepository.insertData(administrador); }
        administradores.add(administrador);
    }

    // RF-01: no se permiten dos técnicos con el mismo documento.
    public void registrarTecnico(Tecnico tecnico) {
        Validar.objeto(tecnico, "técnico");
        if (buscarTecnico(tecnico.getDocumento()) != null) {
            throw new IllegalStateException("Ya existe un técnico con el documento " + tecnico.getDocumento() + ".");
        }
        if (usarArchivos) { tecnicoRepository.insertData(tecnico); }
        tecnicos.add(tecnico);
    }

    // RF-02: lo vacío ya lo rechaza Usuario; aquí se evita repetir el documento.
    public void registrarCliente(Cliente cliente) {
        Validar.objeto(cliente, "cliente");
        if (buscarCliente(cliente.getDocumento()) != null) {
            throw new IllegalStateException("Ya existe un cliente con el documento " + cliente.getDocumento() + ".");
        }
        if (usarArchivos) { clienteRepository.insertData(cliente); }
        clientes.add(cliente);
        System.out.println("Cliente " + cliente.getNombre() + " registrado correctamente.");
    }

    public void registrarServicio(Servicio servicio) {
        Validar.objeto(servicio, "servicio");
        if (buscarServicio(servicio.getTipo()) != null) {
            throw new IllegalStateException("Ya existe el servicio " + servicio.getTipo() + ".");
        }
        if (usarArchivos) { servicioRepository.insertData(servicio); }
        servicios.add(servicio);
    }

    public void registrarOrden(OrdenServicio orden) {
        Validar.objeto(orden, "orden");
        if (!clientes.contains(orden.getCliente())) { throw new IllegalStateException("El cliente de la orden no está registrado."); }
        if (buscarOrden(orden.getCodigo()) != null) { throw new IllegalStateException("Ya existe la orden " + orden.getCodigo() + "."); }
        if (usarArchivos) { ordenRepository.insertData(orden); }
        ordenes.add(orden);
        System.out.println("Orden " + orden.getCodigo() + " creada en estado " + orden.getEstado() + ".");
    }

    // ---------- Guardar cambios de objetos que ya existen ----------

    public void guardarCliente(Cliente cliente) {
        if (usarArchivos) { clienteRepository.updateData(cliente, cliente); }
    }

    public void guardarTecnico(Tecnico tecnico) {
        if (usarArchivos) { tecnicoRepository.updateData(tecnico, tecnico); }
    }

    public void guardarAdministrador(Administrador administrador) {
        if (usarArchivos) { administradorRepository.updateData(administrador, administrador); }
    }

    public void guardarOrden(OrdenServicio orden) {
        if (usarArchivos) { ordenRepository.updateData(orden, orden); }
    }

    // ---------- Búsquedas ----------

    public Administrador buscarAdministrador(String documento) {
        for (Administrador a : administradores) { if (a.getDocumento().equals(documento)) { return a; } }
        return null;
    }

    public Tecnico buscarTecnico(String documento) {
        for (Tecnico t : tecnicos) { if (t.getDocumento().equals(documento)) { return t; } }
        return null;
    }

    public Cliente buscarCliente(String documento) {
        for (Cliente c : clientes) { if (c.getDocumento().equals(documento)) { return c; } }
        return null;
    }

    public Servicio buscarServicio(String tipo) {
        if (tipo == null) { return null; }
        for (Servicio s : servicios) { if (s.getTipo().equalsIgnoreCase(tipo.trim())) { return s; } }
        return null;
    }

    public OrdenServicio buscarOrden(String codigo) {
        if (codigo == null) { return null; }
        for (OrdenServicio o : ordenes) { if (o.getCodigo().equalsIgnoreCase(codigo.trim())) { return o; } }
        return null;
    }

    // RF-04: solo técnicos de esa especialidad y zona, con el horario de la solicitud libre.
    public List<Tecnico> buscarTecnicosDisponibles(Solicitud solicitud) {
        List<Tecnico> resultado = new ArrayList<>();
        for (Tecnico t : tecnicos) {
            if (t.puedeAtender(solicitud.getTipoProblema(), solicitud.getUbicacion()) && t.estaDisponible(solicitud.getHorarioDisponible())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    // RF-08 y RNF-02: el cliente solo ve sus propias órdenes.
    public List<OrdenServicio> consultarHistorialCliente(Cliente cliente) {
        Validar.objeto(cliente, "cliente");
        List<OrdenServicio> resultado = new ArrayList<>();
        for (OrdenServicio o : ordenes) {
            if (o.getCliente() == cliente) { resultado.add(o); }
        }
        return resultado;
    }

    // RF-09 y RNF-02: el técnico solo ve sus órdenes no finalizadas.
    public List<OrdenServicio> consultarOrdenesActivasTecnico(Tecnico tecnico) {
        Validar.objeto(tecnico, "técnico");
        List<OrdenServicio> resultado = new ArrayList<>();
        for (OrdenServicio o : ordenes) {
            if (o.getTecnico() == tecnico && o.estaActiva()) { resultado.add(o); }
        }
        return resultado;
    }

    // RF-20: devuelve las órdenes que cumplen TODOS los filtros dados. Un filtro en null no se aplica.
    public List<OrdenServicio> filtrarOrdenes(EstadoOrden estado, LocalDate fecha, Cliente cliente, Tecnico tecnico) {
        List<OrdenServicio> resultado = new ArrayList<>();
        for (OrdenServicio o : ordenes) {
            boolean cumple = (estado == null || o.getEstado() == estado)
                    && (fecha == null || o.getFechaCreacion().toLocalDate().equals(fecha))
                    && (cliente == null || o.getCliente() == cliente)
                    && (tecnico == null || o.getTecnico() == tecnico);
            if (cumple) { resultado.add(o); }
        }
        return resultado;
    }

    public ArrayList<Administrador> getAdministradores() { return new ArrayList<>(administradores); }
    public ArrayList<Tecnico> getTecnicos() { return new ArrayList<>(tecnicos); }
    public ArrayList<Cliente> getClientes() { return new ArrayList<>(clientes); }
    public ArrayList<Servicio> getServicios() { return new ArrayList<>(servicios); }
    public ArrayList<OrdenServicio> getOrdenes() { return new ArrayList<>(ordenes); }
}
