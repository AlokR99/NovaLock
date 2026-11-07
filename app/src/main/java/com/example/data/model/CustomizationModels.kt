package com.example.data.model

enum class ModuleCategory(val displayName: String, val description: String) {
    MAKE_UP("Make Up", "Appearance & System Styling"),
    LIFE_UP("Life Up", "Productivity & Advanced Tools")
}

enum class CompatibilityLevel(val label: String, val badgeColorHex: Long) {
    FULLY_SUPPORTED("Full Android API", 0xFF12B76A),
    PARTIALLY_SUPPORTED("Supported via Intent/Picker", 0xFFF79009),
    PREVIEW_STUDIO("Design & In-App Studio", 0xFF0381FE)
}

enum class ModuleId(
    val title: String,
    val subtitle: String,
    val category: ModuleCategory,
    val compatibility: CompatibilityLevel,
    val compatibilityNote: String,
    val description: String
) {
    // MAKE UP (Appearance)
    LOCK_STAR(
        "LockStar",
        "Lock Screen Studio",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Uses WallpaperManager and System Widget APIs for lockscreen personalization without bypassing lock security.",
        "Customize digital and analog lock screen clocks, placement, custom stickers, widgets, and lockscreen wallpapers."
    ),
    QUICK_STAR(
        "QuickStar",
        "Quick Settings & Tiles",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.PREVIEW_STUDIO,
        "System UI overlays are restricted on Android. Provides a real-time theme palette studio and in-app network monitor.",
        "Personalize quick tile palettes, layout density, brightness slider styling, and live status bar network indicators."
    ),
    THEME_PARK(
        "Theme Park",
        "Complete Theme Engine",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Extracts dynamic colors from wallpapers and applies Material You color schemes across the app and launcher.",
        "Create custom themes from wallpapers, configure primary & accent colors, icon shapes, and typography."
    ),
    HOME_UP(
        "Home Up",
        "Home Screen & Launcher",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Runs as a customizable Android Launcher with full app drawer, grid adjustments, and icon sizing.",
        "Fine-tune home grid dimensions, app drawer layouts, icon scales, label visibility, and custom folders."
    ),
    NAV_STAR(
        "NavStar",
        "Navigation Bar & Gestures",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.PARTIALLY_SUPPORTED,
        "Provides interactive gesture demonstrations, one-handed shortcuts, and custom navigation bar button previews.",
        "Customize navigation button styles, gesture pill visuals, and learn one-handed navigation shortcuts."
    ),
    CLOCK_FACE(
        "ClockFace",
        "Clock Studio",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Renders dynamic analog and digital clocks with customizable fonts, gradients, and date formats.",
        "Design bespoke analog, digital, minimalist, and gradient clocks with live animated second hands."
    ),
    WONDERLAND(
        "Wonderland",
        "Live Wallpaper Studio",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Applies generated live gradient and particle wallpapers directly to Home or Lock screen via WallpaperManager.",
        "Craft animated wallpapers with 3D parallax tilt, particle motion, layered effects, and gradient generators."
    ),
    KEYS_CAFE(
        "Keys Cafe",
        "Keyboard Theme Studio",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.PREVIEW_STUDIO,
        "Works offline with 0 telemetry. Preview keyboard themes, key shapes, animated ripples, and haptic audio feedback.",
        "Design custom keyboard colorways, key shapes, keypress ripple effects, and haptic feedback sounds."
    ),
    EDGE_LIGHTING(
        "Edge Lighting+",
        "Perimeter Glow Studio",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Renders animated neon waterfall and pulse edge lighting around the screen edges for user notifications.",
        "Configure glowing screen border effects, pulsating neon animations, and test full-screen lighting triggers."
    ),
    PENTASTIC(
        "Pentastic",
        "Stylus & Pointer Themes",
        ModuleCategory.MAKE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Customizes on-screen pointer cursor icons (Crosshair, Cyber Star, Arrow) for mice, trackpads, and styluses.",
        "Personalize pointer icons, custom cursor colors, pointer size, and interactive stylus gesture tutorials."
    ),

    // LIFE UP (Productivity)
    SOUND_ASSISTANT(
        "Sound Assistant",
        "Audio & Equalizer Studio",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Utilizes Android AudioManager and Vibrator APIs for volume curves, equalizer presets, and ringtone selection.",
        "Adjust per-channel volume previews, 5-band equalizer presets, audio sound profiles, and custom vibration haptics."
    ),
    MULTI_STAR(
        "MultiStar",
        "Multitasking & PIP",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Demonstrates split-screen shortcuts, picture-in-picture video floating player, and foldable multi-pane previews.",
        "Master split-screen app pairing, floating PIP window mode, and foldable multi-window layouts."
    ),
    NOTI_STAR(
        "NotiStar",
        "Privacy Notification Hub",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Privacy-first: Only manages notifications created by this app. Never reads or inspects third-party apps.",
        "Organize custom user-created reminders, urgent alerts, and scheduled notifications with custom categories."
    ),
    REGI_STAR(
        "RegiStar",
        "Shortcuts & Quick Actions",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Launches standard Android settings intents (Display, Battery, Sound, Apps, Security) and back-tap simulator.",
        "Create custom quick action cards and fast-launch Android system configuration screens."
    ),
    ONE_HAND(
        "One Hand Operation+",
        "Gesture Navigation",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Interactive gesture zones for easy one-handed smartphone ergonomics without invasive accessibility hooks.",
        "Configure left and right edge swipe triggers for quick access to actions, tools, and one-handed reach."
    ),
    NICE_SHOT(
        "Nice Shot",
        "Screenshot Studio",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Full local image editor: crop, draw, annotate, text sticker, and manual privacy blur brush.",
        "Capture and edit images with pen tools, rectangle highlights, text stamps, and sensitive data blur."
    ),
    NICE_CATCH(
        "Nice Catch",
        "Device Activity & Vitals",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Polls public Android BatteryManager, StatFs, and ActivityManager APIs with 0 telemetry or tracking.",
        "Inspect real-time battery health, temperature, charging status, internal storage usage, and RAM allocation."
    ),
    EDGE_TOUCH(
        "Edge Touch",
        "Touch Rejection Visualizer",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Visualizes dead zones and grip zones along screen edges to prevent accidental palm and thumb touches.",
        "Adjust edge sensitivity margins, dead touch zones, and test grip rejection on curved and flat displays."
    ),
    GAME_BOOSTER(
        "Game Booster+",
        "Gaming Performance Hub",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Tracks gaming sessions, monitors frame rate performance, thermal status, and provides Do-Not-Disturb shortcuts.",
        "Track gameplay session durations, simulate frame rate stability, monitor temperature, and launch focus modes."
    ),
    DISPLAY_ASSISTANT(
        "Display Assistant",
        "Display & Color Tuner",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Inspects screen refresh rate (Hz), display DPI density, and provides reading blue-light filter overlay.",
        "Check screen refresh rates, display scaling, wallpaper color extraction, and test blue-light comfort filters."
    ),
    ROUTINE_PLUS(
        "Routine+",
        "Local Automation Studio",
        ModuleCategory.LIFE_UP,
        CompatibilityLevel.FULLY_SUPPORTED,
        "Configures time and battery triggered automation routines using standard Android notification and alert APIs.",
        "Create IF-THEN routines for low battery alerts, evening quiet hours, wallpaper switching, and daily reminders."
    )
}

data class ModuleItem(
    val id: ModuleId,
    val isEnabled: Boolean = true,
    val isFavorite: Boolean = false,
    val lastUsedTimestamp: Long = 0L
)

// Module Configurations
data class LockStarConfig(
    val clockStyle: String = "Stacked Modern", // Stacked Modern, Bold Digital, Minimalist Analog, Cyberpunk, Circular
    val clockColorHex: String = "#0381FE",
    val clockScale: Float = 1.0f,
    val is24Hour: Boolean = true,
    val stickerText: String = "✨ Make Every Moment Count",
    val showBatteryBadge: Boolean = true
)

data class QuickStarConfig(
    val themeColorHex: String = "#0381FE",
    val tileRows: Int = 3,
    val tileCols: Int = 3,
    val showNetworkSpeed: Boolean = true,
    val roundedCornersDp: Int = 16,
    val activeTileBrightness: Float = 0.95f
)

data class ThemeParkConfig(
    val themeName: String = "Nova Royal",
    val primaryColorHex: String = "#0381FE",
    val accentColorHex: String = "#7A5AF8",
    val surfaceColorHex: String = "#181A24",
    val iconShape: String = "Squircle", // Squircle, Circle, Teardrop, RoundedRect
    val fontScale: Float = 1.0f
)

data class HomeUpConfig(
    val gridRows: Int = 5,
    val gridCols: Int = 4,
    val iconScale: Float = 1.0f,
    val showLabels: Boolean = true,
    val appDrawerStyle: String = "Vertical Grid", // Vertical Grid, Horizontal Pages, Categorized
    val dockAppCount: Int = 5
)

data class NavStarConfig(
    val buttonStyle: String = "OneUI Minimal", // OneUI Minimal, Classic Galaxy, Cyberpunk, Geometric Lines
    val navBarColorHex: String = "#0F1016",
    val gesturePillWidthDp: Int = 72,
    val gestureSensitivity: Float = 0.8f
)

data class ClockFaceConfig(
    val clockType: String = "Dual Gradient Digital", // Dual Gradient Digital, Bauhaus Analog, Neon Glow, Typography Big
    val primaryColorHex: String = "#0381FE",
    val secondaryColorHex: String = "#06AED4",
    val showSeconds: Boolean = true,
    val showDate: Boolean = true,
    val customFontLabel: String = "Default Modern"
)

data class WonderlandConfig(
    val effectStyle: String = "Cosmic Neon Nebula", // Cosmic Neon Nebula, Aurora Flow, Liquid Gradient, Floating Bubbles
    val particleCount: Int = 35,
    val animationSpeed: Float = 1.0f,
    val parallaxEnabled: Boolean = true,
    val gradientStartHex: String = "#0381FE",
    val gradientEndHex: String = "#7A5AF8"
)

data class KeysCafeConfig(
    val themeName: String = "Cyberpunk Blue", // Cyberpunk Blue, Cotton Candy, AMOLED Dark, Retro Mint
    val keyShape: String = "Rounded Squircle", // Rounded Squircle, Soft Pill, Sharp Modern
    val keySoundPreset: String = "Mechanical Thock", // Mechanical Thock, Modern Click, Water Drop, Silent
    val hapticIntensity: Float = 0.8f,
    val rippleAnimation: Boolean = true
)

data class EdgeLightingConfig(
    val effectStyle: String = "Neon Waterfall", // Neon Waterfall, Pulsing Glow, Cosmic Trail, Dual Corner Wave
    val glowColorHex: String = "#06AED4",
    val animationDurationSeconds: Int = 3,
    val strokeWidthDp: Int = 8
)

data class PentasticConfig(
    val pointerStyle: String = "Cyber Star", // Cyber Star, Precision Crosshair, Elegant Arrow, Neon Dot
    val pointerColorHex: String = "#0381FE",
    val cursorScale: Float = 1.0f,
    val trailEffectEnabled: Boolean = true
)

data class SoundAssistantConfig(
    val mediaVolumePercent: Int = 65,
    val ringVolumePercent: Int = 80,
    val notifVolumePercent: Int = 70,
    val eqPreset: String = "Dynamic Boost", // Normal, Bass Boost, Vocal Clear, Dynamic Boost, Rock
    val hapticPattern: String = "Crisp Tap" // Crisp Tap, Double Click, Pulse Wave, Gentle Buzz
)

data class RoutineRule(
    val id: String,
    val name: String,
    val triggerDescription: String,
    val actionDescription: String,
    val isEnabled: Boolean
)
