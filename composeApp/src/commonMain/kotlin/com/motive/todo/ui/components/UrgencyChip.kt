package com.motive.todo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.motive.todo.model.Urgency
import com.motive.todo.ui.theme.CriticalRed
import com.motive.todo.ui.theme.CriticalRedLight
import com.motive.todo.ui.theme.ModerateBlue
import com.motive.todo.ui.theme.ModerateBlueLight
import com.motive.todo.ui.theme.UrgentOrange
import com.motive.todo.ui.theme.UrgentOrangeLight

data class UrgencyStyle(
    val background: Color,
    val textColor: Color,
    val borderColor: Color
)

fun urgencyStyle(urgency: Urgency): UrgencyStyle = when (urgency) {
    Urgency.CRITICAL -> UrgencyStyle(CriticalRedLight, CriticalRed, CriticalRed)
    Urgency.URGENT -> UrgencyStyle(UrgentOrangeLight, UrgentOrange, UrgentOrange)
    Urgency.MODERATE -> UrgencyStyle(ModerateBlueLight, ModerateBlue, ModerateBlue)
}

@Composable
fun UrgencyBadge(urgency: Urgency, modifier: Modifier = Modifier) {
    val style = urgencyStyle(urgency)
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(style.background)
            .border(1.dp, style.borderColor.copy(alpha = 0.4f), shape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = urgency.label,
            color = style.textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
fun UrgencySelectChip(
    urgency: Urgency,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val style = urgencyStyle(urgency)
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(if (selected) style.textColor else style.background)
            .border(1.5.dp, style.borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = urgency.label,
            color = if (selected) Color.White else style.textColor,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
