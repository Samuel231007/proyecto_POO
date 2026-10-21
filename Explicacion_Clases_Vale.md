# Parte de Valeria Alvarado · Orden de servicio

**Clases:** `OrdenServicio` (con la clase anidada `CambioEstado` y el enum anidado `Estado`), `Servicio` y `Garantia`\
**Requerimientos:** RF-05, RF-06, RF-16 y RF-18 (hoja *Tareas* del Excel); también apoya RF-07, RF-17, RF-19 y RNF-04\
**Decisiones relacionadas del registro:** Decisión 2 (Solicitud y OrdenServicio separadas), Decisión 5 (detalle y materiales), Decisión 7 (Pagable) y Decisión 9 (clases anidadas)

![Diagrama de clases de la parte de la orden de servicio](UML_OrdenServicio_Vale.png){width=6.5in}

## 1. Idea general

`OrdenServicio` es la clase central del proceso: reúne el servicio pedido, el técnico asignado, el estado, los costos, la garantía y los pagos. Cada vez que cambia de estado deja un registro (`CambioEstado`). Al finalizar, crea la `Garantia` del servicio con los días que define su tipo (`Servicio`). Como implementa `Pagable`, también recibe pagos y dice cuánto falta por pagar.

## 2. Clases, atributos y métodos

### `Servicio`

| Miembro | Acceso | Para qué sirve |
|---|---|---|
| `FACTOR_NORMAL: double` (**estático**, constante) | público | Factor de una tarifa normal (1.0); es igual para todos los servicios. |
| `tipo`, `factorTarifa`, `duracionEstimadaHoras`, `diasGarantia` | privados | Datos propios de cada tipo de servicio: nombre, recargo, duración esperada y días de garantía. |
| Constructor y setters | públicos | Validan que los datos no sean vacíos ni negativos. |
| `calcularManoObra(horas, tarifaBaseTecnico)` | público | Horas x tarifa base del técnico x factor del tipo (RF-05 y RF-16). |
| getters y `toString()` | públicos | Consultar los datos. |

### `Garantia`

| Miembro | Acceso | Para qué sirve |
|---|---|---|
| `fechaInicio`, `diasVigencia`, `cobertura` | privados (`final`) | Periodo y cobertura; no cambian después de crearla. |
| `revisiones: List<String>` | privado | Revisiones pedidas durante la vigencia (colección). |
| `getFechaFin()`, `estaVigente(fecha)` | públicos | Calculan y consultan la vigencia. |
| `solicitarRevision(fecha, problema)` | público | Registra una revisión solo si la garantía está vigente; si no, lanza una excepción con un mensaje claro (RF-18). |
| `getRevisiones()` | público | Devuelve la lista de solo lectura. |

### `OrdenServicio` (implementa `Pagable`)

| Miembro | Acceso | Para qué sirve |
|---|---|---|
| `consecutivo: int` (**estático**) | privado | Cuenta cuántos códigos de orden se han generado; lo comparten todas las órdenes. |
| `generarCodigo()` y `getTotalOrdenes()` (**estáticos**) | públicos | Generan el siguiente código ("OS-001") y consultan el contador. Son estáticos porque no dependen de una orden en particular. |
| `codigo`, `documentoCliente`, `documentoTecnico`, `tarifaBaseTecnico`, `servicio`, `estado`, `fechaCreacion`, `costoEstimado`, `costoFinal`, `garantia` | privados | Datos de la orden. |
| `cambios: List<CambioEstado>` y `pagos: List<Pago>` | privados | Colecciones con el historial de estados y los pagos. |
| `asignarTecnico`, `iniciarTrabajo`, `finalizar`, `cancelar` | públicos | Cambian el estado; todas pasan por `cambiarEstado` (RF-06). |
| `calcularCostoEstimado`, `calcularCostoFinal`, `compararConEstimado` | públicos | RF-05 y RF-16. |
| `crearAvisoUltimoCambio(...)` | público | Crea la `Notificacion` del último cambio de estado (RF-19); la envía un canal `Notificable`. |
| `solicitarRevisionGarantia(fecha, problema)` | público | Delega en `Garantia` (RF-18). |
| `registrarPago(pago)` y `getSaldoPendiente()` | públicos | Cumplen el contrato `Pagable` (RF-17). |
| `cambiarEstado`, `transicionPermitida`, `validarMateriales` | privados | Reglas internas: solo estas funciones modifican el estado. |

### `OrdenServicio.CambioEstado` (clase anidada estática)

Guarda `estadoAnterior`, `estadoNuevo`, `fecha` y `responsable` (todos privados y `final`). Tiene **constructor privado**, así que solo `OrdenServicio` puede crear cambios; no tiene setters. Es estática porque no necesita datos de la orden que la contiene.

### `OrdenServicio.Estado` (enum anidado)

`PENDIENTE`, `ASIGNADA`, `EN_PROCESO`, `FINALIZADA` y `CANCELADA`. Va dentro de la orden porque esos estados solo tienen sentido para ella. Usar un enum evita errores de escritura en un texto.

## 3. Relaciones (como van en el UML)

| Relación | Tipo | Explicación |
|---|---|---|
| `OrdenServicio` → `Pagable` | **Realización** (implementa la interfaz) | La orden cumple el contrato de recibir pagos y calcular el saldo. |
| `OrdenServicio` ⊕ `CambioEstado` | **Clase anidada** (composición, 1 a 0..*) | Los cambios no existen sin la orden. |
| `OrdenServicio` ⊕ `Estado` | **Enum anidado** | El enum vive dentro de la orden. |
| `OrdenServicio` → `Servicio` | **Asociación** (1 a 1) | Cada orden es de un tipo de servicio. |
| `OrdenServicio` ◆— `Garantia` | **Composición** (1 a 0..1) | La garantía nace al finalizar la orden y no existe sin ella. |
| `OrdenServicio` → `Pago` | **Asociación** (1 a 0..*) | La orden guarda varios pagos. |
| `Servicio` → `Garantia` | **Dependencia** | El servicio define los días de vigencia de la garantía. |
| `OrdenServicio` → `Notificacion` | **Dependencia** «crea» | Crea el aviso al cambiar de estado (punto de unión con la parte de Juan David). |
| `Solicitud`, `Asignacion`, `DetalleTrabajo` y `Material` → `OrdenServicio` | **Dependencia / asociación** (en gris en el UML) | Son clases de otros compañeros que se conectan con la orden. |

## 4. Conceptos del curso y cómo se aplican

| Concepto | Dónde se aplica | Utilidad en el diseño |
|---|---|---|
| **Clases y objetos** | Cada orden, servicio y garantía es un objeto con su propio estado. | Cada orden lleva su historial, costos y garantía. |
| **Atributos y métodos estáticos** | `consecutivo`, `generarCodigo()`, `getTotalOrdenes()` y `Servicio.FACTOR_NORMAL`. | Lo común a toda la clase se escribe una sola vez. |
| **Clases anidadas** | `OrdenServicio.CambioEstado` y `OrdenServicio.Estado`. | Agrupan lo que solo tiene sentido dentro de la orden. |
| **Interfaces** | `Pagable`, implementada por `OrdenServicio`. | Separa "ser una orden" de "poder recibir pagos" (Decisión 7). |
| **Polimorfismo** | `registrarPago(Pago)` recibe un `Pago` que puede ser `Efectivo`, `Tarjeta` o `Transferencia`; la orden solo usa `estaConfirmado()` y `getMonto()`. Con `Notificable`, el aviso se envía por el canal que corresponda. | No hay `if` por tipo de pago ni `instanceof`. Se puede agregar otro medio de pago sin tocar `OrdenServicio`. |
| **Colecciones** | `ArrayList` de `CambioEstado`, `Pago` y revisiones. | Administran el historial y los pagos. |
| **Encapsulamiento** | Atributos privados, `cambiarEstado` privado, constructor privado de `CambioEstado`, listas de solo lectura. | Nadie cambia el estado ni el historial sin pasar por las reglas. |
| **Excepciones** | `IllegalArgumentException` y `IllegalStateException` con mensajes claros. | Por ejemplo: "No se puede finalizar una orden que no ha sido asignada." |

## 5. Cómo se cubre cada requerimiento

| Req. | Cómo se cumple | Depende de |
|---|---|---|
| **RF-05** Precio estimado | `calcularCostoEstimado`: mano de obra con la duración del servicio + materiales; cada tipo aplica su propio factor. | Tarifa del técnico (Valerie) y materiales (Sara). |
| **RF-06** Cambiar el estado | `cambiarEstado` valida las transiciones y registra cada cambio en `CambioEstado`; una orden no asignada no se finaliza. | Técnico y Administrador, que llaman los métodos. |
| **RF-16** Costo final | `calcularCostoFinal` usa las horas reales; `compararConEstimado` da la diferencia. | `DetalleTrabajo` y `Material` (Sara). |
| **RF-18** Revisión por garantía | `solicitarRevisionGarantia` delega en `Garantia`, que solo acepta revisiones dentro de la vigencia. | Cliente (Valeria Morales). |
| **RF-07, RF-17, RF-19 y RNF-04** | Garantía creada al finalizar; pagos por `Pagable`; aviso con `crearAvisoUltimoCambio`; historial en `CambioEstado`. | Samuel (`Pago`) y Juan David (`Notificacion`). |

## 6. Relación con las decisiones del registro

- **Decisión 2:** separa `Solicitud` de `OrdenServicio`; la orden conserva los datos de ejecución y estado.
- **Decisión 5:** el detalle de trabajo y los materiales no se guardan dentro de la orden, para que no concentre todo.
- **Decisión 7:** `Pagable` es una interfaz independiente de `Pago`.
- **Decisión 9:** `CambioEstado` es una clase anidada estática, sin rama propia.
- Decisiones nuevas de esta parte (`Decisiones_Vale.md`): `Servicio` guarda la tarifa y la garantía de cada tipo, `Garantia` es una clase propia, `CambioEstado` tiene constructor privado y los costos se calculan en la orden.

## 7. Código y supuestos

Las clases están en Java, sin paquetes, una por archivo, y compilan. Se probaron con `PruebaOrdenServicio` (que no se sube al repositorio). Para compilar se usan copias de `Pago`, `Efectivo`, `Pagable` y de las clases de notificaciones, que están en la carpeta `provisional_de_otros`.

Supuestos para revisar con el equipo:
- La orden guarda el documento del cliente y del técnico como texto, porque `Cliente`, `Tecnico` y `Solicitud` todavía no están en el repositorio; se cambia por objetos cuando estén.
- `Pagable` es provisional (`registrarPago` y `getSaldoPendiente`); si la de Samuel usa otros nombres, se ajusta `OrdenServicio`.
- El costo de materiales entra como un número; cuando exista `Material` se calcula desde ahí.
