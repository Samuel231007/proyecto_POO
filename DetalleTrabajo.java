import java.util.ArrayList;
import java.util.List;

public class detalleTrabajo {
    private String descripcion;
    private double horasTrabajo;
    private List<String> actividades;
    private List<material> materiales;

    public detalleTrabajo() {
        this.actividades = new ArrayList<>();
        this.materiales = new ArrayList<>();
    }

    public void agregarActividad(String actividad) {
        actividades.add(actividad);
    }

    public void registrarMaterial(material material) {
        materiales.add(material);
    }

    public double calcularCostoManoObra(double tarifaHora) {
        // TODO: Implementar el calculo
        return 0.0;
    }

    public double calcularCostoMateriales() {
        // TODO: Implementar el calculo
        return 0.0;
    }

    public double calcularCostoTotal(double tarifaHora) {
        // TODO: Implementar el calculo
        return 0.0;
    }
}

