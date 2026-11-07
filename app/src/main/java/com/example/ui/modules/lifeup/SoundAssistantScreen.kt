package com.example.ui.modules.lifeup

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SoundAssistantConfig
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundAssistantScreen(
    config: SoundAssistantConfig,
    onUpdateConfig: ((SoundAssistantConfig) -> SoundAssistantConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun testVibration(pattern: String) {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                when (pattern) {
                    "Double Click" -> {
                        val timings = longArrayOf(0, 40, 60, 40)
                        val amps = intArrayOf(0, 200, 0, 255)
                        vibrator?.vibrate(VibrationEffect.createWaveform(timings, amps, -1))
                    }
                    "Pulse Wave" -> {
                        val timings = longArrayOf(0, 100, 80, 150)
                        val amps = intArrayOf(0, 150, 0, 220)
                        vibrator?.vibrate(VibrationEffect.createWaveform(timings, amps, -1))
                    }
                    else -> {
                        vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(50)
            }
            Toast.makeText(context, "Haptic: $pattern", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sound Assistant", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("soundassistant_back_btn")) {
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
            // Per-Channel Volume Sliders
            item {
                OneUICard {
                    Text("Volume Channel Calibration", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Media
                    VolumeRow(
                        label = "Media Playback",
                        icon = Icons.Default.MusicNote,
                        volume = config.mediaVolumePercent,
                        onVolumeChange = { v -> onUpdateConfig { it.copy(mediaVolumePercent = v) } }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Ringtone
                    VolumeRow(
                        label = "Ringtone & Calls",
                        icon = Icons.Default.Call,
                        volume = config.ringVolumePercent,
                        onVolumeChange = { v -> onUpdateConfig { it.copy(ringVolumePercent = v) } }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Notification
                    VolumeRow(
                        label = "Notifications & Alerts",
                        icon = Icons.Default.Notifications,
                        volume = config.notifVolumePercent,
                        onVolumeChange = { v -> onUpdateConfig { it.copy(notifVolumePercent = v) } }
                    )
                }
            }

            // Equalizer Presets
            item {
                OneUICard {
                    Text("5-Band Equalizer Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val presets = listOf("Normal", "Bass Boost", "Vocal Clear", "Dynamic Boost", "Rock")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        presets.forEach { eq ->
                            FilterChip(
                                selected = config.eqPreset == eq,
                                onClick = { onUpdateConfig { it.copy(eqPreset = eq) } },
                                label = { Text(eq, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Visual Equalizer Spectrum Bars Simulation
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(horizontal = 16.dp)
                    ) {
                        val barHeights = when (config.eqPreset) {
                            "Bass Boost" -> listOf(0.9f, 0.75f, 0.4f, 0.35f, 0.3f)
                            "Vocal Clear" -> listOf(0.3f, 0.5f, 0.95f, 0.7f, 0.4f)
                            "Rock" -> listOf(0.85f, 0.6f, 0.4f, 0.7f, 0.9f)
                            else -> listOf(0.6f, 0.65f, 0.7f, 0.65f, 0.6f)
                        }
                        val labels = listOf("60Hz", "230Hz", "910Hz", "3.6kHz", "14kHz")
                        barHeights.forEachIndexed { idx, h ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Surface(
                                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height((45 * h).dp)
                                ) {}
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(labels[idx], fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Haptic Vibration Patterns
            item {
                OneUICard {
                    Text("Custom Vibration Feedback", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val patterns = listOf("Crisp Tap", "Double Click", "Pulse Wave")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        patterns.forEach { pat ->
                            FilterChip(
                                selected = config.hapticPattern == pat,
                                onClick = {
                                    onUpdateConfig { it.copy(hapticPattern = pat) }
                                    testVibration(pat)
                                },
                                label = { Text(pat, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // System Ringtone Picker Intent
            item {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
                            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Audio settings opened.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("system_ringtone_btn")
                ) {
                    Icon(Icons.Default.LibraryMusic, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Choose Device Ringtone via System Picker")
                }
            }
        }
    }
}

@Composable
fun VolumeRow(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    volume: Int,
    onVolumeChange: (Int) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            }
            Text("$volume%", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = volume.toFloat(),
            onValueChange = { onVolumeChange(it.toInt()) },
            valueRange = 0f..100f
        )
    }
}
