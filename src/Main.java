import ui.MenuPrincipal;

/**
 * Punto de entrada de la plataforma "Técnicos del hogar a domicilio".
 * Arquitectura de 4 capas, igual que el proyecto base: ui → servicios → repositorios/persistencia → modelos.
 *
 * IMPORTANTE: ejecutar con la carpeta raíz del proyecto como directorio de trabajo,
 * para que encuentre los archivos en src/files/ (igual que el proyecto base).
 */
public class Main {

    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.iniciar();
    }
}
