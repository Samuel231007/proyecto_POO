
import java.time.LocalDate;

public class Calificacion {

    private int puntaje;
    private String comentario;
    private LocalDate fecha;
    private Cliente cliente;

    // Constructor
    public Calificacion(int puntaje, String comentario,
                        LocalDate fecha, Cliente cliente) {
        this.puntaje = puntaje;
        this.comentario = comentario;
        this.fecha = fecha;
        this.cliente = cliente;
    }

    // Metodos
    public boolean validarPuntaje() {
        return puntaje >= 1 && puntaje <= 5;
    }

    // Setters & Getters
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getDocumentoCliente() {
        return cliente.getDocumento();
    }
}
