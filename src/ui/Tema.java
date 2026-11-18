package ui;

import enums.EstadoOrden;
import enums.EstadoPago;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

/**
 * Identidad visual de la aplicación: colores, fuentes y formatos.
 * Todos los paneles toman sus colores de aquí, así la interfaz se ve igual en todas las pantallas
 * y basta cambiar un valor para cambiar el estilo completo.
 *
 * Paleta pensada para un servicio técnico del hogar:
 *   azul profundo = confianza y profesionalismo (barra lateral)
 *   naranja       = herramienta / acción (botones principales)
 */
public final class Tema {

    public static final String NOMBRE_APP = "Técnicos del Hogar";
    public static final String ESLOGAN = "Expertos a domicilio para tu casa";

    public static final Locale COLOMBIA = Locale.forLanguageTag("es-CO");

    // ─── Colores de la marca ──────────────────────────────────────────────────
    public static final Color AZUL_OSCURO = new Color(0x15, 0x2B, 0x47);
    public static final Color AZUL = new Color(0x1E, 0x3A, 0x5F);
    public static final Color AZUL_CLARO = new Color(0x2D, 0x5A, 0x8E);
    public static final Color NARANJA = new Color(0xF2, 0x8C, 0x28);
    public static final Color NARANJA_OSCURO = new Color(0xD9, 0x73, 0x10);

    // ─── Colores de la interfaz ───────────────────────────────────────────────
    public static final Color FONDO = new Color(0xF3, 0xF5, 0xF9);
    public static final Color BLANCO = Color.WHITE;
    public static final Color BORDE = new Color(0xE3, 0xE7, 0xEE);
    public static final Color TEXTO = new Color(0x1F, 0x29, 0x37);
    public static final Color TEXTO_SUAVE = new Color(0x6B, 0x72, 0x80);
    public static final Color VERDE = new Color(0x10, 0xB9, 0x81);
    public static final Color ROJO = new Color(0xEF, 0x44, 0x44);
    public static final Color AMBAR = new Color(0xF5, 0x9E, 0x0B);
    public static final Color AZUL_INFO = new Color(0x3B, 0x82, 0xF6);
    public static final Color VIOLETA = new Color(0x8B, 0x5C, 0xF6);

    private static final String FAMILIA = elegirFamilia();

    private Tema() { }

    // Usa la fuente de Windows si existe; si no, una equivalente del sistema.
    private static String elegirFamilia() {
        String[] disponibles = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String preferida : new String[]{"Segoe UI", "Inter", "Roboto", "Helvetica Neue", "DejaVu Sans"}) {
            if (Arrays.asList(disponibles).contains(preferida)) { return preferida; }
        }
        return Font.SANS_SERIF;
    }

    public static Font fuente(int estilo, float tamanio) {
        return new Font(FAMILIA, estilo, Math.round(tamanio));
    }

    public static Font normal(float tamanio) { return fuente(Font.PLAIN, tamanio); }
    public static Font negrita(float tamanio) { return fuente(Font.BOLD, tamanio); }

    /** Configura Swing (Nimbus) con los colores y la fuente del tema. Se llama una vez al iniciar. */
    public static void instalar() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception e) {
            // Si Nimbus no está, se usa el aspecto por defecto: la aplicación funciona igual.
        }
        UIManager.put("defaultFont", normal(14));
        UIManager.put("control", FONDO);
        UIManager.put("nimbusBase", AZUL);
        UIManager.put("nimbusBlueGrey", new Color(0xB8, 0xC2, 0xD0));
        UIManager.put("nimbusFocus", NARANJA);
        UIManager.put("nimbusSelectionBackground", AZUL_CLARO);
        UIManager.put("nimbusSelection", AZUL_CLARO);
        UIManager.put("textForeground", TEXTO);
        UIManager.put("text", TEXTO);
        UIManager.put("Table.alternateRowColor", new Color(0xF8, 0xFA, 0xFC));
        UIManager.put("OptionPane.background", BLANCO);
        UIManager.put("Panel.background", FONDO);
        UIManager.put("ToolTip.background", AZUL_OSCURO);
        UIManager.put("ToolTip.foreground", BLANCO);
    }

    // ─── Colores según el estado (se usan en las insignias y el calendario) ───

    public static Color colorEstado(EstadoOrden estado) {
        switch (estado) {
            case PENDIENTE: return AMBAR;
            case ASIGNADA: return AZUL_INFO;
            case EN_PROCESO: return VIOLETA;
            case FINALIZADA: return VERDE;
            default: return ROJO;   // CANCELADA
        }
    }

    public static Color colorPago(EstadoPago estado) {
        switch (estado) {
            case CONFIRMADO: return VERDE;
            case RECHAZADO: return ROJO;
            default: return AMBAR;
        }
    }

    /** Mezcla un color con blanco: 0.12 = fondo suave para insignias y tarjetas. */
    public static Color suave(Color color, float intensidad) {
        int r = Math.round(255 - (255 - color.getRed()) * intensidad);
        int g = Math.round(255 - (255 - color.getGreen()) * intensidad);
        int b = Math.round(255 - (255 - color.getBlue()) * intensidad);
        return new Color(r, g, b);
    }

    // ─── Formatos ─────────────────────────────────────────────────────────────

    /** $241.000 (formato colombiano, sin decimales). */
    public static String dinero(double valor) {
        return String.format(COLOMBIA, "$%,.0f", valor);
    }

    public static String nombreDia(DayOfWeek dia) {
        String nombre = dia.getDisplayName(TextStyle.FULL, COLOMBIA);
        return nombre.substring(0, 1).toUpperCase(COLOMBIA) + nombre.substring(1);
    }

    public static String diaCorto(DayOfWeek dia) {
        String nombre = dia.getDisplayName(TextStyle.SHORT, COLOMBIA).replace(".", "");
        return nombre.substring(0, 1).toUpperCase(COLOMBIA) + nombre.substring(1);
    }

    /** Texto de un estado como se muestra al usuario: "En proceso". */
    public static String texto(EstadoOrden estado) { return estado.getDescripcion(); }

    /** Escapa texto para mostrarlo dentro de una etiqueta HTML de Swing. */
    public static String html(String texto) {
        if (texto == null) { return ""; }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /** Activa el suavizado de bordes y texto al dibujar. */
    public static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }
}
