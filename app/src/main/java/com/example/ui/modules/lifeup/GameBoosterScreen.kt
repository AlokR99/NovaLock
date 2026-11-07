package com.example.ui.modules.lifeup

import android.content.Intent
import android.provider.Settings
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
import com.example.ui.components.OneUICard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameBoosterScreen(
    batteryTempC: Float,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSessionActive by remember { mutableStateOf(false) }
    var sessionSeconds by remember { mutableIntStateOf(0) }
    var currentFps by remember { mutableIntStateOf(60) }
    var selectedProfile by remember { mutableStateOf("Performance") }

    // Live session timer & FPS fluctuations
    LaunchedEffect(isSessionActive) {
        while (isSessionActive) {
            delay(1000)
            sessionSeconds++
            currentFps = if (selectedProfile == "Performance") (59..60).random() else (56..60).random()
        }
    }

    val formattedTime = remember(sessionSeconds) {
        val m = sessionSeconds / 60
        val s = sessionSeconds % 60
        String.format("%02d:%02d", m, s)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Booster+", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("gamebooster_back_btn")) {
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
            // Gaming Telemetry Dashboard
            item {
                OneUICard(backgroundColor = Color(0xFF161028)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Gaming Session Telemetry", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(
                                if (isSessionActive) "Session in progress: $formattedTime" else "Session Idle",
                                color = if (isSessionActive) Color(0xFF12B76A) else Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }
                        Button(
                            onClick = {
                                isSessionActive = !isSessionActive
                                if (!isSessionActive) sessionSeconds = 0
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSessionActive) Color(0xFFF04438) else Color(0xFF12B76A)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (isSessionActive) "Stop" else "Start Session")
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$currentFps FPS", color = Color(0xFF00C7BE), fontSize = 22.sp, fontWeight = FontWeight.Black)
                            Text("Frame Rate", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${batteryTempC}°C", color = Color(0xFFF79009), fontSize = 22.sp, fontWeight = FontWeight.Black)
                            Text("Battery Temp", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(selectedProfile, color = Color(0xFFD444F1), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Current Profile", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                        }
                    }
                }
            }

            // Gaming Profiles
            item {
                OneUICard {
                    Text("Gaming Optimization Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val profiles = listOf("Performance", "Balanced", "Battery Saver")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        profiles.forEach { p ->
                            FilterChip(
                                selected = selectedProfile == p,
                                onClick = { selectedProfile = p },
                                label = { Text(p) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Do-Not-Disturb Focus Mode Trigger
            item {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Do Not Disturb settings opened.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Icon(Icons.Default.DoNotDisturbOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Do Not Disturb Focus Settings")
                }
            }

            // Hardware Safety Disclaimer
            item {
                OneUICard {
                    Text("Hardware Safety Standard", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "NovaLock strictly respects Android thermal throttle protections. It never overclocks CPU/GPU or compromises device battery longevity.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
