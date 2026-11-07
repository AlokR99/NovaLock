package com.example.ui.modules.lifeup

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.ui.components.OneUICard

data class UserReminder(
    val id: String,
    val title: String,
    val category: String,
    val timeLabel: String,
    var isDone: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotiStarScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var reminderText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("VIP") }

    val reminders = remember {
        mutableStateListOf(
            UserReminder("1", "Check lockscreen wallpaper contrast", "Design", "Today, 14:00"),
            UserReminder("2", "Test Bluetooth headphone volume profile", "Audio", "Today, 18:30"),
            UserReminder("3", "Review device storage cleanup stats", "System", "Tomorrow, 09:00")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NotiStar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("notistar_back_btn")) {
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
            // Privacy Shield Notice
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFDB022).copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDB022).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFFFDB022), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Privacy First: NotiStar only logs reminders and notifications created inside NovaLock. It never reads or intercepts private notifications from your other apps.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Create New Reminder Card
            item {
                OneUICard {
                    Text("Schedule Local Reminder", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reminderText,
                        onValueChange = { reminderText = it },
                        placeholder = { Text("Reminder title...") },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().testTag("new_reminder_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("VIP", "Design", "Audio", "System").forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat, fontSize = 11.sp) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (reminderText.isNotBlank()) {
                                reminders.add(
                                    UserReminder(
                                        id = "rem_${System.currentTimeMillis()}",
                                        title = reminderText.trim(),
                                        category = selectedCategory,
                                        timeLabel = "Scheduled for today"
                                    )
                                )
                                reminderText = ""
                                Toast.makeText(context, "Reminder scheduled locally!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.AddAlert, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Local Notification Reminder")
                    }
                }
            }

            // Active Reminders List
            item {
                Text("Active Reminders (${reminders.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(reminders, key = { it.id }) { rem ->
                OneUICard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = rem.isDone,
                            onCheckedChange = { done ->
                                val idx = reminders.indexOfFirst { it.id == rem.id }
                                if (idx != -1) {
                                    reminders[idx] = reminders[idx].copy(isDone = done)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = rem.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = if (rem.isDone) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${rem.category} • ${rem.timeLabel}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { reminders.removeAll { it.id == rem.id } }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
