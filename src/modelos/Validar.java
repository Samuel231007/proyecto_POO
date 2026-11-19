package modelos;

public class Validar {
    // RNF-01 y RNF-03: estos caracteres separan los campos en los archivos .txt.
    // Si un texto los tuviera, al leer el archivo los campos quedarían corridos y los datos se dañarían.
    public static final String SEPARADORES = "|;";

    // Texto obligatorio: no puede ser nulo, vacío ni solo espacios. Devuelve el texto sin espacios sobrantes.
    public static String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) { throw new IllegalArgumentException("El campo \"" + campo + "\" es obligatorio."); }
        sinSeparadores(valor, campo);
        return valor.trim();
    }

    // Texto opcional: puede ir vacío, pero no puede tener los separadores de los archivos.
    public static String sinSeparadores(String valor, String campo) {
        if (valor == null) { return null; }
        for (char c : SEPARADORES.toCharArray()) {
            if (valor.indexOf(c) >= 0) {
                throw new IllegalArgumentException("El campo \"" + campo + "\" no puede contener el carácter '" + c + "'.");
            }
        }
        return valor;
    }

    public static double positivo(double valor, String campo) {
        if (valor <= 0) { throw new IllegalArgumentException("El campo \"" + campo + "\" debe ser mayor a 0."); }
        return valor;
    }

    public static void objeto(Object valor, String campo) {
        if (valor == null) { throw new IllegalArgumentException("El campo \"" + campo + "\" es obligatorio."); }
    }
}
