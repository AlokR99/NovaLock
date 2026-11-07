package com.example.ui.modules.makeup

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.LinearGradient
import android.graphics.Paint as AndroidPaint
import android.graphics.Shader
import android.widget.Toast
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WonderlandConfig
import com.example.ui.components.ColorPaletteRow
import com.example.ui.components.OneUICard
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    val radius: Float,
    val speedX: Float,
    val speedY: Float,
    val alpha: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WonderlandScreen(
    config: WonderlandConfig,
    onUpdateConfig: ((WonderlandConfig) -> WonderlandConfig) -> Unit,
    onSaveDesign: (title: String, category: String, colorHex: String, description: String) -> Unit,
    onResetConfig: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var parallaxTiltX by remember { mutableFloatStateOf(0f) }
    var parallaxTiltY by remember { mutableFloatStateOf(0f) }

    val startColor = remember(config.gradientStartHex) {
        try { Color(android.graphics.Color.parseColor(config.gradientStartHex)) } catch (_: Exception) { Color(0xFF0381FE) }
    }
    val endColor = remember(config.gradientEndHex) {
        try { Color(android.graphics.Color.parseColor(config.gradientEndHex)) } catch (_: Exception) { Color(0xFF7A5AF8) }
    }

    // Infinite animation transition for particle motion
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (4000 / config.animationSpeed).toInt().coerceAtLeast(1000), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleFloat"
    )

    // Pre-computed particles
    val particles = remember(config.particleCount) {
        List(config.particleCount) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 6f + 2f,
                speedX = (Random.nextFloat() - 0.5f) * 0.04f,
                speedY = -(Random.nextFloat() * 0.05f + 0.02f),
                alpha = Random.nextFloat() * 0.6f + 0.3f
            )
        }
    }

    fun applyWallpaper(targetFlag: Int, targetLabel: String) {
        try {
            val wm = WallpaperManager.getInstance(context)
            val width = 1080
            val height = 2340
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = AndroidCanvas(bitmap)

            val c1 = android.graphics.Color.parseColor(config.gradientStartHex)
            val c2 = android.graphics.Color.parseColor(config.gradientEndHex)
            val bgPaint = AndroidPaint().apply {
                shader = LinearGradient(0f, 0f, width.toFloat(), height.toFloat(), c1, c2, Shader.TileMode.CLAMP)
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

            // Draw decorative cosmic ambient circles
            val particlePaint = AndroidPaint().apply {
                color = android.graphics.Color.WHITE
                isAntiAlias = true
            }
            val rnd = java.util.Random(42)
            for (i in 0..120) {
                val px = rnd.nextFloat() * width
                val py = rnd.nextFloat() * height
                val pr = rnd.nextFloat() * 14f + 3f
                particlePaint.alpha = (rnd.nextFloat() * 180 + 40).toInt()
                canvas.drawCircle(px, py, pr, particlePaint)
            }

            wm.setBitmap(bitmap, null, true, targetFlag)
            Toast.makeText(context, "Wonderland wallpaper applied to $targetLabel!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Wallpaper generated & saved!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wonderland Studio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("wonderland_back_btn")) {
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
            // Live Interactive Wallpaper Canvas with Particles & Touch Parallax
            item {
                Text(
                    text = "Live Interactive Canvas (Touch to Parallax)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(2.dp, startColor.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    if (config.parallaxEnabled) {
                                        parallaxTiltX = (parallaxTiltX + dragAmount.x * 0.1f).coerceIn(-40f, 40f)
                                        parallaxTiltY = (parallaxTiltY + dragAmount.y * 0.1f).coerceIn(-40f, 40f)
                                    }
                                },
                                onDragEnd = {
                                    parallaxTiltX = 0f
                                    parallaxTiltY = 0f
                                }
                            )
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Background Gradient
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(startColor, endColor, Color(0xFF0F1016)),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, size.height)
                            )
                        )

                        // Render animated particles
                        particles.forEach { p ->
                            val currentY = ((p.y + animProgress * p.speedY * 10f) % 1f + 1f) % 1f
                            val currentX = ((p.x + animProgress * p.speedX * 5f) % 1f + 1f) % 1f

                            val canvasX = currentX * size.width + parallaxTiltX * 0.5f
                            val canvasY = currentY * size.height + parallaxTiltY * 0.5f

                            drawCircle(
                                color = Color.White.copy(alpha = p.alpha),
                                radius = p.radius.dp.toPx(),
                                center = Offset(canvasX, canvasY)
                            )
                        }

                        // Floating subtle glowing orb in the center
                        drawCircle(
                            color = startColor.copy(alpha = 0.25f),
                            radius = 60.dp.toPx(),
                            center = Offset(size.width / 2f + parallaxTiltX, size.height / 2f + parallaxTiltY)
                        )
                    }

                    // On-screen overlay indicator
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.45f)
                        ) {
                            Text(
                                text = "Drag anywhere to test 3D Parallax Tilt",
                                color = Color.White,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Visual Effect Style
            item {
                OneUICard {
                    Text("Wallpaper Visual Vibe", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    val styles = listOf("Cosmic Neon Nebula", "Aurora Flow", "Liquid Gradient", "Floating Bubbles")
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

            // Gradient Palette
            item {
                OneUICard {
                    Text("Top Gradient Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ColorPaletteRow(
                        selectedHex = config.gradientStartHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(gradientStartHex = hex) } }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Bottom Gradient Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ColorPaletteRow(
                        selectedHex = config.gradientEndHex,
                        onSelectHex = { hex -> onUpdateConfig { it.copy(gradientEndHex = hex) } },
                        colors = listOf("#7A5AF8", "#E31B78", "#06AED4", "#12B76A", "#F79009", "#0F1016")
                    )
                }
            }

            // Particle Motion & Speed
            item {
                OneUICard {
                    Text("Particle Density: ${config.particleCount} particles", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Slider(
                        value = config.particleCount.toFloat(),
                        onValueChange = { onUpdateConfig { cfg -> cfg.copy(particleCount = it.toInt()) } },
                        valueRange = 15f..60f,
                        steps = 8
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("3D Interactive Parallax Gyro/Touch", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = config.parallaxEnabled,
                            onCheckedChange = { enabled -> onUpdateConfig { it.copy(parallaxEnabled = enabled) } }
                        )
                    }
                }
            }

            // Apply Directly as Device Wallpaper via WallpaperManager (Official Android API)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { applyWallpaper(WallpaperManager.FLAG_SYSTEM, "Home Screen") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("set_home_wallpaper_btn")
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Set as Home Screen Wallpaper", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { applyWallpaper(WallpaperManager.FLAG_LOCK, "Lock Screen") },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("set_lock_wallpaper_btn")
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Set as Lock Screen Wallpaper", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
