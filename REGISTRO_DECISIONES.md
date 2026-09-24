# Registro de decisiones de POO

Proyecto integrador: plataforma de técnicos a domicilio.

---

## Decisión 1 · Usuario como clase padre de Cliente, Tecnico y Administrador

**Decisión**
Crear una clase padre `Usuario` con la información general de las personas del sistema, como nombre y documento. Las clases `Tecnico`, `Cliente` y `Administrador` heredan de `Usuario` y agregan sus propios datos y acciones.

**Alternativas consideradas**

1. Tres clases independientes (`Tecnico`, `Cliente` y `Administrador`) que repiten los atributos nombre y documento.
2. Una sola clase `Usuario` con un atributo que indique si es cliente, técnico o administrador.

**Por qué**
`Tecnico`, `Cliente` y `Administrador` cumplen la relación "es un" `Usuario`, porque todos se identifican con nombre y documento. Pero cada uno tiene datos y acciones distintas: el técnico tiene especialidad, zona de cobertura, horario disponible y tarifa base (RF-01); el cliente tiene teléfono, correo y ubicación (RF-02), y el administrador registra técnicos y gestiona el estado de las órdenes. Con clases independientes se repetiría código, y con una sola clase habría que usar condicionales para saber qué puede hacer cada usuario.

**Consecuencia**
Los atributos comunes se escriben una sola vez en `Usuario` y cada subclase solo agrega lo que la diferencia. Los atributos serán privados y se accederá a ellos con getters y setters (encapsulamiento). En el diagrama de clases se representará la herencia `Tecnico ← Usuario`, `Cliente ← Usuario` y `Administrador ← Usuario`.

---

## Decisión 2 · Solicitud y OrdenServicio como clases separadas

**Decisión**
Separar la solicitud del cliente y la orden de servicio en dos clases. `Solicitud` almacena el tipo de problema, la ubicación, la descripción y el horario disponible. `OrdenServicio` relaciona la solicitud con el técnico asignado y guarda la información de la orden y su estado.

**Alternativas consideradas**

1. Una sola clase `OrdenServicio` que guarde tanto los datos de la solicitud como los del técnico asignado y el estado.
2. Guardar la solicitud como atributos dentro de la clase `Cliente`.

**Por qué**
La solicitud existe antes de que haya un técnico: el cliente la crea con sus datos (RF-03) y después el sistema busca un técnico de la especialidad, zona y horario requeridos (RF-04). La orden de servicio aparece cuando se hace esa asignación. Tenerlas separadas deja a cada clase con una sola responsabilidad y evita que `OrdenServicio` o `Cliente` acumulen demasiada información.

**Consecuencia**
`OrdenServicio` se asocia con `Solicitud` y con `Tecnico`, y utiliza la información de la solicitud para asignar el técnico (asociación en el diagrama UML). A partir de `OrdenServicio` se calcula el costo del servicio (RF-05) y se relacionan la garantía y la calificación.
