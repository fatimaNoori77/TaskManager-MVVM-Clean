package ir.noori.taskmanager.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.noori.taskmanager.data.alarm.AlarmScheduler
import ir.noori.taskmanager.data.local.DataStore
import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.domain.repository.TaskRepository
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
    private val repository: TaskRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = getTasksUseCase.invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), emptyList())

    private val alarmScheduler = AlarmScheduler(context)

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task)
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

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()
    fun refreshTasks() {
        viewModelScope.launch {
            repository.fetchRemoteTasks()
            repository.getTasks().collect { tasks ->
                _taskList.value = tasks
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