package ui;

import modelos.Administrador;
import modelos.Cliente;
import modelos.Tecnico;
import modelos.Usuario;
import servicios.AdministradorServicio;
import servicios.ClienteServicio;
import servicios.GestorPlataforma;
import servicios.TecnicoServicio;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana de la aplicación. Crea el gestor (que carga los .txt) y los servicios, igual que MenuPrincipal,
 * y cambia entre la pantalla de inicio de sesión y el panel de cada rol (CardLayout).
 *
 * La interfaz gráfica es otra CAPA UI: usa exactamente los mismos servicios, repositorios y modelos
 * que los menús de consola. Por eso no hubo que cambiar la lógica del proyecto.
 */
public class VentanaPrincipal extends JFrame {

    private static final String LOGIN = "login", SESION = "sesion";

    private final GestorPlataforma gestor;
    private final ClienteServicio clienteServicio;
    private final TecnicoServicio tecnicoServicio;
    private final AdministradorServicio administradorServicio;

    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenedor = new JPanel(tarjetas);
    private JComponent panelSesion;

    public VentanaPrincipal() {
        super(Tema.NOMBRE_APP + " — Técnicos del hogar a domicilio");
        this.gestor = new GestorPlataforma();   // MODO ARCHIVO: carga src/files/*.txt (RNF-01)
        this.clienteServicio = new ClienteServicio(gestor);
        this.tecnicoServicio = new TecnicoServicio(gestor);
        this.administradorServicio = new AdministradorServicio(gestor);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(imagenLogo());
        setMinimumSize(new Dimension(1180, 740));
        setSize(1320, 820);
        setLocationRelativeTo(null);

        contenedor.add(new PanelLogin(this), LOGIN);
        setContentPane(contenedor);
    }

    /** Abre el panel que corresponde al rol del usuario. POLIMORFISMO con instanceof sobre Usuario. */
    public void abrirSesion(Usuario usuario) {
        if (panelSesion != null) { contenedor.remove(panelSesion); }
        if (usuario instanceof Cliente) {
            panelSesion = new PanelCliente(this, (Cliente) usuario);
        } else if (usuario instanceof Tecnico) {
            panelSesion = new PanelTecnico(this, (Tecnico) usuario);
        } else {
            panelSesion = new PanelAdministrador(this, (Administrador) usuario);
        }
        contenedor.add(panelSesion, SESION);
        tarjetas.show(contenedor, SESION);
    }

    public void cerrarSesion() {
        tarjetas.show(contenedor, LOGIN);
        if (panelSesion != null) {
            contenedor.remove(panelSesion);
            panelSesion = null;
        }
    }

    public GestorPlataforma getGestor() { return gestor; }
    public ClienteServicio getClienteServicio() { return clienteServicio; }
    public TecnicoServicio getTecnicoServicio() { return tecnicoServicio; }
    public AdministradorServicio getAdministradorServicio() { return administradorServicio; }

    // Ícono de la ventana (barra de tareas): el mismo logo dibujado.
    private static Image imagenLogo() {
        Icon icono = Iconos.logo(64);
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(64, 64, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        icono.paintIcon(null, g, 0, 0);
        g.dispose();
        return img;
    }
}
