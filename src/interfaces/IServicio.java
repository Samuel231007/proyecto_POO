package interfaces;

import java.util.ArrayList;

// Consultas comunes de los servicios (igual que en el proyecto base). El id es el documento del usuario.
public interface IServicio<T> {
    ArrayList<T> getDataList();

    ArrayList<T> buscarPorNombre(String nombre);

    T buscarPorId(String id);

    boolean existeEntidad(String id);

    void mostrarTodos();
}
