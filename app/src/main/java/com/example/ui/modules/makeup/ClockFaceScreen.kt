package com.example.ui.modules.makeup

import android.widget.Toast
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ClockFaceConfig
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockFaceScreen(
    config: ClockFaceConfig,
    onUpdateConfig: ((ClockFaceConfig) -> ClockFaceConfig) -> Unit,
    onSaveDesign: (title: String, category: String, colorHex: String, description: String) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var calendar by remember { mutableStateOf(Calendar.getInstance()) }

    // Live ticking loop
    LaunchedEffect(Unit) {
        while (true) {
            calendar = Calendar.getInstance()
            delay(1000)
        }
    }

    val hours = calendar.get(Calendar.HOUR)
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)

    val primaryColor = remember(config.primaryColorHex) {
        try { Color(android.graphics.Color.parseColor(config.primaryColorHex)) } catch (_: Exception) { Color(0xFF0381FE) }
    }
    val secondaryColor = remember(config.secondaryColorHex) {
        try { Color(android.graphics.Color.parseColor(config.secondaryColorHex)) } catch (_: Exception) { Color(0xFF06AED4) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ClockFace", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("clockface_back_btn")) {
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
            // Live Clock Preview Canvas
            item {
                Text(
                    text = "Live Clock Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0C0E17))
                        .border(2.dp, primaryColor.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
                        .padding(16.dp)
                ) {
                    when (config.clockType) {
                        "Bauhaus Analog" -> {
                            // Canvas Analog Clock with real ticking hands
                            Canvas(modifier = Modifier.size(200.dp)) {
                                val center = Offset(size.width / 2f, size.height / 2f)
                                val radius = size.width / 2f - 12.dp.toPx()

                                // Dial circle
                                drawCircle(
                                    color = Color.White.copy(alpha = 0.08f),
                                    radius = radius,
                                    center = center
                                )
                                drawCircle(
                                    color = primaryColor.copy(alpha = 0.4f),
                                    radius = radius,
                                    center = center,
                                    style = Stroke(width = 3.dp.toPx())
                                )

                                // Hour ticks
                                for (i in 0 until 12) {
                                    val angle = (i * 30.0) * (PI / 180.0)
                                    val startX = center.x + (radius - 12.dp.toPx()) * sin(angle).toFloat()
                                    val startY = center.y - (radius - 12.dp.toPx()) * cos(angle).toFloat()
                                    val endX = center.x + radius * sin(angle).toFloat()
                                    val endY = center.y - radius * cos(angle).toFloat()
                                    drawLine(
                                        color = if (i % 3 == 0) primaryColor else Color.White.copy(alpha = 0.4f),
                                        start = Offset(startX, startY),
                                        end = Offset(endX, endY),
                                        strokeWidth = if (i % 3 == 0) 3.dp.toPx() else 1.5.dp.toPx()
                                    )
                                }

                                // Hour hand
                                val hourAngle = ((hours % 12 + minutes / 60.0) * 30.0) * (PI / 180.0)
                                val hourHandLength = radius * 0.55f
                                drawLine(
                                    color = Color.White,
                                    start = center,
                                    end = Offset(
                                        center.x + hourHandLength * sin(hourAngle).toFloat(),
                                        center.y - hourHandLength * cos(hourAngle).toFloat()
                                    ),
                                    strokeWidth = 5.dp.toPx(),
                                    cap = StrokeCap.Round
                                )

                                // Minute hand
                                val minAngle = (minutes * 6.0) * (PI / 180.0)
                                val minHandLength = radius * 0.75f
                                drawLine(
                                    color = secondaryColor,
                                    start = center,
                                    end = Offset(
                                        center.x + minHandLength * sin(minAngle).toFloat(),
                                        center.y - minHandLength * cos(minAngle).toFloat()
                                    ),
                                    strokeWidth = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )

                                // Second hand
                                if (config.showSeconds) {
                                    val secAngle = (seconds * 6.0) * (PI / 180.0)
                                    val secHandLength = radius * 0.85f
                                    drawLine(
                                        color = primaryColor,
                                        start = center,
                                        end = Offset(
                                            center.x + secHandLength * sin(secAngle).toFloat(),
                                            center.y - secHandLength * cos(secAngle).toFloat()
                                        ),
                                        strokeWidth = 2.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }

                                // Center pin
                                drawCircle(color = primaryColor, radius = 5.dp.toPx(), center = center)
                            }
                        }
                        else -> {
                            // Digital Gradient Clock Presentation
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                                val secStr = String.format("%02d", seconds)
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = timeStr,
                                        fontSize = 58.sp,
                                        fontWeight = FontWeight.Black,
                                        color = primaryColor,
                                        lineHeight = 60.sp
                                    )
                                    if (config.showSeconds) {
                                        Text(
                                            text = ":$secStr",
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = secondaryColor,
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        )
                                    }
                                }
                                if (config.showDate) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(calendar.time),
                                        fontSize = 15.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Clock Type Style Choices
            item {
                OneUICard {
                    Text("Clock Design Typology", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    val styles = listOf("Dual Gradient Digital", "Bauhaus Analog", "Neon Glow", "Typography Big")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        styles.forEach { style ->
                            FilterChip(
                                selected = config.clockType == style,
                                onClick = { onUpdateConfig { it.copy(clockType = style) } },
                                label = { Text(style, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Colors
            item {
                OneUICard {
                    Text("Primary Hand / Glyph Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ColorPaletteRow(
                        selectedHex = config.primaryColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(primaryColorHex = hex) } }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Secondary / Accent Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ColorPaletteRow(
                        selectedHex = config.secondaryColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(secondaryColorHex = hex) } },
                        colors = listOf("#06AED4", "#7A5AF8", "#12B76A", "#F79009", "#E31B78", "#FFFFFF")
                    )
                }
            }

            // Elements Toggles
            item {
                OneUICard {
                    Text("Clock Elements", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Show Seconds Indicator")
                        Switch(
                            checked = config.showSeconds,
                            onCheckedChange = { onUpdateConfig { cfg -> cfg.copy(showSeconds = it) } }
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Show Current Date")
                        Switch(
                            checked = config.showDate,
                            onCheckedChange = { onUpdateConfig { cfg -> cfg.copy(showDate = it) } }
                        )
                    }
                }
            }

            // Save Design Button
            item {
                Button(
                    onClick = {
                        onSaveDesign(
                            "${config.clockType} Clock",
                            "ClockFace",
                            config.primaryColorHex,
                            "Custom ClockFace design with ${config.primaryColorHex} primary accents"
                        )
                        Toast.makeText(context, "Clock design saved to My Designs!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("save_clock_btn")
                ) {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Clock Design to My Designs", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
