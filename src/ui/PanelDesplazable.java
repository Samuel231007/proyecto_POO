package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel para poner dentro de un JScrollPane que se ajusta al ANCHO de la ventana
 * (solo se desplaza hacia abajo, nunca de lado). IMPLEMENTA la interfaz Scrollable de Swing.
 */
public class PanelDesplazable extends JPanel implements Scrollable {

    public PanelDesplazable(LayoutManager layout) {
        super(layout);
    }

    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle visible, int orientacion, int direccion) { return 18; }
    @Override public int getScrollableBlockIncrement(Rectangle visible, int orientacion, int direccion) { return visible.height - 40; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getParent() instanceof JViewport && getParent().getHeight() > getPreferredSize().height;
    }
}
