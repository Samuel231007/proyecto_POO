# Explicación de mis clases · Valeria Alvarado

Nivel de acceso: `-` privado, `+` público.

## Servicio
- **Para qué sirve:** representa un tipo de servicio con su tarifa, duración y garantía.
- **Atributos:** `- tipo: String`, `- factorTarifa: double`, `- duracionEstimadaHoras: double`, `- diasGarantia: int`.
- **Métodos:** `+ calcularManoObra(horas, tarifaBaseTecnico)`, getters y setters que validan.
- **Conceptos:** encapsulamiento (atributos privados y validación en los setters), constructor, comunicación con OrdenServicio.

## Garantia
- **Para qué sirve:** guarda la vigencia y la cobertura, y recibe solicitudes de revisión.
- **Atributos:** `- fechaInicio`, `- diasVigencia`, `- cobertura`, `- revisiones: List<String>`.
- **Métodos:** `+ getFechaFin()`, `+ estaVigente(fecha)`, `+ solicitarRevision(fecha, problema)`, `+ getRevisiones()` (lista de solo lectura).
- **Conceptos:** encapsulamiento, colecciones (ArrayList), excepciones (`IllegalStateException` si no está vigente).

## OrdenServicio (implementa Pagable)
- **Para qué sirve:** reúne el servicio, el técnico, el estado, los costos, la garantía y los pagos.
- **Atributos:** `- codigo`, `- documentoCliente`, `- documentoTecnico`, `- tarifaBaseTecnico`, `- estado: Estado`, `- costoEstimado`, `- costoFinal`, `- garantia`, `- cambios`, `- pagos`.
- **Métodos públicos:** `asignarTecnico`, `iniciarTrabajo`, `finalizar`, `cancelar`, `calcularCostoEstimado`, `calcularCostoFinal`, `compararConEstimado`, `solicitarRevisionGarantia`, `registrarPago`, `getSaldoPendiente`.
- **Método privado:** `- cambiarEstado` (valida y registra cada cambio) y `- transicionPermitida`.
- **Conceptos:** interfaz (Pagable), enum anidado (Estado), clase anidada (CambioEstado), colecciones, encapsulamiento y excepciones.

## OrdenServicio.CambioEstado (clase anidada estática)
- **Atributos:** `- estadoAnterior`, `- estadoNuevo`, `- fecha`, `- responsable` (todos `final`).
- **Métodos:** solo getters y `toString`. El constructor es privado.
- **Concepto:** clase anidada estática; solo lectura.

## Relaciones
| Origen | Destino | Tipo |
|---|---|---|
| OrdenServicio | Pagable | Realización de interfaz (implementa) |
| OrdenServicio | CambioEstado | Composición (1 a 0..*) |
| OrdenServicio | Garantia | Composición (1 a 0..1) |
| OrdenServicio | Servicio | Asociación (1 a 1) |
| OrdenServicio | Pago | Asociación (1 a 0..*) |
| Servicio | Garantia | Dependencia: aporta los días de vigencia |

## Cómo probarlo
Compilar todo junto y ejecutar `PruebaOrdenServicio`: `javac *.java` y luego `java PruebaOrdenServicio`. Para compilar se necesitan `Pago`, `Efectivo` y `Pagable` (de Samuel); en `provisional_de_otros` hay copias solo para poder compilar.

## Supuestos (para revisar con el equipo)
- `OrdenServicio` guarda el documento del cliente y del técnico como texto, porque las clases `Cliente`, `Tecnico` y `Solicitud` todavía no están en el repo. Cuando estén, se cambia por referencias a esos objetos.
- La interfaz `Pagable` provisional tiene `registrarPago(Pago)` y `getSaldoPendiente()`. Si la de Samuel usa otros nombres, se ajustan en `OrdenServicio`.
- Los costos de materiales se reciben como un número. Cuando exista `Material` se calcula desde ahí.
