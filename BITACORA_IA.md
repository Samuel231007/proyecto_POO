# Bitácora de uso de inteligencia artificial

Registro del uso de herramientas de IA durante el desarrollo del proyecto **Técnicos del Hogar a Domicilio**.

---

## Registro 1 · Descripción del problema y requerimientos

| Campo | Detalle |
|---|---|
| **Fecha** | 18/09/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo Claude Sonnet 5 |
| **Qué se le pidió** | Ayudar a estructurar la descripción del problema (contexto, actores, proceso actual, dolor e impacto) y el formato de los requerimientos de la Entrega 1, a partir de la idea de la plataforma de técnicos a domicilio propuesta por el equipo. |
| **Qué respondió** | Una propuesta de descripción del problema y una tabla con 12 requerimientos funcionales (RF-01 a RF-12) y 3 no funcionales (RNF-01 a RNF-03), cada uno con actor, prioridad y criterio de aceptación. |
| **Qué aceptamos** | La estructura de la descripción del problema y el formato de la tabla de requerimientos. |
| **Qué corregimos** | Dejamos 10 requerimientos funcionales y 1 no funcional. Quitamos el registro de materiales como requerimiento aparte y el límite diario de órdenes por técnico. Agregamos el documento de identificación y el horario disponible del técnico, el horario disponible en la solicitud del cliente y la tarifa base del técnico en el cálculo del precio. |

---

## Registro 2 · Priorización y orden de implementación de requerimientos

| Campo | Detalle |
|---|---|
| **Fecha** | 23/09/2026 |
| **Herramienta y modelo** | ChatGPT (OpenAI) · modelo GPT-5.6 Luna |
| **Qué se le pidió** | Revisar la tabla de trazabilidad, identificar si alguno tenía mayor prioridad y reorganizar los requisitos de acuerdo con su dependencia y orden lógico de implementación. |
| **Qué respondió** | Propuso reorganizar los requisitos desde las funcionalidades base (registro de técnicos y clientes), pasando por solicitudes, asignación y gestión de órdenes, hasta garantías, calificaciones y consultas. |
| **Qué aceptamos** | La reorganización general por prioridad, ya que permite desarrollar primero las funcionalidades que sirven como base para las demás. |
| **Qué corregimos** | Revisamos el orden de las prioridades y lo cambiamos para que tuviera más sentido. Pusimos primero las funciones que son necesarias para poder realizar las demás, como registrar técnicos y clientes. Después organizamos las funciones que dependen de esas, como solicitar y asignar un servicio, y al final dejamos las consultas y las funciones que se realizan cuando el servicio ya terminó. |

---

---

## Registro 3 · Validaciones de pago con excepciones

| Campo | Detalle |
|---|---|
| **Fecha** | 16/10/2026 |
| **Herramienta y modelo** | Codex (OpenAI) |
| **Qué se le pidió** | Añadir excepciones al código para impedir pagos con monto nulo o negativo, fechas nulas y referencias de autorización vacías en los pagos con tarjeta o transferencia. |
| **Qué respondió** | Propuso validar esos datos en los constructores y setters de las clases de pago, lanzando una excepción cuando el valor no sea válido. También aclaró que `double`, el tipo elegido para el monto, no puede ser nulo; para detectar un monto nulo habría que usar `Double`. |
| **Qué aceptamos** | Añadir validaciones para evitar montos negativos, fechas nulas y referencias de autorización vacías antes de guardar o actualizar un pago. |
| **Qué corregimos** | Como se decidió mantener el monto como `double`, se aclaró que no aplica una comprobación de nulidad para ese atributo. Se valida que el monto sea mayor que cero; si se necesita distinguir entre un monto nulo y uno informado, habría que cambiar su tipo a `Double`. |

---


## Registro 4 · Organización de las clases de notificaciones

| Campo | Detalle |
|---|---|
| **Fecha** | 16/10/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo claude-opus-5-5 |
| **Qué se le pidió** | Ayuda para organizar las clases `Notificacion`, `Notificable`, `NotificacionWhatsApp` y `NotificacionCorreo` según el Excel de requerimientos y lo que pide el profesor: qué hace cada clase, cómo se relacionan y qué decisión de diseño las justifica. |
| **Qué respondió** | Propuso separar `Notificacion` (el registro del aviso) de `Notificable` (interfaz con `enviar`), con WhatsApp y Correo como implementaciones, y explicó por qué interfaz y no herencia. Propuso atributos privados, un contador estático, el enum anidado `EstadoEnvio` y las relaciones del UML. Redactó un borrador de la Decisión 8 y de la trazabilidad de RF-19, RNF-04 y RNF-06. |
| **Qué aceptamos** | La estructura de las cuatro clases con la interfaz `Notificable`, la Decisión 8 del registro de decisiones y el código de cada clase en su rama. |
| **Qué corregimos** | La explicación escrita salió muy larga y se acortó de unas 1400 a unas 430 palabras para que quedara con una extensión similar a la de los demás integrantes. |

---


## Registro 5 · Revisión y coherencia de las clases de pago y detalle del servicio

| Campo | Detalle |
|---|---|
| **Fecha** | 19/10/2026 |
| **Herramienta y modelo** | ChatGPT (OpenAI) · modelo GPT-6 |
| **Qué se le pidió** | Revisar el UML y la planificación de las clases `Pagable`, `DetalleTrabajo`, `Calificacion` y `Material`, contrastándolas con algunas clases ya desarrolladas y la documentación existente del proyecto. Ayudar a mantener nombres de atributos y métodos coherentes con el resto del documento  |
| **Qué respondió** | Propuso ajustes en los nombres de algunos atributos para mejorar la claridad y la coherencia del modelo. Se revisó el uso de `monto` en la clase relacionada con los pagos, propuso cambiarlo a `pago` y se planteó el cambio de `horas` a `horasReales` en `DetalleTrabajo`. |
| **Qué aceptamos** | Se aceptó el uso de `pago` en lugar de `monto` para nombrar el atributo correspondiente al valor monetario. También se aceptó cambiar el nombre relacionado con las horas, pero se eligió `horasTrabajo` en lugar de `horasReales`, ya que describe de forma más clara el tiempo dedicado al trabajo y evita confusiones con otra definición de horas existente en una clase diferente. |
| **Qué corregimos** | Se ajustó la propuesta para conservar la coherencia con el modelo existente. En lugar de aplicar directamente todos los nombres sugeridos, se priorizó que cada atributo expresara claramente su propósito y no se confundiera con conceptos definidos en otras clases. |

---


## Registro 6 · Revisión diagrama de clases y organización para mayor claridad
| Campo | Detalle |
|---|---|
| **Fecha**| 20/10/2026 |
| **Herramienta**| Lucidchart AI |
| **¿Qué se le pidió?** | Se pidió apoyo para revisar la claridad y el orden del diagrama UML, haciendo énfasis en los tipos de relaciones, las multiplicidades y la diferencia entre asociación, composición, herencia e implementación de interfaces. |
| **¿Qué respondió?** | La herramienta generó una propuesta de diagrama UML más organizada y legible, con las clases conectadas, incluyendo relaciones de asociación, herencia, composición e implementación de interfaces, además de las multiplicidades. |
| **¿Qué aceptamos?** | Aceptamos las sugerencias que ayudaron a mejorar la legibilidad del diagrama y a identificar con mayor claridad las relaciones entre las clases y su organización. |
| **¿Qué corregimos?** | Corregimos las relaciones que no representaban las reglas del negocio. En particular, revisamos las clases vinculadas a `OrdenServicio` y la representación de las clases anidadas y estáticas para asegurar que su estructura correspondiera con el diseño definido por el equipo. |


---

*Se irán agregando nuevos registros a medida que el equipo use IA en el desarrollo del proyecto (diseño de clases, generación de código, documentación, etc.), cada uno con su fecha, herramienta, lo pedido, lo respondido, lo aceptado y lo corregido.*
