package com.motive.todo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motive.todo.ui.components.EmptyState
import com.motive.todo.ui.components.UrgencySection
import com.motive.todo.ui.theme.BackgroundWhite
import com.motive.todo.ui.theme.BluePrimary
import com.motive.todo.ui.theme.CriticalRed
import com.motive.todo.ui.theme.DividerColor
import com.motive.todo.ui.theme.SurfaceLight
import com.motive.todo.ui.theme.TextSecondary
import com.motive.todo.viewmodel.TodoViewModel

@Composable
fun HomeScreen(
    viewModel: TodoViewModel,
    onAddTask: () -> Unit,
    onEditTask: (String) -> Unit
) {
    val allTasks by viewModel.allTasks.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showClearDialog by remember { mutableStateOf(false) }

    val pendingGroups = remember(allTasks) { viewModel.pendingGroups(allTasks) }
    val completedGroups = remember(allTasks) { viewModel.completedGroups(allTasks) }

    if (showClearDialog) {
        ClearAllDialog(
            onConfirm = {
                if (selectedTab == 0) {
                    allTasks.filter { !it.isCompleted }.forEach { viewModel.deleteTask(it.id) }
                } else {
                    allTasks.filter { it.isCompleted }.forEach { viewModel.deleteTask(it.id) }
                }
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false }
        )
    }

    Scaffold(
        containerColor = SurfaceLight,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                shape = CircleShape,
                containerColor = BluePrimary,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add task",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top bar with segmented control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundWhite)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PendingCompleteTabRow(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                IconButton(
                    onClick = {
                        val hasItems =
                            if (selectedTab == 0) pendingGroups.isNotEmpty()
                            else completedGroups.isNotEmpty()
                        if (hasItems) showClearDialog = true
                    }
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "Clear", tint = TextSecondary)
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                // Empty state
                val isEmpty = (selectedTab == 0 && pendingGroups.isEmpty()) ||
                        (selectedTab == 1 && completedGroups.isEmpty())
                if (isEmpty) {
                    EmptyState(
                        message = if (selectedTab == 0) "No pending to-dos" else "No completed tasks"
                    )
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    val groups = if (selectedTab == 0) pendingGroups else completedGroups
                    items(groups, key = { it.urgency.name }) { group ->
                        UrgencySection(
                            urgency = group.urgency,
                            tasks = group.tasks,
                            onToggleComplete = { viewModel.toggleComplete(it) },
                            onTaskTap = onEditTask,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun PendingCompleteTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(DividerColor)
            .padding(3.dp)
    ) {
        Row {
            TabPill(
                label = "Pending",
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                modifier = Modifier.weight(1f)
            )
            TabPill(
                label = "Complete",
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TabPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) BluePrimary else Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun ClearAllDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Clear Tasks", fontWeight = FontWeight.SemiBold) },
        text = {
            Text(
                "Are you sure you want to remove all tasks in this view?",
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = CriticalRed)
            ) { Text("Clear All") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
