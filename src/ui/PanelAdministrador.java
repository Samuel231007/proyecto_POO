package ui;

import enums.EstadoOrden;
import enums.EstadoPago;
import modelos.Administrador;
import modelos.Cliente;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Servicio;
import modelos.Tecnico;
import servicios.AdministradorServicio;
import servicios.GestorPlataforma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PanelAdministrador extends PanelRol {
    private final Administrador admin;
    private final AdministradorServicio servicio;
    private final GestorPlataforma gestor;

    // Filtros elegidos en la sección Órdenes (se conservan al refrescar). RF-20
    private EstadoOrden filtroEstado;
    private String filtroFecha = "";
    private Cliente filtroCliente;
    private Tecnico filtroTecnico;

    public PanelAdministrador(VentanaPrincipal ventana, Administrador admin) {
        super(ventana, admin);
        this.admin = admin;
        this.servicio = ventana.getAdministradorServicio();
        this.gestor = ventana.getGestor();
        iniciar();
    }

    @Override
    protected void registrarSecciones() {
        agregarSeccion("Panel general", "Así va la operación de la plataforma", c -> Iconos.casa(18, c), this::panelGeneral);
        agregarSeccion("Órdenes", "Asignar, reasignar, cancelar y filtrar órdenes (RF-04, RF-14, RF-20)", c -> Iconos.lista(18, c), this::ordenes);
        agregarSeccion("Técnicos", "Técnicos registrados y su agenda (RF-01, RF-13)", c -> Iconos.casco(18, c), this::tecnicos);
        agregarSeccion("Clientes", "Clientes registrados en la plataforma", c -> Iconos.usuario(18, c), this::clientes);
        agregarSeccion("Servicios", "Catálogo: tarifa, duración y garantía (RF-05, RF-07)", c -> Iconos.herramienta(18, c), this::servicios);
        agregarSeccion("Pagos", "Confirmar o rechazar los pagos registrados (RF-17)", c -> Iconos.dinero(18, c), this::pagos);
    }

    @Override
    protected String descripcionUsuario() { return "Administrador · " + admin.getAreaResponsable(); }

    @Override
    protected Color colorRol() { return Tema.AZUL_INFO; }

    private JComponent panelGeneral() {
        List<OrdenServicio> todas = gestor.getOrdenes();
        Map<EstadoOrden, Integer> porEstado = new LinkedHashMap<>();
        for (EstadoOrden e : EstadoOrden.values()) { porEstado.put(e, 0); }
        double ingresos = 0, sumaEstrellas = 0;
        int calificadas = 0;
        List<OrdenServicio> pendientes = new ArrayList<>();
        for (OrdenServicio o : todas) {
            porEstado.put(o.getEstado(), porEstado.get(o.getEstado()) + 1);
            for (Pago p : o.getPagos(admin)) { if (p.estaConfirmado()) { ingresos += p.getMonto(); } }
            if (o.getCalificacion() != null) { sumaEstrellas += o.getCalificacion().getEstrellas(); calificadas++; }
            if (o.getEstado() == EstadoOrden.PENDIENTE) { pendientes.add(o); }
        }

        JPanel estados = new JPanel(new GridLayout(1, 5, 14, 0));
        estados.setOpaque(false);
        for (Map.Entry<EstadoOrden, Integer> e : porEstado.entrySet()) {
            estados.add(tarjetaEstado(e.getKey(), e.getValue()));
        }

        JComponent resumen = filaEstadisticas(
                Componentes.estadistica("Ingresos confirmados", Tema.dinero(ingresos), Iconos.dinero(24, Tema.VERDE), Tema.VERDE),
                Componentes.estadistica("Técnicos registrados", String.valueOf(gestor.getTecnicos().size()), Iconos.casco(24, Tema.AZUL_INFO), Tema.AZUL_INFO),
                Componentes.estadistica("Clientes registrados", String.valueOf(gestor.getClientes().size()), Iconos.usuario(24, Tema.NARANJA), Tema.NARANJA),
                Componentes.estadistica("Calificación promedio", calificadas == 0 ? "Sin datos"
                        : String.format(Tema.COLOMBIA, "%.1f / 5", sumaEstrellas / calificadas), Iconos.estrella(24, Tema.AMBAR, true), Tema.AMBAR));

        JComponent porAsignar;
        if (pendientes.isEmpty()) {
            porAsignar = Componentes.vacio("¡Todo al día! No hay órdenes esperando técnico.", Iconos.check(32, Tema.VERDE));
        } else {
            JPanel lista = new JPanel(new GridLayout(0, 1, 0, 10));
            lista.setOpaque(false);
            for (OrdenServicio o : pendientes) { lista.add(filaPendiente(o)); }
            porAsignar = lista;
        }

        JPanel abajo = new JPanel(new BorderLayout(16, 0));
        abajo.setOpaque(false);
        abajo.add(tarjeta("Esperando técnico", pendientes.size() + " orden(es) pendiente(s) · RF-04", porAsignar), BorderLayout.CENTER);
        PanelTarjeta grafico = tarjeta("Órdenes por servicio", "Cantidad de órdenes de cada oficio", new GraficoBarras(contarPorServicio(todas)));
        grafico.setPreferredSize(new Dimension(390, grafico.getPreferredSize().height));
        abajo.add(grafico, BorderLayout.EAST);
        return pila(estados, resumen, abajo);
    }

    private JComponent filaPendiente(OrdenServicio o) {
        String tipo = o.getServicio().getTipo();
        Color color = Iconos.colorServicio(tipo);
        PanelTarjeta t = new PanelTarjeta(new BorderLayout(14, 0)).conFondo(new Color(0xFB, 0xFC, 0xFE), Tema.BORDE);
        t.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));
        t.add(Componentes.cuadroIcono(Iconos.servicio(tipo, 22, color), color, 44), BorderLayout.WEST);
        modelos.Solicitud s = o.getSolicitud();
        JLabel textos = new JLabel("<html><b>" + o.getCodigo() + " · " + Tema.html(tipo) + "</b> — " + Tema.html(o.getCliente().getNombre())
                + "<br><span style='color:#6B7280'>" + Tema.html(s.getUbicacion()) + " · " + Tema.nombreDia(s.getHorarioDisponible().getDia())
                + " " + s.getHorarioDisponible().getHoraInicio() + "–" + s.getHorarioDisponible().getHoraFin() + "</span></html>");
        textos.setFont(Tema.normal(13.5f));
        t.add(textos, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botones.setOpaque(false);
        Boton ver = new Boton("Ver", Boton.Tipo.SECUNDARIO);
        ver.addActionListener(e -> verDetalle(o));
        Boton asignar = new Boton("Asignar", Iconos.casco(15, Color.WHITE), Boton.Tipo.PRIMARIO);
        asignar.addActionListener(e -> asignar(o));
        botones.add(ver);
        botones.add(asignar);
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(botones);
        t.add(centro, BorderLayout.EAST);
        return t;
    }

    private JComponent tarjetaEstado(EstadoOrden estado, int cantidad) {
        Color color = Tema.colorEstado(estado);
        PanelTarjeta t = new PanelTarjeta(new BorderLayout(0, 6));
        t.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        t.add(Componentes.fila(0, Insignia.de(estado)), BorderLayout.NORTH);
        JLabel n = Componentes.titulo(String.valueOf(cantidad), 30);
        n.setForeground(color.darker());
        t.add(n, BorderLayout.CENTER);
        t.add(Componentes.texto(cantidad == 1 ? "orden" : "órdenes"), BorderLayout.SOUTH);
        return t;
    }

    private Map<String, Integer> contarPorServicio(List<OrdenServicio> ordenes) {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (Servicio s : gestor.getServicios()) { conteo.put(s.getTipo(), 0); }
        for (OrdenServicio o : ordenes) { conteo.merge(o.getServicio().getTipo(), 1, Integer::sum); }
        return conteo;
    }

    private static class GraficoBarras extends JComponent {
        private final Map<String, Integer> datos;

        GraficoBarras(Map<String, Integer> datos) {
            this.datos = datos;
            setPreferredSize(new Dimension(380, Math.max(3, datos.size()) * 44));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suavizar(g);
            int maximo = 1;
            for (int v : datos.values()) { maximo = Math.max(maximo, v); }
            int y = 4, anchoEtiqueta = 150, anchoBarra = getWidth() - anchoEtiqueta - 40;
            for (Map.Entry<String, Integer> e : datos.entrySet()) {
                Color color = Iconos.colorServicio(e.getKey());
                Iconos.servicio(e.getKey(), 18, color).paintIcon(this, g2, 0, y + 6);
                g2.setFont(Tema.negrita(13));
                g2.setColor(Tema.TEXTO);
                g2.drawString(e.getKey(), 28, y + 20);
                g2.setColor(new Color(0xF1, 0xF5, 0xF9));
                g2.fill(new RoundRectangle2D.Double(anchoEtiqueta, y + 6, anchoBarra, 18, 9, 9));
                double ancho = anchoBarra * e.getValue() / (double) maximo;
                if (e.getValue() > 0) {
                    g2.setColor(color);
                    g2.fill(new RoundRectangle2D.Double(anchoEtiqueta, y + 6, Math.max(18, ancho), 18, 9, 9));
                }
                g2.setColor(Tema.TEXTO_SUAVE);
                g2.setFont(Tema.negrita(13));
                g2.drawString(String.valueOf(e.getValue()), (int) (anchoEtiqueta + Math.max(18, ancho)) + 8, y + 20);
                y += 44;
            }
            g2.dispose();
        }
    }

    private JComponent ordenes() {
        JComboBox<String> estado = Componentes.combo(opcionesEstado());
        estado.setSelectedIndex(filtroEstado == null ? 0 : filtroEstado.ordinal() + 1);
        JTextField fecha = Componentes.campoTexto(filtroFecha);
        fecha.setColumns(9);
        fecha.setToolTipText("AAAA-MM-DD, por ejemplo " + LocalDate.now());
        List<Cliente> clientes = gestor.getClientes();
        List<Tecnico> tecnicos = gestor.getTecnicos();
        JComboBox<String> cliente = Componentes.combo(nombres(clientes, "Todos los clientes"));
        cliente.setSelectedIndex(filtroCliente == null ? 0 : clientes.indexOf(filtroCliente) + 1);
        JComboBox<String> tecnico = Componentes.combo(nombres(tecnicos, "Todos los técnicos"));
        tecnico.setSelectedIndex(filtroTecnico == null ? 0 : tecnicos.indexOf(filtroTecnico) + 1);
        Boton filtrar = new Boton("Filtrar", Iconos.filtro(14, Color.WHITE), Boton.Tipo.AZUL);
        Boton limpiar = new Boton("Limpiar", Boton.Tipo.SECUNDARIO);
        filtrar.addActionListener(e -> {
            String textoFecha = fecha.getText().trim();
            if (!textoFecha.isEmpty()) {
                try {
                    LocalDate.parse(textoFecha);
                } catch (DateTimeParseException ex) {
                    Acciones.error(this, "Fecha no válida", "Escriba la fecha como AAAA-MM-DD, por ejemplo " + LocalDate.now() + ", o déjela vacía.");
                    return;
                }
            }
            filtroEstado = estado.getSelectedIndex() == 0 ? null : EstadoOrden.values()[estado.getSelectedIndex() - 1];
            filtroFecha = textoFecha;
            filtroCliente = cliente.getSelectedIndex() == 0 ? null : clientes.get(cliente.getSelectedIndex() - 1);
            filtroTecnico = tecnico.getSelectedIndex() == 0 ? null : tecnicos.get(tecnico.getSelectedIndex() - 1);
            refrescar();
        });
        limpiar.addActionListener(e -> {
            filtroEstado = null; filtroFecha = ""; filtroCliente = null; filtroTecnico = null;
            refrescar();
        });
        JPanel filtros = new JPanel(new GridBagLayout());
        filtros.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 10);
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        filtros.add(PanelCliente.conEtiqueta("Estado", estado), c);
        c.weightx = 0.6;
        filtros.add(PanelCliente.conEtiqueta("Fecha de creación", fecha), c);
        c.weightx = 1;
        filtros.add(PanelCliente.conEtiqueta("Cliente", cliente), c);
        filtros.add(PanelCliente.conEtiqueta("Técnico", tecnico), c);
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        filtros.add(filtrar, c);
        c.insets = new Insets(0, 0, 0, 0);
        filtros.add(limpiar, c);

        LocalDate dia = filtroFecha.isEmpty() ? null : LocalDate.parse(filtroFecha);
        List<OrdenServicio> lista = servicio.filtrarOrdenes(filtroEstado, dia, filtroCliente, filtroTecnico);
        JComponent cuerpoTabla;
        JPanel acciones;
        if (lista.isEmpty()) {
            cuerpoTabla = Componentes.vacio("Ninguna orden cumple los filtros elegidos.", Iconos.filtro(32, Tema.TEXTO_SUAVE));
            acciones = new JPanel();
            acciones.setOpaque(false);
        } else {
            TablaOrdenes tabla = new TablaOrdenes(lista, true, this::verDetalle);
            Boton detalle = new Boton("Ver detalle y trazabilidad", Iconos.ojo(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
            Boton asignar = new Boton("Asignar técnico", Iconos.casco(15, Color.WHITE), Boton.Tipo.PRIMARIO);
            Boton reasignar = new Boton("Reasignar", Iconos.reasignar(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
            Boton cancelar = new Boton("Cancelar orden", Iconos.equis(14, Tema.ROJO), Boton.Tipo.PELIGRO);
            detalle.addActionListener(e -> verDetalle(seleccionada(tabla)));
            asignar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { asignar(o); } });
            reasignar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { reasignar(o); } });
            cancelar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { cancelar(o); } });
            Runnable actualizar = () -> {
                OrdenServicio o = tabla.getSeleccionada();
                detalle.setEnabled(o != null);
                asignar.setEnabled(o != null && o.getEstado() == EstadoOrden.PENDIENTE);
                reasignar.setEnabled(o != null && o.getEstado() == EstadoOrden.ASIGNADA);
                cancelar.setEnabled(o != null && (o.getEstado() == EstadoOrden.PENDIENTE || o.getEstado() == EstadoOrden.ASIGNADA));
            };
            tabla.alCambiarSeleccion(actualizar);
            actualizar.run();
            tabla.seleccionar(0);
            cuerpoTabla = tabla;
            acciones = Componentes.fila(8, detalle, asignar, reasignar, cancelar);
            acciones.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        }
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        cuerpo.add(cuerpoTabla, BorderLayout.CENTER);
        cuerpo.add(acciones, BorderLayout.SOUTH);

        boolean hayFiltros = filtroEstado != null || !filtroFecha.isEmpty() || filtroCliente != null || filtroTecnico != null;
        return pila(tarjeta("Filtrar órdenes", "RF-20 · Combine uno o varios filtros", filtros),
                tarjeta(hayFiltros ? "Resultado del filtro" : "Todas las órdenes", lista.size() + " orden(es)", cuerpo));
    }

    private static String[] opcionesEstado() {
        String[] opciones = new String[EstadoOrden.values().length + 1];
        opciones[0] = "Todos los estados";
        for (EstadoOrden e : EstadoOrden.values()) { opciones[e.ordinal() + 1] = Tema.texto(e); }
        return opciones;
    }

    private static String[] nombres(List<? extends modelos.Usuario> usuarios, String primero) {
        String[] r = new String[usuarios.size() + 1];
        r[0] = primero;
        for (int i = 0; i < usuarios.size(); i++) { r[i + 1] = usuarios.get(i).getNombre() + " (" + usuarios.get(i).getDocumento() + ")"; }
        return r;
    }

    private void asignar(OrdenServicio o) {
        List<Tecnico> disponibles = servicio.buscarTecnicosDisponibles(o.getCodigo());
        if (disponibles.isEmpty()) {
            Acciones.mostrar(this, Acciones.Tipo.ADVERTENCIA, "No hay técnicos disponibles",
                    List.of("No hay técnicos de " + o.getServicio().getTipo() + " en " + o.getSolicitud().getUbicacion()
                            + " libres el " + Tema.nombreDia(o.getSolicitud().getHorarioDisponible().getDia()) + " de "
                            + o.getSolicitud().getHorarioDisponible().getHoraInicio() + " a " + o.getSolicitud().getHorarioDisponible().getHoraFin() + ".",
                            "Registre un técnico de esa especialidad y zona, o pida al técnico que revise su agenda (RF-13)."));
            return;
        }
        JComboBox<String> combo = Componentes.combo(opcionesTecnicos(disponibles));
        JTextField materiales = Componentes.campoTexto("0");
        JLabel estimado = Componentes.titulo("", 18);
        estimado.setForeground(Tema.NARANJA_OSCURO);
        Runnable calcular = () -> {
            Tecnico t = disponibles.get(combo.getSelectedIndex());
            double mat;
            try { mat = Double.parseDouble(materiales.getText().trim()); } catch (NumberFormatException ex) { mat = 0; }
            double manoObra = o.getServicio().calcularManoObra(o.getServicio().getDuracionEstimadaHoras(), t.getTarifaBase());
            estimado.setText(Tema.dinero(manoObra + mat) + "   (mano de obra " + Tema.dinero(manoObra) + " + materiales " + Tema.dinero(mat) + ")");
        };
        combo.addActionListener(e -> calcular.run());
        materiales.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcular.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcular.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcular.run(); }
        });
        calcular.run();
        Formulario f = new Formulario(this, "Asignar técnico a " + o.getCodigo(), o.getServicio().getTipo() + " · "
                + o.getSolicitud().getUbicacion() + " · " + Tema.nombreDia(o.getSolicitud().getHorarioDisponible().getDia()) + " "
                + o.getSolicitud().getHorarioDisponible().getHoraInicio(), Iconos.casco(22, Color.WHITE), "Asignar");
        f.nota("Solo aparecen técnicos de " + o.getServicio().getTipo() + " que cubren " + o.getSolicitud().getUbicacion()
                + " y están libres en ese horario (RF-04, RF-13).", Tema.AZUL_INFO)
                .campo("Técnico disponible", combo)
                .campo("Costo estimado de materiales", materiales)
                .campo("Costo estimado para el cliente (RF-05)", estimado);
        f.mostrar(() -> Acciones.ejecutar(f, "Técnico asignado", () -> servicio.asignarTecnico(admin, o.getCodigo(),
                disponibles.get(combo.getSelectedIndex()).getDocumento(), PanelTecnico.numero(materiales.getText(), "materiales"))));
        refrescar();
    }

    private static String[] opcionesTecnicos(List<Tecnico> tecnicos) {
        String[] r = new String[tecnicos.size()];
        for (int i = 0; i < r.length; i++) {
            Tecnico t = tecnicos.get(i);
            r[i] = t.getNombre() + "  ·  " + t.getZonaCobertura() + "  ·  " + Tema.dinero(t.getTarifaBase()) + "/h";
        }
        return r;
    }

    private void reasignar(OrdenServicio o) {
        List<Tecnico> disponibles = new ArrayList<>(servicio.buscarTecnicosDisponibles(o.getCodigo()));
        disponibles.remove(o.getTecnico());
        if (disponibles.isEmpty()) {
            Acciones.info(this, "No hay otro técnico disponible", "Ningún otro técnico de " + o.getServicio().getTipo() + " en "
                    + o.getSolicitud().getUbicacion() + " está libre en ese horario.");
            return;
        }
        JComboBox<String> combo = Componentes.combo(opcionesTecnicos(disponibles));
        Formulario f = new Formulario(this, "Reasignar " + o.getCodigo(), "RF-14 · Técnico actual: " + o.getTecnico().getNombre(),
                Iconos.reasignar(22, Color.WHITE), "Reasignar");
        f.campo("Nuevo técnico", combo)
                .nota("El técnico anterior queda en el historial y usted queda como responsable del cambio (RNF-04). "
                        + "El costo estimado se recalcula con la tarifa del nuevo técnico.", Tema.AZUL_INFO);
        f.mostrar(() -> Acciones.ejecutar(f, "Orden reasignada",
                () -> servicio.reasignarOrden(admin, o.getCodigo(), disponibles.get(combo.getSelectedIndex()).getDocumento())));
        refrescar();
    }

    private void cancelar(OrdenServicio o) {
        JTextArea motivo = Componentes.areaTexto(3);
        Formulario f = new Formulario(this, "Cancelar " + o.getCodigo(), "RF-06 · Solo órdenes pendientes o asignadas",
                Iconos.equis(20, Color.WHITE), "Cancelar orden");
        f.campo("Motivo de la cancelación", Componentes.conBorde(motivo))
                .nota("Se avisará al cliente " + o.getCliente().getNombre() + " por " + o.getCliente().getCanalNotificacion().getNombreCanal() + ".", Tema.AMBAR);
        f.mostrar(() -> Acciones.ejecutar(f, "Orden cancelada", () -> {
            servicio.cancelarOrden(admin, o.getCodigo(), motivo.getText());
            return true;
        }));
        refrescar();
    }

    private JComponent tecnicos() {
        List<Tecnico> lista = gestor.getTecnicos();
        DefaultTableModel modelo = modeloSoloLectura("Documento", "Nombre", "Especialidad", "Zona", "Tarifa / hora", "Órdenes activas", "Calificación");
        for (Tecnico t : lista) {
            double suma = 0;
            int n = 0;
            for (OrdenServicio o : gestor.getOrdenes()) {
                if (o.getTecnico() == t && o.getCalificacion() != null) { suma += o.getCalificacion().getEstrellas(); n++; }
            }
            modelo.addRow(new Object[]{t.getDocumento(), t.getNombre(), t.getEspecialidad(), t.getZonaCobertura(),
                    Tema.dinero(t.getTarifaBase()), gestor.consultarOrdenesActivasTecnico(t).size(),
                    n == 0 ? "—" : String.format(Tema.COLOMBIA, "★ %.1f", suma / n)});
        }
        JTable tabla = new JTable(modelo);
        Componentes.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) t.getDefaultRenderer(String.class).getTableCellRendererComponent(t, v, s, f, r, c);
                l.setIcon(Iconos.servicio(String.valueOf(v), 17, Iconos.colorServicio(String.valueOf(v))));
                l.setIconTextGap(8);
                return l;
            }
        });
        if (!lista.isEmpty()) { tabla.setRowSelectionInterval(0, 0); }
        JScrollPane scroll = Componentes.scrollTabla(tabla);
        scroll.setPreferredSize(new Dimension(600, Math.min(lista.size() + 1, 10) * 40 + 4));

        Boton registrar = new Boton("Registrar técnico", Iconos.mas(14, Color.WHITE), Boton.Tipo.PRIMARIO);
        registrar.addActionListener(e -> registrarTecnico());
        Boton agenda = new Boton("Ver agenda", Iconos.calendario(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        agenda.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { Acciones.info(this, "Seleccione un técnico", "Haga clic en una fila de la tabla."); return; }
            verAgenda(lista.get(tabla.convertRowIndexToModel(fila)));
        });
        return tarjeta("Técnicos registrados", lista.size() + " técnico(s)", scroll, agenda, registrar);
    }

    private void verAgenda(Tecnico t) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this), "Agenda de " + t.getNombre(), Dialog.ModalityType.APPLICATION_MODAL);
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        p.add(Componentes.encabezado("Agenda de " + t.getNombre(), t.getEspecialidad() + " · " + t.getZonaCobertura() + " · RF-13"), BorderLayout.NORTH);
        JPanel marco = new JPanel(new BorderLayout());
        marco.setBorder(BorderFactory.createLineBorder(Tema.BORDE, 1, true));
        marco.add(new CalendarioSemanal(t, gestor.consultarOrdenesActivasTecnico(t)));
        p.add(marco, BorderLayout.CENTER);
        p.add(CalendarioSemanal.leyenda(), BorderLayout.SOUTH);
        d.setContentPane(p);
        d.setSize(960, 640);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void registrarTecnico() {
        JTextField nombre = Componentes.campoTexto("");
        JTextField documento = Componentes.campoTexto("");
        JTextField correo = Componentes.campoTexto("");
        JTextField telefono = Componentes.campoTexto("");
        JComboBox<String> especialidad = Componentes.combo(tiposServicio());
        JTextField zona = Componentes.campoTexto("");
        JTextField tarifa = Componentes.campoTexto("");
        JPasswordField clave = Componentes.campoClave();
        Formulario f = new Formulario(this, "Registrar técnico", "RF-01 · No se permiten documentos repetidos",
                Iconos.casco(22, Color.WHITE), "Registrar");
        f.campo("Nombre completo", nombre).campo("Documento", documento).campo("Correo", correo).campo("Teléfono", telefono)
                .campo("Especialidad", especialidad).campo("Zona de cobertura (municipio)", zona)
                .campo("Tarifa base por hora", tarifa).campo("Contraseña inicial", clave)
                .nota("Después de registrarlo, el técnico debe ingresar y agregar su horario en \"Mi agenda\" para recibir órdenes (RF-13).", Tema.AZUL_INFO);
        f.mostrar(() -> Acciones.ejecutar(f, "Técnico registrado", () -> {
            servicio.registrarTecnico(admin, new Tecnico(nombre.getText(), documento.getText(), correo.getText(), telefono.getText(),
                    new String(clave.getPassword()), (String) especialidad.getSelectedItem(), zona.getText(),
                    PanelTecnico.numero(tarifa.getText(), "tarifa base")));
            return true;
        }));
        refrescar();
    }

    private String[] tiposServicio() {
        List<Servicio> servicios = gestor.getServicios();
        String[] r = new String[servicios.size()];
        for (int i = 0; i < r.length; i++) { r[i] = servicios.get(i).getTipo(); }
        return r;
    }

    private JComponent clientes() {
        List<Cliente> lista = gestor.getClientes();
        DefaultTableModel modelo = modeloSoloLectura("Documento", "Nombre", "Celular", "Correo", "Ubicación", "Avisos por", "Órdenes");
        for (Cliente cl : lista) {
            modelo.addRow(new Object[]{cl.getDocumento(), cl.getNombre(), cl.getTelefono(), cl.getCorreo(), cl.getUbicacion(),
                    cl.getCanalNotificacion().getNombreCanal(), gestor.consultarHistorialCliente(cl).size()});
        }
        JTable tabla = new JTable(modelo);
        Componentes.estilizarTabla(tabla);
        if (!lista.isEmpty()) { tabla.setRowSelectionInterval(0, 0); }
        JScrollPane scroll = Componentes.scrollTabla(tabla);
        scroll.setPreferredSize(new Dimension(600, Math.min(lista.size() + 1, 10) * 40 + 4));
        Boton historial = new Boton("Ver sus órdenes", Iconos.lista(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        historial.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) { Acciones.info(this, "Seleccione un cliente", "Haga clic en una fila de la tabla."); return; }
            filtroCliente = lista.get(tabla.convertRowIndexToModel(fila));
            filtroEstado = null; filtroFecha = ""; filtroTecnico = null;
            irA("Órdenes");
        });
        return tarjeta("Clientes registrados", lista.size() + " cliente(s) · RF-02", scroll, historial);
    }

    private JComponent servicios() {
        JPanel rejilla = new JPanel(new GridLayout(0, 3, 16, 16));
        rejilla.setOpaque(false);
        for (Servicio s : gestor.getServicios()) {
            Color color = Iconos.colorServicio(s.getTipo());
            PanelTarjeta t = new PanelTarjeta(new BorderLayout(14, 0));
            t.add(Componentes.cuadroIcono(Iconos.servicio(s.getTipo(), 26, color), color, 52), BorderLayout.WEST);
            JPanel textos = new JPanel(new GridLayout(4, 1, 0, 2));
            textos.setOpaque(false);
            textos.add(Componentes.titulo(s.getTipo(), 16));
            textos.add(Componentes.texto("Factor de tarifa: × " + s.getFactorTarifa()));
            textos.add(Componentes.texto("Duración estimada: " + s.getDuracionEstimadaHoras() + " h"));
            textos.add(Componentes.texto("Garantía: " + s.getDiasGarantia() + " días"));
            t.add(textos, BorderLayout.CENTER);
            rejilla.add(t);
        }
        Boton agregar = new Boton("Agregar servicio", Iconos.mas(14, Color.WHITE), Boton.Tipo.PRIMARIO);
        agregar.addActionListener(e -> agregarServicio());
        JComponent nota = new JLabel("<html>Costo de mano de obra = horas × tarifa base del técnico × factor del servicio. "
                + "Ej.: 3 h × $60.000 × 1,2 = $216.000 (RF-05).</html>");
        nota.setFont(Tema.normal(13));
        nota.setForeground(Tema.TEXTO_SUAVE);
        JPanel cuerpo = new JPanel(new BorderLayout(0, 14));
        cuerpo.setOpaque(false);
        cuerpo.add(rejilla, BorderLayout.CENTER);
        cuerpo.add(nota, BorderLayout.SOUTH);
        return tarjeta("Catálogo de servicios", gestor.getServicios().size() + " tipo(s) de servicio", cuerpo, agregar);
    }

    private void agregarServicio() {
        JTextField tipo = Componentes.campoTexto("");
        JTextField factor = Componentes.campoTexto("1.0");
        JTextField horas = Componentes.campoTexto("2");
        JTextField dias = Componentes.campoTexto("30");
        Formulario f = new Formulario(this, "Agregar tipo de servicio", "RF-05 y RF-07", Iconos.herramienta(22, Color.WHITE), "Agregar");
        f.campo("Nombre del servicio (ej. Jardinería)", tipo)
                .campo("Factor de tarifa (1.0 = normal, 1.5 = 50% más)", factor)
                .campo("Duración estimada en horas", horas)
                .campo("Días de garantía", dias);
        f.mostrar(() -> Acciones.ejecutar(f, "Servicio agregado", () -> {
            int d;
            try { d = Integer.parseInt(dias.getText().trim()); } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Los días de garantía deben ser un número entero.");
            }
            servicio.registrarServicio(new Servicio(tipo.getText(), PanelTecnico.numero(factor.getText(), "factor"),
                    PanelTecnico.numero(horas.getText(), "duración"), d));
            return true;
        }));
        refrescar();
    }

    private static final class FilaPago {
        final OrdenServicio orden;
        final Pago pago;
        final int numero;

        FilaPago(OrdenServicio orden, Pago pago, int numero) {
            this.orden = orden;
            this.pago = pago;
            this.numero = numero;
        }
    }

    private JComponent pagos() {
        List<FilaPago> filas = new ArrayList<>();
        double pendiente = 0, confirmado = 0;
        for (OrdenServicio o : gestor.getOrdenes()) {
            List<Pago> pagos = o.getPagos(admin);
            for (int i = 0; i < pagos.size(); i++) {
                filas.add(new FilaPago(o, pagos.get(i), i + 1));
                if (pagos.get(i).estaPendiente()) { pendiente += pagos.get(i).getMonto(); }
                if (pagos.get(i).estaConfirmado()) { confirmado += pagos.get(i).getMonto(); }
            }
        }
        JComponent resumen = filaEstadisticas(
                Componentes.estadistica("Por revisar", Tema.dinero(pendiente), Iconos.reloj(24, Tema.AMBAR), Tema.AMBAR),
                Componentes.estadistica("Confirmado", Tema.dinero(confirmado), Iconos.check(24, Tema.VERDE), Tema.VERDE),
                Componentes.estadistica("Pagos registrados", String.valueOf(filas.size()), Iconos.dinero(24, Tema.AZUL_INFO), Tema.AZUL_INFO));
        if (filas.isEmpty()) {
            return pila(resumen, tarjeta("Pagos", null, Componentes.vacio("No hay pagos registrados.", Iconos.dinero(32, Tema.TEXTO_SUAVE))));
        }

        DefaultTableModel modelo = modeloSoloLectura("Orden", "Cliente", "Método", "Valor", "Fecha", "Referencia", "Estado");
        for (FilaPago f : filas) {
            modelo.addRow(new Object[]{f.orden.getCodigo(), f.orden.getCliente().getNombre(), f.pago.getMetodoPago(),
                    Tema.dinero(f.pago.getMonto()), f.pago.getFecha().toString(),
                    f.pago.getReferencia().isEmpty() ? "—" : f.pago.getReferencia(), f.pago.getEstado()});
        }
        JTable tabla = new JTable(modelo);
        Componentes.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(6).setCellRenderer((t, v, s, foco, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 9));
            p.setBackground(s ? t.getSelectionBackground() : Color.WHITE);
            p.add(Insignia.de((EstadoPago) v));
            return p;
        });
        JScrollPane scroll = Componentes.scrollTabla(tabla);
        scroll.setPreferredSize(new Dimension(600, Math.min(filas.size() + 1, 10) * 40 + 4));

        Boton confirmar = new Boton("Confirmar pago", Iconos.check(15, Color.WHITE), Boton.Tipo.EXITO);
        Boton rechazar = new Boton("Rechazar", Iconos.equis(13, Tema.ROJO), Boton.Tipo.PELIGRO);
        Runnable actualizar = () -> {
            int fila = tabla.getSelectedRow();
            boolean pend = fila >= 0 && filas.get(tabla.convertRowIndexToModel(fila)).pago.estaPendiente();
            confirmar.setEnabled(pend);
            rechazar.setEnabled(pend);
        };
        tabla.getSelectionModel().addListSelectionListener(e -> actualizar.run());
        confirmar.addActionListener(e -> revisarPago(tabla, filas, true));
        rechazar.addActionListener(e -> revisarPago(tabla, filas, false));
        int primero = 0;
        for (int i = 0; i < filas.size(); i++) { if (filas.get(i).pago.estaPendiente()) { primero = i; break; } }
        tabla.setRowSelectionInterval(primero, primero);
        actualizar.run();

        JPanel cuerpo = new JPanel(new BorderLayout(0, 14));
        cuerpo.setOpaque(false);
        cuerpo.add(scroll, BorderLayout.CENTER);
        cuerpo.add(Componentes.fila(8, confirmar, rechazar), BorderLayout.SOUTH);
        return pila(resumen, tarjeta("Pagos registrados", "RNF-05 · De las tarjetas solo se guarda la referencia de autorización", cuerpo));
    }

    private void revisarPago(JTable tabla, List<FilaPago> filas, boolean confirmar) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { return; }
        FilaPago f = filas.get(tabla.convertRowIndexToModel(fila));
        Acciones.ejecutar(this, confirmar ? "Pago confirmado" : "Pago rechazado", () -> {
            if (confirmar) { servicio.confirmarPago(admin, f.orden.getCodigo(), f.numero); }
            else { servicio.rechazarPago(admin, f.orden.getCodigo(), f.numero); }
            System.out.println("Saldo pendiente de la orden " + f.orden.getCodigo() + ": " + Tema.dinero(f.orden.getSaldoPendiente()));
            return true;
        });
        refrescar();
    }

    private static DefaultTableModel modeloSoloLectura(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }
}
