package ir.noori.taskmanager.presentation.ui.tasklist

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.databinding.DialogAddTaskBinding
import ir.noori.taskmanager.domain.model.Task

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
        binding.editTextDeadline.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select deadline")
                .build()

            picker.addOnPositiveButtonClickListener { selectedDate ->
                binding.editTextDeadline.setText(picker.headerText)
            }
            picker.show(parentFragmentManager, "deadlinePicker")
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.submitButton.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            val description = binding.editTextDescription.text.toString().trim()
            val deadline = binding.editTextDeadline.text.toString().trim()
            val reminder = binding.chbReminder.isChecked

            if (title.isEmpty()) {
                binding.editTextTitle.error = "Title is required"
                return@setOnClickListener
            }

            viewModel.addTask(Task(title = title, description = description))
            Toast.makeText(requireContext(), "Task added", Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}