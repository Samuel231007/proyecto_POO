import java.time.LocalDate;

/**
 * Pruebas sencillas de las reglas de Servicio y Garantia (se ejecuta con main).
 * Parte de Valeria Alvarado.
 * No necesita otras clases del proyecto: se compila solo con Servicio.java y Garantia.java.
 * Apoya RF-05 (precio por tipo de servicio), RF-07 (garantía) y RF-18 (revisión por garantía).
 *
 * Cómo ejecutarla:
 *   javac Servicio.java Garantia.java PruebaServicioGarantia.java
 *   java PruebaServicioGarantia
 */
public class PruebaServicioGarantia {

    private static int correctas = 0;
    private static int fallidas = 0;

    // Muestra si una comprobación salió bien o mal y lleva la cuenta.
    private static void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            correctas++;
            System.out.println("OK     - " + descripcion);
        } else {
            fallidas++;
            System.out.println("FALLÓ  - " + descripcion);
        }
    }

    // Los double no se comparan con ==, se comparan con una diferencia mínima.
    private static boolean iguales(double a, double b) {
        return Math.abs(a - b) < 0.0001;
    }

    public static void main(String[] args) {
        probarServicio();
        probarGarantia();
        System.out.println();
        System.out.println("Resultado: " + correctas + " pruebas correctas y " + fallidas + " con fallas.");
    }

    // ---------- Servicio (RF-05 y RF-07) ----------
    private static void probarServicio() {
        System.out.println("=== Servicio ===");

        Servicio plomeria = new Servicio("Plomería", 1.2, 2, 30);
        verificar("Guarda el tipo, la duración y los días de garantía",
                plomeria.getTipo().equals("Plomería")
                        && iguales(plomeria.getDuracionEstimadaHoras(), 2)
                        && plomeria.getDiasGarantia() == 30);

        // 2 horas x 50000 x 1.2 = 120000
        verificar("Mano de obra = horas x tarifa del técnico x factor del tipo",
                iguales(plomeria.calcularManoObra(2, 50000), 120000));

        Servicio pintura = new Servicio("Pintura", Servicio.FACTOR_NORMAL, 4, 15);
        verificar("Con el factor normal (1.0) no hay recargo: 3 h x 40000 = 120000",
                iguales(pintura.calcularManoObra(3, 40000), 120000));

        Servicio electricidad = new Servicio("Electricidad", 1.5, 3, 60);
        verificar("Cada tipo aplica su propia tarifa (RF-05)",
                !iguales(plomeria.calcularManoObra(2, 50000), electricidad.calcularManoObra(2, 50000)));

        Servicio conEspacios = new Servicio("  Gasfitería  ", 1.0, 1, 10);
        verificar("El tipo se guarda sin espacios sobrantes", conEspacios.getTipo().equals("Gasfitería"));

        // Datos inválidos: deben lanzar IllegalArgumentException.
        boolean huboError = false;
        try {
            new Servicio("", 1.0, 2, 30);
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite un tipo de servicio vacío", huboError);

        huboError = false;
        try {
            new Servicio("Plomería", 0, 2, 30);
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite un factor de tarifa de 0", huboError);

        huboError = false;
        try {
            new Servicio("Plomería", 1.2, 0, 30);
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite una duración estimada de 0 horas", huboError);

        huboError = false;
        try {
            new Servicio("Plomería", 1.2, 2, -5);
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite días de garantía negativos", huboError);

        huboError = false;
        try {
            plomeria.calcularManoObra(0, 50000);
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No calcula la mano de obra con 0 horas", huboError);

        huboError = false;
        try {
            plomeria.calcularManoObra(2, 0);
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No calcula la mano de obra con una tarifa de 0", huboError);

        // Los setters también validan.
        huboError = false;
        try {
            plomeria.setDiasGarantia(-1);
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("El setter de días de garantía también valida", huboError);
        verificar("Tras el error, el servicio conserva su valor anterior", plomeria.getDiasGarantia() == 30);
    }

    // ---------- Garantia (RF-07 y RF-18) ----------
    private static void probarGarantia() {
        System.out.println();
        System.out.println("=== Garantia ===");

        LocalDate inicio = LocalDate.of(2026, 11, 1);
        Garantia garantia = new Garantia(inicio, 30, "Fallas del servicio de Plomería");

        verificar("La fecha de fin es el inicio más los días de vigencia",
                garantia.getFechaFin().equals(LocalDate.of(2026, 12, 1)));
        verificar("Está vigente el día de inicio", garantia.estaVigente(inicio));
        verificar("Está vigente el último día", garantia.estaVigente(LocalDate.of(2026, 12, 1)));
        verificar("No está vigente un día después del fin", !garantia.estaVigente(LocalDate.of(2026, 12, 2)));
        verificar("No está vigente un día antes del inicio", !garantia.estaVigente(LocalDate.of(2026, 10, 31)));

        Garantia sinDias = new Garantia(inicio, 0, "Cobertura mínima");
        verificar("Con 0 días solo está vigente el mismo día",
                sinDias.estaVigente(inicio) && !sinDias.estaVigente(LocalDate.of(2026, 11, 2)));

        // RF-18: revisión dentro de la vigencia.
        garantia.solicitarRevision(LocalDate.of(2026, 11, 10), "La llave vuelve a gotear");
        verificar("Registra una revisión dentro de la vigencia", garantia.getRevisiones().size() == 1);
        verificar("La revisión guarda la fecha y el problema",
                garantia.getRevisiones().get(0).equals("2026-11-10 - La llave vuelve a gotear"));

        // RF-18: revisión fuera de la vigencia.
        boolean huboError = false;
        try {
            garantia.solicitarRevision(LocalDate.of(2027, 1, 15), "Otro problema");
        } catch (IllegalStateException e) {
            huboError = true;
        }
        verificar("Rechaza una revisión fuera de la vigencia", huboError);
        verificar("La revisión rechazada no se guarda", garantia.getRevisiones().size() == 1);

        huboError = false;
        try {
            garantia.solicitarRevision(LocalDate.of(2026, 11, 12), "   ");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("Rechaza una revisión sin descripción del problema", huboError);

        // getRevisiones devuelve una copia: cambiarla no afecta la original.
        garantia.getRevisiones().add("Texto que no debería quedar guardado");
        verificar("Modificar la lista devuelta no cambia las revisiones guardadas",
                garantia.getRevisiones().size() == 1);

        // Datos inválidos en el constructor.
        huboError = false;
        try {
            new Garantia(null, 30, "Cobertura");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite una garantía sin fecha de inicio", huboError);

        huboError = false;
        try {
            new Garantia(inicio, -3, "Cobertura");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite días de vigencia negativos", huboError);

        huboError = false;
        try {
            new Garantia(inicio, 30, "");
        } catch (IllegalArgumentException e) {
            huboError = true;
        }
        verificar("No permite una cobertura vacía", huboError);
    }
}
