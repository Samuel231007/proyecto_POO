package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

// Íconos dibujados con Java2D sobre una cuadrícula de 24 x 24: no se usan archivos de imagen.
public final class Iconos {
    @FunctionalInterface
    private interface Pintor {
        void pintar(Graphics2D g);
    }

    private static final class IconoDibujado implements Icon {
        private final int tamanio;
        private final Color color;
        private final Pintor pintor;

        IconoDibujado(int tamanio, Color color, Pintor pintor) {
            this.tamanio = tamanio;
            this.color = color;
            this.pintor = pintor;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = Tema.suavizar(g);
            g2.translate(x, y);
            g2.scale(tamanio / 24.0, tamanio / 24.0);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            pintor.pintar(g2);
            g2.dispose();
        }

        @Override public int getIconWidth() { return tamanio; }
        @Override public int getIconHeight() { return tamanio; }
    }

    private Iconos() { }

    private static Icon crear(int tamanio, Color color, Pintor pintor) { return new IconoDibujado(tamanio, color, pintor); }

    public static Icon logo(int tamanio) {
        return crear(tamanio, Tema.NARANJA, g -> {
            g.fill(new RoundRectangle2D.Double(0, 0, 24, 24, 7, 7));
            g.setColor(Color.WHITE);
            Path2D casa = new Path2D.Double();
            casa.moveTo(4.5, 11.5); casa.lineTo(12, 5); casa.lineTo(19.5, 11.5);
            g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(casa);
            g.draw(new Line2D.Double(6.5, 10.5, 6.5, 19));
            g.draw(new Line2D.Double(17.5, 10.5, 17.5, 19));
            g.draw(new Line2D.Double(6.5, 19, 17.5, 19));
            g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(new Line2D.Double(9.3, 16.7, 13.3, 12.7));
            g.draw(new Arc2D.Double(12.2, 9.6, 4.4, 4.4, 200, 270, Arc2D.OPEN));
        });
    }

    public static Icon servicio(String tipo, int tamanio, Color color) {
        String t = tipo == null ? "" : tipo.toLowerCase(Tema.COLOMBIA);
        if (t.startsWith("plom")) { return gota(tamanio, color); }
        if (t.startsWith("elec")) { return rayo(tamanio, color); }
        if (t.startsWith("pint")) { return rodillo(tamanio, color); }
        if (t.startsWith("cerr")) { return llave(tamanio, color); }
        if (t.startsWith("carp")) { return martillo(tamanio, color); }
        return herramienta(tamanio, color);
    }

    public static Color colorServicio(String tipo) {
        String t = tipo == null ? "" : tipo.toLowerCase(Tema.COLOMBIA);
        if (t.startsWith("plom")) { return new Color(0x0E, 0xA5, 0xE9); }
        if (t.startsWith("elec")) { return new Color(0xEA, 0xB3, 0x08); }
        if (t.startsWith("pint")) { return new Color(0xEC, 0x48, 0x99); }
        if (t.startsWith("cerr")) { return new Color(0x64, 0x74, 0x8B); }
        if (t.startsWith("carp")) { return new Color(0xB4, 0x53, 0x09); }
        return Tema.NARANJA;
    }

    public static Icon gota(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(12, 3);
            p.curveTo(12, 3, 5, 11, 5, 15);
            p.curveTo(5, 19, 8.2, 21.5, 12, 21.5);
            p.curveTo(15.8, 21.5, 19, 19, 19, 15);
            p.curveTo(19, 11, 12, 3, 12, 3);
            g.draw(p);
            g.draw(new Arc2D.Double(8.5, 12, 7, 7, 190, 70, Arc2D.OPEN));
        });
    }

    public static Icon rayo(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(13.5, 2.5); p.lineTo(5, 13.5); p.lineTo(11.5, 13.5);
            p.lineTo(10.5, 21.5); p.lineTo(19, 10.5); p.lineTo(12.5, 10.5); p.closePath();
            g.draw(p);
        });
    }

    public static Icon rodillo(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new RoundRectangle2D.Double(3, 3.5, 15, 5.5, 2.5, 2.5));
            Path2D mango = new Path2D.Double();
            mango.moveTo(18, 6.2); mango.lineTo(21, 6.2); mango.lineTo(21, 12); mango.lineTo(11, 12); mango.lineTo(11, 15);
            g.draw(mango);
            g.draw(new RoundRectangle2D.Double(9.5, 15, 3, 6.5, 1.5, 1.5));
        });
    }

    public static Icon llave(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new Ellipse2D.Double(3, 9, 7.5, 7.5));
            g.draw(new Line2D.Double(10.5, 12.75, 21, 12.75));
            g.draw(new Line2D.Double(18, 12.75, 18, 16));
            g.draw(new Line2D.Double(15, 12.75, 15, 15));
        });
    }

    public static Icon martillo(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new Line2D.Double(13.5, 10.5, 4.5, 19.5));
            Path2D cabeza = new Path2D.Double();
            cabeza.moveTo(10.5, 7.5); cabeza.lineTo(14.5, 3.5); cabeza.lineTo(20.5, 9.5); cabeza.lineTo(16.5, 13.5); cabeza.closePath();
            g.draw(cabeza);
        });
    }

    public static Icon herramienta(int s, Color c) {
        return crear(s, c, g -> {
            g.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(new Line2D.Double(4.5, 19.5, 12.5, 11.5));
            g.draw(new Arc2D.Double(11, 3, 10, 10, 225, 270, Arc2D.OPEN));
        });
    }

    public static Icon casa(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(3, 11); p.lineTo(12, 3.5); p.lineTo(21, 11);
            g.draw(p);
            Path2D cuerpo = new Path2D.Double();
            cuerpo.moveTo(5.5, 9.5); cuerpo.lineTo(5.5, 20.5); cuerpo.lineTo(18.5, 20.5); cuerpo.lineTo(18.5, 9.5);
            g.draw(cuerpo);
            g.draw(new Rectangle2D.Double(10, 14, 4, 6.5));
        });
    }

    public static Icon usuario(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new Ellipse2D.Double(8, 3.5, 8, 8));
            g.draw(new Arc2D.Double(4, 13.5, 16, 14, 0, 180, Arc2D.OPEN));
        });
    }

    public static Icon casco(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new Arc2D.Double(4.5, 6, 15, 14, 0, 180, Arc2D.OPEN));
            g.draw(new RoundRectangle2D.Double(2.5, 13, 19, 4, 2, 2));
            g.draw(new Line2D.Double(12, 6, 12, 10));
        });
    }

    public static Icon escudo(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(12, 2.5); p.lineTo(20, 5.5); p.lineTo(20, 11);
            p.curveTo(20, 16.5, 16.5, 19.8, 12, 21.5);
            p.curveTo(7.5, 19.8, 4, 16.5, 4, 11);
            p.lineTo(4, 5.5); p.closePath();
            g.draw(p);
            Path2D check = new Path2D.Double();
            check.moveTo(8.5, 12); check.lineTo(11, 14.5); check.lineTo(15.5, 9.5);
            g.draw(check);
        });
    }

    public static Icon lista(int s, Color c) {
        return crear(s, c, g -> {
            for (int i = 0; i < 3; i++) {
                double y = 6 + i * 6;
                g.fill(new Ellipse2D.Double(3.5, y - 1.3, 2.6, 2.6));
                g.draw(new Line2D.Double(9, y, 20.5, y));
            }
        });
    }

    public static Icon calendario(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new RoundRectangle2D.Double(3, 4.5, 18, 16.5, 3, 3));
            g.draw(new Line2D.Double(3, 9.5, 21, 9.5));
            g.draw(new Line2D.Double(8, 2.5, 8, 6.5));
            g.draw(new Line2D.Double(16, 2.5, 16, 6.5));
            g.fill(new Rectangle2D.Double(7, 13, 3, 3));
        });
    }

    public static Icon dinero(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new RoundRectangle2D.Double(2.5, 6, 19, 12, 2.5, 2.5));
            g.draw(new Ellipse2D.Double(9, 9, 6, 6));
            g.draw(new Line2D.Double(5.5, 9, 5.5, 9));
            g.draw(new Line2D.Double(18.5, 15, 18.5, 15));
        });
    }

    public static Icon campana(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(5, 17); p.lineTo(6.5, 15); p.lineTo(6.5, 10.5);
            p.curveTo(6.5, 7, 9, 4.5, 12, 4.5);
            p.curveTo(15, 4.5, 17.5, 7, 17.5, 10.5);
            p.lineTo(17.5, 15); p.lineTo(19, 17); p.closePath();
            g.draw(p);
            g.draw(new Arc2D.Double(9.5, 17.5, 5, 4, 180, 180, Arc2D.OPEN));
        });
    }

    public static Icon salir(int s, Color c) {
        return crear(s, c, g -> {
            Path2D puerta = new Path2D.Double();
            puerta.moveTo(10, 4); puerta.lineTo(4, 4); puerta.lineTo(4, 20); puerta.lineTo(10, 20);
            g.draw(puerta);
            g.draw(new Line2D.Double(9, 12, 20, 12));
            Path2D flecha = new Path2D.Double();
            flecha.moveTo(16, 8); flecha.lineTo(20, 12); flecha.lineTo(16, 16);
            g.draw(flecha);
        });
    }

    public static Icon mas(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new Line2D.Double(12, 5, 12, 19));
            g.draw(new Line2D.Double(5, 12, 19, 12));
        });
    }

    public static Icon ojo(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(2.5, 12);
            p.curveTo(5.5, 6.5, 18.5, 6.5, 21.5, 12);
            p.curveTo(18.5, 17.5, 5.5, 17.5, 2.5, 12);
            g.draw(p);
            g.draw(new Ellipse2D.Double(9, 9, 6, 6));
        });
    }

    public static Icon play(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(7, 4.5); p.lineTo(19, 12); p.lineTo(7, 19.5); p.closePath();
            g.draw(p);
        });
    }

    public static Icon check(int s, Color c) {
        return crear(s, c, g -> {
            g.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D p = new Path2D.Double();
            p.moveTo(5, 12.5); p.lineTo(10, 17.5); p.lineTo(19.5, 7);
            g.draw(p);
        });
    }

    public static Icon equis(int s, Color c) {
        return crear(s, c, g -> {
            g.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(new Line2D.Double(6, 6, 18, 18));
            g.draw(new Line2D.Double(18, 6, 6, 18));
        });
    }

    public static Icon alerta(int s, Color c) {
        return crear(s, c, g -> {
            g.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(new Line2D.Double(12, 5.5, 12, 13.5));
            g.fill(new Ellipse2D.Double(10.5, 16.5, 3, 3));
        });
    }

    public static Icon estrella(int s, Color c, boolean rellena) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            for (int i = 0; i < 10; i++) {
                double radio = (i % 2 == 0) ? 10 : 4.3;
                double angulo = Math.toRadians(-90 + i * 36);
                double x = 12 + radio * Math.cos(angulo);
                double y = 12.6 + radio * Math.sin(angulo);
                if (i == 0) { p.moveTo(x, y); } else { p.lineTo(x, y); }
            }
            p.closePath();
            if (rellena) { g.fill(p); } else { g.setStroke(new BasicStroke(1.6f)); g.draw(p); }
        });
    }

    public static Icon reloj(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new Ellipse2D.Double(3, 3, 18, 18));
            Path2D agujas = new Path2D.Double();
            agujas.moveTo(12, 7); agujas.lineTo(12, 12); agujas.lineTo(15.5, 14);
            g.draw(agujas);
        });
    }

    public static Icon filtro(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(3.5, 4.5); p.lineTo(20.5, 4.5); p.lineTo(14, 12.5); p.lineTo(14, 19.5); p.lineTo(10, 17.5); p.lineTo(10, 12.5); p.closePath();
            g.draw(p);
        });
    }

    public static Icon reasignar(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new Arc2D.Double(4, 4, 16, 16, 30, 250, Arc2D.OPEN));
            Path2D flecha = new Path2D.Double();
            flecha.moveTo(16, 3.5); flecha.lineTo(19.5, 6.5); flecha.lineTo(15.5, 9);
            g.draw(flecha);
        });
    }

    public static Icon candado(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new RoundRectangle2D.Double(5, 10.5, 14, 10.5, 2.5, 2.5));
            g.draw(new Arc2D.Double(8, 3.5, 8, 12, 0, 180, Arc2D.OPEN));
            g.draw(new Line2D.Double(8, 9.5, 8, 10.5));
            g.draw(new Line2D.Double(16, 9.5, 16, 10.5));
        });
    }

    public static Icon ubicacion(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(12, 21.5);
            p.curveTo(12, 21.5, 5, 14.5, 5, 9.5);
            p.curveTo(5, 5.5, 8.2, 2.5, 12, 2.5);
            p.curveTo(15.8, 2.5, 19, 5.5, 19, 9.5);
            p.curveTo(19, 14.5, 12, 21.5, 12, 21.5);
            g.draw(p);
            g.draw(new Ellipse2D.Double(9.5, 7, 5, 5));
        });
    }

    public static Icon telefono(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new RoundRectangle2D.Double(6.5, 2.5, 11, 19, 3, 3));
            g.draw(new Line2D.Double(10.5, 18, 13.5, 18));
        });
    }

    public static Icon correo(int s, Color c) {
        return crear(s, c, g -> {
            g.draw(new RoundRectangle2D.Double(2.5, 5, 19, 14, 2.5, 2.5));
            Path2D sobre = new Path2D.Double();
            sobre.moveTo(3.5, 6.5); sobre.lineTo(12, 13); sobre.lineTo(20.5, 6.5);
            g.draw(sobre);
        });
    }

    public static Icon chevron(int s, Color c) {
        return crear(s, c, g -> {
            Path2D p = new Path2D.Double();
            p.moveTo(6, 9); p.lineTo(12, 15); p.lineTo(18, 9);
            g.draw(p);
        });
    }
}
