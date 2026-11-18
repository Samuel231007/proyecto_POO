package ui;

import enums.EstadoOrden;
import modelos.OrdenServicio;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Tabla de órdenes de servicio con el ícono del oficio y una insignia de color para el estado.
 * Doble clic en una fila abre el detalle.
 */
public class TablaOrdenes extends JPanel {

    private final ModeloOrdenes modelo;
    private final JTable tabla;

    public TablaOrdenes(List<OrdenServicio> ordenes, boolean mostrarCliente, Consumer<OrdenServicio> alAbrir) {
        super(new BorderLayout());
        setOpaque(false);
        modelo = new ModeloOrdenes(ordenes, mostrarCliente);
        tabla = new JTable(modelo);
        Componentes.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(140);
        tabla.getColumnModel().getColumn(1).setCellRenderer(new RenderServicio());
        int columnaEstado = modelo.columnaEstado();
        tabla.getColumnModel().getColumn(columnaEstado).setCellRenderer(new RenderEstado());
        tabla.getColumnModel().getColumn(columnaEstado).setPreferredWidth(120);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && getSeleccionada() != null) { alAbrir.accept(getSeleccionada()); }
            }
        });
        add(Componentes.scrollTabla(tabla), BorderLayout.CENTER);
        int alto = Math.min(Math.max(ordenes.size(), 3), 9) * 40 + 42;
        setPreferredSize(new Dimension(600, alto));
    }

    /** Orden de la fila seleccionada, o null. */
    public OrdenServicio getSeleccionada() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : modelo.ordenes.get(tabla.convertRowIndexToModel(fila));
    }

    public void seleccionar(int fila) {
        if (fila >= 0 && fila < tabla.getRowCount()) { tabla.setRowSelectionInterval(fila, fila); }
    }

    public void alCambiarSeleccion(Runnable accion) {
        tabla.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) { accion.run(); } });
    }

    // ─── Modelo de datos de la tabla (clase anidada estática) ─────────────────

    private static class ModeloOrdenes extends AbstractTableModel {
        private final List<OrdenServicio> ordenes;
        private final List<String> columnas = new ArrayList<>();
        private final boolean mostrarCliente;

        ModeloOrdenes(List<OrdenServicio> ordenes, boolean mostrarCliente) {
            this.ordenes = ordenes;
            this.mostrarCliente = mostrarCliente;
            columnas.add("Orden");
            columnas.add("Servicio");
            if (mostrarCliente) { columnas.add("Cliente"); }
            columnas.add("Técnico");
            columnas.add("Ubicación");
            columnas.add("Estado");
            columnas.add("Creada");
            columnas.add("Valor");
        }

        int columnaEstado() { return columnas.indexOf("Estado"); }

        @Override public int getRowCount() { return ordenes.size(); }
        @Override public int getColumnCount() { return columnas.size(); }
        @Override public String getColumnName(int c) { return columnas.get(c); }
        @Override public Class<?> getColumnClass(int c) { return columnas.get(c).equals("Estado") ? EstadoOrden.class : String.class; }

        @Override
        public Object getValueAt(int fila, int columna) {
            OrdenServicio o = ordenes.get(fila);
            switch (columnas.get(columna)) {
                case "Orden": return o.getCodigo();
                case "Servicio": return o.getServicio().getTipo();
                case "Cliente": return o.getCliente().getNombre();
                case "Técnico": return o.getTecnico() == null ? "Sin asignar" : o.getTecnico().getNombre();
                case "Ubicación": return o.getSolicitud().getUbicacion();
                case "Estado": return o.getEstado();
                case "Creada": return o.getFechaCreacion().toLocalDate().toString();
                default:
                    if (o.getCostoFinal() > 0) { return Tema.dinero(o.getCostoFinal()); }
                    if (o.getCostoEstimado() > 0) { return "≈ " + Tema.dinero(o.getCostoEstimado()); }
                    return "Por definir";
            }
        }
    }

    // ─── Cómo se dibujan las celdas especiales ────────────────────────────────

    /** Celda de servicio: ícono del oficio + nombre. */
    private static class RenderServicio extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JLabel l = (JLabel) t.getDefaultRenderer(String.class).getTableCellRendererComponent(t, v, s, f, r, c);
            l.setIcon(Iconos.servicio(String.valueOf(v), 18, Iconos.colorServicio(String.valueOf(v))));
            l.setIconTextGap(8);
            l.setFont(Tema.negrita(13.5f));
            return l;
        }
    }

    /** Celda de estado: insignia de color. */
    private static class RenderEstado implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 9));
            p.setBackground(s ? t.getSelectionBackground() : Color.WHITE);
            p.add(Insignia.de((EstadoOrden) v));
            p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xF1, 0xF3, 0xF6)));
            return p;
        }
    }
}
