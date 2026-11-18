package interfaces;

import java.util.ArrayList;

/**
 * Interfaz genérica: contrato de consulta para todos los servicios (igual que en el proyecto base).
 * Aquí el id es un String porque los usuarios se identifican por documento.
 *
 * @param <T> tipo de entidad que consulta el servicio
 */
public interface IServicio<T> {

    ArrayList<T> getDataList();

    ArrayList<T> buscarPorNombre(String nombre);

    T buscarPorId(String id);

    boolean existeEntidad(String id);

    void mostrarTodos();
}
