# Project Plan

Design a complete, high-fidelity 4-screen Android Mobile Application UI for "PackGuide" (a smart luggage optimization and packing assistant) following Nothing OS / Tech ASCII Aesthetic.

STRICT ICONOGRAPHY & TEXT RULE (NO EMOJIS):
- ABSOLUTELY NO EMOJIS anywhere in the system. Use ONLY clean technical ASCII symbols (e.g., [ _ ], [ • ], ->, //, +), vector line icons, or pixelated HUD badges.
- All UI text, buttons, labels in SPANISH.

SYSTEM ART STYLE & DESIGN LANGUAGE (NOTHING OS / TECH ASCII AESTHETIC):
- Nothing Phone UI inspired: NDot typography, tech-industrial HUD, pixel-art metrics, minimal monochrome palette (#0F0F11, #FFFFFF, #F2F4F7, #8A8D93) with bright red (#FF2A2A) and green (#00E676) LED accents.
- Micro-dot matrix background pattern (#F2F4F7) with white elevated rounded container cards (#FFFFFF).
- Monospace / Blocky NDot headers, technical sans-serif body.
- Fixed bottom navigation bar with 4 tabs: [ESCÁNER] [CATÁLOGO] [GUÍA] [RESUMEN].

SCREENS TO IMPLEMENT:
1. "ESCÁNER DE ESPACIO" (AR Luggage Scanner)
   - Header: SYS // PACKING_OS, subheader ESPACIO MAPEO: 100% // LIDAR ACTIVE | FPS: 60, toggle/settings icons.
   - AR Camera Viewport: suitacase frame with red bounding boxes, AR crosshairs, TARGET_LOCK: CARRY_ON, IATA VALID green badge, ANCHO 40 CM, ALTO 55 CM, PROF 20 CM, central floating badge 38L VOLUMEN INTERNO.
   - Mode Switcher: [AUTO] / [MANUAL] segmented toggle.
   - Manual Adjustment Modal/Card: ALTO 55 CM, ANCHO 40 CM, PROF 20 CM, VOLUMEN CALCULADO 44.0L / 44.000 CM³, CABINA IATA OK green badge, CANCELAR and APLICAR MEDIDAS.
   - Bottom Action Button: OPTIMIZAR - PULSA PARA GENERAR DISTRIBUCIÓN with vector scan crosshair icon.

2. "CATÁLOGO DE ÍTEMS" (Inventory & Volume Calculator)
   - Top Bar: NOTHING (R) // SYS CATÁLOGO DE ÍTEMS, Search BUSCAR ÍTEM... [ _ ].
   - Category Filters: [• ROPA] (active red LED), [CALZADO], [ASEO], [TECH], [TODOS].
   - Inventory List with counters [- N +], unit dimensions, unit volume, red subtotal:
     1. CAMISETAS (28x20x4 cm, 1.2L, SUBTOTAL 3.6L, x3)
     2. PANTALONES (38x26x6 cm, 2.8L, SUBTOTAL 5.6L, x2)
     3. CALZADO (32x22x12 cm, 4.5L, SUBTOTAL 4.5L, x1)
     4. CHAQUETA (40x28x8 cm, 3.2L, SUBTOTAL 3.2L, x1)
     5. NECESER (22x14x8 cm, 1.5L, SUBTOTAL 1.5L, x1)
     6. CARGADORES & TECH (18x12x4 cm, 0.8L, SUBTOTAL 0.8L, x1)
   - Bottom Capacity Tracker Card: VOLUMEN USADO 19.2L / 38.0L, 10 segmented blocks progress bar, CAPACIDAD DISPONIBLE: 18.8L green badge, CREAR PLAN DE EMPACADO -> button.

3. "GUÍA DE EMPACADO" (Folding Method & 2D Layer Allocation)
   - Step tracking header: NOTHING // VALIJA AR - PASO 3: DOBLAR CAMISETAS, Step 03 / 05 progress bar.
   - Instruction Card: MÉTODO: RANGER ROLL (-35% VOL).
   - 3-Step Diagram Card (Fase 01 BASE Y DOBLADILLO, Fase 02 PLIEGUE AL CENTRO, Fase 03 ENROLLADO Y BLOQUEO).
   - 2D Suitcase Layer Allocation Grid Card: Blueprint with 2D slots [CALZADO], [TECH PACK], [NECESER], active green target slot [• TARGET ACTIVO x4 - ZONA INF. IZQUIERDA (EQUILIBRIO PESO)]. Metrics row: CAPACIDAD 4.2L, ESTABILIDAD ÓPTIMA, DISTRIBUCIÓN BASE INF.
   - Navigation: [ MARCAR EMPACADO (3/6) ], <- ANTERIOR, SIGUIENTE ->.

4. "RESUMEN FINAL" (Luggage Diagnostic & Trip Export)
   - Header: RESUMEN FINAL with theme toggle.
   - Blueprint Card: MAPA DE DISTRIBUCIÓN // 38L - VOL: 38.0 L with layered compartments tech/camera, camisetas, pantalones, base calzado and legend.
   - AR LiDAR Confirmation Card: CAM-01 TOP-DOWN REALTIME, LIDAR SURFACE MAPPING, BOUNDING BOX PASS, MATCH 100% OK.
   - Efficiency Metrics Card: EFICIENCIA DE VOLUMEN: 92%, PESO TOTAL 9.8 KG (ESTADO: ÓPTIMO CABINA), ESPACIO LIBRE 8% (+3.0L VOL RESIDUAL).
   - Actions: [ GUARDAR CONFIGURACIÓN VIAJE ], [ REORGANIZAR EQUIPAJE ].
   - Footer: ••• NOTHING (R) OS // PACK-ID #NT-38-04.

## Project Brief

# Project Brief: PackGuide

## Features
1. **AR Luggage Space Scanner**: Scans suitcase dimensions (width, height, depth) using camera viewport overlay, locks carry-on targets, calculates internal volume, and provides interactive manual/auto adjustment modals with IATA cabin allowance validation.
2. **Item Inventory & Volume Calculator**: Manages item quantities, dimensions, and subtotal volumes across categories (`[• ROPA]`, `[CALZADO]`, `[ASEO]`, `[TECH]`, `[TODOS]`), with a real-time capacity tracking progress bar.
3. **Step-by-Step Packing & 2D Layer Allocation Guide**: Guides users through space-saving folding techniques (e.g., Ranger Roll) and visually places items on a 2D luggage allocation blueprint grid for optimal weight distribution and stability.
4. **Luggage Diagnostic & Summary**: Generates a distribution map blueprint, performs volume efficiency checks (e.g., 92% efficiency), verifies total weight against cabin limits, and allows saving or reorganizing the trip configuration.

## High-Level Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (using custom Nothing OS / Tech ASCII design system tokens, monochrome palette, and NDot / Monospace typography)
- **Navigation**: **Jetpack Navigation 3** (state-driven navigation)
- **Adaptive Layouts**: **Compose Material Adaptive** library for multi-pane responsive layouts
- **Asynchronous Execution & State Management**: Kotlin Coroutines, Flow, and ViewModel
- **Graphics Rendering**: Compose Canvas API & Vector Graphics for 2D blueprint grids, crosshairs, and dot-matrix HUD layers

## Implementation Steps
**Total Duration:** 1h 52m 59s

### Task_1_DesignSystemAndNavigation: Build Nothing OS / Tech ASCII design system tokens (colors, typography, HUD controls) and set up 4-tab bottom navigation ([ESCÁNER], [CATÁLOGO], [GUÍA], [RESUMEN]).
- **Status:** COMPLETED
- **Updates:** Design system tokens, custom Tech/Nothing OS HUD components, and stateful 4-tab bottom navigation [ESCÁNER], [CATÁLOGO], [GUÍA], [RESUMEN] built in Spanish with zero emojis. Build verified successfully.
- **Acceptance Criteria:**
  - Monochrome theme with red/green LED accents created
  - NDot/Monospace technical typography and HUD styling implemented in Spanish without emojis
  - Bottom navigation with 4 tabs implemented
  - Build pass
- **Duration:** 22m 51s

### Task_2_LuggageScannerAndCatalogScreens: Implement Screen 1: 'ESCÁNER DE ESPACIO' (AR camera overlay viewport, dimension controls, IATA validation modal) and Screen 2: 'CATÁLOGO DE ÍTEMS' (category tabs, volume calculator, capacity progress bar).
- **Status:** COMPLETED
- **Updates:** Implemented Screen 1 ESCÁNER DE ESPACIO (AR luggage scanner viewport, red bounding box overlay, crosshairs, dimension callouts, 38L internal volume badge, mode switcher AUTO/MANUAL, manual adjustment modal card with IATA validation, action button) and Screen 2 CATÁLOGO DE ÍTEMS (search bar, category filter chips with red LED on ROPA, 6 item inventory cards with [- qty +] counter buttons, dynamic subtotal calculations, capacity tracker card with 10 segmented blocks and CREAR PLAN DE EMPACADO button). All in Spanish, zero emojis. Build verified successfully.
- **Acceptance Criteria:**
  - Screen 1 ESCÁNER DE ESPACIO shows camera viewport overlay with HUD dimensions and IATA cabin allowance validation
  - Screen 2 CATÁLOGO DE ÍTEMS features category filters, volume calculations, and capacity tracking bar
  - UI text in Spanish without emojis
  - Build pass
- **Duration:** 5m 30s

### Task_3_PackingGuideAndSummaryScreens: Implement Screen 3: 'GUÍA DE EMPACADO' (Ranger Roll folding guide and 2D layer allocation blueprint grid using Compose Canvas) and Screen 4: 'RESUMEN FINAL' (luggage diagnostic, weight limit check, and trip export).
- **Status:** COMPLETED
- **Updates:** Implemented Screen 3 GUÍA DE EMPACADO (Ranger Roll -35% VOL folding technique step diagram, 2D luggage allocation grid canvas, active slot green highlight, metrics row, action controls) and Screen 4 RESUMEN FINAL (luggage distribution blueprint card with layered compartments, real-time top-down AR LiDAR confirmation card, volume efficiency 92%, weight limit check 9.8 KG ÓPTIMO CABINA, action buttons, Nothing OS footer). Integrated into 4-tab bottom navigation in Spanish without emojis. Build verified successfully.
- **Acceptance Criteria:**
  - Screen 3 GUÍA DE EMPACADO shows folding steps and 2D canvas luggage blueprint layout
  - Screen 4 RESUMEN FINAL displays volume efficiency (e.g. 92%), weight compliance, and export configuration
  - All 4 screens fully integrated into bottom navigation
  - Build pass
- **Duration:** 3m 48s

### Task_4_RunAndVerify: Run and verify application stability, ensure no crashes, confirm alignment with user requirements, and report critical UI issues. Instruct critic_agent to verify app stability and UI alignment.
- **Status:** COMPLETED
- **Updates:** All tasks successfully completed and verified.
- **Acceptance Criteria:**
  - Make sure all existing tests pass
  - Build pass
  - App does not crash
  - All 4 screens align with Nothing OS / Tech ASCII aesthetic without emojis in Spanish
- **Duration:** 1h 20m 50s

