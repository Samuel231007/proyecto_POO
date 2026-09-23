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

*Se irán agregando nuevos registros a medida que el equipo use IA en el desarrollo del proyecto (diseño de clases, generación de código, documentación, etc.), cada uno con su fecha, herramienta, lo pedido, lo respondido, lo aceptado y lo corregido.*
