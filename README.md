# Estructura del proyecto

## Cómo organizamos las ramas

Trabajamos con una rama por cada requerimiento (RF o RNF), además de `main`, así cada integrante avanza en lo suyo sin pisar el trabajo de los demás.

| Rama | Requerimiento |
|---|---|
| `main` | Versión integrada y funcional del proyecto |
| `rf-01` | Registrar técnicos |
| `rf-02` | Registrar clientes |
| `rf-03` | Solicitar un servicio |
| `rf-04` | Asignar un técnico disponible |
| `rf-05` | Calcular el precio estimado del servicio |
| `rf-06` | Cambiar el estado de una orden |
| `rf-07` | Registrar la garantía del servicio |
| `rf-08` | Consultar historial de órdenes de un cliente |
| `rf-09` | Consultar órdenes activas de un técnico |
| `rf-10` | Calificar el servicio recibido |
| `rnf-01` | Persistencia de datos entre ejecuciones |

En total son 11 ramas (RF-01 a RF-10 y RNF-01), más `main`. Cada rama se llama igual que el requerimiento que le corresponde, en minúsculas.

## Ramas por clase

Además de las ramas de requerimiento, tenemos una rama por cada clase del diseño, para trabajar su implementación de forma aislada. Estas se nombran igual que la clase en Java (con mayúscula inicial):

| Rama | Clase |
|---|---|
| `Usuario` | Clase padre de Tecnico y Cliente |
| `Tecnico` | Técnicos registrados |
| `Cliente` | Clientes registrados |
| `Solicitud` | Solicitudes de servicio |
| `OrdenServicio` | Órdenes de servicio |
| `Servicio` | Tipos de servicio |
| `Garantia` | Garantías de los servicios |
| `Calificacion` | Calificaciones de los clientes |

## Cómo trabajamos

Antes de crear una rama o empezar a trabajar en ella, hacemos `git pull` sobre `main` para partir siempre de la última versión.

Cada rama se va actualizando de forma independiente por el integrante encargado de ese requerimiento. Antes de subir un avance, lo avisamos al equipo (por ejemplo, en el chat) y confirmamos que todos tengan la versión más reciente, para evitar conflictos; una vez que queda aprobado, se hace `push` directo a la rama.

Cuando una rama termina su requerimiento (RF o RNF), se integra a `main` con `git merge`. Así vamos avanzando rama por rama, integrando cada una a medida que se completa, hasta dejar el proyecto funcionando de principio a fin.

`main` siempre debe quedar en un estado funcional: si al integrar una rama algo se rompe, se corrige antes de seguir con la siguiente.

## Commits

Los mensajes de commit son descriptivos y en español, en modo imperativo (ej. `Agrega validación de documento único en Tecnico`), referenciando el ID del requerimiento cuando aplica (ej. `RF-01: agrega validación de documento único`).

## Buenas prácticas que seguimos

- `git status` antes y después de cada operación, para saber en qué queda cada archivo.
- Commits pequeños y frecuentes, no uno solo gigante al final.
- `.gitignore` desde el primer commit (compilados de Java, configuración del IDE, etc.).
- Nunca se sube nada a `main` que no compile.
- Evitamos `push --force` y no reescribimos historial que ya se compartió con el equipo.
