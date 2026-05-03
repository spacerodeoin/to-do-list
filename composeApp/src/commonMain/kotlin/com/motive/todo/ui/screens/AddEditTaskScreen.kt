package com.motive.todo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motive.todo.model.Urgency
import com.motive.todo.ui.components.UrgencySelectChip
import com.motive.todo.ui.theme.BackgroundWhite
import com.motive.todo.ui.theme.BluePrimary
import com.motive.todo.ui.theme.CriticalRed
import com.motive.todo.ui.theme.DeleteRedLight
import com.motive.todo.ui.theme.DividerColor
import com.motive.todo.ui.theme.SurfaceLight
import com.motive.todo.ui.theme.TextPrimary
import com.motive.todo.ui.theme.TextSecondary
import com.motive.todo.viewmodel.TodoViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TodoViewModel,
    taskId: String?,
    onBack: () -> Unit
) {
    val isEditing = taskId != null
    val existing = remember(taskId) { taskId?.let { viewModel.getTask(it) } }

    var title by remember { mutableStateOf(existing?.title ?: "") }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }
    var selectedDate by remember { mutableStateOf(existing?.dueDate ?: viewModel.todayDate()) }
    var selectedUrgency by remember { mutableStateOf(existing?.urgency ?: Urgency.MODERATE) }
    var titleError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerModal(
            initialDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showDeleteDialog) {
        DeleteTaskDialog(
            taskTitle = title,
            onConfirm = {
                taskId?.let { viewModel.deleteTask(it) }
                onBack()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        containerColor = SurfaceLight,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Edit Task" else "New Task",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Rounded.Delete,
                                contentDescription = "Delete task",
                                tint = CriticalRed
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundWhite)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title field
            FormSection(label = "Task Title") {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = false
                    },
                    placeholder = { Text("What needs to be done?", color = TextSecondary) },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Title is required", color = CriticalRed, fontSize = 12.sp) }
                    } else null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedFieldColors()
                )
            }

            // Notes field
            FormSection(label = "Notes (optional)") {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Add details...", color = TextSecondary) },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedFieldColors()
                )
            }

            // Due date
            FormSection(label = "Due Date") {
                DateField(
                    date = selectedDate,
                    onClick = { showDatePicker = true }
                )
            }

            // Urgency
            FormSection(label = "Urgency") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Urgency.entries.forEach { urgency ->
                        UrgencySelectChip(
                            urgency = urgency,
                            selected = selectedUrgency == urgency,
                            onClick = { selectedUrgency = urgency },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Save button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                        return@Button
                    }
                    if (isEditing && taskId != null) {
                        viewModel.updateTask(taskId, title, notes, selectedDate, selectedUrgency)
                    } else {
                        viewModel.addTask(title, notes, selectedDate, selectedUrgency)
                    }
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text(
                    text = if (isEditing) "Save Changes" else "Add Task",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FormSection(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
        content()
    }
}

@Composable
private fun DateField(date: LocalDate, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
            .background(BackgroundWhite)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatDate(date),
            color = TextPrimary,
            fontSize = 15.sp
        )
        IconButton(onClick = onClick, modifier = Modifier.size(24.dp)) {
            Icon(
                Icons.Rounded.DateRange,
                contentDescription = "Pick date",
                tint = BluePrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = initialDate.toEpochMillis()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val date = Instant.fromEpochMilliseconds(millis)
                        .toLocalDateTime(TimeZone.UTC)
                        .date
                    onDateSelected(date)
                }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun DeleteTaskDialog(
    taskTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BackgroundWhite,
        title = { Text("Delete Task", fontWeight = FontWeight.SemiBold) },
        text = {
            Column {
                Text("Delete \"$taskTitle\"?", color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text("This action cannot be undone.", color = TextSecondary, fontSize = 12.sp)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CriticalRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) { Text("Delete") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = BluePrimary,
    unfocusedBorderColor = DividerColor,
    focusedContainerColor = BackgroundWhite,
    unfocusedContainerColor = BackgroundWhite,
    cursorColor = BluePrimary
)

private fun formatDate(date: LocalDate): String {
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    return "${date.dayOfMonth} $month ${date.year}"
}

private fun LocalDate.toEpochMillis(): Long {
    val ldt = LocalDateTime(year, monthNumber, dayOfMonth, 12, 0)
    return ldt.toInstant(TimeZone.UTC).toEpochMilliseconds()
}
