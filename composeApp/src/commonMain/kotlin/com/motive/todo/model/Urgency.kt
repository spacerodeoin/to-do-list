package com.motive.todo.model

enum class Urgency(val label: String, val priority: Int) {
    CRITICAL("Critical", 0),
    URGENT("Urgent", 1),
    MODERATE("Moderate", 2);

    companion object {
        fun sortedByPriority(): List<Urgency> = entries.sortedBy { it.priority }
    }
}
