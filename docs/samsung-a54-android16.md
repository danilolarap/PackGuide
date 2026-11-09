# Samsung Galaxy A54 5G / Android 16

Preparación local del 8 de noviembre de 2026.

## Cambios implementados

- La sesión, el inventario, la pestaña y el historial de navegación viven en
  PackGuideSessionViewModel. Se conservan al girar el dispositivo o cambiar de
  tamaño en multiventana. Cerrar sesión reinicia el inventario y la navegación,
  y sigue cancelando y eliminando las fotos y vistas.
- Se eliminó la limpieza de vistas al desmontar MainScreen, que también se
  ejecutaba al recrear la actividad. Los ViewModels conservan sus imágenes.
- Atrás usa BackHandler de AndroidX y un historial limitado a las cuatro
  pestañas. Sin historial, Android gestiona la salida de la actividad.
  No se desactiva el sistema de regreso de Android 16.
- Scaffold usa safeDrawing y consume sus márgenes. La barra inferior protege
  también los laterales y el recorte de cámara en horizontal.
- El tema deja de asignar colores a las barras mediante APIs obsoletas;
  enableEdgeToEdge gestiona las barras y el tema actualiza el contraste de iconos.
- Un Mutex compartido evita decodificar y rotar varias fotos simultáneamente
  entre Escáner y Resumen. Se solicita ARGB_8888 y se comprueba cancelación.
  Se mantienen el muestreo máximo a 1600 px, el límite de entrada de 100 MP
  (permite fotos de 50 MP) y el límite de archivo de 50 MiB.
- Se mantienen R8 y reducción de recursos en Release. No se fijan resolución,
  frecuencia de pantalla, fabricante ni arquitectura; tampoco se fuerza
  mantener la pantalla encendida o ignorar el ahorro de batería de One UI.

compileSdk y targetSdk siguen en 37; minSdk 24 permite instalar en Android 16
(API 36). No hace falta reducir targetSdk al del teléfono.

## Verificación ejecutada

- assembleDebug y assembleRelease: correctos.
- testDebugUnitTest: 22 pruebas correctas.
- lintDebug: 0 errores, 12 avisos y 3 sugerencias; no se desactivan verificaciones.
- DeviceCompatibilityTest en el emulador disponible API 37: conserva sesión,
  cantidades, pestaña y fotos tras recrear MainActivity; Atrás vuelve al escáner
  sin cerrar la actividad.
- APK Release firmado con el certificado debug local: firma válida,
  instalación y arranque correctos en el emulador.
- zipalign -c -P 16 4: correcto. Se verificó también que los segmentos LOAD de
  las bibliotecas ELF arm64-v8a y x86_64 tienen alineación de al menos 16 KB.

El primer análisis de Lint falló internamente al cambiar archivos durante su
ejecución; la ejecución final completa terminó correctamente.

## APK para instalación local

app/build/outputs/apk/release/packguide-release-local.apk
1.653.754 bytes, optimizado con R8 y firmado con la clave local de pruebas.
No es una firma de publicación de Google Play. El APK Debug mide 20.118.073 bytes;
la diferencia de tamaño no demuestra una mejora concreta de velocidad o batería.

## Validación física pendiente

No había un Galaxy A54 conectado ni un emulador API 36 instalado. La prueba en
API 37 es una regresión de ciclo de vida, no certifica el comportamiento en
Android 16 / One UI. Faltan mediciones de memoria, fluidez, temperatura y batería
en el teléfono real. Verificar allí navegación por gestos y tres botones,
rotación y multiventana, teclado Samsung, tamaño de letra ampliado, selección
de fotos de 50 MP y generación de vistas con varias maletas.

La sesión y el inventario sobreviven a cambios de configuración dentro del
proceso. No se añadió persistencia del inventario: si Android termina el
proceso se vuelve a solicitar acceso y se reinicia ese estado, como antes.
La captura sigue usando la miniatura de la cámara del sistema; las fotos
elegidas de la galería se procesan con muestreo y orientación EXIF.

## Referencias

- https://developer.android.com/about/versions/16/behavior-changes-16
- https://developer.android.com/develop/ui/compose/state
- https://developer.android.com/develop/ui/compose/system/insets-ui
- https://developer.android.com/guide/practices/page-sizes