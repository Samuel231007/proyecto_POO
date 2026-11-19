package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class Boton extends JButton {
    public enum Tipo {
        PRIMARIO(Tema.NARANJA, Tema.NARANJA_OSCURO, Color.WHITE),
        SECUNDARIO(Color.WHITE, new Color(0xEE, 0xF2, 0xF7), Tema.TEXTO),
        AZUL(Tema.AZUL, Tema.AZUL_OSCURO, Color.WHITE),
        EXITO(Tema.VERDE, new Color(0x05, 0x96, 0x69), Color.WHITE),
        PELIGRO(Color.WHITE, Tema.suave(Tema.ROJO, 0.10f), Tema.ROJO);

        private final Color fondo, fondoHover, texto;

        Tipo(Color fondo, Color fondoHover, Color texto) {
            this.fondo = fondo;
            this.fondoHover = fondoHover;
            this.texto = texto;
        }
    }

    private final Tipo tipo;
    private boolean encima;

    public Boton(String texto, Tipo tipo) {
        this(texto, null, tipo);
    }

    public Boton(String texto, Icon icono, Tipo tipo) {
        super(texto, icono);
        this.tipo = tipo;
        setFont(Tema.negrita(13.5f));
        setForeground(tipo.texto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setIconTextGap(8);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { encima = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suavizar(g);
        Color fondo = !isEnabled() ? new Color(0xE5, 0xE7, 0xEB) : (encima || getModel().isPressed()) ? tipo.fondoHover : tipo.fondo;
        RoundRectangle2D forma = new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, 12, 12);
        g2.setColor(fondo);
        g2.fill(forma);
        if (tipo == Tipo.SECUNDARIO || tipo == Tipo.PELIGRO) {
            g2.setColor(tipo == Tipo.PELIGRO && isEnabled() ? Tema.suave(Tema.ROJO, 0.45f) : Tema.BORDE);
            g2.draw(forma);
        }
        g2.dispose();
        setForeground(isEnabled() ? tipo.texto : Tema.TEXTO_SUAVE);
        super.paintComponent(g);
    }
}
