package ui;

import modelos.OrdenServicio;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

// Piezas visuales que se repiten en todas las pantallas: títulos, campos, tablas y estadísticas.
public final class Componentes {
    private Componentes() { }

    public static JLabel titulo(String texto, float tamanio) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.negrita(tamanio));
        l.setForeground(Tema.TEXTO);
        return l;
    }

    public static JLabel texto(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.normal(13.5f));
        l.setForeground(Tema.TEXTO_SUAVE);
        return l;
    }

    public static JLabel etiquetaCampo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.negrita(12.5f));
        l.setForeground(Tema.TEXTO);
        return l;
    }

    private static Border bordeCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    public static JTextField campoTexto(String valor) {
        JTextField campo = new JTextField(valor == null ? "" : valor, 22);
        estilizarCampo(campo);
        return campo;
    }

    public static JPasswordField campoClave() {
        JPasswordField campo = new JPasswordField(22);
        estilizarCampo(campo);
        return campo;
    }

    public static JTextArea areaTexto(int filas) {
        JTextArea area = new JTextArea(filas, 22);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(Tema.normal(14));
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return area;
    }

    public static JScrollPane conBorde(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE, 1, true));
        return scroll;
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setFont(Tema.normal(14));
        campo.setBorder(bordeCampo());
        campo.setBackground(Color.WHITE);
    }

    public static <T> JComboBox<T> combo(T[] opciones) {
        JComboBox<T> combo = new JComboBox<>(opciones);
        combo.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = new JButton(Iconos.chevron(14, Tema.TEXTO_SUAVE));
                b.setContentAreaFilled(false);
                b.setBorderPainted(false);
                b.setFocusPainted(false);
                b.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 10));
                return b;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean foco) {
                g.setColor(Color.WHITE);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            @Override
            public void paintCurrentValue(Graphics g, Rectangle bounds, boolean foco) {
                super.paintCurrentValue(g, bounds, false);
            }
        });
        combo.setFont(Tema.normal(14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1, true),
                BorderFactory.createEmptyBorder(5, 4, 5, 0)));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int i, boolean sel, boolean foco) {
                JLabel l = (JLabel) super.getListCellRendererComponent(lista, valor, i, sel, false);
                l.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                l.setFont(Tema.normal(14));
                l.setBackground(sel && i >= 0 ? Tema.suave(Tema.NARANJA, 0.18f) : Color.WHITE);
                l.setForeground(Tema.TEXTO);
                return l;
            }
        });
        return combo;
    }

    public static JComponent circuloIcono(Icon icono, Color color, int tamanio) {
        JComponent c = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setColor(color);
                g2.fill(new Ellipse2D.Double(0, 0, tamanio, tamanio));
                icono.paintIcon(this, g2, (tamanio - icono.getIconWidth()) / 2, (tamanio - icono.getIconHeight()) / 2);
                g2.dispose();
            }
        };
        Dimension d = new Dimension(tamanio, tamanio);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
        JPanel envoltura = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        envoltura.setOpaque(false);
        envoltura.add(c);
        return envoltura;
    }

    public static JComponent cuadroIcono(Icon icono, Color color, int tamanio) {
        JComponent c = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setColor(Tema.suave(color, 0.16f));
                g2.fill(new RoundRectangle2D.Double(0, 0, tamanio, tamanio, 14, 14));
                icono.paintIcon(this, g2, (tamanio - icono.getIconWidth()) / 2, (tamanio - icono.getIconHeight()) / 2);
                g2.dispose();
            }
        };
        Dimension d = new Dimension(tamanio, tamanio);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
        return c;
    }

    public static JComponent avatar(String nombre, Color color, int tamanio) {
        String[] partes = nombre.trim().split("\\s+");
        String iniciales = (partes[0].substring(0, 1) + (partes.length > 1 ? partes[1].substring(0, 1) : "")).toUpperCase(Tema.COLOMBIA);
        JComponent c = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setColor(color);
                g2.fill(new Ellipse2D.Double(0, 0, tamanio, tamanio));
                g2.setColor(Color.WHITE);
                g2.setFont(Tema.negrita(tamanio * 0.38f));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(iniciales, (tamanio - fm.stringWidth(iniciales)) / 2, (tamanio - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        Dimension d = new Dimension(tamanio, tamanio);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
        return c;
    }

    public static PanelTarjeta estadistica(String titulo, String valor, Icon icono, Color color) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout(14, 0));
        t.add(cuadroIcono(icono, color, 48), BorderLayout.WEST);
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        JLabel lValor = titulo(valor, 22);
        JLabel lTitulo = texto(titulo);
        textos.add(lValor);
        textos.add(lTitulo);
        t.add(textos, BorderLayout.CENTER);
        return t;
    }

    public static JComponent vacio(String mensaje, Icon icono) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        JPanel caja = new JPanel();
        caja.setOpaque(false);
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
        JComponent ic = cuadroIcono(icono, Tema.TEXTO_SUAVE, 64);
        ic.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel l = texto(mensaje);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        caja.add(ic);
        caja.add(Box.createVerticalStrut(12));
        caja.add(l);
        p.add(caja);
        p.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        return p;
    }

    public static JPanel fila(int espacio, Component... componentes) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, espacio, 0));
        p.setOpaque(false);
        for (Component c : componentes) { p.add(c); }
        return p;
    }

    public static JPanel encabezado(String titulo, String subtitulo) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(titulo(titulo, 16));
        if (subtitulo != null) {
            p.add(Box.createVerticalStrut(3));
            p.add(texto(subtitulo));
        }
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        return p;
    }

    public static JPanel dato(String etiqueta, String valor) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setOpaque(false);
        JLabel e = texto(etiqueta);
        e.setFont(Tema.normal(12));
        JLabel v = new JLabel(valor == null || valor.isEmpty() ? "—" : valor);
        v.setFont(Tema.negrita(14));
        v.setForeground(Tema.TEXTO);
        p.add(e);
        p.add(v);
        return p;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(40);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setFont(Tema.normal(13.5f));
        tabla.setSelectionBackground(Tema.suave(Tema.NARANJA, 0.18f));
        tabla.setSelectionForeground(Tema.TEXTO);
        tabla.setFillsViewportHeight(true);
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        JTableHeader header = tabla.getTableHeader();
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                l.setFont(Tema.negrita(12));
                l.setForeground(Tema.TEXTO_SUAVE);
                l.setBackground(new Color(0xF8, 0xFA, 0xFC));
                l.setOpaque(true);
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                l.setText(v == null ? "" : v.toString().toUpperCase(Tema.COLOMBIA));
                return l;
            }
        });
        DefaultTableCellRenderer celda = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, false, r, c);
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xF1, 0xF3, 0xF6)),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                if (!s) { l.setBackground(Color.WHITE); }
                l.setIcon(null);   // el renderer se reutiliza en todas las celdas: se limpia
                l.setFont(Tema.normal(13.5f));
                return l;
            }
        };
        tabla.setDefaultRenderer(Object.class, celda);
        tabla.setDefaultRenderer(String.class, celda);
    }

    public static JScrollPane scrollTabla(JTable tabla) {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDE, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public static String resumen(OrdenServicio o) {
        return o.getCodigo() + " · " + o.getServicio().getTipo();
    }
}
