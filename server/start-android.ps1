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
if (-not $Device) {
    $connected = @(& $Adb devices | Where-Object { $_ -match '^\S+\s+device$' } | ForEach-Object { ($_ -split '\s+')[0] })
    $phones = @($connected | Where-Object { $_ -notlike 'emulator-*' })
    if ($phones.Count -eq 1) { $Device = $phones[0] }
    elseif ($connected.Count -eq 1) { $Device = $connected[0] }
    else { throw 'No hay un dispositivo unico. Conecta el telefono o usa -Device con su identificador de adb devices.' }
}
if ($Device) { $deviceArgs = @('-s', $Device) }
& $Adb @deviceArgs get-state
if ($LASTEXITCODE -ne 0) { throw 'Conecta y autoriza un telefono o emulador. Si hay varios, usa -Device con el numero de serie de adb devices.' }

$nodeArgs = @()
$envFile = Join-Path $PSScriptRoot '.env'
if (Test-Path -LiteralPath $envFile) { $nodeArgs += "--env-file=$envFile" }
$previousKey = $env:GEMINI_API_KEY
$previousPort = $env:PORT
$previousProvider = $env:PACKGUIDE_REVIEW_PROVIDER
$connectionMonitor = $null
try {
    & $node @nodeArgs -e 'process.exit(process.env.GEMINI_API_KEY?.trim() ? 0 : 1)'
    if ($LASTEXITCODE -ne 0) {
        $key = Read-Host 'Clave de la API de Google AI Studio (Gemini) para esta sesion' -AsSecureString
        if ($key.Length -eq 0) { throw 'Falta la clave de la API de Google AI Studio (Gemini).' }
        $buffer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($key)
        try { $env:GEMINI_API_KEY = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($buffer).Trim() }
        finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($buffer); $key.Dispose() }
        if (-not $env:GEMINI_API_KEY) { throw 'Falta la clave de la API de Google AI Studio (Gemini).' }
    }

    $env:PORT = '8787'
    $env:PACKGUIDE_REVIEW_PROVIDER = 'gemini'
    & $Adb @deviceArgs reverse 'tcp:8787' 'tcp:8787'
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo conectar el dispositivo con el servidor local.' }
    # ADB removes reverse mappings when the phone reconnects or the daemon restarts.
    # Keep restoring the mapping while the local development service is running.
    $connectionMonitor = Start-Job -ArgumentList $Adb, $Device -ScriptBlock {
        param($adbPath, $serial)
        while ($true) {
            & $adbPath -s $serial reverse 'tcp:8787' 'tcp:8787' 2>$null | Out-Null
            Start-Sleep -Seconds 5
        }
    }
    Write-Host 'Cuando aparezca PackGuide AI listening, abre Resumen, Ver vista previa y Evaluar foto con IA. Deja abierta esta terminal.'
    & $node @nodeArgs (Join-Path $PSScriptRoot 'server.mjs')
    if ($LASTEXITCODE -ne 0) { throw 'El servidor de imagenes no pudo iniciarse o se detuvo con un error.' }
}
finally {
    if ($connectionMonitor) {
        Stop-Job -Job $connectionMonitor -ErrorAction SilentlyContinue
        Remove-Job -Job $connectionMonitor -Force -ErrorAction SilentlyContinue
    }
    $env:GEMINI_API_KEY = $previousKey
    $env:PORT = $previousPort
    $env:PACKGUIDE_REVIEW_PROVIDER = $previousProvider
}
