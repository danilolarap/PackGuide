# PackGuide

Aplicación Android para organizar el equipaje de un viaje. Permite distribuir
artículos entre equipaje de mano, cabina y bodega, estimar el espacio ocupado
y seguir el progreso del empaque.

Desarrollada en Kotlin con Jetpack Compose y Material 3, con temas claro y oscuro
e interfaz en español e inglés.

## Funciones

- **Acceso local:** registro e inicio de sesión con cuentas guardadas en Room.
  Las contraseñas se derivan con PBKDF2 y salt; no se almacenan en texto plano.
- **Medidas:** selección de maletas, ajuste manual de sus dimensiones interiores
  y confirmación de medidas, sin cámara ni carga de fotos.
  La confirmación destaca las medidas pendientes y cambia al confirmarlas.
  El menú inferior se habilita al confirmar las medidas de todas las maletas
  seleccionadas; cambiar las medidas vuelve a bloquear el acceso.
- **Catálogo:** búsqueda, categorías y cantidades independientes para cada maleta,
  con 36 artículos de referencia. Las barras de espacio de todas las maletas
  seleccionadas permanecen visibles mientras se desplaza el inventario.
- **Guía:** secciones por maleta con tutoriales de YouTube dentro de la app y
  casillas independientes para cada unidad, debajo del video. Los tutoriales
  requieren internet, empiezan al tocarlos y se detienen al salir de la guía.
  Incluyen controles de pausa, continuación y cierre dentro de la pantalla.
  Organización por zonas y seguimiento de las unidades empacadas.
- **Resumen:** totales de capacidad, volumen y progreso. La vista previa local
  reúne la foto original, las estimaciones de capacidad y las tarjetas por zona.
  La evaluación opcional de la foto usa el [servidor de IA](docs/packing-ai.md).
- **Ajustes:** tema, idioma y cierre de sesión.

El inventario, el seguimiento y el esquema funcionan sin conexión. La evaluación
de la foto usa Gemini 3.5 Flash-Lite mediante el servidor de evaluación, con
nivel gratuito sujeto a las cuotas de Google. La clave se guarda en el
servidor. El esquema es ilustrativo. El cálculo
de volumen sigue usando las medidas manuales y los valores del catálogo.

## Requisitos

| Componente | Configuración del proyecto |
| --- | --- |
| Dispositivo | Android 7.0 o posterior (API 24+) |
| SDK de compilación y destino | API 37 |
| Android Gradle Plugin | 9.4.1 |
| Gradle Wrapper | 9.6.0 |
| JVM del daemon de Gradle | JDK 25 |
| Kotlin / plugin Compose | 2.2.10 |

Usa Android Studio con soporte para las herramientas indicadas y el SDK API 37
instalado. El wrapper incluye la configuración para resolver la JVM del daemon;
la primera sincronización puede descargar herramientas y dependencias.
Las versiones se definen en [el catálogo de dependencias](gradle/libs.versions.toml).

## Ejecutar el proyecto

1. Clona el repositorio:

   ```sh
   git clone https://github.com/danilolarap/PackGuide.git
   cd PackGuide
   ```

2. Abre la carpeta en Android Studio y sincroniza Gradle.
3. Configura la ruta del SDK en Android Studio o en un archivo local
   `local.properties` con la propiedad `sdk.dir`. Este archivo no se versiona.
4. Conecta un teléfono con depuración USB o inicia un emulador y ejecuta
   el módulo `app`.
5. Crea una cuenta local desde la pantalla de acceso.

Para generar un APK de depuración desde PowerShell:

```powershell
.\gradlew.bat :app:assembleDebug
```

En macOS o Linux, usa `./gradlew` en lugar de `.\gradlew.bat`.

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.
Puedes instalarlo con Android Studio o con:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Compilación y pruebas

```powershell
# Pruebas unitarias y análisis estático
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug

# Pruebas de interfaz: requieren un dispositivo o emulador conectado
.\gradlew.bat :app:connectedDebugAndroidTest

# APK optimizado con R8 y reducción de recursos
.\gradlew.bat :app:assembleRelease
```

La compilación Release genera `app/build/outputs/apk/release/app-release-unsigned.apk`.
Es necesario firmarlo antes de instalarlo o distribuirlo; la configuración del
repositorio no incluye una clave de publicación.

## Almacenamiento y compatibilidad

Las cuentas se guardan en Room y las preferencias de tema e idioma en DataStore.
La sesión, el inventario y las fotos permanecen en memoria y se conservan durante
rotación y cambios de tamaño de la actividad. Al cerrar sesión o terminar el
proceso se reinicia ese estado; no hay sincronización de cuentas ni inventario.

La aplicación tiene ajustes para márgenes del sistema, navegación Atrás y
procesamiento de fotos pensando en Android 16 y el Galaxy A54 5G.
La validación documentada se realizó en un emulador API 37; todavía está
pendiente probarla y medir su rendimiento en el teléfono físico.

## Estructura

```text
app/src/main/java/com/example/packguide/
├── MainActivity.kt                 # Entrada y acceso
├── PackGuideSessionViewModel.kt    # Sesión, inventario y navegación
├── ai/                            # Composición local de vistas
├── data/auth/                     # Cuentas y base de datos Room
├── scanning/                      # Lectura de fotos y reglas de volumen
├── settings/                      # Preferencias con DataStore
└── ui/                            # Pantallas, componentes, tema y traducciones

app/src/test/                      # Pruebas unitarias
app/src/androidTest/               # Pruebas instrumentadas
docs/                              # Documentación de funciones y validación
```

## Documentación

- [Fotos y flujo de varias maletas](docs/photo-scanning.md)
- [Vista organizada sin conexión](docs/packing-ai.md)
- [Cálculos de volumen y referencias del catálogo](docs/volume-calculations.md)
- [Preparación para Galaxy A54 y Android 16](docs/samsung-a54-android16.md)
- [Optimización y comprobaciones anteriores](docs/optimization.md)
- [Contexto de desarrollo](PROMPT.md)
