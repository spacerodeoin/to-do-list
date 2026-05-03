package com.motive.todo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.motive.todo.ui.screens.AddEditTaskScreen
import com.motive.todo.ui.screens.HomeScreen
import com.motive.todo.ui.screens.SplashScreen
import com.motive.todo.ui.theme.TodoTheme
import com.motive.todo.viewmodel.TodoViewModel

sealed class Screen {
    data object Splash : Screen()
    data object Home : Screen()
    data class AddEdit(val taskId: String? = null) : Screen()
}

@Composable
fun App() {
    TodoTheme {
        val viewModel = remember { TodoViewModel() }
        var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

        when (val screen = currentScreen) {
            Screen.Splash -> SplashScreen(
                onComplete = { currentScreen = Screen.Home }
            )
            Screen.Home -> HomeScreen(
                viewModel = viewModel,
                onAddTask = { currentScreen = Screen.AddEdit() },
                onEditTask = { taskId -> currentScreen = Screen.AddEdit(taskId) }
            )
            is Screen.AddEdit -> AddEditTaskScreen(
                viewModel = viewModel,
                taskId = screen.taskId,
                onBack = { currentScreen = Screen.Home }
            )
        }
    }
}
