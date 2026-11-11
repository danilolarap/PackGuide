# Foto de la maleta

**Medidas** permite elegir mano, cabina y bodega, conservando al menos una.
Cada maleta tiene dimensiones interiores y confirmación independientes.
Introduce las medidas manualmente; esta pantalla no toma ni carga fotos.

Catálogo mantiene las cantidades por destino y las barras de espacio de todas
las maletas seleccionadas. El volumen y el progreso se calculan por maleta.
Quitar una maleta elimina sus asignaciones y pide confirmación si tiene artículos.

Guía ofrece un tutorial de video por maleta y casillas para marcar cada unidad.
No solicita fotos ni genera imágenes.

Las fotos se añaden únicamente en **Resumen**, mediante **AÑADIR FOTOS** o
**TOMAR FOTO**, hasta cuatro por maleta. **EVALUAR FOTO CON IA** envía la foto
elegida para obtener una calificación del orden visible y sugerencias. La vista
previa muestra el inventario seleccionado y sus zonas junto a la foto original,
incluso cuando el servicio de evaluación está desconectado.
Consulta [la referencia visual](packing-ai.md).

El selector usa PickVisualMedia sin permiso general de almacenamiento.
La cámara devuelve una miniatura. Las imágenes se reducen y se corrigen con
EXIF fuera del hilo principal y permanecen en memoria durante la sesión.

El espacio se estima usando las dimensiones confirmadas y los litros por
unidad del catálogo. Es una referencia, no una garantía de ajuste.
Consulta [la regla de cálculo](volume-calculations.md).
