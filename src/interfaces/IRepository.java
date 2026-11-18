package interfaces;

import java.util.ArrayList;

/**
 * Interfaz genérica: contrato CRUD para los repositorios principales (igual que en el proyecto base).
 * IRepository<Cliente>, IRepository<Tecnico>, IRepository<OrdenServicio>...
 *
 * @param <T> tipo de entidad que maneja el repositorio
 */
public interface IRepository<T> {

    /** READ: devuelve todos los registros del archivo convertidos en objetos. */
    ArrayList<T> getDataList();

    /** CREATE: guarda un objeto nuevo en el archivo. */
    void insertData(T entity);

    /** UPDATE: reemplaza en el archivo los datos de oldEntity por los de newEntity. */
    void updateData(T oldEntity, T newEntity);

    /** DELETE: elimina el objeto del archivo. */
    void deleteData(T entity);

    /** Convierte el objeto en la línea de texto que se guarda en el archivo. */
    String getDataLine(T entity);
}
