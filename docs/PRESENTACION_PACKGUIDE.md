# PackGuide — Presentación del proyecto

## ¿De qué trata?

PackGuide es una aplicación Android que ayuda a preparar y organizar el equipaje de viaje. Combina un inventario por maleta, estimaciones de espacio, seguimiento del empacado y evaluación de fotografías mediante inteligencia artificial.

## Problema que resuelve

Preparar una maleta puede generar desorden, olvidos y dificultades para distribuir los artículos. PackGuide permite planificar qué llevar, registrar lo que ya se empacó y consultar recomendaciones de organización.

## Funcionamiento de la aplicación

1. **Medidas:** el usuario selecciona sus maletas —mano, cabina o bodega— e introduce y confirma sus dimensiones.
2. **Catálogo:** añade artículos y cantidades de forma independiente para cada maleta y consulta las estimaciones de espacio ocupado y disponible.
3. **Guía de empacado:** consulta tutoriales de video dentro de la aplicación, marca individualmente las unidades empacadas y asigna artículos a las zonas base, centro y acceso rápido.
4. **Resumen:** revisa el progreso, la capacidad estimada y una vista esquemática de la organización. Puede añadir una fotografía y solicitar su evaluación con IA.
5. **Ajustes:** cambia entre español e inglés, selecciona el tema claro u oscuro y cierra su sesión.

## Tecnologías utilizadas

| Tecnología | Uso en el proyecto |
|---|---|
| Kotlin | Lenguaje principal de la aplicación Android. |
| Android Studio | Entorno de desarrollo, compilación y ejecución. |
| Jetpack Compose | Construcción de las pantallas y componentes visuales. |
| Material 3 | Botones, tarjetas, diálogos, navegación y temas visuales. |
| ViewModel | Gestión del estado de las pantallas y de la sesión. |
| Corrutinas de Kotlin | Ejecución de tareas en segundo plano sin bloquear la interfaz. |
| Room, sobre SQLite | Almacenamiento local de cuentas. |
| DataStore | Guardado de preferencias de idioma y tema. |
| WebView | Reproducción integrada de tutoriales de YouTube. |
| Node.js y JavaScript | Servidor que comunica la aplicación con el proveedor de IA. |
| API de Gemini | Análisis de fotografías y generación de evaluaciones textuales. |
| Gradle | Gestión de dependencias y compilación del proyecto. |
| Git y GitHub | Control de versiones y alojamiento del código. |

## Función de la inteligencia artificial

La IA está integrada en la pantalla **Resumen**. El usuario selecciona una fotografía de su maleta y solicita su evaluación. El servidor la envía a **Gemini 3.5 Flash-Lite**, que devuelve:

- Una puntuación de organización de 1 a 10 cuando la fotografía permite evaluarla.
- Un comentario sobre la distribución visible de los artículos.
- Observaciones sobre el aprovechamiento aparente del espacio y el acceso al contenido.
- Una sugerencia de organización, doblado o distribución de prendas.

Si la imagen no permite evaluar el equipaje, el modelo puede omitir la puntuación y explicar cómo tomar una fotografía útil.

La IA analiza fotografías; no genera una fotografía nueva ni mide las dimensiones de la maleta. El esquema visual del resumen lo construye la propia aplicación.

Además, PackGuide incluye **veinte consejos de organización** y selecciona **dos aleatoriamente para cada foto nueva**. Estos consejos complementan la evaluación y proceden de una lista incluida en el proyecto; no son observaciones del modelo sobre la fotografía.

### Comunicación con el servicio

```text
Usuario selecciona una foto y solicita evaluarla
                    ↓
Aplicación Android
                    ↓
Servidor Node.js
                    ↓
API de Gemini: analiza la imagen
                    ↓
Puntuación y comentario en el resumen
```

La clave de API permanece en el servidor y no se incorpora al APK.

## Herramientas utilizadas para las interfaces

Las interfaces se desarrollaron directamente en **Kotlin con Jetpack Compose**, utilizando componentes de **Material 3** y componentes personalizados.

Compose permite definir las pantallas mediante funciones y actualizar su contenido cuando cambia el estado de la aplicación, por ejemplo al modificar una cantidad o marcar un artículo como empacado.

El diseño incluye:

- Selectores para los distintos tipos de maleta.
- Campos y botones de confirmación de medidas.
- Tarjetas de artículos y controles de cantidades.
- Barras con estimaciones de capacidad por maleta.
- Casillas para registrar cada unidad empacada.
- Tutoriales de video integrados.
- Un diálogo de resumen con fotografía, puntuación, consejos y distribución por zonas.
- Tema claro y oscuro.
- Interfaz en español e inglés.

## Almacenamiento y seguridad

Las cuentas se almacenan localmente mediante Room. Las contraseñas se procesan con **PBKDF2 y salt** y no se guardan en texto plano.

Las preferencias de idioma y tema se guardan con DataStore. El inventario, las fotografías y el progreso de la sesión permanecen en memoria; no existe sincronización de estos datos con una cuenta en la nube.

## Alcance actual y limitaciones

- El inventario, el seguimiento y la vista esquemática funcionan sin conexión.
- Los videos y la evaluación mediante IA requieren Internet.
- La evaluación depende actualmente de un servidor local en el PC. Para distribuir esa función de forma independiente habría que alojarlo en Internet y proteger su acceso.
- Las cifras de espacio son estimaciones basadas en las medidas introducidas y los valores del catálogo; no garantizan que todos los objetos quepan físicamente.
- La evaluación de la IA se limita a lo visible en la fotografía y puede cometer errores.

## Texto breve para exponer

> PackGuide es una aplicación Android para planificar y organizar el equipaje. Permite configurar distintas maletas, seleccionar artículos, estimar el espacio ocupado y marcar lo que ya está empacado. Está desarrollada en Kotlin con Jetpack Compose y Material 3. También integra Gemini para analizar una fotografía de la maleta y ofrecer una puntuación y sugerencias de organización. Su objetivo es hacer que preparar el equipaje sea un proceso más claro, ordenado y fácil de seguir.

## Repositorio

[Código fuente de PackGuide en GitHub](https://github.com/danilolarap/PackGuide)
