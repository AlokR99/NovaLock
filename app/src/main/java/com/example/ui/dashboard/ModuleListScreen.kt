package com.example.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CompatibilityLevel
import com.example.data.model.ModuleCategory
import com.example.data.model.ModuleId
import com.example.data.model.ModuleItem
import com.example.ui.components.ModuleCard
import com.example.ui.components.OneUIHeader
import com.example.ui.components.OneUISearchBar

@Composable
fun ModuleListScreen(
    category: ModuleCategory,
    modules: List<ModuleItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSelectModule: (ModuleId) -> Unit,
    onToggleEnable: (ModuleId) -> Unit,
    onToggleFavorite: (ModuleId) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<CompatibilityLevel?>(null) }

    val categoryModules = modules.filter { it.id.category == category }

    val filteredList = categoryModules.filter { item ->
        val matchesSearch = searchQuery.isBlank() ||
                item.id.title.contains(searchQuery, ignoreCase = true) ||
                item.id.subtitle.contains(searchQuery, ignoreCase = true) ||
                item.id.description.contains(searchQuery, ignoreCase = true)
        val matchesCompat = selectedFilter == null || item.id.compatibility == selectedFilter
        matchesSearch && matchesCompat
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("module_list_${category.name.lowercase()}")
    ) {
        item {
            OneUIHeader(
                title = category.displayName,
                subtitle = category.description,
                badgeText = "${categoryModules.count { it.isEnabled }}/${categoryModules.size} Active"
            )
        }

        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                OneUISearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    placeholder = "Search ${category.displayName} modules..."
                )
            }
        }

        // Compatibility filter chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == null,
                        onClick = { selectedFilter = null },
                        label = { Text("All (${categoryModules.size})") },
                        shape = RoundedCornerShape(14.dp)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == CompatibilityLevel.FULLY_SUPPORTED,
                        onClick = { selectedFilter = CompatibilityLevel.FULLY_SUPPORTED },
                        label = { Text("Native APIs") },
                        shape = RoundedCornerShape(14.dp)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == CompatibilityLevel.PARTIALLY_SUPPORTED,
                        onClick = { selectedFilter = CompatibilityLevel.PARTIALLY_SUPPORTED },
                        label = { Text("Hybrid") },
                        shape = RoundedCornerShape(14.dp)
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == CompatibilityLevel.PREVIEW_STUDIO,
                        onClick = { selectedFilter = CompatibilityLevel.PREVIEW_STUDIO },
                        label = { Text("Studio Preview") },
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp)
                ) {
                    Text(
                        text = "No modules found matching criteria.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filteredList, key = { it.id.name }) { item ->
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
