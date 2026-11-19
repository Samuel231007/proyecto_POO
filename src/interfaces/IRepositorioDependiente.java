package interfaces;

// Repositorio de datos que pertenecen a otro objeto (padre): los pagos de una orden, el horario de un técnico...
// Cada línea del archivo empieza con el identificador del padre.
public interface IRepositorioDependiente<P> {
    void guardarDe(P padre);

    void cargarEn(P padre);

    void eliminarDe(String idPadre);
}
