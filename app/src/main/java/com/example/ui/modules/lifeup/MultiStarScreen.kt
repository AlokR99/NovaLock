package com.example.ui.modules.lifeup

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
fun MultiStarScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPipPlaying by remember { mutableStateOf(true) }
    var splitRatio by remember { mutableFloatStateOf(0.5f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MultiStar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("multistar_back_btn")) {
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
            // Live Split-Screen App Pair Simulator
            item {
                Text(
                    text = "Split-Screen & Multi-Window Simulator",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0F111A))
                        .border(2.dp, Color(0xFFF04438).copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Top App (e.g. Video / Browser)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF1E2130),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(splitRatio)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(Icons.Default.PlayCircle, contentDescription = null, tint = Color(0xFFF04438), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("App A: Media Player", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Split divider handle
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(4.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.4f))
                            )
                        }

                        // Bottom App (e.g. Chat / Notes)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF172033),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f - splitRatio)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, tint = Color(0xFF0381FE), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("App B: Messages / Notes", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Split Ratio Control
            item {
                OneUICard {
                    Text("Split Window Ratio: ${(splitRatio * 100).toInt()} / ${((1f - splitRatio) * 100).toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = splitRatio,
                        onValueChange = { splitRatio = it },
                        valueRange = 0.3f..0.7f
                    )
                }
            }

            // Floating PIP Mini Player Demonstration
            item {
                OneUICard {
                    Text("Picture-in-Picture (PIP) Floating Mode", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Supported media activities can shrink into a floating overlay while you multitask.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFF1A1C28),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF04438).copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth().height(90.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF04438).copy(alpha = 0.2f))
                                ) {
                                    Icon(
                                        imageVector = if (isPipPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color(0xFFF04438)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("PIP Video Overlay", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Text(if (isPipPlaying) "Status: Floating active" else "Status: Paused", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                                }
                            }
                            Button(
                                onClick = { isPipPlaying = !isPipPlaying },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF04438))
                            ) {
                                Text(if (isPipPlaying) "Pause" else "Play", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Multi-window gestures & Foldables Guide
            item {
                OneUICard {
                    Text("Multitasking Tips & Foldable Support", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• On Android 12+, swipe up and hold from the bottom of any supported app, then tap the app icon to choose 'Split Top'.", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• On foldable and tablet displays, multi-pane layouts enable 3 apps to run simultaneously with adaptive aspect ratios.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
