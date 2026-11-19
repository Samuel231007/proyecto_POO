package ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.BooleanSupplier;

public class Formulario extends JDialog {
    private final JPanel campos = new JPanel(new GridBagLayout());
    private final Boton aceptar;
    private int fila = 0;
    private boolean aceptado = false;

    public Formulario(Component padre, String titulo, String subtitulo, Icon icono, String textoAceptar) {
        super(padre == null ? null : SwingUtilities.getWindowAncestor(padre), titulo, ModalityType.APPLICATION_MODAL);
        JPanel encabezado = new JPanel(new BorderLayout(14, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = Tema.suavizar(g);
                g2.setPaint(new GradientPaint(0, 0, Tema.AZUL, getWidth(), 0, Tema.AZUL_CLARO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        encabezado.add(Componentes.circuloIcono(icono, Tema.NARANJA, 44), BorderLayout.WEST);
        JPanel textos = new JPanel(new GridLayout(subtitulo == null ? 1 : 2, 1, 0, 2));
        textos.setOpaque(false);
        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(Tema.negrita(18));
        lTitulo.setForeground(Color.WHITE);
        textos.add(lTitulo);
        if (subtitulo != null) {
            JLabel lSub = new JLabel(subtitulo);
            lSub.setFont(Tema.normal(13));
            lSub.setForeground(new Color(0xC7, 0xD5, 0xE6));
            textos.add(lSub);
        }
        encabezado.add(textos, BorderLayout.CENTER);

        campos.setBackground(Color.WHITE);
        campos.setBorder(BorderFactory.createEmptyBorder(18, 22, 8, 22));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setBackground(Color.WHITE);
        botones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDE),
                BorderFactory.createEmptyBorder(14, 22, 14, 22)));
        Boton cancelar = new Boton("Cancelar", Boton.Tipo.SECUNDARIO);
        cancelar.addActionListener(e -> dispose());
        aceptar = new Boton(textoAceptar, Boton.Tipo.PRIMARIO);
        botones.add(cancelar);
        botones.add(aceptar);

        JScrollPane scroll = new JScrollPane(campos);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(encabezado, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(aceptar);
    }

    public Formulario campo(String etiqueta, JComponent componente) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = fila++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 5, 0);
        campos.add(Componentes.etiquetaCampo(etiqueta), c);
        c.gridy = fila++;
        c.insets = new Insets(0, 0, 14, 0);
        campos.add(componente, c);
        return this;
    }

    public Formulario bloque(JComponent componente) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = fila++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 14, 0);
        campos.add(componente, c);
        return this;
    }

    public Formulario nota(String texto, Color color) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout()).conFondo(Tema.suave(color, 0.10f), Tema.suave(color, 0.35f));
        t.setBorder(BorderFactory.createEmptyBorder(10, 14, 12, 14));
        JLabel l = new JLabel("<html><body style='width:380px'>" + Tema.html(texto) + "</body></html>");
        l.setFont(Tema.normal(13));
        l.setForeground(color.darker().darker());
        t.add(l);
        return bloque(t);
    }

    // Si alAceptar devuelve false (dato inválido) la ventana sigue abierta para corregir.
    public boolean mostrar(BooleanSupplier alAceptar) {
        aceptar.addActionListener(e -> {
            if (alAceptar.getAsBoolean()) {
                aceptado = true;
                dispose();
            }
        });
        pack();
        Dimension d = getSize();
        setSize(Math.max(d.width, 500), Math.min(d.height, 720));
        setLocationRelativeTo(getOwner());
        setVisible(true);
        return aceptado;
    }
}
