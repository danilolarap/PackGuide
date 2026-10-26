package com.example.packguide.ui.localization

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text as MaterialText
import androidx.compose.runtime.Composable
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

private fun translateCommonTerms(text: String): String {
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
    return words.entries.sortedByDescending { it.key.length }.fold(text) { result, entry ->
        Regex(Regex.escape(entry.key), RegexOption.IGNORE_CASE).replace(result, entry.value)
    }
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
    MaterialText(
        text = localizedText(text, language), modifier = modifier, color = color,
        fontSize = fontSize, fontStyle = fontStyle, fontWeight = fontWeight,
        fontFamily = fontFamily, letterSpacing = letterSpacing, textDecoration = textDecoration,
        textAlign = textAlign, lineHeight = lineHeight, overflow = overflow, softWrap = softWrap,
        maxLines = maxLines, minLines = minLines, onTextLayout = onTextLayout, style = style
    )
}
