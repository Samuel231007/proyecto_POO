package ui;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Puente entre la interfaz gráfica y la lógica del proyecto.
 *
 * Los modelos y servicios del equipo informan con System.out.println ("Bloqueo registrado", "El técnico no es
 * de esa zona"...). Para no cambiar ese código, aquí se "captura" lo que imprimen durante una acción y se
 * muestra en una ventana bonita. Si la acción lanza una excepción, se muestra su mensaje como error (RNF-06).
 */
public final class Acciones {

    /** Acción de la interfaz: devuelve true si se completó. */
    @FunctionalInterface
    public interface Accion {
        boolean ejecutar();
    }

    private Acciones() { }

    /**
     * Ejecuta la acción y muestra el resultado.
     * @param tituloExito título del mensaje de éxito; null = no mostrar nada si salió bien.
     * @return true si la acción se completó.
     */
    public static boolean ejecutar(Component padre, String tituloExito, Accion accion) {
        PrintStream original = System.out;
        ByteArrayOutputStream capturado = new ByteArrayOutputStream();
        boolean resultado;
        try {
            System.setOut(new PrintStream(capturado, true, StandardCharsets.UTF_8));
            resultado = accion.ejecutar();
        } catch (RuntimeException e) {
            System.setOut(original);
            mostrar(padre, Tipo.ERROR, "No se pudo completar", lineas(capturado, e.getMessage()));
            return false;
        } finally {
            System.setOut(original);
        }
        List<String> mensajes = lineas(capturado, null);
        System.out.print(capturado.toString(StandardCharsets.UTF_8));   // también queda en la consola
        if (!resultado) {
            if (mensajes.isEmpty()) { mensajes.add("La operación no se pudo completar."); }
            mostrar(padre, Tipo.ADVERTENCIA, "Revise la información", mensajes);
        } else if (tituloExito != null) {
            mostrar(padre, Tipo.EXITO, tituloExito, mensajes);
        }
        return resultado;
    }

    // Convierte lo capturado en líneas para mostrar; los avisos al cliente se presentan de forma amable.
    private static List<String> lineas(ByteArrayOutputStream capturado, String error) {
        List<String> resultado = new ArrayList<>();
        for (String linea : capturado.toString(StandardCharsets.UTF_8).split("\\R")) {
            String l = linea.trim();
            if (l.isEmpty()) { continue; }
            if (l.startsWith("Aviso al cliente -> ")) {
                String[] partes = l.split(" \\| ");
                String canal = partes.length > 3 ? partes[3] : "";
                String estado = partes.length > 4 ? partes[4] : "";
                l = "ENVIADA".equals(estado) ? "Aviso enviado al cliente por " + canal + "."
                        : "No se pudo avisar al cliente por " + canal + ": " + (partes.length > 5 ? partes[5] : "");
            }
            resultado.add(l);
        }
        if (error != null) { resultado.add(error); }
        return resultado;
    }

    // ─── Ventanas de mensaje ──────────────────────────────────────────────────

    public enum Tipo { EXITO, ERROR, ADVERTENCIA, INFO }

    public static void exito(Component padre, String titulo, String mensaje) { mostrar(padre, Tipo.EXITO, titulo, List.of(mensaje)); }
    public static void error(Component padre, String titulo, String mensaje) { mostrar(padre, Tipo.ERROR, titulo, List.of(mensaje)); }
    public static void info(Component padre, String titulo, String mensaje) { mostrar(padre, Tipo.INFO, titulo, List.of(mensaje)); }

    public static boolean confirmar(Component padre, String titulo, String mensaje) {
        return mostrarDialogo(padre, Tipo.ADVERTENCIA, titulo, List.of(mensaje), true);
    }

    public static void mostrar(Component padre, Tipo tipo, String titulo, List<String> mensajes) {
        mostrarDialogo(padre, tipo, titulo, mensajes, false);
    }

    private static boolean mostrarDialogo(Component padre, Tipo tipo, String titulo, List<String> mensajes, boolean conCancelar) {
        Color color;
        Icon icono;
        switch (tipo) {
            case EXITO: color = Tema.VERDE; icono = Iconos.check(26, Color.WHITE); break;
            case ERROR: color = Tema.ROJO; icono = Iconos.equis(26, Color.WHITE); break;
            case ADVERTENCIA: color = Tema.AMBAR; icono = Iconos.alerta(26, Color.WHITE); break;
            default: color = Tema.AZUL_INFO; icono = Iconos.campana(24, Color.WHITE);
        }
        Window ventana = padre == null ? null : SwingUtilities.getWindowAncestor(padre);
        JDialog dialogo = new JDialog(ventana, titulo, Dialog.ModalityType.APPLICATION_MODAL);
        boolean[] aceptado = {false};

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setBackground(Color.WHITE);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(24, 24, 16, 24));
        cuerpo.add(Componentes.circuloIcono(icono, color, 48), BorderLayout.WEST);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(Tema.negrita(17));
        lTitulo.setForeground(Tema.TEXTO);
        textos.add(lTitulo);
        textos.add(Box.createVerticalStrut(8));
        StringBuilder html = new StringBuilder("<html><body style='width:360px'>");
        for (String m : mensajes) {
            html.append("<p style='margin-bottom:5px'>").append(Tema.html(m).replace("\n", "<br>")).append("</p>");
        }
        JLabel lMensaje = new JLabel(html.append("</body></html>").toString());
        lMensaje.setFont(Tema.normal(13.5f));
        lMensaje.setForeground(Tema.TEXTO_SUAVE);
        textos.add(lMensaje);
        cuerpo.add(textos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        if (conCancelar) {
            Boton cancelar = new Boton("Cancelar", Boton.Tipo.SECUNDARIO);
            cancelar.addActionListener(e -> dialogo.dispose());
            botones.add(cancelar);
        }
        Boton ok = new Boton(conCancelar ? "Sí, continuar" : "Entendido", tipo == Tipo.ERROR ? Boton.Tipo.AZUL : Boton.Tipo.PRIMARIO);
        ok.addActionListener(e -> { aceptado[0] = true; dialogo.dispose(); });
        botones.add(ok);
        cuerpo.add(botones, BorderLayout.SOUTH);

        JPanel franja = new JPanel();
        franja.setBackground(color);
        franja.setPreferredSize(new Dimension(1, 5));
        dialogo.getContentPane().add(franja, BorderLayout.NORTH);
        dialogo.getContentPane().add(cuerpo, BorderLayout.CENTER);
        dialogo.getRootPane().setDefaultButton(ok);
        dialogo.pack();
        dialogo.setResizable(false);
        dialogo.setLocationRelativeTo(ventana);
        dialogo.setVisible(true);
        return aceptado[0];
    }
}
