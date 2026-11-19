package interfaces;

import java.util.ArrayList;

// Contrato CRUD de los repositorios principales: convierten objetos en líneas de texto y al revés.
public interface IRepository<T> {
    // Lee el archivo y devuelve todos los objetos.
    ArrayList<T> getDataList();

    // Agrega un objeto nuevo al archivo.
    void insertData(T entity);

    // Reemplaza en el archivo los datos de un objeto.
    void updateData(T oldEntity, T newEntity);

    // Elimina el objeto del archivo.
    void deleteData(T entity);

    // Convierte el objeto en la línea de texto que se guarda.
    String getDataLine(T entity);
}
