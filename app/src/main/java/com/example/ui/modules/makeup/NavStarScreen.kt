package com.example.ui.modules.makeup

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NavStarConfig
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavStarScreen(
    config: NavStarConfig,
    onUpdateConfig: ((NavStarConfig) -> NavStarConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lastActionFeedback by remember { mutableStateOf("Tap any button to test haptics") }
    var activeTutorial by remember { mutableStateOf("Back Gesture") }

    fun triggerHaptic(label: String) {
        lastActionFeedback = "Triggered: $label"
        try {
            val vibrator = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(25)
            }
        } catch (_: Exception) {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NavStar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("navstar_back_btn")) {
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
            // Live Interactive Navigation Bar Preview
            item {
                Text(
                    text = "Navigation Bar Studio Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OneUICard(backgroundColor = Color(0xFF0F111A)) {
                    Text(
                        text = lastActionFeedback,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // 3-Button Nav Bar Representation
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth().height(60.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)
                        ) {
                            // Recents Button
                            IconButton(onClick = { triggerHaptic("Recent Apps") }) {
                                when (config.buttonStyle) {
                                    "Classic Galaxy" -> Icon(Icons.Default.Menu, contentDescription = "Recents", tint = Color.White)
                                    "Cyberpunk" -> Icon(Icons.Default.GridView, contentDescription = "Recents", tint = Color(0xFF06AED4))
                                    "Geometric Lines" -> Icon(Icons.Default.DensityMedium, contentDescription = "Recents", tint = Color.White)
                                    else -> Icon(Icons.Default.ViewAgenda, contentDescription = "Recents", tint = Color.White)
                                }
                            }

                            // Home Button
                            IconButton(onClick = { triggerHaptic("Home") }) {
                                when (config.buttonStyle) {
                                    "Classic Galaxy" -> Icon(Icons.Default.RadioButtonUnchecked, contentDescription = "Home", tint = Color.White, modifier = Modifier.size(28.dp))
                                    "Cyberpunk" -> Icon(Icons.Default.Hexagon, contentDescription = "Home", tint = Color(0xFF0381FE), modifier = Modifier.size(26.dp))
                                    "Geometric Lines" -> Icon(Icons.Default.CropSquare, contentDescription = "Home", tint = Color.White, modifier = Modifier.size(24.dp))
                                    else -> Icon(Icons.Default.Circle, contentDescription = "Home", tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                            }

                            // Back Button
                            IconButton(onClick = { triggerHaptic("Back") }) {
                                when (config.buttonStyle) {
                                    "Classic Galaxy" -> Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                                    "Cyberpunk" -> Icon(Icons.Default.ChevronLeft, contentDescription = "Back", tint = Color(0xFF06AED4), modifier = Modifier.size(32.dp))
                                    "Geometric Lines" -> Icon(Icons.Default.PlayArrow, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(22.dp))
                                    else -> Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(30.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gesture Pill Bar Representation
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gesture Handle Appearance", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(config.gesturePillWidthDp.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }

            // Navigation Button Style Choices
            item {
                OneUICard {
                    Text("Button Design Theme", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val styles = listOf("OneUI Minimal", "Classic Galaxy", "Cyberpunk", "Geometric Lines")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        styles.forEach { style ->
                            FilterChip(
                                selected = config.buttonStyle == style,
                                onClick = { onUpdateConfig { it.copy(buttonStyle = style) } },
                                label = { Text(style, fontSize = 12.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Gesture Handle Sizing
            item {
                OneUICard {
                    Text("Gesture Handle Width: ${config.gesturePillWidthDp} dp", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = config.gesturePillWidthDp.toFloat(),
                        onValueChange = { width -> onUpdateConfig { it.copy(gesturePillWidthDp = width.toInt()) } },
                        valueRange = 48f..140f,
                        steps = 5
                    )
                }
            }

            // Gesture Navigation Interactive Guides
            item {
                OneUICard {
                    Text("Interactive Gesture Tutorials", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Back Gesture", "Home Gesture", "Recent Apps").forEach { tut ->
                            FilterChip(
                                selected = activeTutorial == tut,
                                onClick = { activeTutorial = tut },
                                label = { Text(tut) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().padding(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            when (activeTutorial) {
                                "Back Gesture" -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("Swipe inwards from either the left or right edge of the screen to go back.", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                                "Home Gesture" -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("Swipe up quickly from the bottom gesture line to return to the Home screen.", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                                else -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.PanTool, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("Swipe up from the bottom edge and hold for 0.5s to reveal open recent apps.", style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
