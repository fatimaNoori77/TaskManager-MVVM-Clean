package ir.noori.taskmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.domain.usecase.DeleteTaskUseCase
import ir.noori.taskmanager.domain.usecase.GetTasksUseCase
import ir.noori.taskmanager.domain.usecase.ToggleTaskStatusUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val toggleTaskStatusUseCase: ToggleTaskStatusUseCase
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = getTasksUseCase.invoke().stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), emptyList())

    fun toggleTaskDone(task: Task) {
        viewModelScope.launch {
            toggleTaskStatusUseCase(task)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
        }
    }
}