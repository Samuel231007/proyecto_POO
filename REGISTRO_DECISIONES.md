# Registro de decisiones de POO

Proyecto integrador: plataforma de técnicos a domicilio.

---

## Decisión 1 · Usuario como clase abstracta

**Decisión**

Crear Usuario como clase abstracta con la información general común, como nombre y documento. Cliente, Tecnico y Administrador heredan de Usuario y agregan sus propios datos y acciones. Usuario no se instancia directamente.

**Alternativas consideradas**

1. Crear Cliente, Tecnico y Administrador como clases independientes y repetir los datos comunes.
2. Crear una sola clase Usuario con un atributo que indique el tipo de usuario.

**Por qué**

Las tres clases comparten información, pero representan roles distintos. La herencia evita repetir atributos y permite mantener las responsabilidades propias de cada rol.

**Consecuencia**

Los atributos comunes se definen una sola vez en Usuario. Las subclases reutilizan esos datos y añaden los suyos. En UML, Usuario se representa como abstracta y las otras tres clases heredan de ella. Esta decisión apoya RF-01, RF-02 y RNF-02.

---

## Decisión 2 · Solicitud y OrdenServicio como clases separadas

**Decisión**

Solicitud almacena el tipo de problema, la ubicación, la descripción y el horario disponible. OrdenServicio relaciona la solicitud con el técnico asignado y conserva los datos de ejecución y estado.

**Alternativas consideradas**

1. Guardar la solicitud, la asignación y el estado en una sola clase OrdenServicio.
2. Guardar la solicitud como atributos de Cliente.

**Por qué**

La solicitud existe antes de que se asigne un técnico. Separar ambas clases permite representar ese proceso y mantiene responsabilidades claras.

**Consecuencia**

OrdenServicio se asocia con Solicitud y Tecnico. También relaciona el servicio, la garantía, la calificación, los pagos y los cambios de estado. Esta decisión apoya RF-03, RF-04 y RF-05.

---

## Decisión 3 · DisponibilidadTecnico: separar los horarios de los datos del técnico

**Decisión**

Crear la clase `DisponibilidadTecnico` para registrar los horarios en los que un técnico puede trabajar y los bloques temporales en los que no puede recibir asignaciones. 

**Alternativa considerada**

1. Guardar todos los horarios disponibles y los bloqueos directamente como atributos de la clase `Tecnico`, sin separar la gestión de disponibilidad.
2. Registrar únicamente el horario habitual del técnico, sin contemplar los bloques de indisponibilidad temporal.

**Por qué**

Un técnico puede tener varios intervalos. Representarlos como objetos facilita registrar y consultar cada bloque sin acumular atributos repetidos en Tecnico. Se eligió separar esta responsabilidad para administrar los horarios y las indisponibilidades sin mezclar toda esa lógica con los datos personales y profesionales del técnico.

**Consecuencia**

Se relaciona por composición con `Tecnico` y contiene, también mediante composición, objetos de la clase `FranjaHoraria`. Por esta razón, no se considera completamente independiente de esas relaciones dentro del modelo. Cumple directamente con el **RF-13**, al permitir registrar bloques de indisponibilidad temporal, y apoya el **RF-04**, porque permite consultar la disponibilidad del técnico antes de asignarle un servicio.


---

## Decisión 4 · Asignacion: vincular órdenes y conservar el historial

**Decisión**

Crear la clase `Asignacion` para vincular un técnico con una orden de servicio. También permite representar la reasignación cuando el técnico inicialmente asignado no puede atenderla y conservar las asignaciones anteriores para mantener la trazabilidad.

**Alternativas consideradas**

1. Guardar el técnico asignado directamente en `OrdenServicio`, sin crear una clase independiente para gestionar las asignaciones y sus cambios.
2. Cambiar directamente el técnico asociado a la orden sin conservar un historial de las asignaciones anteriores.

**Por qué**

Se eligió crear una clase propia para centralizar la asignación de técnicos a las órdenes y facilitar el registro de las reasignaciones y de los técnicos anteriores.

**Consecuencia**

Se asocia con `Tecnico` y con `OrdenServicio`. No es completamente independiente, porque necesita relacionar ambas clases para cumplir su función. Apoya el **RF-04**, al representar la asignación del técnico al servicio; el **RF-14**, al permitir representar las reasignaciones; y el **RNF-04**, al conservar la trazabilidad de los cambios de asignación.

---

## Decisión 5 · DetalleTrabajo y Material registran la ejecución

**Decisión**

Crear DetalleTrabajo para registrar actividades y tiempo real. Crear Material para registrar los insumos usados, sus cantidades y costos.

**Alternativa considerada**

Guardar actividades, tiempo y materiales directamente en OrdenServicio.

**Por qué**

La orden puede tener varios detalles y materiales. Mantenerlos como objetos relacionados evita que OrdenServicio concentre todos esos datos.

**Consecuencia**

OrdenServicio reúne los detalles y materiales necesarios para calcular el costo final. Esta decisión apoya RF-15, RF-16 y RNF-03.

---

## Decisión 6 · Pago como clase abstracta y medios de pago como subclases

**Decisión**

Usar Pago como clase abstracta para reunir monto, fecha y estado. Transferencia, Efectivo y Tarjeta heredan de Pago y añaden solo la información propia de su medio.

**Alternativas consideradas**

1. Repetir monto, fecha y estado en las tres clases de pago.
2. Usar una interfaz Pago para compartir los atributos comunes.

**Por qué**

Las interfaces definen operaciones, pero no comparten atributos de instancia. La clase abstracta permite reutilizar los datos comunes y reducir duplicación. Cada subclase implementa getMetodoPago().

**Consecuencia**

Pago usa double para monto, LocalDate para fecha y String para estado. Además, ofrece confirmarPago(), rechazarPago() y estaConfirmado(). Transferencia almacena su referencia bancaria; Tarjeta almacena la referencia de autorización y no guarda el número completo ni el código de seguridad; Efectivo reutiliza los datos heredados. Esta decisión apoya RF-17, RNF-03 y RNF-05.

---

## Decisión 7 · Pagable es una interfaz independiente de Pago

**Decisión**

Mantener Pagable como interfaz con las operaciones que ofrece una orden para recibir pagos y consultar el saldo pendiente. OrdenServicio implementa Pagable.

**Alternativa considerada**

Usar Pago como interfaz y como contrato de la orden.

**Por qué**

Pago representa cada pago realizado; Pagable representa la capacidad de una orden para aceptar pagos. Son responsabilidades distintas.

**Consecuencia**

OrdenServicio implementa Pagable y los objetos Transferencia, Efectivo y Tarjeta heredan de Pago. Esta decisión apoya RF-17.

---

## Decisión 8 · Notificacion y Notificable tienen responsabilidades distintas

**Decisión**

Notificacion almacena el aviso, su destinatario, canal y resultado. Notificable define la operación para enviarlo. NotificacionWhatsApp y NotificacionCorreo implementan la interfaz.

**Alternativas consideradas**

1. Hacer que Notificacion almacene los datos y también contenga toda la lógica de envío para cada canal.
2. Hacer Notificacion clase padre de NotificacionWhatsApp y NotificacionCorreo (herencia) en lugar de usar una interfaz.

**Por qué**

Separar el registro del aviso y el mecanismo de envío evita mezclar responsabilidades y permite agregar canales. WhatsApp y correo no comparten datos entre sí, solo comparten una capacidad: saber enviar un aviso. Por eso la relación es un contrato (interfaz) y no una herencia: un canal no "es una" notificación.

**Consecuencia**

Los canales implementan el mismo contrato y pueden usarse polimórficamente: quien envía llama `canal.enviar(aviso)` sin `if` por canal, y para agregar otro canal (por ejemplo SMS) basta una clase nueva que implemente Notificable. Detalles de implementación:

- Notificable declara `getNombreCanal(): String` y `enviar(Notificacion): boolean`.
- El envío es simulado: NotificacionWhatsApp valida que el celular tenga 10 dígitos y empiece por 3, y NotificacionCorreo valida el correo y arma el asunto. Cada canal deja el aviso como ENVIADA o FALLIDA con el detalle o la causa (RNF-06).
- Notificacion guarda código de la orden, documento, nombre, teléfono y correo del cliente, mensaje, fecha (`LocalDateTime`), canal, estado y detalle, todos privados. No tiene setters: solo `marcarEnviada()` y `marcarFallida()`, que funcionan una sola vez para que el registro no se altere (RNF-04).
- `EstadoEnvio` (PENDIENTE, ENVIADA, FALLIDA) es un enum anidado dentro de Notificacion.
- Miembros estáticos: el contador `totalNotificaciones` (numera los avisos), `getTotalNotificaciones()`, `crearMensajeCambioEstado()` (arma el texto del aviso en lenguaje claro) y la constante `NOMBRE_CANAL` de cada canal.

Esta decisión apoya RF-19, RNF-04 y RNF-06.

---

## Decisión 9 · Clases anidadas relacionadas con su clase contenedora

**Decisión**

Mantener FranjaHoraria dentro de DisponibilidadTecnico y CambioEstado dentro de OrdenServicio como clases anidadas estáticas.

**Por qué**

Cada clase anidada representa un concepto estrechamente relacionado con su clase contenedora y no requiere una rama independiente.

**Consecuencia**

Las clases se implementan en los archivos de sus contenedoras. FranjaHoraria apoya RF-13 y CambioEstado apoya RNF-04.

---

## Decisión 10 · Clase Tecnico: heredar lo común y especializar el servicio

**Decisión**

Crear la clase `Tecnico` para representar a los técnicos que prestan los servicios. Contiene sus datos específicos, como especialidad, zona de cobertura, horario disponible y tarifa base. Además, hereda de la clase `Usuario` los datos personales y permite representar la recepción de solicitudes.

**Alternativas consideradas**

1. Crear una clase independiente que incluya todos los datos personales y los datos específicos del técnico, sin heredar de `Usuario`.
2. Incluir los datos personales y las características del técnico en una sola clase, sin utilizar herencia.

**Por qué**

Se eligió la herencia para reutilizar los datos de `Usuario` y mantener en `Tecnico` únicamente la información y las funciones propias de los técnicos.

**Consecuencia**

La clase hereda los atributos de `Usuario`, se relaciona por composición con `DisponibilidadTecnico` y por asociación con `Asignacion`. Apoya el **RF-01**, porque permite registrar los datos y características del técnico, y contribuye al **RF-04**, porque su especialidad, zona y disponibilidad sirven para determinar si puede atender un servicio solicitado.

---

## Decisión 11 · Clase FranjaHoraria: organizar cada intervalo como una unidad

**Decisión**

Crear la clase anidada estática `FranjaHoraria` dentro de `DisponibilidadTecnico` para representar un día específico y un intervalo horario mediante una hora de inicio y una hora de finalización. También permite validar los intervalos y comprobar si dos franjas se entrelazan.

**Alternativas consideradas**

1. Representar cada franja horaria mediante datos separados, como el día, la hora de inicio y la hora de finalización, directamente dentro de `DisponibilidadTecnico`, sin crear una clase específica.
2. Crear `FranjaHoraria` como una clase independiente, fuera de `DisponibilidadTecnico`.

**Por qué**

Se eligió una clase anidada para organizar cada intervalo horario como una unidad y facilitar la validación de los horarios y los bloques de indisponibilidad.

**Consecuencia**

Está relacionada con `DisponibilidadTecnico` mediante composición, por lo que cada franja forma parte de la disponibilidad que la contiene. Su función está vinculada a esa clase y apoya el **RF-13**, porque permite representar y validar los intervalos horarios en los que el técnico no puede recibir asignaciones.

---

### Decisión 12 · Utilizar herencia para Cliente, Tecnico y Administrador

**Decisión**

Se estableció una relación de herencia entre Usuario y sus tres clases derivadas: Cliente, Tecnico y Administrador.

**Por qué**

Aunque los tres tipos de usuario comparten información básica, cada uno cumple funciones diferentes dentro de la plataforma.

**Consecuencia**

Cada clase conserva los atributos y comportamientos generales de Usuario y añade los propios de su rol. Por ejemplo, Cliente gestiona solicitudes, Tecnico administra su disponibilidad y Administrador gestiona usuarios y órdenes de servicio.

---

### Decisión 13 · Asociar Cliente con Solicitud

**Decisión**

Se decidió asociar Cliente y Solicitud, de manera que un cliente puede crear varias solicitudes y cada solicitud pertenece a un cliente (multiplicidad).

**Por qué**

El sistema necesita identificar quién solicita un servicio y mantener relacionadas las solicitudes con la persona que las creó, además esto se complementa para el cumplimiento del RF-03 que exige que el cliente solicite un servicio y registre la información necesaria.

**Consecuencia**

La plataforma puede consultar las solicitudes de cada cliente, identificar al responsable de una solicitud y facilitar las operaciones como consultar.

---

### Decisión 14 · Incorporar la actualización de datos para el RF-11

**Decisión**

Se incluyó en Cliente la operación `actualizarDatos()` para modificar la información personal del cliente.

**Por qué**

Los datos de contacto o ubicación del cliente pueden cambiar y el sistema debe permitir que la información registrada se mantenga actualizada cada vez que sea necesario.

**Consecuencia**

El cliente puede actualizar sus datos sin necesidad de crear una cuenta nueva. La operación debe verificar que la información ingresada sea válida y guardar los cambios correctamente.

---

### Decisión 15 · Controlar la cancelación de solicitudes para el RF-12

**Decisión**

Se asignó a Solicitud la responsabilidad de comprobar si una petición puede cancelarse por medio de `validarCancelacion()` y `cancelar()`, esto de la mano de crear `EstadoSolicitud` con los estados posibles de la solicitud para consultas y validaciones de los métodos nombrados.

**Por qué**

La cancelación depende del estado en el que se encuentre la solicitud. No se permite que cualquier solicitud se cancele sin verificar las condiciones del servicio.

**Consecuencia**

El sistema puede impedir cancelaciones que no sean permitidas por las reglas del negocio. Por ejemplo, si las condiciones definidas permiten cancelar solo las solicitudes pendientes, la operación debe rechazar la cancelación cuando la solicitud ya esté asignada.

---

### Decisión 16 · Aplicar encapsulamiento y validación

**Decisión**

Se decidió mantener los atributos de las clases como privados y acceder a ellos mediante operaciones controladas cuando sea necesario en los métodos.

**Por qué**

Los datos de los usuarios y de las solicitudes no deben modificarse sin verificar las reglas correspondientes y con un objetivo específico.

**Consecuencia**

Se reduce el riesgo de cambios inconsistentes, se facilita la validación de la información y se tiene un mayor control sobre las operaciones de registro, actualización y cancelación.
