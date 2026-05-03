package com.motive.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motive.todo.model.Urgency
import com.motive.todo.model.TodoItem
import com.motive.todo.ui.theme.CriticalRed
import com.motive.todo.ui.theme.DividerColor
import com.motive.todo.ui.theme.ModerateBlue
import com.motive.todo.ui.theme.TextSecondary
import com.motive.todo.ui.theme.UrgentOrange

@Composable
fun UrgencySectionHeader(urgency: Urgency, taskCount: Int, modifier: Modifier = Modifier) {
    val dotColor: Color = when (urgency) {
        Urgency.CRITICAL -> CriticalRed
        Urgency.URGENT -> UrgentOrange
        Urgency.MODERATE -> ModerateBlue
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .drawBehind { drawCircle(dotColor) }
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = urgency.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = dotColor
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "($taskCount)",
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun UrgencySection(
    urgency: Urgency,
    tasks: List<TodoItem>,
    onToggleComplete: (String) -> Unit,
    onTaskTap: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        UrgencySectionHeader(urgency = urgency, taskCount = tasks.size)

        tasks.forEachIndexed { index, task ->
            TaskItem(
                task = task,
                onToggleComplete = { onToggleComplete(task.id) },
                onTap = { onTaskTap(task.id) },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
            if (index < tasks.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = DividerColor,
                    thickness = 0.5.dp
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
