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

## Decisión 3 · DisponibilidadTecnico y sus intervalos

**Decisión**

Crear DisponibilidadTecnico para registrar cuándo puede atender un técnico y sus bloques de indisponibilidad. La clase anidada estática FranjaHoraria representa cada intervalo.

**Alternativa considerada**

Guardar todos los intervalos como atributos separados de Tecnico.

**Por qué**

Un técnico puede tener varios intervalos. Representarlos como objetos facilita registrar y consultar cada bloque sin acumular atributos repetidos en Tecnico.

**Consecuencia**

FranjaHoraria se define dentro de DisponibilidadTecnico y no tiene una rama independiente. Esta estructura apoya RF-04 y RF-13.

---

## Decisión 4 · Asignacion conserva los cambios de técnico

**Decisión**

Crear Asignacion para relacionar una orden con un técnico y conservar las asignaciones anteriores cuando se reasigna una orden.

**Alternativa considerada**

Guardar únicamente el técnico actual dentro de OrdenServicio.

**Por qué**

Si solo se conserva el técnico actual, se pierde el historial de reasignaciones.

**Consecuencia**

OrdenServicio mantiene la asignación vigente y Asignacion registra los cambios. Esta decisión apoya RF-14 y RNF-04.

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

**Alternativa considerada**

Hacer que Notificacion almacene los datos y también contenga toda la lógica de envío para cada canal.

**Por qué**

Separar el registro del aviso y el mecanismo de envío evita mezclar responsabilidades y permite agregar canales.

**Consecuencia**

Los canales implementan el mismo contrato y pueden usarse polimórficamente. Esta decisión apoya RF-19 y RNF-06.

---

## Decisión 9 · Clases anidadas relacionadas con su clase contenedora

**Decisión**

Mantener FranjaHoraria dentro de DisponibilidadTecnico y CambioEstado dentro de OrdenServicio como clases anidadas estáticas.

**Por qué**

Cada clase anidada representa un concepto estrechamente relacionado con su clase contenedora y no requiere una rama independiente.

**Consecuencia**

Las clases se implementan en los archivos de sus contenedoras. FranjaHoraria apoya RF-13 y CambioEstado apoya RNF-04.