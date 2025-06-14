package ir.noori.taskmanager.presentation.ui.tasklist

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.R
import ir.noori.taskmanager.databinding.ActivityMainBinding
import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()

    private val taskAdapter = TaskAdapter(
        onCheckChanged = { task, _ ->
            viewModel.toggleTaskDone(task)
        },
        onDeleteClicked = { task ->
            viewModel.deleteTask(task.id)
        },
        onTaskClicked = {
            showTaskDialog(it)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isDarkMode.observe(this) { isDark ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.actionBar.btnRefresh.setOnClickListener{
            viewModel.refreshTasks()
        }

        binding.actionBar.btnToggleTheme.setOnClickListener{
            viewModel.isDarkMode.value?.let { current ->
                viewModel.toggleTheme(current)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        setupRecyclerView()
        observeTasks()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        binding.recyclerTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeTasks() {
        lifecycleScope.launch {
            viewModel.tasks.collectLatest { taskList ->
                taskAdapter.submitList(taskList)
            }
        }
    }

    private fun setupClickListeners() {
        binding.fabAddTask.setOnClickListener {
          showTaskDialog(null)
        }
    }

    private fun showTaskDialog(task: Task? = null) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.editTextDescription)
        val deadlineEditText = dialogView.findViewById<EditText>(R.id.editTextDeadline)

        task?.let {
            titleEditText.setText(it.title)
            descriptionEditText.setText(it.description ?: "")
            deadlineEditText.setText(convertLongToDate(it.dueDate))
        }

        var selectedDeadline: Long? = null
        deadlineEditText.inputType = InputType.TYPE_NULL
        deadlineEditText.setOnClickListener {
            showDatePickerDialog { timestamp ->
                selectedDeadline = timestamp
                val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
                deadlineEditText.setText(dateStr)
            }
        }

        AlertDialog.Builder(this)
            .setTitle(if(task == null) "add task" else "update task")
            .setView(dialogView)
            .setPositiveButton("submit") { _, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (title.isNotBlank()) {
                    val updatedTask = Task(
                        id = task?.id ?: 0,
                        title = title,
                        description = description,
                        dueDate = selectedDeadline ?: 0,
                        isDone = false,
                        reminderTime = 0
                    )

                    if(task == null){
                        viewModel.addTask(updatedTask)
                    }else{
                        viewModel.updateTask(updatedTask)
                    }

                } else {
                    Toast.makeText(this, "fill the title", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("cancel", null)
            .show()
    }

    private fun convertLongToDate(timeMillis: Long, pattern: String = "yyyy/MM/dd"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }

    private fun showDatePickerDialog(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth, 0, 0, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val selectedTimestamp = calendar.timeInMillis
                onDateSelected(selectedTimestamp)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

}
