package com.motive.todo.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

data class TodoItem(
    val id: String = generateId(),
    val title: String,
    val notes: String = "",
    val dueDate: LocalDate,
    val urgency: Urgency,
    val isCompleted: Boolean = false,
    val completedAt: Instant? = null,
    val createdAt: Instant = Clock.System.now()
)

private fun generateId(): String =
    "${Clock.System.now().toEpochMilliseconds()}-${Random.nextInt(100_000)}"
