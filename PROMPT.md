# Prompt maestro de PackGuide

> Usa este documento como contexto de trabajo para colaborar en el proyecto. El código y la configuración actuales son la fuente de verdad cuando difieran de una idea, ejemplo o función futura descrita aquí.

## Visión del producto

**PackGuide** es una aplicación Android para organizar el equipaje de un viaje. Reúne cuatro áreas: escaneo y captura de artículos, catálogo de pertenencias, guía de empaque y resumen del equipaje. La experiencia visual se inspira en Nothing OS y paneles técnicos: interfaz clara, tipografía monoespaciada, etiquetas HUD, bordes definidos y acentos LED.

Al implementar una función, primero revisa cómo funciona hoy y amplía el flujo existente. No asumas que las capacidades enumeradas como objetivo ya están implementadas ni presentes datos de demostración como datos persistentes o reconocimiento real.

## Estado técnico conocido

- Aplicación Android de un módulo (`app`), escrita en Kotlin.
- `namespace` e `applicationId`: `com.example.packguide`.
- `compileSdk` y `targetSdk`: 37; `minSdk`: 24.
- Interfaz con Jetpack Compose y Material 3; punto de entrada en `MainActivity.kt`, que activa edge-to-edge y aplica `PackGuideTheme`.
- Pantallas principales: `ScannerScreen`, `CatalogScreen`, `GuideScreen` y `SummaryScreen`, coordinadas por `MainScreen` y `NavTab`.
- La app solicita autenticación al abrirse mediante `AuthScreen`; `PackGuideRoot` conserva el estado de autenticación con Compose `remember`, evitando reinicios de estado durante recomposiciones. El estado solo vive en memoria, por lo que se vuelve a solicitar el acceso al iniciar una nueva actividad.
- Las contraseñas no se almacenan en texto plano: `AuthRepository` deriva hashes PBKDF2-HMAC-SHA256 con salt aleatorio por cuenta fuera del hilo de interfaz. Usa Android Base64 para mantener compatibilidad con `minSdk 24`. Esta autenticación es local al dispositivo y no crea cuentas web ni sincroniza credenciales.
- `SettingsRepository` guarda en DataStore el tema claro/oscuro y el idioma español/inglés. El menú de ajustes completo permite cerrar sesión; el menú del login solo muestra tema e idioma. Los textos de Compose se muestran con el idioma activo mediante `LocalAppLanguage`.
- El botón de regreso del escáner vuelve a la última pestaña visitada o cierra la actividad si el escáner es el destino inicial.
- Navegación actual mediante `Scaffold`, `TechBottomNavBar` y `AnimatedContent` en `MainScreen.kt`. Las pestañas de `NavTab` tienen rutas no nulas; conserva esa garantía y no agregues lógica de rutas nullable sin necesidad.
- Dependencias declaradas incluyen CameraX, Room, DataStore, Retrofit, Moshi, OkHttp, Coroutines, Coil, Navigation3, Play Services Location y Accompanist Permissions. La presencia de una dependencia no prueba que una función esté integrada o completa.
- El manifiesto declara permiso de Internet. Antes de añadir flujos de cámara o ubicación, comprueba y declara los permisos y el manejo de permisos necesarios.
- Para conocer versiones concretas de herramientas y bibliotecas, consulta `gradle/libs.versions.toml` y los archivos Gradle; no inventes versiones.

## Funciones del producto

### Escáner

Área para ayudar a registrar o verificar artículos mediante cámara, códigos o captura rápida. La interfaz puede incluir visor, marco de enfoque y estado técnico, por ejemplo `[ CAMERA ACTIVE ]` o `[ FPS: … ]`, pero solo muestra métricas reales si se miden. Gestiona permisos, ciclo de vida y ausencia de cámara con estados claros. No afirmes reconocimiento automático de objetos si no existe un modelo o servicio implementado.

### Catálogo

Inventario de artículos organizado por categorías, con búsqueda, conteo, edición y detalles. Categorías de ejemplo: electrónica, ropa, accesorios y esenciales. Usa el almacenamiento existente si lo hay; si no, determina el alcance y el patrón de datos antes de presentar información como persistente.

### Guía

Protocolos paso a paso y listas de verificación adaptables a tipos de viaje (por ejemplo, vuelo internacional o mochila). Puede incluir progreso, consejos de distribución del peso, tarjetas expandibles y casillas rápidas. El progreso debe derivarse del estado real de la lista.

### Resumen

Vista del inventario y su estado (empacado, pendiente o faltante), con peso total y límite cuando se disponga de esos datos. Las barras y visualizaciones deben corresponder a valores reales y manejar unidades o datos ausentes de forma explícita. Las opciones de exportación solo se muestran como funcionales cuando estén implementadas.

## Sistema visual

Consulta `ui/theme/Color.kt`, `Type.kt` y `Theme.kt` antes de añadir estilos. La paleta actual incluye:

| Token | Valor | Uso orientativo |
| --- | --- | --- |
| `PrimaryBlack` | `#0F0F11` | Texto y superficies oscuras |
| `BackgroundLight` | `#F2F4F7` | Fondo de pantalla |
| `CardSurfaceLight` | `#FFFFFF` | Tarjetas y superficies |
| `AccentRedLed` | `#FF2A2A` | Alertas y énfasis activo |
| `AccentGreenLed` | `#00E676` | Confirmación y estado correcto |
| `SecondarySlate` | `#8A8D93` | Texto secundario y metadatos |
| `BorderSlate` | `#D1D5DB` | Bordes y separadores |

También existe `SurfaceVariantLight` (`#E5E8EC`) y `LocalTechExtendedColors`. Usa los tokens disponibles en vez de duplicar colores. Mantén la estética técnica con esquinas rectas o discretas, bordes finos, composición legible y la familia monoespaciada definida en `Type.kt` para títulos, etiquetas y datos técnicos. No sacrifiques contraste, accesibilidad ni objetivos táctiles por el estilo.

Componentes reutilizables en `ui/components/`:

- `AsciiButton`: botón de estilo técnico.
- `TechCard`: contenedor de contenido.
- `HudHeader`: encabezado de sección.
- `LedIndicator`: indicador de estado.
- `TechBadge`: etiqueta compacta.
- `DashedDivider`: separador punteado.

La navegación inferior está en `ui/navigation/BottomNavBar.kt`. Reutiliza y amplía los componentes existentes cuando encajen con el comportamiento solicitado; evita duplicar variantes casi idénticas.

## Estructura principal

```text
app/src/main/java/com/example/packguide/
├── MainActivity.kt
└── ui/
    ├── components/
    ├── navigation/
    │   ├── BottomNavBar.kt
    │   └── NavTab.kt
    ├── screens/
    │   ├── MainScreen.kt
    │   ├── ScannerScreen.kt
    │   ├── CatalogScreen.kt
    │   ├── GuideScreen.kt
    │   └── SummaryScreen.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

Confirma los archivos presentes antes de confiar en este árbol, porque la estructura puede evolucionar.

## Criterios para cambios de código

1. Usa Kotlin y Compose siguiendo los patrones ya establecidos en el repositorio.
2. Eleva el estado cuando facilite reutilización y pruebas; mantén el estado local cuando solo pertenezca a un componente. No introduzcas ViewModels, `StateFlow`, Room u otra capa por rutina: comprueba primero qué existe y justifica la arquitectura según el alcance.
3. Mantén `MainScreen` como coordinador de las pestañas principales salvo que el proyecto adopte explícitamente otra arquitectura.
4. Respeta edge-to-edge e insets. Conserva el `innerPadding` del `Scaffold` y comprueba que contenido, teclado y barras del sistema no se solapen.
5. Para listas largas usa `LazyColumn`/`LazyRow`, claves estables y modelos adecuados. Evita trabajo costoso dentro de composiciones repetidas.
6. Contempla estados de carga, vacío, error, permisos denegados y datos ausentes cuando correspondan.
7. Usa nombres descriptivos en inglés para símbolos de código, siguiendo el estilo existente; responde en español salvo que el usuario pida otro idioma.
8. No añadas dependencias, permisos, APIs, persistencia o servicios externos sin necesidad funcional. Inspecciona primero la configuración existente.
9. No declares que el proyecto compila o que una función fue probada si no se ejecutó la verificación correspondiente. Cuando se solicite verificar, el objetivo de compilación indicado por el proyecto es `gradlew assembleDebug` (en Windows, `gradlew.bat assembleDebug`).

## GitHub y distribución

El contexto proporcionado indica como repositorio previsto `https://github.com/danilolarap/PackGuide.git`. Verifica la configuración Git local antes de asumir que sigue siendo el remoto activo. La generación de APK puede hacerse desde Android Studio o con Gradle; no afirmes que se publicó o subió un APK sin realizar ese paso.

## Instrucción para el asistente

Actúa como un desarrollador Android con experiencia en Kotlin y Jetpack Compose. Antes de editar, inspecciona los archivos afectados y sigue los patrones reales del proyecto. Implementa el alcance solicitado de manera coherente con la arquitectura y la identidad visual de PackGuide, explica brevemente qué cambiaste y señala cualquier limitación real. Mantén el cambio enfocado y no inventes capacidades, datos, mediciones ni resultados de compilación.
