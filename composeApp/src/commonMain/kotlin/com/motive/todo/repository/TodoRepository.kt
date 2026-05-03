package com.motive.todo.repository

import com.motive.todo.model.TodoItem
import com.motive.todo.model.Urgency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class TodoRepository {
    private val _tasks = MutableStateFlow<List<TodoItem>>(sampleTasks())
    val tasks: StateFlow<List<TodoItem>> = _tasks.asStateFlow()

    fun addTask(
        title: String,
        notes: String,
        dueDate: LocalDate,
        urgency: Urgency
    ) {
        val task = TodoItem(
            title = title.trim(),
            notes = notes.trim(),
            dueDate = dueDate,
            urgency = urgency
        )
        _tasks.update { it + task }
    }

    fun updateTask(
        id: String,
        title: String,
        notes: String,
        dueDate: LocalDate,
        urgency: Urgency
    ) {
        _tasks.update { tasks ->
            tasks.map { task ->
                if (task.id == id) task.copy(
                    title = title.trim(),
                    notes = notes.trim(),
                    dueDate = dueDate,
                    urgency = urgency
                ) else task
            }
        }
    }

    fun toggleComplete(id: String) {
        _tasks.update { tasks ->
            tasks.map { task ->
                if (task.id == id) task.copy(
                    isCompleted = !task.isCompleted,
                    completedAt = if (!task.isCompleted) Clock.System.now() else null
                ) else task
            }
        }
    }

    fun deleteTask(id: String) {
        _tasks.update { tasks -> tasks.filter { it.id != id } }
    }

    fun getTask(id: String): TodoItem? = _tasks.value.find { it.id == id }

    private fun sampleTasks(): List<TodoItem> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return listOf(
            TodoItem(
                title = "Finish project proposal",
                notes = "Include timeline and budget breakdown",
                dueDate = today,
                urgency = Urgency.CRITICAL
            ),
            TodoItem(
                title = "Review pull request",
                notes = "",
                dueDate = today,
                urgency = Urgency.URGENT
            ),
            TodoItem(
                title = "Schedule team meeting",
                notes = "Agenda: Q2 roadmap",
                dueDate = today,
                urgency = Urgency.MODERATE
            )
        )
    }
}
