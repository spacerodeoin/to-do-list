package com.motive.todo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motive.todo.model.TodoItem
import com.motive.todo.ui.theme.BackgroundWhite
import com.motive.todo.ui.theme.CheckboxChecked
import com.motive.todo.ui.theme.CheckboxUnchecked
import com.motive.todo.ui.theme.CompletedText
import com.motive.todo.ui.theme.DividerColor
import com.motive.todo.ui.theme.TextPrimary
import com.motive.todo.ui.theme.TextSecondary
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TaskItem(
    task: TodoItem,
    onToggleComplete: () -> Unit,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val checkboxColor by animateColorAsState(
        targetValue = if (task.isCompleted) CheckboxChecked else CheckboxUnchecked,
        label = "checkboxColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BackgroundWhite)
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(checkboxColor)
                .clickable(onClick = onToggleComplete),
            contentAlignment = Alignment.Center
        ) {
            if (task.isCompleted) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Completed",
                    tint = BackgroundWhite,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Title + metadata
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                fontSize = 15.sp,
                color = if (task.isCompleted) CompletedText else TextPrimary,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (task.notes.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = task.notes,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (task.isCompleted && task.completedAt != null) {
                Spacer(Modifier.height(2.dp))
                val completedLocal = task.completedAt.toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    text = "Completed at ${completedLocal.date} ${completedLocal.hour.toString().padStart(2,'0')}:${completedLocal.minute.toString().padStart(2,'0')}",
                    fontSize = 11.sp,
                    color = TextSecondary.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Due date chip
        DueDateChip(task.dueDate.toString())
    }
}

@Composable
private fun DueDateChip(dateText: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DividerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = dateText,
            fontSize = 11.sp,
            color = TextSecondary
        )
    }
}
