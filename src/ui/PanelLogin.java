package ui;

import modelos.Cliente;
import modelos.Servicio;
import modelos.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class PanelLogin extends JPanel {
    private enum Rol { CLIENTE, TECNICO, ADMINISTRADOR }

    private final VentanaPrincipal ventana;
    private Rol rolElegido = Rol.CLIENTE;
    private final JTextField documento = Componentes.campoTexto("");
    private final JPasswordField clave = Componentes.campoClave();
    private final JToggleButton[] botonesRol = new JToggleButton[3];
    private final JLabel pista = Componentes.texto("");

    public PanelLogin(VentanaPrincipal ventana) {
        super(new GridLayout(1, 2));
        this.ventana = ventana;
        add(new PanelPresentacion(ventana.getGestor().getServicios()));
        add(crearFormulario());
        actualizarPista();
    }

    private JComponent crearFormulario() {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(Tema.FONDO);

        PanelTarjeta tarjeta = new PanelTarjeta();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(34, 38, 34, 38));
        tarjeta.setPreferredSize(new Dimension(450, 560));

        tarjeta.add(izq(Componentes.titulo("Bienvenido(a)", 26)));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(izq(Componentes.texto("Elija cómo quiere ingresar a la plataforma")));
        tarjeta.add(Box.createVerticalStrut(22));

        JPanel roles = new JPanel(new GridLayout(1, 3, 10, 0));
        roles.setOpaque(false);
        ButtonGroup grupo = new ButtonGroup();
        Rol[] valores = Rol.values();
        String[] nombres = {"Cliente", "Técnico", "Administrador"};
        for (int i = 0; i < 3; i++) {
            Rol rol = valores[i];
            JToggleButton b = new BotonRol(nombres[i], rol);
            b.addActionListener(e -> { rolElegido = rol; actualizarPista(); repintarRoles(); });
            grupo.add(b);
            roles.add(b);
            botonesRol[i] = b;
        }
        botonesRol[0].setSelected(true);
        roles.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        tarjeta.add(izq(roles));
        tarjeta.add(Box.createVerticalStrut(22));

        tarjeta.add(izq(Componentes.etiquetaCampo("Número de documento")));
        tarjeta.add(Box.createVerticalStrut(6));
        documento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tarjeta.add(izq(documento));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(izq(Componentes.etiquetaCampo("Contraseña")));
        tarjeta.add(Box.createVerticalStrut(6));
        clave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tarjeta.add(izq(clave));
        tarjeta.add(Box.createVerticalStrut(22));

        Boton ingresar = new Boton("Ingresar", Iconos.candado(16, Color.WHITE), Boton.Tipo.PRIMARIO);
        ingresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        ingresar.addActionListener(e -> ingresar());
        clave.addActionListener(e -> ingresar());
        documento.addActionListener(e -> clave.requestFocusInWindow());
        tarjeta.add(izq(ingresar));
        tarjeta.add(Box.createVerticalStrut(16));

        JPanel registro = Componentes.fila(4, Componentes.texto("¿Es cliente nuevo?"));
        JButton crear = new JButton("Crear una cuenta");
        crear.setFont(Tema.negrita(13.5f));
        crear.setForeground(Tema.NARANJA_OSCURO);
        crear.setContentAreaFilled(false);
        crear.setBorderPainted(false);
        crear.setFocusPainted(false);
        crear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        crear.addActionListener(e -> registrarCliente());
        registro.add(crear);
        tarjeta.add(izq(registro));
        tarjeta.add(Box.createVerticalGlue());

        pista.setFont(Tema.normal(12));
        PanelTarjeta caja = new PanelTarjeta(new BorderLayout()).conFondo(Tema.suave(Tema.AZUL_INFO, 0.08f), Tema.suave(Tema.AZUL_INFO, 0.25f));
        caja.setBorder(BorderFactory.createEmptyBorder(10, 14, 12, 14));
        caja.add(pista);
        caja.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        tarjeta.add(izq(caja));

        fondo.add(tarjeta);
        return fondo;
    }

    private static JComponent izq(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private void repintarRoles() { for (JToggleButton b : botonesRol) { b.repaint(); } }

    // Usuarios de prueba de los .txt, útiles para la sustentación.
    private void actualizarPista() {
        String texto;
        switch (rolElegido) {
            case CLIENTE: texto = "Cliente de prueba: <b>3001</b> / <b>miClave</b>"; break;
            case TECNICO: texto = "Técnico de prueba: <b>2001</b> / <b>clave1</b> (o 2002 a 2005)"; break;
            default: texto = "Administrador de prueba: <b>1001</b> / <b>admin123</b>";
        }
        pista.setText("<html><span style='color:#1E40AF'>" + texto + "</span></html>");
    }

    private void ingresar() {
        String doc = documento.getText().trim();
        String contrasenia = new String(clave.getPassword());
        Usuario[] encontrado = new Usuario[1];
        boolean ok = Acciones.ejecutar(this, null, () -> {
            switch (rolElegido) {
                case CLIENTE: encontrado[0] = ventana.getClienteServicio().iniciarSesion(doc, contrasenia); break;
                case TECNICO: encontrado[0] = ventana.getTecnicoServicio().iniciarSesion(doc, contrasenia); break;
                default: encontrado[0] = ventana.getAdministradorServicio().iniciarSesion(doc, contrasenia);
            }
            return encontrado[0] != null;
        });
        if (ok) {
            clave.setText("");
            ventana.abrirSesion(encontrado[0]);
        }
    }

    private void registrarCliente() {
        JTextField nombre = Componentes.campoTexto("");
        JTextField doc = Componentes.campoTexto("");
        JTextField telefono = Componentes.campoTexto("");
        JTextField correo = Componentes.campoTexto("");
        JTextField ubicacion = Componentes.campoTexto("");
        JPasswordField contrasenia = Componentes.campoClave();
        Formulario f = new Formulario(this, "Crear cuenta de cliente", "RF-02 · Todos los campos son obligatorios",
                Iconos.usuario(22, Color.WHITE), "Crear cuenta");
        f.campo("Nombre completo", nombre)
                .campo("Número de documento", doc)
                .campo("Celular (10 dígitos, para avisos por WhatsApp)", telefono)
                .campo("Correo electrónico", correo)
                .campo("Municipio o zona donde vive (ej. Chía)", ubicacion)
                .campo("Contraseña", contrasenia)
                .nota("Su contraseña se guarda cifrada (RNF-05): nadie, ni el administrador, puede leerla.", Tema.AZUL_INFO);
        f.mostrar(() -> Acciones.ejecutar(f, "¡Cuenta creada!", () -> {
            ventana.getClienteServicio().registrarCliente(new Cliente(ubicacion.getText(), nombre.getText(),
                    doc.getText(), telefono.getText(), correo.getText(), new String(contrasenia.getPassword())));
            System.out.println("Ya puede ingresar con su documento y contraseña.");
            documento.setText(doc.getText().trim());
            return true;
        }));
    }

    private class BotonRol extends JToggleButton {
        private final Rol rol;

        BotonRol(String texto, Rol rol) {
            super(texto);
            this.rol = rol;
            setFont(Tema.negrita(12.5f));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setIconTextGap(6);
            setFont(Tema.negrita(12));
            setBorder(BorderFactory.createEmptyBorder(8, 2, 8, 2));
            setMargin(new Insets(0, 0, 0, 0));
            setPreferredSize(new Dimension(110, 80));
        }

        @Override
        protected void paintComponent(Graphics g) {
            boolean activo = isSelected();
            Color color = activo ? Tema.NARANJA : Tema.TEXTO_SUAVE;
            Icon icono = rol == Rol.CLIENTE ? Iconos.casa(24, color) : rol == Rol.TECNICO ? Iconos.casco(24, color) : Iconos.escudo(24, color);
            setIcon(icono);
            setForeground(activo ? Tema.NARANJA_OSCURO : Tema.TEXTO_SUAVE);
            Graphics2D g2 = Tema.suavizar(g);
            RoundRectangle2D forma = new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 14, 14);
            g2.setColor(activo ? Tema.suave(Tema.NARANJA, 0.10f) : Color.WHITE);
            g2.fill(forma);
            g2.setStroke(new BasicStroke(activo ? 2f : 1f));
            g2.setColor(activo ? Tema.NARANJA : Tema.BORDE);
            g2.draw(forma);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class PanelPresentacion extends JPanel {
        PanelPresentacion(List<Servicio> servicios) {
            setLayout(new GridBagLayout());
            JPanel caja = new JPanel();
            caja.setOpaque(false);
            caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));

            JLabel logo = new JLabel(Iconos.logo(76));
            caja.add(izq(logo));
            caja.add(Box.createVerticalStrut(24));
            JLabel nombre = new JLabel(Tema.NOMBRE_APP);
            nombre.setFont(Tema.negrita(38));
            nombre.setForeground(Color.WHITE);
            caja.add(izq(nombre));
            JLabel eslogan = new JLabel(Tema.ESLOGAN);
            eslogan.setFont(Tema.normal(18));
            eslogan.setForeground(new Color(0xC7, 0xD5, 0xE6));
            caja.add(izq(eslogan));
            caja.add(Box.createVerticalStrut(34));

            JLabel subt = new JLabel("NUESTROS SERVICIOS");
            subt.setFont(Tema.negrita(12));
            subt.setForeground(Tema.NARANJA);
            caja.add(izq(subt));
            caja.add(Box.createVerticalStrut(12));
            for (Servicio s : servicios) {
                JLabel l = new JLabel("<html><b>" + Tema.html(s.getTipo()) + "</b>  <span style='color:#9FB3C8'>· garantía de "
                        + s.getDiasGarantia() + " días</span></html>",
                        Iconos.servicio(s.getTipo(), 20, Iconos.colorServicio(s.getTipo()).brighter()), SwingConstants.LEFT);
                l.setFont(Tema.normal(15));
                l.setForeground(Color.WHITE);
                l.setIconTextGap(12);
                l.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                caja.add(izq(l));
            }
            caja.add(Box.createVerticalStrut(30));
            caja.add(izq(beneficio(Iconos.casco(18, Tema.NARANJA), "Técnicos verificados de tu zona")));
            caja.add(izq(beneficio(Iconos.dinero(18, Tema.NARANJA), "Costo estimado antes de la visita")));
            caja.add(izq(beneficio(Iconos.escudo(18, Tema.NARANJA), "Garantía en cada trabajo")));
            caja.add(izq(beneficio(Iconos.campana(18, Tema.NARANJA), "Avisos por WhatsApp o correo")));
            add(caja);
        }

        private static JLabel beneficio(Icon icono, String texto) {
            JLabel l = new JLabel(texto, icono, SwingConstants.LEFT);
            l.setFont(Tema.normal(14.5f));
            l.setForeground(new Color(0xE2, 0xE8, 0xF0));
            l.setIconTextGap(12);
            l.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            return l;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suavizar(g);
            int w = getWidth(), h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, Tema.AZUL_OSCURO, w, h, Tema.AZUL_CLARO));
            g2.fillRect(0, 0, w, h);
            g2.setColor(new Color(255, 255, 255, 12));
            g2.fill(new Ellipse2D.Double(w - 260, -120, 420, 420));
            g2.fill(new Ellipse2D.Double(-140, h - 220, 360, 360));
            g2.setColor(new Color(242, 140, 40, 28));
            g2.fill(new Ellipse2D.Double(w - 150, h - 170, 240, 240));
            g2.dispose();
        }
    }
}
