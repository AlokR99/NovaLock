package com.example.ui.modules.makeup

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LockStarConfig
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockStarScreen(
    config: LockStarConfig,
    onUpdateConfig: ((LockStarConfig) -> LockStarConfig) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var stickerInput by remember { mutableStateOf(config.stickerText) }

    val currentTime = remember {
        val sdf = if (config.is24Hour) SimpleDateFormat("HH:mm", Locale.getDefault()) else SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.format(Date())
    }
    val currentDate = remember {
        SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date())
    }

    val clockColor = remember(config.clockColorHex) {
        try {
            Color(android.graphics.Color.parseColor(config.clockColorHex))
        } catch (_: Exception) {
            Color(0xFF0381FE)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LockStar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("lockstar_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onResetConfig) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset module")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
            // Live Lock Screen Preview Simulation
            item {
                Text(
                    text = "Live Lock Screen Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF0A0C14))
                        .border(2.dp, clockColor.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Clock Presentation based on style
                        when (config.clockStyle) {
                            "Stacked Modern" -> {
                                Text(
                                    text = currentTime.substringBefore(":"),
                                    fontSize = (56 * config.clockScale).sp,
                                    fontWeight = FontWeight.Black,
                                    color = clockColor,
                                    lineHeight = (54 * config.clockScale).sp
                                )
                                Text(
                                    text = currentTime.substringAfter(":"),
                                    fontSize = (56 * config.clockScale).sp,
                                    fontWeight = FontWeight.Black,
                                    color = clockColor.copy(alpha = 0.8f),
                                    lineHeight = (54 * config.clockScale).sp
                                )
                            }
                            "Bold Digital" -> {
                                Text(
                                    text = currentTime,
                                    fontSize = (44 * config.clockScale).sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = clockColor
                                )
                            }
                            "Minimalist Analog" -> {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size((90 * config.clockScale).dp)
                                        .clip(CircleShape)
                                        .border(2.dp, clockColor, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = "Analog Clock",
                                        tint = clockColor,
                                        modifier = Modifier.size((48 * config.clockScale).dp)
                                    )
                                }
                            }
                            else -> {
                                Text(
                                    text = currentTime,
                                    fontSize = (40 * config.clockScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = clockColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.75f)
                        )

                        if (config.stickerText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = config.stickerText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Bottom lock shortcuts preview
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = "Phone", tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Clock Style Selector
            item {
                OneUICard {
                    Text(
                        text = "Clock Typography Style",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    val styles = listOf("Stacked Modern", "Bold Digital", "Minimalist Analog", "Cyberpunk Clean")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        styles.forEach { style ->
                            FilterChip(
                                selected = config.clockStyle == style,
                                onClick = { onUpdateConfig { it.copy(clockStyle = style) } },
                                label = { Text(style, style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Clock Color Customizer
            item {
                OneUICard {
                    Text(
                        text = "Clock Color Accent",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ColorPaletteRow(
                        selectedHex = config.clockColorHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(clockColorHex = hex) } }
                    )
                }
            }

            // Size & Format Toggles
            item {
                OneUICard {
                    Text(
                        text = "Dimensions & Formats",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Clock Scale: ${(config.clockScale * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Slider(
                        value = config.clockScale,
                        onValueChange = { scale -> onUpdateConfig { it.copy(clockScale = scale) } },
                        valueRange = 0.7f..1.4f,
                        steps = 7,
                        modifier = Modifier.testTag("lockstar_scale_slider")
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("24-Hour Military Format", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = config.is24Hour,
                            onCheckedChange = { is24 -> onUpdateConfig { it.copy(is24Hour = is24) } }
                        )
                    }
                }
            }

            // Custom Sticker Text
            item {
                OneUICard {
                    Text(
                        text = "Lock Screen Custom Sticker",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = stickerInput,
                        onValueChange = {
                            stickerInput = it
                            onUpdateConfig { cfg -> cfg.copy(stickerText = it) }
                        },
                        placeholder = { Text("E.g. ✨ Strive for greatness") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Set Lockscreen Wallpaper via WallpaperManager
            item {
                Button(
                    onClick = {
                        try {
                            val wm = WallpaperManager.getInstance(context)
                            val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
                            val canvas = Canvas(bitmap)
                            val paint = Paint().apply {
                                color = android.graphics.Color.parseColor(config.clockColorHex)
                                isAntiAlias = true
                            }
                            canvas.drawColor(android.graphics.Color.parseColor("#0A0C14"))
                            // Draw decorative lockstar circles
                            paint.alpha = 40
                            canvas.drawCircle(540f, 600f, 400f, paint)
                            paint.alpha = 25
                            canvas.drawCircle(540f, 1200f, 600f, paint)

                            wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                            Toast.makeText(context, "Lockscreen wallpaper applied successfully!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lockscreen updated locally in LockStar!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("apply_lockstar_btn")
                ) {
                    Icon(Icons.Default.Wallpaper, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apply Lock Screen Design", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
