package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectorEstrellas extends JPanel {
    private static final Color DORADO = new Color(0xF5, 0xB0, 0x0B);
    private static final String[] TEXTOS = {"Toque una estrella", "Muy malo", "Malo", "Regular", "Bueno", "¡Excelente!"};

    private int valor;
    private final JLabel[] estrellas = new JLabel[5];
    private final JLabel descripcion = Componentes.texto(TEXTOS[0]);

    public SelectorEstrellas(int valorInicial, boolean editable, int tamanio) {
        super(new FlowLayout(FlowLayout.LEFT, 2, 0));
        setOpaque(false);
        for (int i = 0; i < 5; i++) {
            final int n = i + 1;
            estrellas[i] = new JLabel();
            if (editable) {
                estrellas[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                estrellas[i].addMouseListener(new MouseAdapter() {
                    @Override public void mouseClicked(MouseEvent e) { setValor(n); }
                    @Override public void mouseEntered(MouseEvent e) { pintar(n, tamanio); }
                    @Override public void mouseExited(MouseEvent e) { pintar(valor, tamanio); }
                });
            }
            add(estrellas[i]);
        }
        if (editable) {
            add(Box.createHorizontalStrut(10));
            add(descripcion);
        }
        this.valor = valorInicial;
        pintar(valorInicial, tamanio);
        descripcion.setText(TEXTOS[valorInicial]);
    }

    private void pintar(int cuantas, int tamanio) {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setIcon(Iconos.estrella(tamanio, i < cuantas ? DORADO : new Color(0xCB, 0xD5, 0xE1), i < cuantas));
        }
    }

    public void setValor(int valor) {
        this.valor = valor;
        pintar(valor, estrellas[0].getIcon().getIconWidth());
        descripcion.setText(TEXTOS[valor]);
    }

    public int getValor() { return valor; }
}
