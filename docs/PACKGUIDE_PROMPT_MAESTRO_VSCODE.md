# PackGuide — Prompt maestro para reproducir la interfaz actual

> Instantánea de referencia: 8 de noviembre de 2026. Documento obtenido del código local de PackGuide, incluyendo cambios que pudieran no estar confirmados en Git. Lee primero la especificación y consulta después las fuentes completas incluidas en el anexo. El objetivo es reproducir la aplicación existente, con sus estados y medidas, sin reinterpretar su diseño.

## 1. Encargo e instrucciones de ejecución

Actúa como un desarrollador de interfaces especializado en reproducción visual exacta, Kotlin, Jetpack Compose y adaptación de interfaces entre plataformas. Implementa PackGuide en el proyecto de destino hasta dejarla ejecutable y revisable. Trabaja sobre archivos reales; una explicación o maqueta estática no completa este encargo.

La referencia es la aplicación Android cuya implementación completa se adjunta al final de este documento. Conserva su composición, jerarquía, dimensiones, textos, colores, iconos, radios, bordes, espaciados, navegación, estados, reglas de cálculo y comportamiento. No agregues decisiones estéticas propias. No reemplaces el diseño por una plantilla.

VS Code es el editor de destino, pero no define la plataforma de ejecución. Antes de implementar, inspecciona la carpeta abierta y las instrucciones del usuario:

1. Si el destino es el mismo proyecto Android, conserva Kotlin, Compose, Material 3, los recursos y las versiones originales. Cambiar de editor no exige reescribir la aplicación. Es la vía con mayor fidelidad de renderizado.
2. Si el usuario ha elegido web o el destino contiene una aplicación web, adapta el código de referencia al framework ya utilizado. Aplica las equivalencias web de este documento.
3. Si se ha elegido web y la carpeta está vacía, usa React con TypeScript y estilos explícitos, salvo preferencia expresa diferente. Mantén componentes equivalentes a los originales.
4. Si la carpeta está vacía y no se ha indicado plataforma, pregunta una sola vez si se desea Android o web. Mientras tanto puedes preparar tokens, inventario y especificaciones; no elijas silenciosamente una tecnología incompatible con lo solicitado.

No sustituyas un proyecto existente ni borres archivos ajenos para crear una demostración. Inspecciona sus instrucciones y dependencias antes de editar. Implementa por etapas comprobables y continúa hasta completar todas las pantallas y sus interacciones.

### Prioridad de las referencias

Para esta reproducción utiliza, en este orden:

1. Capturas actuales de la app y código de la misma versión, si ambos están disponibles y corresponden al mismo estado, idioma, tema y dispositivo.
2. Las fuentes completas de esta instantánea, incluidas en el anexo.
3. Las especificaciones numéricas y de comportamiento de este documento.
4. Aproximaciones medidas y documentadas únicamente cuando falte una referencia de plataforma.

Si una captura y el código discrepan, comprueba primero versión, estado y configuración; no mezcles diseños de versiones distintas. Si se pide reproducir una captura concreta, esa captura define el estado visual objetivo.

El antiguo `PROMPT.md` del proyecto contiene información histórica. Sus colores y algunas dependencias descritas no reflejan esta instantánea. No uses sus colores como referencia visual. Tampoco conviertas en componentes visibles todas las frases presentes en el diccionario de traducciones: parte de ese diccionario corresponde a interfaces anteriores que ya no se renderizan.

## 2. Qué es PackGuide y cuál es el flujo real

PackGuide organiza equipaje mediante cuatro pantallas: Escáner, Catálogo, Guía y Resumen. Antes de acceder aparece una pantalla de autenticación local con modo de registro. Ajustes existe tanto en autenticación como dentro de la aplicación.

Flujo inicial real:

`Inicio → Iniciar sesión / Crear cuenta → Escáner → Catálogo → Guía → Resumen`

Las cuatro pestañas están disponibles tras autenticarse; los botones de continuación ofrecen un recorrido adicional. Ajustes abre un diálogo y no constituye una quinta pantalla de contenido.

Estado inicial verificable:

- Tema claro e idioma español, salvo ajustes guardados previamente.
- No hay sesión autenticada al crear una nueva actividad. No se recuerda automáticamente una sesión con almacenamiento persistente.
- Tras entrar se abre Escáner.
- Solo CABINA está seleccionada y es la maleta activa.
- CABINA mide 55 × 35 × 25 cm y sus dimensiones están sin confirmar.
- No hay fotografías seleccionadas ni vistas generadas.
- El catálogo tiene 36 artículos. Los seis primeros tienen cantidades iniciales 3, 2, 1, 1, 1 y 1; los demás tienen 0.
- Hay 9 unidades seleccionadas, 0 empacadas y 6 tipos de artículos con cantidad positiva.
- Todas las zonas de los artículos comienzan en BASE.
- Volumen inicial usado: 19.2 L. Capacidad real de cálculo: 48.125 L; presentación: 48.1 L.
- Espacio estimado presentado: 28.9 L. Ocupación truncada: 39%. Progreso de empaque: 0%.
- En el indicador de diez segmentos del catálogo aparecen 3 segmentos llenos, porque se trunca `ocupación × 10`.

No reemplaces estas cantidades por un inventario de ejemplo diferente. No hagas que la aplicación comience en Catálogo ni omitas el acceso para simplificar el desarrollo final.

## 3. Identidad visual actual

Interfaz técnica, cálida y contenida: fondo marfil, superficies casi blancas, texto carbón, metadatos gris oliva, rojo apagado y verde salvia. Combina etiquetas monoespaciadas en mayúsculas con descripciones sans serif. Las tarjetas y botones tienen esquinas redondeadas. La marca visual secundaria es una matriz de puntos de 6 × 6.

No se dibuja un logotipo grande ni una portada promocional. No hay fotografías decorativas, degradados decorativos, ilustraciones de viajes, barras laterales de escritorio, tarjetas de estadísticas inventadas ni encabezados comerciales. Los recursos visuales de contenido son iconos Material Outlined, la matriz dibujada por código, las fotografías elegidas por el usuario y las composiciones locales del inventario.

### 3.1. Colores exactos del tema claro

| Token del código | Hexadecimal | Aplicación |
| --- | --- | --- |
| PrimaryBlack | #191919 | Texto principal y botón principal |
| BackgroundLight | #F3F2EE | Fondo general |
| CardSurfaceLight | #FFFEFA | Superficies y texto invertido de botones |
| AccentRedLed | #D63830 | LED, acentos y errores |
| AccentGreenLed | #54816A | Confirmaciones y estado positivo |
| SecondarySlate | #686862 | Texto secundario |
| BorderSlate | #DEDED7 | Bordes |
| SurfaceVariantLight | #EAE9E3 | Fondos internos, selección y pistas |

El blanco puro #FFFFFF no sustituye globalmente a #FFFEFA. El negro puro #000000 tampoco sustituye a #191919. El rojo de esta interfaz no es #FF2A2A y el verde no es #00E676.

### 3.2. Colores exactos del tema oscuro

| Propiedad semántica | Hexadecimal |
| --- | --- |
| background | #101110 |
| surface | #1B1C1A |
| surfaceVariant | #282925 |
| onBackground / onSurface / primary | #FFFEFA |
| secondary | #B2B1AA |
| outline | #8C8D86 |
| outlineVariant | #393A35 |
| onPrimary / primaryContainer | #191919 |
| tertiary / error | #D63830 |

`PackGuideColors` obtiene sus valores de `MaterialTheme.colorScheme`. Respeta esa relación. Los LED rojo y verde mantienen sus colores constantes al cambiar de tema. La composición bitmap de equipaje tiene su propia paleta fija, descrita más adelante; no la recolorees con los tokens de las tarjetas.

No actives colores dinámicos del dispositivo: el tema actual usa esquemas explícitos. En Android se adapta el color y apariencia de las barras del sistema al tema, según el comportamiento de la plataforma y edge-to-edge.

### 3.3. Tipografía completa

La fuente técnica del código es `FontFamily.Monospace`; la fuente de lectura es `FontFamily.SansSerif`. No hay archivos de fuentes personalizados en los recursos de la aplicación. Su resolución concreta depende del Android de referencia: no afirmes que una fuente web genérica coincide exactamente sin comprobarla.

| Estilo | Familia | Peso | Tamaño sp | Alto de línea sp | Tracking sp |
| --- | --- | --- | --- | --- | --- |
| displayLarge | mono | 700 | 32 | 38 | -0.5 |
| displayMedium | mono | 700 | 26 | 32 | 0 |
| displaySmall | mono | 700 | 24 | 30 | 0 |
| headlineLarge | mono | 700 | 22 | 28 | 0 |
| headlineMedium | mono | 700 | 18 | 24 | 0.25 |
| headlineSmall | mono | 700 | 16 | 22 | 0.25 |
| titleLarge | mono | 600 | 16 | 22 | 0.5 |
| titleMedium | mono | 500 | 14 | 20 | 0.5 |
| titleSmall | mono | 500 | 12 | 18 | 0.5 |
| bodyLarge | sans | 400 | 15 | 22 | 0.25 |
| bodyMedium | sans | 400 | 13 | 18 | 0.25 |
| bodySmall | sans | 400 | 11 | 16 | 0.25 |
| labelLarge | mono | 700 | 13 | 16 | 1 |
| labelMedium | mono | 500 | 11 | 14 | 1 |
| labelSmall | mono | 400 | 10 | 12 | 0.8 |

Hay overrides locales que se deben mantener: navegación a 9 sp con tracking 0; chips y badges a 11 sp; placeholder y entrada de búsqueda con estilo bodyMedium pero familia mono. Algunos `Text`, `TextButton`, `IconButton`, `Checkbox`, `FilterChip` y campos usan valores heredados de Material 3, por lo que debes consultar el código y el componente real en vez de aplicar labelSmall a todo por uniformidad.

## 4. Geometría general, unidades e insets

Todas las medidas de layout siguientes están en dp; las de texto, en sp. En Android conserva esas unidades. Un dp equivale a `density` píxeles físicos; no copies un screenshot de 1080 píxeles como si midiera 1080 dp. Las métricas sp deben observar además la escala de fuente del dispositivo.

Las pantallas principales ocupan el espacio del `Scaffold`, respetan `innerPadding` y se sitúan encima de la navegación inferior. Los encabezados quedan fuera de las listas desplazables. En Catálogo, búsqueda, filtros y tarjeta inferior permanecen fuera del scroll del inventario. Escáner, Guía y Resumen desplazan sus listas de contenido.

| Elemento | Medida |
| --- | --- |
| Margen horizontal de contenido principal | 20 |
| Padding vertical del HudHeader | 18 |
| Padding horizontal del HudHeader normal | 20 |
| Separación entre título y matriz | 12 |
| TechCard: radio / borde / padding | 18 / 1 / 16 |
| TechCard: espacio tras fila de etiqueta | 14 |
| AsciiButton: radio / altura mínima | 24 / 48 |
| AsciiButton: padding horizontal / vertical | 20 / 14 |
| Campo de búsqueda: radio / borde | 16 / 1 |
| Campo de búsqueda: padding horizontal / vertical | 12 / 10 |
| Tarjeta inferior de capacidad: radio / padding | 20 / 14 |
| Diálogo de ajustes: radio / padding | 22 / 20 |
| Barra inferior: radios superiores | 24 |

No impongas altura fija a tarjetas con textos variables. El texto largo debe envolver según la anchura y tipografía reales. La altura mínima de un botón no significa que todos los botones tengan exactamente 48 dp: el contenido y su padding pueden hacerlos más altos.

## 5. Componentes compartidos: contrato exacto

### 5.1. HudHeader

Contenedor de ancho completo con padding horizontal 20 y vertical 18. El título utiliza headlineMedium y ocupa el espacio restante. A la derecha aparece `DotMatrixMark` de 32 × 32.

Si `sysCode` o `statusText` no están vacíos se agrega una fila superior: código a la izquierda, estado LED a la derecha cuando corresponda, y 10 dp de separación antes del título. Valores predeterminados: `SYS // PACKING_OS` y `[ ONLINE ]`.

Detalle esencial: Escáner llama al encabezado con `statusText = ""` pero conserva el `sysCode` predeterminado, por lo que muestra `SYS // PACKING_OS`. Catálogo, Guía, Resumen y autenticación pasan vacíos ambos textos y no muestran esa fila. No agregues un estado ONLINE a todas las pantallas.

### 5.2. DotMatrixMark

Canvas de 32 × 32, con 36 círculos distribuidos en una matriz 6 × 6. Paso = ancho / 6. Centro de cada círculo = `((columna + 0.5) × paso, (fila + 0.5) × paso)`. Radio = paso × 0.16. Color normal = texto principal con alfa 0.18. Solo los tres puntos inferiores de la última columna son rojos: columna 5, filas 3, 4 y 5. No escribas letras, símbolos de terminal ni un icono de cuadrícula para reemplazarla.

### 5.3. TechCard

Ancho completo, superficie del tema, radio 18, recorte de contenido al radio, borde 1, padding 16 y sin elevación decorativa. Borde predeterminado = border. Si se solicita exactamente primaryText, el componente reduce ese color a alfa 0.22; no dibujes un contorno carbón sólido en esas tarjetas.

Cuando existe headerTag o subTag, dibuja una fila con indicador rojo vertical de 3 × 10, radio 2, separación horizontal 8, etiqueta labelSmall secundaria a la izquierda y subetiqueta opcional a la derecha. Espacio posterior = 14. Si no existe ninguna etiqueta, omite toda esa fila y su espacio.

### 5.4. AsciiButton

Contenedor redondeado radio 24, altura mínima 48, padding horizontal 20 y vertical 14. Fila centrada, separación interna 8; icono opcional de 18; texto labelLarge. El texto es exactamente el recibido: algunos botones incluyen corchetes, otros no. No agregues corchetes automáticamente.

| Estado | Fondo | Texto |
| --- | --- | --- |
| Principal habilitado | primaryText | surface |
| Secundario habilitado | surface | primaryText |
| Acento rojo habilitado | AccentRedLed | blanco puro |
| Deshabilitado | surfaceVariant | secondaryText |

Borde 1: mismo color del fondo para principal habilitado; border en los demás casos según la lógica original. Al presionar, escala ambos ejes a 0.985 con tween de 120 ms; al soltar vuelve a 1. No hay ripple en este componente (`indication = null`). No uses sombra o desplazamiento vertical para el estado pulsado.

### 5.5. LED y badges

LED: el tamaño lógico predeterminado es 8; su Canvas ocupa tamaño × 1.8, el núcleo tiene radio tamaño / 2 y el halo radio × 1.4 del núcleo, alfa 0.12. No confundas el diámetro del núcleo con el espacio reservado. Si hay etiqueta, separación 4, labelSmall secundaria. Solo si `isPulsing` es true anima alfa 0.4→1, tween 1400 ms FastOutSlowIn, repetición inversa; el valor predeterminado es false.

TechBadge: radio 8, borde 1 transparente por defecto, padding horizontal 8 y vertical 4, texto labelSmall con tamaño 11. Fondo predeterminado primaryText, texto surface. LED opcional de tamaño lógico 6 y separación 6. Los badges de categoría sobrescriben fondo por surfaceVariant y texto por secondaryText.

DashedDivider existe como componente: grosor 1, trazo 1, hueco 5, color border. No lo agregues a pantallas que solo lo importan y no lo utilizan.

## 6. Navegación inferior

Superficie de ancho completo, color surface, esquinas superiores de radio 24 y elevación de sombra 2. Incluye el inset inferior de navegación del sistema. Fila con padding horizontal 8, vertical 8 y separación de 2.

Cinco columnas del mismo peso, en este orden:

| Elemento | Ruta | Icono exacto |
| --- | --- | --- |
| ESCÁNER | scanner | Outlined.QrCodeScanner |
| CATÁLOGO | catalog | Outlined.Inventory2 |
| GUÍA | guide | Outlined.Checklist |
| RESUMEN | summary | Outlined.Analytics |
| AJUSTES | diálogo | Outlined.Settings |

Cada columna: radio 18, padding horizontal 2 y vertical 9, contenido centrado, espacio vertical 4, icono 21, texto mono de 9 sp y tracking 0, una sola línea con ellipsis si hace falta. La columna seleccionada lleva fondo surfaceVariant, texto e icono primaryText y punto rojo de 3 dp bajo la etiqueta. Las no seleccionadas usan secondaryText y un punto transparente que conserva la geometría. Ajustes reserva un Spacer de 3 en esa posición y no se vuelve pestaña seleccionada.

Las etiquetas visibles se obtienen quitando los corchetes de los títulos de NavTab. Anima el tint de selección durante 180 ms. La transición entre pantallas es `fadeIn() togetherWith fadeOut()` de Compose; no inventes desplazamientos laterales ni duración personalizada si no se mide o resuelve desde la implementación original.

## 7. Autenticación: inicio de sesión y registro

Referencia completa: AuthScreen.kt. Esta pantalla no muestra navegación inferior.

Contenedor de pantalla: fillMaxSize, fondo background, safeDrawingPadding, imePadding, scroll vertical y padding horizontal 24 / vertical 28. Distribución vertical centrada cuando el espacio lo permite y accesible por scroll con teclado o contenido extenso.

Orden visual exacto:

1. Fila con LED rojo sin pulso, espacio 10, `PACKGUIDE // ACCESS` en labelMedium secondaryText, espacio flexible y botón de ajustes con icono Outlined.Settings.
2. Espacio 24.
3. HudHeader con `INICIAR SESIÓN` o `CREAR CUENTA`, sysCode y statusText vacíos, padding horizontal 0; mantiene su propio padding vertical 18 y la matriz de puntos.
4. Dos espacios consecutivos de 20 y 22: total 42. Conserva ese hueco del código actual.
5. Tarjeta de credenciales, superficie surface, radio 14, borde 1 y padding 20.
6. Separación 18 después de la tarjeta.
7. Fila con LED verde sin pulso, espacio 8 y `CUENTA ALMACENADA EN ESTE DISPOSITIVO`, labelSmall secundaria.

Interior de credenciales:

- `[ CREDENCIALES ]`, labelMedium principal; espacio 18.
- Campo USUARIO; espacio 12.
- Campo CONTRASEÑA.
- En registro: espacio 12 y campo CONFIRMAR CONTRASEÑA.
- Si hay error: espacio 12 y mensaje bodyMedium rojo, región de anuncio accesible polite.
- Espacio 20 y botón principal de ancho completo.
- Durante carga: espacio 12, indicador circular de 16 con grosor 2 rojo, espacio horizontal 10 y `VERIFICANDO CREDENCIALES...`, labelSmall secundaria.
- Espacio 10 y botón secundario de ancho completo para alternar modo.

Campos: OutlinedTextField Material 3, singleLine, ancho completo, radio 14, etiqueta en familia mono. Borde enfocado primaryText; sin enfoque border; contenedor sin enfoque surfaceVariant a alfa 0.35; etiqueta enfocada primaryText; cursor rojo. Las demás propiedades conservan los valores de Material 3, no un input web genérico. La etiqueta flotante, el notch del borde y sus posiciones forman parte de la reproducción.

Los campos secretos tienen control de texto `VER` / `OCULTAR`, labelSmall, a la derecha. No se usa un icono de ojo. En registro aparecen las ayudas exactas `Recomendado: letras, números, punto, guion o guion bajo.` y `1 a 45 caracteres: solo letras y números.`; en login no aparecen esas ayudas.

Textos de botones:

| Modo / estado | Botón principal | Botón secundario |
| --- | --- | --- |
| Inicio de sesión | [ ENTRAR ] | [ CREAR CUENTA ] |
| Registro | [ REGISTRAR ] | [ YA TENGO CUENTA ] |
| Validando | [ VALIDANDO... ] | Deshabilitado |
| Creando cuenta | [ CREANDO CUENTA... ] | Deshabilitado |

Usuario: trim para validar, longitud entre 3 y 24. Los caracteres sugeridos son una recomendación, no una restricción adicional implementada. Registro: contraseña no vacía, máximo 45, únicamente letras o números según `Char.isLetterOrDigit`, y confirmación coincidente. No impongas un mínimo de ocho caracteres: ese texto histórico está en traducciones pero no es la regla vigente. Al escribir se limpia el error. Al alternar modo se limpia confirmación, su visibilidad y error; se conserva usuario y contraseña.

La cuenta existe localmente con Room; contraseña derivada mediante el algoritmo del AuthRepository adjunto. La réplica debe conservar la interacción de registro/login y el manejo de errores, sin convertir el botón ENTRAR en una omisión incondicional del acceso. Si el destino web requiere un almacén diferente, implementa un equivalente local explícito y no lo describas como una cuenta sincronizada con Android. No copies contraseñas reales ni bases privadas.

## 8. Pantalla Escáner

Título `EQUIPAJE DEL VIAJE`, encabezado con fila `SYS // PACKING_OS` y sin estado ONLINE. Fondo background. Lista con márgenes horizontales 20 y separación vertical 14.

Primer bloque: `EQUIPAJE DEL VIAJE` en titleMedium, explicación bodySmall secundaria `Selecciona todas las maletas que llevarás. Debe quedar al menos una.`, y fila de tres opciones del mismo peso separadas por 4: MANO, CABINA y BODEGA. Cada opción tiene padding vertical 8, checkbox Material y texto labelMedium. Debe quedar al menos una seleccionada.

Por cada maleta seleccionada se dibuja una TechCard con encabezado `MALETA: {etiqueta}`. Dentro, una columna separada por 12 contiene:

1. Otra TechCard anidada, `EQUIPAJE`, con borde primaryText resuelto a alfa 0.22: es PhotoScanPanel. Conserva las dos capas de padding y borde; no las aplanes.
2. `MEDIDAS INTERIORES ÚTILES`, labelMedium.
3. Stepper de ALTO, ANCHO y PROF., en ese orden.
4. `CAPACIDAD INTERIOR TOTAL: {litros} L`, titleMedium.
5. Descripción `Una foto común no mide la maleta. Ajusta las medidas interiores útiles y confírmalas.`, bodySmall secundaria.
6. Fila con checkbox y `CONFIRMO LAS MEDIDAS: {etiqueta}`, labelSmall.

Stepper: fila de ancho completo, etiqueta con peso flexible titleMedium, botón IconButton con carácter `−`, valor `{entero} CM` en titleMedium y botón `+`. Rango de dimensiones 1 a 300, paso 1. Cualquier cambio desmarca la confirmación de esa maleta. Conserva las áreas táctiles Material del original.

PhotoScanPanel: texto `Toma o elige una foto de tu maleta.`, bodyMedium; foto opcional con ajuste Fit, ancho completo, altura limitada entre 140 y 260, radio 12 y fondo surfaceVariant; botón principal TOMAR FOTO con PhotoCamera; botón secundario ELEGIR FOTO con AddPhotoAlternate; indicador de carga condicional; mensaje opcional bodySmall secundario; descripción de referencia y cálculo que figura en el código. Separación entre hijos 12.

TOMAR FOTO invoca cámara del sistema y recibe una vista previa. ELEGIR FOTO abre selección de imagen. No hay un visor de cámara en vivo, marco de enfoque animado, medición LiDAR, FPS ni reconocimiento de objetos en esta pantalla actual.

Al final de la lista: texto sobre repartir artículos en Catálogo; espacio 12; botón CONTINUAR AL CATÁLOGO de ancho completo, habilitado solo si todas las maletas seleccionadas tienen medidas confirmadas; espacio final 20.

| Maleta | Alto | Ancho | Fondo | Capacidad sin redondear |
| --- | --- | --- | --- | --- |
| MANO | 45 | 35 | 20 | 31.5 L |
| CABINA | 55 | 35 | 25 | 48.125 L |
| BODEGA | 75 | 50 | 30 | 112.5 L |

Añadir una maleta no la activa automáticamente y comienza con todas sus cantidades en cero. El orden de presentación sigue MANO, CABINA, BODEGA entre las seleccionadas. Quitar una con artículos solicita confirmación con AlertDialog: título `QUITAR MALETA: {etiqueta}`, explicación literal del código, confirmar QUITAR MALETA, cancelar CANCELAR. Elimina sus asignaciones y su fotografía; no transfiere artículos a otra. Si era activa se activa la primera restante.

## 9. Pantalla Catálogo

Título `CATÁLOGO DE ÍTEMS`, sin fila SYS ni ONLINE. Debajo, contenedor con márgenes horizontales 20 y altura restante.

### 9.1. Búsqueda y categorías

Buscador surface, radio 16, borde 1 border, padding 12 horizontal / 10 vertical. Icono Outlined.Search 18, espacio 8 y entrada mono bodyMedium. Placeholder exacto `BUSCAR ÍTEM... [ _ ]`. Una línea. Sin botón de limpiar agregado.

Espacio 10; fila horizontal desplazable de categorías con separación 8: `[TODOS]`, `[ROPA]`, `[CALZADO]`, `[ASEO]`, `[TECH]`. Radio 16, borde 1, padding 12 horizontal / 7 vertical. Texto 11 con tracking heredado labelSmall. Activa: fondo primaryText, texto surface en negrita, LED rojo tamaño lógico 5 y espacio 6 antes del texto. Inactivas: fondo surface, borde border, texto primaryText normal. Espacio 10 después.

Búsqueda por nombre o categoría, sin distinguir mayúsculas; intersección con filtro de categoría. No elimines el filtro cuando se escribe. `TODOS` está activo inicialmente. Resultado vacío: `No hay artículos para esta búsqueda.`, bodyMedium secondaryText, padding vertical 24.

### 9.2. Región desplazable de inventario

Lista con peso 1 y separación 10. Su primer elemento contiene LuggageSelector y la explicación DESTINOS DEL VIAJE, resumen de cada maleta, etiqueta de volumen y advertencia sobre referencias del catálogo. Estos textos son contenido visible y afectan la posición de la primera tarjeta: no los omitas para colocar artículos más arriba.

LuggageSelector muestra `MALETA A ORGANIZAR`, labelMedium secundaria, y una fila de FilterChip Material 3 separados por 8 para las maletas seleccionadas. No son los chips personalizados de categorías; conserva esa diferencia.

Cada uno de los 36 artículos usa CatalogInventoryCard:

1. TechCard sin fila de headerTag.
2. Fila centrada con separación 12: caja de icono 44 × 44, fondo surfaceVariant, radio 14 e icono Material de 22; columna flexible con nombre titleMedium y texto `{dimensiones} · ESTIMACIÓN POR UNIDAD: {litros} L`, bodySmall. Separación interna de columna 4.
3. Texto de volumen rectangular de referencia, bodySmall, inmediatamente después de la fila.
4. Espacio 12; badge de categoría; espacio 12.
5. `DESTINO DEL ARTÍCULO`, labelSmall secundaria; espacio 12.
6. Una fila de asignación por maleta seleccionada, fondo surfaceVariant, radio 16 y padding 12. Columna izquierda flexible con etiqueta labelMedium y litros titleLarge. A la derecha: botón menos 48 × 48, cantidad centrada con ancho mínimo 24 y botón más 48 × 48, separación 8. Cada botón tiene radio 12, fondo surface, borde 1 border e icono de 18.
7. Espacio 12 después de cada fila; luego espacio adicional 8 y total combinado de todas las maletas en bodySmall secundaria.

Menos deshabilitado en cero y con icono secondaryText a alfa 0.45. Más incrementa en uno. Reducir la cantidad limita packedQuantity al nuevo total. Cambiar cantidades en una maleta no altera las de otra. Los totales de la tarjeta combinan maletas; el tracker inferior se refiere a la activa.

### 9.3. Tarjeta inferior de capacidad

Permanece fuera de la lista, sobre la barra de navegación. Padding externo vertical 8. Radio 20, borde 1, superficie surface, padding interno 14 y elevación explícita 0. Columna con separación 10.

Orden: maleta seleccionada labelMedium; conteos labelMedium; columna separada por 6 con descripción bodySmall, volumen titleMedium en negrita y badge de balance; barra de diez segmentos; botón CREAR PLAN DE EMPACADO con icono ArrowForward.

Segmentos: diez cajas del mismo peso, separación 4, altura 5, radio 1, borde 1. Llenas primaryText; vacías surfaceVariant con borde border. Cantidad llena = truncar(ocupación × 10), limitada entre 0 y 10. Badge oscuro con LED verde si cabe y rojo si excede. El botón navega a Guía; no inventes una operación remota, spinner o animación de optimización para este paso.

## 10. Pantalla Guía

Título `GUÍA DE EMPACADO`, sin fila técnica superior. Lista con margen horizontal 20 y separación 12. Orden exacto:

1. LuggageSelector.
2. PackingSuggestionPanel, TechCard `SUGERENCIA ORIENTATIVA`.
3. TechCard `PROGRESO REAL`, con borde solicitado primaryText y resuelto a alfa 0.22.
4. PackingGuidePreviewPanel: `VISTA ORGANIZADA DE EMPAQUE`.
5. TechCard `CONSEJO DE DISTRIBUCIÓN`.
6. Título `ASIGNACIÓN EN VALIJA` titleLarge y explicación bodySmall.
7. Tarjetas de los artículos con cantidad positiva de la maleta activa, o estado vacío.
8. Botón VER RESUMEN y espacio final 16.

Sugerencia: columna separada por 10; descripción bodySmall, volumen displaySmall (rojo solo si excede), advertencia cuando no están confirmadas las dimensiones, recomendación calculada y TextButton `VER CRITERIOS Y CONSEJOS`. Al expandirse cambia a `OCULTAR DETALLES` y revela con AnimatedVisibility los cuatro párrafos exactos de la implementación. No muestres los detalles inicialmente.

Progreso: texto `{empacadas} de {seleccionadas} unidades empacadas · {porcentaje}%`, titleMedium; espacio 8; LinearProgressIndicator altura 4, primaryText, pista surfaceVariant y sin indicador de parada; espacio 12; instrucciones bodySmall. El porcentaje se calcula de unidades, no de número de tipos.

Consejo de distribución: `Coloca el calzado y los objetos pesados en la base; la ropa en el centro y lo que necesitarás durante el viaje arriba.`, bodyMedium.

Tarjeta de artículo: icono y nombre titleMedium a la izquierda, `{packedQuantity}/{quantity}` a la derecha; espacio 10; fila de dos AsciiButton con igual peso y separación 8. Primer botón secundario `ZONA: {zona} ↻`; segundo `MARCAR EMPACADO` principal o `DESMARCAR` secundario cuando se completó. El primero cicla BASE → CENTRO → ACCESO RÁPIDO → BASE. El segundo incrementa una unidad; si ya están todas, vuelve a cero. No es un booleano que marque todas las unidades desde la primera pulsación.

Si no hay artículos: `Aún no hay artículos seleccionados. Añádelos en el catálogo.` y botón IR AL CATÁLOGO. Mantén los demás paneles de la pantalla según el código.

## 11. Fotografías y vista organizada de Guía

Panel con separación 12 entre hijos. Texto sobre funcionamiento sin conexión y composición de foto original más inventario. Sin foto: mensaje y botón AÑADIR FOTO EN ESCÁNER. Con foto: etiqueta FOTO DE REFERENCIA, imagen Fit de ancho completo con altura entre 100 y 180 y TextButton CAMBIAR FOTO.

Sin artículos: mensaje de selección y botón IR AL CATÁLOGO. Botón GENERAR IMAGEN ORGANIZADA, de ancho completo, habilitado solo con foto, sin carga de foto, inventario no vacío y sin trabajo de generación en curso. Durante trabajo muestra indicador, texto `Creando vista organizada…` o `Abriendo fotos…` según el trabajo, y CANCELAR secundario.

La generación usa OfflinePackingImageService, no IA remota. Copia el resultado de Canvas o su equivalente fiel. La foto permanece sin modificar dentro de una composición con datos y un esquema. Si cambia el plan o la foto, el resultado anterior no se debe seguir mostrando como vigente; aparece el aviso del código.

Imagen generada visible: ajuste Fit, ancho completo y altura limitada entre 160 y 340; pulsar la imagen o `Ampliar vista organizada` abre diálogo con ancho de plataforma desactivado, superficie de ancho completo, 95% de altura y padding 12, imagen flexible y botón CERRAR con altura mínima 48.

### 11.1. Especificación del bitmap generado

La composición es una excepción deliberada a la paleta del resto de la UI. Tiene anchura de 1200 píxeles y altura variable. Usa fondo #F2F4F7, tinta #0F0F11, texto secundario #5E646E, rojo #D22020, tarjetas blancas, bordes #D1D5DB y cajas de artículos #F5F7F9. No uses los marfiles de las tarjetas para corregir estos colores históricos del generador: forman parte de su salida actual.

La fuente del bitmap se crea como `sans-serif` normal. Título en (48,65) a 36 px; subtítulo en (48,105) a 26. Caja de foto (48,140)-(600,470), foto centrada en (324,305), ajuste dentro de 528 × 306 sin recorte. Etiqueta de foto en (48,505), 24. Inventario empieza en x=640: título y=180; unidades y=230; volumen y=285; descripción y=325; balance y=380; dimensiones y=430.

Asa de maleta: rectángulo (480,535)-(720,575). Cuerpo: (48,565)-(1152,altura-100), radio 28. Zonas desde y=610, en orden ACCESO RÁPIDO, CENTRO, BASE. Artículos en dos columnas x=80 y x=610; cajas de 510 × 98, paso vertical 112; ilustraciones lineales por categoría y textos con truncamiento según ancho medido. El pie se dibuja en (48,altura-50), 26 px. Grosor de borde de cajas 2; ilustraciones 3.

Altura = `750 + suma por zona(56 + (62 si está vacía; si no, ceil(número de tipos / 2) × 112 + 8))`. Límite de altura 4096 y máximo 64 tipos por plan. El Canvas original completo está incluido en el anexo y prevalece para las coordenadas restantes.

## 12. Pantalla Resumen

Título `RESUMEN DEL EQUIPAJE`, sin fila técnica superior. Lista con margen horizontal 20, separación 12. Orden real:

1. TechCard TODAS LAS MALETAS: por maleta, nombre, volumen, empacados/seleccionados en bodyMedium; rojo si excede; separación 6; total global labelMedium.
2. LuggageSelector.
3. PackingReviewPreviewPanel, REFERENCIA PARA REVISAR TU MALETA.
4. TechCard MALETA SELECCIONADA.
5. TechCard CAPACIDAD ESTIMADA.
6. TechCard ESTADO DE EMPACADO.
7. Título DETALLE POR ARTÍCULO, titleLarge.
8. TechCard DISTRIBUCIÓN POR ZONA.
9. Tarjetas de artículos con cantidad positiva o mensaje `No hay artículos seleccionados.`
10. EDITAR CATÁLOGO secundario con Inventory2; espacio 8; CONTINUAR EMPACANDO principal; espacio final 16.

Aunque el título DETALLE POR ARTÍCULO aparezca antes de DISTRIBUCIÓN POR ZONA, conserva ese orden del código.

MALETA SELECCIONADA: borde primaryText a alfa 0.22, icono Outlined.Luggage rojo, etiqueta y medidas `{alto}×{ancho}×{fondo} cm` titleMedium; espacio 8; aviso sobre medidas interiores, exteriores y normas de aerolínea bodySmall secundario.

CAPACIDAD ESTIMADA: descripción bodySmall, volumen headlineMedium, ocupación bodySmall; espacio 6; barra de 4 dp con valor visual limitado entre 0 y 1, color rojo si excede o primaryText si no; espacio 10; balance. El porcentaje numérico puede exceder 100%; la barra no.

ESTADO DE EMPACADO: fila CheckCircle verde y empacados; espacio 6; RadioButtonUnchecked secundario y pendientes; espacio 6; número de tipos y unidades bodySmall.

DISTRIBUCIÓN POR ZONA usa orden BASE, CENTRO, ACCESO RÁPIDO, con unidades y litros sumados de los artículos seleccionados de la maleta activa, bodyMedium y separación 4. Este orden es diferente al dibujo del bitmap y debe mantenerse.

Detalle por artículo: icono, nombre titleMedium y contador empacados/seleccionados; espacio 4; `{zona} · {litros} L` bodySmall secundaria. No agregues controles de edición en estas tarjetas.

### 12.1. Panel de referencia para revisión

Explica que admite hasta cuatro fotos y usa la primera como referencia, con procesamiento local y sin calificación automática. Fila horizontal desplazable separada por 10; cada miniatura ocupa columna de ancho 180, imagen Fit de altura 150 y TextButton QUITAR FOTO. No conviertas las miniaturas en una cuadrícula.

Botones en columna: AÑADIR FOTOS secundario, TOMAR FOTO secundario y GENERAR REFERENCIA VISUAL principal. Los dos primeros se deshabilitan con cuatro fotos o durante trabajo. Generar requiere alguna foto, contenido seleccionado y ningún trabajo activo. Estado de carga, cancelación, errores y ampliación funcionan como en Guía. Si cambia el plan, muestra el aviso de regenerar.

## 13. Diálogo Ajustes

Referencia SettingsDialog.kt. Dialog de plataforma, superficie de ancho disponible dentro del diálogo, radio 22, borde 1 border, padding 20 y separación vertical 10. Conserva el scrim y comportamiento de cierre del Dialog original; si se adapta a web, mide la referencia en vez de inventar su opacidad.

Orden: `[ AJUSTES DE SISTEMA ]` titleMedium; `PERSONALIZA PACKGUIDE` labelSmall secundaria; Spacer 4; fila TEMA; fila IDIOMA; fila CERRAR SESIÓN solo después de autenticarse; TextButton `[ CERRAR ]` alineado al final.

Cada fila tiene fondo background, radio 2, borde 1 border, padding horizontal 12 y vertical 14, icono a la izquierda, espacio 12, columna flexible con título labelMedium y valor labelSmall separado 3, y carácter `›` a la derecha titleMedium. Toda la fila se puede pulsar. No dibujes switches o selectores desplegables: cada pulsación alterna el valor.

TEMA usa LightMode con CLARO o DarkMode con OSCURO; IDIOMA usa Language con ESPAÑOL o ENGLISH; cierre usa Logout, título rojo y valor SALIR DE LA CUENTA secundario. Cerrar sesión limpia fotos y vistas, vuelve al acceso y permite crear una nueva sesión de contenido. Tema e idioma sí se guardan en DataStore.

## 14. Datos de catálogo, cálculos y localización

Reproduce los 36 elementos en su orden exacto, con IDs, nombres, categorías, iconos, dimensiones y litros por unidad de `defaultCatalogItems()` en PackingState.kt. El anexo incluye los datos completos para evitar transcripción aproximada.

Capacidad = alto × ancho × profundidad / 1000, usando las medidas interiores, sin reserva adicional. Volumen utilizado = suma de cantidad × litros por unidad. Los litros por unidad son supuestos independientes; no se recalculan a partir de la caja rectangular de referencia de cada objeto. En pantalla se muestran ambas métricas, con etiquetas diferentes.

Nunca apliques el descuento del 20% que aparece en algunas frases históricas del diccionario. `PackingRules` usa suma decimal para evitar errores de exceso en ajustes exactos. En una adaptación a JavaScript utiliza aritmética decimal o unidades enteras adecuadas a la precisión de los datos; no permitas que errores binarios activen una alarma de exceso cuando el volumen coincide exactamente.

Formato de litros: una cifra decimal con punto, usando Locale.US; entre cero exclusivo y 0.05 exclusivo presenta `<0.1`. No uses coma decimal por el idioma español. Redondea para presentar, sin redondear los valores internos antes de comparar. No sustituyas 48.125 por 48.1 dentro del modelo.

Exceso cuando usado > capacidad, estrictamente. Espacio libre = max(capacidad-usado,0). Exceso = max(usado-capacidad,0). Ocupación = usado/capacidad. Porcentaje de ocupación = truncar(ocupación × 100). Progreso = división entera de empacadas × 100 / seleccionadas; cero cuando no hay unidades. No muestres NaN, Infinity ni porcentajes de ejemplo.

Español inicial e inglés alternable. Utiliza el diccionario y las funciones reales de AppText.kt y PackingText.kt. `localizedText` aplica traducción exacta y fallback de términos comunes; `previewText` tiene sus propias parejas de textos. Respeta espacios, acentos, mayúsculas, corchetes, `×`, `·`, `↻`, `−`, `›` y los distintos usos de `...` y `…`.

No reconstruyas pantallas antiguas a partir de claves como `LIDAR ACTIVE`, `PESO TOTAL: 9.8 KG`, `EFICIENCIA DE VOLUMEN: 92%`, `IATA VALID`, pasos de doblado o exportaciones. Su presencia en traducciones no demuestra que sean contenido actual.

## 15. Adaptación específica si el destino es web

Esta sección solo se aplica si se ha elegido web. El objetivo visual sigue siendo la app móvil. Mantén la composición vertical y la barra inferior también en una ventana grande; no conviertas el producto en un dashboard de escritorio por iniciativa propia.

1. Establece un viewport de comparación equivalente al ancho y alto útiles en dp del dispositivo Android de referencia. Usa 1 dp → 1 CSS px solo como escala lógica inicial, con zoom 100% y escala de fuente equivalente, y calibra contra capturas. No equipares píxeles físicos con CSS px.
2. Al comparar imágenes, normaliza la densidad y recorta las regiones de sistema que no pertenezcan a la aplicación. No dibujes una hora, señal, batería, barra de gestos o marco de teléfono falsos dentro del producto para simular Android.
3. Usa `box-sizing: border-box`, elimina márgenes nativos de página y define explícitamente tipografía y bordes de controles. No permitas que los estilos predeterminados del navegador decidan los botones, inputs o checkboxes.
4. Modela el shell como filas de contenido y navegación; usa `min-height: 0` en los hijos flex/grid que deban desplazarse. El catálogo debe conservar búsqueda y tracker fuera de la lista. Evita dos scrolls verticales de página y contenido a la vez.
5. Maneja la altura visible y los safe areas reales del navegador. El teclado no debe tapar el acceso ni sus errores. No dupliques un inset reservado ya por otro contenedor.
6. Proporciona las fuentes correctas si están disponibles y comprueba su licencia. Monospace o sans-serif del sistema de Windows no garantizan la misma anchura que Android. Ajusta familia y métricas con referencia, sin deformar todo el layout con un transform global.
7. Usa los vectores de Material Outlined correspondientes. No sustituyas automáticamente por Lucide, emojis, iconos filled o caracteres Unicode parecidos. Si extraes vectores de la dependencia original, respeta su licencia y viewBox.
8. Recrea la matriz con SVG o Canvas y las coordenadas descritas. El LED debe reservar también su halo; no basta un círculo CSS de 8 píxeles.
9. Reproduce los campos outlined de etiqueta flotante, radios y estados. Reproduce FilterChip y Checkbox Material de la referencia. No supongas que instalar una biblioteca Material web produce el mismo tamaño que Compose.
10. Imágenes con `object-fit: contain`; no recortes la maleta con cover. La composición organizada debe conservar sus coordenadas internas de 1200 píxeles y escalarse como una sola imagen al mostrarse.
11. Para tomar/elegir imágenes adapta los mecanismos del navegador. Si no hay cámara disponible, conserva un error explícito y la alternativa de elegir archivo. Nunca muestres una cámara simulada como captura real.
12. Portar comportamiento a web puede requerir equivalentes de Room, DataStore y Bitmap; mantén los límites de persistencia visibles y el funcionamiento local. No añadas autenticación remota, API de IA o claves externas para una réplica visual.
13. No uses longitudes fijas para el contenido completo ni posiciones absolutas para cada tarjeta. Reserva coordenadas absolutas para los dibujos que ya las usan: matriz, LEDs y composición bitmap.
14. Para un monitor ancho sin referencia aprobada, permite una región móvil de revisión con ancho igual al viewport objetivo, fondo exterior discreto y sin controles nuevos. Esa envoltura no debe incluirse al comparar el recorte de la app. No inventes un ancho de dispositivo y luego lo declares una medida extraída de Android.

Los renderizadores Android y navegador pueden diferir en hinting de fuente, antialiasing y controles del sistema. Trabaja para eliminar discrepancias medibles de la UI y registra las diferencias de plataforma que queden. No prometas igualdad literal de todos los píxeles únicamente por haber copiado estos valores.

## 16. Procedimiento de implementación y validación visual

### 16.1. Antes de construir

Lee este documento, el anexo y el proyecto destino. Enumera sus pantallas y componentes reales. Determina plataforma, viewport de referencia, densidad, fontScale, tema, idioma y versión. Separa hechos medidos de supuestos. Si faltan capturas, comienza con las medidas del código; el objetivo final de pixel perfect requerirá comparación visual contra un render de referencia.

No dependas de que el usuario vuelva a copiar todo el código fuente en el chat: este archivo ya contiene la instantánea completa. Si el contexto es limitado, lee el documento por secciones y consulta los bloques de código por la ruta del archivo.

### 16.2. Orden recomendado

1. Tokens y tipografía.
2. Matriz, LED, AsciiButton, TechCard, TechBadge y HudHeader.
3. Autenticación y SettingsDialog.
4. Shell y navegación inferior.
5. Modelo completo de maletas e inventario.
6. Escáner, fotos y confirmación de medidas.
7. Catálogo, filtros, cantidades y tracker.
8. Guía, zonas, progreso y composición local.
9. Resumen, revisión y ampliación.
10. Tema oscuro e inglés en todos los estados anteriores.
11. Comparación visual e iteraciones de ajuste.

### 16.3. Matriz mínima de comprobación

| Caso | Comportamiento o evidencia requerida |
| --- | --- |
| Login vacío | Posiciones y botones iniciales; sin barra inferior |
| Registro | Tres campos, ayudas, cambio de altura y scroll |
| Error de acceso | Mensaje correcto, color rojo y desplazamiento natural |
| Contraseña visible | VER / OCULTAR sin cambios de geometría no previstos |
| Ajustes en login | Tema e idioma; sin cierre de sesión |
| Ajustes tras acceso | Incluye CERRAR SESIÓN |
| Escáner inicial | CABINA, 55×35×25, sin foto, continuar deshabilitado |
| Medidas confirmadas | Continuar habilitado al confirmar todas las maletas |
| Cambio posterior de medida | Desmarca confirmación y actualiza capacidad |
| Múltiples maletas | Datos independientes y orden estable |
| Quitar maleta con contenido | Diálogo, cancelar y confirmar correctos |
| Catálogo inicial | 36 tipos, cantidades originales y tracker 19.2/48.1 |
| Búsqueda / filtro | Intersección correcta y estado sin coincidencias |
| Cantidad cero | Menos deshabilitado, no negativos |
| Empaque parcial | Contador y porcentaje derivados de unidades |
| Empaque completo | DESMARCAR y vuelta a cero al pulsar |
| Cambio de zona | Ciclo de tres zonas y actualización del resumen |
| Capacidad exacta | No aparece exceso por un error decimal |
| Exceso | Texto rojo, badge y barra correctos sin recortar el dato numérico |
| Guía sin foto / sin artículos | Acciones y botones deshabilitados correctos |
| Vista organizada | Foto intacta, datos, tres zonas y ampliación |
| Plan cambiado | Aviso de regeneración y resultado vigente correcto |
| Revisión con 4 fotos | No permite una quinta; puede quitar y reemplazar |
| Operación cancelada / foto inválida | Mensajes originales y recuperación |
| Tema oscuro | Tokens semánticos correctos; bitmap mantiene paleta propia |
| Inglés | Diccionario correcto, sin cortar controles por traducciones |
| Teclado / pantalla pequeña | Acceso desplazable, contenido visible y sin solapamientos |
| Cierre de sesión | Regreso al acceso, limpieza de fotos y vistas |

### 16.4. Comparación pixel a pixel

Cuando haya emulador/dispositivo o capturas disponibles, captura ambas implementaciones en estados equivalentes. Espera a que terminen las transiciones y cargas; usa la misma fotografía y el mismo plan. Fija idioma, tema y scroll; normaliza densidad y tamaño. Compara recortes idénticos.

Genera superposición al 50% y un mapa de diferencias si las herramientas disponibles lo permiten. Ajusta en este orden: geometría global e insets, fuente y métricas, contenedores, textos y saltos de línea, iconos, bordes, colores y estados. Verifica la posición del primer artículo, la altura del tracker y el borde superior de la barra inferior: son puntos donde suelen acumularse diferencias.

No uses un porcentaje arbitrario de similitud como prueba suficiente. Registra los desajustes concretos por pantalla y no ignores cambios visibles en espaciado o iconografía porque el fondo ocupa muchos píxeles. Si no existe referencia ejecutada o captura, entrega una implementación basada en código y declara que falta contrastarla visualmente.

Ejecuta las comprobaciones del proyecto destino apropiadas a los cambios. Para el Android original, la compilación es `gradlew.bat assembleDebug` en Windows; ejecuta las pruebas de cálculos/estado pertinentes si modificas su comportamiento. No afirmes que compilaste, abriste la app o comparaste capturas si no lo hiciste.

## 17. Entregable y condiciones de finalización

Entrega la aplicación implementada, con las seis vistas principales contando acceso y ajustes, todos sus estados, los datos completos, las fotografías y la composición local. Incluye instrucciones concretas para ejecutar el proyecto destino.

En tu informe final indica qué archivos cambiaste, qué verificaste realmente y qué diferencias visuales siguen pendientes. Si una capacidad de plataforma no puede reproducirse, conserva el flujo más fiel disponible y describe la diferencia concreta. No declares «pixel perfect» sin evidencia visual al mismo tamaño y estado.

El trabajo no termina al entregar tokens, una única pantalla, una captura estática, un plan o cuatro pestañas con contenido provisional. Continúa hasta que el producto completo sea navegable y sus cálculos estén conectados a estado compartido real.

## 18. Anexo: instantánea íntegra de las fuentes de referencia

Los bloques siguientes son referencias del proyecto Android y no una orden de copiar Kotlin dentro de un proyecto web. Conservan imports, comentarios, valores, datos y funciones originales para poder resolver cualquier detalle omitido en la descripción. Cada bloque está identificado por su ruta y una huella SHA-256 del archivo original.

Se incluyen las fuentes Kotlin de producción, configuración Gradle, manifiesto y recursos XML de producción. No se incluyen contraseñas, bases locales de cuentas, claves, `local.properties`, cachés ni artefactos de compilación. Los iconos Material vienen de las dependencias; las imágenes del lanzador son recursos binarios del repositorio y no se utilizan como ilustraciones de estas pantallas.

<!-- PACKGUIDE_SOURCE_SNAPSHOT_START -->

Archivos de referencia incluidos: 57. Las huellas describen los bytes originales; el contenido de los bloques conserva el texto UTF-8.

### Fuente: `app/build.gradle.kts`

SHA-256: `34d3056f3f05e7ed808c8f8a20114c95926d9818016f14ca7d65d2aa387c9cb2`

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.example.packguide"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.packguide"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.exifinterface)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    "ksp"(libs.androidx.room.compiler)
}
```

### Fuente: `app/src/main/AndroidManifest.xml`

SHA-256: `b0caeae1861f1c82d4ad80c73e2e1ba09c12644f2be39d8ad51502b9211868df`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.PackGuide">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.PackGuide"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

### Fuente: `app/src/main/java/com/example/packguide/ai/PackingImageService.kt`

SHA-256: `d2a289d08e7f52bc85734a25a42634dc764e4ab2538bcc384551bbbf1932f80a`

```kotlin
package com.example.packguide.ai

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.packguide.scanning.PackingRules
import com.example.packguide.settings.AppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.localizedText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

/** UI-independent boundary for a future authenticated server implementation. No provider secrets. */
fun interface PackingImageService {
    suspend fun generate(plan: PackingPlan, photo: Bitmap, language: AppLanguage): Bitmap
}

/** A local composition, not photo editing, object recognition, or a physical packing simulation. */
class OfflinePackingImageService : PackingImageService {
    override suspend fun generate(plan: PackingPlan, photo: Bitmap, language: AppLanguage): Bitmap =
        withContext(Dispatchers.Default) {
            require(!photo.isRecycled && photo.width > 0 && photo.height > 0)
            val layout = PackingPreviewLayout.from(plan)
            val english = language == AppLanguage.English
            fun text(es: String, en: String) = if (english) en else es
            val result = Bitmap.createBitmap(1200, layout.height, Bitmap.Config.ARGB_8888)
            try {
                val canvas = Canvas(result)
                val ink = Color.rgb(15, 15, 17)
                val muted = Color.rgb(94, 100, 110)
                val red = Color.rgb(210, 32, 32)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                fun label(value: String, x: Float, y: Float, size: Float = 28f, color: Int = ink) {
                    paint.style = Paint.Style.FILL
                    paint.color = color
                    paint.textSize = size
                    paint.typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
                    canvas.drawText(value, x, y, paint)
                }
                fun box(rect: RectF, fill: Int, stroke: Int = Color.rgb(209, 213, 219), radius: Float = 12f) {
                    paint.style = Paint.Style.FILL; paint.color = fill
                    canvas.drawRoundRect(rect, radius, radius, paint)
                    paint.style = Paint.Style.STROKE; paint.strokeWidth = 2f; paint.color = stroke
                    canvas.drawRoundRect(rect, radius, radius, paint)
                    paint.style = Paint.Style.FILL
                }
                fun fitLabel(value: String, x: Float, y: Float, width: Float, size: Float, color: Int = ink) {
                    paint.textSize = size
                    val count = paint.breakText(value, true, width, null)
                    label(if (count < value.length) value.take((count - 1).coerceAtLeast(0)) + "…" else value,
                        x, y, size, color)
                }
                canvas.drawColor(Color.rgb(242, 244, 247))
                label(text("VISTA ORGANIZADA DE EMPAQUE", "ORGANIZED PACKING PREVIEW"), 48f, 65f, 36f)
                label(text("Composición local · foto original + inventario", "Local composition · original photo + inventory"), 48f, 105f, 26f, muted)
                box(RectF(48f, 140f, 600f, 470f), Color.WHITE)
                val scale = minOf(528f / photo.width, 306f / photo.height)
                val photoWidth = photo.width * scale; val photoHeight = photo.height * scale
                val photoRect = RectF(324f - photoWidth / 2, 305f - photoHeight / 2,
                    324f + photoWidth / 2, 305f + photoHeight / 2)
                canvas.drawBitmap(photo, null, photoRect, paint)
                val estimate = PackingRules.estimate(plan.dimensions[0], plan.dimensions[1], plan.dimensions[2],
                    plan.contents.map { it.quantity to it.unitVolumeL })
                val copy = PackingText(language)
                label(text("FOTO DE REFERENCIA", "REFERENCE PHOTO"), 48f, 505f, 24f, muted)
                label(text("INVENTARIO SELECCIONADO", "SELECTED INVENTORY"), 640f, 180f, 26f)
                label("${plan.contents.sumOf { it.quantity }} " + text("unidades", "units"), 640f, 230f, 34f)
                label(copy.volume(estimate), 640f, 285f, 30f)
                label(text("Estimación / capacidad interior", "Estimate / interior capacity"), 640f, 325f, 24f, muted)
                label(copy.balance(estimate), 640f, 380f, 27f, if (estimate.exceedsCapacity) red else ink)
                label(plan.dimensions.joinToString(" × ") + " cm", 640f, 430f, 27f, muted)
                // A suitcase-shaped diagram below the unmodified reference photo.
                box(RectF(480f, 535f, 720f, 575f), Color.WHITE, ink)
                box(RectF(48f, 565f, 1152f, layout.height - 100f), Color.WHITE, ink, 28f)
                var y = 610f
                layout.zones.forEach { zone ->
                    currentCoroutineContext().ensureActive()
                    label(localizedText(zone.label, language), 80f, y, 30f)
                    y += 22f
                    if (zone.items.isEmpty()) {
                        label(text("Sin artículos asignados", "No assigned items"), 90f, y + 30f, 25f, muted)
                        y += 62f
                    } else {
                        zone.items.forEachIndexed { index, item ->
                            val x = if (index % 2 == 0) 80f else 610f
                            val rowY = y + (index / 2) * 112f
                            box(RectF(x, rowY, x + 510f, rowY + 98f), Color.rgb(245, 247, 249))
                            // Simple category illustrations are schematic, never detected objects.
                            paint.color = ink; paint.style = Paint.Style.STROKE; paint.strokeWidth = 3f
                            when (item.category) {
                                "CALZADO" -> {
                                    canvas.drawRoundRect(RectF(x + 16f, rowY + 48f, x + 70f, rowY + 72f), 8f, 8f, paint)
                                    canvas.drawLine(x + 16f, rowY + 48f, x + 24f, rowY + 28f, paint)
                                    canvas.drawLine(x + 24f, rowY + 28f, x + 42f, rowY + 48f, paint)
                                }
                                "ASEO" -> {
                                    canvas.drawRoundRect(RectF(x + 25f, rowY + 32f, x + 60f, rowY + 76f), 5f, 5f, paint)
                                    canvas.drawRect(x + 33f, rowY + 20f, x + 52f, rowY + 32f, paint)
                                }
                                "TECH" -> {
                                    canvas.drawRoundRect(RectF(x + 24f, rowY + 22f, x + 62f, rowY + 76f), 5f, 5f, paint)
                                    canvas.drawLine(x + 32f, rowY + 65f, x + 54f, rowY + 65f, paint)
                                }
                                else -> {
                                    canvas.drawRoundRect(RectF(x + 18f, rowY + 30f, x + 69f, rowY + 70f), 8f, 8f, paint)
                                    canvas.drawLine(x + 18f, rowY + 49f, x + 69f, rowY + 49f, paint)
                                }
                            }
                            paint.style = Paint.Style.FILL
                            fitLabel("${item.quantity} × ${localizedText(item.name, language)}", x + 88f, rowY + 38f, 405f, 26f)
                            fitLabel(item.dimensions, x + 88f, rowY + 73f, 405f, 23f, muted)
                        }
                        y += ((zone.items.size + 1) / 2) * 112f + 8f
                    }
                    y += 34f
                }
                label(text("Esquema orientativo; no mide ni garantiza el ajuste físico.",
                    "Illustrative layout; it does not measure or guarantee physical fit."), 48f, layout.height - 50f, 26f, muted)
                currentCoroutineContext().ensureActive()
                result
            } catch (error: Throwable) {
                result.recycle()
                throw error
            }
        }
}

data class PreviewZone(val label: String, val items: List<PlanItem>)
data class PackingPreviewLayout(val zones: List<PreviewZone>, val height: Int) {
    companion object {
        fun from(plan: PackingPlan): PackingPreviewLayout {
            require(plan.dimensions.size == 3 && plan.dimensions.all { it in 1..300 })
            require(plan.contents.isNotEmpty() && plan.contents.size <= 64)
            val order = listOf("ACCESO RÁPIDO", "CENTRO", "BASE")
            require(plan.contents.all { it.quantity > 0 && it.zone in order && it.unitVolumeL.isFinite() && it.unitVolumeL >= 0 })
            val zones = order.map { label -> PreviewZone(label, plan.contents.filter { it.zone == label }) }
            val height = 750 + zones.sumOf { 56 + if (it.items.isEmpty()) 62 else ((it.items.size + 1) / 2) * 112 + 8 }
            require(height <= 4096)
            return PackingPreviewLayout(zones, height)
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ai/PackingPlan.kt`

SHA-256: `c6699855e793a27b88dbd6546a67c3a4d7626d52296205251f89534e39111f7c`

```kotlin
package com.example.packguide.ai

import com.example.packguide.ui.screens.LuggagePacking

/** Immutable request input. Packing checkmarks do not change the desired layout. */
data class PackingPlan(val dimensions: List<Int>, val contents: List<PlanItem>) {
    val description: String get() = "Medidas interiores: ${dimensions.joinToString(" × ")} cm.\n" +
        "Capacidad interior total: ${com.example.packguide.scanning.PackingRules.rectangularVolumeL(dimensions[0], dimensions[1], dimensions[2])} L. Sin reserva adicional.\n" +
        "Las medidas de artículos son referencias; los litros son estimaciones independientes, sin compresión calibrada.\n" +
        contents.joinToString("\n") { "${it.quantity} × ${it.name}; referencia ${it.dimensions}; estimación por unidad ${it.unitVolumeL} L; zona ${it.zone}" }

    companion object {
        fun from(bag: LuggagePacking) = PackingPlan(
            listOf(bag.heightCm.value, bag.widthCm.value, bag.depthCm.value),
            bag.items.filter { it.quantity > 0 }.map { PlanItem(it.name, it.quantity, it.dimensions, it.zone.label, it.unitVolumeL, it.category) }
        )
    }
}

data class PlanItem(val name: String, val quantity: Int, val dimensions: String, val zone: String,
                    val unitVolumeL: Double, val category: String)
```

### Fuente: `app/src/main/java/com/example/packguide/ai/PackingPreviewViewModel.kt`

SHA-256: `d0af15ddaadc7778b41a96baa665461bc58aa8a5d4070e629ebd408a0fbc608d`

```kotlin
package com.example.packguide.ai

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.packguide.scanning.PhotoScanner
import com.example.packguide.scanning.PhotoReadException
import com.example.packguide.scanning.PhotoProblem
import com.example.packguide.settings.AppLanguage
import com.example.packguide.ui.screens.LuggagePreset
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import java.io.IOException

enum class PreviewProblem { Photo, UnsupportedPhoto, LargePhoto, Generation, Connection, Cancelled }
enum class PreviewWork { Photos, Guide, Review }

data class PackingPreviewState(
    val work: PreviewWork? = null,
    val guide: Bitmap? = null,
    val guidePlan: PackingPlan? = null,
    val guideSource: Bitmap? = null,
    val photos: List<Bitmap> = emptyList(),
    val review: Bitmap? = null,
    val reviewPlan: PackingPlan? = null,
    val problem: PreviewProblem? = null
)

class PackingPreviewViewModel(application: Application, private val service: PackingImageService) : AndroidViewModel(application) {
    constructor(application: Application) : this(application, OfflinePackingImageService())

    private val states = mutableStateMapOf<LuggagePreset, PackingPreviewState>()
    private val jobs = mutableMapOf<LuggagePreset, Job>()
    fun stateFor(type: LuggagePreset) = states[type] ?: PackingPreviewState()

    fun retain(types: List<LuggagePreset>) {
        (states.keys.toList() - types.toSet()).forEach { type ->
            jobs.remove(type)?.cancel()
            states.remove(type)
        }
    }

    fun clear() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
        states.clear()
    }

    fun cancel(type: LuggagePreset) {
        jobs.remove(type)?.cancel()
        states[type] = stateFor(type).copy(work = null, problem = PreviewProblem.Cancelled)
    }

    fun photoError(type: LuggagePreset) { states[type] = stateFor(type).copy(problem = PreviewProblem.Photo) }

    fun removePhoto(type: LuggagePreset, index: Int) {
        val state = stateFor(type)
        if (state.work != null) return
        states[type] = state.copy(photos = state.photos.filterIndexed { i, _ -> i != index },
            review = null, reviewPlan = null, problem = null)
    }

    fun addPhotos(type: LuggagePreset, uris: List<Uri>) {
        val slots = 4 - stateFor(type).photos.size
        if (slots <= 0 || uris.isEmpty()) return // Closing the picker preserves the previous selection.
        launch(type, PreviewWork.Photos) {
            val photos = mutableListOf<Bitmap>()
            try {
                for (uri in uris.take(slots)) photos += PhotoScanner().decode(getApplication(), uri)
                currentCoroutineContext().ensureActive()
                states[type] = stateFor(type).copy(photos = stateFor(type).photos + photos, review = null, reviewPlan = null)
            } catch (error: Throwable) {
                photos.forEach { it.recycle() }
                throw error
            }
        }
    }

    fun addPhoto(type: LuggagePreset, photo: Bitmap) {
        val state = stateFor(type)
        if (state.work != null || state.photos.size >= 4 || photo.isRecycled) return
        states[type] = state.copy(photos = state.photos + photo, review = null, reviewPlan = null, problem = null)
    }

    fun generate(type: LuggagePreset, plan: PackingPlan, source: Bitmap, language: AppLanguage, review: Boolean = false) {
        if (plan.contents.isEmpty()) return
        launch(type, if (review) PreviewWork.Review else PreviewWork.Guide) {
            val image = service.generate(plan, source, language)
            currentCoroutineContext().ensureActive()
            states[type] = if (review) stateFor(type).copy(review = image, reviewPlan = plan)
                else stateFor(type).copy(guide = image, guidePlan = plan, guideSource = source)
        }
    }

    private fun launch(type: LuggagePreset, work: PreviewWork, block: suspend () -> Unit) {
        if (stateFor(type).work != null) return
        states[type] = stateFor(type).copy(work = work, problem = null)
        val job = viewModelScope.launch(start = CoroutineStart.LAZY) {
            try { block() }
            catch (cancelled: CancellationException) { throw cancelled }
            catch (_: OutOfMemoryError) { states[type] = stateFor(type).copy(problem = PreviewProblem.LargePhoto) }
            catch (error: Exception) {
                val problem = when {
                    error is PhotoReadException && error.problem == PhotoProblem.TooLarge -> PreviewProblem.LargePhoto
                    error is PhotoReadException && error.problem == PhotoProblem.Unsupported -> PreviewProblem.UnsupportedPhoto
                    work == PreviewWork.Photos -> PreviewProblem.Photo
                    error is IOException -> PreviewProblem.Connection
                    else -> PreviewProblem.Generation
                }
                states[type] = stateFor(type).copy(problem = problem)
            } finally {
                if (jobs[type] == coroutineContext[Job]) {
                    states[type] = stateFor(type).copy(work = null)
                    jobs.remove(type)
                }
            }
        }
        jobs[type] = job
        job.start()
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/data/auth/AuthRepository.kt`

SHA-256: `f833d7135d1a63adab226d5a79888bef5c21087db03c8bd76c953046bac8d78b`

```kotlin
package com.example.packguide.data.auth

import android.database.sqlite.SQLiteConstraintException
import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64
import java.util.Locale
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed interface AuthResult {
    data object Success : AuthResult
    data object InvalidCredentials : AuthResult
    data object InvalidInput : AuthResult
    data object UsernameTaken : AuthResult
    data object StorageError : AuthResult
}

class AuthRepository(private val userAccountDao: UserAccountDao) {
    suspend fun register(username: String, password: String): AuthResult {
        val normalizedUsername = normalizeUsername(username)
        if (AuthValidation.usernameError(normalizedUsername) != null ||
            AuthValidation.passwordError(password) != null
        ) return AuthResult.InvalidInput
        return runCatching {
            if (userAccountDao.findByUsername(normalizedUsername) != null) {
                return@runCatching AuthResult.UsernameTaken
            }
            val salt = ByteArray(SALT_SIZE).also(SecureRandom()::nextBytes)
            userAccountDao.insert(
                UserAccount(
                    username = normalizedUsername,
                    passwordHash = withContext(Dispatchers.Default) {
                        derivePasswordHash(password, salt)
                    },
                    passwordSalt = Base64.encodeToString(salt, Base64.NO_WRAP)
                )
            )
            AuthResult.Success
        }.getOrElse { error ->
            if (error is CancellationException) throw error
            if (error is SQLiteConstraintException) AuthResult.UsernameTaken
            else AuthResult.StorageError
        }
    }

    suspend fun login(username: String, password: String): AuthResult {
        val normalizedUsername = normalizeUsername(username)
        val account = runCatching { userAccountDao.findByUsername(normalizedUsername) }
            .getOrElse {
                if (it is CancellationException) throw it
                return AuthResult.StorageError
            }
            ?: return AuthResult.InvalidCredentials

        val salt = runCatching { Base64.decode(account.passwordSalt, Base64.NO_WRAP) }
            .getOrElse { return AuthResult.StorageError }
        val actual = runCatching {
            withContext(Dispatchers.Default) { derivePasswordHash(password, salt) }
        }
            .getOrElse {
                if (it is CancellationException) throw it
                return AuthResult.StorageError
            }
        return if (MessageDigest.isEqual(actual.toByteArray(), account.passwordHash.toByteArray())) {
            AuthResult.Success
        } else {
            AuthResult.InvalidCredentials
        }
    }

    private fun derivePasswordHash(password: String, salt: ByteArray): String {
        val passwordChars = password.toCharArray()
        val spec = javax.crypto.spec.PBEKeySpec(passwordChars, salt, ITERATIONS, HASH_BITS)
        return try {
            Base64.encodeToString(
                javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).encoded,
                Base64.NO_WRAP
            )
        } finally {
            spec.clearPassword()
            passwordChars.fill('\u0000')
        }
    }

    private fun normalizeUsername(username: String): String =
        username.trim().lowercase(Locale.ROOT)

    private companion object {
        const val SALT_SIZE = 16
        const val ITERATIONS = 120_000
        const val HASH_BITS = 256
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/data/auth/AuthValidation.kt`

SHA-256: `be3dbaca2bc24c9a015b4584b15d6aabbce7ec80f437062d087d90fdce23d40d`

```kotlin
package com.example.packguide.data.auth

object AuthValidation {
    fun usernameError(username: String): String? = when {
        username.trim().length < MIN_USERNAME_LENGTH -> "El usuario debe tener al menos 3 caracteres."
        username.trim().length > MAX_USERNAME_LENGTH -> "El usuario no puede superar 24 caracteres."
        else -> null
    }

    fun passwordError(password: String): String? = when {
        password.isEmpty() -> "La contraseña no puede estar vacía."
        password.length > MAX_PASSWORD_LENGTH -> "La contraseña no puede superar 45 caracteres."
        !password.all(Char::isLetterOrDigit) -> "Usa solo letras y números en la contraseña."
        else -> null
    }

    private const val MIN_USERNAME_LENGTH = 3
    private const val MAX_USERNAME_LENGTH = 24
    private const val MAX_PASSWORD_LENGTH = 45
}
```

### Fuente: `app/src/main/java/com/example/packguide/data/auth/PackGuideDatabase.kt`

SHA-256: `654e716ca9a9c31a6704b3b2a075e91246465d9ac1b9110c41de2b7a83c2fb43`

```kotlin
package com.example.packguide.data.auth

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserAccount::class], version = 1, exportSchema = false)
abstract class PackGuideDatabase : RoomDatabase() {
    abstract fun userAccountDao(): UserAccountDao

    companion object {
        @Volatile
        private var instance: PackGuideDatabase? = null

        fun getInstance(context: Context): PackGuideDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                PackGuideDatabase::class.java,
                "packguide.db"
            ).build().also { instance = it }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/data/auth/UserAccount.kt`

SHA-256: `27ef955ccc48343d6b858630352f7dbf36b326842d10b2f5915db4a8fff0ec17`

```kotlin
package com.example.packguide.data.auth

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts", indices = [Index(value = ["username"], unique = true)])
data class UserAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val passwordSalt: String,
    val createdAtMillis: Long = System.currentTimeMillis()
)
```

### Fuente: `app/src/main/java/com/example/packguide/data/auth/UserAccountDao.kt`

SHA-256: `1c139e58c94939b6a3c86d9e17f8ee2a01563f2365cda295eefbeb9cf3990c82`

```kotlin
package com.example.packguide.data.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserAccountDao {
    @Query("SELECT * FROM user_accounts WHERE username = :username COLLATE NOCASE LIMIT 1")
    suspend fun findByUsername(username: String): UserAccount?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(account: UserAccount): Long
}
```

### Fuente: `app/src/main/java/com/example/packguide/MainActivity.kt`

SHA-256: `b9aac666d3df88ad43cdd69357dcd6187ae22e297c1c9b649a8530fb57294e19`

```kotlin
package com.example.packguide

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.example.packguide.data.auth.AuthRepository
import com.example.packguide.data.auth.PackGuideDatabase
import com.example.packguide.settings.AppSettings
import com.example.packguide.settings.SettingsRepository
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.screens.AuthScreen
import com.example.packguide.ui.screens.MainScreen
import com.example.packguide.ui.theme.PackGuideTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val authRepository by lazy {
        AuthRepository(PackGuideDatabase.getInstance(applicationContext).userAccountDao())
    }
    private val settingsRepository by lazy { SettingsRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                PackGuideRoot(authRepository, settingsRepository)
        }
    }
}

@Composable
private fun PackGuideRoot(authRepository: AuthRepository, settingsRepository: SettingsRepository) {
    var isAuthenticated by remember { mutableStateOf(false) }
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(initialValue = AppSettings())
    val scope = rememberCoroutineScope()

    PackGuideTheme(darkTheme = settings.isDarkTheme) {
        CompositionLocalProvider(LocalAppLanguage provides settings.language) {
            if (isAuthenticated) {
                MainScreen(
                    settings = settings,
                    onThemeChange = { enabled -> scope.launch { settingsRepository.setDarkTheme(enabled) } },
                    onLanguageChange = { language -> scope.launch { settingsRepository.setLanguage(language) } },
                    onLogout = { isAuthenticated = false }
                )
            } else {
                AuthScreen(
                    repository = authRepository,
                    settings = settings,
                    onThemeChange = { enabled -> scope.launch { settingsRepository.setDarkTheme(enabled) } },
                    onLanguageChange = { language -> scope.launch { settingsRepository.setLanguage(language) } },
                    onAuthenticated = { isAuthenticated = true }
                )
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/scanning/PackingRules.kt`

SHA-256: `1418c8625c73fa6a9c3b210d2a49b136575689c597e19f893805449291119e2b`

```kotlin
package com.example.packguide.scanning

import java.math.BigDecimal

data class PackingEstimate(val capacityL: Double, val usedL: Double) {
    val exceedsCapacity: Boolean get() = usedL > capacityL
    val freeL: Double get() = (capacityL - usedL).coerceAtLeast(0.0)
    val excessL: Double get() = (usedL - capacityL).coerceAtLeast(0.0)
    val occupancy: Double get() = if (capacityL > 0) usedL / capacityL else 0.0
    val occupancyPercent: Int get() = (occupancy * 100).toInt()
}

object PackingRules {
    /** Interior rectangular capacity; no hidden allowance or compression factor. */
    fun rectangularVolumeL(height: Int, width: Int, depth: Int): Double {
        require(listOf(height, width, depth).all { it in 1..300 })
        return height.toDouble() * width * depth / 1000.0
    }

    fun totalVolumeL(volumes: List<Pair<Int, Double>>): Double {
        require(volumes.all { (count, volume) -> count >= 0 && volume.isFinite() && volume >= 0 })
        // Sum decimal catalog values exactly; binary floating point must not turn an exact fit
        // into an excess. No display rounding is applied to these calculations.
        return volumes.fold(BigDecimal.ZERO) { total, (count, volume) ->
            total + BigDecimal.valueOf(volume).multiply(BigDecimal.valueOf(count.toLong()))
        }.toDouble()
    }

    fun progressPercent(packed: Int, selected: Int): Int {
        require(selected >= 0 && packed in 0..selected)
        return if (selected == 0) 0 else (packed.toLong() * 100 / selected).toInt()
    }

    fun estimate(height: Int, width: Int, depth: Int, volumes: List<Pair<Int, Double>>): PackingEstimate {
        return PackingEstimate(rectangularVolumeL(height, width, depth), totalVolumeL(volumes))
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/scanning/PhotoScanner.kt`

SHA-256: `351cd647391f926b589349e549fdf73424398d355f3f3b0a5c0ffc73e21f74ac`

```kotlin
package com.example.packguide.scanning

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FilterInputStream
import java.io.InputStream

enum class PhotoProblem { Unsupported, TooLarge }
class PhotoReadException(val problem: PhotoProblem) : Exception()

class PhotoScanner {
    suspend fun decode(context: Context, uri: Uri): Bitmap {
        var decoded: Bitmap? = null
        try {
            return withContext(Dispatchers.IO) {
                val resolver = context.contentResolver
                val mime = resolver.getType(uri)
                if (mime != null && !mime.startsWith("image/")) throw PhotoReadException(PhotoProblem.Unsupported)
                fun open(): InputStream = LimitedPhotoStream(resolver.openInputStream(uri) ?: error("Cannot open photo"))
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                open().use { BitmapFactory.decodeStream(it, null, bounds) }
                if (bounds.outWidth <= 0 || bounds.outHeight <= 0) throw PhotoReadException(PhotoProblem.Unsupported)
                if (bounds.outWidth.toLong() * bounds.outHeight > 100_000_000L) throw PhotoReadException(PhotoProblem.TooLarge)
                var sample = 1
                while (maxOf(bounds.outWidth, bounds.outHeight) / sample > 1600) sample *= 2
                val options = BitmapFactory.Options().apply { inSampleSize = sample }
                val bitmap = open().use { BitmapFactory.decodeStream(it, null, options) }
                    ?: error("Invalid image")
                try {
                    val exif = open().use { ExifInterface(it) }
                    val matrix = Matrix().apply {
                        if (exif?.isFlipped == true) postScale(-1f, 1f)
                        postRotate((exif?.rotationDegrees ?: 0).toFloat())
                    }
                    val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    if (rotated !== bitmap) bitmap.recycle()
                    decoded = rotated
                    rotated
                } catch (error: Throwable) {
                    bitmap.recycle()
                    throw error
                }
            }
        } catch (error: Throwable) {
            // withContext can discard its result when the owning ViewModel is canceled.
            decoded?.recycle()
            throw error
        }
    }

}

/** Applies to content streams too, including providers with no filesystem path or known size. */
private class LimitedPhotoStream(input: InputStream) : FilterInputStream(input) {
    private var consumed = 0L
    private fun count(amount: Long) {
        consumed += amount.coerceAtLeast(0)
        if (consumed > 50L * 1024 * 1024) throw PhotoReadException(PhotoProblem.TooLarge)
    }
    override fun read(): Int = super.read().also { if (it >= 0) count(1) }
    override fun read(buffer: ByteArray, offset: Int, length: Int): Int =
        `in`.read(buffer, offset, length).also { count(it.toLong()) }
    override fun skip(amount: Long): Long = `in`.skip(amount).also(::count)
}
```

### Fuente: `app/src/main/java/com/example/packguide/scanning/ScannerViewModel.kt`

SHA-256: `49d41925862c03d65b8f9647aade8e5d07b43e0faf5ef1d2b44d1d636db1e3ce`

```kotlin
package com.example.packguide.scanning

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import com.example.packguide.ui.screens.LuggagePreset

data class ScanState(
    val loading: Boolean = false,
    val photo: Bitmap? = null,
    val photoUri: Uri? = null,
    val message: String? = null
)

class ScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val states = mutableStateMapOf<LuggagePreset, ScanState>()
    private val jobs = mutableMapOf<LuggagePreset, Job>()
    fun stateFor(type: LuggagePreset): ScanState = states[type] ?: ScanState()
    private val scanner = PhotoScanner()

    fun message(text: String, type: LuggagePreset) { states[type] = stateFor(type).copy(message = text) }
    fun clearPhoto(type: LuggagePreset) {
        jobs.remove(type)?.cancel()
        states.remove(type)
    }
    fun clearPhotos() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
        states.clear()
    }
    fun selectPhoto(uri: Uri, type: LuggagePreset) {
        if (stateFor(type).loading) return
        states[type] = stateFor(type).copy(loading = true, message = null)
        jobs[type] = viewModelScope.launch {
            try {
                val photo = scanner.decode(getApplication(), uri)
                states[type] = ScanState(photo = photo, photoUri = uri)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: OutOfMemoryError) {
                states[type] = stateFor(type).copy(loading = false, message = "La foto es demasiado grande. Elige una imagen más pequeña.")
            } catch (error: Exception) {
                val message = when ((error as? PhotoReadException)?.problem) {
                    PhotoProblem.Unsupported -> "Ese archivo no es una foto compatible. Elige una imagen JPG, PNG o WebP."
                    PhotoProblem.TooLarge -> "La foto es demasiado grande. Elige una imagen más pequeña."
                    null -> "No se pudo abrir la foto. Elige otra imagen de la maleta."
                }
                states[type] = stateFor(type).copy(loading = false, message = message)
            } finally {
                if (jobs[type] == coroutineContext[Job]) jobs.remove(type)
            }
        }
    }

    fun selectPhoto(bitmap: Bitmap, type: LuggagePreset) {
        if (!stateFor(type).loading) states[type] = ScanState(photo = bitmap)
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/settings/AppSettings.kt`

SHA-256: `bf0062eb0edb9c13137b6b209d6ace368f5d0da135a08f7c80f8bdcf65151310`

```kotlin
package com.example.packguide.settings

enum class AppLanguage(val storageKey: String) {
    Spanish("es"),
    English("en");

    companion object {
        fun fromStorage(value: String?): AppLanguage =
            entries.firstOrNull { it.storageKey == value } ?: Spanish
    }
}

data class AppSettings(
    val isDarkTheme: Boolean = false,
    val language: AppLanguage = AppLanguage.Spanish
)
```

### Fuente: `app/src/main/java/com/example/packguide/settings/SettingsRepository.kt`

SHA-256: `045d37a92e9beca4f38e5c2e157a514fcd1393f309b05a95cc1bf17bbced5bf8`

```kotlin
package com.example.packguide.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.distinctUntilChanged

private val Context.packGuideSettingsStore: DataStore<Preferences> by preferencesDataStore(
    name = "packguide_settings"
)

class SettingsRepository(context: Context) {
    private val settingsStore = context.applicationContext.packGuideSettingsStore

    val settings: Flow<AppSettings> = settingsStore.data.map { preferences ->
        AppSettings(
            isDarkTheme = preferences[DARK_THEME_KEY] ?: false,
            language = AppLanguage.fromStorage(preferences[LANGUAGE_KEY])
        )
    }.distinctUntilChanged()

    suspend fun setDarkTheme(enabled: Boolean) {
        settingsStore.edit { it[DARK_THEME_KEY] = enabled }
    }

    suspend fun setLanguage(language: AppLanguage) {
        settingsStore.edit { it[LANGUAGE_KEY] = language.storageKey }
    }

    private companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val LANGUAGE_KEY = stringPreferencesKey("language")
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/AsciiButton.kt`

SHA-256: `9bfdaddd7069a5a6a130097cf62de98112ebfc946f55c0858ee497ca359f5af6`

```kotlin
package com.example.packguide.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun AsciiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    isAccentRed: Boolean = false,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val containerColor = when {
        !enabled -> PackGuideColors.surfaceVariant
        isAccentRed -> AccentRedLed
        isPrimary -> PackGuideColors.primaryText
        else -> PackGuideColors.surface
    }
    val contentColor = when {
        !enabled -> PackGuideColors.secondaryText
        isAccentRed -> Color.White
        isPrimary -> PackGuideColors.surface
        else -> PackGuideColors.primaryText
    }
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.985f else 1f, tween(120), label = "ButtonPress")
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier.graphicsLayer { scaleX = scale; scaleY = scale }
            .heightIn(min = 48.dp).clip(shape).background(containerColor)
            .border(1.dp, if (isPrimary && enabled) containerColor else PackGuideColors.border, shape)
            .clickable(enabled = enabled, role = Role.Button, interactionSource = interaction,
                indication = null, onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (icon != null) Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
            Text(text, style = Typography.labelLarge, color = contentColor)
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/DashedDivider.kt`

SHA-256: `3f20873653bfb7b0895a469c82af84e4d522f228738f9dfdb1ef3f97009406ca`

```kotlin
package com.example.packguide.ui.components


import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 1.dp,
    gapLength: Dp = 5.dp
) {
    val resolvedColor = if (color == Color.Unspecified) {
        PackGuideColors.border
    } else color
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(strokeWidth)
    ) {
        val dashPx = dashLength.toPx()
        val gapPx = gapLength.toPx()
        val strokePx = strokeWidth.toPx()

        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f)

        drawLine(
            color = resolvedColor,
            start = androidx.compose.ui.geometry.Offset(0f, strokePx / 2f),
            end = androidx.compose.ui.geometry.Offset(size.width, strokePx / 2f),
            strokeWidth = strokePx,
            pathEffect = pathEffect
        )
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/DotMatrixMark.kt`

SHA-256: `b0859efe685a2970bc0dbff9d03d1a6d587c19e09ca71ec9f07e54ad98bff2eb`

```kotlin
package com.example.packguide.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors

@Composable
fun DotMatrixMark(modifier: Modifier = Modifier) {
    val neutral = PackGuideColors.primaryText.copy(alpha = 0.18f)
    Canvas(modifier.size(32.dp)) {
        val step = size.width / 6
        repeat(6) { row ->
            repeat(6) { column ->
                drawCircle(
                    color = if (column == 5 && row >= 3) AccentRedLed else neutral,
                    radius = step * 0.16f,
                    center = Offset(step * (column + 0.5f), step * (row + 0.5f))
                )
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/HudHeader.kt`

SHA-256: `77bbba9678b8dc601a7975b572b926499b43cb56aa8345538f9f9520c3b9f815`

```kotlin
package com.example.packguide.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun HudHeader(
    title: String,
    modifier: Modifier = Modifier,
    sysCode: String = "SYS // PACKING_OS",
    statusText: String = "[ ONLINE ]",
    ledColor: Color = AccentGreenLed,
    horizontalPadding: Dp = 20.dp
) {
    Column(modifier.fillMaxWidth().padding(horizontal = horizontalPadding, vertical = 18.dp)) {
        if (sysCode.isNotBlank() || statusText.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (sysCode.isNotBlank()) Text(sysCode, style = Typography.labelSmall,
                    color = PackGuideColors.secondaryText, modifier = Modifier.weight(1f))
                else Spacer(Modifier.weight(1f))
                if (statusText.isNotBlank()) LedIndicator(color = ledColor, size = 5.dp,
                    label = statusText, isPulsing = false)
            }
            Spacer(Modifier.height(10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = Typography.headlineMedium, color = PackGuideColors.primaryText,
                modifier = Modifier.weight(1f))
            DotMatrixMark()
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/LedIndicator.kt`

SHA-256: `1a01bb043c71b6022d3a1d513095dc1c5bf4ab81034e5b8271141b705517a361`

```kotlin
package com.example.packguide.ui.components


import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun LedIndicator(
    modifier: Modifier = Modifier,
    color: Color = AccentGreenLed,
    size: Dp = 8.dp,
    label: String? = null,
    isPulsing: Boolean = false
) {
    val alphaAnim by if (isPulsing) {
        val transition = rememberInfiniteTransition(label = "LedPulse")
        transition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "LedPulseAlpha"
        )
    } else {
        androidx.compose.runtime.rememberUpdatedState(1.0f)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(size * 1.8f)) {
            val centerPt = center
            val radiusPx = size.toPx() / 2f

            // Glow outer halo
            drawCircle(
                color = color.copy(alpha = 0.12f * alphaAnim),
                radius = radiusPx * 1.4f,
                center = centerPt
            )
            // Core LED circle
            drawCircle(
                color = color.copy(alpha = alphaAnim),
                radius = radiusPx,
                center = centerPt
            )
        }

        if (label != null) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = Typography.labelSmall,
                fontFamily = TechMonospaceFontFamily,
                color = PackGuideColors.secondaryText
            )
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/SettingsDialog.kt`

SHA-256: `aae56e510fb0cb2be09946fe171c7094b92e2347ce185a4b853939b8507ccb67`

```kotlin
package com.example.packguide.ui.components

import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.packguide.settings.AppLanguage
import com.example.packguide.ui.localization.Text as LocalizedText
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.Typography

@Composable
fun SettingsDialog(
    language: AppLanguage,
    isDarkTheme: Boolean,
    showLogout: Boolean,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().background(PackGuideColors.surface, RoundedCornerShape(22.dp))
                .border(1.dp, PackGuideColors.border, RoundedCornerShape(22.dp)).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LocalizedText("[ AJUSTES DE SISTEMA ]", style = Typography.titleMedium, color = PackGuideColors.primaryText)
            LocalizedText("PERSONALIZA PACKGUIDE", style = Typography.labelSmall, color = PackGuideColors.secondaryText)
            Spacer(Modifier.height(4.dp))
            SettingsRow(
                icon = if (isDarkTheme) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                title = "TEMA",
                value = if (isDarkTheme) "OSCURO" else "CLARO",
                onClick = onToggleTheme
            )
            SettingsRow(
                icon = Icons.Outlined.Language,
                title = "IDIOMA",
                value = if (language == AppLanguage.Spanish) "ESPAÑOL" else "ENGLISH",
                onClick = onToggleLanguage
            )
            if (showLogout) {
                SettingsRow(
                    icon = Icons.Outlined.Logout,
                    title = "CERRAR SESIÓN",
                    value = "SALIR DE LA CUENTA",
                    accent = AccentRedLed,
                    onClick = onLogout
                )
            }
            TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                LocalizedText("[ CERRAR ]", color = PackGuideColors.primaryText, style = Typography.labelMedium)
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    value: String,
    accent: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    val resolvedAccent = if (accent == Color.Unspecified) PackGuideColors.primaryText else accent
    Row(
        modifier = Modifier.fillMaxWidth().background(PackGuideColors.background, RoundedCornerShape(2.dp))
            .border(1.dp, PackGuideColors.border, RoundedCornerShape(2.dp)).clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = resolvedAccent)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            LocalizedText(title, style = Typography.labelMedium, color = resolvedAccent)
            Spacer(Modifier.height(3.dp))
            LocalizedText(value, style = Typography.labelSmall, color = PackGuideColors.secondaryText)
        }
        LocalizedText("›", style = Typography.titleMedium, color = resolvedAccent)
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/TechBadge.kt`

SHA-256: `9e5b38ae461d5547a20c5aae87f315b7dcacccfeebe41f1f11eab894e0b7d803`

```kotlin
package com.example.packguide.ui.components


import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun TechBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    ledColor: Color? = null
) {
    val resolvedContainerColor = if (containerColor == Color.Unspecified) PackGuideColors.primaryText else containerColor
    val resolvedContentColor = if (contentColor == Color.Unspecified) PackGuideColors.surface else contentColor
    val resolvedBorderColor = if (borderColor == Color.Unspecified) Color.Transparent else borderColor
    Box(
        modifier = modifier
            .background(resolvedContainerColor, shape = RoundedCornerShape(8.dp))
            .border(1.dp, resolvedBorderColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (ledColor != null) {
                LedIndicator(color = ledColor, size = 6.dp, isPulsing = false)
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                style = Typography.labelSmall.copy(fontSize = 11.sp),
                fontFamily = TechMonospaceFontFamily,
                color = resolvedContentColor
            )
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/components/TechCard.kt`

SHA-256: `05ce63df12b23359e57579f637231e99e81956f78c0c94b0cd37c62d85f40b57`

```kotlin
package com.example.packguide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun TechCard(
    modifier: Modifier = Modifier,
    headerTag: String? = null,
    subTag: String? = null,
    borderColor: Color = Color.Unspecified,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val stroke = when (borderColor) {
        Color.Unspecified -> PackGuideColors.border
        PackGuideColors.primaryText -> PackGuideColors.primaryText.copy(alpha = 0.22f)
        else -> borderColor
    }
    Column(
        modifier.fillMaxWidth().clip(shape)
            .background(PackGuideColors.surface)
            .border(1.dp, stroke, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp)
    ) {
        if (headerTag != null || subTag != null) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.width(3.dp).height(10.dp).background(AccentRedLed, RoundedCornerShape(2.dp)))
                if (headerTag != null) Text(headerTag, style = Typography.labelSmall,
                    color = PackGuideColors.secondaryText, modifier = Modifier.weight(1f))
                else Spacer(Modifier.weight(1f))
                if (subTag != null) Text(subTag, style = Typography.labelSmall, color = PackGuideColors.secondaryText)
            }
            Spacer(Modifier.height(14.dp))
        }
        content()
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/localization/AppText.kt`

SHA-256: `5cf3018d49f6f9a98f19fbc2684ad4c9e10449fb79aca46c670aacb352263ffc`

```kotlin
package com.example.packguide.ui.localization

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text as MaterialText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.packguide.settings.AppLanguage

val LocalAppLanguage = compositionLocalOf { AppLanguage.Spanish }

private val englishTranslations = mapOf(
    "Ese archivo no es una foto compatible. Elige una imagen JPG, PNG o WebP." to "This file is not a supported photo. Choose a JPG, PNG or WebP image.",
    "La foto es demasiado grande. Elige una imagen más pequeña." to "The photo is too large. Choose a smaller image.",
    "ROPA INTERIOR" to "UNDERWEAR",
    "MEDIAS" to "SOCKS",
    "PIJAMA" to "PAJAMAS",
    "VESTIDO" to "DRESS",
    "FALDA" to "SKIRT",
    "SHORTS" to "SHORTS",
    "SUÉTER" to "SWEATER",
    "CAMISA" to "SHIRT",
    "TRAJE DE BAÑO" to "SWIMWEAR",
    "ZAPATOS FORMALES" to "DRESS SHOES",
    "SANDALIAS" to "SANDALS",
    "PANTUFLAS" to "SLIPPERS",
    "CEPILLO DE DIENTES" to "TOOTHBRUSH",
    "PASTA DENTAL" to "TOOTHPASTE",
    "DESODORANTE" to "DEODORANT",
    "CHAMPÚ" to "SHAMPOO",
    "JABÓN" to "SOAP",
    "PROTECTOR SOLAR" to "SUNSCREEN",
    "MEDICAMENTOS" to "MEDICATION",
    "LENTES DE CONTACTO" to "CONTACT LENSES",
    "TELÉFONO" to "PHONE",
    "TABLETA" to "TABLET",
    "PORTÁTIL" to "LAPTOP",
    "AURICULARES" to "HEADPHONES",
    "BATERÍA EXTERNA" to "POWER BANK",
    "ADAPTADOR DE ENCHUFE" to "PLUG ADAPTER",
    "CÁMARA" to "CAMERA",
    "TARJETAS DE MEMORIA" to "MEMORY CARDS",
    "CABLE USB" to "USB CABLE",
    "RELOJ" to "WATCH",
    "CAPACIDAD INTERIOR TOTAL" to "TOTAL INTERIOR CAPACITY",
    "Volumen estimado / capacidad interior total" to "Estimated volume / total interior capacity",
    "Medidas de referencia; litros por unidad estimados de forma independiente. No hay un factor de compresión medido." to "Reference dimensions; liters per unit are independent estimates. No measured compression factor is available.",
    "Capacidad interior = alto × ancho × fondo / 1000. Sin descuento adicional. Volumen estimado = suma de litros por unidad × cantidad seleccionada." to "Interior capacity = height × width × depth / 1000. No additional reduction. Estimated volume = sum of liters per unit × selected quantity.",
    "EQUIPAJE DEL VIAJE" to "TRIP LUGGAGE",
    "Selecciona todas las maletas que llevarás. Debe quedar al menos una." to "Select all the bags you will take. Keep at least one selected.",
    "MALETA A ORGANIZAR" to "BAG TO PACK",
    "DESTINO DEL ARTÍCULO" to "ITEM DESTINATION",
    "DESTINOS DEL VIAJE" to "TRIP BAGS",
    "TODAS LAS MALETAS" to "ALL BAGS",
    "Reparte las cantidades de cada artículo entre las maletas elegidas en Escáner." to "Split item quantities between the bags selected in Scanner.",
    "En Catálogo podrás repartir los artículos entre las maletas seleccionadas." to "In Catalog you can split items between the selected bags.",
    "CONTINUAR AL CATÁLOGO" to "CONTINUE TO CATALOG",
    "QUITAR MALETA" to "REMOVE BAG",
    "CANCELAR" to "CANCEL",
    "Se quitarán del plan los artículos asignados a esta maleta. Las demás maletas conservarán sus artículos." to "Items assigned to this bag will be removed from the plan. Other bags will keep their items.",
    "VER CRITERIOS Y CONSEJOS" to "VIEW RULES AND TIPS",
    "OCULTAR DETALLES" to "HIDE DETAILS",
    "SUBTOTAL" to "SUBTOTAL",
    "Disminuir cantidad" to "Decrease quantity",
    "Aumentar cantidad" to "Increase quantity",
    "FOTO DE LA MALETA" to "SUITCASE PHOTO",
    "Toma o elige una foto de tu maleta." to "Take or choose a photo of your suitcase.",
    "Foto de tu maleta" to "Photo of your suitcase",
    "Cámara no disponible. Usa Elegir foto." to "Camera unavailable. Use Choose photo.",
    "Acceso a la cámara denegado. Usa Elegir foto." to "Camera access denied. Use Choose photo.",
    "Selector de fotos no disponible. Usa Tomar foto." to "Photo picker unavailable. Use Take photo.",
    "Acceso a la foto denegado. Intenta elegirla de nuevo." to "Photo access denied. Try choosing it again.",
    "No se pudo abrir la foto. Elige otra imagen de la maleta." to "Could not open the photo. Choose another picture of your suitcase.",
    "La foto es una referencia. El espacio se calcula con las medidas de la maleta y los volúmenes estándar del catálogo." to "The photo is a reference. Space is calculated from suitcase dimensions and standard catalog volumes.",
    "Capacidad calculada con medidas interiores. Verifica las medidas exteriores y las normas de la aerolínea por separado." to "Capacity calculated from interior dimensions. Check exterior dimensions and airline rules separately.",
    "CAMISA" to "SHIRT",
    "PANTALONES" to "PANTS",
    "VESTIDO" to "DRESS",
    "CHAQUETA" to "JACKET",
    "CALZADO" to "SHOES",
    "ALTO:" to "HEIGHT:",
    "ANCHO:" to "WIDTH:",
    "PROF:" to "DEPTH:",
    "MEDIDAS INTERIORES ÚTILES" to "USABLE INTERIOR DIMENSIONS",
    "Referencias de tamaño; ajusta las medidas interiores reales. Las medidas exteriores y las normas de la aerolínea se verifican por separado." to "Size presets; adjust actual interior dimensions. Check exterior dimensions and airline rules separately.",
    "CAPACIDAD ORIENTATIVA:" to "APPROXIMATE CAPACITY:",
    "AJUSTA LAS MEDIDAS DE TU EQUIPAJE" to "ADJUST YOUR LUGGAGE DIMENSIONS",
    "TIPO DE EQUIPAJE" to "LUGGAGE TYPE",
    "CONTINUAR A GUÍA DE EMPACADO" to "CONTINUE TO PACKING GUIDE",
    "Los cambios se reflejan al instante en catálogo y resumen." to "Changes appear immediately in the catalog and summary.",
    "ELEGIR FOTO" to "CHOOSE PHOTO",
    "TOMAR FOTO" to "TAKE PHOTO",
    "ACCESORIOS" to "ACCESSORIES",
    "EQUIPAJE" to "LUGGAGE",
    "CONFIRMO LAS MEDIDAS" to "I CONFIRM THE DIMENSIONS",
    "Una foto común no mide la maleta. Ajusta las medidas interiores útiles y confírmalas." to "An ordinary photo does not measure a suitcase. Adjust and confirm usable interior dimensions.",
    "SUGERENCIA ORIENTATIVA" to "APPROXIMATE PACKING SUGGESTION",
    "Confirma las medidas en Escáner antes de usar esta estimación." to "Confirm dimensions in Scanner before using this estimate.",
    "Regla: alto × ancho × fondo / 1000; reserva del 20% para forro, curvas y huecos. Usa medidas interiores útiles." to "Rule: height × width × depth / 1000; reserve 20% for lining, curves and gaps. Use usable interior dimensions.",
    "Estimación de artículos / espacio orientativo:" to "Estimated items / approximate space:",
    "Reduce cantidades o usa una maleta mayor: el volumen estimado supera el espacio orientativo." to "Reduce quantities or use a larger suitcase: estimated volume exceeds the approximate space.",
    "El volumen estimado está dentro del margen orientativo; comprueba el ajuste al empacar." to "Estimated volume is within the approximate allowance; check fit while packing.",
    "Los litros por artículo son supuestos del catálogo, no medidas de la foto. No es una simulación física ni garantiza que todo quepa." to "Liters per item are catalog assumptions, not photo measurements. This is not a physical simulation or a guarantee of fit.",
    "Calzado en bolsas junto a las ruedas; pantalones y chaquetas en la base. Enrolla camisas resistentes en el centro; vestidos delicados arriba. Accesorios en un bolsillo accesible." to "Bag shoes near the wheels; place pants and jackets at the base. Roll sturdy shirts in the center; delicate dresses go on top. Keep accessories in an accessible pocket.",
    "[ AJUSTES DE SISTEMA ]" to "[ SYSTEM SETTINGS ]",
    "[AJUSTES]" to "[SETTINGS]",
    "PERSONALIZA PACKGUIDE" to "CUSTOMIZE PACKGUIDE",
    "PACKGUIDE // ACCESO" to "PACKGUIDE // ACCESS",
    "TEMA" to "THEME",
    "CLARO" to "LIGHT",
    "OSCURO" to "DARK",
    "IDIOMA" to "LANGUAGE",
    "ESPAÑOL" to "SPANISH",
    "CERRAR SESIÓN" to "SIGN OUT",
    "SALIR DE LA CUENTA" to "LOG OUT OF THIS ACCOUNT",
    "[ CERRAR ]" to "[ CLOSE ]",
    "[ ENTRAR ]" to "[ SIGN IN ]",
    "[ CREAR CUENTA ]" to "[ CREATE ACCOUNT ]",
    "[ REGISTRAR ]" to "[ REGISTER ]",
    "[ YA TENGO CUENTA ]" to "[ I HAVE AN ACCOUNT ]",
    "[ CREDENCIALES ]" to "[ CREDENTIALS ]",
    "[ VALIDANDO... ]" to "[ CHECKING... ]",
    "[ CREANDO CUENTA... ]" to "[ CREATING ACCOUNT... ]",
    "INICIAR SESIÓN" to "SIGN IN",
    "INICIAR SESIÃ“N" to "SIGN IN",
    "CREAR CUENTA" to "CREATE ACCOUNT",
    "USUARIO" to "USERNAME",
    "CONTRASEÑA" to "PASSWORD",
    "CONTRASEÃ‘A" to "PASSWORD",
    "CONFIRMAR CONTRASEÑA" to "CONFIRM PASSWORD",
    "CONFIRMAR CONTRASEÃ‘A" to "CONFIRM PASSWORD",
    "VER" to "SHOW",
    "OCULTAR" to "HIDE",
    "CUENTA ALMACENADA EN ESTE DISPOSITIVO" to "ACCOUNT STORED ON THIS DEVICE",
    "Usuario o contraseña incorrectos." to "Incorrect username or password.",
    "Ese usuario ya existe. Inicia sesión con esa cuenta." to "That username already exists. Sign in with it.",
    "Ese usuario ya está registrado." to "That username is already registered.",
    "Las contraseñas no coinciden." to "Passwords do not match.",
    "Revisa el usuario y la contraseña e intenta de nuevo." to "Check your username and password, then try again.",
    "No se pudo acceder a la base local. Intenta de nuevo." to "Could not access local storage. Try again.",
    "El usuario debe tener al menos 3 caracteres." to "Username must have at least 3 characters.",
    "El usuario no puede superar 24 caracteres." to "Username cannot exceed 24 characters.",
    "Usa letras, números, punto, guion o guion bajo." to "Use letters, numbers, periods, hyphens, or underscores.",
    "Recomendado: letras, números, punto, guion o guion bajo." to "Recommended: letters, numbers, periods, hyphens, or underscores.",
    "La contraseña debe tener al menos 8 caracteres." to "Password must have at least 8 characters.",
    "La contraseña no puede estar vacía." to "Password cannot be empty.",
    "La contraseña no puede superar 45 caracteres." to "Password cannot exceed 45 characters.",
    "Usa solo letras y números en la contraseña." to "Use only letters and numbers in the password.",
    "Ingresa tu contraseña." to "Enter your password.",
    "1 a 45 caracteres: solo letras y números." to "1 to 45 characters: letters and numbers only.",
    "PACKGUIDE // ACCESS" to "PACKGUIDE // ACCESS",
    "SYS // IDENTIDAD LOCAL" to "SYS // LOCAL IDENTITY",
    "[ SINCRONIZACIÓN OFF ]" to "[ SYNC OFF ]",
    "[ SINCRONIZACIÃ“N OFF ]" to "[ SYNC OFF ]",
    "VERIFICANDO CREDENCIALES..." to "VERIFYING CREDENTIALS...",
    "SYS // PACKING_OS" to "SYS // PACKING_OS",
    "SYS // FINAL_DIAGNOSTIC" to "SYS // FINAL_DIAGNOSTIC",
    "RESUMEN FINAL" to "FINAL SUMMARY",
    "RESUMEN" to "SUMMARY",
    "[RESUMEN]" to "[SUMMARY]",
    "[ RESUMEN ]" to "[ SUMMARY ]",
    "[ESCÃNER]" to "[SCANNER]",
    "[ ESCÃNER ]" to "[ SCANNER ]",
    "[CATÃLOGO]" to "[CATALOG]",
    "[ CATÃLOGO ]" to "[ CATALOG ]",
    "[GUÃA]" to "[GUIDE]",
    "[ GUÃA ]" to "[ GUIDE ]",
    "CATÃLOGO DE ÃTEMS" to "ITEM CATALOG",
    "BUSCAR ÍTEM... [ _ ]" to "SEARCH ITEM... [ _ ]",
    "BUSCAR ÃTEM... [ _ ]" to "SEARCH ITEM... [ _ ]",
    "Buscar" to "Search",
    "TODOS" to "ALL",
    "ROPA" to "CLOTHING",
    "CALZADO" to "FOOTWEAR",
    "ASEO" to "TOILETRIES",
    "NECESER" to "TOILETRY KIT",
    "CAMISETAS" to "T-SHIRTS",
    "PANTALONES" to "TROUSERS",
    "CHAQUETA" to "JACKET",
    "CARGADORES & TECH" to "CHARGERS & TECH",
    "CAPACIDAD" to "CAPACITY",
    "DISTRIBUCIÓN" to "DISTRIBUTION",
    "DISTRIBUCIÃ“N" to "DISTRIBUTION",
    "EFICIENCIA & MÉTRICAS DE EQUIPAJE" to "LUGGAGE EFFICIENCY & METRICS",
    "EFICIENCIA & MÃ‰TRICAS DE EQUIPAJE" to "LUGGAGE EFFICIENCY & METRICS",
    "EFICIENCIA DE VOLUMEN: 92%" to "VOLUME EFFICIENCY: 92%",
    "PESO TOTAL: 9.8 KG" to "TOTAL WEIGHT: 9.8 KG",
    "ESTADO: ÓPTIMO CABINA" to "STATUS: CABIN OPTIMAL",
    "ESTADO: Ã“PTIMO CABINA" to "STATUS: CABIN OPTIMAL",
    "ESPACIO LIBRE: 8%" to "FREE SPACE: 8%",
    "+3.0L VOL RESIDUAL" to "+3.0L REMAINING VOLUME",
    "38L VOLUMEN INTERNO" to "38L INTERNAL VOLUME",
    "[• PESADOS]" to "[• HEAVY]",
    "[• ENROLLADOS]" to "[• ROLLED]",
    "[• ACCESO RÁPIDO]" to "[• QUICK ACCESS]",
    "[â€¢ ACCESO RÃPIDO]" to "[â€¢ QUICK ACCESS]",
    "[ACCESO RÁPIDO]" to "[QUICK ACCESS]",
    "[ACCESO RÃPIDO]" to "[QUICK ACCESS]",
    "[ENROLLADO]" to "[ROLLED]",
    "[PESADO]" to "[HEAVY]",
    "ENROLLADO Y BLOQUEO" to "ROLL AND LOCK",
    "PLIEGUE AL CENTRO" to "FOLD TO CENTER",
    "BASE Y DOBLADILLO" to "BASE AND HEM",
    "BASE INF" to "LOWER BASE",
    "VOLUMEN CALCULADO:" to "CALCULATED VOLUME:",
    "VOLUMEN USADO:" to "VOLUME USED:",
    "CAPACIDAD DISPONIBLE:" to "AVAILABLE CAPACITY:",
    "SUBTOTAL:" to "SUBTOTAL:",
    "ANCHO" to "WIDTH",
    "ALTO" to "HEIGHT",
    "PROF." to "DEPTH",
    "AJUSTE MANUAL DE MEDIDAS" to "MANUAL DIMENSION ADJUSTMENT",
    "[ APLICAR MEDIDAS ]" to "[ APPLY DIMENSIONS ]",
    "[ CANCELAR ]" to "[ CANCEL ]",
    "AUTO" to "AUTO",
    "MANUAL" to "MANUAL",
    "ÓPTIMA" to "OPTIMAL",
    "Ã“PTIMA" to "OPTIMAL",
    "[AUTO]" to "[AUTO]",
    "[MANUAL]" to "[MANUAL]",
    "NOTHING // VALIJA AR - PASO 3: DOBLAR CAMISETAS" to "NOTHING // SUITCASE AR - STEP 3: FOLD T-SHIRTS",
    "ASIGNACIÓN EN VALIJA - GRID 2D // CARGA" to "SUITCASE ASSIGNMENT - 2D GRID // LOAD",
    "ASIGNACIÃ“N EN VALIJA - GRID 2D // CARGA" to "SUITCASE ASSIGNMENT - 2D GRID // LOAD",
    "[ SYS ONLINE ]" to "[ SYS ONLINE ]",
    "PESADO" to "HEAVY",
    "[ VISTA LATERAL ]" to "[ SIDE VIEW ]",
    "[ VISTA SUPERIOR // CAPA B ]" to "[ TOP VIEW // LAYER B ]",
    "OPTIMIZAR - PULSA PARA GENERAR DISTRIBUCIÓN" to "OPTIMIZE - TAP TO GENERATE A LAYOUT",
    "OPTIMIZAR - PULSA PARA GENERAR DISTRIBUCIÃ“N" to "OPTIMIZE - TAP TO GENERATE A LAYOUT",
    "[ REORGANIZAR EQUIPAJE ]" to "[ REORGANIZE LUGGAGE ]",
    "[ GUARDAR CONFIGURACIÓN VIAJE ]" to "[ SAVE TRIP SETTINGS ]",
    "[ GUARDAR CONFIGURACIÃ“N VIAJE ]" to "[ SAVE TRIP SETTINGS ]",
    "CONFIGURACIÓN GUARDADA // TRIP-ID: #NT-38-04" to "SETTINGS SAVED // TRIP-ID: #NT-38-04",
    "CONFIGURACIÃ“N GUARDADA // TRIP-ID: #NT-38-04" to "SETTINGS SAVED // TRIP-ID: #NT-38-04",
    "CREAR PLAN DE EMPACADO" to "CREATE PACKING PLAN",
    "CREAR PLAN DE EMPACADO ->" to "CREATE PACKING PLAN ->",
    "[ MARCAR EMPACADO (\$packedCount/6) ]" to "[ MARK PACKED (\$packedCount/6) ]",
    "Paso 03 / 05" to "Step 03 / 05",
    "FASE 01" to "PHASE 01",
    "FASE 02" to "PHASE 02",
    "FASE 03" to "PHASE 03",
    "FASE " to "PHASE ",
    "SIGUIENTE ->" to "NEXT ->",
    "<- ANTERIOR" to "<- PREVIOUS",
    "Extiende la prenda plana y pliega el borde inferior (8 cm)." to "Lay the garment flat and fold the bottom edge (8 cm).",
    "Dobla ambos laterales y mangas hacia el eje central." to "Fold both sides and sleeves toward the center line.",
    "Enrolla con tensión máxima desde el cuello." to "Roll tightly from the collar.",
    "MÉTODO: RANGER ROLL" to "METHOD: RANGER ROLL",
    "MÃ‰TODO: RANGER ROLL" to "METHOD: RANGER ROLL",
    "ESTABILIDAD" to "STABILITY",
    "EXCEDE CABINA" to "EXCEEDS CABIN LIMIT",
    "CABINA IATA OK" to "IATA CABIN OK",
    "IATA VALID" to "IATA VALID",
    "TARGET_LOCK: CARRY_ON" to "TARGET_LOCK: CARRY_ON",
    "ESPACIO MAPEO: 100% // LIDAR ACTIVE | FPS: 60" to "MAPPING SPACE: 100% // LIDAR ACTIVE | FPS: 60",
    "ANCHO: \$anchoCm CM" to "WIDTH: \$anchoCm CM",
    "ALTO: \$altoCm CM" to "HEIGHT: \$altoCm CM",
    "PROF: \$profCm CM" to "DEPTH: \$profCm CM",
    "Volver" to "Back",
    "Cambiar Tema" to "Change theme"
)

fun localizedText(text: String, language: AppLanguage): String =
    if (language == AppLanguage.English) englishTranslations[text] ?: translateCommonTerms(text) else text

private val commonTermTranslations = run {
    val words = mapOf(
        "Configuración" to "Settings", "ConfiguraciÃ³n" to "Settings",
        "Empacado" to "Packed", "Pendiente" to "Pending", "Faltante" to "Missing",
        "Peso" to "Weight", "Volumen" to "Volume", "Disponible" to "Available",
        "Empacar" to "Pack", "Artículo" to "Item", "Ítem" to "Item", "Item" to "Item",
        "Categoría" to "Category", "Estado" to "Status", "Medidas" to "Dimensions",
        "Continuar" to "Continue", "Cancelar" to "Cancel", "Guardar" to "Save",
        "Eliminar" to "Delete", "Editar" to "Edit", "Añadir" to "Add",
        "Buscar" to "Search", "Resumen" to "Summary", "Guía" to "Guide",
        "Escáner" to "Scanner", "Catálogo" to "Catalog", "Equipaje" to "Luggage",
        "Viaje" to "Trip", "Ropa" to "Clothing", "Accesorios" to "Accessories",
        "Electrónica" to "Electronics", "Sí" to "Yes", "No" to "No"
    )
    words.entries.sortedByDescending { it.key.length }.map { entry ->
        Regex(Regex.escape(entry.key), RegexOption.IGNORE_CASE) to entry.value
    }
}

private fun translateCommonTerms(text: String): String =
    commonTermTranslations.fold(text) { result, (pattern, translation) ->
        pattern.replace(result, translation)
    }

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val language = LocalAppLanguage.current
    val translatedText = remember(text, language) { localizedText(text, language) }
    MaterialText(
        text = translatedText, modifier = modifier, color = color,
        fontSize = fontSize, fontStyle = fontStyle, fontWeight = fontWeight,
        fontFamily = fontFamily, letterSpacing = letterSpacing, textDecoration = textDecoration,
        textAlign = textAlign, lineHeight = lineHeight, overflow = overflow, softWrap = softWrap,
        maxLines = maxLines, minLines = minLines, onTextLayout = onTextLayout, style = style
    )
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/localization/PackingText.kt`

SHA-256: `77b687afba99f06a2f1014bc79b24758aa56077e651ad1f545a2f437c5441780`

```kotlin
package com.example.packguide.ui.localization

import com.example.packguide.scanning.PackingEstimate
import com.example.packguide.settings.AppLanguage
import java.util.Locale

/** One decimal separator/precision across every packing screen. Round only for display. */
fun formatLiters(value: Double): String =
    if (value > 0 && value < 0.05) "<0.1" else String.format(Locale.US, "%.1f", value)

class PackingText(private val language: AppLanguage) {
    private fun choose(es: String, en: String) = if (language == AppLanguage.English) en else es
    fun volume(estimate: PackingEstimate) = "${formatLiters(estimate.usedL)} L / ${formatLiters(estimate.capacityL)} L"
    fun balance(estimate: PackingEstimate): String = if (estimate.exceedsCapacity)
        choose("EXCESO EST.: ${formatLiters(estimate.excessL)} L", "EST. EXCESS: ${formatLiters(estimate.excessL)} L")
    else choose("ESPACIO EST.: ${formatLiters(estimate.freeL)} L", "EST. FREE SPACE: ${formatLiters(estimate.freeL)} L")
    fun recommendation(estimate: PackingEstimate) = balance(estimate) + "; " + if (estimate.exceedsCapacity)
        choose("reduce artículos o elige una maleta mayor.", "reduce items or choose a larger bag.")
    else choose("comprueba el ajuste al empacar.", "check the fit while packing.")
    fun itemTotal(units: Int, volume: Double) = choose("TOTAL: $units unidades · ${formatLiters(volume)} L",
        "TOTAL: $units units · ${formatLiters(volume)} L")
    fun counts(selected: Int, packed: Int) = choose("$selected unidades seleccionadas · $packed empacadas",
        "$selected units selected · $packed packed")
    fun progress(packed: Int, selected: Int, percent: Int) = choose("$packed de $selected unidades empacadas · $percent%",
        "$packed of $selected units packed · $percent%")
    fun selectedBag(label: String) = choose("MALETA SELECCIONADA: ", "SELECTED BAG: ") + localizedText(label, language)
    fun itemEstimate(volume: Double) = choose("ESTIMACIÓN POR UNIDAD: ", "ESTIMATE PER UNIT: ") + "${formatLiters(volume)} L"
    fun itemGeometry(volume: Double) = choose("VOLUMEN RECTANGULAR DE REFERENCIA: ", "REFERENCE RECTANGULAR VOLUME: ") + "${formatLiters(volume)} L"
    fun bagSummary(label: String, estimate: PackingEstimate, packed: Int, selected: Int) =
        "${localizedText(label, language)} · ${volume(estimate)} · $packed/$selected " + choose("empacados", "packed")
    fun occupancy(percent: Int) = choose("OCUPACIÓN EST.: $percent%", "EST. OCCUPANCY: $percent%")
    fun zone(label: String, units: Int, volume: Double) = "${localizedText(label, language)}: $units " +
        choose("unidades", "units") + " · ${formatLiters(volume)} L"
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/navigation/BottomNavBar.kt`

SHA-256: `481dd3cf0cd582920f3bc3e445a7b7ebe36e9e5655ef6c4b3858b7341ef5f2a4`

```kotlin
package com.example.packguide.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun TechBottomNavBar(
    selectedTab: NavTab?,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier,
    tabs: List<NavTab?> = NavTab.tabs,
    currentDestination: NavTab? = selectedTab,
    onSettingsClick: () -> Unit = {}
) {
    val language = LocalAppLanguage.current
    Surface(modifier = modifier.fillMaxWidth(), color = PackGuideColors.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), shadowElevation = 2.dp) {
        Row(
            Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            tabs.filterNotNull().forEach { tab ->
                val selected = currentDestination?.route == tab.route
                val tint by animateColorAsState(
                    if (selected) PackGuideColors.primaryText else PackGuideColors.secondaryText,
                    tween(180), label = "NavigationTint")
                Column(
                    Modifier.weight(1f).clip(RoundedCornerShape(18.dp))
                        .background(if (selected) PackGuideColors.surfaceVariant else Color.Transparent)
                        .selectable(selected = selected, role = Role.Tab, onClick = { onTabSelected(tab) })
                        .padding(horizontal = 2.dp, vertical = 9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(tab.icon, contentDescription = null, tint = tint, modifier = Modifier.size(21.dp))
                    Text(localizedText(tab.title, language).trim('[', ']'),
                        style = Typography.labelSmall.copy(fontSize = 9.sp, letterSpacing = 0.sp),
                        color = tint, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Box(Modifier.size(3.dp).background(if (selected) AccentRedLed else Color.Transparent, CircleShape))
                }
            }
            Column(
                Modifier.weight(1f).clip(RoundedCornerShape(18.dp))
                    .clickable(role = Role.Button, onClick = onSettingsClick)
                    .padding(horizontal = 2.dp, vertical = 9.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Outlined.Settings,
                    contentDescription = localizedText("Configuración", language),
                    tint = PackGuideColors.secondaryText, modifier = Modifier.size(21.dp))
                Text(localizedText("[AJUSTES]", language).trim('[', ']'),
                    style = Typography.labelSmall.copy(fontSize = 9.sp, letterSpacing = 0.sp),
                    color = PackGuideColors.secondaryText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.size(3.dp))
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/navigation/NavTab.kt`

SHA-256: `93195f2ca654016733ffc5a7f2239cb2b2f274507c9e657a5512f1852120533e`

```kotlin
package com.example.packguide.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavTab(
    val route: String,
    val title: String,
    val asciiLabel: String,
    val icon: ImageVector,
    val sysCode: String
) {
    object Scanner : NavTab(
        route = "scanner",
        title = "[ESCÁNER]",
        asciiLabel = "[ ESCÁNER ]",
        icon = Icons.Outlined.QrCodeScanner,
        sysCode = "SYS // CAM_MODULE"
    )

    object Catalog : NavTab(
        route = "catalog",
        title = "[CATÁLOGO]",
        asciiLabel = "[ CATÁLOGO ]",
        icon = Icons.Outlined.Inventory2,
        sysCode = "SYS // INVENTORY_DB"
    )

    object Guide : NavTab(
        route = "guide",
        title = "[GUÍA]",
        asciiLabel = "[ GUÍA ]",
        icon = Icons.Outlined.Checklist,
        sysCode = "SYS // PACKING_GUIDE"
    )

    object Summary : NavTab(
        route = "summary",
        title = "[RESUMEN]",
        asciiLabel = "[ RESUMEN ]",
        icon = Icons.Outlined.Analytics,
        sysCode = "SYS // OVERVIEW_METRICS"
    )

    companion object {
        val tabs = listOf(Scanner, Catalog, Guide, Summary)
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/AuthScreen.kt`

SHA-256: `a59bb8c7a08513db04a1b5599287532e5a41f2e5f1a5d3d71ce11e2848802b5d`

```kotlin
package com.example.packguide.ui.screens

import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.example.packguide.ui.localization.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.packguide.data.auth.AuthRepository
import com.example.packguide.data.auth.AuthResult
import com.example.packguide.data.auth.AuthValidation
import com.example.packguide.settings.AppLanguage
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.DashedDivider
import com.example.packguide.ui.components.HudHeader
import com.example.packguide.ui.components.LedIndicator
import com.example.packguide.ui.components.SettingsDialog
import com.example.packguide.ui.localization.Text as LocalizedText
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    repository: AuthRepository,
    settings: AppSettings,
    onThemeChange: (Boolean) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onAuthenticated: () -> Unit
) {
    var isRegistering by rememberSaveable { mutableStateOf(false) }
    var username by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val submit = {
        val cleanUser = username.trim()
        error = AuthValidation.usernameError(cleanUser)
            ?: if (isRegistering) AuthValidation.passwordError(password)
            else if (password.isEmpty()) "Ingresa tu contraseña."
            else null
            ?: if (isRegistering && password != confirmPassword) {
                "Las contraseñas no coinciden."
            } else null
        if (error == null && !isLoading) {
            isLoading = true
            scope.launch {
                val result = if (isRegistering) repository.register(cleanUser, password)
                else repository.login(cleanUser, password)
                isLoading = false
                when (result) {
                    AuthResult.Success -> onAuthenticated()
                    AuthResult.InvalidInput -> error = "Revisa el usuario y la contraseña e intenta de nuevo."
                    AuthResult.InvalidCredentials -> error = "Usuario o contraseña incorrectos."
                    AuthResult.UsernameTaken -> {
                        isRegistering = false
                        confirmPassword = ""
                        error = "Ese usuario ya existe. Inicia sesión con esa cuenta."
                    }
                    AuthResult.StorageError -> error = "No se pudo acceder a la base local. Intenta de nuevo."
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(PackGuideColors.background).safeDrawingPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LedIndicator(color = AccentRedLed, isPulsing = false)
            Spacer(Modifier.width(10.dp))
            LocalizedText("PACKGUIDE // ACCESS", style = Typography.labelMedium, color = PackGuideColors.secondaryText)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { showSettings = true }) {
                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = localizedText("Configuración", LocalAppLanguage.current),
                    tint = PackGuideColors.primaryText
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        HudHeader(
            title = if (isRegistering) "CREAR CUENTA" else "INICIAR SESIÓN",
            sysCode = "",
            statusText = "", horizontalPadding = 0.dp
        )
        Spacer(Modifier.height(20.dp))
        Spacer(Modifier.height(22.dp))

        Column(
            modifier = Modifier.fillMaxWidth().background(PackGuideColors.surface, RoundedCornerShape(14.dp))
                .border(1.dp, PackGuideColors.border, RoundedCornerShape(14.dp)).padding(20.dp)
        ) {
            Text("[ CREDENCIALES ]", style = Typography.labelMedium, color = PackGuideColors.primaryText)
            Spacer(Modifier.height(18.dp))
            AuthField(
                value = username,
                onValueChange = { username = it; error = null },
                label = "USUARIO",
                isUsername = true,
                supportingText = if (isRegistering) {
                    "Recomendado: letras, números, punto, guion o guion bajo."
                } else null,
                enabled = !isLoading
            )
            Spacer(Modifier.height(12.dp))
            AuthField(
                value = password,
                onValueChange = {
                    password = if (isRegistering) it.take(45) else it
                    error = null
                },
                label = "CONTRASEÑA",
                secret = true,
                supportingText = if (isRegistering) "1 a 45 caracteres: solo letras y números." else null,
                isPasswordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it },
                onImeAction = { if (!isRegistering) submit() },
                enabled = !isLoading
            )
            if (isRegistering) {
                Spacer(Modifier.height(12.dp))
                AuthField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it.take(45)
                        error = null
                    },
                    label = "CONFIRMAR CONTRASEÑA",
                    secret = true,
                    isPasswordVisible = confirmPasswordVisible,
                    onPasswordVisibilityChange = { confirmPasswordVisible = it },
                    onImeAction = submit,
                    enabled = !isLoading
                )
            }
            if (error != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = error.orEmpty(),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = Typography.bodyMedium,
                    color = AccentRedLed
                )
            }
            Spacer(Modifier.height(20.dp))
            AsciiButton(
                text = when {
                    isLoading && isRegistering -> "[ CREANDO CUENTA... ]"
                    isLoading -> "[ VALIDANDO... ]"
                    isRegistering -> "[ REGISTRAR ]"
                    else -> "[ ENTRAR ]"
                },
                onClick = submit,
                modifier = Modifier.fillMaxWidth(),
                isAccentRed = false,
                enabled = !isLoading
            )
            if (isLoading) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(16.dp), color = AccentRedLed, strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                    Text("VERIFICANDO CREDENCIALES...", style = Typography.labelSmall, color = PackGuideColors.secondaryText)
                }
            }
            Spacer(Modifier.height(10.dp))
            AsciiButton(
                text = if (isRegistering) "[ YA TENGO CUENTA ]" else "[ CREAR CUENTA ]",
                onClick = {
                    isRegistering = !isRegistering
                    confirmPassword = ""
                    confirmPasswordVisible = false
                    error = null
                },
                modifier = Modifier.fillMaxWidth(),
                isPrimary = false,
                enabled = !isLoading
            )
        }
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            LedIndicator(color = AccentGreenLed, isPulsing = false)
            Spacer(Modifier.width(8.dp))
            Text("CUENTA ALMACENADA EN ESTE DISPOSITIVO", style = Typography.labelSmall, color = PackGuideColors.secondaryText)
        }
        if (showSettings) {
            SettingsDialog(
                language = settings.language,
                isDarkTheme = settings.isDarkTheme,
                showLogout = false,
                onToggleTheme = { onThemeChange(!settings.isDarkTheme) },
                onToggleLanguage = {
                    onLanguageChange(if (settings.language == AppLanguage.Spanish) AppLanguage.English else AppLanguage.Spanish)
                },
                onLogout = {},
                onDismiss = { showSettings = false }
            )
        }
    }
}

@Composable
private fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    secret: Boolean = false,
    isUsername: Boolean = false,
    supportingText: String? = null,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: (Boolean) -> Unit = {},
    onImeAction: () -> Unit = {},
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        label = { Text(label, fontFamily = TechMonospaceFontFamily) },
        supportingText = supportingText?.let { help ->
            { Text(help, style = Typography.labelSmall, color = PackGuideColors.secondaryText) }
        },
        singleLine = true,
        visualTransformation = if (secret && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (secret) KeyboardType.Password else KeyboardType.Text,
            imeAction = if (secret) ImeAction.Done else ImeAction.Next,
            capitalization = if (isUsername) KeyboardCapitalization.None else KeyboardCapitalization.Sentences,
            autoCorrectEnabled = false
        ),
        keyboardActions = KeyboardActions(onDone = { onImeAction() }),
        trailingIcon = if (secret) {
            {
                TextButton(onClick = { onPasswordVisibilityChange(!isPasswordVisible) }) {
                    Text(if (isPasswordVisible) "OCULTAR" else "VER", style = Typography.labelSmall)
                }
            }
        } else null,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PackGuideColors.primaryText,
            unfocusedBorderColor = PackGuideColors.border,
            unfocusedContainerColor = PackGuideColors.surfaceVariant.copy(alpha = 0.35f),
            focusedLabelColor = PackGuideColors.primaryText,
            cursorColor = AccentRedLed
        )
    )
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/CatalogInventoryCard.kt`

SHA-256: `70b92286042cb1ab48440734ec7fed3c8b40ca26d29175ac865933bfcd440d5c`

```kotlin
package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.components.TechBadge
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import java.util.Locale
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.formatLiters

@Composable
fun CatalogInventoryCard(item: CatalogItemData, packing: PackingState) {
    val language = LocalAppLanguage.current
    val copy = PackingText(language)
    val totalQuantity = packing.selectedLuggage.sumOf { type -> packing.bag(type).items.first { it.id == item.id }.quantity }
    val totalVolume = packing.selectedLuggage.sumOf { type -> packing.bag(type).items.first { it.id == item.id }.totalVolumeL }
    TechCard {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(44.dp).background(PackGuideColors.surfaceVariant, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center) {
                Icon(item.icon, contentDescription = null, tint = PackGuideColors.primaryText, modifier = Modifier.size(22.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.name, style = Typography.titleMedium, color = PackGuideColors.primaryText)
                Text("${item.dimensions} · ${copy.itemEstimate(item.unitVolumeL)}", style = Typography.bodySmall,
                    color = PackGuideColors.secondaryText)
            }
        }
        Text(copy.itemGeometry(item.dimensionsCm.rectangularVolumeL), style = Typography.bodySmall,
            color = PackGuideColors.secondaryText)
        Spacer(Modifier.height(12.dp))
        TechBadge(item.category, containerColor = PackGuideColors.surfaceVariant, contentColor = PackGuideColors.secondaryText)
        Spacer(Modifier.height(12.dp))
        Text("DESTINO DEL ARTÍCULO", style = Typography.labelSmall, color = PackGuideColors.secondaryText)
        Spacer(Modifier.height(12.dp))
        packing.selectedLuggage.forEach { type ->
            val assigned = packing.bag(type).items.first { it.id == item.id }
            Row(Modifier.fillMaxWidth()
                .background(PackGuideColors.surfaceVariant, RoundedCornerShape(16.dp))
                .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(type.label, style = Typography.labelMedium, color = PackGuideColors.primaryText)
                Text("${formatLiters(assigned.totalVolumeL)} L",
                    style = Typography.titleLarge, color = PackGuideColors.primaryText)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    packing.updateItem(item.id, type) { current ->
                        val quantity = (current.quantity - 1).coerceAtLeast(0)
                        current.copy(quantity = quantity, packedQuantity = current.packedQuantity.coerceAtMost(quantity))
                    }
                }, enabled = assigned.quantity > 0,
                    modifier = Modifier.size(48.dp)
                        .background(PackGuideColors.surface, RoundedCornerShape(12.dp))
                        .border(1.dp, PackGuideColors.border, RoundedCornerShape(12.dp))) {
                    Icon(Icons.Outlined.Remove, "${localizedText("Disminuir cantidad", language)}: ${type.label}",
                        tint = if (assigned.quantity > 0) PackGuideColors.primaryText else PackGuideColors.secondaryText.copy(alpha = 0.45f),
                        modifier = Modifier.size(18.dp))
                }
                Text(assigned.quantity.toString(), style = Typography.titleMedium, color = PackGuideColors.primaryText,
                    modifier = Modifier.widthIn(min = 24.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                IconButton(onClick = { packing.updateItem(item.id, type) { it.copy(quantity = it.quantity + 1) } },
                    modifier = Modifier.size(48.dp)
                        .background(PackGuideColors.surface, RoundedCornerShape(12.dp))
                        .border(1.dp, PackGuideColors.border, RoundedCornerShape(12.dp))) {
                    Icon(Icons.Outlined.Add, "${localizedText("Aumentar cantidad", language)}: ${type.label}",
                        tint = PackGuideColors.primaryText, modifier = Modifier.size(18.dp))
                }
            }
        }
            Spacer(Modifier.height(12.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(copy.itemTotal(totalQuantity, totalVolume),
            style = Typography.bodySmall, color = PackGuideColors.secondaryText)
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/CatalogScreen.kt`

SHA-256: `acd2b21ac90c65eb675c5b9fc55e56b237a9c68890f6ad0c5b213c1320aa08e6`

```kotlin
package com.example.packguide.ui.screens


import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.components.HudHeader
import com.example.packguide.ui.components.LedIndicator
import com.example.packguide.ui.components.TechBadge
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography
import java.util.Locale
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText

@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier,
    packing: PackingState,
    onCreatePlan: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("TODOS") }

    val categories = listOf("TODOS", "ROPA", "CALZADO", "ASEO", "TECH")

    // Items list initialized with exact specification defaults
    val itemsList = packing.catalogItems

    val filteredItems = remember(itemsList, selectedCategory, searchQuery) { itemsList.filter { item ->
        val matchesCategory = (selectedCategory == "TODOS") || (item.category == selectedCategory)
        val matchesQuery = searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true) || item.category.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    } }

    val copy = PackingText(LocalAppLanguage.current)
    val estimate = packing.activeBag.estimate

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PackGuideColors.background)
    ) {
        // Encabezado del catálogo
        HudHeader(
            title = "CATÁLOGO DE ÍTEMS",
            sysCode = "",
            statusText = "",
            ledColor = AccentGreenLed
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            // --- 2. SEARCH INPUT FIELD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PackGuideColors.surface, shape = RoundedCornerShape(16.dp))
                    .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = PackGuideColors.primaryText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "BUSCAR ÍTEM... [ _ ]",
                                style = Typography.bodyMedium,
                                fontFamily = TechMonospaceFontFamily,
                                color = PackGuideColors.secondaryText
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = Typography.bodyMedium.copy(
                                color = PackGuideColors.primaryText,
                                fontFamily = TechMonospaceFontFamily
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- 3. CATEGORY FILTER CHIPS ---
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isActive = selectedCategory == category
                    val chipText = "[$category]"

                    Box(
                        modifier = Modifier
                            .background(
                                if (isActive) PackGuideColors.primaryText else PackGuideColors.surface,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                1.dp,
                                if (isActive) PackGuideColors.primaryText else PackGuideColors.border,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isActive) {
                                LedIndicator(color = AccentRedLed, size = 5.dp, isPulsing = false)
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = chipText,
                                style = Typography.labelSmall.copy(fontSize = 11.sp),
                                fontFamily = TechMonospaceFontFamily,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                color = if (isActive) PackGuideColors.surface else PackGuideColors.primaryText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- 4. ITEM INVENTORY LIST ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    LuggageSelector(packing)
                    Text("DESTINOS DEL VIAJE", style = Typography.labelMedium, color = PackGuideColors.primaryText)
                    Text("Reparte las cantidades de cada artículo entre las maletas elegidas en Escáner.",
                        style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                    packing.selectedLuggage.forEach { type ->
                        val bag = packing.bag(type)
                        val over = bag.estimate.exceedsCapacity
                        Text("${type.label}: ${copy.volume(bag.estimate)} · ${copy.balance(bag.estimate)}",
                            style = Typography.bodySmall,
                            color = if (over) AccentRedLed else PackGuideColors.primaryText)
                    }
                    Text("Volumen estimado / capacidad interior total", style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                    Text("Medidas de referencia; litros por unidad estimados de forma independiente. No hay un factor de compresión medido.",
                        style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                }
                if (filteredItems.isEmpty()) {
                    item {
                        Text("No hay artículos para esta búsqueda.",
                            style = Typography.bodyMedium,
                            color = PackGuideColors.secondaryText,
                            modifier = Modifier.padding(vertical = 24.dp))
                    }
                }
                items(filteredItems, key = { it.id }) { item ->
                    CatalogInventoryCard(item = item, packing = packing)
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // --- 5. BOTTOM CAPACITY TRACKER CARD ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(0.dp, shape = RoundedCornerShape(20.dp))
                    .background(PackGuideColors.surface, shape = RoundedCornerShape(20.dp))
                    .border(1.dp, PackGuideColors.border, shape = RoundedCornerShape(20.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(copy.selectedBag(packing.activeLuggage.label), style = Typography.labelMedium, color = PackGuideColors.primaryText)
                    Text(copy.counts(packing.selectedUnits, packing.packedUnits),
                        style = Typography.labelMedium,
                        color = PackGuideColors.primaryText)
                    // Metric header & available badge
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Volumen estimado / capacidad interior total", style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                        Text(
                            text = copy.volume(estimate),
                            style = Typography.titleMedium,
                            fontFamily = TechMonospaceFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = PackGuideColors.primaryText
                        )

                        TechBadge(
                            text = copy.balance(estimate),
                            containerColor = PackGuideColors.primaryText,
                            contentColor = PackGuideColors.surface,
                            ledColor = if (estimate.exceedsCapacity) AccentRedLed else AccentGreenLed
                        )
                    }

                    // Visual Segmented Progress Bar (10 block segments)
                    val filledSegmentsCount = (estimate.occupancy * 10).toInt().coerceIn(0, 10)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (i in 1..10) {
                            val isFilled = i <= filledSegmentsCount
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(5.dp)
                                    .background(
                                        if (isFilled) PackGuideColors.primaryText else PackGuideColors.surfaceVariant,
                                        shape = RoundedCornerShape(1.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isFilled) PackGuideColors.primaryText else PackGuideColors.border,
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )
                        }
                    }

                    com.example.packguide.ui.components.AsciiButton(
                        text = "CREAR PLAN DE EMPACADO",
                        onClick = onCreatePlan,
                        icon = Icons.AutoMirrored.Outlined.ArrowForward,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/GuideScreen.kt`

SHA-256: `386e78dfafd267d5c5c2696d2cd2df3a9538d1f11b733634d067bf94608baf57`

```kotlin
package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun GuideScreen(
    packing: PackingState,
    modifier: Modifier = Modifier,
    onNavigateToTab: ((NavTab) -> Unit)? = null,
    scanner: com.example.packguide.scanning.ScannerViewModel? = null,
    preview: com.example.packguide.ai.PackingPreviewViewModel? = null
) {
    val selectedItems = packing.items.filter { it.quantity > 0 }
    val progress = packing.activeBag.progressPercent
    val copy = com.example.packguide.ui.localization.PackingText(com.example.packguide.ui.localization.LocalAppLanguage.current)

    Column(modifier.fillMaxSize().background(PackGuideColors.background)) {
        com.example.packguide.ui.components.HudHeader(title = "GUÍA DE EMPACADO", sysCode = "", statusText = "")
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { LuggageSelector(packing) }
            item { PackingSuggestionPanel(packing) }
            item {
                TechCard(headerTag = "PROGRESO REAL", borderColor = PackGuideColors.primaryText) {
                    Text(copy.progress(packing.packedUnits, packing.selectedUnits, progress),
                        style = Typography.titleMedium, color = PackGuideColors.primaryText)
                    Spacer(Modifier.height(8.dp))
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = PackGuideColors.primaryText,
                        trackColor = PackGuideColors.surfaceVariant,
                        drawStopIndicator = {}
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Marca cada objeto cuando esté dentro de la maleta. Toca la zona para cambiar su ubicación.",
                        style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                }
            }
            if (scanner != null && preview != null) item {
                PackingGuidePreviewPanel(packing, scanner, preview,
                    onOpenScanner = { onNavigateToTab?.invoke(NavTab.Scanner) },
                    onOpenCatalog = { onNavigateToTab?.invoke(NavTab.Catalog) })
            }
            item {
                TechCard(headerTag = "CONSEJO DE DISTRIBUCIÓN", borderColor = PackGuideColors.border) {
                    Text("Coloca el calzado y los objetos pesados en la base; la ropa en el centro y lo que necesitarás durante el viaje arriba.",
                        style = Typography.bodyMedium, color = PackGuideColors.primaryText)
                }
            }
            item {
                Text("ASIGNACIÓN EN VALIJA", style = Typography.titleLarge,
                    color = PackGuideColors.primaryText)
                Text("Cada artículo tiene una zona editable y un contador de unidades empacadas.",
                    style = Typography.bodySmall, color = PackGuideColors.secondaryText)
            }
            if (selectedItems.isEmpty()) {
                item {
                    Text("Aún no hay artículos seleccionados. Añádelos en el catálogo.",
                        color = PackGuideColors.secondaryText)
                    AsciiButton("IR AL CATÁLOGO", onClick = { onNavigateToTab?.invoke(NavTab.Catalog) },
                        modifier = Modifier.fillMaxWidth())
                }
            }
            items(selectedItems, key = { it.id }) { item ->
                TechCard(borderColor = PackGuideColors.border) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(item.icon, contentDescription = null, tint = PackGuideColors.primaryText)
                        Text("  ${item.name}", style = Typography.titleMedium,
                            color = PackGuideColors.primaryText, modifier = Modifier.weight(1f))
                        Text("${item.packedQuantity}/${item.quantity}", color = PackGuideColors.primaryText)
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsciiButton(
                            text = "ZONA: ${item.zone.label} ↻",
                            onClick = { packing.updateItem(item.id) { it.copy(zone = it.zone.next()) } },
                            modifier = Modifier.weight(1f), isPrimary = false
                        )
                        AsciiButton(
                            text = if (item.packedQuantity == item.quantity) "DESMARCAR" else "MARCAR EMPACADO",
                            onClick = {
                                packing.updateItem(item.id) {
                                    it.copy(packedQuantity = if (it.packedQuantity == it.quantity) 0 else it.packedQuantity + 1)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            isPrimary = item.packedQuantity != item.quantity
                        )
                    }
                }
            }
            item {
                AsciiButton("VER RESUMEN", onClick = { onNavigateToTab?.invoke(NavTab.Summary) },
                    modifier = Modifier.fillMaxWidth(), isPrimary = true)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/LuggageSelector.kt`

SHA-256: `8736c2a04821a82b0f381a28d9088f6543515fc7b3920885886cc10eb0b98c35`

```kotlin
package com.example.packguide.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun LuggageSelector(packing: PackingState) {
    Column {
        Text("MALETA A ORGANIZAR", style = Typography.labelMedium, color = PackGuideColors.secondaryText)
        Row(Modifier.fillMaxWidth().selectableGroup(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            packing.selectedLuggage.forEach { type ->
                FilterChip(
                    selected = packing.activeLuggage == type,
                    onClick = { packing.selectLuggage(type) },
                    label = { Text(type.label, style = Typography.labelSmall) }
                )
            }
        }
    }
}

@Composable
fun TripLuggageSelection(packing: PackingState, onToggle: (LuggagePreset) -> Unit) {
    Column {
        Text("EQUIPAJE DEL VIAJE", style = Typography.titleMedium, color = PackGuideColors.primaryText)
        Text("Selecciona todas las maletas que llevarás. Debe quedar al menos una.",
            style = Typography.bodySmall, color = PackGuideColors.secondaryText)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LuggagePreset.entries.forEach { type ->
                val selected = type in packing.selectedLuggage
                Column(Modifier.weight(1f).selectable(
                    selected = selected,
                    enabled = !selected || packing.selectedLuggage.size > 1,
                    role = Role.Checkbox,
                    onClick = { onToggle(type) }
                ).padding(vertical = 8.dp)) {
                    Checkbox(checked = selected, onCheckedChange = null)
                    Text(type.label, style = Typography.labelMedium, color = PackGuideColors.primaryText)
                }
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/MainScreen.kt`

SHA-256: `a530e20da400dcbeb1d7be12d796e57bbdccf18ea4aa2fb59e8ec43c39b1c893`

```kotlin
package com.example.packguide.ui.screens


import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.navigation.TechBottomNavBar
import com.example.packguide.settings.AppLanguage
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.components.SettingsDialog

@Composable
fun MainScreen(
    settings: AppSettings,
    onThemeChange: (Boolean) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onLogout: () -> Unit
) {
    var currentTab by remember { mutableStateOf<NavTab>(NavTab.Scanner) }
    var showSettings by remember { mutableStateOf(false) }
    val scanner: com.example.packguide.scanning.ScannerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val packing = remember { PackingState() }
    val preview: com.example.packguide.ai.PackingPreviewViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    androidx.compose.runtime.LaunchedEffect(packing.selectedLuggage) { preview.retain(packing.selectedLuggage) }
    androidx.compose.runtime.DisposableEffect(preview) { onDispose { preview.clear() } }

    fun navigateTo(tab: NavTab) {
        currentTab = tab
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PackGuideColors.background,
        bottomBar = {
            TechBottomNavBar(
                selectedTab = currentTab,
                onTabSelected = ::navigateTo,
                tabs = listOf(NavTab.Scanner, NavTab.Catalog, NavTab.Guide, NavTab.Summary),
                currentDestination = currentTab,
                onSettingsClick = { showSettings = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    is NavTab.Scanner -> ScannerScreen(packing = packing, scanner = scanner, onOpenCatalog = { navigateTo(NavTab.Catalog) })
                    is NavTab.Catalog -> CatalogScreen(packing = packing, onCreatePlan = { navigateTo(NavTab.Guide) })
                    is NavTab.Guide -> GuideScreen(packing = packing, scanner = scanner, preview = preview, onNavigateToTab = ::navigateTo)
                    is NavTab.Summary -> SummaryScreen(

                        packing = packing,
                        preview = preview,
                        onNavigateToTab = ::navigateTo
                    )
                }
            }
            if (showSettings) {
                SettingsDialog(
                    language = settings.language,
                    isDarkTheme = settings.isDarkTheme,
                    showLogout = true,
                    onToggleTheme = { onThemeChange(!settings.isDarkTheme) },
                    onToggleLanguage = {
                        onLanguageChange(if (settings.language == AppLanguage.Spanish) AppLanguage.English else AppLanguage.Spanish)
                    },
                    onLogout = {
                        showSettings = false
                        scanner.clearPhotos()
                        preview.clear()
                        onLogout()
                    },
                    onDismiss = { showSettings = false }
                )
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/PackingPreviewPanels.kt`

SHA-256: `e9c321265fbf5a4f5f1c6656e447a19b7ce0770752a29ea45e6142b382aeb8af`

```kotlin
package com.example.packguide.ui.screens

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.packguide.ai.*
import com.example.packguide.scanning.ScannerViewModel
import com.example.packguide.settings.AppLanguage
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
private fun previewText(es: String, en: String) = if (LocalAppLanguage.current == AppLanguage.English) en else es

@Composable
private fun PreviewImage(bitmap: Bitmap, description: String) {
    var expanded by remember(bitmap) { mutableStateOf(false) }
    val enlarge = previewText("Ampliar vista organizada", "Enlarge organized preview")
    Image(bitmap.asImageBitmap(), contentDescription = description,
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp, max = 340.dp)
            .clickable(role = Role.Button, onClickLabel = enlarge) { expanded = true })
    TextButton(onClick = { expanded = true }) { Text(enlarge) }
    if (expanded) Dialog(onDismissRequest = { expanded = false },
        properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxWidth().fillMaxHeight(0.95f).padding(12.dp), color = PackGuideColors.surface) {
            Column {
                Image(bitmap.asImageBitmap(), contentDescription = description,
                    modifier = Modifier.fillMaxWidth().weight(1f), contentScale = ContentScale.Fit)
                TextButton(onClick = { expanded = false }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text(previewText("CERRAR", "CLOSE"))
                }
            }
        }
    }
}

@Composable
private fun PreviewStatus(state: PackingPreviewState, onCancel: () -> Unit) {
    state.work?.let { work ->
        CircularProgressIndicator(color = PackGuideColors.primaryText)
        Text(if (work == PreviewWork.Photos) previewText("Abriendo fotos…", "Opening photos…")
            else previewText("Creando vista organizada…", "Creating organized preview…"),
            color = PackGuideColors.primaryText, style = Typography.bodySmall)
        AsciiButton(previewText("CANCELAR", "CANCEL"), onClick = onCancel,
            isPrimary = false, modifier = Modifier.fillMaxWidth())
    }
    state.problem?.let { problem ->
        Text(when (problem) {
            PreviewProblem.Photo -> previewText("No se pudo abrir la foto o la cámara. Intenta elegir otra imagen.", "Could not open the photo or camera. Try another image.")
            PreviewProblem.UnsupportedPhoto -> previewText("Ese archivo no es una foto compatible. Elige una imagen JPG, PNG o WebP.", "This file is not a supported photo. Choose a JPG, PNG or WebP image.")
            PreviewProblem.LargePhoto -> previewText("La foto es demasiado grande. Elige una imagen más pequeña.", "The photo is too large. Choose a smaller image.")
            PreviewProblem.Generation -> previewText("No se pudo crear la vista. Inténtalo de nuevo o elige otra foto.", "Could not create the preview. Try again or choose another photo.")
            PreviewProblem.Connection -> previewText("El servicio no está disponible. Inténtalo de nuevo más tarde.", "The service is unavailable. Please try again later.")
            PreviewProblem.Cancelled -> previewText("Operación cancelada. Puedes volver a intentarlo.", "Operation cancelled. You can try again.")
        }, color = if (problem == PreviewProblem.Cancelled) PackGuideColors.secondaryText else MaterialTheme.colorScheme.error,
            style = Typography.bodySmall)
    }
}

@Composable
fun PackingGuidePreviewPanel(packing: PackingState, scanner: ScannerViewModel, preview: PackingPreviewViewModel,
                             onOpenScanner: () -> Unit, onOpenCatalog: () -> Unit) {
    val type = packing.activeLuggage
    val plan = PackingPlan.from(packing.activeBag)
    val source = scanner.stateFor(type).photo
    val state = preview.stateFor(type)
    val language = LocalAppLanguage.current
    val current = state.guidePlan == plan && state.guideSource === source
    TechCard(headerTag = previewText("VISTA ORGANIZADA DE EMPAQUE", "ORGANIZED PACKING PREVIEW")) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(previewText("Funciona sin conexión. Combina tu foto original con un esquema de los artículos por zonas; no modifica la foto ni reconoce objetos.",
                "Works offline. Combines your original photo with an item layout by zone; it does not edit the photo or recognize objects."),
                style = Typography.bodySmall, color = PackGuideColors.secondaryText)
            when {
                source == null -> {
                    Text(previewText("Añade una foto de esta maleta abierta en Escáner.", "Add a photo of this open bag in Scanner."), color = PackGuideColors.secondaryText)
                    AsciiButton(previewText("AÑADIR FOTO EN ESCÁNER", "ADD PHOTO IN SCANNER"), onClick = onOpenScanner, modifier = Modifier.fillMaxWidth())
                }
                else -> {
                    Text(previewText("FOTO DE REFERENCIA", "REFERENCE PHOTO"), style = Typography.labelMedium, color = PackGuideColors.primaryText)
                    Image(source.asImageBitmap(), contentDescription = previewText("Foto de tu maleta", "Photo of your suitcase"),
                        contentScale = ContentScale.Fit, modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 180.dp))
                    TextButton(onClick = onOpenScanner) { Text(previewText("CAMBIAR FOTO", "CHANGE PHOTO")) }
                }
            }
            if (plan.contents.isEmpty()) {
                Text(previewText("Selecciona los objetos de esta maleta en Catálogo.", "Select this bag's items in Catalog."), color = PackGuideColors.secondaryText)
                AsciiButton(previewText("IR AL CATÁLOGO", "OPEN CATALOG"), onClick = onOpenCatalog, modifier = Modifier.fillMaxWidth())
            }
            if (current) state.guide?.let { bitmap ->
                val description = previewText("Vista organizada de empaque. ", "Organized packing preview. ") +
                    plan.contents.joinToString("; ") { "${it.quantity} × ${localizedText(it.name, language)}: ${localizedText(it.zone, language)}" }
                PreviewImage(bitmap, description)
            }
            if (!current && state.guide != null) Text(previewText("Cambió la foto o el plan. Genera una nueva vista.", "The photo or plan changed. Generate a new preview."), color = PackGuideColors.secondaryText)
            AsciiButton(previewText("GENERAR IMAGEN ORGANIZADA", "GENERATE PACKING IMAGE"),
                onClick = { if (source != null) preview.generate(type, plan, source, language) },
                enabled = source != null && !scanner.stateFor(type).loading && plan.contents.isNotEmpty() && state.work == null,
                modifier = Modifier.fillMaxWidth())
            PreviewStatus(state) { preview.cancel(type) }
            Text(previewText("Es una vista orientativa, no una imagen generada por IA ni una garantía de que todo quepa. Tócala para ampliarla.",
                "This is an illustrative preview, not an AI generated image or a guarantee that everything fits. Tap to enlarge."),
                color = PackGuideColors.secondaryText, style = Typography.bodySmall)
        }
    }
}

@Composable
fun PackingReviewPreviewPanel(packing: PackingState, preview: PackingPreviewViewModel) {
    val type = packing.activeLuggage
    val plan = PackingPlan.from(packing.activeBag)
    val state = preview.stateFor(type)
    val language = LocalAppLanguage.current
    key(type) {
        val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(4)) { uris -> preview.addPhotos(type, uris) }
        val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo -> if (photo != null) preview.addPhoto(type, photo) }
        TechCard(headerTag = previewText("REFERENCIA PARA REVISAR TU MALETA", "PACKING REVIEW REFERENCE")) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(previewText("Añade hasta 4 fotos para comparar tu maleta con el esquema del inventario. La vista usa la primera foto como referencia. Se procesa en el dispositivo y no califica automáticamente las fotos.",
                    "Add up to 4 photos to compare your bag with the inventory layout. The preview uses the first photo as reference. Processing stays on the device and does not automatically score photos."),
                    style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.photos.size) { index ->
                        Column(Modifier.width(180.dp)) {
                            Image(state.photos[index].asImageBitmap(), contentDescription = previewText("Foto para comparar", "Photo to compare"),
                                contentScale = ContentScale.Fit, modifier = Modifier.fillMaxWidth().height(150.dp))
                            TextButton(onClick = { preview.removePhoto(type, index) }, enabled = state.work == null) { Text(previewText("QUITAR FOTO", "REMOVE PHOTO")) }
                        }
                    }
                }
                AsciiButton(previewText("AÑADIR FOTOS", "ADD PHOTOS"), onClick = {
                    try { gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
                    catch (_: ActivityNotFoundException) { preview.photoError(type) }
                    catch (_: SecurityException) { preview.photoError(type) }
                }, enabled = state.photos.size < 4 && state.work == null, isPrimary = false, modifier = Modifier.fillMaxWidth())
                AsciiButton(previewText("TOMAR FOTO", "TAKE PHOTO"), onClick = {
                    try { camera.launch(null) }
                    catch (_: ActivityNotFoundException) { preview.photoError(type) }
                    catch (_: SecurityException) { preview.photoError(type) }
                }, enabled = state.photos.size < 4 && state.work == null, isPrimary = false, modifier = Modifier.fillMaxWidth())
                AsciiButton(previewText("GENERAR REFERENCIA VISUAL", "CREATE VISUAL REFERENCE"),
                    onClick = { state.photos.firstOrNull()?.let { preview.generate(type, plan, it, language, review = true) } },
                    enabled = state.photos.isNotEmpty() && state.work == null && plan.contents.isNotEmpty(), modifier = Modifier.fillMaxWidth())
                PreviewStatus(state) { preview.cancel(type) }
                if (state.reviewPlan == plan) state.review?.let {
                    PreviewImage(it, previewText("Referencia de organización creada en el dispositivo", "Packing reference created on the device"))
                } else if (state.review != null) Text(previewText("Cambió el plan. Genera una nueva referencia.", "The plan changed. Create a new reference."), color = PackGuideColors.secondaryText)
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/PackingState.kt`

SHA-256: `605970c2248cfe2d3f241e740fae1e16a1070680e670571d8259a7cd701a7a7b`

```kotlin
package com.example.packguide.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.DryCleaning
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.packguide.scanning.PackingRules

/** Catalog reference envelope, not a measurement of a particular user's belongings. */
data class ItemDimensions(val lengthCm: Int, val widthCm: Int, val heightCm: Int) {
    val rectangularVolumeL: Double get() = PackingRules.rectangularVolumeL(lengthCm, widthCm, heightCm)
    val label: String get() = "$lengthCm × $widthCm × $heightCm cm"
}

data class CatalogItemData(
    val id: String,
    val name: String,
    val category: String,
    val dimensionsCm: ItemDimensions,
    // Legacy packing estimate per unit, independent of the reference envelope above.
    // No calibrated compression factors or measured object volumes exist in the catalog.
    val unitVolumeL: Double,
    val icon: ImageVector,
    val quantity: Int,
    val packedQuantity: Int = 0,
    val zone: PackingZone = PackingZone.Base
) {
    val dimensions: String get() = dimensionsCm.label
    val totalVolumeL: Double get() = PackingRules.totalVolumeL(listOf(quantity to unitVolumeL))
    init {
        require(quantity >= 0 && packedQuantity in 0..quantity)
        require(unitVolumeL.isFinite() && unitVolumeL >= 0)
        require(listOf(dimensionsCm.lengthCm, dimensionsCm.widthCm, dimensionsCm.heightCm).all { it in 1..300 })
    }
}

enum class PackingZone(val label: String) {
    Base("BASE"), Center("CENTRO"), Top("ACCESO RÁPIDO");
    fun next(): PackingZone = entries[(ordinal + 1) % entries.size]
}

enum class LuggagePreset(val label: String, val heightCm: Int, val widthCm: Int, val depthCm: Int) {
    Personal("MANO", 45, 35, 20),
    Cabin("CABINA", 55, 35, 25),
    Checked("BODEGA", 75, 50, 30);

    val capacityL: Double get() = PackingRules.rectangularVolumeL(heightCm, widthCm, depthCm)
}

private fun defaultCatalogItems() = listOf(
        CatalogItemData("ITEM-01", "CAMISETAS", "ROPA", ItemDimensions(28, 20, 4), 1.2, Icons.Outlined.Checkroom, 3),
        CatalogItemData("ITEM-02", "PANTALONES", "ROPA", ItemDimensions(38, 26, 6), 2.8, Icons.Outlined.DryCleaning, 2),
        CatalogItemData("ITEM-03", "CALZADO", "CALZADO", ItemDimensions(32, 22, 12), 4.5, Icons.AutoMirrored.Outlined.DirectionsWalk, 1),
        CatalogItemData("ITEM-04", "CHAQUETA", "ROPA", ItemDimensions(40, 28, 8), 3.2, Icons.Outlined.Checkroom, 1),
        CatalogItemData("ITEM-05", "NECESER", "ASEO", ItemDimensions(22, 14, 8), 1.5, Icons.Outlined.MedicalServices, 1),
        CatalogItemData("ITEM-06", "CARGADORES & TECH", "TECH", ItemDimensions(18, 12, 4), 0.8, Icons.Outlined.Devices, 1),
        CatalogItemData("ITEM-07", "ROPA INTERIOR", "ROPA", ItemDimensions(18, 12, 3), 0.3, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-08", "MEDIAS", "ROPA", ItemDimensions(15, 10, 3), 0.2, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-09", "PIJAMA", "ROPA", ItemDimensions(30, 22, 5), 1.4, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-10", "VESTIDO", "ROPA", ItemDimensions(35, 25, 5), 1.8, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-11", "FALDA", "ROPA", ItemDimensions(28, 20, 4), 1.0, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-12", "SHORTS", "ROPA", ItemDimensions(28, 20, 4), 1.0, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-13", "SUÉTER", "ROPA", ItemDimensions(35, 28, 7), 2.3, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-14", "CAMISA", "ROPA", ItemDimensions(30, 23, 4), 1.2, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-15", "TRAJE DE BAÑO", "ROPA", ItemDimensions(20, 15, 2), 0.3, Icons.Outlined.Checkroom, 0),
        CatalogItemData("ITEM-16", "ZAPATOS FORMALES", "CALZADO", ItemDimensions(34, 23, 12), 5.0, Icons.AutoMirrored.Outlined.DirectionsWalk, 0),
        CatalogItemData("ITEM-17", "SANDALIAS", "CALZADO", ItemDimensions(28, 20, 6), 2.0, Icons.AutoMirrored.Outlined.DirectionsWalk, 0),
        CatalogItemData("ITEM-18", "PANTUFLAS", "CALZADO", ItemDimensions(28, 18, 7), 2.1, Icons.AutoMirrored.Outlined.DirectionsWalk, 0),
        CatalogItemData("ITEM-19", "CEPILLO DE DIENTES", "ASEO", ItemDimensions(20, 3, 3), 0.1, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-20", "PASTA DENTAL", "ASEO", ItemDimensions(16, 5, 4), 0.2, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-21", "DESODORANTE", "ASEO", ItemDimensions(14, 5, 5), 0.3, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-22", "CHAMPÚ", "ASEO", ItemDimensions(16, 6, 6), 0.6, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-23", "JABÓN", "ASEO", ItemDimensions(10, 7, 4), 0.3, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-24", "PROTECTOR SOLAR", "ASEO", ItemDimensions(15, 6, 5), 0.5, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-25", "MEDICAMENTOS", "ASEO", ItemDimensions(18, 12, 5), 0.8, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-26", "LENTES DE CONTACTO", "ASEO", ItemDimensions(12, 8, 3), 0.2, Icons.Outlined.MedicalServices, 0),
        CatalogItemData("ITEM-27", "TELÉFONO", "TECH", ItemDimensions(16, 8, 2), 0.3, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-28", "TABLETA", "TECH", ItemDimensions(26, 18, 2), 0.7, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-29", "PORTÁTIL", "TECH", ItemDimensions(35, 25, 3), 2.6, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-30", "AURICULARES", "TECH", ItemDimensions(18, 15, 8), 1.0, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-31", "BATERÍA EXTERNA", "TECH", ItemDimensions(15, 8, 3), 0.4, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-32", "ADAPTADOR DE ENCHUFE", "TECH", ItemDimensions(8, 7, 5), 0.3, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-33", "CÁMARA", "TECH", ItemDimensions(18, 13, 10), 1.7, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-34", "TARJETAS DE MEMORIA", "TECH", ItemDimensions(8, 6, 2), 0.1, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-35", "CABLE USB", "TECH", ItemDimensions(12, 9, 3), 0.2, Icons.Outlined.Devices, 0),
        CatalogItemData("ITEM-36", "RELOJ", "TECH", ItemDimensions(10, 8, 5), 0.4, Icons.Outlined.Devices, 0),
    )

class LuggagePacking(val type: LuggagePreset, initialItems: List<CatalogItemData>) {
    init { require(initialItems.map { it.id }.distinct().size == initialItems.size) }
    val dimensionsConfirmed = mutableStateOf(false)
    val heightCm = mutableStateOf(type.heightCm)
    val widthCm = mutableStateOf(type.widthCm)
    val depthCm = mutableStateOf(type.depthCm)
    val items = mutableStateListOf<CatalogItemData>().apply { addAll(initialItems) }

    val estimate get() = PackingRules.estimate(heightCm.value, widthCm.value, depthCm.value,
        items.map { it.quantity to it.unitVolumeL })
    val capacityL: Double get() = estimate.capacityL
    val usedL: Double get() = estimate.usedL
    val packedUnits: Int get() = items.sumOf { it.packedQuantity }
    val selectedUnits: Int get() = items.sumOf { it.quantity }
    val progressPercent: Int get() = PackingRules.progressPercent(packedUnits, selectedUnits)

    fun updateItem(id: String, transform: (CatalogItemData) -> CatalogItemData) {
        val index = items.indexOfFirst { it.id == id }
        if (index >= 0) {
            val updated = transform(items[index])
            require(updated.id == id) { "Updating an item must preserve its identity" }
            items[index] = updated
        }
    }
}

class PackingState {
    val catalogItems: List<CatalogItemData> = defaultCatalogItems()
    private val luggage = mutableStateMapOf(
        LuggagePreset.Cabin to LuggagePacking(LuggagePreset.Cabin, catalogItems)
    )
    private val activeType = mutableStateOf(LuggagePreset.Cabin)
    val selectedLuggage: List<LuggagePreset> get() = LuggagePreset.entries.filter { it in luggage }
    val activeLuggage: LuggagePreset get() = activeType.value
    val activeBag: LuggagePacking get() = bag(activeLuggage)
    fun bag(type: LuggagePreset): LuggagePacking = requireNotNull(luggage[type])

    // Removing a bag removes its assignments; they are never silently moved to another bag.
    fun toggleLuggage(type: LuggagePreset) {
        if (type in luggage) {
            if (luggage.size == 1) return
            luggage.remove(type)
            if (activeLuggage == type) activeType.value = selectedLuggage.first()
        } else {
            luggage[type] = LuggagePacking(type, catalogItems.map { it.copy(quantity = 0, packedQuantity = 0) })
        }
    }

    fun selectLuggage(type: LuggagePreset) {
        if (type in luggage) activeType.value = type
    }

    val allDimensionsConfirmed: Boolean get() = selectedLuggage.all { bag(it).dimensionsConfirmed.value }
    val totalCapacityL: Double get() = selectedLuggage.sumOf { bag(it).capacityL }
    val totalUsedL: Double get() = selectedLuggage.sumOf { bag(it).usedL }
    val totalSelectedUnits: Int get() = selectedLuggage.sumOf { bag(it).selectedUnits }
    val totalPackedUnits: Int get() = selectedLuggage.sumOf { bag(it).packedUnits }

    val items get() = activeBag.items
    val heightCm get() = activeBag.heightCm
    val widthCm get() = activeBag.widthCm
    val depthCm get() = activeBag.depthCm
    val dimensionsConfirmed get() = activeBag.dimensionsConfirmed
    val capacityL get() = activeBag.capacityL
    val usedL get() = activeBag.usedL
    val selectedUnits get() = activeBag.selectedUnits
    val packedUnits get() = activeBag.packedUnits

    fun updateItem(id: String, type: LuggagePreset = activeLuggage, transform: (CatalogItemData) -> CatalogItemData) {
        luggage[type]?.updateItem(id, transform)
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/PackingSuggestionPanel.kt`

SHA-256: `a1f8668e6b1602b87e02db1019f8102020b5b984cde691fd7e66b5148e75afd0`

```kotlin
package com.example.packguide.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.packguide.scanning.PackingRules
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import java.util.Locale
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText

@Composable
fun PackingSuggestionPanel(packing: PackingState) {
    val estimate = packing.activeBag.estimate
    val copy = PackingText(LocalAppLanguage.current)
    var expanded by rememberSaveable { mutableStateOf(false) }
    TechCard(headerTag = "SUGERENCIA ORIENTATIVA") {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Volumen estimado / capacidad interior total", color = PackGuideColors.secondaryText,
                style = Typography.bodySmall)
            androidx.compose.material3.Text(
                copy.volume(estimate),
                color = if (estimate.exceedsCapacity) AccentRedLed else PackGuideColors.primaryText,
                style = Typography.displaySmall)
            if (!packing.dimensionsConfirmed.value) Text(
                "Confirma las medidas en Escáner antes de usar esta estimación.",
                color = PackGuideColors.secondaryText, style = Typography.bodySmall)
            Text(copy.recommendation(estimate),
                color = PackGuideColors.primaryText, style = Typography.bodySmall)
            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "OCULTAR DETALLES" else "VER CRITERIOS Y CONSEJOS",
                    color = PackGuideColors.primaryText, style = Typography.labelMedium)
            }
            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Capacidad interior = alto × ancho × fondo / 1000. Sin descuento adicional. Volumen estimado = suma de litros por unidad × cantidad seleccionada.",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                    Text("Medidas de referencia; litros por unidad estimados de forma independiente. No hay un factor de compresión medido.",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                    Text("Los litros por artículo son supuestos del catálogo, no medidas de la foto. No es una simulación física ni garantiza que todo quepa.",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                    Text("Calzado en bolsas junto a las ruedas; pantalones y chaquetas en la base. Enrolla camisas resistentes en el centro; vestidos delicados arriba. Accesorios en un bolsillo accesible.",
                        color = PackGuideColors.primaryText, style = Typography.bodySmall)
                }
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/PhotoScanPanel.kt`

SHA-256: `24bd9d832e7681638f89ba9fbad8be61771eae0a6bd90e88ef16610f5d8912b1`

```kotlin
package com.example.packguide.ui.screens

import android.content.ActivityNotFoundException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.packguide.scanning.ScannerViewModel
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.localization.localizedText
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography

@Composable
fun PhotoScanPanel(scanner: ScannerViewModel, luggage: LuggagePreset) {
    val state = scanner.stateFor(luggage)
    val language = LocalAppLanguage.current
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) scanner.selectPhoto(uri, luggage)
    }
    // The system camera supplies an in-memory thumbnail without creating app photo files.
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) scanner.selectPhoto(bitmap, luggage)
    }
    TechCard(headerTag = "EQUIPAJE", borderColor = PackGuideColors.primaryText) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Toma o elige una foto de tu maleta.",
                style = Typography.bodyMedium, color = PackGuideColors.primaryText)
            state.photo?.let { photo ->
                Image(photo.asImageBitmap(),
                    contentDescription = localizedText("Foto de tu maleta", language),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 140.dp, max = 260.dp)
                        .clip(RoundedCornerShape(12.dp)).background(PackGuideColors.surfaceVariant))
            }
            AsciiButton("TOMAR FOTO", onClick = {
                try { camera.launch(null) }
                catch (_: ActivityNotFoundException) {
                    scanner.message("Cámara no disponible. Usa Elegir foto.", luggage)
                } catch (_: SecurityException) {
                    scanner.message("Acceso a la cámara denegado. Usa Elegir foto.", luggage)
                }
            }, enabled = !state.loading, icon = Icons.Outlined.PhotoCamera, modifier = Modifier.fillMaxWidth())
            AsciiButton("ELEGIR FOTO", onClick = {
                try {
                    gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } catch (_: ActivityNotFoundException) {
                    scanner.message("Selector de fotos no disponible. Usa Tomar foto.", luggage)
                } catch (_: SecurityException) {
                    scanner.message("Acceso a la foto denegado. Intenta elegirla de nuevo.", luggage)
                }
            }, enabled = !state.loading, isPrimary = false, icon = Icons.Outlined.AddPhotoAlternate, modifier = Modifier.fillMaxWidth())
            if (state.loading) CircularProgressIndicator(color = PackGuideColors.primaryText)
            state.message?.let { Text(it, color = PackGuideColors.secondaryText, style = Typography.bodySmall) }
            Text("La foto es una referencia. El espacio se calcula con las medidas de la maleta y los volúmenes estándar del catálogo.",
                style = Typography.bodySmall, color = PackGuideColors.secondaryText)
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/ScannerScreen.kt`

SHA-256: `2c254d41602290ee4bceab70da35a800cfe25f6dc89737a5cdd74169881eb1ca`

```kotlin
package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.packguide.scanning.ScannerViewModel
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import java.util.Locale

@Composable
fun ScannerScreen(
    modifier: Modifier = Modifier,
    packing: PackingState,
    scanner: ScannerViewModel,
    onOpenCatalog: () -> Unit
) {
    var pendingRemoval by remember { mutableStateOf<LuggagePreset?>(null) }
    val remove = pendingRemoval
    if (remove != null) {
        AlertDialog(
            onDismissRequest = { pendingRemoval = null },
            title = { Text("QUITAR MALETA: ${remove.label}") },
            text = { Text("Se quitarán del plan los artículos asignados a esta maleta. Las demás maletas conservarán sus artículos.") },
            confirmButton = { TextButton(onClick = {
                packing.toggleLuggage(remove)
                scanner.clearPhoto(remove)
                pendingRemoval = null
            }) { Text("QUITAR MALETA") } },
            dismissButton = { TextButton(onClick = { pendingRemoval = null }) { Text("CANCELAR") } }
        )
    }
    Column(modifier.fillMaxSize().background(PackGuideColors.background)) {
        com.example.packguide.ui.components.HudHeader(title = "EQUIPAJE DEL VIAJE", statusText = "")
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                TripLuggageSelection(packing) { type ->
                    if (type in packing.selectedLuggage && packing.bag(type).selectedUnits > 0) {
                        pendingRemoval = type
                    } else {
                        packing.toggleLuggage(type)
                        if (type !in packing.selectedLuggage) scanner.clearPhoto(type)
                    }
                }
            }
            packing.selectedLuggage.forEach { type ->
                item(key = type.name) {
                    val bag = packing.bag(type)
                    TechCard(headerTag = "MALETA: ${type.label}") {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            PhotoScanPanel(scanner, type)
                            Text("MEDIDAS INTERIORES ÚTILES", style = Typography.labelMedium,
                                color = PackGuideColors.primaryText)
                            DimensionStepperRow("ALTO", bag.heightCm.value) {
                                bag.heightCm.value = it; bag.dimensionsConfirmed.value = false
                            }
                            DimensionStepperRow("ANCHO", bag.widthCm.value) {
                                bag.widthCm.value = it; bag.dimensionsConfirmed.value = false
                            }
                            DimensionStepperRow("PROF.", bag.depthCm.value) {
                                bag.depthCm.value = it; bag.dimensionsConfirmed.value = false
                            }
                            Text("${com.example.packguide.ui.localization.localizedText("CAPACIDAD INTERIOR TOTAL", com.example.packguide.ui.localization.LocalAppLanguage.current)}: ${com.example.packguide.ui.localization.formatLiters(bag.capacityL)} L",
                                style = Typography.titleMedium, color = PackGuideColors.primaryText)
                            Text("Una foto común no mide la maleta. Ajusta las medidas interiores útiles y confírmalas.",
                                style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = bag.dimensionsConfirmed.value,
                                    onCheckedChange = { bag.dimensionsConfirmed.value = it })
                                Text("CONFIRMO LAS MEDIDAS: ${type.label}", style = Typography.labelSmall,
                                    color = PackGuideColors.primaryText)
                            }
                        }
                    }
                }
            }
            item {
                Text("En Catálogo podrás repartir los artículos entre las maletas seleccionadas.",
                    style = Typography.bodySmall, color = PackGuideColors.secondaryText)
                Spacer(Modifier.height(12.dp))
                AsciiButton("CONTINUAR AL CATÁLOGO", onClick = onOpenCatalog,
                    enabled = packing.allDimensionsConfirmed, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun DimensionStepperRow(label: String, valueCm: Int, onValueChange: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text("$label:", modifier = Modifier.weight(1f), style = Typography.titleMedium,
            color = PackGuideColors.primaryText)
        androidx.compose.material3.IconButton(onClick = { onValueChange(valueCm - 1) }, enabled = valueCm > 1) {
            Text("−", color = PackGuideColors.primaryText)
        }
        Text("$valueCm CM", style = Typography.titleMedium, color = PackGuideColors.primaryText)
        androidx.compose.material3.IconButton(onClick = { onValueChange(valueCm + 1) }, enabled = valueCm < 300) {
            Text("+", color = PackGuideColors.primaryText)
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/screens/SummaryScreen.kt`

SHA-256: `b6bfecd610f0476bb73343b933e6c169ce56bbaaf76c038014e8091c58a3bd5e`

```kotlin
package com.example.packguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.components.AsciiButton
import com.example.packguide.ui.components.TechCard
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PackGuideColors
import com.example.packguide.ui.theme.Typography
import java.util.Locale
import com.example.packguide.ui.localization.LocalAppLanguage
import com.example.packguide.ui.localization.PackingText
import com.example.packguide.ui.localization.formatLiters

@Composable
fun SummaryScreen(
    packing: PackingState,
    modifier: Modifier = Modifier,
    onNavigateToTab: ((NavTab) -> Unit)? = null,
    preview: com.example.packguide.ai.PackingPreviewViewModel? = null
) {
    val selected = packing.items.filter { it.quantity > 0 }
    val estimate = packing.activeBag.estimate
    val copy = PackingText(LocalAppLanguage.current)

    Column(modifier.fillMaxSize().background(PackGuideColors.background)) {
        com.example.packguide.ui.components.HudHeader(title = "RESUMEN DEL EQUIPAJE", sysCode = "", statusText = "")
        LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                TechCard(headerTag = "TODAS LAS MALETAS") {
                    packing.selectedLuggage.forEach { type ->
                        val bag = packing.bag(type)
                        Text(copy.bagSummary(type.label, bag.estimate, bag.packedUnits, bag.selectedUnits),
                            style = Typography.bodyMedium,
                            color = if (bag.estimate.exceedsCapacity) AccentRedLed else PackGuideColors.primaryText)
                        Spacer(Modifier.height(6.dp))
                    }
                    Text("TOTAL: ${packing.totalSelectedUnits} unidades · ${packing.totalPackedUnits} empacadas",
                        style = Typography.labelMedium, color = PackGuideColors.primaryText)
                }
            }
            item { LuggageSelector(packing) }
            if (preview != null) item { PackingReviewPreviewPanel(packing, preview) }
            item {
                TechCard(headerTag = "MALETA SELECCIONADA", borderColor = PackGuideColors.primaryText) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Luggage, contentDescription = null, tint = AccentRedLed)
                        Text("  ${packing.activeLuggage.label} · ${packing.heightCm.value}×${packing.widthCm.value}×${packing.depthCm.value} cm",
                            color = PackGuideColors.primaryText, style = Typography.titleMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Capacidad calculada con medidas interiores. Verifica las medidas exteriores y las normas de la aerolínea por separado.",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                }
            }
            item {
                TechCard(headerTag = "CAPACIDAD ESTIMADA", borderColor = PackGuideColors.border) {
                    Text("Volumen estimado / capacidad interior total", color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                    Text(copy.volume(estimate),
                        color = PackGuideColors.primaryText, style = Typography.headlineMedium)
                    Text(copy.occupancy(estimate.occupancyPercent), color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                    Spacer(Modifier.height(6.dp))
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { estimate.occupancy.toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = if (estimate.exceedsCapacity) AccentRedLed else PackGuideColors.primaryText,
                        trackColor = PackGuideColors.surfaceVariant,
                        drawStopIndicator = {}
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(copy.balance(estimate), color = if (estimate.exceedsCapacity) AccentRedLed else PackGuideColors.primaryText)
                }
            }
            item {
                TechCard(headerTag = "ESTADO DE EMPACADO", borderColor = PackGuideColors.border) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = AccentGreenLed)
                        Text("  ${packing.packedUnits} empacados", color = PackGuideColors.primaryText)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.RadioButtonUnchecked, contentDescription = null,
                            tint = PackGuideColors.secondaryText)
                        Text("  ${packing.selectedUnits - packing.packedUnits} pendientes", color = PackGuideColors.primaryText)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("${selected.size} tipos de artículos · ${packing.selectedUnits} unidades seleccionadas",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                }
            }
            item {
                Text("DETALLE POR ARTÍCULO", style = Typography.titleLarge,
                    color = PackGuideColors.primaryText)
            }
            item {
                TechCard(headerTag = "DISTRIBUCIÓN POR ZONA", borderColor = PackGuideColors.border) {
                    PackingZone.entries.forEach { zone ->
                        val zoneItems = selected.filter { it.zone == zone }
                        val units = zoneItems.sumOf { it.quantity }
                        val volume = zoneItems.sumOf { it.totalVolumeL }
                        Text(copy.zone(zone.label, units, volume),
                            color = PackGuideColors.primaryText, style = Typography.bodyMedium)
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
            if (selected.isEmpty()) {
                item { Text("No hay artículos seleccionados.", color = PackGuideColors.secondaryText) }
            }
            items(selected, key = { it.id }) { item ->
                TechCard(borderColor = PackGuideColors.border) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(item.icon, contentDescription = null, tint = PackGuideColors.primaryText)
                        Text("  ${item.name}", modifier = Modifier.weight(1f),
                            color = PackGuideColors.primaryText, style = Typography.titleMedium)
                        Text("${item.packedQuantity}/${item.quantity}", color = PackGuideColors.primaryText)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("${item.zone.label} · ${formatLiters(item.totalVolumeL)} L",
                        color = PackGuideColors.secondaryText, style = Typography.bodySmall)
                }
            }
            item {
                AsciiButton("EDITAR CATÁLOGO", onClick = { onNavigateToTab?.invoke(NavTab.Catalog) },
                    modifier = Modifier.fillMaxWidth(), isPrimary = false,
                    icon = Icons.Outlined.Inventory2)
                Spacer(Modifier.height(8.dp))
                AsciiButton("CONTINUAR EMPACANDO", onClick = { onNavigateToTab?.invoke(NavTab.Guide) },
                    modifier = Modifier.fillMaxWidth(), isPrimary = true)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/theme/Color.kt`

SHA-256: `2fece96483d14abfde6ad210264bfc1ed91cd29040c7101c5f757747c9b8273c`

```kotlin
package com.example.packguide.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Nothing OS / Tech ASCII Palette
val PrimaryBlack = Color(0xFF191919)
val BackgroundLight = Color(0xFFF3F2EE)
val CardSurfaceLight = Color(0xFFFFFEFA)
val AccentRedLed = Color(0xFFD63830)
val AccentGreenLed = Color(0xFF54816A)
val SecondarySlate = Color(0xFF686862)
val BorderSlate = Color(0xFFDEDED7)
val SurfaceVariantLight = Color(0xFFEAE9E3)

@Immutable
data class TechExtendedColors(
    val primaryBlack: Color = PrimaryBlack,
    val backgroundLight: Color = BackgroundLight,
    val cardSurfaceLight: Color = CardSurfaceLight,
    val accentRedLed: Color = AccentRedLed,
    val accentGreenLed: Color = AccentGreenLed,
    val secondarySlate: Color = SecondarySlate,
    val borderSlate: Color = BorderSlate,
    val surfaceVariantLight: Color = SurfaceVariantLight
)

val LocalTechExtendedColors = staticCompositionLocalOf { TechExtendedColors() }
```

### Fuente: `app/src/main/java/com/example/packguide/ui/theme/PackGuideColors.kt`

SHA-256: `9bca0ca4165d294665a229f80c7a4b230f4a95b6cb4f54e7012c924ba0d72bec`

```kotlin
package com.example.packguide.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object PackGuideColors {
    val background: Color @Composable get() = MaterialTheme.colorScheme.background
    val surface: Color @Composable get() = MaterialTheme.colorScheme.surface
    val surfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val primaryText: Color @Composable get() = MaterialTheme.colorScheme.onBackground
    val secondaryText: Color @Composable get() = MaterialTheme.colorScheme.secondary
    val border: Color @Composable get() = MaterialTheme.colorScheme.outlineVariant
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/theme/Theme.kt`

SHA-256: `930bec22723971fb5d555486e263793e598f7d8ba43db49df09443d90e20507a`

```kotlin
package com.example.packguide.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val TechLightColorScheme = lightColorScheme(
    primary = PrimaryBlack,
    onPrimary = CardSurfaceLight,
    primaryContainer = SurfaceVariantLight,
    onPrimaryContainer = PrimaryBlack,
    secondary = SecondarySlate,
    onSecondary = CardSurfaceLight,
    tertiary = AccentRedLed,
    onTertiary = CardSurfaceLight,
    background = BackgroundLight,
    onBackground = PrimaryBlack,
    surface = CardSurfaceLight,
    onSurface = PrimaryBlack,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = PrimaryBlack,
    outline = SecondarySlate,
    outlineVariant = BorderSlate,
    error = AccentRedLed,
    onError = CardSurfaceLight
)

private val TechDarkColorScheme = darkColorScheme(
    primary = CardSurfaceLight,
    onPrimary = PrimaryBlack,
    primaryContainer = PrimaryBlack,
    onPrimaryContainer = CardSurfaceLight,
    secondary = Color(0xFFB2B1AA),
    onSecondary = PrimaryBlack,
    tertiary = AccentRedLed,
    onTertiary = CardSurfaceLight,
    background = Color(0xFF101110),
    onBackground = CardSurfaceLight,
    surface = Color(0xFF1B1C1A),
    onSurface = CardSurfaceLight,
    surfaceVariant = Color(0xFF282925),
    onSurfaceVariant = CardSurfaceLight,
    outline = Color(0xFF8C8D86),
    outlineVariant = Color(0xFF393A35),
    error = AccentRedLed,
    onError = CardSurfaceLight
)

object TechTheme {
    val colors: TechExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalTechExtendedColors.current
}

@Composable
fun PackGuideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) TechDarkColorScheme else TechLightColorScheme
    val techExtendedColors = TechExtendedColors()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalTechExtendedColors provides techExtendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
```

### Fuente: `app/src/main/java/com/example/packguide/ui/theme/Type.kt`

SHA-256: `5090219a298e22eb31acfc66f700d664eeab019298615cbc12f23c7504f5683c`

```kotlin
package com.example.packguide.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val TechMonospaceFontFamily = FontFamily.Monospace
val TechSansFontFamily = FontFamily.SansSerif

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    titleSmall = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = TechSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp
    ),
    titleLarge = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = TechSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = TechSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = TechMonospaceFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.8.sp
    )
)
```

### Fuente: `app/src/main/res/drawable/ic_launcher_background.xml`

SHA-256: `38381d0d0a64770d992c98e5b5e492555690af40e2fc6ee59e5a1c2a7c0fdcde`

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#3DDC84"
        android:pathData="M0,0h108v108h-108z" />
    <path
        android:fillColor="#00000000"
        android:pathData="M9,0L9,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,0L19,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,0L29,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,0L39,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,0L49,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,0L59,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,0L69,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,0L79,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M89,0L89,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M99,0L99,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,9L108,9"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,19L108,19"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,29L108,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,39L108,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,49L108,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,59L108,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,69L108,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,79L108,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,89L108,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,99L108,99"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,29L89,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,39L89,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,49L89,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,59L89,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,69L89,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,79L89,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,19L29,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,19L39,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,19L49,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,19L59,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,19L69,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,19L79,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
</vector>
```

### Fuente: `app/src/main/res/drawable/ic_launcher_foreground.xml`

SHA-256: `6e3c1bb64d9bec079cf0030d867c58b064d2fb19ca6c5a691543c43134f64a2a`

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path android:pathData="M31,63.928c0,0 6.4,-11 12.1,-13.1c7.2,-2.6 26,-1.4 26,-1.4l38.1,38.1L107,108.928l-32,-1L31,63.928z">
        <aapt:attr name="android:fillColor">
            <gradient
                android:endX="85.84757"
                android:endY="92.4963"
                android:startX="42.9492"
                android:startY="49.59793"
                android:type="linear">
                <item
                    android:color="#44000000"
                    android:offset="0.0" />
                <item
                    android:color="#00000000"
                    android:offset="1.0" />
            </gradient>
        </aapt:attr>
    </path>
    <path
        android:fillColor="#FFFFFF"
        android:fillType="nonZero"
        android:pathData="M65.3,45.828l3.8,-6.6c0.2,-0.4 0.1,-0.9 -0.3,-1.1c-0.4,-0.2 -0.9,-0.1 -1.1,0.3l-3.9,6.7c-6.3,-2.8 -13.4,-2.8 -19.7,0l-3.9,-6.7c-0.2,-0.4 -0.7,-0.5 -1.1,-0.3C38.8,38.328 38.7,38.828 38.9,39.228l3.8,6.6C36.2,49.428 31.7,56.028 31,63.928h46C76.3,56.028 71.8,49.428 65.3,45.828zM43.4,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2c-0.3,-0.7 -0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C45.3,56.528 44.5,57.328 43.4,57.328L43.4,57.328zM64.6,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2s-0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C66.5,56.528 65.6,57.328 64.6,57.328L64.6,57.328z"
        android:strokeWidth="1"
        android:strokeColor="#00000000" />
</vector>
```

### Fuente: `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`

SHA-256: `e7c073fbec1a51a73f678b266a1cb207aa6468e3ea1504d24f43591f31d2b7c5`

```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

### Fuente: `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`

SHA-256: `e7c073fbec1a51a73f678b266a1cb207aa6468e3ea1504d24f43591f31d2b7c5`

```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

### Fuente: `app/src/main/res/values/colors.xml`

SHA-256: `a5212e69869eb6d4202c2333c2c6a1e90475936a29a4006420184aa0f8532829`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
</resources>
```

### Fuente: `app/src/main/res/values/strings.xml`

SHA-256: `0b512098261bc6bbe4277ccfd39746eb4dd2e5c2baede48e20c8729bd3ffe574`

```xml
<resources>
    <string name="app_name">PackGuide</string>
</resources>
```

### Fuente: `app/src/main/res/values/themes.xml`

SHA-256: `770768c14d3c4e2bbfe0d63ce1493b820bed20c94001e4bfcf759e225aa952b3`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <style name="Theme.PackGuide" parent="android:Theme.Material.Light.NoActionBar" />
</resources>
```

### Fuente: `app/src/main/res/xml/backup_rules.xml`

SHA-256: `facaaef2162be6dd86544a819278042729ed481c38933a410651b81424da7ea7`

```xml
<?xml version="1.0" encoding="utf-8"?><!--
   Sample backup rules file; uncomment and customize as necessary.
   See https://developer.android.com/guide/topics/data/autobackup
   for details.
   Note: This file is ignored for devices older than API 31
   See https://developer.android.com/about/versions/12/backup-restore
-->
<full-backup-content>
    <!--
   <include domain="sharedpref" path="."/>
   <exclude domain="sharedpref" path="device.xml"/>
-->
</full-backup-content>
```

### Fuente: `app/src/main/res/xml/data_extraction_rules.xml`

SHA-256: `940f48fb2ba4d9ec5a55671126796fefa71661c0a5d7b434dc1e9be23da41690`

```xml
<?xml version="1.0" encoding="utf-8"?><!--
   Sample data extraction rules file; uncomment and customize as necessary.
   See https://developer.android.com/about/versions/12/backup-restore#xml-changes
   for details.
-->
<data-extraction-rules>
    <cloud-backup>
        <!-- TODO: Use <include> and <exclude> to control what is backed up.
        <include .../>
        <exclude .../>
        -->
    </cloud-backup>
    <!--
    <device-transfer>
        <include .../>
        <exclude .../>
    </device-transfer>
    -->
</data-extraction-rules>
```

### Fuente: `build.gradle.kts`

SHA-256: `3e0d4a556ad93feeb2880ec8b5f48eb8cb1dadcacc556a102173345895a30613`

```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}
```

### Fuente: `gradle/libs.versions.toml`

SHA-256: `0b9a346f0e2c1f4e1968d02fc301fbdf779b49f5490342b7f2cbb949170d90b6`

```toml
[versions]
agp = "9.4.1"
coreKtx = "1.10.1"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.10.0"
lifecycleViewmodelCompose = "2.10.0"
lifecycleRuntimeCompose = "2.10.0"
activityCompose = "1.13.0"
kotlin = "2.2.10"
composeBom = "2026.05.01"
googleDevtoolsKsp = "2.3.8"
roomRuntime = "2.8.4"
roomKtx = "2.8.4"
roomCompiler = "2.8.4"
kotlinxCoroutinesTest = "1.11.0"
runner = "1.7.0"
kotlinxCoroutinesAndroid = "1.11.0"
kotlinxCoroutinesCore = "1.11.0"
datastorePreferences = "1.2.1"

[libraries]
androidx-exifinterface = { module = "androidx.exifinterface:exifinterface", version = "1.4.2" }
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeCompose" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "roomRuntime" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "roomKtx" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "roomCompiler" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "kotlinxCoroutinesTest" }
androidx-runner = { group = "androidx.test", name = "runner", version.ref = "runner" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-material-icons-core = { group = "androidx.compose.material", name = "material-icons-core" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "kotlinxCoroutinesAndroid" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "kotlinxCoroutinesCore" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastorePreferences" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
google-devtools-ksp = { id = "com.google.devtools.ksp", version.ref = "googleDevtoolsKsp" }
```

### Fuente: `gradle/wrapper/gradle-wrapper.properties`

SHA-256: `7f401adb53bc2f3e4e25a21e0a555faca41a5809b9e2947875459746c8f5d2a8`

```properties
#Tue Oct 06 19:44:46 GMT-05:00 2026
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionSha256Sum=bbaeb2fef8710818cf0e261201dab964c572f92b942812df0c3620d62a529a01
distributionUrl=https\://services.gradle.org/distributions/gradle-9.6.0-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

### Fuente: `settings.gradle.kts`

SHA-256: `813be99c82c7381daa076f9938ee1a50655f278ce62c956c05ae20f4f64b21c3`

```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PackGuide"
include(":app")
```

<!-- PACKGUIDE_SOURCE_SNAPSHOT_END -->
