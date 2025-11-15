package ir.noori.taskmanager.presentation.ui.tasklist

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.databinding.DialogAddTaskBinding
import ir.noori.taskmanager.domain.model.Task
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddTaskDialogFragment : DialogFragment() {

    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddTaskBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.7f)
        }
        setupUi()

        return dialog
    }

    private fun setupUi() {
        var selectedDeadlineMillis: Long? = null
        binding.editTextDeadline.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select deadline date")
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedDateMillis ->

                val selectedDate = Instant.ofEpochMilli(selectedDateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select deadline time")
                    .build()

                timePicker.addOnPositiveButtonClickListener {

                    val hour = timePicker.hour
                    val minute = timePicker.minute
                    val dateTime = selectedDate.atTime(hour, minute)
                    val deadlineMillis = dateTime
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    val formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    binding.editTextDeadline.setText(formatted)
                    selectedDeadlineMillis = deadlineMillis
                }

                timePicker.show(parentFragmentManager, "timePicker")
            }

            datePicker.show(parentFragmentManager, "datePicker")
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.submitButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            val reminder = binding.chbReminder.isChecked

            if (title.isEmpty()) {
                binding.editTextTitle.error = "Title is required"
                return@setOnClickListener
            }
            if (selectedDeadlineMillis == null) {
                binding.editTextDeadline.error = "Please select deadline"
                return@setOnClickListener
            }

            viewModel.addTask(Task(title = title, description = description, dueDate = selectedDeadlineMillis, reminderTime = reminder))
            Toast.makeText(requireContext(), "Task added", Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}