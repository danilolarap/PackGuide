# Vista organizada de empaque sin conexión

En Escáner, toma o elige una foto de la maleta abierta. Selecciona sus artículos
 en Catálogo y pulsa **GENERAR IMAGEN ORGANIZADA** en Guía. Sin foto, el panel
ofrece **AÑADIR FOTO EN ESCÁNER**. Toca la vista o pulsa **Ampliar vista organizada**
para abrir el visor. Resumen permite añadir hasta cuatro fotos y usar la primera
como referencia visual para comparar con el esquema del inventario.

El resultado es una **composición local** de la foto original y tarjetas por
zonas, con cantidades, medidas y métricas del inventario. No es IA, edición
fotorealista, detección de objetos, evaluación automática ni simulación física.
No se inventa una puntuación de organización. Las medidas no confirmadas siguen
siendo orientativas.

## Arquitectura

La inspección encontró una llamada directa a Gemini, sin backend ni proxy.
`local.properties` solo contenía la configuración del SDK; no había configuración
de servidor ni variables de proveedor en el entorno consultado. La autenticación
con Room es local y no autentica un servicio remoto.

Se retiraron el cliente remoto, el formulario de claves y su estado de sesión.
No se añadieron secretos, dependencias, servicios de pago ni permisos. El permiso
normal de Internet preexistente permanece, pero este flujo no lo usa ni sube fotos.

- `PackingImageService` recibe el plan, una foto y el idioma. Un futuro backend
  autenticado puede implementarlo manteniendo sus credenciales en el servidor.
- `OfflinePackingImageService` crea un Bitmap con Canvas fuera del hilo principal.
  Conserva los artículos seleccionados, cantidades y zonas, sin duplicar unidades
  empacadas. La foto de referencia permanece sin modificar.
- `PackingPreviewViewModel` conserva resultados en memoria por maleta al navegar,
  impide trabajos duplicados, permite cancelar y reintentar, e invalida resultados
  al cambiar sus entradas. Retirar una maleta o cerrar sesión cancela su trabajo.
- Se mantiene la persistencia de sesión existente. No se guardan fotos, URI ni
  resultados en una base de datos ni se promete recuperación tras cerrar el proceso.

## Imágenes y errores

El selector usa `PickVisualMedia`, sin permiso general de almacenamiento. Los URI
se conservan en memoria y se leen con ContentResolver sin asumir rutas de archivo.
La cámara sigue usando la miniatura del sistema. La foto se reduce a un lado máximo
de 1600 px y se corrige con EXIF fuera del hilo principal. Se rechazan cabeceras
superiores a 100 millones de píxeles y se acota cada stream a 50 MiB. La composición
mide 1200 px de ancho y como máximo 4096 px de alto.

Cancelar el selector conserva la foto anterior. Hay mensajes ES/EN para archivos
incompatibles, tamaño excesivo, falta de acceso, cancelación y fallos de composición.
Los fallos de un futuro servicio se traducen a un mensaje sencillo. La interfaz
no muestra excepciones, trazas ni detalles de credenciales.

## Comprobaciones ejecutadas

- `assembleDebug testDebugUnitTest`: compilación correcta y **22 pruebas unitarias**
  sin fallos, incluyendo cálculos, inventario, snapshots y disposición de los 36 tipos.
- `PackingPreviewFlowTest`: **5 pruebas instrumentadas** correctas en el emulador
  Medium_Phone_API_37.0. Se usó la copia aislada `.volumecheck`, conservando la app
  instalada con otra firma. Se verificaron los XML de resultados.
- Se verificaron el acceso a Escáner desde Guía, cancelación y retorno del selector
  con un URI real de MediaStore, decodificación, generación sin credenciales, visor,
  conservación al navegar, etiquetas en inglés y ausencia de configuración de claves.
- Se probaron archivos inválidos y cabeceras sobredimensionadas, conservación de
  píxeles de la foto, bloqueo de duplicados, cancelación y recuperación después de
  un fallo de servicio simulado.
- La búsqueda estática en el código principal confirmó que no quedan el cliente
  Gemini, su endpoint directo, el formulario ni el estado de la clave.

El selector se sustituyó por ActivityResultRegistry en la prueba: se comprobó su
contrato de retorno y la lectura real del URI, no la interfaz manual del selector
ni una cámara física. No se generaron imágenes de IA en la nube: no existe una
solicitud remota en este flujo.
