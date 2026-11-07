package com.example.ui.modules.makeup

import android.widget.Toast
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ThemeParkConfig
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeParkScreen(
    config: ThemeParkConfig,
    onUpdateConfig: ((ThemeParkConfig) -> ThemeParkConfig) -> Unit,
    onSaveDesign: (title: String, category: String, colorHex: String, description: String) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var themeNameInput by remember { mutableStateOf(config.themeName) }

    val primaryColor = remember(config.primaryColorHex) {
        try { Color(android.graphics.Color.parseColor(config.primaryColorHex)) } catch (_: Exception) { Color(0xFF0381FE) }
    }
    val accentColor = remember(config.accentColorHex) {
        try { Color(android.graphics.Color.parseColor(config.accentColorHex)) } catch (_: Exception) { Color(0xFF7A5AF8) }
    }

    val iconShape: Shape = remember(config.iconShape) {
        when (config.iconShape) {
            "Circle" -> CircleShape
            "Teardrop" -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 4.dp)
            "RoundedRect" -> RoundedCornerShape(8.dp)
            else -> RoundedCornerShape(16.dp) // Squircle
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Park", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("themepark_back_btn")) {
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
            // Live Device Theme Mockup Preview
            item {
                Text(
                    text = "Live Theme Mockup",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0F111A))
                        .border(2.dp, primaryColor.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .padding(18.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Mini Status Bar
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("10:24", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Wifi, contentDescription = null, tint = primaryColor, modifier = Modifier.size(13.dp))
                                Icon(Icons.Default.BatteryFull, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Mini App Icons with Custom Shape
                        Text(
                            text = config.themeName,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val sampleIcons = listOf(
                                Triple("Phone", Icons.Default.Call, primaryColor),
                                Triple("Messages", Icons.Default.Chat, accentColor),
                                Triple("Camera", Icons.Default.CameraAlt, Color(0xFFE31B78)),
                                Triple("Settings", Icons.Default.Settings, Color(0xFF12B76A))
                            )
                            sampleIcons.forEach { (label, icon, color) ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(iconShape)
                                            .background(color)
                                    ) {
                                        Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Mini M3 Sample Card in theme
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = primaryColor.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(primaryColor)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Theme Element Preview", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Dynamic palette applied across UI", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Mini Pill Button
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        ) {
                            Text("Themed Action", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }

            // Theme Name Input
            item {
                OneUICard {
                    Text("Theme Name", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = themeNameInput,
                        onValueChange = {
                            themeNameInput = it
                            onUpdateConfig { cfg -> cfg.copy(themeName = it) }
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Primary Color Picker
            item {
                OneUICard {
                    Text("Primary Dominant Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ColorPaletteRow(
                        selectedHex = config.primaryColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(primaryColorHex = hex) } }
                    )
                }
            }

            // Accent Color Picker
            item {
                OneUICard {
                    Text("Secondary Accent Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ColorPaletteRow(
                        selectedHex = config.accentColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(accentColorHex = hex) } },
                        colors = listOf("#7A5AF8", "#E31B78", "#06AED4", "#F79009", "#12B76A", "#0381FE", "#FF6B6B", "#4D96FF")
                    )
                }
            }

            // Icon Shape Selector
            item {
                OneUICard {
                    Text("Icon Silhouette Shape", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    val shapes = listOf("Squircle", "Circle", "Teardrop", "RoundedRect")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        shapes.forEach { shape ->
                            FilterChip(
                                selected = config.iconShape == shape,
                                onClick = { onUpdateConfig { it.copy(iconShape = shape) } },
                                label = { Text(shape) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Actions: Apply Theme & Save Theme Design
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onSaveDesign(
                                themeNameInput,
                                "Theme Park",
                                config.primaryColorHex,
                                "Custom theme with ${config.iconShape} icons and ${config.primaryColorHex} primary accent"
                            )
                            Toast.makeText(context, "Theme \"$themeNameInput\" saved to My Designs!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Design")
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Theme \"$themeNameInput\" activated in NovaLock!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("apply_theme_btn")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Apply Theme")
                    }
                }
            }
        }
    }
}
