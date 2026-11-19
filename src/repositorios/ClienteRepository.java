package repositorios;

import interfaces.IRepository;
import interfaces.Notificable;
import modelos.Cliente;
import modelos.NotificacionCorreo;
import modelos.NotificacionWhatsApp;

import java.util.ArrayList;

// Formato: documento|nombre|correo|telefono|contraseniaCifrada|ubicacion|canal
public class ClienteRepository extends BaseRepository implements IRepository<Cliente> {
    public ClienteRepository() {
        super("clientes.txt", "|");
    }

    @Override
    public ArrayList<Cliente> getDataList() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        for (String linea : fileManage.getDataOfFile()) {
            String[] c = separar(linea);
            Notificable canal = crearCanal(c[6]);
            clientes.add(new Cliente(c[5], c[1], c[0], c[3], c[2], c[4], true, canal));
        }
        return clientes;
    }

    @Override
    public void insertData(Cliente cliente) {
        fileManage.writeFile(getDataLine(cliente));
    }

    @Override
    public void updateData(Cliente oldCliente, Cliente newCliente) {
        guardarLinea(oldCliente.getDocumento(), getDataLine(newCliente));
    }

    @Override
    public void deleteData(Cliente cliente) {
        eliminarPorClave(cliente.getDocumento());
    }

    @Override
    public String getDataLine(Cliente c) {
        return unir(c.getDocumento(), c.getNombre(), c.getCorreo(), c.getTelefono(), c.getContraseniaCifrada(),
                c.getUbicacion(), c.getCanalNotificacion().getNombreCanal());
    }

    public Cliente buscarPorDocumento(String documento) {
        for (Cliente c : getDataList()) {
            if (c.getDocumento().equals(documento)) { return c; }
        }
        return null;
    }

    private Notificable crearCanal(String nombre) {
        if ("Correo".equalsIgnoreCase(nombre)) { return new NotificacionCorreo(); }
        return new NotificacionWhatsApp();
    }
}
