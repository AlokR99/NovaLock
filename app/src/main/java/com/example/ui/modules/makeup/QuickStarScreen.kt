package com.example.ui.modules.makeup

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuickStarConfig
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickStarScreen(
    config: QuickStarConfig,
    onUpdateConfig: ((QuickStarConfig) -> QuickStarConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var simSpeed by remember { mutableStateOf("2.8 MB/s") }
    var brightnessLevel by remember { mutableFloatStateOf(0.75f) }

    // Simulated network speed fluctuation
    LaunchedEffect(config.showNetworkSpeed) {
        val speeds = listOf("1.4 MB/s", "2.8 MB/s", "4.2 MB/s", "890 KB/s", "3.6 MB/s")
        var i = 0
        while (config.showNetworkSpeed) {
            delay(1800)
            simSpeed = speeds[i % speeds.size]
            i++
        }
    }

    val themeColor = remember(config.themeColorHex) {
        try {
            Color(android.graphics.Color.parseColor(config.themeColorHex))
        } catch (_: Exception) {
            Color(0xFF0381FE)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QuickStar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("quickstar_back_btn")) {
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
            // Live Quick Settings Preview Panel
            item {
                Text(
                    text = "Quick Settings Panel Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OneUICard(backgroundColor = Color(0xFF141722)) {
                    // Status Bar Header with Network Speed Indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("10:24", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            if (config.showNetworkSpeed) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = themeColor.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = "↓↑ $simSpeed",
                                        color = themeColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Icon(Icons.Default.SignalCellularAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Icon(Icons.Default.BatteryFull, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Tiles Grid Preview
                    val cols = config.tileCols
                    val rows = config.tileRows
                    val tiles = listOf(
                        Triple("Wi-Fi", Icons.Default.Wifi, true),
                        Triple("Sound", Icons.Default.VolumeUp, true),
                        Triple("Bluetooth", Icons.Default.Bluetooth, true),
                        Triple("Auto Rotate", Icons.Default.ScreenRotation, false),
                        Triple("Torch", Icons.Default.FlashlightOn, true),
                        Triple("Flight Mode", Icons.Default.AirplanemodeActive, false),
                        Triple("Location", Icons.Default.LocationOn, true),
                        Triple("Dark Mode", Icons.Default.DarkMode, true),
                        Triple("Hotspot", Icons.Default.WifiTethering, false),
                        Triple("NFC", Icons.Default.Nfc, false),
                        Triple("Eye Comfort", Icons.Default.RemoveRedEye, true),
                        Triple("Power Save", Icons.Default.BatterySaver, false)
                    ).take(cols * rows)

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (r in 0 until rows) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                for (c in 0 until cols) {
                                    val index = r * cols + c
                                    if (index < tiles.size) {
                                        val tile = tiles[index]
                                        QuickTilePreview(
                                            label = tile.first,
                                            icon = tile.second,
                                            isActive = tile.third,
                                            activeColor = themeColor,
                                            cornerRadius = config.roundedCornersDp,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Brightness Slider Preview
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.BrightnessLow, contentDescription = null, tint = Color.White.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = brightnessLevel,
                            onValueChange = { brightnessLevel = it },
                            colors = SliderDefaults.colors(
                                thumbColor = themeColor,
                                activeTrackColor = themeColor,
                                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.BrightnessHigh, contentDescription = null, tint = themeColor)
                    }
                }
            }

            // Quick Tile Color Palette
            item {
                OneUICard {
                    Text(
                        text = "Quick Settings Theme Palette",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ColorPaletteRow(
                        selectedHex = config.themeColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(themeColorHex = hex) } }
                    )
                }
            }

            // Grid Density Configuration
            item {
                OneUICard {
                    Text(
                        text = "Tile Grid Density",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = config.tileCols == 3 && config.tileRows == 3,
                            onClick = { onUpdateConfig { it.copy(tileCols = 3, tileRows = 3) } },
                            label = { Text("3 × 3 Compact") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        FilterChip(
                            selected = config.tileCols == 4 && config.tileRows == 3,
                            onClick = { onUpdateConfig { it.copy(tileCols = 4, tileRows = 3) } },
                            label = { Text("4 × 3 Expanded") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Status Bar & Indicator Toggles
            item {
                OneUICard {
                    Text(
                        text = "Status Bar Indicators",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Network Speed Indicator", fontWeight = FontWeight.SemiBold)
                            Text(
                                "Display real-time transfer rates in the preview header",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = config.showNetworkSpeed,
                            onCheckedChange = { show -> onUpdateConfig { it.copy(showNetworkSpeed = show) } }
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Tile Corner Radius: ${config.roundedCornersDp} dp", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = config.roundedCornersDp.toFloat(),
                        onValueChange = { rad -> onUpdateConfig { it.copy(roundedCornersDp = rad.toInt()) } },
                        valueRange = 8f..32f,
                        steps = 5
                    )
                }
            }
        }
    }
}

@Composable
fun QuickTilePreview(
    label: String,
    icon: ImageVector,
    isActive: Boolean,
    activeColor: Color,
    cornerRadius: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(cornerRadius.dp),
        color = if (isActive) activeColor else Color.White.copy(alpha = 0.1f),
        modifier = modifier.height(64.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) Color.White else Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = if (isActive) Color.White else Color.White.copy(alpha = 0.7f),
                maxLines = 1
            )
        }
    }
}
