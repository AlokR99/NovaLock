package com.example.data.repository

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

data class DeviceHardwareVitals(
    val batteryPercent: Int = 100,
    val isCharging: Boolean = false,
    val batteryHealth: String = "Good",
    val batteryTemperatureC: Float = 28.5f,
    val totalStorageGb: Long = 128,
    val freeStorageGb: Long = 64,
    val totalRamMb: Long = 8192,
    val availRamMb: Long = 4096,
    val androidVersion: String = "Android 15",
    val securityPatch: String = "2026-05"
)

data class SavedDesign(
    val id: String,
    val title: String,
    val categoryName: String,
    val previewColorHex: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

class CustomizationRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("novalock_settings", Context.MODE_PRIVATE)

    private val _modules = MutableStateFlow<List<ModuleItem>>(emptyList())
    val modules: StateFlow<List<ModuleItem>> = _modules.asStateFlow()

    // Module configs
    private val _lockStarConfig = MutableStateFlow(LockStarConfig())
    val lockStarConfig: StateFlow<LockStarConfig> = _lockStarConfig.asStateFlow()

    private val _quickStarConfig = MutableStateFlow(QuickStarConfig())
    val quickStarConfig: StateFlow<QuickStarConfig> = _quickStarConfig.asStateFlow()

    private val _themeParkConfig = MutableStateFlow(ThemeParkConfig())
    val themeParkConfig: StateFlow<ThemeParkConfig> = _themeParkConfig.asStateFlow()

    private val _homeUpConfig = MutableStateFlow(HomeUpConfig())
    val homeUpConfig: StateFlow<HomeUpConfig> = _homeUpConfig.asStateFlow()

    private val _navStarConfig = MutableStateFlow(NavStarConfig())
    val navStarConfig: StateFlow<NavStarConfig> = _navStarConfig.asStateFlow()

    private val _clockFaceConfig = MutableStateFlow(ClockFaceConfig())
    val clockFaceConfig: StateFlow<ClockFaceConfig> = _clockFaceConfig.asStateFlow()

    private val _wonderlandConfig = MutableStateFlow(WonderlandConfig())
    val wonderlandConfig: StateFlow<WonderlandConfig> = _wonderlandConfig.asStateFlow()

    private val _keysCafeConfig = MutableStateFlow(KeysCafeConfig())
    val keysCafeConfig: StateFlow<KeysCafeConfig> = _keysCafeConfig.asStateFlow()

    private val _edgeLightingConfig = MutableStateFlow(EdgeLightingConfig())
    val edgeLightingConfig: StateFlow<EdgeLightingConfig> = _edgeLightingConfig.asStateFlow()

    private val _pentasticConfig = MutableStateFlow(PentasticConfig())
    val pentasticConfig: StateFlow<PentasticConfig> = _pentasticConfig.asStateFlow()

    private val _soundAssistantConfig = MutableStateFlow(SoundAssistantConfig())
    val soundAssistantConfig: StateFlow<SoundAssistantConfig> = _soundAssistantConfig.asStateFlow()

    private val _routines = MutableStateFlow<List<RoutineRule>>(emptyList())
    val routines: StateFlow<List<RoutineRule>> = _routines.asStateFlow()

    private val _savedDesigns = MutableStateFlow<List<SavedDesign>>(emptyList())
    val savedDesigns: StateFlow<List<SavedDesign>> = _savedDesigns.asStateFlow()

    private val _deviceVitals = MutableStateFlow(DeviceHardwareVitals())
    val deviceVitals: StateFlow<DeviceHardwareVitals> = _deviceVitals.asStateFlow()

    init {
        loadInitialState()
        refreshHardwareVitals()
    }

    private fun loadInitialState() {
        // Load modules state
        val loadedModules = ModuleId.values().map { id ->
            val isEnabled = prefs.getBoolean("mod_${id.name}_enabled", true)
            val isFav = prefs.getBoolean("mod_${id.name}_fav", false)
            val lastUsed = prefs.getLong("mod_${id.name}_last_used", 0L)
            ModuleItem(id = id, isEnabled = isEnabled, isFavorite = isFav, lastUsedTimestamp = lastUsed)
        }
        _modules.value = loadedModules

        // Default initial routines
        _routines.value = listOf(
            RoutineRule("r1", "Low Battery Shield", "Battery falls below 20%", "Activate dark mode & reduce animation scale", true),
            RoutineRule("r2", "Night Quiet Hours", "Time reaches 22:30", "Enable Do-Not-Disturb & mute non-urgent sounds", true),
            RoutineRule("r3", "Morning Atmosphere", "Time reaches 07:00", "Apply Sunrise Gradient live wallpaper", false),
            RoutineRule("r4", "Headphones Connected", "Wired/Bluetooth headset plugged in", "Set media volume to 60% & activate Rock EQ", true)
        )

        // Default initial saved designs
        _savedDesigns.value = listOf(
            SavedDesign("d1", "Cyber Neon Cyberpunk", "Theme Park", "#0381FE", "Cyan primary with purple accent & squircle icons"),
            SavedDesign("d2", "Minimalist Bauhaus Clock", "ClockFace", "#06AED4", "Dual timezone clock with sans-serif typography"),
            SavedDesign("d3", "Cosmic Aurora Borealis", "Wonderland", "#7A5AF8", "Particle stars with slow parallax tilt effect"),
            SavedDesign("d4", "AMOLED Stealth Lock", "LockStar", "#12B76A", "Vertical stacked clock with battery gauge badge")
        )
    }

    fun toggleModuleEnabled(id: ModuleId) {
        _modules.update { list ->
            list.map { item ->
                if (item.id == id) {
                    val newState = !item.isEnabled
                    prefs.edit().putBoolean("mod_${id.name}_enabled", newState).apply()
                    item.copy(isEnabled = newState)
                } else item
            }
        }
    }

    fun toggleModuleFavorite(id: ModuleId) {
        _modules.update { list ->
            list.map { item ->
                if (item.id == id) {
                    val newFav = !item.isFavorite
                    prefs.edit().putBoolean("mod_${id.name}_fav", newFav).apply()
                    item.copy(isFavorite = newFav)
                } else item
            }
        }
    }

    fun recordModuleUsed(id: ModuleId) {
        val now = System.currentTimeMillis()
        prefs.edit().putLong("mod_${id.name}_last_used", now).apply()
        _modules.update { list ->
            list.map { item ->
                if (item.id == id) item.copy(lastUsedTimestamp = now) else item
            }
        }
    }

    fun updateLockStar(update: (LockStarConfig) -> LockStarConfig) {
        _lockStarConfig.update(update)
    }

    fun updateQuickStar(update: (QuickStarConfig) -> QuickStarConfig) {
        _quickStarConfig.update(update)
    }

    fun updateThemePark(update: (ThemeParkConfig) -> ThemeParkConfig) {
        _themeParkConfig.update(update)
    }

    fun updateHomeUp(update: (HomeUpConfig) -> HomeUpConfig) {
        _homeUpConfig.update(update)
    }

    fun updateNavStar(update: (NavStarConfig) -> NavStarConfig) {
        _navStarConfig.update(update)
    }

    fun updateClockFace(update: (ClockFaceConfig) -> ClockFaceConfig) {
        _clockFaceConfig.update(update)
    }

    fun updateWonderland(update: (WonderlandConfig) -> WonderlandConfig) {
        _wonderlandConfig.update(update)
    }

    fun updateKeysCafe(update: (KeysCafeConfig) -> KeysCafeConfig) {
        _keysCafeConfig.update(update)
    }

    fun updateEdgeLighting(update: (EdgeLightingConfig) -> EdgeLightingConfig) {
        _edgeLightingConfig.update(update)
    }

    fun updatePentastic(update: (PentasticConfig) -> PentasticConfig) {
        _pentasticConfig.update(update)
    }

    fun updateSoundAssistant(update: (SoundAssistantConfig) -> SoundAssistantConfig) {
        _soundAssistantConfig.update(update)
    }

    fun toggleRoutine(routineId: String) {
        _routines.update { list ->
            list.map { rule ->
                if (rule.id == routineId) rule.copy(isEnabled = !rule.isEnabled) else rule
            }
        }
    }

    fun addRoutine(name: String, trigger: String, action: String) {
        val newRule = RoutineRule(
            id = "r_${System.currentTimeMillis()}",
            name = name,
            triggerDescription = trigger,
            actionDescription = action,
            isEnabled = true
        )
        _routines.update { it + newRule }
    }

    fun deleteRoutine(routineId: String) {
        _routines.update { list -> list.filterNot { it.id == routineId } }
    }

    fun saveDesign(title: String, category: String, colorHex: String, description: String) {
        val newDesign = SavedDesign(
            id = "d_${System.currentTimeMillis()}",
            title = title,
            categoryName = category,
            previewColorHex = colorHex,
            description = description
        )
        _savedDesigns.update { listOf(newDesign) + it }
    }

    fun deleteDesign(designId: String) {
        _savedDesigns.update { list -> list.filterNot { it.id == designId } }
    }

    fun resetModule(id: ModuleId) {
        when (id) {
            ModuleId.LOCK_STAR -> _lockStarConfig.value = LockStarConfig()
            ModuleId.QUICK_STAR -> _quickStarConfig.value = QuickStarConfig()
            ModuleId.THEME_PARK -> _themeParkConfig.value = ThemeParkConfig()
            ModuleId.HOME_UP -> _homeUpConfig.value = HomeUpConfig()
            ModuleId.NAV_STAR -> _navStarConfig.value = NavStarConfig()
            ModuleId.CLOCK_FACE -> _clockFaceConfig.value = ClockFaceConfig()
            ModuleId.WONDERLAND -> _wonderlandConfig.value = WonderlandConfig()
            ModuleId.KEYS_CAFE -> _keysCafeConfig.value = KeysCafeConfig()
            ModuleId.EDGE_LIGHTING -> _edgeLightingConfig.value = EdgeLightingConfig()
            ModuleId.PENTASTIC -> _pentasticConfig.value = PentasticConfig()
            ModuleId.SOUND_ASSISTANT -> _soundAssistantConfig.value = SoundAssistantConfig()
            else -> {}
        }
    }

    fun resetAllSettings() {
        prefs.edit().clear().apply()
        _lockStarConfig.value = LockStarConfig()
        _quickStarConfig.value = QuickStarConfig()
        _themeParkConfig.value = ThemeParkConfig()
        _homeUpConfig.value = HomeUpConfig()
        _navStarConfig.value = NavStarConfig()
        _clockFaceConfig.value = ClockFaceConfig()
        _wonderlandConfig.value = WonderlandConfig()
        _keysCafeConfig.value = KeysCafeConfig()
        _edgeLightingConfig.value = EdgeLightingConfig()
        _pentasticConfig.value = PentasticConfig()
        _soundAssistantConfig.value = SoundAssistantConfig()
        loadInitialState()
    }

    fun exportSettingsJson(): String {
        val json = JSONObject()
        json.put("app", "NovaLock")
        json.put("version", "1.0")
        json.put("exportedAt", System.currentTimeMillis())

        val themeObj = JSONObject()
        val theme = _themeParkConfig.value
        themeObj.put("themeName", theme.themeName)
        themeObj.put("primaryColorHex", theme.primaryColorHex)
        themeObj.put("accentColorHex", theme.accentColorHex)
        themeObj.put("iconShape", theme.iconShape)
        json.put("themePark", themeObj)

        val lockObj = JSONObject()
        val lock = _lockStarConfig.value
        lockObj.put("clockStyle", lock.clockStyle)
        lockObj.put("clockColorHex", lock.clockColorHex)
        json.put("lockStar", lockObj)

        val clockObj = JSONObject()
        val clock = _clockFaceConfig.value
        clockObj.put("clockType", clock.clockType)
        clockObj.put("primaryColorHex", clock.primaryColorHex)
        json.put("clockFace", clockObj)

        return json.toString(2)
    }

    fun refreshHardwareVitals() {
        try {
            // Battery info via sticky intent
            val batteryFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryIntent = context.registerReceiver(null, batteryFilter)
            val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 85
            val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 100
            val batteryPct = if (level >= 0 && scale > 0) (level * 100 / scale) else 85
            val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
            val rawTemp = batteryIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 280) ?: 280
            val tempC = rawTemp / 10.0f
            val healthInt = batteryIntent?.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD)
            val healthStr = when (healthInt) {
                BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
                BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
                else -> "Normal"
            }

            // Storage info
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            val availBlocks = stat.availableBlocksLong
            val totalStorageGb = (totalBlocks * blockSize) / (1024 * 1024 * 1024)
            val freeStorageGb = (availBlocks * blockSize) / (1024 * 1024 * 1024)

            // RAM info
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager?.getMemoryInfo(memInfo)
            val totalRamMb = memInfo.totalMem / (1024 * 1024)
            val availRamMb = memInfo.availMem / (1024 * 1024)

            _deviceVitals.value = DeviceHardwareVitals(
                batteryPercent = batteryPct,
                isCharging = isCharging,
                batteryHealth = healthStr,
                batteryTemperatureC = tempC,
                totalStorageGb = totalStorageGb.coerceAtLeast(32),
                freeStorageGb = freeStorageGb.coerceAtLeast(8),
                totalRamMb = totalRamMb.coerceAtLeast(4096),
                availRamMb = availRamMb.coerceAtLeast(1500),
                androidVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
                securityPatch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.SECURITY_PATCH else "Supported"
            )
        } catch (_: Exception) {
            // Safe fallback vitals
            _deviceVitals.value = DeviceHardwareVitals()
        }
    }
}
