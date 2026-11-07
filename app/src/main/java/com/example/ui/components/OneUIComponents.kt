package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompatibilityLevel
import com.example.data.model.ModuleCategory
import com.example.data.model.ModuleId
import com.example.data.model.ModuleItem

@Composable
fun OneUIHeader(
    title: String,
    subtitle: String? = null,
    badgeText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            if (badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun OneUICard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(18.dp)
    } else {
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .padding(18.dp)
    }

    Column(modifier = cardModifier) {
        content()
    }
}

@Composable
fun ModuleCard(
    moduleItem: ModuleItem,
    onClick: () -> Unit,
    onToggleEnable: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val id = moduleItem.id
    val icon = getModuleIcon(id)
    val iconBgColor = getModuleIconColor(id)

    OneUICard(
        modifier = modifier.testTag("module_card_${id.name.lowercase()}"),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Module Icon with One UI rounded square
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconBgColor.copy(alpha = 0.16f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = id.title,
                    tint = iconBgColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Module Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = id.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CompatibilityBadge(compatibility = id.compatibility)
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = id.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Favorite button
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("fav_btn_${id.name.lowercase()}")
            ) {
                Icon(
                    imageVector = if (moduleItem.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Toggle favorite",
                    tint = if (moduleItem.isFavorite) Color(0xFFF79009) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            // Enable / Disable switch
            Switch(
                checked = moduleItem.isEnabled,
                onCheckedChange = { onToggleEnable() },
                modifier = Modifier.testTag("switch_${id.name.lowercase()}"),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Description preview
        Text(
            text = id.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CompatibilityBadge(compatibility: CompatibilityLevel) {
    val bgColor = Color(compatibility.badgeColorHex)
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor.copy(alpha = 0.14f)
    ) {
        Text(
            text = when (compatibility) {
                CompatibilityLevel.FULLY_SUPPORTED -> "Native"
                CompatibilityLevel.PARTIALLY_SUPPORTED -> "Hybrid"
                CompatibilityLevel.PREVIEW_STUDIO -> "Studio"
            },
            style = MaterialTheme.typography.labelSmall,
            color = bgColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun OneUISearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search customization modules...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .testTag("search_modules_input")
    )
}

@Composable
fun ColorPaletteRow(
    selectedHex: String,
    onSelectHex: (String) -> Unit,
    colors: List<String> = listOf(
        "#0381FE", "#7A5AF8", "#06AED4", "#12B76A", "#F79009", "#E31B78", "#101828", "#FFFFFF"
    ),
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        colors.forEach { hex ->
            val color = Color(android.graphics.Color.parseColor(hex))
            val isSelected = selectedHex.equals(hex, ignoreCase = true)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Gray.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .clickable { onSelectHex(hex) }
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = if (hex == "#FFFFFF") Color.Black else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

fun getModuleIcon(id: ModuleId): ImageVector {
    return when (id) {
        ModuleId.LOCK_STAR -> Icons.Default.Lock
        ModuleId.QUICK_STAR -> Icons.Default.DashboardCustomize
        ModuleId.THEME_PARK -> Icons.Default.Palette
        ModuleId.HOME_UP -> Icons.Default.Home
        ModuleId.NAV_STAR -> Icons.Default.Navigation
        ModuleId.CLOCK_FACE -> Icons.Default.Schedule
        ModuleId.WONDERLAND -> Icons.Default.AutoAwesome
        ModuleId.KEYS_CAFE -> Icons.Default.Keyboard
        ModuleId.EDGE_LIGHTING -> Icons.Default.Highlight
        ModuleId.PENTASTIC -> Icons.Default.Brush
        ModuleId.SOUND_ASSISTANT -> Icons.Default.VolumeUp
        ModuleId.MULTI_STAR -> Icons.Default.ViewQuilt
        ModuleId.NOTI_STAR -> Icons.Default.Notifications
        ModuleId.REGI_STAR -> Icons.Default.FlashOn
        ModuleId.ONE_HAND -> Icons.Default.PanTool
        ModuleId.NICE_SHOT -> Icons.Default.Crop
        ModuleId.NICE_CATCH -> Icons.Default.Insights
        ModuleId.EDGE_TOUCH -> Icons.Default.TouchApp
        ModuleId.GAME_BOOSTER -> Icons.Default.SportsEsports
        ModuleId.DISPLAY_ASSISTANT -> Icons.Default.BrightnessMedium
        ModuleId.ROUTINE_PLUS -> Icons.Default.ScheduleSend
    }
}

fun getModuleIconColor(id: ModuleId): Color {
    return when (id) {
        ModuleId.LOCK_STAR -> Color(0xFF0381FE)
        ModuleId.QUICK_STAR -> Color(0xFF7A5AF8)
        ModuleId.THEME_PARK -> Color(0xFFE31B78)
        ModuleId.HOME_UP -> Color(0xFF12B76A)
        ModuleId.NAV_STAR -> Color(0xFF06AED4)
        ModuleId.CLOCK_FACE -> Color(0xFFF79009)
        ModuleId.WONDERLAND -> Color(0xFF9E77ED)
        ModuleId.KEYS_CAFE -> Color(0xFFEE46BC)
        ModuleId.EDGE_LIGHTING -> Color(0xFF00C7BE)
        ModuleId.PENTASTIC -> Color(0xFF6172F3)
        ModuleId.SOUND_ASSISTANT -> Color(0xFF2E90FA)
        ModuleId.MULTI_STAR -> Color(0xFFF04438)
        ModuleId.NOTI_STAR -> Color(0xFFFDB022)
        ModuleId.REGI_STAR -> Color(0xFF12B76A)
        ModuleId.ONE_HAND -> Color(0xFF32D583)
        ModuleId.NICE_SHOT -> Color(0xFF0086C9)
        ModuleId.NICE_CATCH -> Color(0xFF7A5AF8)
        ModuleId.EDGE_TOUCH -> Color(0xFFE31B78)
        ModuleId.GAME_BOOSTER -> Color(0xFFD444F1)
        ModuleId.DISPLAY_ASSISTANT -> Color(0xFFF79009)
        ModuleId.ROUTINE_PLUS -> Color(0xFF0381FE)
    }
}
