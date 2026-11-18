package modelos;


import java.util.ArrayList;
import java.util.List;

/** Detalle del trabajo hecho: descripción, horas reales, actividades y materiales usados. RF-15 y RF-16. */
public class DetalleTrabajo {
    private String descripcion;
    private double horasTrabajo;
    private List<String> actividades = new ArrayList<>();
    private List<Material> materiales = new ArrayList<>();

    public void agregarActividad(String actividad) { actividades.add(Validar.texto(actividad, "actividad")); }

    public void registrarMaterial(Material material) {
        Validar.objeto(material, "material");
        materiales.add(material);
    }

    public void setDescripcion(String descripcion) { this.descripcion = Validar.texto(descripcion, "descripción del trabajo"); }
    public void setHorasTrabajo(double horasTrabajo) { this.horasTrabajo = Validar.positivo(horasTrabajo, "horas reales"); }
    public String getDescripcion() { return descripcion; }
    public double getHorasTrabajo() { return horasTrabajo; }
    public List<String> getActividades() { return new ArrayList<>(actividades); }
    public List<Material> getMateriales() { return new ArrayList<>(materiales); }

    // RF-15: el detalle debe tener descripción y horas reales antes de asociarse a la orden.
    public void validarCompleto() {
        if (descripcion == null) { throw new IllegalStateException("Falta la descripción del trabajo realizado."); }
        if (horasTrabajo <= 0) { throw new IllegalStateException("Faltan las horas reales de trabajo."); }
    }

    // Costo de materiales que usa OrdenServicio para el costo final (RF-16).
    public double calcularCostoMateriales() {
        double total = 0;
        for (Material m : materiales) { total += m.calcularSubtotal(); }
        return total;
    }

    // Clase anidada: un material con su costo y cantidad.
    public static class Material {
        private final String descripcion;
        private final double costoUnitario;
        private final double cantidad;

        public Material(String descripcion, double costoUnitario, double cantidad) {
            this.descripcion = Validar.texto(descripcion, "descripción del material");
            if (costoUnitario < 0) { throw new IllegalArgumentException("El costo unitario no puede ser negativo."); }
            this.costoUnitario = costoUnitario;
            this.cantidad = Validar.positivo(cantidad, "cantidad");
        }

        public String getDescripcion() { return descripcion; }
        public double getCostoUnitario() { return costoUnitario; }
        public double getCantidad() { return cantidad; }
        public double calcularSubtotal() { return costoUnitario * cantidad; }

        @Override
        public String toString() { return descripcion + " x" + cantidad + " = " + String.format("$%,.0f", calcularSubtotal()); }
    }
}
