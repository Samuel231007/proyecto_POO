# Parte de Juan David Lara · Notificaciones

**Clases:** `Notificacion` (con el enum anidado `EstadoEnvio`), `Notificable` (interfaz), `NotificacionWhatsApp` y `NotificacionCorreo`\
**Requerimientos:** RF-19; también apoya RNF-04 y RNF-06\
**Decisiones relacionadas del registro:** Decisión 8 (Notificacion y Notificable tienen responsabilidades distintas) y Decisión 9 (clases anidadas)\
**Pruebas:** `PruebaNotificaciones.java`

*Cómo leer las tablas:* los atributos y métodos se escriben con su tipo de acceso (público o privado). Lo **estático** se marca con la palabra «estático». El enum anidado se nombra completo (`Notificacion.EstadoEnvio`) para que no se confunda con los enums de otras clases. Las relaciones entre las clases están explicadas en la sección 3.

## 1. Idea general

Cuando cambia el estado de una orden, el cliente debe enterarse (RF-19). Esta parte separa dos cosas: **guardar el aviso** y **enviarlo**.

- `Notificacion` es el registro del aviso: para quién es, qué dice, por qué canal salió y cómo terminó el envío. No envía nada por sí misma.
- `Notificable` es el contrato que cumple cualquier canal capaz de enviar un aviso.
- `NotificacionWhatsApp` y `NotificacionCorreo` son los dos canales. Cada uno revisa el dato de contacto que le corresponde y deja el aviso como enviado o fallido. El envío es simulado: no se conecta a WhatsApp ni a un servidor de correo.

## 2. Clases, atributos y métodos

### `Notificacion`

| Miembro | Acceso | Para qué sirve |
|---|---|---|
| `totalNotificaciones: int` (**estático**) | privado | Cuenta cuántos avisos se han creado; lo comparten todos y sirve para numerarlos. |
| `FORMATO_FECHA` (**estático**, constante) | privado | Formato en que se muestra la fecha en `toString()`. |
| `id`, `codigoOrden`, `documentoCliente`, `nombreCliente`, `telefonoCliente`, `correoCliente`, `mensaje`, `fecha`, `canal`, `estadoEnvio`, `detalle` | privados | Datos del aviso. No tienen setters, así el registro no se altera desde afuera. |
| Constructor | público | Crea el aviso en estado `PENDIENTE` con la fecha actual. Exige código de la orden, documento, nombre y mensaje; si falta alguno lanza `IllegalArgumentException`. |
| `crearMensajeCambioEstado(...)` (**estático**) | público | Arma el texto «Hola Ana, tu orden OS-001 cambió de PENDIENTE a ASIGNADA.» (RNF-06). Es estático porque no necesita los datos de un aviso en particular. |
| `getTotalNotificaciones()` (**estático**) | público | Consulta el contador. |
| `marcarEnviada(canal, detalle)` y `marcarFallida(canal, causa)` | públicos | Los usa el canal para dejar el resultado. Solo funcionan **una vez**; si el aviso ya fue procesado lanzan `IllegalStateException` (RNF-04). |
| `fueEnviada()` y los getters | públicos | Consultar el aviso y su resultado. |
| `toString()` | público | Resumen de una línea: número, orden, fecha, canal, estado y detalle. |
| `verificarPendiente()` y `validarTextoObligatorio(...)` | privados | Reglas internas del registro. |

### `Notificacion.EstadoEnvio` (enum anidado)

`PENDIENTE`, `ENVIADA` y `FALLIDA`. Va dentro de `Notificacion` porque esos estados solo tienen sentido para un aviso. Usar un enum evita errores de escritura en un texto.

### `Notificable` (interfaz)

| Método | Para qué sirve |
|---|---|
| `getNombreCanal(): String` | Devuelve el nombre del canal («WhatsApp» o «Correo»). |
| `enviar(Notificacion): boolean` | Envía el aviso de forma simulada, lo deja `ENVIADA` o `FALLIDA` y devuelve `true` si se pudo enviar. |

### `NotificacionWhatsApp` y `NotificacionCorreo`

| Clase | Qué revisa | Resultado |
|---|---|---|
| `NotificacionWhatsApp` | Que el celular tenga 10 dígitos, todos números, y empiece por 3. | Si es válido: `ENVIADA` con el número. Si no: `FALLIDA` con la causa explicada. |
| `NotificacionCorreo` | Que el correo tenga una `@` con algo antes, un punto después de la `@` (con al menos un carácter entre los dos) y que no termine en punto. También arma el asunto «Actualización de tu orden OS-001». | Si es válido: `ENVIADA` con el correo y el asunto. Si no: `FALLIDA` con la causa explicada. |

Ambas tienen una constante estática `NOMBRE_CANAL` y métodos privados para sus validaciones.

## 3. Relaciones entre las clases

| Relación | Tipo | Explicación |
|---|---|---|
| `NotificacionWhatsApp` → `Notificable` | **Realización** (implementa la interfaz) | Cumple el contrato de enviar un aviso. |
| `NotificacionCorreo` → `Notificable` | **Realización** (implementa la interfaz) | Igual que la anterior, por otro canal. |
| `Notificable` → `Notificacion` | **Dependencia** | `enviar` recibe el aviso y deja en él el resultado. |
| `Notificacion` ⊕ `EstadoEnvio` | **Enum anidado** | El enum vive dentro del aviso. |
| `OrdenServicio` → `Notificacion` | **Dependencia** «crea» | La orden crea el aviso del último cambio de estado (punto de unión con la parte de Valeria Alvarado). |
| `Cliente` → `Notificacion` | **Asociación** (pendiente) | El cliente recibe el aviso. Por ahora `Notificacion` guarda su documento, nombre, teléfono y correo como texto, porque `Cliente` todavía no está en `main`. |

## 4. Conceptos del curso y cómo se aplican

| Concepto | Dónde se aplica | Utilidad en el diseño |
|---|---|---|
| **Interfaces** | `Notificable`, implementada por los dos canales. | Separa «ser un aviso» de «saber enviar un aviso». |
| **Polimorfismo** | Quien envía llama `canal.enviar(aviso)` sin saber si es WhatsApp o correo. | No hay `if` por canal. Para agregar SMS bastaría una clase nueva que implemente `Notificable`. |
| **Atributos y métodos estáticos** | `totalNotificaciones`, `getTotalNotificaciones()`, `crearMensajeCambioEstado()` y `NOMBRE_CANAL`. | Lo común a toda la clase se escribe una sola vez. |
| **Clases anidadas** | `Notificacion.EstadoEnvio` (enum anidado). | Agrupa lo que solo tiene sentido dentro del aviso. |
| **Encapsulamiento** | Atributos privados, sin setters, y `marcarEnviada` / `marcarFallida` que funcionan una sola vez. | El registro del aviso no se altera después de procesarlo (RNF-04). |
| **Excepciones** | `IllegalArgumentException` para datos obligatorios vacíos e `IllegalStateException` si se procesa dos veces un aviso. | Los errores tienen mensajes claros. |

## 5. Por qué interfaz y no herencia

La Decisión 8 consideró hacer de `Notificacion` la clase padre de los canales, y la descartó. WhatsApp y correo no comparten datos entre sí: solo comparten una capacidad, saber enviar. Un canal no «es una» notificación, por eso la relación es un contrato (interfaz) y no una herencia. Además, separar el registro del aviso del mecanismo de envío evita mezclar responsabilidades.

## 6. Cómo se cubre cada requerimiento

| Req. | Cómo se cumple | Depende de |
|---|---|---|
| **RF-19** Avisar al cliente por WhatsApp o correo | `OrdenServicio.crearAvisoUltimoCambio` crea la `Notificacion`; el canal `Notificable` la envía. | `OrdenServicio` (Valeria Alvarado) y `Cliente`. |
| **RNF-04** Trazabilidad | El aviso no tiene setters y solo se puede procesar una vez; guarda canal, estado y detalle. | — |
| **RNF-06** Mensajes claros | `crearMensajeCambioEstado` arma el aviso en lenguaje claro y, si el envío falla, el detalle explica la causa. | — |

## 7. Pruebas y supuestos

`PruebaNotificaciones.java` prueba el registro del aviso, los dos canales y el polimorfismo con `Notificable`, sin necesitar otras clases del proyecto. Se compila y se ejecuta así:

```
javac Notificable.java Notificacion.java NotificacionWhatsApp.java NotificacionCorreo.java PruebaNotificaciones.java
java PruebaNotificaciones
```

Supuestos para revisar con el equipo:
- El envío es simulado: la validación del celular y del correo es sencilla (formato), no comprueba que el número o la cuenta existan.
- Los datos del cliente se guardan como texto hasta que la clase `Cliente` esté en `main`.
