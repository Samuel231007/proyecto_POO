Crea una presentación académica, clara y visual en español sobre el proyecto “Técnicos del hogar a domicilio”. La presentación debe explicar el proyecto completo: su propósito, funcionamiento, requisitos, clases, relaciones UML, pagos, notificaciones y decisiones de diseño.

Usa un estilo profesional y sencillo, adecuado para exponer en clase. Incluye diagramas, tablas y ejemplos visuales cuando ayuden a entender. No inventes datos, resultados, nombres de integrantes ni reglas del negocio que no aparezcan aquí. Si falta información, indícala como pendiente de confirmar.

Organiza la presentación en unas 12 a 15 diapositivas:

1. Portada
   - Proyecto: Técnicos del hogar a domicilio.
   - Deja espacios editables para los nombres de los seis integrantes, curso, docente y fecha.

2. Problema y propósito
   - Explica la necesidad de organizar solicitudes de servicios técnicos a domicilio.
   - Presenta el propósito del sistema: registrar solicitudes, asignar técnicos, hacer seguimiento a las órdenes y gestionar pagos, garantías, calificaciones y notificaciones.

3. Alcance y actores
   - Cliente: registra sus datos, solicita servicios, consulta información y califica la atención.
   - Técnico: cuenta con especialidad, zona de cobertura, tarifa y disponibilidad; atiende órdenes asignadas.
   - Administrador: registra técnicos y gestiona usuarios y órdenes.

4. Flujo general del sistema
   - Cliente crea una Solicitud.
   - La solicitud puede originar una OrdenServicio.
   - Se asigna un Técnico según disponibilidad.
   - La orden cambia de estado mientras se realiza el trabajo.
   - Se registra el trabajo y los materiales, se calcula el costo y se recibe el pago.
   - Al finalizar, puede registrarse una calificación y generarse una notificación.
   - Si aplica, la orden cuenta con garantía.

5. Requerimientos funcionales
   - Presenta los requisitos funcionales en una tabla con: ID, descripción, clases involucradas, temas del curso y estado.
   - Conserva los requisitos originales RF-01 a RF-10 si se proporcionan en los documentos del proyecto.
   - Los requisitos nuevos deben continuar desde RF-11.
   - Incluye funciones relacionadas con solicitudes, disponibilidad y asignación de técnicos, seguimiento de órdenes, costos, pagos, garantías, calificaciones y notificaciones.
   - Evita requisitos duplicados o que expresen la misma función con otras palabras.
   - No inventes descripciones exactas ni estados si no están incluidos en la información fuente.

6. Requerimientos no funcionales
   - Usa la misma estructura de tabla de la diapositiva anterior.
   - Conserva RNF-01 si aparece en los documentos del proyecto. Los nuevos deben comenzar en RNF-02.
   - Incluye solo condiciones de calidad o restricciones del sistema, como persistencia de la información, validación de datos, facilidad de mantenimiento y claridad de uso, siempre que estén respaldadas por la información entregada.
   - No presentes funciones del sistema como requisitos no funcionales.

7. Modelo de clases: usuarios, solicitudes y asignaciones
   Incluye estas clases y responsabilidades:
   - Usuario es abstracta y contiene los datos comunes: nombre, documento, teléfono, contraseña y correo.
   - Cliente y Administrador heredan de Usuario.
   - Técnico también hereda de Usuario y agrega especialidad, zona de cobertura, disponibilidad y tarifa base.
   - Solicitud registra el problema, ubicación, descripción, horario disponible, fecha y estado.
   - DisponibilidadTecnico administra las franjas disponibles y los bloqueos.
   - FranjaHoraria es una clase anidada estática de DisponibilidadTecnico.
   - Asignacion relaciona una orden con el técnico asignado y permite conservar el historial de asignaciones.
   - EstadoSolicitud es el enum asociado a Solicitud.

8. Modelo de clases: órdenes y trabajo
   - OrdenServicio es el centro del modelo. Incluye código, cliente, técnico, estado, costos, fecha, servicio, garantía, cambios de estado y pagos.
   - Servicio contiene el tipo, factor de tarifa, duración estimada y días de garantía.
   - DetalleTrabajo registra descripción, horas, actividades y materiales.
   - Material registra descripción, costo unitario y cantidad.
   - Garantia registra vigencia, cobertura y revisiones.
   - CambioEstado es una clase anidada estática de OrdenServicio.
   - EstadoOrden es un enum anidado en OrdenServicio con: PENDIENTE, ASIGNADA, EN_PROCESO, FINALIZADA y CANCELADA.

9. Modelo de clases: pagos
   - Pagable es una interfaz con las operaciones para registrar un pago y consultar el saldo pendiente.
   - OrdenServicio implementa Pagable.
   - Pago es abstracta y contiene monto, fecha y estado, además de operaciones para confirmar o rechazar el pago.
   - Tarjeta, Transferencia y Efectivo heredan de Pago.
   - Tarjeta guarda una referencia de autorización; Transferencia guarda una referencia bancaria.
   - EstadoPago es un enum anidado en Pago con: PENDIENTE, CONFIRMADO y RECHAZADO.
   - Explica que la herencia permite compartir los datos y comportamientos comunes del pago.

10. Modelo de clases: calificaciones y notificaciones
   - Calificacion guarda puntaje, comentario, fecha y documento del cliente.
   - Notificacion guarda los datos del aviso, su fecha, canal y estado de envío.
   - Notificable es una interfaz para consultar el canal y enviar una notificación.
   - NotificacionCorreo y NotificacionWhatsApp implementan Notificable.
   - EstadoEnvio es un enum anidado en Notificacion con: PENDIENTE, ENVIADA y FALLIDA.
   - No dupliques Notificacion y Notificable: Notificacion representa el aviso; Notificable define el comportamiento de los canales.

11. Diagrama UML completo
   - Genera un diagrama de clases legible, dividido visualmente por áreas si es necesario.
   - Muestra atributos y métodos principales, visibilidad UML (+ público, - privado, # protegido), herencia, interfaces, enums y clases anidadas.
   - Usa triángulos vacíos para la herencia y líneas discontinuas para la implementación de interfaces.
   - Añade las multiplicidades:
     * Cliente 1 — 0..* Solicitud.
     * Solicitud 1 — 0..1 OrdenServicio.
     * Técnico 1 — 1 DisponibilidadTecnico.
     * DisponibilidadTecnico 1 — 0..* FranjaHoraria.
     * Técnico 1 — 0..* Asignacion.
     * OrdenServicio 1 — 0..* Asignacion.
     * OrdenServicio 1 — 1 Servicio; Servicio 0..* — OrdenServicio.
     * OrdenServicio 1 — 0..1 Garantia.
     * OrdenServicio 1 — 0..* DetalleTrabajo.
     * DetalleTrabajo 1 — 0..* Material.
     * OrdenServicio 1 — 0..* CambioEstado.
     * OrdenServicio 1 — 0..1 Calificacion.
     * Cliente 1 — 0..* Calificacion.
     * OrdenServicio 1 — 0..* Pago.
     * OrdenServicio 1 — 0..* Notificacion.
     * Cliente 1 — 0..* Notificacion.
   - No agregues una asociación directa entre Cliente y OrdenServicio ni entre Técnico y OrdenServicio: esas relaciones pasan por Solicitud y Asignacion, respectivamente.
   - Muestra que OrdenServicio implementa Pagable y que NotificacionCorreo y NotificacionWhatsApp implementan Notificable.
   - Mantén el diagrama despejado; si no cabe, repártelo en varias diapositivas conectadas.

12. Decisiones de diseño orientado a objetos
   - Usuario es abstracta porque el sistema crea Cliente, Técnico o Administrador, no un Usuario genérico.
   - Pago es abstracta porque se crean pagos concretos mediante Tarjeta, Transferencia o Efectivo.
   - La herencia comparte atributos y comportamientos comunes y evita duplicación.
   - Pagable y Notificable son interfaces para definir comportamientos que pueden implementarse en distintas clases.
   - Las clases anidadas y enums deben mostrarse dentro de las clases contenedoras correspondientes.

13. Reglas de validación y excepciones
   - Explica las validaciones indicadas para evitar montos nulos o negativos, fechas nulas y referencias de autorización o transferencia vacías.
   - Preséntalas como reglas de validación del código, no como funciones nuevas del negocio.
   - No inventes excepciones específicas si no están definidas en el proyecto.

14. Trazabilidad y temas del curso
   - Relaciona requisitos y clases con temas de programación orientada a objetos: clases, objetos, encapsulamiento, constructores, getters y setters, herencia, abstracción, interfaces, composición/asociación, clases anidadas, enums y excepciones.
   - Usa solo temas que correspondan al diseño descrito.

15. Cierre
   - Resume el flujo principal y las decisiones de diseño.
   - Incluye una diapositiva final con conclusiones y espacios para preguntas.

Mantén consistencia en los nombres: usa “Tecnico” o “Técnico” de manera uniforme en cada diapositiva, y no cambies los nombres de las clases del modelo. Evita saturar las diapositivas: divide diagramas grandes y usa texto breve, claro y fácil de exponer.