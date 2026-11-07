package com.example.ui.modules.lifeup

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayAssistantScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isComfortFilterOn by remember { mutableStateOf(false) }
    var filterWarmth by remember { mutableFloatStateOf(0.4f) }

    val refreshRateHz = remember {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.display?.mode?.refreshRate?.toInt() ?: 60
            } else {
                @Suppress("DEPRECATION")
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
                wm?.defaultDisplay?.mode?.refreshRate?.toInt() ?: 60
            }
        } catch (_: Exception) {
            60
        }
    }

    val displayMetrics = context.resources.displayMetrics
    val densityDpi = displayMetrics.densityDpi
    val screenRes = "${displayMetrics.widthPixels} × ${displayMetrics.heightPixels}"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Display Assistant", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("displayassistant_back_btn")) {
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
            // Display Specifications Card
            item {
                OneUICard {
                    Text("Display Hardware Specifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${refreshRateHz}Hz", color = Color(0xFF0381FE), fontSize = 24.sp, fontWeight = FontWeight.Black)
                            Text("Refresh Rate", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${densityDpi} DPI", color = Color(0xFF12B76A), fontSize = 24.sp, fontWeight = FontWeight.Black)
                            Text("Screen Density", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Native Resolution: $screenRes px",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            // Eye Comfort Blue Light Filter Simulator
            item {
                OneUICard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Eye Comfort Shield Simulator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                "Applies warm amber filter to reduce eye strain during evening hours.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isComfortFilterOn,
                            onCheckedChange = { isComfortFilterOn = it }
                        )
                    }

                    if (isComfortFilterOn) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Warmth Level: ${(filterWarmth * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
                        Slider(
                            value = filterWarmth,
                            onValueChange = { filterWarmth = it },
                            valueRange = 0.2f..0.8f
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Preview box with amber tint
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF9800).copy(alpha = filterWarmth * 0.45f))
                        ) {
                            Text("Warmth Filter Active", color = Color(0xFF3E2723), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Launch System Display Settings
            item {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Display settings opened.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Icon(Icons.Default.SettingsBrightness, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Configure Screen Refresh Rate in Android Settings")
                }
            }
        }
    }
}
