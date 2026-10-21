# Decisiones de POO · Parte de Valeria Alvarado

Clases: OrdenServicio, OrdenServicio.CambioEstado (anidada), Servicio y Garantia.
Requisitos: RF-05, RF-06, RF-16 y RF-18 (también apoya RF-07, RF-17 y RNF-04).

---

## Decisión A · Servicio concentra la tarifa, la duración y la garantía de cada tipo

**Decisión**

Crear la clase Servicio con el tipo, un factor de tarifa propio, la duración estimada y los días de garantía. OrdenServicio le pide a Servicio el cálculo de la mano de obra (`calcularManoObra`).

**Alternativas consideradas**

1. Guardar el tipo como un texto en OrdenServicio y calcular el precio con `if` según el tipo ("plomería", "electricidad"...).
2. Crear una subclase por tipo (Plomeria, Electricidad, Pintura) que herede de Servicio.

**Por qué**

Los `if` por tipo obligan a modificar OrdenServicio cada vez que aparece un tipo nuevo. Las subclases serían herencia solo para cambiar un número, y todos los tipos tienen los mismos datos y el mismo comportamiento; solo cambian los valores. Con un objeto Servicio por tipo, cada uno guarda su propia tarifa y garantía sin repetir código.

**Consecuencia**

Agregar un tipo nuevo es crear un objeto Servicio con otros valores, sin tocar OrdenServicio. Se cumple el criterio de RF-05 ("cada tipo de servicio aplica su propia tarifa") y el de RF-07 ("cada tipo define su propio tiempo de garantía"). Sus atributos son privados y los setters validan los datos.

---

## Decisión B · Garantia es una clase propia que se crea al finalizar la orden

**Decisión**

Garantia guarda la fecha de inicio, los días de vigencia y la cobertura, y decide por sí misma si está vigente (`estaVigente`) y si acepta una revisión (`solicitarRevision`). OrdenServicio crea la garantía cuando la orden pasa a FINALIZADA, con los días que define su Servicio.

**Alternativas consideradas**

1. Guardar la fecha de fin de la garantía como un atributo suelto dentro de OrdenServicio.
2. Crear la garantía junto con la orden, desde que está pendiente.

**Por qué**

La garantía solo tiene sentido cuando el trabajo terminó. Si fuera un atributo suelto, OrdenServicio tendría que conocer las reglas de vigencia y de revisión. Al ser una clase, esas reglas quedan en un solo lugar.

**Consecuencia**

La relación es de composición: sin la orden no existe la garantía. Una revisión fuera de la vigencia lanza una excepción con un mensaje claro. Esta decisión apoya RF-07 y RF-18.

---

## Decisión C · CambioEstado es una clase anidada estática con constructor privado

**Decisión**

CambioEstado se define dentro de OrdenServicio como clase anidada estática. Guarda estado anterior, estado nuevo, fecha y responsable. Su constructor es privado, no tiene setters y la orden solo entrega una lista de solo lectura.

**Alternativas consideradas**

1. Una clase CambioEstado independiente, en su propio archivo.
2. Guardar solo el estado actual y no el historial.

**Por qué**

Un cambio de estado no tiene sentido sin una orden, así que va dentro de ella y no necesita rama propia. Es estática porque no necesita usar datos de la orden que la contiene. Con constructor privado, solo OrdenServicio puede crear cambios, y el historial no se puede falsificar ni modificar desde afuera.

**Consecuencia**

Toda transición queda registrada con su responsable (RNF-04). El único método que cambia el estado es `cambiarEstado`, que valida las transiciones permitidas: PENDIENTE a ASIGNADA o CANCELADA, ASIGNADA a EN_PROCESO o CANCELADA, y EN_PROCESO a FINALIZADA. Una orden no asignada no puede finalizarse (RF-06).

---

## Decisión D · Los costos se calculan en OrdenServicio, delegando la mano de obra a Servicio

**Decisión**

OrdenServicio guarda el costo estimado y el final, y calcula cada uno con `calcularCostoEstimado` y `calcularCostoFinal`. La comparación está en `compararConEstimado`. La parte de la mano de obra la calcula Servicio.

**Alternativa considerada**

Hacer una clase aparte Cotizacion o CalculadoraCostos.

**Por qué**

El costo depende de datos que ya tiene la orden (el servicio, el técnico y el estado). Una clase aparte solo repetiría esos datos y sería una clase con un solo uso.

**Consecuencia**

La fórmula es: horas x tarifa base del técnico x factor del tipo + materiales. El estimado usa la duración estimada de Servicio y el final usa las horas reales. Esta decisión apoya RF-05 y RF-16.
