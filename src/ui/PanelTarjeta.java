package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PanelTarjeta extends JPanel {
    private Color fondo = Tema.BLANCO;
    private Color borde = Tema.BORDE;

    public PanelTarjeta() {
        this(new BorderLayout());
    }

    public PanelTarjeta(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
    }

    public PanelTarjeta conFondo(Color fondo, Color borde) {
        this.fondo = fondo;
        this.borde = borde;
        repaint();
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        int w = getWidth(), h = getHeight();
        g2.setColor(new Color(15, 23, 42, 10));
        g2.fill(new RoundRectangle2D.Double(1, 3, w - 2, h - 3, 18, 18));
        g2.setColor(fondo);
        g2.fill(new RoundRectangle2D.Double(0, 0, w - 1, h - 3, 16, 16));
        g2.setColor(borde);
        g2.draw(new RoundRectangle2D.Double(0, 0, w - 1, h - 3, 16, 16));
        g2.dispose();
        super.paintComponent(g);
    }
}
