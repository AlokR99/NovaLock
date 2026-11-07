package com.example.ui.modules.lifeup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneHandOperationScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var leftAction by remember { mutableStateOf("Back Key") }
    var rightAction by remember { mutableStateOf("Quick Settings") }
    var handleHeightDp by remember { mutableFloatStateOf(140f) }
    var swipeStatus by remember { mutableStateOf("Swipe from left or right edge below to test") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("One Hand Operation+", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("onehand_back_btn")) {
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
            // Interactive Swipe Test Area
            item {
                Text("Interactive Gesture Test Pad", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0C101A))
                        .border(2.dp, Color(0xFF32D583).copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {},
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    if (dragAmount > 25) {
                                        swipeStatus = "Triggered: Left Edge ($leftAction)"
                                    } else if (dragAmount < -25) {
                                        swipeStatus = "Triggered: Right Edge ($rightAction)"
                                    }
                                }
                            )
                        }
                ) {
                    // Left Edge Indicator
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .width(12.dp)
                            .height(handleHeightDp.dp)
                            .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                            .background(Color(0xFF32D583).copy(alpha = 0.8f))
                    )

                    // Right Edge Indicator
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(12.dp)
                            .height(handleHeightDp.dp)
                            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                            .background(Color(0xFF0381FE).copy(alpha = 0.8f))
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.Center).padding(20.dp)
                    ) {
                        Icon(Icons.Default.PanTool, contentDescription = null, tint = Color(0xFF32D583), modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = swipeStatus,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Swipe from left or right screen boundary",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Left Edge Action Mapping
            item {
                OneUICard {
                    Text("Left Handle Action", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val actions = listOf("Back Key", "Home Key", "Recent Apps", "Screenshot Tool", "Mute Audio")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        actions.take(3).forEach { act ->
                            FilterChip(
                                selected = leftAction == act,
                                onClick = { leftAction = act },
                                label = { Text(act, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Right Edge Action Mapping
            item {
                OneUICard {
                    Text("Right Handle Action", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    val actions = listOf("Quick Settings", "Screenshot Tool", "Back Key", "Torch")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        actions.forEach { act ->
                            FilterChip(
                                selected = rightAction == act,
                                onClick = { rightAction = act },
                                label = { Text(act, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Handle Height Slider
            item {
                OneUICard {
                    Text("Handle Length: ${handleHeightDp.toInt()} dp", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = handleHeightDp,
                        onValueChange = { handleHeightDp = it },
                        valueRange = 80f..220f
                    )
                }
            }
        }
    }
}
