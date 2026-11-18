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

- **Enlace al repositorio:** [proyecto_POO](https://github.com/Samuel231007/proyecto_POO)
- **Diagrama UML:** [Ver diagrama de clases](https://lucid.app/lucidchart/6b8de022-7d2f-4c7f-ab6e-32428fa6780c/edit?viewport_loc=-4837%2C-1424%2C11332%2C5940%2C0_0&invitationId=inv_890ba847-9cd2-4821-b89e-546f47aec054)
- Se cuenta con el docente invitado agregado como colaborador.

---

## 3. Documentación del proyecto

La información del proyecto se organiza en el README, el registro de decisiones, la trazabilidad y la bitácora de uso de IA. La bitácora se mantiene como un documento independiente del registro de decisiones.

## Cómo organizamos las ramas

Se trabaja con una rama por requisito y una rama por clase o interfaz de nivel superior. La rama `main` se usa para integrar el proyecto cuando las ramas estén listas.

### Ramas por requisito

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

### Ramas por clase e interfaz

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

## Cómo trabajamos

Antes de empezar en una rama, se actualiza la referencia de `main` y se revisa el estado del repositorio. Cada integrante trabaja en la rama de su clase o requisito. Las ramas se integran en `main` cuando el trabajo correspondiente esté listo y revisado; al integrar, se corrigen los problemas antes de continuar.

## Commits

Los mensajes de commit son descriptivos y en español, en modo imperativo. Cuando aplique, incluyen el identificador del requisito; por ejemplo: `RF-01: agrega validación de documento único en Tecnico`.

## Buenas prácticas

- Usar `git status` antes y después de cada operación.
- Hacer commits pequeños y descriptivos.
- Mantener configurado `.gitignore` para archivos compilados y del IDE.
- Integrar a `main` cambios que compilen.
- Evitar `push --force` y no reescribir historial compartido.

---

## 4. Código del proyecto

El código sigue la misma organización del proyecto base del profesor ([SistemaGestionBiblioteca](https://github.com/wilmertri/SistemaGestionBiblioteca)): una carpeta `src/` con los paquetes `enums`, `files`, `interfaces`, `modelos`, `persistencia`, `repositorios`, `servicios` y `ui`, y un único `Main.java`. Las decisiones de esta organización están en el registro de decisiones (Decisiones 22 a 29).

### Pruebas

Las pruebas del equipo están en la carpeta `pruebas/` (marcada como carpeta de pruebas en IntelliJ). Para ejecutarlas: clic derecho sobre el archivo → **Run**.

### Cómo ejecutar

**Requisitos:** Java 11 o superior (el proyecto está configurado con JDK 21) e IntelliJ IDEA.

1. Clonar el repositorio y abrir la carpeta `proyecto_POO` en IntelliJ IDEA.
2. Verificar que el **directorio de trabajo** sea la raíz del proyecto (así encuentra `src/files/`).
3. Ejecutar **`Main.java`** → abre la aplicación con ventanas (interfaz gráfica).
4. Opcional: para la versión con menús de texto, en IntelliJ ir a *Run → Edit Configurations → Program arguments* y escribir `consola`.

Las dos versiones usan los mismos servicios, repositorios y archivos `.txt`: lo que se hace en una se ve en la otra.

### Interfaz gráfica (paquete `ui/`)

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
| OS-005 | PENDIENTE | El administrador puede asignarla a Diana (opción 7) |

> La opción **5 del menú principal** ejecuta la demo de todos los requerimientos **en memoria**: no modifica los archivos.

---

### Arquitectura

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

### Modo dual: ArrayList vs. archivo

Igual que `EstudianteServicio` en el proyecto base, `GestorPlataforma` funciona en dos modos según el constructor:

```java
new GestorPlataforma();        // MODO ARCHIVO: carga src/files/*.txt y guarda cada cambio (RNF-01)
new GestorPlataforma(false);   // MODO MEMORIA: solo ArrayList (lo usa DemoRequerimientos)
```

Los datos se cargan **una sola vez** al iniciar y todas las órdenes apuntan a los mismos objetos `Cliente` y `Tecnico` de las listas. Esto es necesario porque el código del equipo compara con `==` (por ejemplo `orden.getCliente() != this` en `Cliente.verificarPropia`). Si cada consulta volviera a leer el archivo, se crearían objetos nuevos y esas comparaciones fallarían.

---

### Estructura del proyecto

```
proyecto_POO/
├── README.md, REGISTRO_DECISIONES.md, TRAZABILIDAD.md, BITACORA_IA.md, Explicacion_*.md
├── pruebas/                             Pruebas del equipo (PruebaNotificaciones, PruebaServicioGarantia)
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
    │   └── Validar                      Validaciones comunes de los modelos (RNF-06)
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
    │   ├── DemoRequerimientos.java      El programa de prueba del equipo, en memoria
    │   └── Consola.java                 Lectura de datos compartida por los menús
    │
    └── files/                           16 archivos .txt con datos de ejemplo
```

---

### Formato de los archivos de datos

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

### Conceptos POO en el proyecto

| Concepto | Dónde |
|---|---|
| Herencia | `Cliente`, `Tecnico`, `Administrador` ← `Usuario`; `Efectivo`, `Tarjeta`, `Transferencia` ← `Pago`; todos los repositorios ← `BaseRepository`; `PanelCliente`, `PanelTecnico`, `PanelAdministrador` ← `PanelRol`; `Boton` ← `JButton` |
| Clases abstractas | `Usuario`, `Pago`, `BaseRepository`, `PanelRol` (con métodos abstractos `registrarSecciones`, `descripcionUsuario`, `colorRol`) |
| Interfaces | `Notificable`, `IRepository<T>`, `IRepositorioDependiente<P>`, `IServicio<T>`, `IImprimible`; en la GUI se implementan `Icon` y `Scrollable` de Swing |
| Polimorfismo | `Notificable` (WhatsApp o correo); `Pago` (3 tipos); `OrdenServicioRepository` recorre una lista de `IRepositorioDependiente` sin saber cuál es cada uno |
| Genericidad | `IRepository<T>`, `IRepositorioDependiente<P>`, `IServicio<T>` |
| Composición | `OrdenServicio` TIENE `Asignacion`, `Garantia`, `DetalleTrabajo`...; `Tecnico` TIENE `DisponibilidadTecnico`; repositorios que TIENEN otros repositorios |
| Clases anidadas estáticas | `FranjaHoraria`, `CambioEstado`, `Movimiento`, `Material` |
| Enum | `EstadoOrden`, `EstadoPago`, `EstadoEnvio` |
| Sobrecarga de constructores | Constructor normal + constructor de reconstrucción (lo usa el repositorio al leer el archivo) en `Usuario`, `Cliente`, `Tecnico`, `Solicitud`, `OrdenServicio`, `Calificacion`, `Notificacion`; `GestorPlataforma()` vs `GestorPlataforma(boolean)` |
| Sobrecarga de métodos | `OrdenServicioRepository.getDataList()` y `getDataList(clientes, tecnicos, servicios)` |
| Atributos y métodos estáticos | `OrdenServicio.generarCodigo()`, consecutivos de `Solicitud` y `Notificacion`, `Validar` |
| Encapsulamiento | Atributos `private`; métodos sin `public` que solo usa el mismo paquete (`OrdenServicio.asignarTecnico`, `Solicitud.actualizarEstado`) |
