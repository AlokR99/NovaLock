package com.example.ui.modules.lifeup

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegiStarScreen(
    onNavigateToNiceShot: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var backTapAction by remember { mutableStateOf("Take Screenshot") }
    var backTapFeedback by remember { mutableStateOf("Double tap phone back to trigger") }

    fun launchSettings(action: String, title: String) {
        try {
            val intent = Intent(action)
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "$title setting opened.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RegiStar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("registar_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Back-Tap Gesture Simulator
            item {
                OneUICard(backgroundColor = Color(0xFF101828)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TouchApp, contentDescription = null, tint = Color(0xFF12B76A), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Back-Tap Quick Action", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(backTapFeedback, color = Color(0xFF12B76A), fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Select Action for Double Back-Tap:", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    val actions = listOf("Take Screenshot", "Toggle Flashlight", "Mute Sound", "Open Camera")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        actions.forEach { act ->
                            FilterChip(
                                selected = backTapAction == act,
                                onClick = {
                                    backTapAction = act
                                    backTapFeedback = "Configured: $act"
                                },
                                label = { Text(act, fontSize = 10.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            backTapFeedback = "Simulated Back-Tap Triggered: $backTapAction!"
                            if (backTapAction == "Take Screenshot") {
                                onNavigateToNiceShot()
                            } else {
                                Toast.makeText(context, "Back-Tap Action executed: $backTapAction", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF12B76A)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Test Back-Tap Action")
                    }
                }
            }

            // Quick Android System Settings Launchers
            item {
                Text("Direct System Settings Shortcuts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Fast-launch official Android system panels without browsing deep menus.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val shortcuts = listOf(
                Triple("Display & Brightness", Settings.ACTION_DISPLAY_SETTINGS, Icons.Default.BrightnessMedium),
                Triple("Sound & Vibration", Settings.ACTION_SOUND_SETTINGS, Icons.Default.VolumeUp),
                Triple("Battery & Power", Settings.ACTION_BATTERY_SAVER_SETTINGS, Icons.Default.BatteryFull),
                Triple("Applications Manager", Settings.ACTION_APPLICATION_SETTINGS, Icons.Default.Apps),
                Triple("Security & Privacy", Settings.ACTION_SECURITY_SETTINGS, Icons.Default.Security),
                Triple("Date & Time Setup", Settings.ACTION_DATE_SETTINGS, Icons.Default.Schedule)
            )

            shortcuts.forEach { (label, action, icon) ->
                item {
                    OneUICard(onClick = { launchSettings(action, label) }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                    Text("Official Android Intent", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
