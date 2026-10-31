# Foto de la maleta

Escáner permite combinar mano, cabina y bodega (al menos una). Cada maleta
seleccionada tiene foto, medidas y confirmación independientes. Catálogo muestra
un contador de destino por cada maleta elegida; las cantidades, el volumen y el
progreso de empacado se mantienen separados. Guía y Resumen permiten cambiar
entre las maletas activas y el resumen muestra además los totales del viaje.
Quitar una maleta elimina sus asignaciones y su foto; si tiene artículos, pide
confirmación antes de hacerlo. Las otras maletas conservan su contenido.

El panel solicita solo una foto de la maleta, mediante **Tomar foto** o
**Elegir foto**, y muestra una vista previa. No solicita fotos de prendas,
no ofrece categorías ni cantidades en este panel y no ejecuta reconocimiento ML.

La foto sirve como referencia visual. El cálculo de espacio sigue usando las
medidas de la maleta y los volúmenes estándar de las prendas del catálogo
existente. No se infieren dimensiones reales de una foto. Las medidas se
seleccionan o ajustan y confirman en los controles existentes de Escáner.
La guía compara el volumen seleccionado con la capacidad y una reserva del 20%.
Es una sugerencia, no una simulación física ni una garantía de ajuste.

El selector permite elegir una sola imagen sin permiso general de almacenamiento.
La cámara del sistema devuelve una miniatura en memoria; PackGuide no crea un
archivo de captura. El comportamiento de almacenamiento de la cámara externa
pertenece a esa aplicación. La imagen elegida se reduce y se corrige con EXIF
fuera del hilo principal. La vista previa vive en memoria, sin subida ni
persistencia de la imagen o su URI. Cambiar de pestaña conserva la vista previa.
No se necesitan API keys, Firebase ni modelos de ML Kit.

En Android Studio: sincronizar Gradle y ejecutar en Android >= 24.

Verificación manual: tomar/elegir foto, reemplazar la foto, cancelar ambas
opciones, comprobar cámara/selector ausentes, acceso denegado, imagen inválida,
modo avión, español/inglés y pantallas pequeñas. Revisar que la foto no cambia
las cantidades ni las medidas del inventario.

Comprobaciones: `gradlew.bat :app:assembleDebug :app:testDebugUnitTest`.
