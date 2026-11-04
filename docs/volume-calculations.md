# Medidas, volúmenes y progreso

## Causa y decisión

La capacidad de Cabina era 55 × 35 × 25 / 1000 = **48.125 L** (48.1 L en pantalla).
`PackingRules` descontaba 20 % únicamente en Guía: 38.5 L. Catálogo y Resumen
comparaban los mismos artículos con 48.125 L. Se elimina esa reducción oculta:
las tres pantallas ahora comparan con la capacidad interior total confirmada en
Escáner. La foto no determina dimensiones. El catálogo permite seleccionar la
misma maleta que Guía; su panel inferior indica explícitamente esa selección,
y la lista de destinos conserva los valores de cada maleta por separado.

`CatalogItemData` guardaba dimensiones como texto y un `unitVolumeL` independiente.
Los litros eran supuestos del catálogo, no resultados de multiplicar dimensiones.
No existía un factor de compresión calibrado. Se conservan las 36 referencias y
estimaciones existentes sin inventar medidas ni aplicar una compresión nueva.
Las dimensiones ahora son tres números en `ItemDimensions`; el texto y el volumen
rectangular se derivan de ellos. Cada tarjeta distingue la estimación por unidad
del volumen rectangular de referencia. Este último no interviene en el total
de empaque. Los supuestos pueden diferir por forma, plegado o embalaje, pero no
hay mediciones que permitan atribuir o validar un factor concreto.

Ejemplos: pantalones 38 × 26 × 6 = **5.928 L** rectangulares frente a **2.8 L**
estimados; calzado 32 × 22 × 12 = **8.448 L** rectangulares frente a **4.5 L**
estimados. Se muestran ambos conceptos con etiquetas distintas. Corregir estos
supuestos con mediciones reales requeriría datos nuevos del usuario.

## Fuente de verdad y reglas

- `PackingState` conserva una instancia `LuggagePacking` por maleta. Su `estimate`
  se deriva de las medidas interiores editables y de su inventario actual.
- `PackingRules.rectangularVolumeL`: alto × ancho × fondo / 1000, en cm y L.
- `PackingRules.totalVolumeL`: suma de `unitVolumeL × quantity` de todos los
  artículos asignados, incluidos los que quedan fuera del filtro de búsqueda.
  La suma usa decimales exactos para evitar falsos excesos por error binario;
  los resultados no se redondean antes de comparar o restar.
- Espacio libre = máximo(capacidad − estimación, 0); exceso = máximo(estimación
  − capacidad, 0). La recomendación y las barras observan el mismo resultado.
- Ocupación = volumen estimado / capacidad. El porcentaje puede exceder 100 %;
  solo se recorta la representación de la barra. No es progreso de empaque.
- Progreso = unidades empacadas / unidades seleccionadas; inventario vacío = 0 %.
  Empacar no añade volumen: las unidades empacadas ya pertenecen a las seleccionadas.
  Las cantidades negativas o empacadas por encima de la selección se rechazan.
  La resta en Catálogo reduce también el contador empacado cuando corresponde.
- `PackingText` comparte formato y mensajes ES/EN: punto decimal y un decimal
  para todos los litros. Valores positivos menores de 0.05 L aparecen como
  `<0.1 L` para evitar mostrar un exceso real como cero.
- El inventario de la composición local recibe las mismas medidas de referencia y
  estimaciones independientes, y su copia cambia si cambia el volumen unitario.

## Qué puede reconstruirse de las capturas

El inventario inicial del código selecciona seis tipos: camisetas 3 × 1.2 = 3.6 L,
pantalones 2 × 2.8 = 5.6 L, calzado 1 × 4.5 = 4.5 L, chaqueta 1 × 3.2 = 3.2 L,
neceser 1 × 1.5 = 1.5 L, cargadores y tecnología 1 × 0.8 = 0.8 L.
Suma: **9 unidades y 19.2 L**. Los otros 30 tipos empiezan en cero, pero cualquier
cantidad seleccionada contribuye al total, aunque no aparezca en la vista filtrada.

Las selecciones viven en memoria; no existe un repositorio persistente con las
cantidades de las capturas. Con pantalones 4 y calzado 3 solo se conocen 7 unidades
y 24.7 L. Faltan identificar las otras 11 unidades y 19.4 L; no puede afirmarse
qué artículos exactos componían aquella selección.

La prueba de regresión usa **una combinación posible, no una reconstrucción**:
camisetas 5, pantalones 4, calzado 3, chaquetas 3, neceseres 2 y tecnología 1.
Da 18 unidades y 44.1 L. Con Cabina estándar, las tres pantallas muestran
**44.1 L / 48.1 L** y **ESPACIO EST.: 4.0 L** (resta interna 4.025 L).
Al cambiar sus medidas reales a 44 × 35 × 25 cm, la capacidad pasa a 38.5 L
y las tres muestran **EXCESO EST.: 5.6 L**. No se cambian las medidas de la
maleta por defecto para forzar ese resultado.

## Verificación

Las pruebas unitarias cubren el catálogo completo sin IDs repetidos, ambos
conceptos de volumen, subtotales, la combinación de 18 unidades, capacidad,
espacio/exceso, cambio de medidas y maleta, progreso sin duplicar volumen,
precisión sin redondeo previo y formato/mensajes en español e inglés.
Comando: `gradlew.bat assembleDebug testDebugUnitTest`.

Resultado ejecutado: compilación Debug y **21 pruebas unitarias correctas**.
También se compiló el APK de pruebas y se ejecutaron por separado
`PackingVolumeScreenTest` y `MultiLuggageTest`: **2 pruebas de interfaz correctas**
en `Medium_Phone_API_37.0`, comprobando números al cambiar de pantalla y medidas,
además de cantidades, reparto y navegación entre maletas.

El emulador tenía una app instalada con otra firma. Se ejecutaron las pruebas
en una copia con sufijo `.volumecheck` y salida separada `app/build/volume-check`,
usando el script local `build/packing-volume-tests.init.gradle`. No se eliminó
ni reemplazó la app original. Se comprobaron los XML de resultados: el primer
intento normal falló al instalar, aunque Gradle informó éxito; ese intento no
se contó como prueba ejecutada.

Esto no sustituye una revisión visual exhaustiva en todos los tamaños e idiomas.
Los cálculos son locales. La función de imagen actual también funciona sin
conexión; consulta `packing-ai.md` para su arquitectura y verificación posterior.

## Archivos de esta corrección

- Modelos y reglas: `PackingState.kt`, `scanning/PackingRules.kt`.
- Pantallas: `CatalogScreen.kt`, `CatalogInventoryCard.kt`, `PackingSuggestionPanel.kt`,
  `GuideScreen.kt`, `SummaryScreen.kt`, `ScannerScreen.kt`.
- Formato y traducciones: `ui/localization/PackingText.kt`, `AppText.kt`.
- Inventario de referencia para IA: `ai/PackingPlan.kt`.
- Pruebas: `PackingRulesTest.kt`, `PackingVolumeTest.kt`, `PackingTextTest.kt`,
  `PackingVolumeScreenTest.kt`, `MultiLuggageTest.kt`.
- Documentación: este archivo y `photo-scanning.md`.


## Referencias existentes auditadas

Estas son las referencias originales conservadas; no son mediciones del usuario.

| ID | Artículo | Referencia (cm) | Rectangular (L) | Estimación por unidad (L) |
| --- | --- | --- | --- | --- |
| ITEM-01 | CAMISETAS | 28 × 20 × 4 | 2.240 | 1.2 |
| ITEM-02 | PANTALONES | 38 × 26 × 6 | 5.928 | 2.8 |
| ITEM-03 | CALZADO | 32 × 22 × 12 | 8.448 | 4.5 |
| ITEM-04 | CHAQUETA | 40 × 28 × 8 | 8.960 | 3.2 |
| ITEM-05 | NECESER | 22 × 14 × 8 | 2.464 | 1.5 |
| ITEM-06 | CARGADORES & TECH | 18 × 12 × 4 | 0.864 | 0.8 |
| ITEM-07 | ROPA INTERIOR | 18 × 12 × 3 | 0.648 | 0.3 |
| ITEM-08 | MEDIAS | 15 × 10 × 3 | 0.450 | 0.2 |
| ITEM-09 | PIJAMA | 30 × 22 × 5 | 3.300 | 1.4 |
| ITEM-10 | VESTIDO | 35 × 25 × 5 | 4.375 | 1.8 |
| ITEM-11 | FALDA | 28 × 20 × 4 | 2.240 | 1.0 |
| ITEM-12 | SHORTS | 28 × 20 × 4 | 2.240 | 1.0 |
| ITEM-13 | SUÉTER | 35 × 28 × 7 | 6.860 | 2.3 |
| ITEM-14 | CAMISA | 30 × 23 × 4 | 2.760 | 1.2 |
| ITEM-15 | TRAJE DE BAÑO | 20 × 15 × 2 | 0.600 | 0.3 |
| ITEM-16 | ZAPATOS FORMALES | 34 × 23 × 12 | 9.384 | 5.0 |
| ITEM-17 | SANDALIAS | 28 × 20 × 6 | 3.360 | 2.0 |
| ITEM-18 | PANTUFLAS | 28 × 18 × 7 | 3.528 | 2.1 |
| ITEM-19 | CEPILLO DE DIENTES | 20 × 3 × 3 | 0.180 | 0.1 |
| ITEM-20 | PASTA DENTAL | 16 × 5 × 4 | 0.320 | 0.2 |
| ITEM-21 | DESODORANTE | 14 × 5 × 5 | 0.350 | 0.3 |
| ITEM-22 | CHAMPÚ | 16 × 6 × 6 | 0.576 | 0.6 |
| ITEM-23 | JABÓN | 10 × 7 × 4 | 0.280 | 0.3 |
| ITEM-24 | PROTECTOR SOLAR | 15 × 6 × 5 | 0.450 | 0.5 |
| ITEM-25 | MEDICAMENTOS | 18 × 12 × 5 | 1.080 | 0.8 |
| ITEM-26 | LENTES DE CONTACTO | 12 × 8 × 3 | 0.288 | 0.2 |
| ITEM-27 | TELÉFONO | 16 × 8 × 2 | 0.256 | 0.3 |
| ITEM-28 | TABLETA | 26 × 18 × 2 | 0.936 | 0.7 |
| ITEM-29 | PORTÁTIL | 35 × 25 × 3 | 2.625 | 2.6 |
| ITEM-30 | AURICULARES | 18 × 15 × 8 | 2.160 | 1.0 |
| ITEM-31 | BATERÍA EXTERNA | 15 × 8 × 3 | 0.360 | 0.4 |
| ITEM-32 | ADAPTADOR DE ENCHUFE | 8 × 7 × 5 | 0.280 | 0.3 |
| ITEM-33 | CÁMARA | 18 × 13 × 10 | 2.340 | 1.7 |
| ITEM-34 | TARJETAS DE MEMORIA | 8 × 6 × 2 | 0.096 | 0.1 |
| ITEM-35 | CABLE USB | 12 × 9 × 3 | 0.324 | 0.2 |
| ITEM-36 | RELOJ | 10 × 8 × 5 | 0.400 | 0.4 |
