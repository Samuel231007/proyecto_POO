package repositorios;

import interfaces.IRepository;
import modelos.Tecnico;

import java.util.ArrayList;

// Formato: documento|nombre|correo|telefono|contraseniaCifrada|especialidad|zonaCobertura|tarifaBase
public class TecnicoRepository extends BaseRepository implements IRepository<Tecnico> {
    private final DisponibilidadRepository disponibilidadRepository;

    public TecnicoRepository() {
        super("tecnicos.txt", "|");
        this.disponibilidadRepository = new DisponibilidadRepository();
    }

    @Override
    public ArrayList<Tecnico> getDataList() {
        ArrayList<Tecnico> tecnicos = new ArrayList<>();
        for (String linea : fileManage.getDataOfFile()) {
            String[] c = separar(linea);
            Tecnico tecnico = new Tecnico(c[1], c[0], c[2], c[3], c[4], c[5], c[6], Double.parseDouble(c[7]), true);
            disponibilidadRepository.cargarEn(tecnico);   // horario y bloqueos
            tecnicos.add(tecnico);
        }
        return tecnicos;
    }

    @Override
    public void insertData(Tecnico tecnico) {
        fileManage.writeFile(getDataLine(tecnico));
        disponibilidadRepository.guardarDe(tecnico);
    }

    @Override
    public void updateData(Tecnico oldTecnico, Tecnico newTecnico) {
        guardarLinea(oldTecnico.getDocumento(), getDataLine(newTecnico));
        disponibilidadRepository.guardarDe(newTecnico);
    }

    @Override
    public void deleteData(Tecnico tecnico) {
        eliminarPorClave(tecnico.getDocumento());
        disponibilidadRepository.eliminarDe(tecnico.getDocumento());
    }

    @Override
    public String getDataLine(Tecnico t) {
        return unir(t.getDocumento(), t.getNombre(), t.getCorreo(), t.getTelefono(), t.getContraseniaCifrada(),
                t.getEspecialidad(), t.getZonaCobertura(), t.getTarifaBase());
    }

    public Tecnico buscarPorDocumento(String documento) {
        for (Tecnico t : getDataList()) {
            if (t.getDocumento().equals(documento)) { return t; }
        }
        return null;
    }
}
