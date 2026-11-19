package repositorios;

import interfaces.IRepository;
import modelos.Administrador;

import java.util.ArrayList;

// Formato: documento|nombre|correo|telefono|contraseniaCifrada|areaResponsable
public class AdministradorRepository extends BaseRepository implements IRepository<Administrador> {
    public AdministradorRepository() {
        super("administradores.txt", "|");
    }

    @Override
    public ArrayList<Administrador> getDataList() {
        ArrayList<Administrador> administradores = new ArrayList<>();
        for (String linea : fileManage.getDataOfFile()) {
            String[] c = separar(linea);
            // RNF-05: la contraseña se lee ya cifrada (true) y no se vuelve a cifrar.
            administradores.add(new Administrador(c[1], c[0], c[3], c[4], c[2], c[5], true));
        }
        return administradores;
    }

    @Override
    public void insertData(Administrador administrador) {
        fileManage.writeFile(getDataLine(administrador));
    }

    @Override
    public void updateData(Administrador oldAdministrador, Administrador newAdministrador) {
        guardarLinea(oldAdministrador.getDocumento(), getDataLine(newAdministrador));
    }

    @Override
    public void deleteData(Administrador administrador) {
        eliminarPorClave(administrador.getDocumento());
    }

    @Override
    public String getDataLine(Administrador a) {
        return unir(a.getDocumento(), a.getNombre(), a.getCorreo(), a.getTelefono(),
                a.getContraseniaCifrada(), a.getAreaResponsable());
    }

    public Administrador buscarPorDocumento(String documento) {
        for (Administrador a : getDataList()) {
            if (a.getDocumento().equals(documento)) { return a; }
        }
        return null;
    }
}
