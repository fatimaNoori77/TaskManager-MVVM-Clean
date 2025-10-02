package ir.noori.taskmanager.presentation.ui.tasklist

import CheckInternetStatus
import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.R
import ir.noori.taskmanager.data.local.DataStore
import ir.noori.taskmanager.databinding.ActivityMainBinding
import ir.noori.taskmanager.domain.model.Task
import ir.noori.taskmanager.presentation.viewmodel.SplashEvents
import ir.noori.taskmanager.presentation.viewmodel.SplashViewModel
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
    private val splashViewModel: SplashViewModel by viewModels()
    private lateinit var dataStore: DataStore
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
        dataStore = DataStore(this)

        binding.actionBar.btnRefresh.setOnClickListener {
            binding.swipeRefreshLayout.isRefreshing = true
            viewModel.refreshTasks()
        }
        binding.actionBar.btnToggleTheme.setOnClickListener {
            viewModel.isDarkMode.value?.let { current ->
                viewModel.toggleTheme(current)
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshTasks()
        }

        checkPostNotificationPermission()
        setupRecyclerView()
        observer()
        setupClickListeners()
        checkInternetStatus()
    }

    private fun checkPostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.event.collect { events ->
                    when (events) {
                        SplashEvents.NavigateToHome -> {
                            Toast.makeText(applicationContext, "navigate to home", Toast.LENGTH_SHORT).show()
                        }

                        SplashEvents.NavigateToLogin -> {
                            Toast.makeText(applicationContext, "navigate to login", Toast.LENGTH_SHORT).show()

//                            supportFragmentManager
//                                .beginTransaction()
//                                .replace(R.id.fragment_container, LoginFragment())
//                                .addToBackStack(null)
//                                .commit()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasks.collectLatest { taskList ->
                    binding.swipeRefreshLayout.isRefreshing = false
                    taskAdapter.submitList(taskList)
                }

                splashViewModel.uiState.collect { state ->
                    Toast.makeText(applicationContext, "ui state $state", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isDarkMode.observe(this) { isDark ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    private fun checkInternetStatus() {
        if (!CheckInternetStatus.isInternetAvailable(applicationContext)) {
            Toast.makeText(this, R.string.internet_warning, Toast.LENGTH_SHORT).show()
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
        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val chbReminder = dialogView.findViewById<CheckBox>(R.id.chbReminder)

        task?.let {
            titleEditText.setText(it.title)
            descriptionEditText.setText(it.description ?: "")
            deadlineEditText.setText(convertLongToDate(it.dueDate))
        }

        var selectedDeadline: Long? = null
        deadlineEditText.inputType = InputType.TYPE_NULL
        deadlineEditText.setOnClickListener {
            showDateTimePicker(this)
            { timestamp ->
                selectedDeadline = timestamp
                val dateStr =
                    SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.getDefault()
                    ).format(Date(timestamp))
                deadlineEditText.setText(dateStr)
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (task != null) {
            dialogTitle.text = getString(R.string.edit_task)
            titleEditText.setText(task.title)
            descriptionEditText.setText(task.description)
        } else {
            dialogTitle.text = getString(R.string.add_task)
        }
        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val wantsReminder = chbReminder.isChecked

            if (title.isNotBlank()) {
                val updatedTask = Task(
                    id = task?.id ?: 0,
                    title = title,
                    description = description,
                    dueDate = selectedDeadline ?: 0,
                    isDone = false,
                    reminderTime = wantsReminder
                )

                if (task == null) {
                    viewModel.addTask(updatedTask)
                } else {
                    viewModel.updateTask(updatedTask)
                }

            } else {
                Toast.makeText(this, "fill the title", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun convertLongToDate(timeMillis: Long, pattern: String = "yyyy/MM/dd"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }

    private var selectedDateTimeInMillis: Long = 0

    private fun showDateTimePicker(context: Context, onDateTimeSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(context, { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    val millis = calendar.timeInMillis
                    selectedDateTimeInMillis = millis
                    onDateTimeSelected(millis)

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


}
