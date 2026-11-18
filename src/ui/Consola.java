package ui;

import modelos.DisponibilidadTecnico.FranjaHoraria;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Lectura de datos por consola, compartida por todos los menús.
 * En el proyecto base cada menú tenía su propio leerEntero(); aquí se reúnen en una sola clase
 * para no repetir el mismo código en cinco menús.
 */
public class Consola {

    private final Scanner scanner;

    public Consola(Scanner scanner) {
        this.scanner = scanner;
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    // Devuelve -1 si no se escribió un número (el menú lo trata como opción no válida).
    public int leerEntero(String mensaje) {
        try {
            return Integer.parseInt(leerTexto(mensaje));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Escriba un número válido (por ejemplo 15000 o 2.5).");
            }
        }
    }

    // Fecha opcional: Enter vacío = null (sirve para los filtros de RF-20).
    public LocalDate leerFechaOpcional(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.isEmpty()) { return null; }
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Escriba la fecha como AAAA-MM-DD (por ejemplo 2026-11-17), o Enter para omitir.");
            }
        }
    }

    public LocalDate leerFecha(String mensaje) {
        LocalDate fecha;
        do {
            fecha = leerFechaOpcional(mensaje);
        } while (fecha == null);
        return fecha;
    }

    // Pide día y horas, y crea la franja (FranjaHoraria valida que el inicio sea antes del fin).
    public FranjaHoraria leerFranja() {
        System.out.println("Día: 1=Lunes 2=Martes 3=Miércoles 4=Jueves 5=Viernes 6=Sábado 7=Domingo");
        int dia;
        do {
            dia = leerEntero("Número del día: ");
        } while (dia < 1 || dia > 7);
        LocalTime inicio = leerHora("Hora de inicio (HH:MM, ej. 08:00): ");
        LocalTime fin = leerHora("Hora de fin (HH:MM, ej. 12:00): ");
        return new FranjaHoraria(DayOfWeek.of(dia), inicio, fin);
    }

    private LocalTime leerHora(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                if (texto.length() == 4 && texto.charAt(1) == ':') { texto = "0" + texto; }   // 8:00 -> 08:00
                return LocalTime.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Escriba la hora como HH:MM (por ejemplo 08:00 o 14:30).");
            }
        }
    }

    public boolean confirmar(String mensaje) {
        return leerTexto(mensaje + " (s/n): ").equalsIgnoreCase("s");
    }

    /**
     * RNF-06: ejecuta una opción del menú y, si falla, muestra la causa en lenguaje claro
     * en lugar de cerrar el programa con un error.
     */
    public static void intentar(Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException e) {
            System.out.println("No se pudo completar la operación: " + e.getMessage());
        }
    }

    public static void titulo(String texto) {
        String linea = "═".repeat(texto.length() + 4);
        System.out.println("\n╔" + linea + "╗");
        System.out.println("║  " + texto + "  ║");
        System.out.println("╚" + linea + "╝");
    }
}
