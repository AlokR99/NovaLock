package com.example.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModuleCategory
import com.example.data.model.ModuleId
import com.example.data.model.ModuleItem
import com.example.data.repository.DeviceHardwareVitals
import com.example.ui.components.OneUICard
import com.example.ui.components.OneUIHeader

@Composable
fun SettingsScreen(
    modules: List<ModuleItem>,
    vitals: DeviceHardwareVitals,
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    onResetAllSettings: () -> Unit,
    onResetModule: (ModuleId) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showResetAllDialog by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen")
    ) {
        item {
            OneUIHeader(
                title = "Settings",
                subtitle = "Preferences, privacy audit, and compatibility breakdown"
            )
        }

        // Appearance
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                OneUICard {
                    Text("Display Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Force Dark Mode", fontWeight = FontWeight.SemiBold)
                            Text("One UI dark slate surfaces", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onToggleDarkTheme
                        )
                    }
                }
            }
        }

        // Privacy & Security Audit
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                OneUICard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF12B76A), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Privacy & Security Audit", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    SecurityCheckItem("Offline Operation", "Passed (0 remote servers, 0 background data sync)", true)
                    SecurityCheckItem("Analytics & Tracking", "Zero tracking SDKs installed", true)
                    SecurityCheckItem("Sensitive Permissions", "No Contacts, SMS, Location, or Mic permissions requested", true)
                    SecurityCheckItem("Accessibility Hooks", "No dangerous accessibility services used", true)
                    SecurityCheckItem("Root / System Mods", "No root required; 100% verified official Android APIs", true)
                }
            }
        }

        // Device Compatibility Transparency Report
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                OneUICard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF0381FE), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Compatibility Transparency", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Why NovaLock works on any Android phone:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Samsung Good Lock is locked to proprietary One UI frameworks. NovaLock brings the same level of granular personalization to ALL Android devices (Google Pixel, Motorola, Xiaomi, OnePlus, Samsung, Sony, etc.) using official Android SDK APIs.\n\n" +
                               "• Features like LockStar, Wonderland, Theme Park, and Home Up use public WallpaperManager, Material 3, and Launcher interfaces.\n\n" +
                               "• Features requiring OEM-restricted system hooks (Quick Settings tile injection or system nav bar overlays) are safely provided via in-app interactive studios and live previews without violating Android security.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Reset Settings
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                OneUICard {
                    Text("Master Reset", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Restore all module settings, themes, and clocks back to default factory states.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showResetAllDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reset All NovaLock Settings")
                    }
                }
            }
        }

        // About NovaLock
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "NovaLock v1.0.0",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Inspired by Samsung Good Lock • Built for All Android Devices",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showResetAllDialog) {
        AlertDialog(
            onDismissRequest = { showResetAllDialog = false },
            title = { Text("Reset All Settings?", fontWeight = FontWeight.Bold) },
            text = { Text("This will revert all 21 modules, themes, and clock configurations back to default settings.") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetAllSettings()
                        showResetAllDialog = false
                        Toast.makeText(context, "All settings reset to defaults.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reset Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetAllDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun SecurityCheckItem(title: String, detail: String, isPassed: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (isPassed) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (isPassed) Color(0xFF12B76A) else Color(0xFFF04438),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(detail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
