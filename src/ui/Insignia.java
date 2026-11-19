package ui;

import enums.EstadoOrden;
import enums.EstadoPago;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

// Etiqueta de color para mostrar un estado: Pendiente, Finalizada...
public class Insignia extends JLabel {
    private final Color color;

    public Insignia(String texto, Color color) {
        super(texto);
        this.color = color;
        setFont(Tema.negrita(12));
        setForeground(color.darker());
        setBorder(BorderFactory.createEmptyBorder(4, 22, 4, 11));
        setOpaque(false);
    }

    public static Insignia de(EstadoOrden estado) { return new Insignia(Tema.texto(estado), Tema.colorEstado(estado)); }

    public static Insignia de(EstadoPago estado) { return new Insignia(estado.getDescripcion(), Tema.colorPago(estado)); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        g2.setColor(Tema.suave(color, 0.15f));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight()));
        g2.setColor(color);
        double d = 7;
        g2.fill(new Ellipse2D.Double(10, (getHeight() - d) / 2, d, d));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getMaximumSize() { return getPreferredSize(); }
}
