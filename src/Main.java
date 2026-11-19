import ui.MenuPrincipal;
import ui.Tema;
import ui.VentanaPrincipal;

import javax.swing.SwingUtilities;

// Sin argumentos abre las ventanas; con el argumento "consola" abre los menús de texto.
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
