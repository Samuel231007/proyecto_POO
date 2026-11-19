package repositorios;

import enums.EstadoOrden;
import interfaces.IRepositorioDependiente;
import interfaces.IRepository;
import modelos.Cliente;
import modelos.OrdenServicio;
import modelos.Servicio;
import modelos.Solicitud;
import modelos.Tecnico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Formato: codigo|documentoCliente|idSolicitud|tipoServicio|documentoTecnico|estado|fechaCreacion|
//          costoMaterialesEstimado|costoEstimado|costoManoObraFinal|costoMaterialesFinal|costoFinal
// Las partes de la orden (pagos, cambios, avisos...) se guardan en sus propios archivos.
public class OrdenServicioRepository extends BaseRepository implements IRepository<OrdenServicio> {
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ServicioRepository servicioRepository;
    private final SolicitudRepository solicitudRepository;
    private final AsignacionRepository asignacionRepository;
    private final ArrayList<IRepositorioDependiente<OrdenServicio>> partes = new ArrayList<>();

    public OrdenServicioRepository(ClienteRepository clienteRepository, TecnicoRepository tecnicoRepository,
                                   ServicioRepository servicioRepository, SolicitudRepository solicitudRepository) {
        super("ordenes.txt", "|");
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.servicioRepository = servicioRepository;
        this.solicitudRepository = solicitudRepository;
        this.asignacionRepository = new AsignacionRepository();

        partes.add(new CambioEstadoRepository());
        partes.add(asignacionRepository);
        partes.add(new DetalleTrabajoRepository());
        partes.add(new GarantiaRepository());
        partes.add(new CalificacionRepository());
        partes.add(new PagoRepository());
        partes.add(new NotificacionRepository());
    }

    @Override
    public ArrayList<OrdenServicio> getDataList() {
        return getDataList(clienteRepository.getDataList(), tecnicoRepository.getDataList(), servicioRepository.getDataList());
    }

    // Sobrecarga: usa los objetos que ya están en memoria, para que la orden apunte al mismo Cliente y Tecnico del gestor.
    public ArrayList<OrdenServicio> getDataList(List<Cliente> clientes, List<Tecnico> tecnicos, List<Servicio> servicios) {
        ArrayList<OrdenServicio> ordenes = new ArrayList<>();
        ArrayList<Solicitud> solicitudes = solicitudRepository.getDataList();
        asignacionRepository.usarTecnicos(tecnicos);

        for (String linea : fileManage.getDataOfFile()) {
            String[] c = separar(linea);
            String codigo = c[0];
            Cliente cliente = buscarCliente(clientes, c[1]);
            Solicitud solicitud = buscarSolicitud(solicitudes, Integer.parseInt(c[2]));
            Servicio servicio = buscarServicio(servicios, c[3]);
            Tecnico tecnico = buscarTecnico(tecnicos, vacioANull(c[4]));

            if (cliente == null || solicitud == null || servicio == null) {
                System.out.println("Aviso: se omitió la orden " + codigo + " porque falta su cliente, solicitud o servicio en los archivos.");
                continue;
            }
            OrdenServicio orden = new OrdenServicio(codigo, cliente, solicitud, servicio, LocalDateTime.parse(c[6]),
                    tecnico, EstadoOrden.valueOf(c[5]), Double.parseDouble(c[7]), Double.parseDouble(c[8]),
                    Double.parseDouble(c[9]), Double.parseDouble(c[10]), Double.parseDouble(c[11]));

            // Cada repositorio dependiente carga su parte: cambios, asignaciones, pagos, avisos...
            for (IRepositorioDependiente<OrdenServicio> parte : partes) {
                parte.cargarEn(orden);
            }
            ordenes.add(orden);
        }
        return ordenes;
    }

    @Override
    public void insertData(OrdenServicio orden) {
        guardar(orden);
    }

    @Override
    public void updateData(OrdenServicio oldOrden, OrdenServicio newOrden) {
        guardar(newOrden);
    }

    @Override
    public void deleteData(OrdenServicio orden) {
        eliminarPorClave(orden.getCodigo());
        solicitudRepository.deleteData(orden.getSolicitud());
        for (IRepositorioDependiente<OrdenServicio> parte : partes) {
            parte.eliminarDe(orden.getCodigo());
        }
    }

    @Override
    public String getDataLine(OrdenServicio o) {
        return unir(o.getCodigo(), o.getDocumentoCliente(), o.getSolicitud().getIdSolicitud(), o.getServicio().getTipo(),
                o.getDocumentoTecnico(), o.getEstado().name(), o.getFechaCreacion(),
                o.getCostoMaterialesEstimado(), o.getCostoEstimado(),
                o.getCostoManoObraFinal(), o.getCostoMaterialesFinal(), o.getCostoFinal());
    }

    public void guardar(OrdenServicio orden) {
        solicitudRepository.updateData(orden.getSolicitud(), orden.getSolicitud());
        guardarLinea(orden.getCodigo(), getDataLine(orden));
        for (IRepositorioDependiente<OrdenServicio> parte : partes) {
            parte.guardarDe(orden);
        }
    }

    private Cliente buscarCliente(List<Cliente> clientes, String documento) {
        for (Cliente c : clientes) { if (c.getDocumento().equals(documento)) { return c; } }
        return null;
    }

    private Tecnico buscarTecnico(List<Tecnico> tecnicos, String documento) {
        if (documento == null) { return null; }
        for (Tecnico t : tecnicos) { if (t.getDocumento().equals(documento)) { return t; } }
        return null;
    }

    private Servicio buscarServicio(List<Servicio> servicios, String tipo) {
        for (Servicio s : servicios) { if (s.getTipo().equalsIgnoreCase(tipo)) { return s; } }
        return null;
    }

    private Solicitud buscarSolicitud(List<Solicitud> solicitudes, int id) {
        for (Solicitud s : solicitudes) { if (s.getIdSolicitud() == id) { return s; } }
        return null;
    }
}
