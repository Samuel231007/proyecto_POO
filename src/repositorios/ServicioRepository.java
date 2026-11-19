package repositorios;

import interfaces.IRepository;
import modelos.Servicio;

import java.util.ArrayList;

// Formato: tipo|factorTarifa|duracionEstimadaHoras|diasGarantia
public class ServicioRepository extends BaseRepository implements IRepository<Servicio> {
    public ServicioRepository() {
        super("servicios.txt", "|");
    }

    @Override
    public ArrayList<Servicio> getDataList() {
        ArrayList<Servicio> servicios = new ArrayList<>();
        for (String linea : fileManage.getDataOfFile()) {
            String[] c = separar(linea);
            servicios.add(new Servicio(c[0], Double.parseDouble(c[1]), Double.parseDouble(c[2]), Integer.parseInt(c[3])));
        }
        return servicios;
    }

    @Override
    public void insertData(Servicio servicio) {
        fileManage.writeFile(getDataLine(servicio));
    }

    @Override
    public void updateData(Servicio oldServicio, Servicio newServicio) {
        guardarLinea(oldServicio.getTipo(), getDataLine(newServicio));
    }

    @Override
    public void deleteData(Servicio servicio) {
        eliminarPorClave(servicio.getTipo());
    }

    @Override
    public String getDataLine(Servicio s) {
        return unir(s.getTipo(), s.getFactorTarifa(), s.getDuracionEstimadaHoras(), s.getDiasGarantia());
    }

    public Servicio buscarPorTipo(String tipo) {
        for (Servicio s : getDataList()) {
            if (s.getTipo().equalsIgnoreCase(tipo)) { return s; }
        }
        return null;
    }
}
