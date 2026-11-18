package ui;

import enums.EstadoOrden;
import modelos.DetalleTrabajo;
import modelos.DisponibilidadTecnico.FranjaHoraria;
import modelos.OrdenServicio;
import modelos.Solicitud;
import modelos.Tecnico;
import servicios.TecnicoServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel del TÉCNICO: inicio con sus próximas visitas, órdenes asignadas, agenda semanal y perfil.
 * RF-06, RF-09, RF-13, RF-15, RF-16 y RNF-02 (solo trabaja sobre sus órdenes).
 */
public class PanelTecnico extends PanelRol {

    private final Tecnico tecnico;
    private final TecnicoServicio servicio;

    public PanelTecnico(VentanaPrincipal ventana, Tecnico tecnico) {
        super(ventana, tecnico);
        this.tecnico = tecnico;
        this.servicio = ventana.getTecnicoServicio();
        iniciar();
    }

    @Override
    protected void registrarSecciones() {
        agregarSeccion("Inicio", "Sus próximas visitas", c -> Iconos.casa(18, c), this::inicio);
        agregarSeccion("Mis órdenes", "Órdenes activas asignadas a usted (RF-09)", c -> Iconos.lista(18, c), this::misOrdenes);
        agregarSeccion("Mi agenda", "Horario de trabajo y bloqueos de indisponibilidad (RF-13)", c -> Iconos.calendario(18, c), this::agenda);
        agregarSeccion("Mi perfil", "Sus datos en la plataforma", c -> Iconos.usuario(18, c), this::perfil);
    }

    @Override
    protected String descripcionUsuario() { return tecnico.getEspecialidad() + " · " + tecnico.getZonaCobertura(); }

    @Override
    protected Color colorRol() { return Iconos.colorServicio(tecnico.getEspecialidad()); }

    // ─── Inicio ───────────────────────────────────────────────────────────────

    private JComponent inicio() {
        List<OrdenServicio> activas = servicio.consultarOrdenesActivas(tecnico);
        int enProceso = 0;
        for (OrdenServicio o : activas) { if (o.getEstado() == EstadoOrden.EN_PROCESO) { enProceso++; } }

        JComponent banner = new PanelCliente.BannerBienvenida("Hola, " + tecnico.getNombre().split(" ")[0],
                activas.isEmpty() ? "No tiene visitas pendientes. Mantenga su agenda al día para recibir órdenes."
                        : "Tiene " + activas.size() + " orden(es) activa(s). ¡Buen trabajo!", "Ver mi agenda", () -> irA("Mi agenda"));
        JComponent estadisticas = filaEstadisticas(
                Componentes.estadistica("Órdenes activas", String.valueOf(activas.size()), Iconos.lista(24, Tema.AZUL_INFO), Tema.AZUL_INFO),
                Componentes.estadistica("Trabajos en proceso", String.valueOf(enProceso), Iconos.herramienta(24, Tema.VIOLETA), Tema.VIOLETA),
                Componentes.estadistica("Tarifa base por hora", Tema.dinero(tecnico.getTarifaBase()), Iconos.dinero(24, Tema.VERDE), Tema.VERDE),
                Componentes.estadistica("Zona de cobertura", tecnico.getZonaCobertura(), Iconos.ubicacion(24, Tema.NARANJA), Tema.NARANJA));

        JComponent visitas;
        if (activas.isEmpty()) {
            visitas = Componentes.vacio("No tiene visitas programadas.", Iconos.calendario(32, Tema.TEXTO_SUAVE));
        } else {
            JPanel rejilla = new JPanel(new GridLayout(0, 2, 14, 14));
            rejilla.setOpaque(false);
            for (OrdenServicio o : activas) { rejilla.add(tarjetaVisita(o)); }
            visitas = rejilla;
        }
        return pila(banner, estadisticas, tarjeta("Próximas visitas", "Toque \"Ver\" para abrir la orden completa", visitas));
    }

    private JComponent tarjetaVisita(OrdenServicio o) {
        Solicitud s = o.getSolicitud();
        PanelTarjeta t = new PanelTarjeta(new BorderLayout(14, 0));
        String tipo = o.getServicio().getTipo();
        t.add(Componentes.cuadroIcono(Iconos.servicio(tipo, 26, Iconos.colorServicio(tipo)), Iconos.colorServicio(tipo), 52), BorderLayout.WEST);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JPanel arriba = Componentes.fila(8, Componentes.titulo(o.getCodigo(), 15), Insignia.de(o.getEstado()));
        arriba.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(arriba);
        textos.add(Box.createVerticalStrut(4));
        for (JLabel l : new JLabel[]{
                etiqueta(Iconos.usuario(14, Tema.TEXTO_SUAVE), o.getCliente().getNombre() + " · " + o.getCliente().getTelefono()),
                etiqueta(Iconos.ubicacion(14, Tema.TEXTO_SUAVE), s.getUbicacion() + " — " + s.getDescripcion()),
                etiqueta(Iconos.reloj(14, Tema.TEXTO_SUAVE), Tema.nombreDia(s.getHorarioDisponible().getDia()) + " "
                        + s.getHorarioDisponible().getHoraInicio() + " – " + s.getHorarioDisponible().getHoraFin())}) {
            textos.add(l);
        }
        t.add(textos, BorderLayout.CENTER);
        Boton ver = new Boton("Ver", Boton.Tipo.SECUNDARIO);
        ver.addActionListener(e -> verDetalle(o));
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        p.add(ver);
        t.add(p, BorderLayout.EAST);
        return t;
    }

    private static JLabel etiqueta(Icon icono, String texto) {
        JLabel l = Componentes.texto(texto);
        l.setIcon(icono);
        l.setIconTextGap(7);
        l.setFont(Tema.normal(13));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    // ─── Mis órdenes (RF-06, RF-09, RF-15, RF-16) ─────────────────────────────

    private JComponent misOrdenes() {
        List<OrdenServicio> activas = servicio.consultarOrdenesActivas(tecnico);
        if (activas.isEmpty()) {
            return tarjeta("Órdenes activas", null, Componentes.vacio("No tiene órdenes activas en este momento.", Iconos.lista(32, Tema.TEXTO_SUAVE)));
        }
        TablaOrdenes tabla = new TablaOrdenes(activas, true, this::verDetalle);
        Boton detalle = new Boton("Ver detalle", Iconos.ojo(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        Boton iniciar = new Boton("Iniciar trabajo", Iconos.play(14, Color.WHITE), Boton.Tipo.AZUL);
        Boton registrar = new Boton("Registrar trabajo", Iconos.herramienta(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        Boton finalizar = new Boton("Finalizar orden", Iconos.check(15, Color.WHITE), Boton.Tipo.EXITO);

        detalle.addActionListener(e -> verDetalle(seleccionada(tabla)));
        iniciar.addActionListener(e -> {
            OrdenServicio o = seleccionada(tabla);
            if (o != null && Acciones.ejecutar(this, "Trabajo iniciado", () -> {
                servicio.iniciarTrabajo(tecnico, o.getCodigo());
                System.out.println("La orden " + o.getCodigo() + " ahora está EN PROCESO.");
                return true;
            })) { refrescar(); }
        });
        registrar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { registrarTrabajo(o); } });
        finalizar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { finalizar(o); } });

        Runnable actualizar = () -> {
            OrdenServicio o = tabla.getSeleccionada();
            detalle.setEnabled(o != null);
            iniciar.setEnabled(o != null && o.getEstado() == EstadoOrden.ASIGNADA);
            registrar.setEnabled(o != null && o.getEstado() == EstadoOrden.EN_PROCESO);
            finalizar.setEnabled(o != null && o.getEstado() == EstadoOrden.EN_PROCESO && o.getDetalle() != null);
        };
        tabla.alCambiarSeleccion(actualizar);
        actualizar.run();
        tabla.seleccionar(0);

        JPanel acciones = Componentes.fila(8, detalle, iniciar, registrar, finalizar);
        acciones.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        cuerpo.add(tabla, BorderLayout.CENTER);
        cuerpo.add(acciones, BorderLayout.SOUTH);

        PanelTarjeta pasos = new PanelTarjeta(new GridLayout(1, 3, 14, 0));
        pasos.add(paso("1", "Iniciar trabajo", "Al llegar a la casa del cliente", Tema.AZUL_INFO));
        pasos.add(paso("2", "Registrar trabajo", "Actividades, horas reales y materiales", Tema.VIOLETA));
        pasos.add(paso("3", "Finalizar", "Se calcula el costo final y la garantía", Tema.VERDE));
        return pila(pasos, tarjeta("Órdenes activas", activas.size() + " orden(es) asignada(s) a usted", cuerpo));
    }

    private static JComponent paso(String numero, String titulo, String texto, Color color) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        JComponent circulo = Componentes.circuloIcono(new Icon() {
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setColor(Color.WHITE);
                g2.setFont(Tema.negrita(16));
                g2.drawString(numero, x + 5, y + 16);
                g2.dispose();
            }
            @Override public int getIconWidth() { return 20; }
            @Override public int getIconHeight() { return 20; }
        }, color, 38);
        p.add(circulo, BorderLayout.WEST);
        JPanel t = new JPanel(new GridLayout(2, 1));
        t.setOpaque(false);
        t.add(Componentes.titulo(titulo, 14.5f));
        t.add(Componentes.texto(texto));
        p.add(t, BorderLayout.CENTER);
        return p;
    }

    // RF-15: actividades, horas reales y materiales.
    private void registrarTrabajo(OrdenServicio o) {
        JTextField descripcion = Componentes.campoTexto("");
        JTextField horas = Componentes.campoTexto("");
        JTextArea actividades = Componentes.areaTexto(4);
        DefaultTableModel materiales = new DefaultTableModel(new Object[]{"Material", "Costo unitario", "Cantidad"}, 0);
        JTable tabla = new JTable(materiales);
        Componentes.estilizarTabla(tabla);
        tabla.setAutoCreateRowSorter(false);
        JScrollPane scroll = Componentes.scrollTabla(tabla);
        scroll.setPreferredSize(new Dimension(460, 150));
        JLabel total = Componentes.titulo("Total materiales: $0", 14);
        materiales.addTableModelListener(e -> total.setText("Total materiales: " + Tema.dinero(sumarMateriales(materiales))));
        Boton agregar = new Boton("Agregar material", Iconos.mas(13, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        agregar.addActionListener(e -> {
            materiales.addRow(new Object[]{"", "0", "1"});
            int fila = materiales.getRowCount() - 1;
            tabla.editCellAt(fila, 0);
            tabla.getEditorComponent().requestFocusInWindow();
        });
        Boton quitar = new Boton("Quitar", Boton.Tipo.PELIGRO);
        quitar.addActionListener(e -> { if (tabla.getSelectedRow() >= 0) { materiales.removeRow(tabla.getSelectedRow()); } });
        JPanel bloqueMateriales = new JPanel(new BorderLayout(0, 8));
        bloqueMateriales.setOpaque(false);
        bloqueMateriales.add(scroll, BorderLayout.CENTER);
        JPanel abajo = new JPanel(new BorderLayout());
        abajo.setOpaque(false);
        abajo.add(Componentes.fila(8, agregar, quitar), BorderLayout.WEST);
        abajo.add(total, BorderLayout.EAST);
        bloqueMateriales.add(abajo, BorderLayout.SOUTH);

        Formulario f = new Formulario(this, "Registrar trabajo realizado", "RF-15 · Orden " + o.getCodigo() + " · "
                + o.getServicio().getTipo(), Iconos.herramienta(22, Color.WHITE), "Guardar trabajo");
        f.campo("Descripción del trabajo", descripcion)
                .campo("Horas reales trabajadas (ej. 2.5)", horas)
                .campo("Actividades realizadas (una por línea)", Componentes.conBorde(actividades))
                .campo("Materiales usados", bloqueMateriales);
        f.mostrar(() -> Acciones.ejecutar(f, "Trabajo registrado", () -> {
            if (tabla.isEditing()) { tabla.getCellEditor().stopCellEditing(); }
            DetalleTrabajo detalle = new DetalleTrabajo();
            detalle.setDescripcion(descripcion.getText());
            detalle.setHorasTrabajo(numero(horas.getText(), "horas reales"));
            for (String a : actividades.getText().split("\\R")) {
                if (!a.trim().isEmpty()) { detalle.agregarActividad(a); }
            }
            for (int i = 0; i < materiales.getRowCount(); i++) {
                String nombre = String.valueOf(materiales.getValueAt(i, 0)).trim();
                if (nombre.isEmpty()) { continue; }
                detalle.registrarMaterial(new DetalleTrabajo.Material(nombre,
                        numero(String.valueOf(materiales.getValueAt(i, 1)), "costo de " + nombre),
                        numero(String.valueOf(materiales.getValueAt(i, 2)), "cantidad de " + nombre)));
            }
            servicio.registrarTrabajo(tecnico, o.getCodigo(), detalle);
            System.out.println("Ya puede finalizar la orden para calcular el costo final.");
            return true;
        }));
        refrescar();
    }

    private static double sumarMateriales(DefaultTableModel m) {
        double total = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            try {
                total += Double.parseDouble(String.valueOf(m.getValueAt(i, 1)).trim()) * Double.parseDouble(String.valueOf(m.getValueAt(i, 2)).trim());
            } catch (NumberFormatException e) {
                // fila incompleta: no suma
            }
        }
        return total;
    }

    static double numero(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo \"" + campo + "\" debe ser un número (por ejemplo 2.5 o 15000).");
        }
    }

    // RF-07 y RF-16: al finalizar se muestra el costo final desglosado.
    private void finalizar(OrdenServicio o) {
        if (!Acciones.confirmar(this, "¿Finalizar la orden " + o.getCodigo() + "?",
                "Se calculará el costo final con las horas y materiales registrados, se creará la garantía y se avisará al cliente.")) { return; }
        Acciones.ejecutar(this, "Orden finalizada", () -> {
            System.out.println(servicio.finalizarOrden(tecnico, o.getCodigo()));
            return true;
        });
        refrescar();
    }

    // ─── Mi agenda (RF-13) ────────────────────────────────────────────────────

    private JComponent agenda() {
        CalendarioSemanal calendario = new CalendarioSemanal(tecnico, servicio.consultarOrdenesActivas(tecnico));
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        JPanel marco = new JPanel(new BorderLayout());
        marco.setBorder(BorderFactory.createLineBorder(Tema.BORDE, 1, true));
        marco.add(calendario);
        cuerpo.add(marco, BorderLayout.CENTER);
        cuerpo.add(CalendarioSemanal.leyenda(), BorderLayout.SOUTH);

        Boton horario = new Boton("Agregar horario", Iconos.mas(14, Color.WHITE), Boton.Tipo.EXITO);
        Boton bloqueo = new Boton("Agregar bloqueo", Iconos.candado(14, Tema.ROJO), Boton.Tipo.PELIGRO);
        Boton quitar = new Boton("Quitar bloqueo", Iconos.equis(13, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        horario.addActionListener(e -> formularioFranja(false));
        bloqueo.addActionListener(e -> formularioFranja(true));
        quitar.addActionListener(e -> quitarBloqueo());
        quitar.setEnabled(!tecnico.getHorarioDisponible().getBloqueosIndisponibles().isEmpty());
        return tarjeta("Agenda semanal", "Solo recibe órdenes dentro de su horario y fuera de sus bloqueos", cuerpo, horario, bloqueo, quitar);
    }

    private void formularioFranja(boolean esBloqueo) {
        SelectorFranja franja = new SelectorFranja(esBloqueo ? 13 : 8, esBloqueo ? 15 : 17);
        Formulario f = new Formulario(this, esBloqueo ? "Bloquear un horario" : "Agregar horario de trabajo",
                "RF-13", esBloqueo ? Iconos.candado(22, Color.WHITE) : Iconos.calendario(22, Color.WHITE),
                esBloqueo ? "Bloquear" : "Agregar");
        f.campo("Franja horaria", franja);
        f.nota(esBloqueo ? "En un bloqueo no se le asignarán órdenes (cita médica, otro trabajo...). No puede cruzarse con otro bloqueo."
                : "Agregue el horario en que trabaja; las órdenes solo se asignan dentro de él.", esBloqueo ? Tema.ROJO : Tema.VERDE);
        f.mostrar(() -> Acciones.ejecutar(f, esBloqueo ? "Bloqueo registrado" : "Horario agregado", () -> {
            FranjaHoraria fr = franja.getFranja();
            if (esBloqueo) { return servicio.registrarIndisponibilidad(tecnico, fr); }
            servicio.registrarDisponibilidad(tecnico, fr);
            return true;
        }));
        refrescar();
    }

    private void quitarBloqueo() {
        List<FranjaHoraria> bloqueos = tecnico.getHorarioDisponible().getBloqueosIndisponibles();
        String[] opciones = new String[bloqueos.size()];
        for (int i = 0; i < opciones.length; i++) {
            FranjaHoraria b = bloqueos.get(i);
            opciones[i] = Tema.nombreDia(b.getDia()) + "  " + b.getHoraInicio() + " – " + b.getHoraFin();
        }
        JComboBox<String> combo = Componentes.combo(opciones);
        Formulario f = new Formulario(this, "Quitar un bloqueo", "RF-13 · Volverá a recibir órdenes en ese horario",
                Iconos.calendario(22, Color.WHITE), "Quitar bloqueo");
        f.campo("Bloqueo", combo);
        f.mostrar(() -> Acciones.ejecutar(f, "Bloqueo eliminado",
                () -> servicio.quitarIndisponibilidad(tecnico, bloqueos.get(combo.getSelectedIndex()))));
        refrescar();
    }

    // ─── Mi perfil ────────────────────────────────────────────────────────────

    private JComponent perfil() {
        JPanel datos = new JPanel(new GridLayout(0, 3, 18, 16));
        datos.setOpaque(false);
        datos.add(Componentes.dato("Nombre", tecnico.getNombre()));
        datos.add(Componentes.dato("Documento", tecnico.getDocumento()));
        datos.add(Componentes.dato("Especialidad", tecnico.getEspecialidad()));
        datos.add(Componentes.dato("Zona de cobertura", tecnico.getZonaCobertura()));
        datos.add(Componentes.dato("Teléfono", tecnico.getTelefono()));
        datos.add(Componentes.dato("Correo", tecnico.getCorreo()));
        datos.add(Componentes.dato("Tarifa base por hora", Tema.dinero(tecnico.getTarifaBase())));
        datos.add(Componentes.dato("Solicitudes recibidas", String.valueOf(tecnico.getSolicitudes().size())));

        JPasswordField actual = Componentes.campoClave();
        JPasswordField nueva = Componentes.campoClave();
        Boton cambiar = new Boton("Cambiar contraseña", Iconos.candado(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        cambiar.addActionListener(e -> {
            if (Acciones.ejecutar(this, "Contraseña actualizada", () -> {
                servicio.cambiarContrasenia(tecnico, new String(actual.getPassword()), new String(nueva.getPassword()));
                return true;
            })) { actual.setText(""); nueva.setText(""); }
        });
        JPanel clave = new JPanel(new GridLayout(1, 3, 18, 0));
        clave.setOpaque(false);
        clave.add(PanelCliente.conEtiqueta("Contraseña actual", actual));
        clave.add(PanelCliente.conEtiqueta("Contraseña nueva", nueva));
        JPanel b = new JPanel(new BorderLayout());
        b.setOpaque(false);
        b.add(cambiar, BorderLayout.SOUTH);
        clave.add(b);
        return pila(tarjeta("Datos del técnico", "Registrados por el administrador (RF-01)", datos),
                tarjeta("Seguridad", "RNF-05 · La contraseña se guarda cifrada", clave));
    }
}
