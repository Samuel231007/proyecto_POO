package interfaces;

// Repositorio de datos que pertenecen a otro objeto (padre): los pagos de una orden, el horario de un técnico...
// Cada línea del archivo empieza con el identificador del padre.
public interface IRepositorioDependiente<P> {
    // Reemplaza en el archivo todas las líneas de ese padre por sus datos actuales.
    void guardarDe(P padre);

    // Lee las líneas de ese padre y las vuelve a poner dentro del objeto.
    void cargarEn(P padre);

    // Borra del archivo todas las líneas de ese padre.
    void eliminarDe(String idPadre);
}
