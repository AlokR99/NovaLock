package com.example.ui.modules.makeup

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EdgeLightingConfig
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdgeLightingScreen(
    config: EdgeLightingConfig,
    onUpdateConfig: ((EdgeLightingConfig) -> EdgeLightingConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSimulatingNotification by remember { mutableStateOf(false) }

    val glowColor = remember(config.glowColorHex) {
        try { Color(android.graphics.Color.parseColor(config.glowColorHex)) } catch (_: Exception) { Color(0xFF00C7BE) }
    }

    // Edge animation loop
    val transition = rememberInfiniteTransition(label = "edge_glow")
    val sweepAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )

    val pulseAlpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(isSimulatingNotification) {
        if (isSimulatingNotification) {
            delay(3500)
            isSimulatingNotification = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edge Lighting+", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("edgelighting_back_btn")) {
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
            // Screen Perimeter Glow Canvas Simulator
            item {
                Text(
                    text = "Perimeter Edge Glow Simulator",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0A0C14))
                ) {
                    // Animated Edge Lighting Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = config.strokeWidthDp.dp.toPx()
                        val rectSize = Size(size.width - stroke, size.height - stroke)
                        val offset = Offset(stroke / 2f, stroke / 2f)

                        when (config.effectStyle) {
                            "Pulsing Glow" -> {
                                drawRoundRect(
                                    color = glowColor.copy(alpha = pulseAlpha),
                                    topLeft = offset,
                                    size = rectSize,
                                    cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx()),
                                    style = Stroke(width = stroke)
                                )
                            }
                            else -> {
                                // Waterfall Sweep Gradient
                                val gradientBrush = Brush.sweepGradient(
                                    colors = listOf(
                                        glowColor.copy(alpha = 0.1f),
                                        glowColor,
                                        Color(0xFF7A5AF8),
                                        glowColor.copy(alpha = 0.1f)
                                    ),
                                    center = Offset(size.width / 2f, size.height / 2f)
                                )
                                drawRoundRect(
                                    brush = gradientBrush,
                                    topLeft = offset,
                                    size = rectSize,
                                    cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx()),
                                    style = Stroke(width = stroke)
                                )
                            }
                        }
                    }

                    // Simulated Incoming Notification Banner
                    if (isSimulatingNotification) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF1E2130),
                            border = androidx.compose.foundation.BorderStroke(1.dp, glowColor),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 24.dp, start = 20.dp, end = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(glowColor)
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("NovaLock Alert", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Edge lighting notification triggered!", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Edge Lighting Active",
                            color = glowColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Glow Animation Style
            item {
                OneUICard {
                    Text("Lighting Animation Pattern", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val styles = listOf("Neon Waterfall", "Pulsing Glow", "Cosmic Trail", "Dual Corner Wave")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        styles.forEach { style ->
                            FilterChip(
                                selected = config.effectStyle == style,
                                onClick = { onUpdateConfig { it.copy(effectStyle = style) } },
                                label = { Text(style, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Color Palette
            item {
                OneUICard {
                    Text("Glow Accent Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ColorPaletteRow(
                        selectedHex = config.glowColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(glowColorHex = hex) } },
                        colors = listOf("#00C7BE", "#0381FE", "#7A5AF8", "#E31B78", "#12B76A", "#F79009")
                    )
                }
            }

            // Stroke Width Slider
            item {
                OneUICard {
                    Text("Border Glow Thickness: ${config.strokeWidthDp} dp", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = config.strokeWidthDp.toFloat(),
                        onValueChange = { onUpdateConfig { cfg -> cfg.copy(strokeWidthDp = it.toInt()) } },
                        valueRange = 4f..16f,
                        steps = 5
                    )
                }
            }

            // Test Notification Button
            item {
                Button(
                    onClick = { isSimulatingNotification = true },
                    colors = ButtonDefaults.buttonColors(containerColor = glowColor),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("test_edge_glow_btn")
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Test Notification Edge Glow", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
