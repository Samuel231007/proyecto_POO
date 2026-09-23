# Registro de decisiones de POO

Registro de decisiones de diseño orientado a objetos tomadas para el proyecto **Técnicos del Hogar a Domicilio**.

---

## Decisión 1 · Tipos de servicio con herencia

| Campo | Detalle |
|---|---|
| **Decisión** | Crear una clase abstracta `TipoServicio` con las subclases `Electricidad`, `Plomeria` y `Pintura`. Cada subclase define cómo se calcula su precio y cuánto dura su garantía. |
| **Alternativas consideradas** | 1) Una sola clase `Servicio` con un atributo `tipo` (texto) y condicionales `if`/`switch` para calcular el precio y la garantía.<br>2) Una interfaz (por ejemplo, `Cotizable`) implementada por cada tipo de servicio. |
| **Por qué** | Todos los tipos de servicio comparten datos (nombre, tarifa base), pero se comportan distinto en dos aspectos: el cálculo del precio (RF-05) y el periodo de garantía (RF-07). La clase abstracta permite escribir los datos comunes una sola vez y que cada tipo sobreescriba lo que cambia. Usar un atributo `tipo` con cadenas de `if` es una práctica penalizada, y una interfaz no permite compartir atributos. |
| **Consecuencia** | Agregar un nuevo tipo de servicio (por ejemplo, `Cerrajeria`) solo requiere crear una nueva subclase, sin modificar la clase `OrdenServicio`. Cada subclase queda obligada a implementar los métodos `calcularPrecio()` y `getDiasGarantia()`. Esta decisión prepara el uso de herencia (Entrega 2) y de polimorfismo (Entrega 3). |

---

## Decisión 2 · Estado de la orden encapsulado

| Campo | Detalle |
|---|---|
| **Decisión** | El estado de una `OrdenServicio` es un atributo privado que solo puede cambiar mediante métodos con reglas: `asignar(tecnico)`, `iniciar()`, `finalizar()` y `cancelar()`. |
| **Alternativas consideradas** | 1) Un atributo público `estado`.<br>2) Un método `setEstado(String)` sin validación.<br>3) Validar los cambios de estado desde la interfaz gráfica. |
| **Por qué** | RF-06 exige que no se pueda finalizar una orden que no ha sido asignada, y RF-10 que solo se califiquen órdenes finalizadas. Si cualquier parte del programa puede cambiar el estado libremente, esas reglas se pueden romper. Poner la regla dentro de la clase aplica el encapsulamiento y mantiene las validaciones en la lógica de negocio y no en la vista. |
| **Consecuencia** | La interfaz gráfica solo llamará a estos métodos, sin tomar decisiones sobre el estado. No existirá un `setEstado()` público. Se deberá definir cómo responder ante un cambio de estado inválido; en la Entrega 2 se manejará con excepciones y mensajes claros para el usuario. |
