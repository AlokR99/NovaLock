package com.example.ui.modules.makeup

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HomeUpConfig
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUpScreen(
    config: HomeUpConfig,
    onUpdateConfig: ((HomeUpConfig) -> HomeUpConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isTestLauncherOpen by remember { mutableStateOf(false) }

    val sampleApps = remember {
        listOf(
            Triple("Phone", Icons.Default.Call, Color(0xFF12B76A)),
            Triple("Messages", Icons.Default.Chat, Color(0xFF0381FE)),
            Triple("Camera", Icons.Default.CameraAlt, Color(0xFFE31B78)),
            Triple("Gallery", Icons.Default.Image, Color(0xFFF79009)),
            Triple("Browser", Icons.Default.Language, Color(0xFF06AED4)),
            Triple("Music", Icons.Default.MusicNote, Color(0xFF7A5AF8)),
            Triple("Settings", Icons.Default.Settings, Color(0xFF667085)),
            Triple("Calendar", Icons.Default.CalendarToday, Color(0xFF0086C9)),
            Triple("Clock", Icons.Default.Schedule, Color(0xFF9E77ED)),
            Triple("Notes", Icons.Default.EditNote, Color(0xFFFDB022)),
            Triple("Files", Icons.Default.Folder, Color(0xFF32D583)),
            Triple("Calculator", Icons.Default.Calculate, Color(0xFFEE46BC))
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home Up", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("homeup_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onResetConfig) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding() + 24.dp,
                start = 20.dp,
                end = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Live Launcher Grid Simulator
            item {
                Text(
                    text = "Live Home Screen Simulation",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0D111D))
                        .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Mini Search Widget
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Search apps & web...", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // App Grid
                        val cols = config.gridCols
                        val rows = 3 // show top 3 rows in preview
                        val displayedApps = sampleApps.take(cols * rows)

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            for (r in 0 until rows) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    for (c in 0 until cols) {
                                        val idx = r * cols + c
                                        if (idx < displayedApps.size) {
                                            val (name, icon, color) = displayedApps[idx]
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.clickable {
                                                    Toast.makeText(context, "Clicked $name", Toast.LENGTH_SHORT).show()
                                                }
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size((36 * config.iconScale).dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(color)
                                                ) {
                                                    Icon(icon, contentDescription = name, tint = Color.White, modifier = Modifier.size((20 * config.iconScale).dp))
                                                }
                                                if (config.showLabels) {
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(name, color = Color.White, fontSize = 9.sp, maxLines = 1)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Dock Preview
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                sampleApps.take(config.dockAppCount).forEach { (name, icon, color) ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size((34 * config.iconScale).dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(color)
                                    ) {
                                        Icon(icon, contentDescription = name, tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Grid Size Customizer
            item {
                OneUICard {
                    Text("Home Screen Grid Dimension", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val gridOptions = listOf(Pair(4, 5), Pair(4, 6), Pair(5, 5), Pair(5, 6))
                        gridOptions.forEach { (cols, rows) ->
                            FilterChip(
                                selected = config.gridCols == cols && config.gridRows == rows,
                                onClick = { onUpdateConfig { it.copy(gridCols = cols, gridRows = rows) } },
                                label = { Text("$cols × $rows") },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Icon Scale Slider
            item {
                OneUICard {
                    Text("App Icon Size: ${(config.iconScale * 100).toInt()}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = config.iconScale,
                        onValueChange = { scale -> onUpdateConfig { it.copy(iconScale = scale) } },
                        valueRange = 0.8f..1.3f,
                        steps = 5
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Show App Labels Under Icons", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = config.showLabels,
                            onCheckedChange = { show -> onUpdateConfig { it.copy(showLabels = show) } }
                        )
                    }
                }
            }

            // App Drawer Layout Style
            item {
                OneUICard {
                    Text("App Drawer Presentation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val styles = listOf("Vertical Grid", "Horizontal Pages", "Categorized")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        styles.forEach { style ->
                            FilterChip(
                                selected = config.appDrawerStyle == style,
                                onClick = { onUpdateConfig { it.copy(appDrawerStyle = style) } },
                                label = { Text(style) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Dock App Count: ${config.dockAppCount} apps", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = config.dockAppCount.toFloat(),
                        onValueChange = { onUpdateConfig { cfg -> cfg.copy(dockAppCount = it.toInt()) } },
                        valueRange = 4f..6f,
                        steps = 1
                    )
                }
            }

            // Set as Default Launcher helper
            item {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            try {
                                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "Choose default launcher in device settings.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("set_default_launcher_btn")
                ) {
                    Icon(Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Choose Default Home App in Android Settings", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
