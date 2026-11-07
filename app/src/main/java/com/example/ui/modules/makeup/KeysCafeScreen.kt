package com.example.ui.modules.makeup

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KeysCafeConfig
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeysCafeScreen(
    config: KeysCafeConfig,
    onUpdateConfig: ((KeysCafeConfig) -> KeysCafeConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lastTypedText by remember { mutableStateOf("TAP KEYS TO TEST") }

    val themeColors = remember(config.themeName) {
        when (config.themeName) {
            "Cotton Candy" -> Pair(Color(0xFFFFB6C1), Color(0xFFE0BBE4))
            "AMOLED Dark" -> Pair(Color(0xFF252836), Color(0xFF1E1F2B))
            "Retro Mint" -> Pair(Color(0xFF2EC4B6), Color(0xFFCBF3F0))
            else -> Pair(Color(0xFF0381FE), Color(0xFF7A5AF8)) // Cyberpunk Blue
        }
    }

    val keyShape: Shape = remember(config.keyShape) {
        when (config.keyShape) {
            "Soft Pill" -> RoundedCornerShape(18.dp)
            "Sharp Modern" -> RoundedCornerShape(4.dp)
            else -> RoundedCornerShape(10.dp) // Rounded Squircle
        }
    }

    fun handleKeyPress(char: String) {
        lastTypedText = if (lastTypedText == "TAP KEYS TO TEST") char else (lastTypedText + char).takeLast(24)
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val amplitude = (config.hapticIntensity * 255).toInt().coerceIn(1, 255)
                vibrator?.vibrate(VibrationEffect.createOneShot(18, amplitude))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(18)
            }
        } catch (_: Exception) {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keys Cafe", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("keyscafe_back_btn")) {
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
            // Offline Privacy Disclosure Banner
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF12B76A).copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF12B76A).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF12B76A), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Privacy Guaranteed: Works 100% offline. Never records keystrokes, passwords, or input data. Zero telemetry.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Interactive Keyboard Studio Preview
            item {
                Text(
                    text = "Interactive Keyboard Studio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OneUICard(backgroundColor = Color(0xFF0F111A)) {
                    // Typed text display bar
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        ) {
                            Text(
                                text = lastTypedText,
                                color = themeColors.first,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (lastTypedText != "TAP KEYS TO TEST") {
                                IconButton(onClick = { lastTypedText = "" }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Backspace, contentDescription = "Clear", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulated QWERTY Rows
                    val row1 = listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P")
                    val row2 = listOf("A", "S", "D", "F", "G", "H", "J", "K", "L")
                    val row3 = listOf("Z", "X", "C", "V", "B", "N", "M")

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        // Row 1
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                            row1.forEach { k ->
                                KeyButton(keyChar = k, shape = keyShape, activeColor = themeColors.first, onClick = { handleKeyPress(k) }, modifier = Modifier.weight(1f))
                            }
                        }
                        // Row 2
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                        ) {
                            row2.forEach { k ->
                                KeyButton(keyChar = k, shape = keyShape, activeColor = themeColors.first, onClick = { handleKeyPress(k) }, modifier = Modifier.weight(1f))
                            }
                        }
                        // Row 3 with spacebar preview
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            KeyButton(keyChar = "⇧", shape = keyShape, activeColor = themeColors.second, onClick = { handleKeyPress("⇧") }, modifier = Modifier.weight(1.3f))
                            row3.forEach { k ->
                                KeyButton(keyChar = k, shape = keyShape, activeColor = themeColors.first, onClick = { handleKeyPress(k) }, modifier = Modifier.weight(1f))
                            }
                            KeyButton(keyChar = "⌫", shape = keyShape, activeColor = themeColors.second, onClick = { handleKeyPress("") }, modifier = Modifier.weight(1.3f))
                        }
                        // Space bar
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            KeyButton(keyChar = "SPACE", shape = keyShape, activeColor = themeColors.second, onClick = { handleKeyPress(" ") }, modifier = Modifier.fillMaxWidth(0.6f))
                        }
                    }
                }
            }

            // Theme Preset Choices
            item {
                OneUICard {
                    Text("Keyboard Color Palette", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val themes = listOf("Cyberpunk Blue", "Cotton Candy", "AMOLED Dark", "Retro Mint")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        themes.forEach { th ->
                            FilterChip(
                                selected = config.themeName == th,
                                onClick = { onUpdateConfig { it.copy(themeName = th) } },
                                label = { Text(th, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Key Shape Customizer
            item {
                OneUICard {
                    Text("Key Silhouette Shape", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val shapes = listOf("Rounded Squircle", "Soft Pill", "Sharp Modern")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        shapes.forEach { sh ->
                            FilterChip(
                                selected = config.keyShape == sh,
                                onClick = { onUpdateConfig { it.copy(keyShape = sh) } },
                                label = { Text(sh, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Sound & Haptic Presets
            item {
                OneUICard {
                    Text("Keypress Audio & Haptics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val sounds = listOf("Mechanical Thock", "Modern Click", "Water Drop", "Silent")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        sounds.forEach { snd ->
                            FilterChip(
                                selected = config.keySoundPreset == snd,
                                onClick = { onUpdateConfig { it.copy(keySoundPreset = snd) } },
                                label = { Text(snd, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Haptic Feedback Intensity: ${(config.hapticIntensity * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = config.hapticIntensity,
                        onValueChange = { onUpdateConfig { cfg -> cfg.copy(hapticIntensity = it) } },
                        valueRange = 0.2f..1.0f
                    )
                }
            }
        }
    }
}

@Composable
fun KeyButton(
    keyChar: String,
    shape: Shape,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = shape,
        color = Color.White.copy(alpha = 0.12f),
        modifier = modifier
            .height(42.dp)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = keyChar,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = if (keyChar.length > 2) 11.sp else 14.sp
            )
        }
    }
}
