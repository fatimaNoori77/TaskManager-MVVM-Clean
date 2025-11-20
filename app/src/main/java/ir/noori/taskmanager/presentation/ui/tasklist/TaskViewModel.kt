package ir.noori.taskmanager.presentation.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.noori.taskmanager.R
import ir.noori.taskmanager.data.alarm.AlarmScheduler
import ir.noori.taskmanager.data.local.DataStore
import ir.noori.taskmanager.domain.exceptions.NoInternetException
import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.domain.usecase.AddTaskUseCase
import ir.noori.taskmanager.domain.usecase.DeleteTaskUseCase
import ir.noori.taskmanager.domain.usecase.GetTasksUseCase
import ir.noori.taskmanager.domain.usecase.ToggleTaskStatusUseCase
import ir.noori.taskmanager.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val toggleTaskStatusUseCase: ToggleTaskStatusUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val themePreferences: DataStore,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = getTasksUseCase.invoke()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(2000), emptyList())

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task)
            if(!task.hasReminder) return@launch
            alarmScheduler.schedule(task)
        }
    }

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

    private val _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun refreshTasks() {
        viewModelScope.launch {
            _uiState.value = TaskUiState.Loading
            try {
                getTasksUseCase.fetchFromRemote()
                _uiState.value = TaskUiState.Success
            } catch (e: NoInternetException){
                _uiState.value = TaskUiState.Error(R.string.no_internet_connection_error)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = TaskUiState.Error(R.string.unknown_error)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    val isDarkMode = themePreferences.isDarkMode.asLiveData()
    fun toggleTheme(currentTheme: Boolean) {
        viewModelScope.launch {
            themePreferences.toggleTheme(currentTheme)
        }
    }
}

sealed class TaskUiState{
    object Idle: TaskUiState()
    object Loading: TaskUiState()
    object Success: TaskUiState()
    data class Error(val messageRes: Int): TaskUiState()
}