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
import com.example.data.model.RoutineRule
import com.example.ui.components.OneUICard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinePlusScreen(
    routines: List<RoutineRule>,
    onToggleRoutine: (String) -> Unit,
    onAddRoutine: (String, String, String) -> Unit,
    onDeleteRoutine: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var routineNameInput by remember { mutableStateOf("") }
    var selectedTrigger by remember { mutableStateOf("Battery falls below 15%") }
    var selectedAction by remember { mutableStateOf("Trigger Edge Lighting Warning") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Routine+", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("routineplus_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.AddCircleOutline, contentDescription = "Add Routine")
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
            // Hero Explainer Card
            item {
                OneUICard {
                    Text("Local Automation & If-Then Rules", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Configure privacy-friendly automated routines that trigger based on time, battery, and device states.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Routine items
            items(routines, key = { it.id }) { rule ->
                OneUICard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(rule.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "IF: ${rule.triggerDescription}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "THEN: ${rule.actionDescription}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = rule.isEnabled,
                            onCheckedChange = { onToggleRoutine(rule.id) }
                        )

                        IconButton(onClick = { onDeleteRoutine(rule.id) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            // Button to Add Routine
            item {
                Button(
                    onClick = { showAddDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp).testTag("add_routine_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create New Routine", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("New Routine", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = routineNameInput,
                        onValueChange = { routineNameInput = it },
                        placeholder = { Text("Routine Name (e.g. Study Mode)") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Trigger (IF):", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    val triggers = listOf("Battery falls below 15%", "Time reaches 23:00", "Charger connected")
                    triggers.forEach { trig ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedTrigger == trig,
                                onClick = { selectedTrigger = trig }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(trig, fontSize = 12.sp)
                        }
                    }
                    Text("Action (THEN):", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    val actions = listOf("Trigger Edge Lighting Warning", "Set Mute Sound Profile", "Switch Live Wallpaper")
                    actions.forEach { act ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedAction == act,
                                onClick = { selectedAction = act }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(act, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (routineNameInput.isNotBlank()) {
                            onAddRoutine(routineNameInput.trim(), selectedTrigger, selectedAction)
                            routineNameInput = ""
                            showAddDialog = false
                            Toast.makeText(context, "Routine created!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}
