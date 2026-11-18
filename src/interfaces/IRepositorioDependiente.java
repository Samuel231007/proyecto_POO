package interfaces;

/**
 * Contrato de los repositorios de datos que NO existen solos, sino que pertenecen a otro objeto (su "padre").
 * Ejemplos: los pagos, cambios de estado y notificaciones pertenecen a una OrdenServicio;
 * las franjas de disponibilidad pertenecen a un Tecnico.
 *
 * Cada línea del archivo empieza con el identificador del padre (código de la orden o documento del técnico),
 * así se sabe a quién pertenece al volver a leerla.
 *
 * GENERICIDAD: P es el tipo del padre. IRepositorioDependiente<OrdenServicio>, IRepositorioDependiente<Tecnico>.
 * POLIMORFISMO: OrdenServicioRepository guarda una lista de estos repositorios y los recorre sin saber cuál es cuál.
 *
 * @param <P> tipo del objeto dueño de los datos
 */
public interface IRepositorioDependiente<P> {

    /** Reemplaza en el archivo todas las líneas de ese padre por sus datos actuales. */
    void guardarDe(P padre);

    /** Lee del archivo las líneas de ese padre y las vuelve a poner dentro del objeto. */
    void cargarEn(P padre);

    /** Borra del archivo todas las líneas de ese padre. */
    void eliminarDe(String idPadre);
}
