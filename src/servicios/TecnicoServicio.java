package servicios;

import interfaces.IServicio;
import modelos.DetalleTrabajo;
import modelos.DisponibilidadTecnico.FranjaHoraria;
import modelos.OrdenServicio;
import modelos.Tecnico;

import java.util.ArrayList;
import java.util.List;

public class TecnicoServicio implements IServicio<Tecnico> {
    private final GestorPlataforma gestor;

    public TecnicoServicio(GestorPlataforma gestor) {
        this.gestor = gestor;
    }

    @Override
    public ArrayList<Tecnico> getDataList() { return gestor.getTecnicos(); }

    @Override
    public ArrayList<Tecnico> buscarPorNombre(String nombre) {
        ArrayList<Tecnico> resultado = new ArrayList<>();
        for (Tecnico t : gestor.getTecnicos()) {
            if (t.getNombre().toLowerCase().contains(nombre.toLowerCase().trim())) { resultado.add(t); }
        }
        return resultado;
    }

    @Override
    public Tecnico buscarPorId(String documento) { return gestor.buscarTecnico(documento); }

    @Override
    public boolean existeEntidad(String documento) { return buscarPorId(documento) != null; }

    @Override
    public void mostrarTodos() {
        ArrayList<Tecnico> todos = getDataList();
        if (todos.isEmpty()) {
            System.out.println("No hay técnicos registrados.");
            return;
        }
        System.out.println(String.format("%-12s %-22s %-14s %-12s %12s", "Documento", "Nombre", "Especialidad", "Zona", "Tarifa"));
        System.out.println("─".repeat(76));
        for (Tecnico t : todos) { System.out.println(t.aLinea()); }
        System.out.println("Total: " + todos.size() + " técnico(s)");
    }

    public Tecnico iniciarSesion(String documento, String contrasenia) {
        Tecnico tecnico = gestor.buscarTecnico(documento);
        if (tecnico == null) {
            System.out.println("No existe un técnico con el documento " + documento + ".");
            return null;
        }
        return tecnico.iniciarSesion(contrasenia) ? tecnico : null;
    }

    public void cambiarContrasenia(Tecnico tecnico, String actual, String nueva) {
        tecnico.cambiarContrasenia(actual, nueva);
        gestor.guardarTecnico(tecnico);
    }

    public void registrarDisponibilidad(Tecnico tecnico, FranjaHoraria franja) {
        tecnico.registrarDisponibilidad(franja);
        gestor.guardarTecnico(tecnico);
    }

    public boolean registrarIndisponibilidad(Tecnico tecnico, FranjaHoraria franja) {
        boolean registrado = tecnico.registrarIndisponibilidad(franja);
        if (registrado) { gestor.guardarTecnico(tecnico); }
        return registrado;
    }

    public boolean quitarIndisponibilidad(Tecnico tecnico, FranjaHoraria franja) {
        boolean quitado = tecnico.getHorarioDisponible().quitarIndisponibilidad(franja);
        if (quitado) { gestor.guardarTecnico(tecnico); }
        return quitado;
    }

    public List<OrdenServicio> consultarOrdenesActivas(Tecnico tecnico) {
        return gestor.consultarOrdenesActivasTecnico(tecnico);
    }

    // RNF-02: el técnico solo trabaja sobre las órdenes que tiene asignadas.
    public OrdenServicio buscarOrdenAsignada(Tecnico tecnico, String codigo) {
        OrdenServicio orden = gestor.buscarOrden(codigo);
        if (orden == null) { throw new IllegalArgumentException("No existe la orden " + codigo + "."); }
        if (orden.getTecnico() != tecnico) { throw new IllegalArgumentException("La orden " + orden.getCodigo() + " no está asignada a usted."); }
        return orden;
    }

    public void iniciarTrabajo(Tecnico tecnico, String codigo) {
        OrdenServicio orden = buscarOrdenAsignada(tecnico, codigo);
        orden.iniciarTrabajo(tecnico.getNombre());
        gestor.guardarOrden(orden);
    }

    public void registrarTrabajo(Tecnico tecnico, String codigo, DetalleTrabajo detalle) {
        OrdenServicio orden = buscarOrdenAsignada(tecnico, codigo);
        tecnico.registrarTrabajo(orden, detalle);
        gestor.guardarOrden(orden);
    }

    // RF-06, RF-07 y RF-16: EN_PROCESO -> FINALIZADA; devuelve el costo final desglosado.
    public String finalizarOrden(Tecnico tecnico, String codigo) {
        OrdenServicio orden = buscarOrdenAsignada(tecnico, codigo);
        orden.finalizar(tecnico.getNombre());
        gestor.guardarOrden(orden);
        return orden.getDesgloseCosto();
    }
}
