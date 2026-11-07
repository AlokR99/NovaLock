package com.example.ui.modules.makeup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PentasticConfig
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PentasticScreen(
    config: PentasticConfig,
    onUpdateConfig: ((PentasticConfig) -> PentasticConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawnPoints = remember { mutableStateListOf<Offset>() }
    var currentCursorPos by remember { mutableStateOf(Offset(200f, 200f)) }

    val pointerColor = remember(config.pointerColorHex) {
        try { Color(android.graphics.Color.parseColor(config.pointerColorHex)) } catch (_: Exception) { Color(0xFF6172F3) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pentastic", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("pentastic_back_btn")) {
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
            // Interactive Stylus Sketch Pad
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Interactive Stylus Drawing Pad",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { drawnPoints.clear() }) {
                        Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear Pad")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0F111A))
                        .border(2.dp, pointerColor.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentCursorPos = offset
                                    drawnPoints.add(offset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentCursorPos = change.position
                                    drawnPoints.add(change.position)
                                }
                            )
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Draw lines between consecutive points
                        for (i in 0 until drawnPoints.size - 1) {
                            val p1 = drawnPoints[i]
                            val p2 = drawnPoints[i + 1]
                            // skip far jumps
                            if ((p1 - p2).getDistance() < 80f) {
                                drawLine(
                                    color = pointerColor,
                                    start = p1,
                                    end = p2,
                                    strokeWidth = 5.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        }

                        // Draw pointer icon at current cursor position
                        val c = currentCursorPos
                        val scale = config.cursorScale
                        when (config.pointerStyle) {
                            "Precision Crosshair" -> {
                                drawLine(pointerColor, Offset(c.x - 14 * scale, c.y), Offset(c.x + 14 * scale, c.y), 2.5f)
                                drawLine(pointerColor, Offset(c.x, c.y - 14 * scale), Offset(c.x, c.y + 14 * scale), 2.5f)
                                drawCircle(Color.White, 3f * scale, c)
                            }
                            "Cyber Star" -> {
                                drawCircle(pointerColor, 8f * scale, c)
                                drawCircle(Color.White, 4f * scale, c)
                            }
                            else -> {
                                drawCircle(pointerColor, 10f * scale, c)
                                drawCircle(Color.White, 5f * scale, c)
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Draw with your finger or stylus to test pointer cursor",
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Pointer Styles
            item {
                OneUICard {
                    Text("Pointer Cursor Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val styles = listOf("Cyber Star", "Precision Crosshair", "Elegant Arrow", "Neon Dot")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        styles.forEach { style ->
                            FilterChip(
                                selected = config.pointerStyle == style,
                                onClick = { onUpdateConfig { it.copy(pointerStyle = style) } },
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
                    Text("Cursor Accent Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ColorPaletteRow(
                        selectedHex = config.pointerColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(pointerColorHex = hex) } }
                    )
                }
            }

            // Cursor Scale Slider
            item {
                OneUICard {
                    Text("Pointer Cursor Scale: ${(config.cursorScale * 100).toInt()}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = config.cursorScale,
                        onValueChange = { onUpdateConfig { cfg -> cfg.copy(cursorScale = it) } },
                        valueRange = 0.7f..1.5f,
                        steps = 7
                    )
                }
            }
        }
    }
}
