package ui;

import enums.EstadoOrden;
import modelos.DetalleTrabajo;
import modelos.Garantia;
import modelos.Notificacion;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Solicitud;
import modelos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DialogoDetalleOrden extends JDialog {
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DialogoDetalleOrden(Component padre, OrdenServicio orden, Usuario quienConsulta) {
        super(SwingUtilities.getWindowAncestor(padre), "Orden " + orden.getCodigo(), ModalityType.APPLICATION_MODAL);

        JPanel contenido = new PanelDesplazable(null);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(Tema.FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 22, 22, 22));

        contenido.add(alinear(encabezado(orden)));
        contenido.add(Box.createVerticalStrut(16));

        JPanel columnas = new JPanel(new GridLayout(1, 2, 16, 0));
        columnas.setOpaque(false);
        JPanel izquierda = columna();
        JPanel derecha = columna();
        izquierda.add(alinear(tarjetaSolicitud(orden)));
        izquierda.add(Box.createVerticalStrut(14));
        izquierda.add(alinear(tarjetaCostos(orden)));
        if (orden.getDetalle() != null) {
            izquierda.add(Box.createVerticalStrut(14));
            izquierda.add(alinear(tarjetaTrabajo(orden.getDetalle())));
        }
        derecha.add(alinear(tarjetaLineaTiempo(orden)));
        if (orden.getGarantia() != null || orden.getCalificacion() != null) {
            derecha.add(Box.createVerticalStrut(14));
            derecha.add(alinear(tarjetaGarantia(orden)));
        }
        JComponent pagos = tarjetaPagos(orden, quienConsulta);
        if (pagos != null) {
            derecha.add(Box.createVerticalStrut(14));
            derecha.add(alinear(pagos));
        }
        izquierda.add(Box.createVerticalGlue());
        derecha.add(Box.createVerticalGlue());
        columnas.add(izquierda);
        columnas.add(derecha);
        contenido.add(alinear(columnas));
        contenido.add(Box.createVerticalStrut(14));
        contenido.add(alinear(tarjetaAvisos(orden)));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        getContentPane().add(scroll);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 22, 12));
        pie.setBackground(Color.WHITE);
        pie.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDE));
        Boton cerrar = new Boton("Cerrar", Boton.Tipo.AZUL);
        cerrar.addActionListener(e -> dispose());
        pie.add(cerrar);
        getContentPane().add(pie, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(cerrar);

        setSize(1000, 760);
        setLocationRelativeTo(getOwner());
    }

    private static JPanel columna() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    private static JComponent alinear(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private JComponent encabezado(OrdenServicio o) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout(16, 0));
        String tipo = o.getServicio().getTipo();
        t.add(Componentes.cuadroIcono(Iconos.servicio(tipo, 30, Iconos.colorServicio(tipo)), Iconos.colorServicio(tipo), 60), BorderLayout.WEST);
        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 4));
        textos.setOpaque(false);
        JPanel arriba = Componentes.fila(10, Componentes.titulo("Orden " + o.getCodigo() + "  ·  " + tipo, 21), Insignia.de(o.getEstado()));
        textos.add(arriba);
        textos.add(Componentes.texto("Cliente: " + o.getCliente().getNombre() + "   ·   Creada el "
                + o.getFechaCreacion().format(FECHA_HORA) + "   ·   Solicitud #" + o.getSolicitud().getIdSolicitud()));
        t.add(textos, BorderLayout.CENTER);
        return t;
    }

    private JComponent tarjetaSolicitud(OrdenServicio o) {
        Solicitud s = o.getSolicitud();
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        t.add(Componentes.encabezado("Solicitud del cliente", null), BorderLayout.NORTH);
        JPanel datos = new JPanel(new GridLayout(0, 2, 14, 12));
        datos.setOpaque(false);
        datos.add(Componentes.dato("Problema", s.getDescripcion()));
        datos.add(Componentes.dato("Ubicación", s.getUbicacion()));
        datos.add(Componentes.dato("Horario de visita", Tema.nombreDia(s.getHorarioDisponible().getDia()) + " "
                + s.getHorarioDisponible().getHoraInicio() + " – " + s.getHorarioDisponible().getHoraFin()));
        datos.add(Componentes.dato("Técnico", o.getTecnico() == null ? "Sin asignar"
                : o.getTecnico().getNombre() + " (" + o.getTecnico().getTelefono() + ")"));
        if (o.getEstado() == EstadoOrden.CANCELADA) {
            datos.add(Componentes.dato("Motivo de cancelación", s.getMotivoCancelacion()));
        }
        t.add(datos, BorderLayout.CENTER);
        return t;
    }

    private JComponent tarjetaCostos(OrdenServicio o) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        t.add(Componentes.encabezado("Costos", "RF-05 estimado · RF-16 final"), BorderLayout.NORTH);
        JPanel filas = new JPanel(new GridLayout(0, 2, 8, 8));
        filas.setOpaque(false);
        if (o.getCostoEstimado() > 0) {
            agregarFila(filas, "Costo estimado", Tema.dinero(o.getCostoEstimado()), false);
        }
        if (o.getCostoFinal() > 0) {
            agregarFila(filas, "Mano de obra", Tema.dinero(o.getCostoManoObraFinal()), false);
            agregarFila(filas, "Materiales", Tema.dinero(o.getCostoMaterialesFinal()), false);
            agregarFila(filas, "Costo final", Tema.dinero(o.getCostoFinal()), true);
            if (o.getCostoEstimado() > 0) {
                double dif = o.compararConEstimado();
                agregarFila(filas, "Diferencia con lo estimado", (dif >= 0 ? "+" : "−") + Tema.dinero(Math.abs(dif))
                        + (dif > 0 ? " (más caro)" : dif < 0 ? " (más barato)" : ""), false);
            }
        }
        if (o.getEstado() != EstadoOrden.CANCELADA && (o.getCostoFinal() > 0 || o.getCostoEstimado() > 0)) {
            agregarFila(filas, "Saldo pendiente", Tema.dinero(o.getSaldoPendiente()), true);
        }
        if (filas.getComponentCount() == 0) {
            t.add(Componentes.texto("El costo se calcula cuando se asigna un técnico."), BorderLayout.CENTER);
        } else {
            t.add(filas, BorderLayout.CENTER);
        }
        return t;
    }

    private static void agregarFila(JPanel filas, String etiqueta, String valor, boolean destacado) {
        JLabel e = Componentes.texto(etiqueta);
        JLabel v = new JLabel(valor, SwingConstants.RIGHT);
        v.setFont(destacado ? Tema.negrita(15) : Tema.normal(14));
        v.setForeground(destacado ? Tema.NARANJA_OSCURO : Tema.TEXTO);
        filas.add(e);
        filas.add(v);
    }

    private JComponent tarjetaTrabajo(DetalleTrabajo d) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        t.add(Componentes.encabezado("Trabajo realizado", d.getDescripcion() + " · " + d.getHorasTrabajo() + " h"), BorderLayout.NORTH);
        StringBuilder html = new StringBuilder("<html><body style='width:380px'><b>Actividades</b><ul style='margin-left:16px'>");
        for (String a : d.getActividades()) { html.append("<li>").append(Tema.html(a)).append("</li>"); }
        html.append("</ul><b>Materiales</b><ul style='margin-left:16px'>");
        for (DetalleTrabajo.Material m : d.getMateriales()) {
            html.append("<li>").append(Tema.html(m.getDescripcion())).append(" × ").append(m.getCantidad())
                    .append(" — ").append(Tema.dinero(m.calcularSubtotal())).append("</li>");
        }
        if (d.getMateriales().isEmpty()) { html.append("<li>Sin materiales</li>"); }
        JLabel l = new JLabel(html.append("</ul></body></html>").toString());
        l.setFont(Tema.normal(13));
        l.setForeground(Tema.TEXTO);
        t.add(l, BorderLayout.CENTER);
        return t;
    }

    private JComponent tarjetaGarantia(OrdenServicio o) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        JPanel cuerpo = new JPanel();
        cuerpo.setOpaque(false);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        Garantia g = o.getGarantia();
        if (g != null) {
            boolean vigente = g.estaVigente(LocalDate.now());
            cuerpo.add(alinear(Componentes.fila(10, Componentes.titulo("Garantía", 16),
                    new Insignia(vigente ? "Vigente" : "Vencida", vigente ? Tema.VERDE : Tema.ROJO))));
            cuerpo.add(Box.createVerticalStrut(6));
            cuerpo.add(alinear(Componentes.texto(g.getCobertura() + " · hasta el " + g.getFechaFin() + " (" + g.getDiasVigencia() + " días)")));
            for (String r : g.getRevisiones()) {
                cuerpo.add(Box.createVerticalStrut(4));
                JLabel l = Componentes.texto("Revisión solicitada: " + r);
                l.setIcon(Iconos.herramienta(14, Tema.AMBAR));
                cuerpo.add(alinear(l));
            }
        }
        if (o.getCalificacion() != null) {
            cuerpo.add(Box.createVerticalStrut(14));
            cuerpo.add(alinear(Componentes.titulo("Calificación del cliente", 15)));
            cuerpo.add(Box.createVerticalStrut(4));
            cuerpo.add(alinear(new SelectorEstrellas(o.getCalificacion().getEstrellas(), false, 20)));
            if (!o.getCalificacion().getComentario().isEmpty()) {
                cuerpo.add(alinear(Componentes.texto("“" + o.getCalificacion().getComentario() + "”")));
            }
        }
        t.add(cuerpo, BorderLayout.CENTER);
        return t;
    }

    private JComponent tarjetaPagos(OrdenServicio o, Usuario quien) {
        List<Pago> pagos;
        try {
            pagos = o.getPagos(quien);
        } catch (SecurityException e) {
            return null;   // RNF-05: este usuario no puede ver los pagos
        }
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        t.add(Componentes.encabezado("Pagos", "RF-17"), BorderLayout.NORTH);
        JPanel lista = new JPanel(new GridLayout(0, 1, 0, 8));
        lista.setOpaque(false);
        if (pagos.isEmpty()) { lista.add(Componentes.texto("Aún no hay pagos registrados.")); }
        for (Pago p : pagos) {
            JPanel fila = new JPanel(new BorderLayout(10, 0));
            fila.setOpaque(false);
            JLabel l = new JLabel("<html><b>" + p.getMetodoPago() + "</b> · " + Tema.dinero(p.getMonto())
                    + "<br><span style='color:#6B7280'>" + p.getFecha() + " · " + Tema.html(p.getDetalle()) + "</span></html>");
            l.setFont(Tema.normal(13));
            l.setIcon(Iconos.dinero(20, Tema.VERDE));
            l.setIconTextGap(10);
            fila.add(l, BorderLayout.CENTER);
            JPanel insignia = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
            insignia.setOpaque(false);
            insignia.add(Insignia.de(p.getEstado()));
            fila.add(insignia, BorderLayout.EAST);
            lista.add(fila);
        }
        t.add(lista, BorderLayout.CENTER);
        return t;
    }

    private JComponent tarjetaLineaTiempo(OrdenServicio o) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        t.add(Componentes.encabezado("Línea de tiempo", "Quién hizo cada cambio y cuándo (RNF-04)"), BorderLayout.NORTH);
        JPanel lista = new JPanel();
        lista.setOpaque(false);
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.add(alinear(paso(Tema.AMBAR, "Solicitud creada", o.getFechaCreacion().format(FECHA_HORA) + " · por " + o.getCliente().getNombre(), true)));
        List<OrdenServicio.CambioEstado> cambios = o.getCambios();
        for (int i = 0; i < cambios.size(); i++) {
            OrdenServicio.CambioEstado c = cambios.get(i);
            lista.add(alinear(paso(Tema.colorEstado(c.getEstadoNuevo()), Tema.texto(c.getEstadoNuevo()),
                    c.getFecha().format(FECHA_HORA) + " · por " + c.getResponsable(), i < cambios.size() - 1)));
        }
        List<String> asignaciones = o.getAsignacion().getRegistroCambios();
        if (!asignaciones.isEmpty()) {
            lista.add(Box.createVerticalStrut(10));
            lista.add(alinear(Componentes.etiquetaCampo("Asignaciones de técnico (RF-14)")));
            for (String a : asignaciones) {
                JLabel l = Componentes.texto(a);
                l.setFont(Tema.normal(12.5f));
                l.setIcon(Iconos.casco(14, Tema.AZUL_CLARO));
                l.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
                lista.add(alinear(l));
            }
        }
        t.add(lista, BorderLayout.CENTER);
        return t;
    }

    private static JComponent paso(Color color, String titulo, String detalle, boolean conLinea) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        JComponent punto = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Tema.suavizar(g);
                if (conLinea) {
                    g2.setColor(Tema.BORDE);
                    g2.fillRect(8, 12, 2, getHeight());
                }
                g2.setColor(Tema.suave(color, 0.25f));
                g2.fill(new Ellipse2D.Double(2, 3, 14, 14));
                g2.setColor(color);
                g2.fill(new Ellipse2D.Double(5, 6, 8, 8));
                g2.dispose();
            }
        };
        punto.setPreferredSize(new Dimension(18, 44));
        p.add(punto, BorderLayout.WEST);
        JLabel l = new JLabel("<html><b>" + Tema.html(titulo) + "</b><br><span style='color:#6B7280'>" + Tema.html(detalle) + "</span></html>");
        l.setFont(Tema.normal(13));
        l.setVerticalAlignment(SwingConstants.TOP);
        p.add(l, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        return p;
    }

    private JComponent tarjetaAvisos(OrdenServicio o) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        t.add(Componentes.encabezado("Avisos enviados al cliente", "RF-19 · uno por cada cambio de estado"), BorderLayout.NORTH);
        JPanel lista = new JPanel(new GridLayout(0, 1, 0, 6));
        lista.setOpaque(false);
        if (o.getNotificaciones().isEmpty()) { lista.add(Componentes.texto("Todavía no se han enviado avisos.")); }
        for (Notificacion n : o.getNotificaciones()) {
            boolean ok = n.fueEnviada();
            Icon icono = "Correo".equals(n.getCanal()) ? Iconos.correo(18, ok ? Tema.AZUL_INFO : Tema.ROJO)
                    : Iconos.telefono(18, ok ? Tema.VERDE : Tema.ROJO);
            JLabel l = new JLabel("<html><b>" + Tema.html(n.getMensaje()) + "</b><br><span style='color:#6B7280'>"
                    + n.getFecha().format(FECHA_HORA) + " · " + Tema.html(n.getDetalle()) + "</span></html>", icono, SwingConstants.LEFT);
            l.setIconTextGap(10);
            l.setFont(Tema.normal(12.5f));
            lista.add(l);
        }
        t.add(lista, BorderLayout.CENTER);
        return t;
    }
}
