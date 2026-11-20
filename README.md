# Técnicos del Hogar a Domicilio

Proyecto académico para el curso de **Programación Orientada a Objetos (POO)**.

Plataforma que formaliza la contratación de técnicos para el hogar (plomería, electricidad, pintura, cerrajería, carpintería). El cliente solicita un servicio; el administrador asigna un técnico disponible de esa especialidad y zona; el técnico registra el trabajo; y el sistema calcula costos, garantía y pagos y le avisa al cliente de cada cambio de estado.

- **Repositorio:** [proyecto_POO](https://github.com/Samuel231007/proyecto_POO)
- **Diagrama UML:** [Ver diagrama de clases](https://lucid.app/lucidchart/6b8de022-7d2f-4c7f-ab6e-32428fa6780c/edit?viewport_loc=-4837%2C-1424%2C11332%2C5940%2C0_0&invitationId=inv_890ba847-9cd2-4821-b89e-546f47aec054)
- El docente está agregado como colaborador del repositorio.

---

## Tabla de contenido

1. [Equipo y roles](#equipo-y-roles)
2. [Dominio del problema](#dominio-del-problema)
3. [Requerimientos y trazabilidad](#requerimientos-y-trazabilidad)
4. [Conceptos POO demostrados](#conceptos-poo-demostrados)
5. [Arquitectura del sistema](#arquitectura-del-sistema)
6. [Estructura del proyecto](#estructura-del-proyecto)
7. [Jerarquía de clases](#jerarquía-de-clases)
8. [Formato de archivos de datos](#formato-de-archivos-de-datos)
9. [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
10. [ArrayList vs Archivo de texto](#arraylist-vs-archivo-de-texto)
11. [Reglas de negocio](#reglas-de-negocio)
12. [Registro de decisiones](#registro-de-decisiones)
13. [Bitácora de uso de inteligencia artificial](#bitácora-de-uso-de-inteligencia-artificial)
14. [Organización del repositorio](#organización-del-repositorio)

---

## Equipo y roles

| Integrante | Rol inicial |
|---|---|
| Valeria Morales Quintero | Responsable del producto |
| Valerie Sofía Medieta | Responsable del producto |
| Valeria Alvarado | Responsable de diseño |
| Juan David Lara | Responsable de diseño |
| Sara Gómez | Responsable de calidad |
| Samuel Rusinque | Responsable de integración |

---

## Dominio del problema

El sistema modela una **plataforma de servicios técnicos a domicilio**. Los principales actores y entidades son:

| Entidad | Descripción |
|---|---|
| `Cliente` | Usuario que solicita servicios. Tiene ubicación y elige si recibe los avisos por WhatsApp o por correo. |
| `Tecnico` | Usuario con especialidad, zona de cobertura y tarifa base por hora. Maneja su horario y sus bloqueos (`DisponibilidadTecnico`). |
| `Administrador` | Usuario que registra técnicos, asigna y reasigna órdenes, confirma pagos y filtra órdenes. |
| `Servicio` | Tipo de trabajo (Plomería, Electricidad...) con su factor de tarifa, duración estimada y días de garantía. |
| `Solicitud` | Lo que pide el cliente: tipo de problema, ubicación, descripción y horario en que puede recibir al técnico. |
| `OrdenServicio` | Clase central: une cliente, solicitud, servicio y técnico; maneja el estado, los costos, la garantía, la calificación, los pagos y los avisos. |
| `Asignacion` | Asigna y reasigna el técnico de una orden y conserva el historial. |
| `DetalleTrabajo` | Lo que hizo el técnico: descripción, horas reales, actividades y materiales. |
| `Garantia` | Periodo de vigencia del servicio terminado y revisiones solicitadas. |
| `Calificacion` | Estrellas (1 a 5) y comentario del cliente. |
| `Pago` | Pago de una orden: `Efectivo`, `Tarjeta` o `Transferencia`. |
| `Notificacion` | Aviso al cliente en cada cambio de estado, enviado por un canal `Notificable`. |

---

## Requerimientos y trazabilidad

Cada requerimiento funcional (RF) y no funcional (RNF) con las clases que lo cumplen y los temas del curso que usa:

| ID | Descripción | Clases Involucradas | Temas del curso | Estado |
|----|-------------|---------------------|-----------------|--------|
| RF-01 | Registrar técnicos con nombre, documento de identificación, especialidad, zona de cobertura, horario disponible y tarifa base | **Usuario**: clase abstracta con datos generales.<br>**Tecnico**: hereda de Usuario y agrega sus datos de servicio.<br>**Administrador**: hereda de Usuario y registra técnicos. | Clases abstractas, herencia y encapsulamiento | Implementado |
| RF-02 | Registrar clientes con datos de contacto y ubicación | **Usuario**: clase abstracta con información general.<br>**Cliente**: hereda de Usuario y agrega teléfono, correo y ubicación. | Clases abstractas, herencia y encapsulamiento | Implementado |
| RF-03 | Permitir al cliente solicitar un servicio indicando tipo de problema, ubicación, descripción y horario disponible | **Cliente**: realiza la solicitud.<br>**Solicitud**: almacena tipo de problema, ubicación, descripción y horario disponible. | Clases, objetos, constructores, getters y setters | Implementado |
| RF-04 | Asignar un técnico disponible según especialidad y zona del servicio solicitado | **Tecnico**: contiene especialidad, zona y tarifa.<br>**DisponibilidadTecnico**: aporta los intervalos disponibles.<br>**Solicitud**: contiene las características del servicio requerido.<br>**OrdenServicio**: relaciona solicitud y técnico. | Encapsulamiento, asociaciones y clases anidadas | Implementado |
| RF-05 | Calcular el precio estimado del servicio según tipo de servicio, duración, tarifa base del técnico y materiales | **OrdenServicio**: reúne la información del cálculo.<br>**Tecnico**: proporciona la tarifa base.<br>**Servicio**: contiene el tipo y duración estimada.<br>**DetalleTrabajo.Material**: aporta costos de insumos. | Métodos, encapsulamiento y asociaciones | Implementado |
| RF-06 | Cambiar el estado de una orden (pendiente, asignada, en proceso, finalizada, cancelada) | **OrdenServicio**: contiene el estado actual.<br>**OrdenServicio.CambioEstado**: registra los cambios.<br>**Tecnico** y **Administrador**: participan en la actualización según el proceso. | Clases anidadas, encapsulamiento y estados de objetos | Implementado |
| RF-07 | Registrar la garantía del servicio con su periodo de vigencia | **Garantia**: contiene periodo de vigencia y cobertura.<br>**OrdenServicio**: se relaciona con la garantía del servicio. | Encapsulamiento, asociaciones y composición | Implementado |
| RF-08 | Consultar el historial de órdenes de un cliente | **Cliente**: identifica al cliente que consulta.<br>**OrdenServicio**: almacena las órdenes realizadas. | Encapsulamiento, getters y asociaciones | Implementado |
| RF-09 | Consultar las órdenes activas asignadas a un técnico | **Tecnico**: identifica al técnico.<br>**OrdenServicio**: almacena el técnico asignado y el estado de cada orden. | Encapsulamiento, getters y asociaciones | Implementado |
| RF-10 | Permitir que el cliente califique el servicio recibido al finalizar la orden | **Cliente**: realiza la calificación.<br>**Calificacion**: almacena valoración y comentario.<br>**OrdenServicio**: relaciona la calificación con el servicio. | Encapsulamiento, asociaciones, getters y setters | Implementado |
| RF-11 | Actualizar los datos de contacto y ubicación de una cuenta de cliente | **Cliente**: conserva teléfono, correo y ubicación.<br>**Usuario**: aporta los datos generales heredados. | Herencia, encapsulamiento y setters | Implementado |
| RF-12 | Cancelar una solicitud mientras esté pendiente y no tenga un técnico asignado | **Cliente**: solicita la cancelación.<br>**Solicitud**: conserva la solicitud.<br>**OrdenServicio**: actualiza el estado cuando la solicitud ya originó una orden. | Asociaciones, encapsulamiento y estados de objetos | Implementado |
| RF-13 | Registrar bloques de indisponibilidad temporal de un técnico para excluirlos de nuevas asignaciones | **Tecnico**: registra cuándo no puede atender.<br>**DisponibilidadTecnico**: administra los bloques.<br>**DisponibilidadTecnico.FranjaHoraria**: representa cada intervalo. | Clases anidadas, encapsulamiento y asociaciones | Implementado |
| RF-14 | Reasignar una orden cuando el técnico asignado rechace el servicio o no pueda atenderlo antes de iniciar el trabajo | **Administrador**: inicia la reasignación.<br>**Asignacion**: conserva la asignación nueva y las anteriores.<br>**OrdenServicio**: actualiza el técnico responsable. | Clases, asociaciones y encapsulamiento | Implementado |
| RF-15 | Registrar las actividades realizadas, el tiempo real empleado y los materiales usados en una orden | **Tecnico**: registra el trabajo.<br>**OrdenServicio**: agrupa los detalles.<br>**DetalleTrabajo**: almacena actividades y tiempo.<br>**DetalleTrabajo.Material**: registra insumos y cantidades. | Clases, asociaciones, composición y encapsulamiento | Implementado |
| RF-16 | Calcular el costo final de la orden con el tiempo real y los materiales registrados, y compararlo con el estimado | **Servicio**: aporta tarifa y duración estimada.<br>**OrdenServicio**: conserva los costos estimado y final.<br>**DetalleTrabajo**: aporta tiempo real.<br>**DetalleTrabajo.Material**: aporta costos y cantidades. | Métodos, asociaciones y encapsulamiento | Implementado |
| RF-17 | Registrar pagos de una orden mediante transferencia, efectivo o tarjeta | **OrdenServicio**: recibe pagos y consulta el saldo (sin la interfaz Pagable, Decisión 17).<br>**Pago**: clase abstracta con datos comunes.<br>**Transferencia**, **Efectivo** y **Tarjeta**: heredan de Pago. | Clases abstractas, interfaces, herencia y polimorfismo | Implementado |
| RF-18 | Registrar una solicitud de revisión por un problema cubierto por la garantía de un servicio | **Cliente**: solicita la revisión.<br>**Garantia**: define cobertura y vigencia.<br>**OrdenServicio**: relaciona la revisión con el servicio realizado. | Asociaciones y encapsulamiento | Implementado |
| RF-19 | Enviar al cliente una notificación cuando cambie el estado de una orden, por WhatsApp o correo | **Notificacion**: conserva el aviso y su resultado; contador estático y enum EstadoEnvio.<br>**Notificable**: interfaz con la operación de envío.<br>**NotificacionWhatsApp** y **NotificacionCorreo**: implementan los canales (envío simulado).<br>**Cliente**: recibe el aviso. | Interfaces, polimorfismo, atributos y métodos estáticos, clases anidadas y encapsulamiento | Implementado |
| RF-20 | Filtrar las órdenes por estado, fecha, cliente y técnico asignado | **Administrador**: selecciona filtros.<br>**OrdenServicio**: aporta estado, fecha y técnico.<br>**Cliente** y **Tecnico**: aportan los datos para filtrar. | Clases, métodos, colecciones y búsqueda | Implementado |
| RNF-01 | Guardar técnicos, clientes y órdenes en archivos entre ejecuciones | **Tecnico**, **Cliente** y **OrdenServicio**: datos que deben conservarse.<br>**FileManage** (paquete persistencia): lee y escribe los archivos de `src/files/`.<br>**BaseRepository** y un repositorio por archivo (paquete repositorios): convierten los objetos en líneas de texto y al revés.<br>**GestorPlataforma**: carga los datos al iniciar y los guarda después de cada cambio. | Persistencia de archivos | Implementado |
| RNF-02 | Restringir el acceso a la información según el rol y la relación del usuario con la orden | **Usuario**: clase abstracta con identidad y rol.<br>**Cliente**, **Tecnico** y **Administrador**: heredan de Usuario y acceden según sus permisos. | Clases abstractas, herencia y encapsulamiento | Implementado |
| RNF-03 | Mantener la integridad de órdenes, asignaciones, detalles de trabajo y pagos ante errores | **OrdenServicio**, **Asignacion**, **DetalleTrabajo** y **Pago**: datos que deben conservar coherencia. | Encapsulamiento, herencia, validación y asociaciones | Implementado |
| RNF-04 | Mantener trazabilidad de los cambios de estado y de las reasignaciones de técnicos | **OrdenServicio.CambioEstado**: conserva el historial de estados.<br>**Asignacion**: conserva el historial de reasignaciones. | Clases anidadas, asociaciones y encapsulamiento | Implementado |
| RNF-05 | Proteger los datos personales y de pago frente a accesos no autorizados | **Usuario**: clase abstracta con datos personales.<br>**Pago**: clase abstracta con datos comunes del pago.<br>**Tarjeta**: conserva solo la referencia de autorización. | Clases abstractas, herencia y encapsulamiento | Implementado |
| RNF-06 | Presentar mensajes claros durante las operaciones del sistema | **Cliente**, **Tecnico** y **Administrador**: reciben mensajes sobre el resultado de sus operaciones.<br>**Notificacion**: arma el aviso con `crearMensajeCambioEstado()` (método estático) y guarda la causa cuando el envío falla. | Métodos estáticos, interfaces y manejo de excepciones | Implementado |

#### Ramas por clase e interfaz

| Rama | Responsabilidad |
|---|---|
| `Administrador` | Gestiona técnicos y órdenes |
| `Usuario` | Clase abstracta con los datos comunes de las personas usuarias |
| `Tecnico` | Representa técnicos y sus datos de servicio |
| `Cliente` | Representa clientes y sus datos de contacto |
| `Solicitud` | Almacena la solicitud de servicio |
| `OrdenServicio` | Relaciona solicitud, técnico, estado, costo y pagos |
| `Servicio` | Representa el tipo de servicio |
| `Garantia` | Registra la cobertura y vigencia de garantía |
| `Calificacion` | Almacena la calificación del cliente |
| `DisponibilidadTecnico` | Registra los bloques de disponibilidad e indisponibilidad |
| `Asignacion` | Relaciona un técnico con una orden y conserva reasignaciones |
| `DetalleTrabajo` | Registra actividades y tiempo empleado |
| `Material` | Representa los materiales usados y sus costos |
| `Notificacion` | Almacena la información y el resultado de un aviso |
| `Notificable` | Interfaz para enviar una notificación |
| `NotificacionWhatsApp` | Implementa el envío de notificaciones por WhatsApp |
| `NotificacionCorreo` | Implementa el envío de notificaciones por correo |
| `Pagable` | Interfaz con las operaciones de pago de una orden (se eliminó después, ver Decisión 17) |
| `Pago` | Clase abstracta con los datos y comportamientos comunes de un pago |
| `Transferencia` | Tipo de pago por transferencia |
| `Efectivo` | Tipo de pago en efectivo |
| `Tarjeta` | Tipo de pago con tarjeta |


---

## Conceptos POO demostrados

Cada concepto se puede rastrear a una o más clases concretas del proyecto:

### Herencia 
```
Usuario (abstracto)                Pago (abstracto)
├── Cliente                        ├── Efectivo
├── Tecnico                        ├── Tarjeta
└── Administrador                  └── Transferencia

BaseRepository (abstracto)         PanelRol (abstracto, interfaz gráfica)
├── ClienteRepository              ├── PanelCliente
├── TecnicoRepository              ├── PanelTecnico
├── OrdenServicioRepository        └── PanelAdministrador
└── ... (un repositorio por archivo)
```
`Tecnico` **es un** `Usuario`: hereda nombre, documento, correo, teléfono y el código SHA-256 de su contraseña, y agrega especialidad, zona y tarifa.

### Abstracción
- `Usuario` es **abstracta**: no se puede crear un "usuario" genérico. Obliga a cada subclase a decir su rol con `getRol()`.
- `Pago` es **abstracta**: cada medio de pago define `getMetodoPago()` y `getDetalle()`.
- `PanelRol` es **abstracta**: cada panel define sus secciones con `registrarSecciones()`.

### Polimorfismo
La orden avisa al cliente sin saber si el canal es WhatsApp o correo:
```java
public interface Notificable {
    String getNombreCanal();
    boolean enviar(Notificacion notificacion);
}

// En OrdenServicio: el mismo código funciona con cualquier canal
Notificable canal = cliente.getCanalNotificacion();
canal.enviar(aviso);
```
Y el repositorio de órdenes carga todas sus partes (pagos, cambios, avisos...) con un solo ciclo:
```java
for (IRepositorioDependiente<OrdenServicio> parte : partes) {
    parte.cargarEn(orden);
}
```

### Encapsulación
- Todos los atributos son `private` y se validan antes de guardarse (clase `Validar`).
- No existe `getContrasenia()`: solo se guarda el código SHA-256 de la contraseña y se compara con `verificarContrasenia()` (RNF-05).
- Algunos métodos no son `public` para que solo los use su propio paquete. Por ejemplo, `OrdenServicio.asignarTecnico` solo lo llama `Asignacion`, después de revisar especialidad, zona y horario:
```java
// Sin "public": solo Asignacion la llama (RF-04 y RF-13).
void asignarTecnico(Tecnico nuevo, String responsable) { ... }
```

### Interfaces
| Interfaz | Para qué sirve | La implementan |
|---|---|---|
| `Notificable` | Enviar un aviso al cliente | `NotificacionWhatsApp`, `NotificacionCorreo` |
| `IRepository<T>` | Define cómo se guardan y se leen en los archivos .txt los datos principales del sistema | `ClienteRepository`, `TecnicoRepository`, `OrdenServicioRepository`... |
| `IRepositorioDependiente<P>` | Guardar los datos que pertenecen a otro objeto | `PagoRepository`, `CambioEstadoRepository`, `DisponibilidadRepository`... |
| `IServicio<T>` | Define las consultas comunes que debe tener el servicio de cada tipo de usuario: obtener la lista completa, buscar por nombre, buscar por documento, verificar si alguien ya existe y mostrarlos todos en consola. | `ClienteServicio`, `TecnicoServicio`, `AdministradorServicio` |
| `IImprimible` | Mostrar el objeto en consola | `Cliente`, `Tecnico`, `Servicio`, `OrdenServicio`, `Pago` |

### Composición 
```
OrdenServicio ──tiene──> Asignacion, DetalleTrabajo, Garantia, Calificacion,
                         lista de CambioEstado, lista de Pago, lista de Notificacion
Tecnico ───────tiene──> DisponibilidadTecnico
OrdenServicioRepository ──tiene──> ClienteRepository, TecnicoRepository, ServicioRepository,
                                   SolicitudRepository y 7 repositorios dependientes
GestorPlataforma ──tiene──> los repositorios y las listas en memoria
```

### Clases anidadas
| Clase anidada | Dentro de | Para qué |
|---|---|---|
| `CambioEstado` | `OrdenServicio` | Registro de cada cambio de estado (RNF-04) |
| `FranjaHoraria` | `DisponibilidadTecnico` | Un día con hora de inicio y fin |
| `Material` | `DetalleTrabajo` | Material usado con su costo y cantidad |
| `Movimiento` | `Asignacion` | Cada asignación o reasignación de técnico |

### Sobrescritura de métodos (`@Override`)
```java
// Cada tipo de usuario dice su rol
@Override public String getRol() { return "Cliente"; }        // Cliente
@Override public String getRol() { return "Técnico"; }        // Tecnico
@Override public String getRol() { return "Administrador"; }  // Administrador
```

### Sobrecarga de métodos (Overloading)
`OrdenServicioRepository` tiene dos versiones de `getDataList`:
```java
public ArrayList<OrdenServicio> getDataList()   // vuelve a leer clientes, técnicos y servicios
public ArrayList<OrdenServicio> getDataList(List<Cliente> clientes, List<Tecnico> tecnicos,
                                            List<Servicio> servicios)   // usa los objetos ya cargados
```

### Sobrecarga de constructores
```java
public Cliente(ubicacion, nombre, documento, telefono, correo, contrasenia)          // registro normal
public Cliente(ubicacion, nombre, documento, telefono, correo, contrasenia,
               boolean contraseniaYaCifrada, Notificable canal)                      // al leer clientes.txt

public GestorPlataforma()                       // MODO ARCHIVO
public GestorPlataforma(boolean usarArchivos)   // false = MODO MEMORIA
```

### Métodos y atributos estáticos (`static`)
```java
private static int consecutivo = 0;   // compartido por todas las órdenes

public static String generarCodigo() {   // OS-001, OS-002...
    consecutivo++;
    String ceros = consecutivo < 10 ? "00" : consecutivo < 100 ? "0" : "";
    return "OS-" + ceros + consecutivo;
}
```
Al leer `ordenes.txt` el consecutivo se ajusta, para que nunca se repita un código.

### Enum
```java
public enum EstadoOrden {
    PENDIENTE("Pendiente"), ASIGNADA("Asignada"), EN_PROCESO("En proceso"),
    FINALIZADA("Finalizada"), CANCELADA("Cancelada");
    ...
}
```
También `EstadoPago` (pendiente, confirmado, rechazado) y `EstadoEnvio` (pendiente, enviada, fallida).

### Generics (`<T>`)
`IRepository<T>`, `IServicio<T>` e `IRepositorioDependiente<P>` definen el contrato **una sola vez** para cualquier tipo: `IRepository<Cliente>`, `IRepository<OrdenServicio>`, `IRepositorioDependiente<Tecnico>`...

### Excepciones
Las reglas se validan lanzando `IllegalArgumentException` o `IllegalStateException` con un mensaje claro. La interfaz las atrapa y muestra el mensaje al usuario sin cerrar el programa (RNF-06).

---

## Arquitectura del sistema

```
┌──────────────────────────────────────────────────────────────────┐
│  CAPA UI (ui/)                                                   │
│  Ventanas: VentanaPrincipal, PanelLogin, PanelCliente,           │
│            PanelTecnico, PanelAdministrador, DialogoDetalleOrden │
│  Consola:  MenuPrincipal, MenuCliente... (argumento "consola")   │
│  Responsabilidad: mostrar y pedir datos. Sin reglas de negocio.  │
├──────────────────────────────────────────────────────────────────┤
│  CAPA SERVICIOS (servicios/)                                     │
│  GestorPlataforma, ClienteServicio, TecnicoServicio,             │
│  AdministradorServicio                                           │
│  Responsabilidad: coordinar cada operación y guardarla.          │
├──────────────────────────────────────────────────────────────────┤
│  CAPA REPOSITORIOS (repositorios/) + persistencia/FileManage     │
│  Responsabilidad: convertir objetos Java ↔ líneas de texto.      │
├──────────────────────────────────────────────────────────────────┤
│  CAPA MODELOS (modelos/ + enums/ + interfaces/)                  │
│  Responsabilidad: las entidades del dominio y sus reglas.        │
└──────────────────────────────────────────────────────────────────┘
```

**Flujo de una operación** (ejemplo: el técnico finaliza una orden):

```
MenuTecnico (opción 8)                       [UI]
   ↓
TecnicoServicio.finalizarOrden(tecnico, "OS-003")   [Servicios: revisa que la orden sea suya]
   ↓
OrdenServicio.finalizar(...)                 [Modelos: calcula costo final y garantía]
   ↓
GestorPlataforma.guardarOrden(orden)         [Servicios]
   ↓
OrdenServicioRepository.updateData(...)      [Repositorios: ordenes.txt + garantias.txt + ...]
   ↓
FileManage.writeAllLinesInFile(...)          [Persistencia]
```

---

## Estructura del proyecto

```
proyecto_POO/
├── README.md                            Este documento (incluye decisiones, trazabilidad y bitácora)
├── .gitignore
├── .idea/  ·  proyecto_POO.iml          Configuración de IntelliJ IDEA
└── src/
    ├── Main.java                        Punto de entrada — abre la ventana (o la consola con el argumento "consola")
    │
    ├── enums/
    │   ├── EstadoOrden.java             PENDIENTE, ASIGNADA, EN_PROCESO, FINALIZADA, CANCELADA
    │   ├── EstadoPago.java              PENDIENTE, CONFIRMADO, RECHAZADO
    │   └── EstadoEnvio.java             PENDIENTE, ENVIADA, FALLIDA
    │
    ├── interfaces/
    │   ├── IRepository.java             Contrato CRUD genérico <T>
    │   ├── IRepositorioDependiente.java Contrato para datos que pertenecen a otro objeto <P>
    │   ├── IServicio.java               Contrato de consulta genérico <T>
    │   ├── IImprimible.java             Contrato de impresión en consola
    │   └── Notificable.java             Canal de avisos (RF-19)
    │
    ├── modelos/                         Clases del dominio
    │   ├── Usuario, Administrador, Cliente, Tecnico
    │   ├── DisponibilidadTecnico (+ FranjaHoraria anidada)
    │   ├── Solicitud, Servicio, OrdenServicio (+ CambioEstado anidada), Asignacion (+ Movimiento anidada)
    │   ├── DetalleTrabajo (+ Material anidada), Garantia, Calificacion
    │   ├── Pago (abstracta), Efectivo, Tarjeta, Transferencia
    │   ├── Notificacion, NotificacionWhatsApp, NotificacionCorreo
    │   └── Validar Validaciones comunes de los modelos (RNF-06)
    │
    ├── persistencia/
    │   └── FileManage.java              Leer/escribir líneas en archivos .txt (UTF-8)
    │
    ├── repositorios/
    │   ├── BaseRepository.java          Clase abstracta — fileManage + delimitador + ayudas
    │   ├── AdministradorRepository      administradores.txt
    │   ├── ClienteRepository            clientes.txt
    │   ├── TecnicoRepository            tecnicos.txt          (TIENE un DisponibilidadRepository)
    │   ├── DisponibilidadRepository     disponibilidades.txt
    │   ├── ServicioRepository           servicios.txt
    │   ├── SolicitudRepository          solicitudes.txt
    │   ├── OrdenServicioRepository      ordenes.txt           (TIENE los 7 repositorios de abajo)
    │   ├── CambioEstadoRepository       cambios_estado.txt
    │   ├── AsignacionRepository         asignaciones.txt
    │   ├── DetalleTrabajoRepository     detalles_trabajo.txt  (TIENE un MaterialRepository)
    │   ├── MaterialRepository           materiales.txt
    │   ├── GarantiaRepository           garantias.txt         (TIENE un RevisionGarantiaRepository)
    │   ├── RevisionGarantiaRepository   revisiones_garantia.txt
    │   ├── CalificacionRepository       calificaciones.txt
    │   ├── PagoRepository               pagos.txt
    │   └── NotificacionRepository       notificaciones.txt
    │
    ├── servicios/
    │   ├── GestorPlataforma.java        Listas en memoria + repositorios (modo dual)
    │   ├── ClienteServicio.java         Acciones del cliente
    │   ├── TecnicoServicio.java         Acciones del técnico
    │   └── AdministradorServicio.java   Acciones del administrador
    │
    ├── ui/                              Interfaz de usuario
    │   │   ── Ventanas (Swing) ──
    │   ├── VentanaPrincipal.java        La ventana: crea los servicios y cambia de pantalla
    │   ├── PanelLogin.java              Inicio de sesión por rol y registro de clientes
    │   ├── PanelRol.java                Clase abstracta: barra lateral + secciones
    │   ├── PanelCliente.java            ← hereda de PanelRol
    │   ├── PanelTecnico.java            ← hereda de PanelRol
    │   ├── PanelAdministrador.java      ← hereda de PanelRol
    │   ├── DialogoDetalleOrden.java     Ficha completa de una orden
    │   ├── TablaOrdenes.java            Tabla con íconos de oficio e insignias de estado
    │   ├── CalendarioSemanal.java       Agenda del técnico dibujada con Java2D
    │   ├── Formulario.java              Ventana de formulario reutilizable
    │   ├── Acciones.java                Ejecuta acciones y muestra éxito/error
    │   ├── Tema.java, Iconos.java       Colores, fuentes e íconos dibujados
    │   ├── Boton, PanelTarjeta, Insignia, Componentes, SelectorFranja, SelectorEstrellas, PanelDesplazable
    │   │   ── Consola ──
    │   ├── MenuPrincipal.java           Inicio de sesión por rol y registro de clientes
    │   ├── MenuCliente.java
    │   ├── MenuTecnico.java
    │   ├── MenuAdministrador.java
    │   ├── DemoRequerimientos.java      Demo de todos los requerimientos, en memoria
    │   └── Consola.java                 Lectura de datos compartida por los menús
    │
    └── files/                           16 archivos .txt con datos de ejemplo
```

---

## Jerarquía de clases

```
java.lang.Object
│
├── modelos.Usuario (abstract)
│   ├── modelos.Cliente ─────────────────────────── implements: IImprimible
│   ├── modelos.Tecnico ─────────────────────────── implements: IImprimible
│   └── modelos.Administrador
│
├── modelos.Pago (abstract) ─────────────────────── implements: IImprimible
│   ├── modelos.Efectivo
│   ├── modelos.Tarjeta
│   └── modelos.Transferencia
│
├── modelos.OrdenServicio ───────────────────────── implements: IImprimible
│   └── OrdenServicio.CambioEstado (static)
├── modelos.Asignacion
│   └── Asignacion.Movimiento (static)
├── modelos.DisponibilidadTecnico
│   └── DisponibilidadTecnico.FranjaHoraria (static)
├── modelos.DetalleTrabajo
│   └── DetalleTrabajo.Material (static)
├── modelos.Solicitud · Garantia · Calificacion · Notificacion · Validar
├── modelos.Servicio ────────────────────────────── implements: IImprimible
├── modelos.NotificacionWhatsApp ────────────────── implements: Notificable
├── modelos.NotificacionCorreo ──────────────────── implements: Notificable
│
├── persistencia.FileManage
│
├── repositorios.BaseRepository (abstract)
│   ├── AdministradorRepository · ClienteRepository · TecnicoRepository
│   │   ServicioRepository · SolicitudRepository · OrdenServicioRepository ── implements: IRepository<T>
│   ├── DisponibilidadRepository ───────────────── implements: IRepositorioDependiente<Tecnico>
│   ├── CambioEstadoRepository · AsignacionRepository · DetalleTrabajoRepository
│   │   GarantiaRepository · CalificacionRepository · PagoRepository
│   │   NotificacionRepository ─────────────────── implements: IRepositorioDependiente<OrdenServicio>
│   └── MaterialRepository · RevisionGarantiaRepository
│
├── servicios.GestorPlataforma
├── servicios.ClienteServicio ───────────────────── implements: IServicio<Cliente>
├── servicios.TecnicoServicio ───────────────────── implements: IServicio<Tecnico>
├── servicios.AdministradorServicio ─────────────── implements: IServicio<Administrador>
│
├── ui.VentanaPrincipal (JFrame) · ui.PanelLogin · ui.DialogoDetalleOrden · ui.Formulario
├── ui.PanelRol (abstract)
│   ├── ui.PanelCliente
│   ├── ui.PanelTecnico
│   └── ui.PanelAdministrador
└── ui.MenuPrincipal · MenuCliente · MenuTecnico · MenuAdministrador · DemoRequerimientos (consola)

enums.EstadoOrden · enums.EstadoPago · enums.EstadoEnvio (enum)
interfaces.IRepository<T> · IRepositorioDependiente<P> · IServicio<T> · IImprimible · Notificable (interface)
```

---

## Formato de archivos de datos

Todos usan el delimitador `|`. Cada línea es un registro y **el primer campo es su clave**.
Los archivos de datos que pertenecen a una orden empiezan con el código de la orden; los de disponibilidad, con el documento del técnico.

| Archivo | Formato |
|---|---|
| `administradores.txt` | `documento\|nombre\|correo\|telefono\|contraseniaCifrada\|areaResponsable` |
| `clientes.txt` | `documento\|nombre\|correo\|telefono\|contraseniaCifrada\|ubicacion\|canal` |
| `tecnicos.txt` | `documento\|nombre\|correo\|telefono\|contraseniaCifrada\|especialidad\|zona\|tarifaBase` |
| `disponibilidades.txt` | `documentoTecnico\|DISPONIBLE o BLOQUEO\|dia\|horaInicio\|horaFin` |
| `servicios.txt` | `tipo\|factorTarifa\|duracionHoras\|diasGarantia` |
| `solicitudes.txt` | `id\|tipoProblema\|ubicacion\|descripcion\|dia\|horaInicio\|horaFin\|fechaCreacion\|estado\|motivoCancelacion` |
| `ordenes.txt` | `codigo\|documentoCliente\|idSolicitud\|tipoServicio\|documentoTecnico\|estado\|fechaCreacion\|materialesEstimados\|costoEstimado\|manoObraFinal\|materialesFinal\|costoFinal` |
| `cambios_estado.txt` | `codigoOrden\|estadoAnterior\|estadoNuevo\|fecha\|responsable` |
| `asignaciones.txt` | `codigoOrden\|fecha\|documentoTecnicoAnterior\|documentoTecnicoNuevo\|responsable` |
| `detalles_trabajo.txt` | `codigoOrden\|descripcion\|horas\|actividad1;actividad2;...` |
| `materiales.txt` | `codigoOrden\|descripcion\|costoUnitario\|cantidad` |
| `garantias.txt` | `codigoOrden\|fechaInicio\|diasVigencia\|cobertura` |
| `revisiones_garantia.txt` | `codigoOrden\|fecha\|problema` |
| `calificaciones.txt` | `codigoOrden\|estrellas\|comentario\|fecha` |
| `pagos.txt` | `codigoOrden\|metodo\|monto\|fecha\|estado\|referencia` |
| `notificaciones.txt` | `codigoOrden\|id\|documentoCliente\|nombre\|telefono\|correo\|mensaje\|fecha\|canal\|estadoEnvio\|detalle` |

> **RNF-05:** de las contraseñas solo se guarda su código SHA-256 (nunca el texto original), y de la tarjeta solo se guarda la referencia de autorización, nunca el número ni el código de seguridad.

> **Composición en archivos:** `ordenes.txt` guarda solo los **IDs** del cliente, la solicitud, el servicio y el técnico. Al leer, `OrdenServicioRepository` busca los objetos completos y arma la orden.

---

## Cómo ejecutar el proyecto

**Requisitos:** Java 11 o superior (el proyecto está configurado con JDK 21) e IntelliJ IDEA.

1. Clonar el repositorio y abrir la carpeta del proyecto en IntelliJ IDEA (**File → Open**).
2. Verificar que el **directorio de trabajo** sea la raíz del proyecto (así encuentra `src/files/`).
3. Ejecutar **`src/Main.java`** → abre la aplicación con ventanas.
4. Opcional: para la versión con menús de texto, en *Run → Edit Configurations → Program arguments* escribir `consola`.

Las dos versiones usan los mismos servicios, repositorios y archivos `.txt`: lo que se hace en una se ve en la otra.

### Usuarios de prueba (ya están en los .txt)

| Rol | Documento | Contraseña | Datos |
|---|---|---|---|
| Administrador | 1001 | admin123 | Laura Gómez |
| Técnico | 2001 | clave1 | Luis Pérez — Plomería, Chía |
| Técnico | 2002 | clave2 | Pedro Ruiz — Plomería, Chía |
| Técnico | 2003 | clave3 | Marta Díaz — Electricidad, Bogotá |
| Técnico | 2004 | clave4 | Jorge Castro — Pintura, Cajicá |
| Técnico | 2005 | clave5 | Diana Herrera — Cerrajería, Chía |
| Cliente | 3001 | miClave | Sofía Ríos — Chía (avisos por WhatsApp) |
| Cliente | 3002 | clave3002 | Andrés Mejía — Bogotá (avisos por correo) |
| Cliente | 3003 | clave3003 | Camila Torres — Cajicá |

### Órdenes de ejemplo

| Orden | Estado | Para probar |
|---|---|---|
| OS-001 | FINALIZADA | Costo final vs. estimado, garantía con revisión, calificación, pagos (uno confirmado, uno pendiente), reasignación de Luis a Pedro |
| OS-002 | CANCELADA | Cancelación con motivo |
| OS-003 | EN_PROCESO | Marta puede registrar el trabajo y finalizarla |
| OS-004 | ASIGNADA | Jorge puede iniciarla, o el administrador cancelarla |
| OS-005 | PENDIENTE | El administrador puede asignarla a Diana (ventanas: *Órdenes → Asignar técnico*; consola: opción 7) |

> En la versión de consola, la opción **5 del menú principal** ejecuta la demo de todos los requerimientos **en memoria**: no modifica los archivos.

### Interfaz gráfica

Hecha con **Java Swing** (viene incluido en el JDK, no hay que instalar nada). Colores de la marca: azul (confianza) y naranja (herramienta). Los íconos se dibujan con código Java2D (gota = plomería, rayo = electricidad, rodillo = pintura, llave = cerrajería, martillo = carpintería), así que no hay archivos de imagen.

| Pantalla | Qué permite | Requerimientos |
|---|---|---|
| Inicio de sesión | Elegir rol, ingresar y crear cuenta de cliente | RF-02, RNF-02, RNF-05 |
| Cliente → Inicio | Resumen: órdenes activas, saldo por pagar, canal de avisos | RF-08 |
| Cliente → Solicitar servicio | Tarjetas por oficio con duración, garantía y tarifa | RF-03 |
| Cliente → Mis órdenes | Ver detalle, cancelar, calificar con estrellas, garantía, pagar | RF-08, RF-10, RF-12, RF-17, RF-18 |
| Cliente → Mi perfil | Datos de contacto, canal WhatsApp/correo, contraseña | RF-11, RF-19 |
| Técnico → Inicio y Mis órdenes | Próximas visitas, iniciar, registrar trabajo y materiales, finalizar | RF-06, RF-09, RF-15, RF-16 |
| Técnico → Mi agenda | Calendario semanal con horario, bloqueos y visitas | RF-13 |
| Admin → Panel general | Órdenes por estado, ingresos, calificación promedio, gráfico por oficio | — |
| Admin → Órdenes | Filtros, asignar con costo estimado, reasignar, cancelar | RF-04, RF-05, RF-06, RF-14, RF-20 |
| Admin → Técnicos / Clientes / Servicios | Registrar técnicos, ver agenda, catálogo de servicios | RF-01, RF-05, RF-07 |
| Admin → Pagos | Confirmar o rechazar pagos | RF-17 |
| Detalle de orden | Costos, trabajo, garantía, calificación, pagos, línea de tiempo y avisos | RF-16, RF-19, RNF-04 |

Los botones solo se activan cuando la acción tiene sentido (por ejemplo, "Cancelar" solo con órdenes pendientes), y todo error se muestra en una ventana con un mensaje claro (RNF-06).

---

## ArrayList vs Archivo de texto

`GestorPlataforma` se puede crear de dos formas:
```java
new GestorPlataforma();        // MODO ARCHIVO: carga src/files/*.txt y guarda cada cambio (RNF-01)
new GestorPlataforma(false);   // MODO MEMORIA: solo ArrayList (lo usa DemoRequerimientos)
```

Los datos se leen de los archivos solo una vez, al abrir el programa, y después todo el sistema trabaja con esos mismos objetos. Esto es importante porque usamos `==` para comprobar, por ejemplo, que una orden sea del cliente que la está consultando, y `==` solo funciona si es exactamente el mismo objeto, no una copia con los mismos datos.

---

| Aspecto | ArrayList (memoria) | Archivo de texto (.txt) |
|---|---|---|
| Velocidad | Muy rápida | Más lenta (lee y escribe el disco) |
| Persistencia | Se pierde al cerrar el programa | Se conserva entre ejecuciones (RNF-01) |
| Uso en el proyecto | `DemoRequerimientos` (`new GestorPlataforma(false)`) | La aplicación normal (`new GestorPlataforma()`) |
| Se ve el dato | Solo mientras corre el programa | Se puede abrir el archivo y leerlo |

---

## Reglas de negocio

Las reglas se validan en los **modelos** y los **servicios** las usan:

| Regla | Dónde | Requerimiento |
|---|---|---|
| Una orden solo cambia de estado en este orden: Pendiente → Asignada → En proceso → Finalizada, o se cancela antes de iniciar | `OrdenServicio.transicionPermitida` | RF-06 |
| Solo se asigna un técnico de la especialidad y zona de la solicitud, libre en ese horario | `Asignacion`, `DisponibilidadTecnico` | RF-04, RF-13 |
| Los bloqueos de un técnico no se pueden cruzar entre sí | `DisponibilidadTecnico.registrarIndisponibilidad` | RF-13 |
| Solo se reasigna antes de iniciar el trabajo, y el técnico anterior queda en el historial | `Asignacion.reasignarTecnico` | RF-14 |
| El cliente solo cancela mientras la orden está pendiente | `Cliente.cancelarSolicitud` | RF-12 |
| Solo el técnico asignado registra el trabajo; la orden no se finaliza sin el detalle | `Tecnico.registrarTrabajo`, `OrdenServicio.finalizar` | RF-15, RNF-02 |
| Se califica una sola vez y solo una orden finalizada | `OrdenServicio.calificar` | RF-10 |
| La revisión por garantía solo se acepta si la garantía está vigente | `Garantia.solicitarRevision` | RF-18 |
| Un pago no puede superar lo que falta por pagar, contando los pagos que esperan confirmación | `OrdenServicio.registrarPago` | RF-17 |
| Cada usuario solo ve y gestiona sus propias órdenes; los pagos solo los ven el cliente dueño y el administrador | `Cliente.verificarPropia`, `OrdenServicio.getPagos` | RNF-02, RNF-05 |
| No se repiten documentos de técnicos ni de clientes | `GestorPlataforma` | RF-01, RF-02 |

Las transiciones de estado se controlan en un solo método:

```java
private boolean transicionPermitida(EstadoOrden actual, EstadoOrden nuevo) {
    switch (actual) {
        case PENDIENTE: return nuevo == EstadoOrden.ASIGNADA || nuevo == EstadoOrden.CANCELADA;
        case ASIGNADA: return nuevo == EstadoOrden.EN_PROCESO || nuevo == EstadoOrden.CANCELADA;
        case EN_PROCESO: return nuevo == EstadoOrden.FINALIZADA;
        default: return false;   // FINALIZADA y CANCELADA son finales
    }
}
```

Al finalizar una orden, el sistema:
1. Calcula el costo final: horas reales × tarifa base del técnico × factor del servicio + materiales (RF-16).
2. Crea la garantía con los días que define el tipo de servicio (RF-07).
3. Registra el cambio de estado con fecha y responsable (RNF-04) y avisa al cliente por su canal (RF-19).
4. Guarda la orden y todas sus partes en los archivos `.txt` (RNF-01).

---

## Registro de decisiones

Proyecto integrador: plataforma de técnicos a domicilio.


### Decisión 1 · Usuario como clase abstracta

**Decisión**

Crear Usuario como clase abstracta con la información general común, como nombre y documento. Cliente, Tecnico y Administrador heredan de Usuario y agregan sus propios datos y acciones. Usuario no se instancia directamente.

**Alternativas consideradas**

1. Crear Cliente, Tecnico y Administrador como clases independientes y repetir los datos comunes.
2. Crear una sola clase Usuario con un atributo que indique el tipo de usuario.

**Por qué**

Las tres clases comparten información, pero representan roles distintos. La herencia evita repetir atributos y permite mantener las responsabilidades propias de cada rol.

**Consecuencia**

Los atributos comunes se definen una sola vez en Usuario. Las subclases reutilizan esos datos y añaden los suyos. En UML, Usuario se representa como abstracta y las otras tres clases heredan de ella. Esta decisión apoya RF-01, RF-02 y RNF-02.

### Decisión 2 · Pago como clase abstracta

**Decisión**

Definir Pago como una clase abstracta que reúne la información y el comportamiento común de los pagos. Efectivo, Tarjeta y Transferencia heredan de Pago.

**Alternativas consideradas**

1. Crear las tres clases de pago por separado y repetir en cada una el monto, la fecha y el estado.
2. Crear una sola clase Pago con un atributo que indique el medio de pago.

**Por qué**

Los tres medios de pago comparten atributos como el monto, la fecha y el estado, pero cada uno tiene detalles propios. Además, el sistema siempre crea pagos de un medio específico, nunca un pago genérico, por eso Pago no se instancia directamente.

**Consecuencia**

Los atributos y comportamientos comunes se escriben una sola vez en Pago. Cada medio de pago conserva solo sus datos y operaciones particulares. Esta decisión apoya RF-17.


### Decisión 3 · Operaciones abstractas para los medios de pago

**Decisión**

Definir en Pago los métodos abstractos getMetodoPago() y getDetalle(), que implementan Efectivo, Tarjeta y Transferencia.

**Alternativas consideradas**

1. Usar un if o un switch en Pago para revisar el tipo de pago y devolver el texto correspondiente.

**Por qué**

Cada medio debe informar su nombre y los detalles propios de la transacción, pero esa información cambia según el tipo de pago. Con métodos abstractos cada clase responde a su manera (polimorfismo) y no hay que modificar Pago si se agrega un medio nuevo.

**Consecuencia**

Todas las clases de pago están obligadas a ofrecer esas operaciones y muestran los datos de su propio medio. Esta decisión apoya RF-17.


### Decisión 4 · Validar los datos del pago y sus referencias

**Decisión**

El pago debe tener un monto mayor que cero y una fecha válida. Además, la autorización de Tarjeta y la referencia bancaria de Transferencia no pueden estar vacías.

**Alternativas consideradas**

1. Aceptar los datos tal como llegan y revisarlos después, al confirmar el pago.

**Por qué**

Estos datos son necesarios para identificar y registrar correctamente cada pago. Es mejor rechazarlos desde el principio que guardar un pago incompleto.

**Consecuencia**

El sistema rechaza pagos con datos inválidos y muestra un mensaje que explica el error. Como el monto se maneja como double, no puede ser nulo, pero sí se valida que sea mayor que cero. Esta decisión apoya RF-17 y RNF-06.


### Decisión 5 · Confirmar y rechazar pagos

**Decisión**

Agregar en Pago las operaciones confirmarPago(), rechazarPago() y estaConfirmado() para cambiar y consultar el estado del pago.

**Alternativas consideradas**

1. Dar por confirmado todo pago apenas el cliente lo registra.

**Por qué**

El sistema necesita saber si un pago está pendiente, confirmado o rechazado. El cliente registra el pago, pero es el administrador quien revisa que el dinero sí llegó.

**Consecuencia**

Todo pago empieza como pendiente y su estado se actualiza y se consulta con los métodos de Pago. Un pago solo se puede confirmar o rechazar mientras sigue pendiente. Esta decisión apoya RF-17.

### Decisión 6 · Solicitud y OrdenServicio como clases separadas

**Decisión**

Solicitud almacena el tipo de problema, la ubicación, la descripción y el horario disponible. OrdenServicio relaciona la solicitud con el técnico asignado y conserva los datos de ejecución y estado.

**Alternativas consideradas**

1. Guardar la solicitud, la asignación y el estado en una sola clase OrdenServicio.
2. Guardar la solicitud como atributos de Cliente.

**Por qué**

La solicitud existe antes de que se asigne un técnico. Separar ambas clases permite representar ese proceso y mantiene responsabilidades claras.

**Consecuencia**

OrdenServicio se asocia con Solicitud y Tecnico. También relaciona el servicio, la garantía, la calificación, los pagos y los cambios de estado. Esta decisión apoya RF-03, RF-04 y RF-05.


### Decisión 7 · DisponibilidadTecnico: separar los horarios de los datos del técnico

**Decisión**

Crear la clase `DisponibilidadTecnico` para registrar los horarios en los que un técnico puede trabajar y los bloques temporales en los que no puede recibir asignaciones. 

**Alternativas consideradas**

1. Guardar todos los horarios disponibles y los bloqueos directamente como atributos de la clase `Tecnico`, sin separar la gestión de disponibilidad.
2. Registrar únicamente el horario habitual del técnico, sin contemplar los bloques de indisponibilidad temporal.

**Por qué**

Un técnico puede tener varios intervalos. Representarlos como objetos facilita registrar y consultar cada bloque sin acumular atributos repetidos en Tecnico. Se eligió separar esta responsabilidad para administrar los horarios y las indisponibilidades sin mezclar toda esa lógica con los datos personales y profesionales del técnico.

**Consecuencia**

Se relaciona por composición con `Tecnico` y contiene, también mediante composición, objetos de la clase `FranjaHoraria`. Por esta razón, no se considera completamente independiente de esas relaciones dentro del modelo. Cumple directamente con el **RF-13**, al permitir registrar bloques de indisponibilidad temporal, y apoya el **RF-04**, porque permite consultar la disponibilidad del técnico antes de asignarle un servicio.



### Decisión 8 · Asignacion: vincular órdenes y conservar el historial

**Decisión**

Crear la clase `Asignacion` para vincular un técnico con una orden de servicio. También permite representar la reasignación cuando el técnico inicialmente asignado no puede atenderla y conservar las asignaciones anteriores para mantener la trazabilidad.

**Alternativas consideradas**

1. Guardar el técnico asignado directamente en `OrdenServicio`, sin crear una clase independiente para gestionar las asignaciones y sus cambios.
2. Cambiar directamente el técnico asociado a la orden sin conservar un historial de las asignaciones anteriores.

**Por qué**

Se eligió crear una clase propia para centralizar la asignación de técnicos a las órdenes y facilitar el registro de las reasignaciones y de los técnicos anteriores.

**Consecuencia**

Se asocia con `Tecnico` y con `OrdenServicio`. No es completamente independiente, porque necesita relacionar ambas clases para cumplir su función. Apoya el **RF-04**, al representar la asignación del técnico al servicio; el **RF-14**, al permitir representar las reasignaciones; y el **RNF-04**, al conservar la trazabilidad de los cambios de asignación.


### Decisión 9 · DetalleTrabajo y Material registran la ejecución

**Decisión**

Crear DetalleTrabajo para registrar actividades y tiempo real. Crear Material para registrar los insumos usados, sus cantidades y costos.

**Alternativa considerada**

Guardar actividades, tiempo y materiales directamente en OrdenServicio.

**Por qué**

La orden puede tener varios detalles y materiales. Mantenerlos como objetos relacionados evita que OrdenServicio concentre todos esos datos.

**Consecuencia**

OrdenServicio reúne los detalles y materiales necesarios para calcular el costo final. Esta decisión apoya RF-15, RF-16 y RNF-03.



### Decisión 10 · Notificacion y Notificable tienen responsabilidades distintas

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
- `EstadoEnvio` (PENDIENTE, ENVIADA, FALLIDA) es un enum que está en el paquete `enums`, junto con `EstadoOrden` y `EstadoPago`.
- Miembros estáticos: el contador `totalNotificaciones` (numera los avisos), `getTotalNotificaciones()`, y `crearMensajeCambioEstado()`, que arma el texto del aviso en lenguaje claro.

Esta decisión apoya RF-19, RNF-04 y RNF-06.


### Decisión 11 · Clases anidadas relacionadas con su clase contenedora

**Decisión**

Mantener FranjaHoraria dentro de DisponibilidadTecnico y CambioEstado dentro de OrdenServicio como clases anidadas estáticas.

**Por qué**

Cada clase anidada representa un concepto estrechamente relacionado con su clase contenedora y no requiere una rama independiente.

**Consecuencia**

Las clases se implementan en los archivos de sus contenedoras. FranjaHoraria apoya RF-13 y CambioEstado apoya RNF-04.





### Decisión 12 · Clase FranjaHoraria: determinar los intervalos de tiempo

**Decisión**

Crear la clase anidada estática `FranjaHoraria` dentro de `DisponibilidadTecnico` para representar un día específico y un intervalo horario mediante una hora de inicio y una hora de finalización. También permite validar los intervalos y comprobar si dos franjas se cruzan.

**Alternativas consideradas**

1. Representar cada franja horaria mediante datos separados, como el día, la hora de inicio y la hora de finalización, directamente dentro de `DisponibilidadTecnico`, sin crear una clase específica.
2. Crear `FranjaHoraria` como una clase independiente, fuera de `DisponibilidadTecnico`.

**Por qué**

Se eligió una clase anidada para organizar cada intervalo horario como una unidad y facilitar la validación de los horarios y los bloques de indisponibilidad.

**Consecuencia**

Está relacionada con `DisponibilidadTecnico` mediante composición, por lo que cada franja forma parte de la disponibilidad que la contiene. Su función está vinculada a esa clase y apoya el **RF-13**, porque permite representar y validar los intervalos horarios en los que el técnico no puede recibir asignaciones.


### Decisión 13 · Utilizar herencia para Cliente, Tecnico y Administrador

**Decisión**

Se estableció una relación de herencia entre Usuario y sus tres clases derivadas: Cliente, Tecnico y Administrador.

**Por qué**

Aunque los tres tipos de usuario comparten información básica, cada uno cumple funciones diferentes dentro de la plataforma.

**Consecuencia**

Cada clase conserva los atributos y comportamientos generales de Usuario y añade los propios de su rol. Por ejemplo, Cliente gestiona solicitudes, Tecnico administra su disponibilidad y Administrador gestiona usuarios y órdenes de servicio.


### Decisión 14 · Asociar Cliente con Solicitud

**Decisión**

Se decidió asociar Cliente y Solicitud, de manera que un cliente puede crear varias solicitudes y cada solicitud pertenece a un cliente (multiplicidad).

**Por qué**

El sistema necesita identificar quién solicita un servicio y mantener relacionadas las solicitudes con la persona que las creó. Además, esto ayuda a cumplir el RF-03, que exige que el cliente solicite un servicio y registre la información necesaria.

**Consecuencia**

La plataforma puede consultar las solicitudes de cada cliente, identificar al responsable de una solicitud y facilitar operaciones como consultar su historial (RF-08).


### Decisión 15 · Incorporar la actualización de datos para el RF-11

**Decisión**

Se incluyó en Cliente la operación `actualizarDatos()` para modificar la información personal del cliente.

**Por qué**

Los datos de contacto o ubicación del cliente pueden cambiar y el sistema debe permitir que la información registrada se mantenga actualizada cada vez que sea necesario.

**Consecuencia**

El cliente puede actualizar sus datos sin necesidad de crear una cuenta nueva. La operación debe verificar que la información ingresada sea válida y guardar los cambios correctamente.


### Decisión 16 · Controlar la cancelación de solicitudes para el RF-12

**Decisión**

El cliente cancela con `Cliente.cancelarSolicitud()`, que primero revisa que la orden sea suya y luego llama a `OrdenServicio.cancelar()`, donde se comprueba que el estado permita cancelar y se guarda el motivo en la solicitud. La solicitud usa los mismos estados de la orden (`EstadoOrden`), en lugar de tener una lista de estados propia.

**Por qué**

La cancelación depende del estado en el que se encuentre la solicitud. No se permite que cualquier solicitud se cancele sin verificar las condiciones del servicio.

**Consecuencia**

El sistema impide las cancelaciones que no permiten las reglas del negocio: el cliente solo puede cancelar mientras la orden está pendiente y no tiene técnico asignado. Si ya está asignada, la cancelación se rechaza con un mensaje. Como la solicitud y la orden comparten los estados, nunca pueden quedar contradiciéndose.


### Decisión 17 · Eliminar la interfaz Pagable

**Decisión**

Eliminar la interfaz Pagable y trasladar sus operaciones directamente a OrdenServicio, ya que era la única clase que la implementaba.

**Alternativa considerada**

Mantener Pagable como una interfaz independiente para definir las operaciones de registro de pagos y consulta del saldo pendiente.

**Por qué**

Pagable solamente era implementada por OrdenServicio, por lo que no aportaba reutilización ni una ventaja significativa de diseño. Mantenerla añadía una estructura innecesaria al proyecto.

**Consecuencia**

OrdenServicio incorpora directamente las operaciones establecidas, manteniendo la funcionalidad de gestión de pagos sin depender de una interfaz adicional. Se simplifica la estructura del modelo y se evita utilizar abstracciones que no aportan beneficios concretos.


### Decisión 18 · Una sola clase Servicio para todos los tipos

**Decisión**

Hicimos una sola clase `Servicio` y cada tipo (Plomería, Electricidad, Pintura) es un objeto con sus propios valores: un factor de tarifa, cuántas horas dura más o menos y cuántos días de garantía tiene.

**Alternativas consideradas**

1. Hacer una clase hija de Servicio por cada tipo (Plomeria, Electricidad, Pintura).
2. Usar un enum con los tipos, como hicimos con los estados de la orden.
3. Poner if en OrdenServicio para calcular el precio según el tipo.

**Por qué**

Nos dimos cuenta de que los tipos no hacen nada distinto, solo cambian los números, así que hacer herencia no tenía sentido. Con el enum el problema era que el administrador no podría crear tipos nuevos, porque quedarían fijos en el código. Y con los if tocaría cambiar OrdenServicio cada vez que se agrega un servicio.

**Consecuencia**

Si queremos agregar un servicio nuevo, solo se registra desde el panel del administrador y queda guardado en servicios.txt, sin cambiar el código. El precio se calcula multiplicando las horas por la tarifa del técnico y por el factor del tipo, y con los días de garantía se crea la garantía cuando se termina la orden. Esta decisión apoya RF-05 y RF-07.


### Decisión 19 · Garantia es una clase propia que se crea al finalizar la orden

**Decisión**

Hicimos una clase Garantia que guarda la fecha de inicio, los días de vigencia y la cobertura. Ella misma sabe si está vigente (estaVigente) y si puede aceptar una revisión (solicitarRevision). La garantía la crea OrdenServicio en el momento en que la orden pasa a FINALIZADA, con los días de garantía que tiene su Servicio (por ejemplo, 30 días para Plomería).

**Alternativas consideradas**

1. Guardar la fecha de fin de la garantía como un atributo más dentro de OrdenServicio.
2. Crear la garantía junto con la orden, desde que está pendiente.

**Por qué**

Si la fecha de fin fuera un atributo suelto en OrdenServicio, la orden tendría que encargarse también de las reglas de vigencia y de revisión, y ya tiene muchas responsabilidades. Además, podría quedar una fecha de fin que no coincida con los días de garantía. Por eso la fecha de fin no se guarda: se calcula sumando los días a la fecha de inicio.

Crearla desde que la orden está pendiente tampoco tenía sentido: una orden cancelada quedaría con garantía, y los días empezarían a contar antes de que el técnico hiciera el trabajo.

**Consecuencia**

La relación es de composición: la garantía no existe sin su orden. Una vez creada, su fecha de inicio, sus días y su cobertura no se pueden cambiar, y la lista de revisiones solo se puede llenar con solicitarRevision, que revisa primero que la garantía esté vigente. Si el cliente pide una revisión cuando la garantía ya venció, o cuando la orden todavía no ha terminado, el sistema muestra un mensaje explicando por qué no se puede. La garantía y sus revisiones quedan guardadas en garantias.txt y revisiones_garantia.txt. Esta decisión apoya RF-07 y RF-18.

### Decisión 20 · CambioEstado es una clase anidada estática con constructor privado

**Decisión**

CambioEstado se define dentro de OrdenServicio como clase anidada estática. Guarda estado anterior, estado nuevo, fecha y responsable. Su constructor es privado, no tiene setters y la orden solo entrega una lista de solo lectura.

**Alternativas consideradas**

1. Una clase CambioEstado independiente, en su propio archivo.
2. Guardar solo el estado actual y no el historial.

**Por qué**

Un cambio de estado no tiene sentido sin una orden, así que va dentro de ella y no necesita rama propia. Es estática porque no necesita usar datos de la orden que la contiene. Con constructor privado, solo OrdenServicio puede crear cambios, y el historial no se puede falsificar ni modificar desde afuera.

**Consecuencia**

Toda transición queda registrada con su responsable (RNF-04). El único método que cambia el estado es `cambiarEstado`, que valida las transiciones permitidas: PENDIENTE a ASIGNADA o CANCELADA, ASIGNADA a EN_PROCESO o CANCELADA, y EN_PROCESO a FINALIZADA. Una orden no asignada no puede finalizarse (RF-06).


### Decisión 21 · Los costos se calculan en OrdenServicio, y la mano de obra la calcula Servicio

**Decisión**

`OrdenServicio` guarda el costo estimado y el costo final. El estimado se calcula con `calcularCostoEstimado` cuando el administrador le asigna el técnico, y el final se calcula dentro de `finalizar`, cuando el técnico termina el trabajo. Para ver si el trabajo salió más caro o más barato de lo esperado está `compararConEstimado`. La orden no hace todas las cuentas sola: la mano de obra la calcula `Servicio` (`calcularManoObra`) y el costo de los materiales lo suma `DetalleTrabajo` (`calcularCostoMateriales`).

**Alternativas consideradas**

1. Hacer una clase aparte, como `Cotizacion` o `CalculadoraCostos`, que se encargara de todos los cálculos.

**Por qué**

Todo lo que se necesita para calcular el costo ya está en la orden: el servicio, el técnico y el detalle del trabajo. Una clase aparte tendría que recibir esos mismos datos y solo se usaría para esto. Preferimos que cada clase haga la parte que le corresponde: `Servicio` sabe su factor de tarifa, `DetalleTrabajo` conoce sus materiales y la orden solo junta los resultados.

**Consecuencia**

La fórmula es: (horas × tarifa base del técnico × factor del tipo de servicio) + materiales. Para el estimado se usan las horas estimadas del servicio y los materiales que estima el administrador. Para el final se usan las horas reales y los materiales que registró el técnico. Si la orden se reasigna a otro técnico, el estimado se vuelve a calcular con la tarifa del nuevo. Al finalizar, primero se calcula todo y después se guarda; si falta algo, como el detalle del trabajo, la orden no cambia y se muestra un mensaje. Esta decisión apoya RF-05, RF-14, RF-16 y RNF-03.


### Decisión 22 · Validar reúne las validaciones y prohíbe los separadores de los archivos

**Decisión**

La clase `Validar` reúne las validaciones que se repetían (texto obligatorio, número positivo, objeto obligatorio) y además rechaza los caracteres `|` y `;` en los textos.

**Alternativa considerada**

Escapar esos caracteres al guardar y volver a convertirlos al leer.

**Por qué**

`|` separa los campos de los archivos y `;` separa las actividades de un trabajo. Si un nombre tuviera `|`, al leer el archivo los datos quedarían corridos. Rechazarlos con un mensaje claro es más sencillo que escaparlos.

**Consecuencia**

Si un usuario escribe `|` o `;`, recibe el mensaje: El campo "nombre" no puede contener el carácter '|'. Esta decisión apoya RNF-03 y RNF-06.


### Decisión 23 · Registrar técnicos y filtrar órdenes pasan a AdministradorServicio

**Decisión**

Los métodos `registrarTecnico` y `filtrarOrdenes` salen de la clase `Administrador` y pasan a `AdministradorServicio`. `Administrador` conserva `reasignarOrden`.

**Alternativa considerada**

Dejarlos en `Administrador`, recibiendo el `GestorPlataforma` como parámetro.

**Por qué**

En la arquitectura por capas, un modelo no debe depender de la capa de servicios. Si `Administrador` recibe el gestor, el paquete `modelos` queda dependiendo de `servicios` y las capas se mezclan.

**Consecuencia**

RF-01 y RF-20 siguen siendo acciones del administrador, pero se hacen desde su servicio. Así quedan también los servicios del cliente y del técnico: un servicio por rol.


### Decisión 24 · Asignacion guarda cada movimiento como un objeto

**Decisión**

`Asignacion` guarda cada asignación o reasignación en la clase anidada estática `Movimiento` (fecha, técnico anterior, técnico nuevo y responsable), en lugar de un texto. `getRegistroCambios()` sigue devolviendo el mismo texto que antes.

**Alternativa considerada**

Guardar en el archivo el texto del registro tal como estaba.

**Por qué**

El texto incluye el carácter `|` y los nombres de los técnicos, no sus documentos, así que no se podía volver a armar la asignación al leer el archivo.

**Consecuencia**

El archivo `asignaciones.txt` guarda los documentos de los técnicos, y al leerlo se recupera el historial completo de reasignaciones. Esta decisión apoya RF-14 y RNF-04.

### Decisión 25 · Interfaz gráfica con Swing, conservando la consola

**Decisión**

La aplicación se usa con ventanas hechas en Swing: inicio de sesión por rol y un panel para el cliente, el técnico y el administrador. `PanelRol` es una clase abstracta con la barra lateral, y `PanelCliente`, `PanelTecnico` y `PanelAdministrador` heredan de ella. Los menús de consola se conservan y se abren con el argumento `consola`.

**Alternativas consideradas**

1. Usar solo la consola.
2. Usar JavaFX.

**Por qué**

Las ventanas muestran mejor la información del dominio (agenda del técnico, estados con colores, costos). Swing viene incluido en el JDK, así que no hay que instalar librerías, a diferencia de JavaFX.

**Consecuencia**

Las ventanas usan los mismos servicios, repositorios y archivos que la consola: no se cambió la lógica del proyecto para agregarlas. Cada error se muestra en una ventana con un mensaje claro y los botones solo se activan cuando la acción tiene sentido. Esta decisión apoya RNF-02 y RNF-06.

### Decisión 26 · Usar solo librerías que vienen con Java

**Decisión**

No usamos ninguna librería externa. Todo el proyecto funciona con lo que ya trae Java: `java.util` para las listas, `java.time` para las fechas y horarios, `java.io` y `java.nio.file` para leer y escribir los archivos `.txt`, `java.security` para guardar las contraseñas con SHA-256 y `javax.swing` con `java.awt` para la interfaz gráfica.

**Alternativas consideradas**

1. Usar una base de datos (por ejemplo MySQL) en vez de archivos de texto.
2. Usar librerías externas como JavaFX para la interfaz o Gson para guardar los datos.

**Por qué**

Queríamos que cualquiera pudiera abrir el proyecto en IntelliJ y ejecutarlo sin instalar nada más. Una base de datos o una librería externa habría obligado a cada integrante, y al profesor, a descargarla y configurarla. Además, lo que necesitábamos ya lo trae Java, y así nos enfocamos en las clases y no en aprender herramientas nuevas.

**Consecuencia**

El proyecto no necesita Maven ni archivos `.jar` adicionales: basta con el JDK. Las fechas se manejan con `LocalDate` y `LocalDateTime` en vez de texto, lo que permite compararlas y calcular, por ejemplo, la fecha de fin de la garantía. Esta decisión apoya RNF-01, RNF-02 y RNF-05.

### Decisión 27 · Una clase base y dos interfaces para todos los repositorios

**Decisión**

Todos los repositorios heredan de `BaseRepository`, una clase abstracta que tiene lo que todos necesitan: un `FileManage` para leer y escribir su archivo `.txt` y métodos para separar una línea en campos, unir los campos en una línea y buscar, guardar o borrar una línea por su clave (el primer campo de cada línea). Además, cada repositorio implementa una de dos interfaces, según el tipo de dato que guarda:

- `IRepository<T>`, para los datos que existen por sí solos (clientes, técnicos, administradores, servicios, solicitudes y órdenes). Tiene el CRUD: leer todos (`getDataList`), crear (`insertData`), actualizar (`updateData`) y eliminar (`deleteData`).
- `IRepositorioDependiente<P>`, para los datos que pertenecen a otro objeto, al que llamamos "padre": los pagos, garantías, asignaciones, cambios de estado, detalles de trabajo, calificaciones y notificaciones pertenecen a una orden, y la disponibilidad pertenece a un técnico. Tiene `guardarDe(padre)`, `cargarEn(padre)` y `eliminarDe(idPadre)`.

`MaterialRepository` y `RevisionGarantiaRepository` no implementan ninguna interfaz porque los usan otros repositorios: los materiales se guardan desde `DetalleTrabajoRepository` y las revisiones desde `GarantiaRepository`.

**Alternativas consideradas**

1. Que cada repositorio escribiera su propio código para leer y escribir el archivo.
2. Usar solo `IRepository<T>` para todos los datos, incluidos los pagos y las garantías.

**Por qué**

Con la primera opción, el mismo código para separar líneas y buscar por clave se habría repetido 19 veces, y un error habría que arreglarlo en todas partes. Con la segunda, los pagos o las garantías tendrían que tener su propia clave y buscar a qué orden pertenecen. Es más natural guardarlos y cargarlos junto con su orden, porque no existen sin ella.

**Consecuencia**

Cada repositorio solo define el formato de su archivo y cómo convertir una línea en un objeto y al revés; lo demás lo hereda de `BaseRepository`. La `<T>` y la `<P>` son genéricos: permiten escribir la interfaz una sola vez y usarla con cualquier clase (`IRepository<Cliente>`, `IRepositorioDependiente<OrdenServicio>`...), y Java revisa que no se mezclen los tipos. Al iniciar, `GestorPlataforma` carga primero los datos principales y luego le pide a cada repositorio dependiente que meta sus datos dentro de su padre. Esta decisión apoya RNF-01 y RNF-04.

---

## Bitácora de uso de inteligencia artificial

Registro del uso de herramientas de IA durante el desarrollo del proyecto **Técnicos del Hogar a Domicilio**.


### Registro 1 · Descripción del problema y requerimientos

| Campo | Detalle |
|---|---|
| **Fecha** | 18/09/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo Claude Sonnet 5 |
| **Qué se le pidió** | Ayudar a estructurar la descripción del problema (contexto, actores, proceso actual, dolor e impacto) y el formato de los requerimientos de la Entrega 1, a partir de la idea de la plataforma de técnicos a domicilio propuesta por el equipo. |
| **Qué respondió** | Una propuesta de descripción del problema y una tabla con 12 requerimientos funcionales (RF-01 a RF-12) y 3 no funcionales (RNF-01 a RNF-03), cada uno con actor, prioridad y criterio de aceptación. |
| **Qué aceptamos** | La estructura de la descripción del problema y el formato de la tabla de requerimientos. |
| **Qué corregimos** | Dejamos 10 requerimientos funcionales y 1 no funcional. Quitamos el registro de materiales como requerimiento aparte y el límite diario de órdenes por técnico. Agregamos el documento de identificación y el horario disponible del técnico, el horario disponible en la solicitud del cliente y la tarifa base del técnico en el cálculo del precio. |


### Registro 2 · Priorización y orden de implementación de requerimientos

| Campo | Detalle |
|---|---|
| **Fecha** | 23/09/2026 |
| **Herramienta y modelo** | ChatGPT (OpenAI) · modelo GPT-5.6 Luna |
| **Qué se le pidió** | Revisar la tabla de trazabilidad, identificar si alguno tenía mayor prioridad y reorganizar los requisitos de acuerdo con su dependencia y orden lógico de implementación. |
| **Qué respondió** | Propuso reorganizar los requisitos desde las funcionalidades base (registro de técnicos y clientes), pasando por solicitudes, asignación y gestión de órdenes, hasta garantías, calificaciones y consultas. |
| **Qué aceptamos** | La reorganización general por prioridad, ya que permite desarrollar primero las funcionalidades que sirven como base para las demás. |
| **Qué corregimos** | Revisamos el orden de las prioridades y lo cambiamos para que tuviera más sentido. Pusimos primero las funciones que son necesarias para poder realizar las demás, como registrar técnicos y clientes. Después organizamos las funciones que dependen de esas, como solicitar y asignar un servicio, y al final dejamos las consultas y las funciones que se realizan cuando el servicio ya terminó. |



### Registro 3 · Validaciones de pago con excepciones

| Campo | Detalle |
|---|---|
| **Fecha** | 16/10/2026 |
| **Herramienta y modelo** | Codex (OpenAI) |
| **Qué se le pidió** | Añadir excepciones al código para impedir pagos con monto nulo o negativo, fechas nulas y referencias de autorización vacías en los pagos con tarjeta o transferencia. |
| **Qué respondió** | Propuso validar esos datos en los constructores y setters de las clases de pago, lanzando una excepción cuando el valor no sea válido. También aclaró que `double`, el tipo elegido para el monto, no puede ser nulo; para detectar un monto nulo habría que usar `Double`. |
| **Qué aceptamos** | Añadir validaciones para evitar montos negativos, fechas nulas y referencias de autorización vacías antes de guardar o actualizar un pago. |
| **Qué corregimos** | Como se decidió mantener el monto como `double`, se aclaró que no aplica una comprobación de nulidad para ese atributo. Se valida que el monto sea mayor que cero; si se necesita distinguir entre un monto nulo y uno informado, habría que cambiar su tipo a `Double`. |



### Registro 4 · Organización de las clases de notificaciones

| Campo | Detalle |
|---|---|
| **Fecha** | 16/10/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo claude-opus-5-5 |
| **Qué se le pidió** | No teníamos claro cómo organizar las clases de notificaciones (Notificacion, Notificable, NotificacionWhatsApp y NotificacionCorreo) para cumplir el RF-19, así que le preguntamos cómo podían relacionarse. |
| **Qué respondió** | Nos sugirió separar el aviso (Notificacion) del canal que lo envía (Notificable) y usar una interfaz en vez de herencia, porque WhatsApp y Correo solo comparten la acción de enviar. Nos dio un código base para las clases y un borrador de la decisión sobre notificaciones (hoy Decisión 10). |
| **Qué aceptamos** | La idea de usar la interfaz Notificable y la estructura general de las clases. |
| **Qué corregimos** | Cada uno revisó y ajustó en su rama la clase que le tocaba. Reescribimos la explicación porque era demasiado larga y movimos el enum `EstadoEnvio` al paquete `enums`, junto con `EstadoOrden` y `EstadoPago`, para tener todos los estados en un mismo lugar. |



### Registro 5 · Revisión y coherencia de las clases de pago y detalle del servicio

| Campo | Detalle |
|---|---|
| **Fecha** | 19/10/2026 |
| **Herramienta y modelo** | ChatGPT (OpenAI) · modelo GPT-6 |
| **Qué se le pidió** | Revisar el UML y la planificación de las clases `Pagable`, `DetalleTrabajo`, `Calificacion` y `Material`, contrastándolas con algunas clases ya desarrolladas y la documentación existente del proyecto. Ayudar a mantener nombres de atributos y métodos coherentes con el resto del documento. |
| **Qué respondió** | Propuso ajustes en los nombres de algunos atributos para mejorar la claridad y la coherencia del modelo. Se revisó el uso de `monto` en la clase relacionada con los pagos, propuso cambiarlo a `pago` y se planteó el cambio de `horas` a `horasReales` en `DetalleTrabajo`. |
| **Qué aceptamos** | Se aceptó el uso de `pago` en lugar de `monto` para nombrar el atributo correspondiente al valor monetario. También se aceptó cambiar el nombre relacionado con las horas, pero se eligió `horasTrabajo` en lugar de `horasReales`, ya que describe de forma más clara el tiempo dedicado al trabajo y evita confusiones con otra definición de horas existente en una clase diferente. |
| **Qué corregimos** | Se ajustó la propuesta para conservar la coherencia con el modelo existente. En lugar de aplicar directamente todos los nombres sugeridos, se priorizó que cada atributo expresara claramente su propósito y no se confundiera con conceptos definidos en otras clases. |



### Registro 6 · Revisión diagrama de clases y organización para mayor claridad
| Campo | Detalle |
|---|---|
| **Fecha**| 20/10/2026 |
| **Herramienta**| Lucidchart AI |
| **¿Qué se le pidió?** | Se pidió apoyo para revisar la claridad y el orden del diagrama UML, haciendo énfasis en los tipos de relaciones, las multiplicidades y la diferencia entre asociación, composición, herencia e implementación de interfaces. |
| **¿Qué respondió?** | La herramienta generó una propuesta de diagrama UML más organizada y legible, con las clases conectadas, incluyendo relaciones de asociación, herencia, composición e implementación de interfaces, además de las multiplicidades. |
| **¿Qué aceptamos?** | Aceptamos las sugerencias que ayudaron a mejorar la legibilidad del diagrama y a identificar con mayor claridad las relaciones entre las clases y su organización. |
| **¿Qué corregimos?** | Corregimos las relaciones que no representaban las reglas del negocio. En particular, revisamos las clases vinculadas a `OrdenServicio` y la representación de las clases anidadas y estáticas para asegurar que su estructura correspondiera con el diseño definido por el equipo. |


### Registro 7 · Revisión y ajustes de las clases del diagrama UML

| Campo | Detalle |
|---|---|
| **Fecha** | 20/10/2026 |
| **Herramienta** | ChatGPT |
| **¿Qué se le pidió?** | Se solicitó apoyo para revisar las clases `Tecnico`, `Asignacion`, `DisponibilidadTecnico` y `FranjaHoraria`, con el fin de aclarar algunas dudas sobre sus atributos, métodos y relaciones con las demás clases del diagrama UML. |
| **¿Qué respondió?** | La herramienta proporcionó explicaciones y sugerencias para mejorar la claridad de las responsabilidades de cada clase, revisar los métodos necesarios y comprender mejor las relaciones de herencia, asociación y composición. |
| **¿Qué aceptamos?** | Aceptamos las explicaciones y sugerencias que nos permitieron aclarar la función de cada clase, organizar mejor sus responsabilidades y comprender las conexiones representadas en el diagrama. |
| **¿Qué corregimos?** | Realizamos pequeños ajustes en la definición de algunos métodos y en la descripción de las relaciones entre las clases, procurando mantener el diseño original del equipo y cumplir con los requerimientos establecidos. |


### Registro 8 · Revisión de vacíos en el código de DisponibilidadTecnico

| Campo | Detalle |
|---|---|
| **Fecha** | 10/11/2026 |
| **Herramienta** | Claude (Anthropic) |
| **¿Qué se le pidió?** | Se solicitó ayuda para revisar el código de la clase `DisponibilidadTecnico`, verificando que estuviera correctamente relacionada con las demás clases del proyecto y que realizara las llamadas correspondientes entre ellas, de acuerdo con el diagrama UML. |
| **¿Qué respondió?** | Nos ayudó a revisar la estructura del código, explicó cómo se conectaba `DisponibilidadTecnico` con las demás clases y señaló algunos aspectos que se podían ajustar para que coincidiera con el diagrama UML. |
| **¿Qué aceptamos?** | Únicamente verificamos que el código de `DisponibilidadTecnico` estuviera funcionando correctamente y que sus conexiones con las demás clases correspondieran con el diagrama UML. |
| **¿Qué corregimos?** | Corregimos únicamente un par de detalles que podían mejorar el código, buscando que las conexiones y llamadas entre las clases fueran más claras y mantuvieran la coherencia con el diagrama UML. |


### Registro 9 · Integración a `main` de Servicio, Garantia y OrdenServicio

| Campo | Detalle |
|---|---|
| **Fecha** | 12/11/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo Claude Sonnet 5.5 |
| **Qué se le pidió** | Revisar `Servicio`, `Garantia` y `OrdenServicio`, subirlas a `main` y preparar sus pruebas y la explicación de la parte. |
| **Qué respondió** | Detectó que `OrdenServicio` no compilaba por `Pagable` (eliminada en la Decisión 17), propuso quitarla y subir las clases en 3 commits, y redactó `PruebaServicioGarantia.java` y `Explicacion_Clases_Vale.md`. |
| **Qué aceptamos** | Quitar `Pagable`, subir las tres clases a `main`, pasar las decisiones A–D a las decisiones 18–21 y marcar RF-06 y RF-07 como implementados. |
| **Qué corregimos** | Se pidió no tocar la bitácora hasta decidir cuánto registrar. RF-05, RF-16 y RF-18 se dejaron en "Pendiente" hasta que existan las clases de las que dependen. La explicación se actualizó para quitar `Pagable`. |

### Registro 10 · Repositorios, persistencia en archivos y organización por paquetes

| Campo | Detalle |
|---|---|
| **Fecha** | 17/11/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo claude-opus-5-5 |
| **Qué se le pidió** | Ya teníamos las clases del modelo, pero nos faltaba guardar los datos en archivos para que no se perdieran al cerrar el programa (RNF-01). Debíamos implementar los repositorios para poder convertirlos en texto usando también la persistencia. Le pedimos ayuda con esa parte. |
| **Qué respondió** | Nos ayudó a crear los repositorios a partir de las clases que teníamos: la interfaz `IRepository` con las operaciones CRUD (crear, leer, actualizar y eliminar) y un repositorio por cada archivo, además de 16 archivos de texto con datos de ejemplo. Los repositorios de los datos que dependen de una orden o de un técnico (pagos, garantías, asignaciones...) se conectan con su "padre" mediante `IRepositorioDependiente`. También nos ayudó a crear un servicio por rol y los menús de consola. |
| **Qué aceptamos** | Los repositorios y la persistencia en archivos `.txt` con CRUD y generando los ejemplos base para aplicarlo. |
| **Qué corregimos** | Le pedimos que respetara la estructura que queríamos tener. También le pedimos unificar todo en un solo README ya que teníamos secciones para toma de decisiones y uso de IA. Después de revisarlo, pedimos mejorar la parte visual de la que se encargó principalmente. |


### Registro 11 · Interfaz gráfica e integración a `main`

| Campo | Detalle |
|---|---|
| **Fecha** | 18/11/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo claude-opus-5-5 |
| **Qué se le pidió** | Mejorar la parte visual de la interfaz para que fuera acorde a una plataforma de técnicos del hogar y cumpliera todos los requerimientos; después, organizar el código por paquetes y subirlo al repositorio del equipo. |
| **Qué respondió** | Creó una interfaz gráfica en Swing con un panel por rol y la probó recorriendo los flujos de los tres roles. Al probarla encontró que se podía registrar dos veces el mismo pago y lo corrigió (ver la regla de pagos en Reglas de negocio). Preparó la integración a `main` en commits pequeños que compilan, adaptó las pruebas del equipo a los paquetes y actualizó el README, la trazabilidad y el registro de decisiones. |
| **Qué aceptamos** | La interfaz gráfica conservando la consola, la corrección de los pagos y la integración a `main` con la historia de las clases que ya estaban. |
| **Qué corregimos** | Se pidió organizar el código en los paquetes del proyecto: las ventanas pasaron de un paquete `gui` a `ui`, la clase `Validar` pasó de `utilidades` a `modelos` y quedó un solo `Main.java`. Al integrar se detectó que la versión unificada de `Servicio` había perdido los setters con validación que pide la Decisión 18, y se recuperaron para que las pruebas del equipo pasaran completas. Por último, se pidió reunir toda la documentación (estructura, trazabilidad, decisiones y bitácora) en un solo README. Después se pidió quitar la carpeta de pruebas y dejar en el código solo los comentarios que explican algo necesario. |

### Registro 12 · Cómo funcionan las interfaces de los repositorios

| Campo | Detalle |
|---|---|
| **Fecha** | 18/11/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo claude-opus-5-5 |
| **Qué se le pidió** | Los repositorios, `BaseRepository` y las interfaces `IRepository<T>` e `IRepositorioDependiente<P>` los creó la herramienta para conectar nuestras clases con los archivos `.txt`. Como eso no lo vimos en clase, le pedimos que nos explicara por qué los usó y cómo funcionan. |
| **Qué respondió** | Nos explicó que `BaseRepository` es una clase abstracta con el código que todos los repositorios necesitan (leer el archivo, separar y unir campos, buscar por clave), para no repetirlo. Que hay dos interfaces porque hay dos tipos de datos: los que existen solos, como un cliente, que usan `IRepository<T>` con el CRUD, y los que pertenecen a otro, como un pago, que es de una orden, que usan `IRepositorioDependiente<P>` y se guardan y cargan junto con su padre. También nos explicó que la `<T>` y la `<P>` son genéricos, que permiten usar la misma interfaz con distintas clases sin perder la revisión de tipos de Java. |
| **Qué aceptamos** | La estructura de los repositorios y la explicación, que usamos para escribir la Decisión 27 y la parte de repositorios de la jerarquía de clases. |
| **Qué corregimos / aprendimos** | Revisamos la explicación con el código de `PagoRepository` y `ClienteRepository` para entender el recorrido completo: cómo un objeto se convierte en una línea del `.txt` y cómo, al abrir el programa, cada línea vuelve a ser un objeto dentro de su orden. Aprendimos qué son los genéricos y la diferencia entre heredar de una clase abstracta (reutilizar código) e implementar una interfaz (cumplir un contrato). |


---

## Organización del repositorio
### Cómo trabajamos con Git

Antes de ponernos a trabajar, actualizamos `main` y revisamos con `git status` que no hubiera nada pendiente. Cada uno trabajó en su propia rama, según la clase o el requisito que le tocaba. Cuando terminábamos algo y lo revisábamos entre todos, lo uníamos a `main`. Si al unirlo algo fallaba o no compilaba, lo arreglábamos antes de seguir.

### Commits

Escribimos los mensajes de commit en español y tratamos de que dijeran claramente qué cambió, por ejemplo "agrega", "corrige" o "quita". Cuando el cambio tenía que ver con un requisito, poníamos su número al principio, por ejemplo: `RF-01: agrega validación de documento único en Tecnico`.

### Buenas prácticas

- Usar `git status` antes y después de cada operación.
- Hacer commits pequeños y descriptivos.
- Mantener configurado `.gitignore` para archivos compilados y del IDE.
- Integrar a `main` cambios que compilen.
- Evitar `push --force` y no reescribir historial compartido.

---
