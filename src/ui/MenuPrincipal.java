package ui;

import modelos.Administrador;
import modelos.Cliente;
import modelos.Tecnico;
import servicios.AdministradorServicio;
import servicios.ClienteServicio;
import servicios.GestorPlataforma;
import servicios.TecnicoServicio;

import java.util.Scanner;

/**
 * Menú raíz: crea el gestor (que carga los .txt) y los servicios, y abre el menú de cada rol al iniciar sesión.
 */
public class MenuPrincipal {

    // OOP: COMPOSICIÓN — los servicios se crean aquí y se comparten con los submenús
    private final GestorPlataforma gestor;
    private final ClienteServicio clienteServicio;
    private final TecnicoServicio tecnicoServicio;
    private final AdministradorServicio administradorServicio;

    private final Scanner scanner;   // un único Scanner compartido entre todos los menús
    private final Consola consola;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.consola = new Consola(scanner);
        this.gestor = new GestorPlataforma();   // MODO ARCHIVO: carga src/files/*.txt
        this.clienteServicio = new ClienteServicio(gestor);
        this.tecnicoServicio = new TecnicoServicio(gestor);
        this.administradorServicio = new AdministradorServicio(gestor);
    }

    public void iniciar() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    TÉCNICOS DEL HOGAR A DOMICILIO          ║");
        System.out.println("║    Programación Orientada a Objetos        ║");
        System.out.println("╚════════════════════════════════════════════╝");

        int opcion;
        do {
            System.out.println("\n┌────────────────────────────────────────────┐");
            System.out.println("│              MENÚ PRINCIPAL                │");
            System.out.println("├────────────────────────────────────────────┤");
            System.out.println("│  1. Ingresar como Cliente                  │");
            System.out.println("│  2. Registrarme como Cliente               │");
            System.out.println("│  3. Ingresar como Técnico                  │");
            System.out.println("│  4. Ingresar como Administrador            │");
            System.out.println("│  5. Demo de requerimientos (en memoria)    │");
            System.out.println("│  0. Salir                                  │");
            System.out.println("└────────────────────────────────────────────┘");
            opcion = consola.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1: ingresarCliente(); break;
                case 2: Consola.intentar(this::registrarCliente); break;
                case 3: ingresarTecnico(); break;
                case 4: ingresarAdministrador(); break;
                case 5: DemoRequerimientos.ejecutarDemo(); break;
                case 0:
                    System.out.println("\n¡Hasta luego! Los datos quedaron guardados en src/files/ (RNF-01).");
                    break;
                default: System.out.println("Opción no válida. Escriba un número entre 0 y 5.");
            }
        } while (opcion != 0);
        scanner.close();
    }

    private void ingresarCliente() {
        String documento = consola.leerTexto("Documento: ");
        String clave = consola.leerTexto("Contraseña: ");
        Cliente cliente = clienteServicio.iniciarSesion(documento, clave);
        if (cliente != null) { new MenuCliente(cliente, clienteServicio, consola).mostrar(); }
    }

    // RF-02
    private void registrarCliente() {
        System.out.println("\n--- REGISTRO DE CLIENTE ---");
        String nombre = consola.leerTexto("Nombre completo: ");
        String documento = consola.leerTexto("Documento: ");
        String telefono = consola.leerTexto("Celular (10 dígitos, para WhatsApp): ");
        String correo = consola.leerTexto("Correo: ");
        String ubicacion = consola.leerTexto("Ubicación (municipio o zona, ej. Chía): ");
        String clave = consola.leerTexto("Contraseña: ");
        clienteServicio.registrarCliente(new Cliente(ubicacion, nombre, documento, telefono, correo, clave));
        System.out.println("Ya puede ingresar con su documento y contraseña.");
    }

    private void ingresarTecnico() {
        String documento = consola.leerTexto("Documento: ");
        String clave = consola.leerTexto("Contraseña: ");
        Tecnico tecnico = tecnicoServicio.iniciarSesion(documento, clave);
        if (tecnico != null) { new MenuTecnico(tecnico, tecnicoServicio, consola).mostrar(); }
    }

    private void ingresarAdministrador() {
        String documento = consola.leerTexto("Documento: ");
        String clave = consola.leerTexto("Contraseña: ");
        Administrador admin = administradorServicio.iniciarSesion(documento, clave);
        if (admin != null) {
            new MenuAdministrador(admin, administradorServicio, clienteServicio, tecnicoServicio, consola).mostrar();
        }
    }
}
