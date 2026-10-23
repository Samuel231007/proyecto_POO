Decisión 13. Utilizar herencia para Cliente, Tecnico y Administrador
Decisión: Se estableció una relación de herencia entre Usuario y sus tres clases derivadas: Cliente, Tecnico y Administrador.
¿Por qué? Aunque los tres tipos de usuario comparten información básica, cada uno cumple funciones diferentes dentro de la plataforma.
Consecuencia: Cada clase conserva los atributos y comportamientos generales de Usuario y añade los propios de su rol. Por ejemplo, Cliente gestiona solicitudes, Tecnico administra su disponibilidad y Administrador gestiona usuarios y órdenes de servicio.
Decisión 14. Asociar Cliente con Solicitud
Decisión: Se decidió asociar Cliente y Solicitud, de manera que un cliente puede crear varias solicitudes y cada solicitud pertenece a un cliente(multiplicidad).
¿Por qué? El sistema necesita identificar quién solicita un servicio y mantener relacionadas las solicitudes con la persona que las creó, además esto se complementa para el cumplimiento del RF-03 que exige que el cliente solicite un servicio y registre la información necesaria .
Consecuencia: La plataforma puede consultar las solicitudes de cada cliente, identificar al responsable de una solicitud y facilitar las operaciones como consultar. 
Decisión 15. Incorporar la actualización de datos para el RF-11
Decisión: Se incluyó en Cliente la operación actualizarDatos() para modificar la información personal del cliente.
¿Por qué? Los datos de contacto o ubicación del cliente pueden cambiar y el sistema debe permitir que la información registrada se mantenga actualizada cada vez que sea necesario.
Consecuencia: El cliente puede actualizar sus datos sin necesidad de crear una cuenta nueva. La operación debe verificar que la información ingresada sea válida y guardar los cambios correctamente.
Decisión 16. Controlar la cancelación de solicitudes para el RF-12
Decisión: Se asignó a Solicitud la responsabilidad de comprobar si una petición puede cancelarse por medio de validarCancelacion() y cancelar(), esto de la mano de crear EstadoSolicitud con los estados posibles de la solicitud para consultas y validaciones de los métodos nombrados.
¿Por qué? La cancelación depende del estado en el que se encuentre la solicitud. No se permite que cualquier solicitud se cancele sin verificar las condiciones del servicio.
Consecuencia: El sistema puede impedir cancelaciones que no sean permitidas por las reglas del negocio. Por ejemplo, si las condiciones definidas permite cancelar solo las solicitudes pendientes, la operación debe rechazar la cancelación cuando la solicitud ya esté asignada.
Decisión 17. Aplicar encapsulamiento y validación
Decisión: Se decidió mantener los atributos de las clases como privados y acceder a ellos mediante operaciones controladas cuando sea necesario en los métodos.
¿Por qué? Los datos de los usuarios y de las solicitudes no deben modificarse sin verificar las reglas correspondientes y con un objetivo específico.
Consecuencia: Se reduce el riesgo de cambios inconsistentes, se facilita la validación de la información y se tiene un mayor control sobre las operaciones de registro, actualización y cancelación.

