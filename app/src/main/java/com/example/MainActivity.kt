package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ModuleCategory
import com.example.data.model.ModuleId
import com.example.data.repository.CustomizationRepository
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.dashboard.ModuleListScreen
import com.example.ui.modules.lifeup.*
import com.example.ui.modules.makeup.*
import com.example.ui.mydesigns.MyDesignsScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.NovaLockTheme

enum class MainTab(val label: String, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    MAKE_UP("Make Up", Icons.Filled.Style, Icons.Outlined.Style),
    LIFE_UP("Life Up", Icons.Filled.Widgets, Icons.Outlined.Widgets),
    WALLPAPERS("Wallpapers", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
    MY_DESIGNS("My Designs", Icons.Filled.Folder, Icons.Outlined.Folder),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}

class MainActivity : ComponentActivity() {

    private lateinit var repository: CustomizationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = CustomizationRepository(applicationContext)

        setContent {
            val modules by repository.modules.collectAsStateWithLifecycle()
            val vitals by repository.deviceVitals.collectAsStateWithLifecycle()
            val lockStarConfig by repository.lockStarConfig.collectAsStateWithLifecycle()
            val quickStarConfig by repository.quickStarConfig.collectAsStateWithLifecycle()
            val themeParkConfig by repository.themeParkConfig.collectAsStateWithLifecycle()
            val homeUpConfig by repository.homeUpConfig.collectAsStateWithLifecycle()
            val navStarConfig by repository.navStarConfig.collectAsStateWithLifecycle()
            val clockFaceConfig by repository.clockFaceConfig.collectAsStateWithLifecycle()
            val wonderlandConfig by repository.wonderlandConfig.collectAsStateWithLifecycle()
            val keysCafeConfig by repository.keysCafeConfig.collectAsStateWithLifecycle()
            val edgeLightingConfig by repository.edgeLightingConfig.collectAsStateWithLifecycle()
            val pentasticConfig by repository.pentasticConfig.collectAsStateWithLifecycle()
            val soundAssistantConfig by repository.soundAssistantConfig.collectAsStateWithLifecycle()
            val routines by repository.routines.collectAsStateWithLifecycle()
            val savedDesigns by repository.savedDesigns.collectAsStateWithLifecycle()

            val systemDark = isSystemInDarkTheme()
            var userDarkTheme by remember { mutableStateOf<Boolean?>(null) }
            val isDark = userDarkTheme ?: systemDark

            var currentTab by remember { mutableStateOf(MainTab.HOME) }
            var selectedModule by remember { mutableStateOf<ModuleId?>(null) }
            var searchQuery by remember { mutableStateOf("") }

            // Dynamic theme colors from ThemePark
            val customPrimary = remember(themeParkConfig.primaryColorHex) {
                try { Color(android.graphics.Color.parseColor(themeParkConfig.primaryColorHex)) } catch (_: Exception) { null }
            }
            val customAccent = remember(themeParkConfig.accentColorHex) {
                try { Color(android.graphics.Color.parseColor(themeParkConfig.accentColorHex)) } catch (_: Exception) { null }
            }

            NovaLockTheme(
                darkTheme = isDark,
                customPrimaryColor = customPrimary,
                customAccentColor = customAccent
            ) {
                // Back button handling
                BackHandler(enabled = selectedModule != null) {
                    selectedModule = null
                }

                Scaffold(
                    bottomBar = {
                        if (selectedModule == null) {
                            NavigationBar(
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("main_bottom_nav"),
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp
                            ) {
                                MainTab.values().forEach { tab ->
                                    val isSelected = currentTab == tab
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = {
                                            currentTab = tab
                                            searchQuery = ""
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                                                contentDescription = tab.label
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = tab.label,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                maxLines = 1
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        )
                                    )
                                }
                            }
                        }
                    },
                    contentWindowInsets = WindowInsets.safeDrawing,
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AnimatedContent(
                            targetState = selectedModule,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "screen_content"
                        ) { activeModule ->
                            if (activeModule != null) {
                                // Detailed Module Customization Studio
                                when (activeModule) {
                                    ModuleId.LOCK_STAR -> LockStarScreen(
                                        config = lockStarConfig,
                                        onUpdateConfig = { repository.updateLockStar(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.LOCK_STAR) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.QUICK_STAR -> QuickStarScreen(
                                        config = quickStarConfig,
                                        onUpdateConfig = { repository.updateQuickStar(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.QUICK_STAR) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.THEME_PARK -> ThemeParkScreen(
                                        config = themeParkConfig,
                                        onUpdateConfig = { repository.updateThemePark(it) },
                                        onSaveDesign = { title, cat, hex, desc -> repository.saveDesign(title, cat, hex, desc) },
                                        onResetConfig = { repository.resetModule(ModuleId.THEME_PARK) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.HOME_UP -> HomeUpScreen(
                                        config = homeUpConfig,
                                        onUpdateConfig = { repository.updateHomeUp(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.HOME_UP) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.NAV_STAR -> NavStarScreen(
                                        config = navStarConfig,
                                        onUpdateConfig = { repository.updateNavStar(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.NAV_STAR) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.CLOCK_FACE -> ClockFaceScreen(
                                        config = clockFaceConfig,
                                        onUpdateConfig = { repository.updateClockFace(it) },
                                        onSaveDesign = { title, cat, hex, desc -> repository.saveDesign(title, cat, hex, desc) },
                                        onResetConfig = { repository.resetModule(ModuleId.CLOCK_FACE) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.WONDERLAND -> WonderlandScreen(
                                        config = wonderlandConfig,
                                        onUpdateConfig = { repository.updateWonderland(it) },
                                        onSaveDesign = { title, cat, hex, desc -> repository.saveDesign(title, cat, hex, desc) },
                                        onResetConfig = { repository.resetModule(ModuleId.WONDERLAND) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.KEYS_CAFE -> KeysCafeScreen(
                                        config = keysCafeConfig,
                                        onUpdateConfig = { repository.updateKeysCafe(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.KEYS_CAFE) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.EDGE_LIGHTING -> EdgeLightingScreen(
                                        config = edgeLightingConfig,
                                        onUpdateConfig = { repository.updateEdgeLighting(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.EDGE_LIGHTING) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.PENTASTIC -> PentasticScreen(
                                        config = pentasticConfig,
                                        onUpdateConfig = { repository.updatePentastic(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.PENTASTIC) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.SOUND_ASSISTANT -> SoundAssistantScreen(
                                        config = soundAssistantConfig,
                                        onUpdateConfig = { repository.updateSoundAssistant(it) },
                                        onResetConfig = { repository.resetModule(ModuleId.SOUND_ASSISTANT) },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.MULTI_STAR -> MultiStarScreen(
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.NOTI_STAR -> NotiStarScreen(
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.REGI_STAR -> RegiStarScreen(
                                        onNavigateToNiceShot = { selectedModule = ModuleId.NICE_SHOT },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.ONE_HAND -> OneHandOperationScreen(
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.NICE_SHOT -> NiceShotScreen(
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.NICE_CATCH -> NiceCatchScreen(
                                        vitals = vitals,
                                        onRefresh = { repository.refreshHardwareVitals() },
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.EDGE_TOUCH -> EdgeTouchScreen(
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.GAME_BOOSTER -> GameBoosterScreen(
                                        batteryTempC = vitals.batteryTemperatureC,
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.DISPLAY_ASSISTANT -> DisplayAssistantScreen(
                                        onBack = { selectedModule = null }
                                    )
                                    ModuleId.ROUTINE_PLUS -> RoutinePlusScreen(
                                        routines = routines,
                                        onToggleRoutine = { repository.toggleRoutine(it) },
                                        onAddRoutine = { name, trig, act -> repository.addRoutine(name, trig, act) },
                                        onDeleteRoutine = { repository.deleteRoutine(it) },
                                        onBack = { selectedModule = null }
                                    )
                                }
                            } else {
                                // Main Tab Destination
                                when (currentTab) {
                                    MainTab.HOME -> DashboardScreen(
                                        modules = modules,
                                        deviceVitals = vitals,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { searchQuery = it },
                                        onSelectModule = { mod ->
                                            repository.recordModuleUsed(mod)
                                            selectedModule = mod
                                        },
                                        onToggleEnable = { mod -> repository.toggleModuleEnabled(mod) },
                                        onToggleFavorite = { mod -> repository.toggleModuleFavorite(mod) },
                                        onNavigateToCategory = { cat ->
                                            currentTab = if (cat == ModuleCategory.MAKE_UP) MainTab.MAKE_UP else MainTab.LIFE_UP
                                        },
                                        onQuickAction = { action -> }
                                    )
                                    MainTab.MAKE_UP -> ModuleListScreen(
                                        category = ModuleCategory.MAKE_UP,
                                        modules = modules,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { searchQuery = it },
                                        onSelectModule = { mod ->
                                            repository.recordModuleUsed(mod)
                                            selectedModule = mod
                                        },
                                        onToggleEnable = { mod -> repository.toggleModuleEnabled(mod) },
                                        onToggleFavorite = { mod -> repository.toggleModuleFavorite(mod) }
                                    )
                                    MainTab.LIFE_UP -> ModuleListScreen(
                                        category = ModuleCategory.LIFE_UP,
                                        modules = modules,
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { searchQuery = it },
                                        onSelectModule = { mod ->
                                            repository.recordModuleUsed(mod)
                                            selectedModule = mod
                                        },
                                        onToggleEnable = { mod -> repository.toggleModuleEnabled(mod) },
                                        onToggleFavorite = { mod -> repository.toggleModuleFavorite(mod) }
                                    )
                                    MainTab.WALLPAPERS -> WonderlandScreen(
                                        config = wonderlandConfig,
                                        onUpdateConfig = { repository.updateWonderland(it) },
                                        onSaveDesign = { title, cat, hex, desc -> repository.saveDesign(title, cat, hex, desc) },
                                        onResetConfig = { repository.resetModule(ModuleId.WONDERLAND) },
                                        onBack = { currentTab = MainTab.HOME }
                                    )
                                    MainTab.MY_DESIGNS -> MyDesignsScreen(
                                        designs = savedDesigns,
                                        onDeleteDesign = { repository.deleteDesign(it) },
                                        onExportJson = { repository.exportSettingsJson() }
                                    )
                                    MainTab.SETTINGS -> SettingsScreen(
                                        modules = modules,
                                        vitals = vitals,
                                        isDarkTheme = isDark,
                                        onToggleDarkTheme = { userDarkTheme = it },
                                        onResetAllSettings = { repository.resetAllSettings() },
                                        onResetModule = { mod -> repository.resetModule(mod) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
