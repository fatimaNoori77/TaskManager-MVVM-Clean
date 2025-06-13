package ir.noori.taskmanager.presentation.ui.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.noori.taskmanager.databinding.ItemTaskBinding
import ir.noori.taskmanager.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(
    val onCheckChanged: (Task, Boolean) -> Unit,
    val onDeleteClicked: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) = with(binding) {
            textTaskTitle.text = task.title
            textTaskDueDate.text = buildSpannedString {
                append("Due: ")
                append(SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(task.dueDate)))
            }
            checkBoxDone.isChecked = task.isDone

            checkBoxDone.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(task, isChecked)
            }

            buttonDelete.setOnClickListener {
                onDeleteClicked(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}
