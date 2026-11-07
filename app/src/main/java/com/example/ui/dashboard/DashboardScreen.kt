package com.example.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModuleCategory
import com.example.data.model.ModuleId
import com.example.data.model.ModuleItem
import com.example.data.repository.DeviceHardwareVitals
import com.example.ui.components.*

@Composable
fun DashboardScreen(
    modules: List<ModuleItem>,
    deviceVitals: DeviceHardwareVitals,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSelectModule: (ModuleId) -> Unit,
    onToggleEnable: (ModuleId) -> Unit,
    onToggleFavorite: (ModuleId) -> Unit,
    onNavigateToCategory: (ModuleCategory) -> Unit,
    onQuickAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val enabledCount = modules.count { it.isEnabled }
    val favoriteModules = modules.filter { it.isFavorite }
    val recentlyUsedModules = modules.filter { it.lastUsedTimestamp > 0L }
        .sortedByDescending { it.lastUsedTimestamp }
        .take(4)

    val filteredModules = if (searchQuery.isBlank()) {
        emptyList()
    } else {
        modules.filter {
            it.id.title.contains(searchQuery, ignoreCase = true) ||
            it.id.subtitle.contains(searchQuery, ignoreCase = true) ||
            it.id.description.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen")
    ) {
        // One UI Giant Hero Header
        item {
            OneUIHeader(
                title = "NovaLock",
                subtitle = "Good Lock-inspired customization suite for all Android phones",
                badgeText = "$enabledCount/21 Active"
            )
        }

        // Search Input
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                OneUISearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange
                )
            }
        }

        // If search is active, show filtered results directly
        if (searchQuery.isNotBlank()) {
            item {
                Text(
                    text = "Search Results (${filteredModules.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            if (filteredModules.isEmpty()) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    ) {
                        Text(
                            text = "No modules match \"$searchQuery\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filteredModules, key = { it.id.name }) { item ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        ModuleCard(
                            moduleItem = item,
                            onClick = { onSelectModule(item.id) },
                            onToggleEnable = { onToggleEnable(item.id) },
                            onToggleFavorite = { onToggleFavorite(item.id) }
                        )
                    }
                }
            }
        } else {
            // Hero Status Overview Card
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    OneUICard(
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "System Status: Optimal",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${deviceVitals.androidVersion} • Battery: ${deviceVitals.batteryPercent}% (${deviceVitals.batteryTemperatureC}°C)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Storage: ${deviceVitals.freeStorageGb} GB free • RAM: ${deviceVitals.availRamMb} MB avail",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Privacy Shield",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }

            // Quick Actions Hub
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        QuickActionPill(
                            title = "Theme Park",
                            icon = Icons.Default.Palette,
                            color = Color(0xFFE31B78),
                            onClick = { onSelectModule(ModuleId.THEME_PARK) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionPill(
                            title = "Wonderland",
                            icon = Icons.Default.AutoAwesome,
                            color = Color(0xFF9E77ED),
                            onClick = { onSelectModule(ModuleId.WONDERLAND) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionPill(
                            title = "Edge Glow",
                            icon = Icons.Default.Highlight,
                            color = Color(0xFF00C7BE),
                            onClick = { onSelectModule(ModuleId.EDGE_LIGHTING) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionPill(
                            title = "LockStar",
                            icon = Icons.Default.Lock,
                            color = Color(0xFF0381FE),
                            onClick = { onSelectModule(ModuleId.LOCK_STAR) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Category Jump Cards
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    CategoryBannerCard(
                        title = "Make Up",
                        subtitle = "10 Appearance Modules",
                        description = "LockStar, Theme Park, Home Up, QuickStar, NavStar, ClockFace, Wonderland...",
                        color = Color(0xFF0381FE),
                        icon = Icons.Default.Style,
                        onClick = { onNavigateToCategory(ModuleCategory.MAKE_UP) },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryBannerCard(
                        title = "Life Up",
                        subtitle = "11 Productivity Modules",
                        description = "Sound Assistant, MultiStar, NotiStar, Nice Shot, Nice Catch, Routine+...",
                        color = Color(0xFF12B76A),
                        icon = Icons.Default.Widgets,
                        onClick = { onNavigateToCategory(ModuleCategory.LIFE_UP) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Favorites Section
            if (favoriteModules.isNotEmpty()) {
                item {
                    Text(
                        text = "Favorite Modules ⭐",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                items(favoriteModules, key = { "fav_${it.id.name}" }) { item ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        ModuleCard(
                            moduleItem = item,
                            onClick = { onSelectModule(item.id) },
                            onToggleEnable = { onToggleEnable(item.id) },
                            onToggleFavorite = { onToggleFavorite(item.id) }
                        )
                    }
                }
            }

            // Recently Opened Modules
            if (recentlyUsedModules.isNotEmpty()) {
                item {
                    Text(
                        text = "Recently Opened",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                items(recentlyUsedModules, key = { "recent_${it.id.name}" }) { item ->
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        ModuleCard(
                            moduleItem = item,
                            onClick = { onSelectModule(item.id) },
                            onToggleEnable = { onToggleEnable(item.id) },
                            onToggleFavorite = { onToggleFavorite(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionPill(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CategoryBannerCard(
    title: String,
    subtitle: String,
    description: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OneUICard(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.18f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2
        )
    }
}
