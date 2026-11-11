# Vista previa y evaluación en Resumen

La vista previa compone localmente la foto original, el inventario seleccionado,
las zonas y las estimaciones de capacidad. Se puede abrir sin conexión y sin
foto. El servidor interviene únicamente cuando el usuario pulsa **Evaluar foto
con IA**. La nota y la sugerencia de ese servicio aparecen en el mismo modal.

## Datos que se envían

- **Evaluar foto con IA** envía únicamente la foto elegida para evaluar el orden,
  el uso aparente del espacio y el acceso a los artículos visibles. La evaluación
  muestra una nota de 1 a 10 y una sugerencia. Si la foto no permite evaluar, se
  indica cómo repetirla y se omite la nota.
- La app actual usa el esquema local para la propuesta visual. El endpoint de
  generación se conserva para clientes antiguos.

Para los clientes antiguos, el endpoint de generación resuelve los nombres desde
su catálogo y solo acepta el campo
`packedQuantity`. No usa `quantity` ni `selectedQuantity` como sustitutos.
La respuesta incluye el manifiesto utilizado, que Android comprueba contra la
solicitud antes de mostrar la imagen. Esto verifica los **datos**, no certifica
el número de objetos representados visualmente por el modelo.

La IA propone una distribución alternativa y puede apartarse de las zonas
elegidas por el usuario. No se calculan puntuaciones ficticias ni se afirma que
la distribución garantice el ajuste físico. La imagen puede representar
cantidades de forma imprecisa; el inventario empacado visible es el dato exacto.
Si cambia ese inventario o el idioma, se solicita una nueva propuesta.

## Servidor local: Node.js 22 o posterior

No necesita paquetes externos.

En Windows, instala la variante Debug en el teléfono o emulador desde Android
Studio, mantén el dispositivo conectado mediante ADB y ejecuta desde la raíz:

```powershell
powershell -ExecutionPolicy Bypass -File .\server\start-android.ps1
```

El comando conecta el puerto del dispositivo y levanta el servidor. Si no
encuentra una clave, la pide con entrada oculta para esa sesión. Déjalo abierto
mientras usas **Resumen → Ver vista previa → Evaluar foto con IA**. Si hay varios dispositivos,
añade `-Device` seguido del identificador que muestra `adb devices`.

Para guardar la configuración localmente o iniciar el servidor de forma manual:

1. Copia `server/.env.example` a `server/.env`.
2. Edita ese archivo **localmente** e introduce tu `OPENAI_API_KEY`.
   No la envíes por chat ni la incluyas en Gradle, Android o el repositorio.
3. Desde la raíz ejecuta:

```powershell
node --env-file=server/.env server/server.mjs
```

El servidor escucha exclusivamente en `127.0.0.1:8787`. Si falta la clave,
`GET /health` devuelve `configured: false` y generar devuelve
`503 NOT_CONFIGURED`; no se fabrica una imagen de reemplazo.

Para un teléfono conectado mediante ADB:

```powershell
adb reverse tcp:8787 tcp:8787
```

El APK Debug usa `http://127.0.0.1:8787`. Para un emulador también puede
compilarse con la propiedad pública
`-PPACKGUIDE_AI_BASE_URL=http://10.0.2.2:8787`.
El tráfico HTTP se admite solo para esas direcciones locales en Debug.

## Generación real y despliegue

El backend llama a la API oficial de imágenes de OpenAI:
[generación de imágenes](https://developers.openai.com/api/docs/guides/image-generation),
mediante `/v1/images/generations`, sin fotos de entrada.
La evaluación usa `/v1/responses` con un modelo con visión configurable mediante
`OPENAI_REVIEW_MODEL` (`gpt-5-mini` por defecto).
El modelo se configura en el servidor mediante `OPENAI_IMAGE_MODEL`;
el valor inicial es `gpt-image-2.5-sunburst`. La cuenta debe tener acceso y
saldo para ese modelo. No se ejecutaron peticiones facturables sin credenciales.

Esta entrega incluye un servidor de desarrollo local, **no un servicio público
desplegado**. Para distribuir la app hay que alojarlo detrás de un gateway HTTPS
con autenticación real de usuarios y configurar
`PACKGUIDE_AI_BASE_URL=https://tu-servicio` al compilar.
El gateway debe dirigir tanto `/v1/packing-image` como `/v1/packing-review` al
servidor privado y usar `Host: localhost` en esa conexión.
La cuenta Room de la app es local y no autentica un servidor remoto.
El cliente permite inyectar un proveedor de tokens de usuario para esa integración;
no debe reemplazarse por una clave del proveedor incrustada en el APK.

El servidor limita tamaño, concurrencia y frecuencia. Acepta los hosts de
desarrollo local y del emulador sin encabezado Origin. No registra fotos, inventarios
ni claves. El cliente no sigue redirecciones y limita el tamaño de las imágenes.
Cancelar desconecta la solicitud; el servidor cancela su llamada al proveedor
si se desconecta el cliente. Una solicitud ya procesada por el proveedor puede
haber consumido recursos aunque se cancele.

## Verificación

```powershell
node --test server/test/packing.test.mjs
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:lintDebug
```

Las pruebas del servidor usan un proveedor simulado y comprueban el caso 12/11,
la exclusión de artículos sin empacar, la solicitud sin fotos, el manifiesto y la
ausencia de generación cuando falta configuración. Las pruebas Android del
cliente comprueban la serialización y rechazan un manifiesto que responda 12
cuando se pidieron 11. Las pruebas de UI usan imágenes de prueba identificadas
como tales; no se presentan como una generación real validada.
