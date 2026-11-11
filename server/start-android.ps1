param([string]$Device, [string]$Adb)

$ErrorActionPreference = 'Stop'
$node = (Get-Command node -ErrorAction Stop).Source
$version = & $node -p 'parseInt(process.versions.node)'
if ($LASTEXITCODE -ne 0 -or [int]$version -lt 22) { throw 'Instala Node.js 22 o posterior.' }

if (-not $Adb) {
    $candidates = @((Get-Command adb -ErrorAction SilentlyContinue).Source)
    foreach ($sdk in @($env:ANDROID_HOME, $env:ANDROID_SDK_ROOT, "$env:LOCALAPPDATA\Android\Sdk")) {
        if ($sdk) { $candidates += Join-Path $sdk 'platform-tools\adb.exe' }
    }
    $Adb = $candidates | Where-Object { $_ -and (Test-Path -LiteralPath $_) } | Select-Object -First 1
}
if (-not $Adb) { throw 'ADB no encontrado. Instala Android SDK Platform-Tools o indica la ruta con -Adb.' }

$deviceArgs = @()
if ($Device) { $deviceArgs = @('-s', $Device) }
& $Adb @deviceArgs get-state
if ($LASTEXITCODE -ne 0) { throw 'Conecta y autoriza un telefono o emulador. Si hay varios, usa -Device con el numero de serie de adb devices.' }

$nodeArgs = @()
$envFile = Join-Path $PSScriptRoot '.env'
if (Test-Path -LiteralPath $envFile) { $nodeArgs += "--env-file=$envFile" }
$previousKey = $env:OPENAI_API_KEY
$previousPort = $env:PORT
try {
    & $node @nodeArgs -e 'process.exit(process.env.OPENAI_API_KEY?.trim() ? 0 : 1)'
    if ($LASTEXITCODE -ne 0) {
        $key = Read-Host 'Clave de la API de OpenAI para esta sesion' -AsSecureString
        if ($key.Length -eq 0) { throw 'Falta la clave de la API de OpenAI.' }
        $buffer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($key)
        try { $env:OPENAI_API_KEY = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($buffer).Trim() }
        finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($buffer); $key.Dispose() }
        if (-not $env:OPENAI_API_KEY) { throw 'Falta la clave de la API de OpenAI.' }
    }

    $env:PORT = '8787'
    & $Adb @deviceArgs reverse 'tcp:8787' 'tcp:8787'
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo conectar el dispositivo con el servidor local.' }
    Write-Host 'Cuando aparezca PackGuide AI listening, abre Resumen, Ver vista previa y Evaluar foto con IA. Deja abierta esta terminal.'
    & $node @nodeArgs (Join-Path $PSScriptRoot 'server.mjs')
    if ($LASTEXITCODE -ne 0) { throw 'El servidor de imagenes no pudo iniciarse o se detuvo con un error.' }
}
finally {
    $env:OPENAI_API_KEY = $previousKey
    $env:PORT = $previousPort
}
