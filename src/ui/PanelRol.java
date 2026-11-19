package ui;

import modelos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// Base de los paneles de cada rol: barra lateral, menú y secciones.
// Cada sección se vuelve a construir al mostrarla, así siempre tiene los datos actualizados.
public abstract class PanelRol extends JPanel {
    private static final class Seccion {
        final String nombre, descripcion;
        final Icon icono, iconoActivo;
        final Supplier<JComponent> constructor;
        BotonMenu boton;

        Seccion(String nombre, String descripcion, Icon icono, Icon iconoActivo, Supplier<JComponent> constructor) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.icono = icono;
            this.iconoActivo = iconoActivo;
            this.constructor = constructor;
        }
    }

    protected final VentanaPrincipal ventana;
    protected final Usuario usuario;

    private final List<Seccion> secciones = new ArrayList<>();
    private final JPanel menu = new JPanel();
    private final JPanel contenido = new PanelDesplazable(new BorderLayout());
    private final JLabel tituloSeccion = Componentes.titulo("", 24);
    private final JLabel descripcionSeccion = Componentes.texto("");
    private Seccion actual;

    protected PanelRol(VentanaPrincipal ventana, Usuario usuario) {
        super(new BorderLayout());
        this.ventana = ventana;
        this.usuario = usuario;
        add(crearBarraLateral(), BorderLayout.WEST);

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBackground(Tema.FONDO);
        derecha.add(crearEncabezado(), BorderLayout.NORTH);
        contenido.setOpaque(false);
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Tema.FONDO);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        derecha.add(scroll, BorderLayout.CENTER);
        add(derecha, BorderLayout.CENTER);
    }

    protected abstract void registrarSecciones();

    protected abstract String descripcionUsuario();

    protected abstract Color colorRol();

    // Las subclases lo llaman al final de su constructor, cuando sus atributos ya tienen valor.
    protected final void iniciar() {
        registrarSecciones();
        for (Seccion s : secciones) {
            s.boton = new BotonMenu(s);
            menu.add(s.boton);
            menu.add(Box.createVerticalStrut(4));
        }
        actualizarUsuario();
        mostrar(secciones.get(0));
    }

    protected final void agregarSeccion(String nombre, String descripcion, IconoFabrica icono, Supplier<JComponent> constructor) {
        secciones.add(new Seccion(nombre, descripcion, icono.crear(Color.decode("#9FB3C8")), icono.crear(Color.WHITE), constructor));
    }

    @FunctionalInterface
    protected interface IconoFabrica {
        Icon crear(Color color);
    }

    protected final void refrescar() {
        if (actual != null) { mostrar(actual); }
        actualizarUsuario();
    }

    protected final void irA(String nombre) {
        for (Seccion s : secciones) { if (s.nombre.equals(nombre)) { mostrar(s); return; } }
    }

    private void mostrar(Seccion seccion) {
        actual = seccion;
        tituloSeccion.setText(seccion.nombre);
        descripcionSeccion.setText(seccion.descripcion);
        contenido.removeAll();
        JComponent cuerpo = seccion.constructor.get();
        JPanel margen = new JPanel(new BorderLayout());
        margen.setOpaque(false);
        margen.setBorder(BorderFactory.createEmptyBorder(6, 32, 32, 32));
        margen.add(cuerpo, BorderLayout.NORTH);
        contenido.add(margen, BorderLayout.CENTER);
        for (Seccion s : secciones) { if (s.boton != null) { s.boton.repaint(); } }
        contenido.revalidate();
        contenido.repaint();
    }

    private final JPanel tarjetaUsuario = new JPanel(new BorderLayout(12, 0));

    private JComponent crearBarraLateral() {
        JPanel barra = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setPaint(new GradientPaint(0, 0, Tema.AZUL_OSCURO, 0, getHeight(), Tema.AZUL));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        barra.setPreferredSize(new Dimension(262, 0));
        barra.setBorder(BorderFactory.createEmptyBorder(22, 16, 18, 16));

        JPanel arriba = new JPanel();
        arriba.setOpaque(false);
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));

        JLabel marca = new JLabel("<html><b style='font-size:15px'>" + Tema.NOMBRE_APP + "</b><br>"
                + "<span style='color:#9FB3C8'>Servicios a domicilio</span></html>", Iconos.logo(40), SwingConstants.LEFT);
        marca.setFont(Tema.normal(12.5f));
        marca.setForeground(Color.WHITE);
        marca.setIconTextGap(12);
        marca.setAlignmentX(LEFT_ALIGNMENT);
        arriba.add(marca);
        arriba.add(Box.createVerticalStrut(24));

        tarjetaUsuario.setOpaque(false);
        tarjetaUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(255, 255, 255, 30)),
                BorderFactory.createEmptyBorder(14, 4, 14, 4)));
        tarjetaUsuario.setAlignmentX(LEFT_ALIGNMENT);
        tarjetaUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        arriba.add(tarjetaUsuario);
        arriba.add(Box.createVerticalStrut(18));

        JLabel menuTitulo = new JLabel("MENÚ");
        menuTitulo.setFont(Tema.negrita(11));
        menuTitulo.setForeground(new Color(0x7D, 0x93, 0xAD));
        menuTitulo.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 0));
        menuTitulo.setAlignmentX(LEFT_ALIGNMENT);
        arriba.add(menuTitulo);

        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setAlignmentX(LEFT_ALIGNMENT);
        arriba.add(menu);
        barra.add(arriba, BorderLayout.NORTH);

        BotonMenu salir = new BotonMenu("Cerrar sesión", Iconos.salir(18, Color.decode("#9FB3C8")));
        salir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                usuario.cerrarSesion();
                ventana.cerrarSesion();
            }
        });
        barra.add(salir, BorderLayout.SOUTH);
        return barra;
    }

    private void actualizarUsuario() {
        tarjetaUsuario.removeAll();
        tarjetaUsuario.add(Componentes.avatar(usuario.getNombre(), colorRol(), 44), BorderLayout.WEST);
        JLabel datos = new JLabel("<html><b>" + Tema.html(usuario.getNombre()) + "</b><br><span style='color:#9FB3C8'>"
                + Tema.html(descripcionUsuario()) + "</span></html>");
        datos.setFont(Tema.normal(13));
        datos.setForeground(Color.WHITE);
        tarjetaUsuario.add(datos, BorderLayout.CENTER);
        tarjetaUsuario.revalidate();
        tarjetaUsuario.repaint();
    }

    private JComponent crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.setBorder(BorderFactory.createEmptyBorder(26, 32, 18, 32));
        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(tituloSeccion);
        textos.add(descripcionSeccion);
        encabezado.add(textos, BorderLayout.WEST);
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", Tema.COLOMBIA));
        JLabel lFecha = new JLabel(fecha.substring(0, 1).toUpperCase(Tema.COLOMBIA) + fecha.substring(1),
                Iconos.calendario(16, Tema.TEXTO_SUAVE), SwingConstants.RIGHT);
        lFecha.setFont(Tema.normal(13));
        lFecha.setForeground(Tema.TEXTO_SUAVE);
        lFecha.setIconTextGap(8);
        encabezado.add(lFecha, BorderLayout.EAST);
        return encabezado;
    }

    private class BotonMenu extends JLabel {
        private final Seccion seccion;
        private boolean encima;

        BotonMenu(Seccion seccion) {
            this(seccion.nombre, seccion.icono, seccion);
        }

        BotonMenu(String texto, Icon icono) {
            this(texto, icono, null);
        }

        private BotonMenu(String texto, Icon icono, Seccion seccion) {
            super(texto, icono, SwingConstants.LEFT);
            this.seccion = seccion;
            setFont(Tema.normal(14.5f));
            setForeground(new Color(0xC7, 0xD5, 0xE6));
            setIconTextGap(14);
            setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setAlignmentX(LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { encima = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { if (seccion != null) { mostrar(seccion); } }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            boolean activo = seccion != null && seccion == actual;
            Graphics2D g2 = Tema.suavizar(g);
            if (activo) {
                g2.setColor(new Color(255, 255, 255, 26));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Tema.NARANJA);
                g2.fillRoundRect(0, 9, 4, getHeight() - 18, 4, 4);
            } else if (encima) {
                g2.setColor(new Color(255, 255, 255, 14));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
            g2.dispose();
            setForeground(activo ? Color.WHITE : new Color(0xC7, 0xD5, 0xE6));
            setFont(activo ? Tema.negrita(14.5f) : Tema.normal(14.5f));
            if (seccion != null) { setIcon(activo ? seccion.iconoActivo : seccion.icono); }
            super.paintComponent(g);
        }
    }

    protected static JPanel pila(JComponent... bloques) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (int i = 0; i < bloques.length; i++) {
            bloques[i].setAlignmentX(LEFT_ALIGNMENT);
            p.add(bloques[i]);
            if (i < bloques.length - 1) { p.add(Box.createVerticalStrut(18)); }
        }
        return p;
    }

    protected static JPanel filaEstadisticas(JComponent... tarjetas) {
        JPanel p = new JPanel(new GridLayout(1, tarjetas.length, 16, 0));
        p.setOpaque(false);
        for (JComponent t : tarjetas) { p.add(t); }
        return p;
    }

    protected static PanelTarjeta tarjeta(String titulo, String subtitulo, JComponent contenido, JComponent... acciones) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout());
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.setOpaque(false);
        arriba.add(Componentes.encabezado(titulo, subtitulo), BorderLayout.WEST);
        if (acciones.length > 0) {
            JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            botones.setOpaque(false);
            for (JComponent a : acciones) { botones.add(a); }
            arriba.add(botones, BorderLayout.EAST);
        }
        t.add(arriba, BorderLayout.NORTH);
        t.add(contenido, BorderLayout.CENTER);
        return t;
    }

    protected void verDetalle(modelos.OrdenServicio orden) {
        if (orden == null) { Acciones.info(this, "Seleccione una orden", "Haga clic en una fila de la tabla y vuelva a intentarlo."); return; }
        new DialogoDetalleOrden(this, orden, usuario).setVisible(true);
    }

    protected modelos.OrdenServicio seleccionada(TablaOrdenes tabla) {
        modelos.OrdenServicio o = tabla.getSeleccionada();
        if (o == null) { Acciones.info(this, "Seleccione una orden", "Haga clic en una fila de la tabla y vuelva a intentarlo."); }
        return o;
    }
}
