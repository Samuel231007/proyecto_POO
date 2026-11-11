
import java.util.ArrayList;
import java.util.List;

public class DetalleTrabajo {

    private String descripcion;
    private double horasTrabajo;
    private List<String> actividades;
    private List<Material> materiales;

    public DetalleTrabajo() {
        this.actividades = new ArrayList<>();
        this.materiales = new ArrayList<>();
    }

    // Metodos
    public void agregarActividad(String actividad) {
        actividades.add(actividad);
    }

    public void registrarMaterial(Material material) {
        materiales.add(material);
    }

    // Clase anidada Material
    public static class Material {

        private String descripcion;
        private double costoUnitario;
        private double cantidad;

        // Constructor
        public Material(String descripcion, double costoUnitario,
                        double cantidad) {
            this.descripcion = descripcion;
            this.costoUnitario = costoUnitario;
            this.cantidad = cantidad;
        }

        // Setters
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public void setCostoUnitario(double costoUnitario) {
            this.costoUnitario = costoUnitario;
        }

        public void setCantidad(double cantidad) {
            this.cantidad = cantidad;
        }
    }
}
