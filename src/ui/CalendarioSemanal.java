package ui;

import enums.EstadoOrden;
import modelos.DisponibilidadTecnico.FranjaHoraria;
import modelos.OrdenServicio;
import modelos.Tecnico;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

// Agenda del técnico (RF-13): verde = horario, rojo = bloqueos, azul = visitas asignadas.
public class CalendarioSemanal extends JComponent {
    private static final int HORA_DESDE = 6, HORA_HASTA = 21;
    private static final int ANCHO_HORAS = 54, ALTO_ENCABEZADO = 34;
    private static final Color VERDE_SUAVE = new Color(0xD1, 0xFA, 0xE5);
    private static final Color ROJO_SUAVE = new Color(0xFE, 0xE2, 0xE2);

    private final Tecnico tecnico;
    private final List<OrdenServicio> ordenes;

    public CalendarioSemanal(Tecnico tecnico, List<OrdenServicio> ordenesActivas) {
        this.tecnico = tecnico;
        this.ordenes = ordenesActivas;
        setPreferredSize(new Dimension(820, ALTO_ENCABEZADO + (HORA_HASTA - HORA_DESDE) * 30 + 4));
    }

    private double y(LocalTime hora, double altoHora) {
        double horas = hora.getHour() + hora.getMinute() / 60.0 - HORA_DESDE;
        return ALTO_ENCABEZADO + Math.max(0, Math.min(HORA_HASTA - HORA_DESDE, horas)) * altoHora;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth(), h = getHeight();
        double anchoDia = (w - ANCHO_HORAS) / 7.0;
        double altoHora = (h - ALTO_ENCABEZADO - 4) / (double) (HORA_HASTA - HORA_DESDE);

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        g2.setFont(Tema.negrita(12.5f));
        for (int d = 0; d < 7; d++) {
            String nombre = Tema.diaCorto(DayOfWeek.of(d + 1));
            int x = (int) (ANCHO_HORAS + d * anchoDia);
            g2.setColor(Tema.TEXTO_SUAVE);
            g2.drawString(nombre, (int) (x + (anchoDia - g2.getFontMetrics().stringWidth(nombre)) / 2), 22);
        }

        g2.setFont(Tema.normal(11));
        for (int hora = HORA_DESDE; hora <= HORA_HASTA; hora++) {
            double yy = ALTO_ENCABEZADO + (hora - HORA_DESDE) * altoHora;
            g2.setColor(new Color(0xEE, 0xF1, 0xF5));
            g2.draw(new Line2D.Double(ANCHO_HORAS, yy, w, yy));
            g2.setColor(Tema.TEXTO_SUAVE);
            g2.drawString(String.format("%02d:00", hora), 8, (int) yy + 4);
        }
        for (int d = 0; d <= 7; d++) {
            double x = ANCHO_HORAS + d * anchoDia;
            g2.setColor(new Color(0xEE, 0xF1, 0xF5));
            g2.draw(new Line2D.Double(x, ALTO_ENCABEZADO, x, h));
        }

        for (FranjaHoraria f : tecnico.getHorarioDisponible().getFranjasActuales()) {
            dibujarBloque(g2, f, anchoDia, altoHora, VERDE_SUAVE, Tema.VERDE, "Disponible", false);
        }
        for (FranjaHoraria f : tecnico.getHorarioDisponible().getBloqueosIndisponibles()) {
            dibujarBloque(g2, f, anchoDia, altoHora, ROJO_SUAVE, Tema.ROJO, "Bloqueado", true);
        }
        for (OrdenServicio o : ordenes) {
            Color color = o.getEstado() == EstadoOrden.EN_PROCESO ? Tema.VIOLETA : Tema.AZUL_INFO;
            dibujarBloque(g2, o.getSolicitud().getHorarioDisponible(), anchoDia, altoHora,
                    Tema.suave(color, 0.85f), color.darker(), o.getCodigo(), false);
        }
        g2.dispose();
    }

    private void dibujarBloque(Graphics2D g2, FranjaHoraria f, double anchoDia, double altoHora,
                               Color fondo, Color borde, String texto, boolean rayado) {
        int d = f.getDia().getValue() - 1;
        double x = ANCHO_HORAS + d * anchoDia + 4;
        double y1 = y(f.getHoraInicio(), altoHora) + 1;
        double y2 = y(f.getHoraFin(), altoHora) - 1;
        RoundRectangle2D forma = new RoundRectangle2D.Double(x, y1, anchoDia - 8, Math.max(8, y2 - y1), 10, 10);
        g2.setColor(fondo);
        g2.fill(forma);
        if (rayado) {
            Shape clipAnterior = g2.getClip();
            g2.clip(forma);
            g2.setColor(Tema.suave(Tema.ROJO, 0.35f));
            g2.setStroke(new BasicStroke(2f));
            for (double k = x - (y2 - y1); k < x + anchoDia; k += 10) {
                g2.draw(new Line2D.Double(k, y2, k + (y2 - y1), y1));
            }
            g2.setClip(clipAnterior);
        }
        g2.setStroke(new BasicStroke(1.3f));
        g2.setColor(borde);
        g2.draw(forma);
        if (y2 - y1 > 18) {
            g2.setFont(Tema.negrita(11.5f));
            g2.setColor(borde.darker());
            g2.drawString(texto, (float) x + 7, (float) y1 + 15);
            if (y2 - y1 > 34) {
                g2.setFont(Tema.normal(10.5f));
                g2.drawString(f.getHoraInicio() + " – " + f.getHoraFin(), (float) x + 7, (float) y1 + 29);
            }
        }
    }

    public static JPanel leyenda() {
        JPanel p = Componentes.fila(16,
                elemento(VERDE_SUAVE, Tema.VERDE, "Horario disponible"),
                elemento(ROJO_SUAVE, Tema.ROJO, "Bloqueo (no recibe órdenes)"),
                elemento(Tema.suave(Tema.AZUL_INFO, 0.85f), Tema.AZUL_INFO, "Visita asignada"),
                elemento(Tema.suave(Tema.VIOLETA, 0.85f), Tema.VIOLETA, "Trabajo en proceso"));
        p.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        return p;
    }

    private static JLabel elemento(Color fondo, Color borde, String texto) {
        JLabel l = new JLabel(texto, new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setColor(fondo);
                g2.fill(new RoundRectangle2D.Double(x, y, 14, 14, 5, 5));
                g2.setColor(borde);
                g2.draw(new RoundRectangle2D.Double(x, y, 14, 14, 5, 5));
                g2.dispose();
            }
            @Override public int getIconWidth() { return 14; }
            @Override public int getIconHeight() { return 14; }
        }, SwingConstants.LEFT);
        l.setFont(Tema.normal(12.5f));
        l.setForeground(Tema.TEXTO_SUAVE);
        return l;
    }
}
