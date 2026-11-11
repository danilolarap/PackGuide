# PackGuide — Prompt de continuidad para Codex CLI

Continúa como colaborador en el proyecto Android **PackGuide**. La copia de trabajo de referencia está en `C:\Users\Danilo\Documents\PackGuide` y el shell es PowerShell. Si la abriste en otra ruta, identifica la raíz con `settings.gradle.kts` y el módulo `app`. Lee primero las instrucciones del repositorio y revisa `git status --short` y los diffs. Hay trabajo previo sin commit y archivos nuevos sin seguimiento. La rama remota puede estar atrasada. Conserva los cambios del usuario y toma el árbol local como fuente de verdad. No hagas commits, pushes ni despliegues solo por recibir este contexto.

## Estado que debes conservar

- App Android Kotlin, Jetpack Compose, Material 3 y acceso local en Room. El flujo es **Acceso → Medidas → Catálogo → Guía → Resumen**. Temas claro y oscuro, español e inglés con traducción de frases completas y textos dinámicos consistentes.
- **Medidas** sustituyó a Escáner. Selección de Mano, Cabina y Bodega por filas, dimensiones interiores manuales por maleta y confirmación destacada. La barra inferior completa permanece oculta hasta que estén confirmadas todas las maletas seleccionadas. Editar medidas o añadir una maleta pendiente vuelve a activar el bloqueo. La protección también vive en `PackGuideSessionViewModel`, de modo que otro botón o Atrás no salten la condición.
- **Catálogo** tiene búsqueda, categorías y cantidades por maleta. `CatalogCapacityOverview.kt` fija un conjunto de barras de capacidad para todas las maletas seleccionadas. **Guía** tiene un tutorial embebido por maleta, reproducción visible, pausa, reanudación y cierre, y después casillas por unidad. IDs de video Mano `Sb3_ZvMiCEM`, Cabina `mG0t34yy9sE`, Bodega `1YF7l-JL06M`. Los botones de zona cambian la agrupación en Resumen y en el esquema. Medidas y Guía carecen de captura de fotos.
- Se integró un **icono Android adaptativo** con una maleta abierta en grabado blanco y negro. La ilustración está en `app/src/main/res/drawable-xxxhdpi/ic_launcher_foreground.png`, el fondo blanco en `drawable/ic_launcher_background.xml` y las variantes en `mipmap-*` y `mipmap-anydpi-v26`. El manifiesto apunta a `ic_launcher` e `ic_launcher_round`. No hace falta regenerarlo.
- **Decisión más reciente y vinculante**. El usuario abandonó la generación de imágenes por IA. En **Resumen → Ver vista previa** quiere una composición local con la foto original de su maleta, el inventario seleccionado, la capacidad y un esquema por zonas. `OrganizedPackingDialog.kt` tiene marco oscuro, panel claro desplazable, foto a la izquierda, datos a la derecha, tarjetas de dos columnas y botón Cerrar fijo. Funciona incluso sin foto y sin servidor. Conserva la técnica de doblado local y la nota de distribución ilustrativa.
- La IA se usa solo para **evaluar la foto elegida**. Dentro del modal, **Evaluar foto con IA** devuelve nota de 1 a 10 sobre organización visible y una sugerencia práctica. Si la foto no permite juzgar, `score` es nulo y se muestra la explicación. No inventes una nota cuando el servicio falle. La nota y la sugerencia se muestran en el mismo modal. El inventario de ese modal muestra las unidades **seleccionadas** y Guía conserva las **empacadas**. Por ejemplo, 12 seleccionadas y 11 empacadas deben verse como `12 × CAMISETAS` en el esquema y `11/12` en Guía.

## Qué ocurrió con la IA y qué falta

Se pasó del esquema anterior a generación remota de fotos y luego se retiró ese botón por petición del usuario. En la versión remota anterior, el cliente enviaba las unidades empacadas exactas y el servidor usaba la API de imágenes de OpenAI. El usuario recibió repetidamente el error de conexión porque el APK Debug apuntaba a `127.0.0.1:8787` y no había un servicio activo en ese destino. Por eso se restauró el esquema local. El endpoint `/v1/packing-image` quedó como compatibilidad de clientes anteriores y la interfaz actual no lo llama. No lo vuelvas a poner en pantalla salvo nuevo pedido.

El servidor actual está en `server/`, usa Node.js 22+ sin dependencias externas y ofrece `POST /v1/packing-review`. Android envía una foto JPEG elegida y el idioma. El servidor llama a la API Responses con un modelo de visión configurable por `OPENAI_REVIEW_MODEL` (`gpt-5-mini` en el código actual), valida la nota y devuelve la sugerencia. El cliente guarda la evaluación en memoria por maleta, foto e idioma. Se mantiene la posibilidad de usar el esquema local aunque falle la evaluación.

Al terminar la sesión anterior no había credenciales válidas configuradas ni backend público desplegado. La evaluación real de extremo a extremo permanece pendiente. Si el siguiente encargo es activarla, comprueba primero si existe una dirección de servidor y configuración en el entorno sin imprimir secretos. Para una prueba local consulta `docs/packing-ai.md` y `server/start-android.ps1`, que prepara ADB y arranca el servidor. Para uso sin PC se requiere un servidor HTTPS accesible y configurar `PACKGUIDE_AI_BASE_URL` al compilar. El acceso local de Room no autentica un servicio externo. Mantén `OPENAI_API_KEY` en el servidor y no pidas que se pegue en el chat o se incluya en Android. El gateway descrito en la documentación debe reenviar `/v1/packing-review` al proceso privado con el host que este admite.

## Archivos de entrada para continuar

| Área | Rutas bajo el proyecto |
| --- | --- |
| Sesión y bloqueo | `app/src/main/java/com/example/packguide/PackGuideSessionViewModel.kt` y `ui/screens/MainScreen.kt` bajo el mismo paquete |
| Medidas, catálogo y guía | `app/src/main/java/com/example/packguide/ui/screens/{MeasurementsScreen,LuggageSelector,CatalogScreen,CatalogCapacityOverview,GuideScreen,PackingTutorialPanel,PackingState}.kt` |
| Resumen y modal | `app/src/main/java/com/example/packguide/ui/screens/{SummaryScreen,PackingPreviewPanels,OrganizedPackingDialog}.kt` |
| Evaluación | `app/src/main/java/com/example/packguide/ai/{PackingPreviewViewModel,PackingImageService,RemotePackingImageService}.kt`, `server/server.mjs`, `server/packing.mjs` |
| Textos | `app/src/main/java/com/example/packguide/ui/localization/{AppText,PackingText}.kt` |
| Documentación | `README.md`, `docs/packing-ai.md`, `docs/photo-scanning.md`, `docs/volume-calculations.md` |

## Verificación y forma de trabajar

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
node --test server/test/packing.test.mjs
```

En la última versión del modal pasaron la compilación Debug, las pruebas unitarias Android, las siete pruebas del servidor y las cinco pruebas de `PackingPreviewFlowTest` en un teléfono Android con un paquete aislado de validación. Las pruebas de evaluación usaron respuestas simuladas. La verificación cubrió que el modal abre sin servicio y que muestra una nota y sugerencia simuladas. La compilación y `lintDebug` también pasaron cuando se integró el icono. Repite las comprobaciones relevantes cuando cambie el código y reporta los resultados de esa ejecución.

Si pruebas en el teléfono, preserva la app instalada y sus datos. La validación previa usó `com.example.packguide.validation` y retiró sus paquetes de prueba al acabar. El script de Gradle de validación estaba en `build/device-validation.init.gradle`, que es un directorio de artefactos y puede faltar en otra copia. Evita reemplazar una instalación firmada con otra clave.

Si el usuario solo te entrega este archivo, úsalo para orientarte y espera su siguiente encargo concreto. Si pide una demo, sigue `docs/PACKGUIDE_PROMPT_MAESTRO_DEMO_ACTUALIZADA.md`. Para cambios de código, conserva el flujo y el comportamiento descritos salvo nueva instrucción, localiza primero los puntos de entrada reales y entrega cambios verificables. Separa en el reporte final lo validado en el dispositivo, lo simulado y lo pendiente de un servicio real.
