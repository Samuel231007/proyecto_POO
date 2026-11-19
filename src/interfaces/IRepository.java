package interfaces;

import java.util.ArrayList;

public interface IRepository<T> {
    ArrayList<T> getDataList();

    void insertData(T entity);

    void updateData(T oldEntity, T newEntity);

    void deleteData(T entity);

    String getDataLine(T entity);
}
