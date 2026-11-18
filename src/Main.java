import ui.MenuPrincipal;
import ui.Tema;
import ui.VentanaPrincipal;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la plataforma "Técnicos del hogar a domicilio".
 *
 * Arquitectura del sistema (4 capas, igual que el proyecto base de la biblioteca):
 *
 *   ┌──────────────────────────────────────────────────────────────────┐
 *   │  CAPA UI (ui/)                                                   │
 *   │  Ventanas: VentanaPrincipal, PanelLogin, PanelCliente,           │
 *   │            PanelTecnico, PanelAdministrador...                   │
 *   │  Consola:  MenuPrincipal, MenuCliente, MenuTecnico,              │
 *   │            MenuAdministrador, DemoRequerimientos                 │
 *   ├──────────────────────────────────────────────────────────────────┤
 *   │  CAPA SERVICIOS (servicios/)                                     │
 *   │  GestorPlataforma, ClienteServicio, TecnicoServicio,             │
 *   │  AdministradorServicio                                           │
 *   ├──────────────────────────────────────────────────────────────────┤
 *   │  CAPA REPOSITORIOS (repositorios/) + persistencia/FileManage     │
 *   │  Un repositorio por archivo .txt de src/files/                   │
 *   ├──────────────────────────────────────────────────────────────────┤
 *   │  CAPA MODELOS (modelos/ + enums/ + interfaces/)                  │
 *   │  Usuario, Cliente, Tecnico, Administrador, OrdenServicio, ...    │
 *   └──────────────────────────────────────────────────────────────────┘
 *
 * Cómo ejecutarlo:
 *   - Sin argumentos         → abre la aplicación con ventanas.
 *   - Con el argumento "consola" → abre los menús de texto (en IntelliJ: Run > Edit Configurations >
 *     Program arguments: consola).
 * Las dos formas usan los mismos servicios y los mismos archivos .txt.
 *
 * IMPORTANTE: ejecutar con la carpeta raíz del proyecto como directorio de trabajo,
 * para que encuentre los archivos en src/files/ (igual que el proyecto base).
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("consola")) {
            MenuPrincipal menu = new MenuPrincipal();
            menu.iniciar();
            return;
        }
        // Swing exige crear y usar las ventanas desde su propio hilo (Event Dispatch Thread).
        SwingUtilities.invokeLater(() -> {
            Tema.instalar();
            new VentanaPrincipal().setVisible(true);
        });
    }
}
