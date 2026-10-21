# Estructura del proyecto

## 1. Conformación del equipo y roles

| Integrante | Rol inicial |
|---|---|
| Valeria Morales Quintero | Responsable del producto |
| Valerie Sofía Medieta | Responsable del producto |
| Valeria Alvarado | Responsable de diseño |
| Juan David Lara | Responsable de diseño |
| Sara Gómez | Responsable de calidad |
| Samuel Rusinque | Responsable de integración |

## 2. Repositorio

- **Enlace al repositorio:** [https://github.com/Samuel231007/proyecto_POO](https://github.com/Samuel231007/proyecto_POO)
- Se cuenta con el docente invitado agregado como colaborador.

---

## 3. Documentación del proyecto

La información del proyecto se organiza en el README, el registro de decisiones, la trazabilidad y la bitácora de uso de IA. La bitácora se mantiene como un documento independiente del registro de decisiones.

## 4. Contexto - TÉCNICOS DEL HOGAR A DOMICILIO

| ELEMENTO | DESCRIPCIÓN |
|---|---|
| **Contexto** | Actualmente, en Colombia se necesitan profesionales confiables para realizar servicios técnicos y reparaciones en el hogar, como electricidad, plomería y pintura. Esta búsqueda se realiza normalmente mediante recomendaciones informales de amigos y familiares. El objetivo de nuestra plataforma es formalizar este proceso y brindar mayor seguridad y confianza a los usuarios. |
| **Actores** | Cliente, Técnico, Administrador (dueño de la plataforma) y Sistema. |
| **Proceso actual** | Búsqueda informal de técnicos mediante recomendaciones personales. |
| **Dolor 1** | Puede que no haya técnicos disponibles y confiables en la zona requerida. |
| **Dolor 2** | La confiabilidad y seguridad del servicio se ven afectadas porque la contratación se realiza de manera informal, sin contratos ni garantías de tiempo y calidad. |
| **Dolor 3** | Los técnicos viven en constante incertidumbre al no tener acceso directo a los clientes que requieren sus servicios, por lo que dependen de su círculo cercano de contactos. |
| **Impacto técnico** | Formaliza la asignación y el seguimiento de las órdenes de servicio. |
| **Impacto económico** | Permite establecer tarifas claras y contempla la posibilidad de cobrar una comisión para la plataforma. |
| **Impacto social** | Formaliza el trabajo de técnicos independientes y genera mayor confianza en los clientes. |

## Cómo organizamos las ramas

Se trabaja con una rama por requisito y una rama por clase o interfaz de nivel superior. La rama main se usa para integrar el proyecto cuando las ramas estén listas.

### Ramas por requisito

| Rama | Requisito |
|---|---|
| main | Integración del proyecto |
| RF-01 | Registrar técnicos |
| RF-02 | Registrar clientes |
| RF-03 | Solicitar un servicio |
| RF-04 | Asignar un técnico disponible |
| RF-05 | Calcular el precio estimado del servicio |
| RF-06 | Cambiar el estado de una orden |
| RF-07 | Registrar la garantía del servicio |
| RF-08 | Consultar el historial de órdenes de un cliente |
| RF-09 | Consultar las órdenes activas de un técnico |
| RF-10 | Calificar el servicio recibido |
| RF-11 | Actualizar los datos del cliente |
| RF-12 | Cancelar una solicitud pendiente |
| RF-13 | Registrar la indisponibilidad de un técnico |
| RF-14 | Reasignar una orden |
| RF-15 | Registrar el trabajo realizado y los materiales usados |
| RF-16 | Calcular el costo final del servicio |
| RF-17 | Registrar pagos |
| RF-18 | Solicitar una revisión cubierta por la garantía |
| RF-19 | Notificar cambios de estado |
| RF-20 | Filtrar órdenes |
| RNF-01 | Conservar los datos entre ejecuciones |
| RNF-02 | Restringir el acceso según el rol |
| RNF-03 | Mantener la integridad de la información |
| RNF-04 | Conservar la trazabilidad de cambios y reasignaciones |
| RNF-05 | Proteger los datos personales y de pago |
| RNF-06 | Presentar mensajes claros |

Los nombres de las ramas de requisitos usan los identificadores RF y RNF en mayúsculas.

### Ramas por clase e interfaz

Cada clase o interfaz de nivel superior tiene una rama con el mismo nombre que su archivo Java. Las clases anidadas se trabajan en la rama de su clase contenedora y no tienen una rama independiente.

| Rama | Responsabilidad |
|---|---|
| Administrador | Gestiona técnicos y órdenes |
| Usuario | Clase abstracta con los datos comunes de las personas usuarias |
| Tecnico | Representa técnicos y sus datos de servicio |
| Cliente | Representa clientes y sus datos de contacto |
| Solicitud | Almacena la solicitud de servicio |
| OrdenServicio | Relaciona solicitud, técnico, estado, costo y pagos |
| Servicio | Representa el tipo de servicio |
| Garantia | Registra la cobertura y vigencia de garantía |
| Calificacion | Almacena la calificación del cliente |
| DisponibilidadTecnico | Registra los bloques de disponibilidad e indisponibilidad |
| Asignacion | Relaciona un técnico con una orden y conserva reasignaciones |
| DetalleTrabajo | Registra actividades y tiempo empleado |
| Material | Representa los materiales usados y sus costos |
| Notificacion | Almacena la información y el resultado de un aviso |
| Notificable | Interfaz para enviar una notificación |
| NotificacionWhatsApp | Implementa el envío de notificaciones por WhatsApp |
| NotificacionCorreo | Implementa el envío de notificaciones por correo |
| Pagable | Interfaz con las operaciones de pago de una orden |
| Pago | Clase abstracta con los datos y comportamientos comunes de un pago |
| Transferencia | Tipo de pago por transferencia |
| Efectivo | Tipo de pago en efectivo |
| Tarjeta | Tipo de pago con tarjeta |

DisponibilidadTecnico contiene la clase anidada estática FranjaHoraria. OrdenServicio contiene la clase anidada estática CambioEstado.

## Cómo trabajamos

Antes de empezar en una rama, se actualiza la referencia de main y se revisa el estado del repositorio. Cada integrante trabaja en la rama de su clase o requisito. Las ramas se integran en main cuando el trabajo correspondiente esté listo y revisado; al integrar, se corrigen los problemas antes de continuar.

## Commits

Los mensajes de commit son descriptivos y en español, en modo imperativo. Cuando aplique, incluyen el identificador del requisito; por ejemplo: RF-01: agrega validación de documento único en Tecnico.

## Buenas prácticas

- Usar git status antes y después de cada operación.
- Hacer commits pequeños y descriptivos.
- Mantener configurado .gitignore para archivos compilados y del IDE.
- Integrar a main cambios que compilen.
- Evitar push --force y no reescribir historial compartido.
