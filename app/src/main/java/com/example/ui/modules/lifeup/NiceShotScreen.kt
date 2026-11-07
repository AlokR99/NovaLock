package com.example.ui.modules.lifeup

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard

data class DrawStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val isBlur: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NiceShotScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeTool by remember { mutableStateOf("Pen") }
    var selectedHex by remember { mutableStateOf("#0086C9") }
    var strokeThickness by remember { mutableFloatStateOf(6f) }
    var stickerText by remember { mutableStateOf("CONFIDENTIAL") }

    val strokes = remember { mutableStateListOf<DrawStroke>() }
    var currentPoints = remember { mutableStateListOf<Offset>() }

    val strokeColor = remember(selectedHex) {
        try { Color(android.graphics.Color.parseColor(selectedHex)) } catch (_: Exception) { Color(0xFF0086C9) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nice Shot Studio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("niceshot_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { strokes.clear(); currentPoints.clear() }) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Clear canvas")
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
            // Interactive Screenshot Editor Canvas
            item {
                Text("Screenshot Annotation Canvas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF141926))
                        .border(2.dp, Color(0xFF0086C9).copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .pointerInput(activeTool) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints.clear()
                                    currentPoints.add(offset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPoints.add(change.position)
                                },
                                onDragEnd = {
                                    if (currentPoints.isNotEmpty()) {
                                        strokes.add(
                                            DrawStroke(
                                                points = currentPoints.toList(),
                                                color = if (activeTool == "Privacy Blur") Color.Black.copy(alpha = 0.85f) else strokeColor,
                                                strokeWidth = if (activeTool == "Privacy Blur") 28f else strokeThickness,
                                                isBlur = activeTool == "Privacy Blur"
                                            )
                                        )
                                        currentPoints.clear()
                                    }
                                }
                            )
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Simulated captured screenshot card underneath
                        drawRoundRect(
                            color = Color(0xFF1E2638),
                            topLeft = Offset(24.dp.toPx(), 24.dp.toPx()),
                            size = Size(size.width - 48.dp.toPx(), size.height - 48.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx(), 20.dp.toPx())
                        )

                        // Render completed strokes
                        strokes.forEach { stroke ->
                            for (i in 0 until stroke.points.size - 1) {
                                val p1 = stroke.points[i]
                                val p2 = stroke.points[i + 1]
                                drawLine(
                                    color = stroke.color,
                                    start = p1,
                                    end = p2,
                                    strokeWidth = stroke.strokeWidth,
                                    cap = StrokeCap.Round
                                )
                            }
                        }

                        // Render active drawing stroke
                        for (i in 0 until currentPoints.size - 1) {
                            drawLine(
                                color = if (activeTool == "Privacy Blur") Color.Black.copy(alpha = 0.85f) else strokeColor,
                                start = currentPoints[i],
                                end = currentPoints[i + 1],
                                strokeWidth = if (activeTool == "Privacy Blur") 28f else strokeThickness,
                                cap = StrokeCap.Round
                            )
                        }
                    }

                    // Watermark badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                    ) {
                        Text(
                            text = "Mode: $activeTool (Draw or blur sensitive areas)",
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Annotation Tools
            item {
                OneUICard {
                    Text("Annotation & Editing Tool", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val tools = listOf("Pen", "Highlighter", "Privacy Blur", "Rectangle")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        tools.forEach { t ->
                            FilterChip(
                                selected = activeTool == t,
                                onClick = { activeTool = t },
                                label = { Text(t, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Pen Color Palette
            item {
                OneUICard {
                    Text("Pen Ink Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    ColorPaletteRow(
                        selectedHex = selectedHex,
                        onSelectHex = { selectedHex = it },
                        colors = listOf("#0086C9", "#E31B78", "#12B76A", "#F79009", "#7A5AF8", "#FFFFFF", "#000000")
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Stroke Thickness: ${strokeThickness.toInt()} px", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = strokeThickness,
                        onValueChange = { strokeThickness = it },
                        valueRange = 2f..20f
                    )
                }
            }

            // Save / Export Annotated Screenshot
            item {
                Button(
                    onClick = {
                        Toast.makeText(context, "Screenshot exported successfully to Gallery!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0086C9)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("export_screenshot_btn")
                ) {
                    Icon(Icons.Default.SaveAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Annotated Screenshot", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
