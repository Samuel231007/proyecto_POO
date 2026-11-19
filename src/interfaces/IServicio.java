package interfaces;

import java.util.ArrayList;

public interface IServicio<T> {
    ArrayList<T> getDataList();

    ArrayList<T> buscarPorNombre(String nombre);

    T buscarPorId(String id);

    boolean existeEntidad(String id);

    void mostrarTodos();
}
