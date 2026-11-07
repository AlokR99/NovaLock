package com.example.ui.modules.lifeup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OneUICard

data class TouchPoint(val offset: Offset, val isRejected: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdgeTouchScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deadZoneDp by remember { mutableFloatStateOf(20f) }
    val tapLogs = remember { mutableStateListOf<TouchPoint>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edge Touch", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("edgetouch_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { tapLogs.clear() }) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Clear")
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
            // Touch Rejection Visualizer Canvas
            item {
                Text("Touch Rejection Simulator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0C101A))
                        .border(2.dp, Color(0xFFE31B78).copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .pointerInput(deadZoneDp) {
                            detectTapGestures { offset ->
                                val deadPx = deadZoneDp * density
                                val isRejected = offset.x <= deadPx || offset.x >= (size.width - deadPx)
                                tapLogs.add(TouchPoint(offset, isRejected))
                            }
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val deadPx = deadZoneDp.dp.toPx()

                        // Left dead zone (Red tint)
                        drawRect(
                            color = Color(0xFFF04438).copy(alpha = 0.25f),
                            topLeft = Offset.Zero,
                            size = Size(deadPx, size.height)
                        )

                        // Right dead zone (Red tint)
                        drawRect(
                            color = Color(0xFFF04438).copy(alpha = 0.25f),
                            topLeft = Offset(size.width - deadPx, 0f),
                            size = Size(deadPx, size.height)
                        )

                        // Center accepted zone (Green tint)
                        drawRect(
                            color = Color(0xFF12B76A).copy(alpha = 0.08f),
                            topLeft = Offset(deadPx, 0f),
                            size = Size(size.width - 2 * deadPx, size.height)
                        )

                        // Render tap markers
                        tapLogs.takeLast(15).forEach { tap ->
                            drawCircle(
                                color = if (tap.isRejected) Color(0xFFF04438) else Color(0xFF12B76A),
                                radius = 16f,
                                center = tap.offset
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Tap edges (Rejected in Red) vs center (Accepted in Green)",
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Dead Zone Margin Slider
            item {
                OneUICard {
                    Text("Edge Dead Zone Width: ${deadZoneDp.toInt()} dp", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Increases grip palm touch rejection margin along curved or thin bezel edges.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = deadZoneDp,
                        onValueChange = { deadZoneDp = it },
                        valueRange = 8f..36f,
                        steps = 7
                    )
                }
            }

            // Grip Ergonomics Guidelines
            item {
                OneUICard {
                    Text("Accidental Touch Prevention Tips", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• On bezel-less screens, resting the palm on the lower edge often sends unwanted taps.", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• A 20dp dead margin filters false taps without hindering thumb reach to keyboard buttons.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
