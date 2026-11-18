package modelos;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Clase padre de Cliente, Tecnico y Administrador. RF-01, RF-02, RNF-02 y RNF-05. */
public abstract class Usuario {
    private String nombre, correo, telefono;
    private final String documento;          // identifica al usuario: no cambia (RF-01 y RF-02 no permiten repetidos)
    private String contraseniaCifrada;       // RNF-05: la contraseña nunca se guarda en texto plano

    // RF-02: no se aceptan campos vacíos. Se valida todo antes de guardar nada (RNF-03).
    public Usuario(String nombre, String documento, String correo, String telefono, String contrasenia) {
        this(nombre, documento, correo, telefono, contrasenia, false);
    }

    // RNF-01 (sobrecarga de constructores): lo usan los repositorios al leer el archivo.
    // Si contraseniaYaCifrada es true, recibe el código SHA-256 que estaba guardado y no lo vuelve a cifrar.
    protected Usuario(String nombre, String documento, String correo, String telefono,
                      String contrasenia, boolean contraseniaYaCifrada) {
        this.nombre = Validar.texto(nombre, "nombre");
        this.documento = Validar.texto(documento, "documento");
        this.correo = Validar.texto(correo, "correo");
        this.telefono = Validar.texto(telefono, "teléfono");
        String clave = Validar.texto(contrasenia, "contraseña");
        this.contraseniaCifrada = contraseniaYaCifrada ? clave : cifrar(clave);
    }

    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    // No hay getContrasenia(): nadie puede leer la contraseña (RNF-05).

    // RNF-01 y RNF-05: devuelve solo el código SHA-256, nunca la contraseña real.
    // Lo necesita el repositorio para guardar el usuario en el archivo.
    public String getContraseniaCifrada() { return contraseniaCifrada; }

    // Polimorfismo: cada tipo de usuario dice cuál es su rol.
    public abstract String getRol();

    // Solo las clases hijas cambian estos datos, y siempre validados.
    protected void setNombre(String nombre) { this.nombre = Validar.texto(nombre, "nombre"); }
    protected void setCorreo(String correo) { this.correo = Validar.texto(correo, "correo"); }
    protected void setTelefono(String telefono) { this.telefono = Validar.texto(telefono, "teléfono"); }

    public boolean verificarContrasenia(String contrasenia) {
        return contrasenia != null && cifrar(contrasenia).equals(contraseniaCifrada);
    }

    public void cambiarContrasenia(String actual, String nueva) {
        if (!verificarContrasenia(actual)) { throw new IllegalArgumentException("La contraseña actual no es correcta."); }
        contraseniaCifrada = cifrar(Validar.texto(nueva, "contraseña nueva"));
        System.out.println("Contraseña actualizada correctamente.");
    }

    // RNF-02: solo entra quien conoce la contraseña.
    public boolean iniciarSesion(String contrasenia) {
        if (verificarContrasenia(contrasenia)) {
            System.out.println("Sesión iniciada correctamente. Bienvenido(a), " + nombre + ".");
            return true;
        }
        System.out.println("No se pudo iniciar sesión: la contraseña no es correcta.");
        return false;
    }

    public void cerrarSesion() { System.out.println("Sesión cerrada correctamente."); }

    // Convierte la contraseña en un código SHA-256 que no se puede revertir.
    private static String cifrar(String texto) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) { sb.append(String.format("%02x", b)); }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo proteger la contraseña.");
        }
    }

    @Override
    public String toString() { return getRol() + ": " + nombre + " (doc. " + documento + ")"; }
}
