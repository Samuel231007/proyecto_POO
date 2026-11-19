package modelos;

import java.time.LocalDateTime;

// Calificación del cliente: de 1 a 5 estrellas y un comentario opcional (RF-10).
public class Calificacion {
    private final int estrellas;
    private final String comentario;
    private final LocalDateTime fecha;

    public Calificacion(int estrellas, String comentario) {
        this(estrellas, comentario, LocalDateTime.now());
    }

    // RNF-01: el repositorio la reconstruye con la fecha en que se guardó.
    public Calificacion(int estrellas, String comentario, LocalDateTime fecha) {
        if (estrellas < 1 || estrellas > 5) { throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5."); }
        Validar.objeto(fecha, "fecha");
        this.estrellas = estrellas;
        this.comentario = (comentario == null) ? "" : Validar.sinSeparadores(comentario, "comentario").trim();
        this.fecha = fecha;
    }

    public int getEstrellas() { return estrellas; }
    public String getComentario() { return comentario; }
    public LocalDateTime getFecha() { return fecha; }

    @Override
    public String toString() { return estrellas + " estrella(s)" + (comentario.isEmpty() ? "" : ": \"" + comentario + "\""); }
}
