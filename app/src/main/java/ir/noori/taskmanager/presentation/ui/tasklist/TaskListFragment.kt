package ir.noori.taskmanager.presentation.ui.tasklist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.R
import ir.noori.taskmanager.databinding.FragmentTaskListBinding
import ir.noori.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private var _binding : FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel: TaskViewModel by activityViewModels()
    private val taskAdapter = TaskAdapter(
        onCheckChanged = { task, _ ->
            taskViewModel.toggleTaskDone(task)
        },
        onDeleteClicked = { task ->
            taskViewModel.deleteTask(task.id)
        },
        onTaskClicked = {
            showTaskDialog(it)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaskListBinding.bind(view)

        initViewBindings()
        observers()
    }

    private fun initViewBindings() {
        setupRecyclerView()
        binding.swipeRefreshLayout.setOnRefreshListener {
            taskViewModel.refreshTasks()
        }

        binding.fabAddTask.setOnClickListener {
            AddTaskDialogFragment().show(childFragmentManager, "AddTaskDialog")
        }

    }

    private fun setupRecyclerView() {
        binding.recyclerTasks.apply {
            adapter = taskAdapter
        }
    }

    private fun observers(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                taskViewModel.uiState.collect { state ->
                    when (state) {
                        is TaskUiState.Idle -> {}
                        is TaskUiState.Loading -> {}

                        is TaskUiState.Success -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                        }

                        is TaskUiState.Error -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            Toast.makeText(
                                requireContext(),
                                getString(state.messageRes),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                taskViewModel.tasks.collectLatest { taskList ->
                binding.swipeRefreshLayout.isRefreshing = false
                taskAdapter.submitList(taskList)
            }
            }
        }
    }

    private fun showTaskDialog(task: Task? = null) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
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
            showDateTimePicker(requireContext())
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

        val dialog = AlertDialog.Builder(requireContext())
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
                    taskViewModel.addTask(updatedTask)
                } else {
                    taskViewModel.updateTask(updatedTask)
                }

            } else {
                Toast.makeText(requireContext(), "fill the title", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}