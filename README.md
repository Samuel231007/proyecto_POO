# Técnicos del Hogar a Domicilio

Proyecto académico para el curso de **Programación Orientada a Objetos (POO)**.

Plataforma que formaliza la contratación de técnicos para el hogar (plomería, electricidad, pintura, cerrajería, carpintería). El cliente solicita un servicio, el administrador asigna un técnico disponible de esa especialidad y zona, el técnico registra el trabajo, y el sistema calcula costos, garantía y pagos, y le avisa al cliente cada cambio de estado.

El proyecto sigue la organización del proyecto base [SistemaGestionBiblioteca](https://github.com/wilmertri/SistemaGestionBiblioteca): arquitectura en **4 capas** y almacenamiento **dinámico en memoria** (ArrayList) o **permanente en archivos de texto** (.txt).

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
14. [Explicación de las clases por integrante](#explicación-de-las-clases-por-integrante)
15. [Organización del repositorio](#organización-del-repositorio)

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
| RF-19 | Enviar al cliente una notificación cuando cambie el estado de una orden, por WhatsApp o correo | **Notificacion**: conserva el aviso y su resultado; contador estático y enum anidado EstadoEnvio.<br>**Notificable**: interfaz con la operación de envío.<br>**NotificacionWhatsApp** y **NotificacionCorreo**: implementan los canales (envío simulado).<br>**Cliente**: recibe el aviso. | Interfaces, polimorfismo, atributos y métodos estáticos, clases anidadas y encapsulamiento | Implementado |
| RF-20 | Filtrar las órdenes por estado, fecha, cliente y técnico asignado | **Administrador**: selecciona filtros.<br>**OrdenServicio**: aporta estado, fecha y técnico.<br>**Cliente** y **Tecnico**: aportan los datos para filtrar. | Clases, métodos, colecciones y búsqueda | Implementado |
| RNF-01 | Guardar técnicos, clientes y órdenes en archivos entre ejecuciones | **Tecnico**, **Cliente** y **OrdenServicio**: datos que deben conservarse.<br>**FileManage** (paquete persistencia): lee y escribe los archivos de `src/files/`.<br>**BaseRepository** y un repositorio por archivo (paquete repositorios): convierten los objetos en líneas de texto y al revés.<br>**GestorPlataforma**: carga los datos al iniciar y los guarda después de cada cambio. | Persistencia de archivos | Implementado |
| RNF-02 | Restringir el acceso a la información según el rol y la relación del usuario con la orden | **Usuario**: clase abstracta con identidad y rol.<br>**Cliente**, **Tecnico** y **Administrador**: heredan de Usuario y acceden según sus permisos. | Clases abstractas, herencia y encapsulamiento | Implementado |
| RNF-03 | Mantener la integridad de órdenes, asignaciones, detalles de trabajo y pagos ante errores | **OrdenServicio**, **Asignacion**, **DetalleTrabajo** y **Pago**: datos que deben conservar coherencia. | Encapsulamiento, herencia, validación y asociaciones | Implementado |
| RNF-04 | Mantener trazabilidad de los cambios de estado y de las reasignaciones de técnicos | **OrdenServicio.CambioEstado**: conserva el historial de estados.<br>**Asignacion**: conserva el historial de reasignaciones. | Clases anidadas, asociaciones y encapsulamiento | Implementado |
| RNF-05 | Proteger los datos personales y de pago frente a accesos no autorizados | **Usuario**: clase abstracta con datos personales.<br>**Pago**: clase abstracta con datos comunes del pago.<br>**Tarjeta**: conserva solo la referencia de autorización. | Clases abstractas, herencia y encapsulamiento | Implementado |
| RNF-06 | Presentar mensajes claros durante las operaciones del sistema | **Cliente**, **Tecnico** y **Administrador**: reciben mensajes sobre el resultado de sus operaciones.<br>**Notificacion**: arma el aviso con `crearMensajeCambioEstado()` (método estático) y guarda la causa cuando el envío falla. | Métodos estáticos, interfaces y manejo de excepciones | Implementado |

---

## Conceptos POO demostrados

Cada concepto se puede rastrear a una o más clases concretas del proyecto:

### Herencia (IS-A)
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
`Tecnico` **es un** `Usuario`: hereda nombre, documento, correo, teléfono y contraseña cifrada, y agrega especialidad, zona y tarifa.

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
- No existe `getContrasenia()`: solo se guarda el código SHA-256 de la contraseña (RNF-05).
- Algunos métodos no son `public` para que solo los use su propio paquete. Por ejemplo, `OrdenServicio.asignarTecnico` solo lo llama `Asignacion`, después de revisar especialidad, zona y horario:
```java
// Sin "public": solo Asignacion la llama (RF-04 y RF-13).
void asignarTecnico(Tecnico nuevo, String responsable) { ... }
```

### Interfaces
| Interfaz | Para qué sirve | La implementan |
|---|---|---|
| `Notificable` | Enviar un aviso al cliente | `NotificacionWhatsApp`, `NotificacionCorreo` |
| `IRepository<T>` | CRUD sobre un archivo | `ClienteRepository`, `TecnicoRepository`, `OrdenServicioRepository`... |
| `IRepositorioDependiente<P>` | Guardar los datos que pertenecen a otro objeto | `PagoRepository`, `CambioEstadoRepository`, `DisponibilidadRepository`... |
| `IServicio<T>` | Consultas de cada servicio | `ClienteServicio`, `TecnicoServicio`, `AdministradorServicio` |
| `IImprimible` | Mostrar el objeto en consola | `Cliente`, `Tecnico`, `Servicio`, `OrdenServicio`, `Pago` |

### Composición (HAS-A)
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

### Sobreescritura de métodos (`@Override`)
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

### Genericidad (`<T>`)
`IRepository<T>`, `IServicio<T>` e `IRepositorioDependiente<P>` definen el contrato **una sola vez** para cualquier tipo: `IRepository<Cliente>`, `IRepository<OrdenServicio>`, `IRepositorioDependiente<Tecnico>`...

### Excepciones
Las reglas se validan lanzando `IllegalArgumentException` o `IllegalStateException` con un mensaje claro. La interfaz los atrapa y los muestra al usuario sin cerrar el programa (RNF-06).

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
    ├── modelos/                         Código del equipo (ver "Cambios" abajo)
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

> **RNF-05:** las contraseñas se guardan cifradas con SHA-256 (nunca en texto plano), y de la tarjeta solo se guarda la referencia de autorización, nunca el número ni el código de seguridad.

> **Composición en archivos:** igual que `prestamos.txt` en el proyecto base, `ordenes.txt` guarda solo los **IDs** del cliente, la solicitud, el servicio y el técnico. Al leer, `OrdenServicioRepository` busca los objetos completos y arma la orden.

---

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

Igual que `EstudianteServicio` en el proyecto base, `GestorPlataforma` funciona en dos modos según el constructor:

```java
new GestorPlataforma();        // MODO ARCHIVO: carga src/files/*.txt y guarda cada cambio (RNF-01)
new GestorPlataforma(false);   // MODO MEMORIA: solo ArrayList (lo usa DemoRequerimientos)
```

Los datos se cargan **una sola vez** al iniciar y todas las órdenes apuntan a los mismos objetos `Cliente` y `Tecnico` de las listas. Esto es necesario porque el código del equipo compara con `==` (por ejemplo `orden.getCliente() != this` en `Cliente.verificarPropia`). Si cada consulta volviera a leer el archivo, se crearían objetos nuevos y esas comparaciones fallarían.

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


### Decisión 2 · Solicitud y OrdenServicio como clases separadas

**Decisión**

Solicitud almacena el tipo de problema, la ubicación, la descripción y el horario disponible. OrdenServicio relaciona la solicitud con el técnico asignado y conserva los datos de ejecución y estado.

**Alternativas consideradas**

1. Guardar la solicitud, la asignación y el estado en una sola clase OrdenServicio.
2. Guardar la solicitud como atributos de Cliente.

**Por qué**

La solicitud existe antes de que se asigne un técnico. Separar ambas clases permite representar ese proceso y mantiene responsabilidades claras.

**Consecuencia**

OrdenServicio se asocia con Solicitud y Tecnico. También relaciona el servicio, la garantía, la calificación, los pagos y los cambios de estado. Esta decisión apoya RF-03, RF-04 y RF-05.


### Decisión 3 · DisponibilidadTecnico: separar los horarios de los datos del técnico

**Decisión**

Crear la clase `DisponibilidadTecnico` para registrar los horarios en los que un técnico puede trabajar y los bloques temporales en los que no puede recibir asignaciones. 

**Alternativa considerada**

1. Guardar todos los horarios disponibles y los bloqueos directamente como atributos de la clase `Tecnico`, sin separar la gestión de disponibilidad.
2. Registrar únicamente el horario habitual del técnico, sin contemplar los bloques de indisponibilidad temporal.

**Por qué**

Un técnico puede tener varios intervalos. Representarlos como objetos facilita registrar y consultar cada bloque sin acumular atributos repetidos en Tecnico. Se eligió separar esta responsabilidad para administrar los horarios y las indisponibilidades sin mezclar toda esa lógica con los datos personales y profesionales del técnico.

**Consecuencia**

Se relaciona por composición con `Tecnico` y contiene, también mediante composición, objetos de la clase `FranjaHoraria`. Por esta razón, no se considera completamente independiente de esas relaciones dentro del modelo. Cumple directamente con el **RF-13**, al permitir registrar bloques de indisponibilidad temporal, y apoya el **RF-04**, porque permite consultar la disponibilidad del técnico antes de asignarle un servicio.



### Decisión 4 · Asignacion: vincular órdenes y conservar el historial

**Decisión**

Crear la clase `Asignacion` para vincular un técnico con una orden de servicio. También permite representar la reasignación cuando el técnico inicialmente asignado no puede atenderla y conservar las asignaciones anteriores para mantener la trazabilidad.

**Alternativas consideradas**

1. Guardar el técnico asignado directamente en `OrdenServicio`, sin crear una clase independiente para gestionar las asignaciones y sus cambios.
2. Cambiar directamente el técnico asociado a la orden sin conservar un historial de las asignaciones anteriores.

**Por qué**

Se eligió crear una clase propia para centralizar la asignación de técnicos a las órdenes y facilitar el registro de las reasignaciones y de los técnicos anteriores.

**Consecuencia**

Se asocia con `Tecnico` y con `OrdenServicio`. No es completamente independiente, porque necesita relacionar ambas clases para cumplir su función. Apoya el **RF-04**, al representar la asignación del técnico al servicio; el **RF-14**, al permitir representar las reasignaciones; y el **RNF-04**, al conservar la trazabilidad de los cambios de asignación.


### Decisión 5 · DetalleTrabajo y Material registran la ejecución

**Decisión**

Crear DetalleTrabajo para registrar actividades y tiempo real. Crear Material para registrar los insumos usados, sus cantidades y costos.

**Alternativa considerada**

Guardar actividades, tiempo y materiales directamente en OrdenServicio.

**Por qué**

La orden puede tener varios detalles y materiales. Mantenerlos como objetos relacionados evita que OrdenServicio concentre todos esos datos.

**Consecuencia**

OrdenServicio reúne los detalles y materiales necesarios para calcular el costo final. Esta decisión apoya RF-15, RF-16 y RNF-03.


### Decisión 6 · Pago como clase abstracta y medios de pago como subclases

**Decisión**

Usar Pago como clase abstracta para reunir monto, fecha y estado. Transferencia, Efectivo y Tarjeta heredan de Pago y añaden solo la información propia de su medio.

**Alternativas consideradas**

1. Repetir monto, fecha y estado en las tres clases de pago.
2. Usar una interfaz Pago para compartir los atributos comunes.

**Por qué**

Las interfaces definen operaciones, pero no comparten atributos de instancia. La clase abstracta permite reutilizar los datos comunes y reducir duplicación. Cada subclase implementa getMetodoPago().

**Consecuencia**

Pago usa double para monto, LocalDate para fecha y String para estado. Además, ofrece confirmarPago(), rechazarPago() y estaConfirmado(). Transferencia almacena su referencia bancaria; Tarjeta almacena la referencia de autorización y no guarda el número completo ni el código de seguridad; Efectivo reutiliza los datos heredados. Esta decisión apoya RF-17, RNF-03 y RNF-05.


### Decisión 7 · Pagable es una interfaz independiente de Pago

**Decisión**

Mantener Pagable como interfaz con las operaciones que ofrece una orden para recibir pagos y consultar el saldo pendiente. OrdenServicio implementa Pagable.

**Alternativa considerada**

Usar Pago como interfaz y como contrato de la orden.

**Por qué**

Pago representa cada pago realizado; Pagable representa la capacidad de una orden para aceptar pagos. Son responsabilidades distintas.

**Consecuencia**

OrdenServicio implementa Pagable y los objetos Transferencia, Efectivo y Tarjeta heredan de Pago. Esta decisión apoya RF-17.


### Decisión 8 · Notificacion y Notificable tienen responsabilidades distintas

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


### Decisión 9 · Clases anidadas relacionadas con su clase contenedora

**Decisión**

Mantener FranjaHoraria dentro de DisponibilidadTecnico y CambioEstado dentro de OrdenServicio como clases anidadas estáticas.

**Por qué**

Cada clase anidada representa un concepto estrechamente relacionado con su clase contenedora y no requiere una rama independiente.

**Consecuencia**

Las clases se implementan en los archivos de sus contenedoras. FranjaHoraria apoya RF-13 y CambioEstado apoya RNF-04.


### Decisión 10 · Clase Tecnico: heredar lo común y especializar el servicio

**Decisión**

Crear la clase `Tecnico` para representar a los técnicos que prestan los servicios. Contiene sus datos específicos, como especialidad, zona de cobertura, horario disponible y tarifa base. Además, hereda de la clase `Usuario` los datos personales y permite representar la recepción de solicitudes.

**Alternativas consideradas**

1. Crear una clase independiente que incluya todos los datos personales y los datos específicos del técnico, sin heredar de `Usuario`.
2. Incluir los datos personales y las características del técnico en una sola clase, sin utilizar herencia.

**Por qué**

Se eligió la herencia para reutilizar los datos de `Usuario` y mantener en `Tecnico` únicamente la información y las funciones propias de los técnicos.

**Consecuencia**

La clase hereda los atributos de `Usuario`, se relaciona por composición con `DisponibilidadTecnico` y por asociación con `Asignacion`. Apoya el **RF-01**, porque permite registrar los datos y características del técnico, y contribuye al **RF-04**, porque su especialidad, zona y disponibilidad sirven para determinar si puede atender un servicio solicitado.


### Decisión 11 · Clase FranjaHoraria: organizar cada intervalo como una unidad

**Decisión**

Crear la clase anidada estática `FranjaHoraria` dentro de `DisponibilidadTecnico` para representar un día específico y un intervalo horario mediante una hora de inicio y una hora de finalización. También permite validar los intervalos y comprobar si dos franjas se entrelazan.

**Alternativas consideradas**

1. Representar cada franja horaria mediante datos separados, como el día, la hora de inicio y la hora de finalización, directamente dentro de `DisponibilidadTecnico`, sin crear una clase específica.
2. Crear `FranjaHoraria` como una clase independiente, fuera de `DisponibilidadTecnico`.

**Por qué**

Se eligió una clase anidada para organizar cada intervalo horario como una unidad y facilitar la validación de los horarios y los bloques de indisponibilidad.

**Consecuencia**

Está relacionada con `DisponibilidadTecnico` mediante composición, por lo que cada franja forma parte de la disponibilidad que la contiene. Su función está vinculada a esa clase y apoya el **RF-13**, porque permite representar y validar los intervalos horarios en los que el técnico no puede recibir asignaciones.


### Decisión 12 · Utilizar herencia para Cliente, Tecnico y Administrador

**Decisión**

Se estableció una relación de herencia entre Usuario y sus tres clases derivadas: Cliente, Tecnico y Administrador.

**Por qué**

Aunque los tres tipos de usuario comparten información básica, cada uno cumple funciones diferentes dentro de la plataforma.

**Consecuencia**

Cada clase conserva los atributos y comportamientos generales de Usuario y añade los propios de su rol. Por ejemplo, Cliente gestiona solicitudes, Tecnico administra su disponibilidad y Administrador gestiona usuarios y órdenes de servicio.


### Decisión 13 · Asociar Cliente con Solicitud

**Decisión**

Se decidió asociar Cliente y Solicitud, de manera que un cliente puede crear varias solicitudes y cada solicitud pertenece a un cliente (multiplicidad).

**Por qué**

El sistema necesita identificar quién solicita un servicio y mantener relacionadas las solicitudes con la persona que las creó, además esto se complementa para el cumplimiento del RF-03 que exige que el cliente solicite un servicio y registre la información necesaria.

**Consecuencia**

La plataforma puede consultar las solicitudes de cada cliente, identificar al responsable de una solicitud y facilitar las operaciones como consultar.


### Decisión 14 · Incorporar la actualización de datos para el RF-11

**Decisión**

Se incluyó en Cliente la operación `actualizarDatos()` para modificar la información personal del cliente.

**Por qué**

Los datos de contacto o ubicación del cliente pueden cambiar y el sistema debe permitir que la información registrada se mantenga actualizada cada vez que sea necesario.

**Consecuencia**

El cliente puede actualizar sus datos sin necesidad de crear una cuenta nueva. La operación debe verificar que la información ingresada sea válida y guardar los cambios correctamente.


### Decisión 15 · Controlar la cancelación de solicitudes para el RF-12

**Decisión**

Se asignó a Solicitud la responsabilidad de comprobar si una petición puede cancelarse por medio de `validarCancelacion()` y `cancelar()`, esto de la mano de crear `EstadoSolicitud` con los estados posibles de la solicitud para consultas y validaciones de los métodos nombrados.

**Por qué**

La cancelación depende del estado en el que se encuentre la solicitud. No se permite que cualquier solicitud se cancele sin verificar las condiciones del servicio.

**Consecuencia**

El sistema puede impedir cancelaciones que no sean permitidas por las reglas del negocio. Por ejemplo, si las condiciones definidas permiten cancelar solo las solicitudes pendientes, la operación debe rechazar la cancelación cuando la solicitud ya esté asignada.


### Decisión 16 · Aplicar encapsulamiento y validación

**Decisión**

Se decidió mantener los atributos de las clases como privados y acceder a ellos mediante operaciones controladas cuando sea necesario en los métodos.

**Por qué**

Los datos de los usuarios y de las solicitudes no deben modificarse sin verificar las reglas correspondientes y con un objetivo específico.

**Consecuencia**

Se reduce el riesgo de cambios inconsistentes, se facilita la validación de la información y se tiene un mayor control sobre las operaciones de registro, actualización y cancelación.

### Decisión 17 · Eliminar la interfaz Pagable

**Decisión**

Eliminar la interfaz Pagable y trasladar sus operaciones directamente a OrdenServicio, ya que era la única clase que la implementaba.

**Alternativa considerada**

Mantener Pagable como una interfaz independiente para definir las operaciones de registro de pagos y consulta del saldo pendiente.

**Por qué**

Pagable solamente era implementada por OrdenServicio, por lo que no aportaba reutilización ni una ventaja significativa de diseño. Mantenerla añadía una estructura innecesaria al proyecto.

**Consecuencia**

OrdenServicio incorpora directamente las operaciones establecidas, manteniendo la funcionalidad de gestión de pagos sin depender de una interfaz adicional. Se simplifica la estructura del modelo y se evita utilizar abstracciones que no aportan beneficios concretos.


### Decisión 18 · Servicio concentra la tarifa, la duración y la garantía de cada tipo

**Decisión**

Crear la clase Servicio con el tipo, un factor de tarifa propio, la duración estimada y los días de garantía. OrdenServicio le pide a Servicio el cálculo de la mano de obra (`calcularManoObra`).

**Alternativas consideradas**

1. Guardar el tipo como un texto en OrdenServicio y calcular el precio con `if` según el tipo ("plomería", "electricidad"...).
2. Crear una subclase por tipo (Plomeria, Electricidad, Pintura) que herede de Servicio.

**Por qué**

Los `if` por tipo obligan a modificar OrdenServicio cada vez que aparece un tipo nuevo. Las subclases serían herencia solo para cambiar un número, y todos los tipos tienen los mismos datos y el mismo comportamiento; solo cambian los valores. Con un objeto Servicio por tipo, cada uno guarda su propia tarifa y garantía sin repetir código.

**Consecuencia**

Agregar un tipo nuevo es crear un objeto Servicio con otros valores, sin tocar OrdenServicio. Se cumple el criterio de RF-05 ("cada tipo de servicio aplica su propia tarifa") y el de RF-07 ("cada tipo define su propio tiempo de garantía"). Sus atributos son privados y los setters validan los datos.


### Decisión 19 · Garantia es una clase propia que se crea al finalizar la orden

**Decisión**

Garantia guarda la fecha de inicio, los días de vigencia y la cobertura, y decide por sí misma si está vigente (`estaVigente`) y si acepta una revisión (`solicitarRevision`). OrdenServicio crea la garantía cuando la orden pasa a FINALIZADA, con los días que define su Servicio.

**Alternativas consideradas**

1. Guardar la fecha de fin de la garantía como un atributo suelto dentro de OrdenServicio.
2. Crear la garantía junto con la orden, desde que está pendiente.

**Por qué**

La garantía solo tiene sentido cuando el trabajo terminó. Si fuera un atributo suelto, OrdenServicio tendría que conocer las reglas de vigencia y de revisión. Al ser una clase, esas reglas quedan en un solo lugar.

**Consecuencia**

La relación es de composición: sin la orden no existe la garantía. Una revisión fuera de la vigencia lanza una excepción con un mensaje claro. Esta decisión apoya RF-07 y RF-18.


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


### Decisión 21 · Los costos se calculan en OrdenServicio, delegando la mano de obra a Servicio

**Decisión**

OrdenServicio guarda el costo estimado y el final, y calcula cada uno con `calcularCostoEstimado` y `calcularCostoFinal`. La comparación está en `compararConEstimado`. La parte de la mano de obra la calcula Servicio.

**Alternativa considerada**

Hacer una clase aparte Cotizacion o CalculadoraCostos.

**Por qué**

El costo depende de datos que ya tiene la orden (el servicio, el técnico y el estado). Una clase aparte solo repetiría esos datos y sería una clase con un solo uso.

**Consecuencia**

La fórmula es: horas x tarifa base del técnico x factor del tipo + materiales. El estimado usa la duración estimada de Servicio y el final usa las horas reales. Esta decisión apoya RF-05 y RF-16.


### Decisión 22 · Organizar el código en paquetes y capas, como el proyecto base

**Decisión**

El código va en `src/` con los mismos paquetes del proyecto base del profesor: `modelos`, `enums`, `interfaces`, `persistencia`, `repositorios`, `servicios`, `ui` y `files`, y un único `Main.java`. Cada capa solo usa la de abajo: ui → servicios → repositorios y persistencia → modelos.

**Alternativas consideradas**

1. Dejar todas las clases en la raíz del repositorio, sin paquetes.
2. Crear paquetes propios distintos a los del proyecto base (por ejemplo `gui` o `utilidades`).

**Por qué**

Sin paquetes, la lógica, los archivos y la interfaz quedan mezclados y cuesta saber dónde va cada cosa. Usar los mismos paquetes del proyecto base hace que el profesor reconozca la estructura y que cada capa tenga una sola responsabilidad.

**Consecuencia**

Las clases que estaban en la raíz se movieron con `git mv` para conservar su historia. La clase de apoyo `Validar` quedó en `modelos` porque la usan los modelos, y las ventanas quedaron en `ui` junto a los menús de consola. Esta decisión apoya RNF-03.


### Decisión 23 · Guardar los datos en archivos .txt con un repositorio por archivo

**Decisión**

Cada tipo de dato se guarda en su propio archivo de `src/files/` (clientes, técnicos, órdenes, pagos...). `FileManage` lee y escribe las líneas, y cada repositorio convierte los objetos en líneas separadas por `|` y al revés. El primer campo de cada línea es su clave (documento, código de la orden o id). Lo que pertenece a una orden (cambios de estado, pagos, avisos...) se guarda en su propio archivo con el código de la orden al inicio, y lo maneja un repositorio que implementa la interfaz genérica `IRepositorioDependiente<P>`.

**Alternativas consideradas**

1. Serializar todo en un solo archivo binario (`plataforma.dat`).
2. Guardar cada orden con todos sus datos en una sola línea.

**Por qué**

El proyecto base usa archivos de texto con repositorios, y un archivo de texto se puede abrir y revisar. Una sola línea por orden con listas adentro (pagos, cambios, avisos) sería difícil de leer y de actualizar.

**Consecuencia**

Los datos se cargan una sola vez al iniciar y todas las órdenes apuntan a los mismos objetos `Cliente` y `Tecnico`, porque el código compara con `==` (por ejemplo en `Cliente.verificarPropia`). Para leer los archivos se agregaron constructores de reconstrucción (sobrecarga) y métodos `restaurar...` que no vuelven a enviar avisos. Los archivos se escriben en UTF-8 y primero en un archivo temporal, para que un error no deje el archivo a medias (RNF-03). Esta decisión apoya RNF-01.


### Decisión 24 · Los enums de estado pasan al paquete enums

**Decisión**

`OrdenServicio.Estado`, `Pago.Estado` y `Notificacion.EstadoEnvio` pasan a ser `EstadoOrden`, `EstadoPago` y `EstadoEnvio` en el paquete `enums`.

**Alternativa considerada**

Dejarlos como enums anidados dentro de sus clases.

**Por qué**

El proyecto base tiene un paquete `enums`, y estos estados los usan varias capas (repositorios, servicios e interfaz). La Solicitud ya usaba el mismo estado de la orden, así que no pertenecía solo a `OrdenServicio`.

**Consecuencia**

Las clases anidadas que no son enums (`CambioEstado`, `FranjaHoraria`, `Material`) siguen dentro de su clase contenedora, como dice la Decisión 9.


### Decisión 25 · Validar reúne las validaciones y prohíbe los separadores de los archivos

**Decisión**

La clase `Validar` reúne las validaciones que se repetían (texto obligatorio, número positivo, objeto obligatorio) y además rechaza los caracteres `|` y `;` en los textos.

**Alternativa considerada**

Escapar esos caracteres al guardar y volver a convertirlos al leer.

**Por qué**

`|` separa los campos de los archivos y `;` separa las actividades de un trabajo. Si un nombre tuviera `|`, al leer el archivo los datos quedarían corridos. Rechazarlos con un mensaje claro es más sencillo que escaparlos.

**Consecuencia**

Si un usuario escribe `|` o `;`, recibe el mensaje: El campo "nombre" no puede contener el carácter '|'. Esta decisión apoya RNF-03 y RNF-06.


### Decisión 26 · Registrar técnicos y filtrar órdenes pasan a AdministradorServicio

**Decisión**

Los métodos `registrarTecnico` y `filtrarOrdenes` salen de la clase `Administrador` y pasan a `AdministradorServicio`. `Administrador` conserva `reasignarOrden`.

**Alternativa considerada**

Dejarlos en `Administrador`, recibiendo el `GestorPlataforma` como parámetro.

**Por qué**

En la arquitectura por capas, un modelo no debe depender de la capa de servicios. Si `Administrador` recibe el gestor, el paquete `modelos` queda dependiendo de `servicios` y las capas se mezclan.

**Consecuencia**

RF-01 y RF-20 siguen siendo acciones del administrador, pero se hacen desde su servicio. Así quedan también los servicios del cliente y del técnico: un servicio por rol.


### Decisión 27 · Asignacion guarda cada movimiento como un objeto

**Decisión**

`Asignacion` guarda cada asignación o reasignación en la clase anidada estática `Movimiento` (fecha, técnico anterior, técnico nuevo y responsable), en lugar de un texto. `getRegistroCambios()` sigue devolviendo el mismo texto que antes.

**Alternativa considerada**

Guardar en el archivo el texto del registro tal como estaba.

**Por qué**

El texto incluye el carácter `|` y los nombres de los técnicos, no sus documentos, así que no se podía volver a armar la asignación al leer el archivo.

**Consecuencia**

El archivo `asignaciones.txt` guarda los documentos de los técnicos, y al leerlo se recupera el historial completo de reasignaciones. Esta decisión apoya RF-14 y RNF-04.


### Decisión 28 · Los pagos pendientes cuentan al registrar un pago nuevo

**Decisión**

`OrdenServicio.registrarPago` rechaza un pago si, sumado a los pagos que esperan confirmación, supera el saldo de la orden.

**Alternativa considerada**

Contar solo los pagos confirmados, como estaba antes.

**Por qué**

Al probar la interfaz se encontró que una orden con $141.000 de saldo y un pago de $141.000 pendiente aceptaba otro pago igual, así que se podía cobrar dos veces lo mismo.

**Consecuencia**

El mensaje explica cuánto está en revisión y cuánto falta por pagar. Si el administrador rechaza un pago, ese valor vuelve a quedar disponible. Esta decisión apoya RF-17 y RNF-03.


### Decisión 29 · Interfaz gráfica con Swing, conservando la consola

**Decisión**

La aplicación se usa con ventanas hechas en Swing: inicio de sesión por rol y un panel para el cliente, el técnico y el administrador. `PanelRol` es una clase abstracta con la barra lateral, y `PanelCliente`, `PanelTecnico` y `PanelAdministrador` heredan de ella. Los menús de consola del estilo del proyecto base se conservan y se abren con el argumento `consola`.

**Alternativas consideradas**

1. Usar solo la consola.
2. Usar JavaFX.

**Por qué**

Las ventanas muestran mejor la información del dominio (agenda del técnico, estados con colores, costos). Swing viene incluido en el JDK, así que no hay que instalar librerías, a diferencia de JavaFX.

**Consecuencia**

Las ventanas usan los mismos servicios, repositorios y archivos que la consola: no se cambió la lógica del proyecto para agregarlas. Cada error se muestra en una ventana con un mensaje claro y los botones solo se activan cuando la acción tiene sentido. Esta decisión apoya RNF-02 y RNF-06.

### Decisión 30 · Código con comentarios solo donde explican algo y sin carpeta de pruebas

**Decisión**

El código conserva solo los comentarios que explican algo que no se ve a simple vista: una regla de negocio, el porqué de una decisión, el formato de cada archivo `.txt` o el requerimiento que se cumple. Se quitan las introducciones largas, los separadores de sección y los comentarios que repiten lo que el código ya dice. También se quita la carpeta `pruebas/`.

**Alternativa considerada**

Mantener los comentarios largos al inicio de cada clase y las pruebas del equipo.

**Por qué**

Los comentarios largos hacían el código más difícil de leer y repetían lo que ya está en este README (arquitectura, formatos y decisiones). Las pruebas cumplieron su función durante la integración: todas pasaron (39 de notificaciones y 28 de servicio y garantía), y la demo de requerimientos sigue recorriendo todo el sistema.

**Consecuencia**

El código pasó de 764 a cerca de 170 líneas de comentarios, sin cambiar ninguna instrucción: las clases compiladas son idénticas antes y después. Las pruebas siguen disponibles en el historial de Git.

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
| **Qué se le pidió** | Ayuda para organizar las clases `Notificacion`, `Notificable`, `NotificacionWhatsApp` y `NotificacionCorreo` según el Excel de requerimientos y lo que pide el profesor: qué hace cada clase, cómo se relacionan y qué decisión de diseño las justifica. |
| **Qué respondió** | Propuso separar `Notificacion` (el registro del aviso) de `Notificable` (interfaz con `enviar`), con WhatsApp y Correo como implementaciones, y explicó por qué interfaz y no herencia. Propuso atributos privados, un contador estático, el enum anidado `EstadoEnvio` y las relaciones del UML. Redactó un borrador de la Decisión 8 y de la trazabilidad de RF-19, RNF-04 y RNF-06. |
| **Qué aceptamos** | La estructura de las cuatro clases con la interfaz `Notificable`, la Decisión 8 del registro de decisiones y el código de cada clase en su rama. |
| **Qué corregimos** | La explicación escrita salió muy larga y se acortó de unas 1400 a unas 430 palabras para que quedara con una extensión similar a la de los demás integrantes. |



### Registro 5 · Revisión y coherencia de las clases de pago y detalle del servicio

| Campo | Detalle |
|---|---|
| **Fecha** | 19/10/2026 |
| **Herramienta y modelo** | ChatGPT (OpenAI) · modelo GPT-6 |
| **Qué se le pidió** | Revisar el UML y la planificación de las clases `Pagable`, `DetalleTrabajo`, `Calificacion` y `Material`, contrastándolas con algunas clases ya desarrolladas y la documentación existente del proyecto. Ayudar a mantener nombres de atributos y métodos coherentes con el resto del documento  |
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




### Registro 8 · Revisión de vacios en el código de DisponibilidadTecnico

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


### Registro 10 · Integración a `main` de las clases de notificaciones

| Campo | Detalle |
|---|---|
| **Fecha** | 12/11/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo Claude Sonnet 5.5 |
| **Qué se le pidió** | Comprobar los commits de cada integrante, subir a `main` las 4 clases de notificaciones y preparar sus pruebas y su explicación. |
| **Qué respondió** | Contó los commits del repositorio, explicó cómo verificar la subida en GitHub y generó los scripts para llevar las clases a `main` en 3 commits. También redactó `PruebaNotificaciones.java` y `Explicacion_Notificaciones.md`. |
| **Qué aceptamos** | Llevar las 4 clases a `main` sin cambiar su código y marcar RF-19 como implementado. |
| **Qué corregimos** | Windows bloqueó el archivo `.bat`, así que se usó una versión para Git Bash. No se tocó la bitácora hasta decidir cuánto registrar. |


### Registro 11 · Repositorios, persistencia en archivos y organización por paquetes

| Campo | Detalle |
|---|---|
| **Fecha** | 17/11/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo claude-opus-5-5 |
| **Qué se le pidió** | Completar el código unificado del proyecto según los requerimientos, tomando como base el repositorio del profesor (SistemaGestionBiblioteca): organizarlo por paquetes y crear los repositorios, la persistencia y los archivos de texto que faltaban. |
| **Qué respondió** | Organizó las clases en los paquetes del proyecto base, creó `FileManage`, `BaseRepository` y un repositorio por archivo `.txt` (16 archivos con datos de ejemplo), la capa de servicios con un servicio por rol y los menús de consola. Hizo los ajustes mínimos a los modelos para poder leerlos de los archivos y probó que los datos se conservan al cerrar y volver a abrir el programa. |
| **Qué aceptamos** | La estructura por paquetes, la persistencia en archivos `.txt` y las decisiones 22 a 27 del registro de decisiones. |
| **Qué corregimos** | Se pidió una imagen de cómo se veía la interfaz para revisarla; al verla, se pidió mejorar la parte visual (Registro 12). |


### Registro 12 · Interfaz gráfica e integración a `main`

| Campo | Detalle |
|---|---|
| **Fecha** | 18/11/2026 |
| **Herramienta y modelo** | Claude (Anthropic) · modelo claude-opus-5-5 |
| **Qué se le pidió** | Mejorar la parte visual de la interfaz para que fuera acorde a una plataforma de técnicos del hogar y cumpliera todos los requerimientos; después, ajustar el código a la estructura exacta del repositorio del profesor y subirlo al repositorio del equipo. |
| **Qué respondió** | Creó una interfaz gráfica en Swing con un panel por rol y la probó recorriendo los flujos de los tres roles. Al probarla encontró que se podía registrar dos veces el mismo pago y lo corrigió (Decisión 28). Preparó la integración a `main` en commits pequeños que compilan, adaptó las pruebas del equipo a los paquetes y actualizó el README, la trazabilidad y el registro de decisiones. |
| **Qué aceptamos** | La interfaz gráfica conservando la consola, la corrección de los pagos y la integración a `main` con la historia de las clases que ya estaban. |
| **Qué corregimos** | Se pidió seguir exactamente la estructura del repositorio del profesor: las ventanas pasaron de un paquete `gui` a `ui`, la clase `Validar` pasó de `utilidades` a `modelos` y quedó un solo `Main.java`. Al integrar se detectó que la versión unificada de `Servicio` había perdido los setters con validación que pide la Decisión 18, y se recuperaron para que las pruebas del equipo pasaran completas. Por último, se pidió reunir toda la documentación (estructura, trazabilidad, decisiones y bitácora) en un solo README con el formato del proyecto base. Después se pidió quitar la carpeta de pruebas y dejar en el código solo los comentarios que explican algo necesario. |


*Se irán agregando nuevos registros a medida que el equipo use IA en el desarrollo del proyecto (diseño de clases, generación de código, documentación, etc.), cada uno con su fecha, herramienta, lo pedido, lo respondido, lo aceptado y lo corregido.*

---

## Explicación de las clases por integrante

Explicaciones escritas por cada integrante sobre sus clases. Describen las clases como estaban antes de la integración: después de ella, los enums anidados pasaron al paquete `enums` (`OrdenServicio.Estado` → `EstadoOrden`, `Notificacion.EstadoEnvio` → `EstadoEnvio`, Decisión 24).

### Parte de Valeria Alvarado · Orden de servicio

**Clases:** `OrdenServicio` (con la clase anidada `CambioEstado` y el enum anidado `Estado`), `Servicio` y `Garantia`\
**Requerimientos:** RF-05, RF-06, RF-16 y RF-18; también apoya RF-07, RF-17, RF-19 y RNF-04\
**Decisiones relacionadas del registro:** Decisión 2 (Solicitud y OrdenServicio separadas), Decisión 5 (detalle y materiales), Decisión 9 (clases anidadas), Decisión 17 (se eliminó `Pagable`) y Decisiones 18 a 21 (las de esta parte)

*Cómo leer las tablas:* los atributos y métodos se escriben con su tipo de acceso (público o privado). Lo **estático** se marca con la palabra «estático». La clase anidada y el enum anidado se nombran completos (`OrdenServicio.CambioEstado` y `OrdenServicio.Estado`) para que no se confundan con los enums de otras clases. Las relaciones entre las clases están explicadas en la sección 3.

#### 1. Idea general

`OrdenServicio` es la clase central del proceso: reúne el servicio pedido, el técnico asignado, el estado, los costos, la garantía y los pagos. Cada vez que cambia de estado deja un registro (`CambioEstado`). Al finalizar, crea la `Garantia` del servicio con los días que define su tipo (`Servicio`). También recibe pagos y dice cuánto falta por pagar: desde la Decisión 17 esas dos operaciones (`registrarPago` y `getSaldoPendiente`) están directamente en `OrdenServicio`, porque la interfaz `Pagable` se eliminó.

#### 2. Clases, atributos y métodos

##### `Servicio`

| Miembro | Acceso | Para qué sirve |
|---|---|---|
| `FACTOR_NORMAL: double` (**estático**, constante) | público | Factor de una tarifa normal (1.0); es igual para todos los servicios. |
| `tipo`, `factorTarifa`, `duracionEstimadaHoras`, `diasGarantia` | privados | Datos propios de cada tipo de servicio: nombre, recargo, duración esperada y días de garantía. |
| Constructor y setters | públicos | Validan que los datos no sean vacíos ni negativos. |
| `calcularManoObra(horas, tarifaBaseTecnico)` | público | Horas x tarifa base del técnico x factor del tipo (RF-05 y RF-16). |
| `getTipo()`, `getDuracionEstimadaHoras()`, `getDiasGarantia()` | públicos | Solo los getters que `OrdenServicio` necesita para calcular costos y crear la garantía. |

##### `Garantia`

| Miembro | Acceso | Para qué sirve |
|---|---|---|
| `fechaInicio`, `diasVigencia`, `cobertura` | privados (`final`) | Periodo y cobertura; no cambian después de crearla. |
| `revisiones: List<String>` | privado | Revisiones pedidas durante la vigencia (colección). |
| `getFechaFin()`, `estaVigente(fecha)` | públicos | Calculan y consultan la vigencia. |
| `solicitarRevision(fecha, problema)` | público | Registra una revisión solo si la garantía está vigente; si no, lanza una excepción con un mensaje claro (RF-18). |
| `getRevisiones()` | público | Devuelve una copia de la lista, para que nadie cambie la original desde afuera. |

##### `OrdenServicio`

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
| `registrarPago(pago)` y `getSaldoPendiente()` | públicos | Reciben pagos y calculan lo que falta por pagar (RF-17). |
| `getGarantia()` y `getCambios()` | públicos | Consultar la garantía y el historial de cambios (RNF-04). Son los únicos getters de la orden: no se crearon más porque no hacían falta. |
| `cambiarEstado`, `transicionPermitida`, `validarMateriales` | privados | Reglas internas: solo estas funciones modifican el estado. |

##### `OrdenServicio.CambioEstado` (clase anidada estática)

Guarda `estadoAnterior`, `estadoNuevo`, `fecha` y `responsable` (todos privados y `final`). Tiene **constructor privado**, así que solo `OrdenServicio` puede crear cambios; no tiene setters. Es estática porque no necesita datos de la orden que la contiene.

##### `OrdenServicio.Estado` (enum anidado)

`PENDIENTE`, `ASIGNADA`, `EN_PROCESO`, `FINALIZADA` y `CANCELADA`. Va dentro de la orden porque esos estados solo tienen sentido para ella. Usar un enum evita errores de escritura en un texto.

#### 3. Relaciones entre las clases

| Relación | Tipo | Explicación |
|---|---|---|
| `OrdenServicio` ⊕ `CambioEstado` | **Clase anidada** (composición, 1 a 0..*) | Los cambios no existen sin la orden. |
| `OrdenServicio` ⊕ `Estado` | **Enum anidado** | El enum vive dentro de la orden. |
| `OrdenServicio` → `Servicio` | **Asociación** (1 a 1) | Cada orden es de un tipo de servicio. |
| `OrdenServicio` ◆— `Garantia` | **Composición** (1 a 0..1) | La garantía nace al finalizar la orden y no existe sin ella. |
| `OrdenServicio` → `Pago` | **Asociación** (1 a 0..*) | La orden guarda varios pagos. |
| `Servicio` → `Garantia` | **Dependencia** | El servicio define los días de vigencia de la garantía. |
| `OrdenServicio` → `Notificacion` | **Dependencia** «crea» | Crea el aviso al cambiar de estado (punto de unión con la parte de Juan David). |
| `Solicitud`, `Asignacion`, `DetalleTrabajo` y `Material` → `OrdenServicio` | **Dependencia / asociación** | Son clases de otros compañeros que se conectan con la orden. |

#### 4. Conceptos del curso y cómo se aplican

| Concepto | Dónde se aplica | Utilidad en el diseño |
|---|---|---|
| **Clases y objetos** | Cada orden, servicio y garantía es un objeto con su propio estado. | Cada orden lleva su historial, costos y garantía. |
| **Atributos y métodos estáticos** | `consecutivo`, `generarCodigo()`, `getTotalOrdenes()` y `Servicio.FACTOR_NORMAL`. | Lo común a toda la clase se escribe una sola vez. |
| **Clases anidadas** | `OrdenServicio.CambioEstado` y `OrdenServicio.Estado`. | Agrupan lo que solo tiene sentido dentro de la orden. |
| **Polimorfismo** | `registrarPago(Pago)` recibe un `Pago` que puede ser `Efectivo`, `Tarjeta` o `Transferencia`; la orden solo usa `estaConfirmado()` y `getMonto()`. | No hay `if` por tipo de pago ni `instanceof`. Se puede agregar otro medio de pago sin tocar `OrdenServicio`. |
| **Interfaces** | En esta parte ya no hay una propia: `Pagable` se eliminó en la Decisión 17. La interfaz del grupo es `Notificable` (parte de Juan David). | Una interfaz con una sola clase que la implementa no aportaba reutilización. |
| **Colecciones** | `ArrayList` de `CambioEstado`, `Pago` y revisiones. | Administran el historial y los pagos. |
| **Encapsulamiento** | Atributos privados, `cambiarEstado` privado, constructor privado de `CambioEstado`, getters que devuelven copias de las listas. | Nadie cambia el estado ni el historial sin pasar por las reglas. |
| **Excepciones** | `IllegalArgumentException` y `IllegalStateException` con mensajes claros. | Por ejemplo: "No se puede finalizar una orden que no ha sido asignada." |

#### 5. Cómo se cubre cada requerimiento

| Req. | Cómo se cumple | Depende de |
|---|---|---|
| **RF-05** Precio estimado | `calcularCostoEstimado`: mano de obra con la duración del servicio + materiales; cada tipo aplica su propio factor. | Tarifa del técnico y materiales (otros compañeros). |
| **RF-06** Cambiar el estado | `cambiarEstado` valida las transiciones y registra cada cambio en `CambioEstado`; una orden no asignada no se finaliza. | Técnico y Administrador, que llaman los métodos. |
| **RF-16** Costo final | `calcularCostoFinal` usa las horas reales; `compararConEstimado` da la diferencia. | `DetalleTrabajo` y `Material`. |
| **RF-18** Revisión por garantía | `solicitarRevisionGarantia` delega en `Garantia`, que solo acepta revisiones dentro de la vigencia. | Cliente. |
| **RF-07, RF-17, RF-19 y RNF-04** | Garantía creada al finalizar; pagos con `registrarPago`; aviso con `crearAvisoUltimoCambio`; historial en `CambioEstado`. | `Pago` y sus subclases, y `Notificacion`. |

#### 6. Relación con las decisiones del registro

- **Decisión 2:** separa `Solicitud` de `OrdenServicio`; la orden conserva los datos de ejecución y estado.
- **Decisión 5:** el detalle de trabajo y los materiales no se guardan dentro de la orden, para que no concentre todo.
- **Decisión 9:** `CambioEstado` es una clase anidada estática, sin rama propia.
- **Decisión 17:** se eliminó `Pagable`; `OrdenServicio` incorpora directamente `registrarPago` y `getSaldoPendiente`.
- **Decisión 18:** `Servicio` guarda la tarifa, la duración y la garantía de cada tipo.
- **Decisión 19:** `Garantia` es una clase propia que se crea al finalizar la orden.
- **Decisión 20:** `CambioEstado` es una clase anidada estática con constructor privado.
- **Decisión 21:** los costos se calculan en `OrdenServicio`, y la mano de obra se delega en `Servicio`.

#### 7. Supuestos

Supuestos para revisar con el equipo:
- La orden guarda el documento del cliente y del técnico como texto, porque `Cliente`, `Tecnico` y `Solicitud` todavía no están en `main`; se cambia por objetos cuando estén.
- El costo de materiales entra como un número; cuando `Material` esté disponible se calcula desde ahí.
- En `TRAZABILIDAD.md`, RF-05, RF-16 y RF-18 siguen en «Pendiente» hasta que existan las clases de las que dependen.

### Parte de Juan David Lara · Notificaciones

**Clases:** `Notificacion` (con el enum anidado `EstadoEnvio`), `Notificable` (interfaz), `NotificacionWhatsApp` y `NotificacionCorreo`\
**Requerimientos:** RF-19; también apoya RNF-04 y RNF-06\
**Decisiones relacionadas del registro:** Decisión 8 (Notificacion y Notificable tienen responsabilidades distintas) y Decisión 9 (clases anidadas)

*Cómo leer las tablas:* los atributos y métodos se escriben con su tipo de acceso (público o privado). Lo **estático** se marca con la palabra «estático». El enum anidado se nombra completo (`Notificacion.EstadoEnvio`) para que no se confunda con los enums de otras clases. Las relaciones entre las clases están explicadas en la sección 3.

#### 1. Idea general

Cuando cambia el estado de una orden, el cliente debe enterarse (RF-19). Esta parte separa dos cosas: **guardar el aviso** y **enviarlo**.

- `Notificacion` es el registro del aviso: para quién es, qué dice, por qué canal salió y cómo terminó el envío. No envía nada por sí misma.
- `Notificable` es el contrato que cumple cualquier canal capaz de enviar un aviso.
- `NotificacionWhatsApp` y `NotificacionCorreo` son los dos canales. Cada uno revisa el dato de contacto que le corresponde y deja el aviso como enviado o fallido. El envío es simulado: no se conecta a WhatsApp ni a un servidor de correo.

#### 2. Clases, atributos y métodos

##### `Notificacion`

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

##### `Notificacion.EstadoEnvio` (enum anidado)

`PENDIENTE`, `ENVIADA` y `FALLIDA`. Va dentro de `Notificacion` porque esos estados solo tienen sentido para un aviso. Usar un enum evita errores de escritura en un texto.

##### `Notificable` (interfaz)

| Método | Para qué sirve |
|---|---|
| `getNombreCanal(): String` | Devuelve el nombre del canal («WhatsApp» o «Correo»). |
| `enviar(Notificacion): boolean` | Envía el aviso de forma simulada, lo deja `ENVIADA` o `FALLIDA` y devuelve `true` si se pudo enviar. |

##### `NotificacionWhatsApp` y `NotificacionCorreo`

| Clase | Qué revisa | Resultado |
|---|---|---|
| `NotificacionWhatsApp` | Que el celular tenga 10 dígitos, todos números, y empiece por 3. | Si es válido: `ENVIADA` con el número. Si no: `FALLIDA` con la causa explicada. |
| `NotificacionCorreo` | Que el correo tenga una `@` con algo antes, un punto después de la `@` (con al menos un carácter entre los dos) y que no termine en punto. También arma el asunto «Actualización de tu orden OS-001». | Si es válido: `ENVIADA` con el correo y el asunto. Si no: `FALLIDA` con la causa explicada. |

Ambas tienen una constante estática `NOMBRE_CANAL` y métodos privados para sus validaciones.

#### 3. Relaciones entre las clases

| Relación | Tipo | Explicación |
|---|---|---|
| `NotificacionWhatsApp` → `Notificable` | **Realización** (implementa la interfaz) | Cumple el contrato de enviar un aviso. |
| `NotificacionCorreo` → `Notificable` | **Realización** (implementa la interfaz) | Igual que la anterior, por otro canal. |
| `Notificable` → `Notificacion` | **Dependencia** | `enviar` recibe el aviso y deja en él el resultado. |
| `Notificacion` ⊕ `EstadoEnvio` | **Enum anidado** | El enum vive dentro del aviso. |
| `OrdenServicio` → `Notificacion` | **Dependencia** «crea» | La orden crea el aviso del último cambio de estado (punto de unión con la parte de Valeria Alvarado). |
| `Cliente` → `Notificacion` | **Asociación** (pendiente) | El cliente recibe el aviso. Por ahora `Notificacion` guarda su documento, nombre, teléfono y correo como texto, porque `Cliente` todavía no está en `main`. |

#### 4. Conceptos del curso y cómo se aplican

| Concepto | Dónde se aplica | Utilidad en el diseño |
|---|---|---|
| **Interfaces** | `Notificable`, implementada por los dos canales. | Separa «ser un aviso» de «saber enviar un aviso». |
| **Polimorfismo** | Quien envía llama `canal.enviar(aviso)` sin saber si es WhatsApp o correo. | No hay `if` por canal. Para agregar SMS bastaría una clase nueva que implemente `Notificable`. |
| **Atributos y métodos estáticos** | `totalNotificaciones`, `getTotalNotificaciones()`, `crearMensajeCambioEstado()` y `NOMBRE_CANAL`. | Lo común a toda la clase se escribe una sola vez. |
| **Clases anidadas** | `Notificacion.EstadoEnvio` (enum anidado). | Agrupa lo que solo tiene sentido dentro del aviso. |
| **Encapsulamiento** | Atributos privados, sin setters, y `marcarEnviada` / `marcarFallida` que funcionan una sola vez. | El registro del aviso no se altera después de procesarlo (RNF-04). |
| **Excepciones** | `IllegalArgumentException` para datos obligatorios vacíos e `IllegalStateException` si se procesa dos veces un aviso. | Los errores tienen mensajes claros. |

#### 5. Por qué interfaz y no herencia

La Decisión 8 consideró hacer de `Notificacion` la clase padre de los canales, y la descartó. WhatsApp y correo no comparten datos entre sí: solo comparten una capacidad, saber enviar. Un canal no «es una» notificación, por eso la relación es un contrato (interfaz) y no una herencia. Además, separar el registro del aviso del mecanismo de envío evita mezclar responsabilidades.

#### 6. Cómo se cubre cada requerimiento

| Req. | Cómo se cumple | Depende de |
|---|---|---|
| **RF-19** Avisar al cliente por WhatsApp o correo | `OrdenServicio.crearAvisoUltimoCambio` crea la `Notificacion`; el canal `Notificable` la envía. | `OrdenServicio` (Valeria Alvarado) y `Cliente`. |
| **RNF-04** Trazabilidad | El aviso no tiene setters y solo se puede procesar una vez; guarda canal, estado y detalle. | — |
| **RNF-06** Mensajes claros | `crearMensajeCambioEstado` arma el aviso en lenguaje claro y, si el envío falla, el detalle explica la causa. | — |

#### 7. Supuestos

Supuestos para revisar con el equipo:
- El envío es simulado: la validación del celular y del correo es sencilla (formato), no comprueba que el número o la cuenta existan.
- Los datos del cliente se guardan como texto hasta que la clase `Cliente` esté en `main`.

---

## Organización del repositorio

### Cómo organizamos las ramas

Se trabaja con una rama por requisito y una rama por clase o interfaz de nivel superior. La rama `main` se usa para integrar el proyecto cuando las ramas estén listas.

#### Ramas por requisito

| Rama | Requisito |
|---|---|
| `main` | Integración del proyecto |
| `RF-01` | Registrar técnicos |
| `RF-02` | Registrar clientes |
| `RF-03` | Solicitar un servicio |
| `RF-04` | Asignar un técnico disponible |
| `RF-05` | Calcular el precio estimado del servicio |
| `RF-06` | Cambiar el estado de una orden |
| `RF-07` | Registrar la garantía del servicio |
| `RF-08` | Consultar el historial de órdenes de un cliente |
| `RF-09` | Consultar las órdenes activas de un técnico |
| `RF-10` | Calificar el servicio recibido |
| `RF-11` | Actualizar los datos del cliente |
| `RF-12` | Cancelar una solicitud pendiente |
| `RF-13` | Registrar la indisponibilidad de un técnico |
| `RF-14` | Reasignar una orden |
| `RF-15` | Registrar el trabajo realizado y los materiales usados |
| `RF-16` | Calcular el costo final del servicio |
| `RF-17` | Registrar pagos |
| `RF-18` | Solicitar una revisión cubierta por la garantía |
| `RF-19` | Notificar cambios de estado |
| `RF-20` | Filtrar órdenes |
| `RNF-01` | Conservar los datos entre ejecuciones |
| `RNF-02` | Restringir el acceso según el rol |
| `RNF-03` | Mantener la integridad de la información |
| `RNF-04` | Conservar la trazabilidad de cambios y reasignaciones |
| `RNF-05` | Proteger los datos personales y de pago |
| `RNF-06` | Presentar mensajes claros |

Los nombres de las ramas de requisitos usan los identificadores RF y RNF en mayúsculas.

#### Ramas por clase e interfaz

Cada clase o interfaz de nivel superior tiene una rama con el mismo nombre que su archivo Java. Las clases anidadas se trabajan en la rama de su clase contenedora y no tienen una rama independiente.

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
| `Pagable` | Interfaz con las operaciones de pago de una orden |
| `Pago` | Clase abstracta con los datos y comportamientos comunes de un pago |
| `Transferencia` | Tipo de pago por transferencia |
| `Efectivo` | Tipo de pago en efectivo |
| `Tarjeta` | Tipo de pago con tarjeta |

`DisponibilidadTecnico` contiene la clase anidada estática `FranjaHoraria`. `OrdenServicio` contiene la clase anidada estática `CambioEstado`.

### Cómo trabajamos con Git

Antes de empezar en una rama, se actualiza la referencia de `main` y se revisa el estado del repositorio. Cada integrante trabaja en la rama de su clase o requisito. Las ramas se integran en `main` cuando el trabajo correspondiente esté listo y revisado; al integrar, se corrigen los problemas antes de continuar.

### Commits

Los mensajes de commit son descriptivos y en español, en modo imperativo. Cuando aplique, incluyen el identificador del requisito; por ejemplo: `RF-01: agrega validación de documento único en Tecnico`.

### Buenas prácticas

- Usar `git status` antes y después de cada operación.
- Hacer commits pequeños y descriptivos.
- Mantener configurado `.gitignore` para archivos compilados y del IDE.
- Integrar a `main` cambios que compilen.
- Evitar `push --force` y no reescribir historial compartido.

---

*Proyecto desarrollado en Java, sin frameworks externos ni base de datos.*
*El almacenamiento permanente se implementa con archivos de texto plano, igual que en el proyecto base.*
