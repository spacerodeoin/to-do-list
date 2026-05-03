package com.motive.todo.viewmodel

import com.motive.todo.model.TodoItem
import com.motive.todo.model.Urgency
import com.motive.todo.repository.TodoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class UrgencyGroup(
    val urgency: Urgency,
    val tasks: List<TodoItem>
)

class TodoViewModel {
    private val repository = TodoRepository()

    val allTasks: StateFlow<List<TodoItem>> = repository.tasks

    fun pendingGroups(tasks: List<TodoItem>): List<UrgencyGroup> =
        tasks
            .filter { !it.isCompleted }
            .groupBy { it.urgency }
            .let { grouped ->
                Urgency.sortedByPriority().mapNotNull { urgency ->
                    grouped[urgency]
                        ?.sortedWith(compareBy({ it.dueDate }, { it.createdAt }))
                        ?.let { UrgencyGroup(urgency, it) }
                }
            }

    fun completedGroups(tasks: List<TodoItem>): List<UrgencyGroup> =
        tasks
            .filter { it.isCompleted }
            .groupBy { it.urgency }
            .let { grouped ->
                Urgency.sortedByPriority().mapNotNull { urgency ->
                    grouped[urgency]
                        ?.sortedWith(compareByDescending { it.completedAt })
                        ?.let { UrgencyGroup(urgency, it) }
                }
            }

    fun addTask(title: String, notes: String, dueDate: LocalDate, urgency: Urgency) {
        repository.addTask(title, notes, dueDate, urgency)
    }

    fun updateTask(id: String, title: String, notes: String, dueDate: LocalDate, urgency: Urgency) {
        repository.updateTask(id, title, notes, dueDate, urgency)
    }

    fun toggleComplete(id: String) {
        repository.toggleComplete(id)
    }

    fun deleteTask(id: String) {
        repository.deleteTask(id)
    }

    fun getTask(id: String): TodoItem? = repository.getTask(id)

    fun todayDate(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
}
