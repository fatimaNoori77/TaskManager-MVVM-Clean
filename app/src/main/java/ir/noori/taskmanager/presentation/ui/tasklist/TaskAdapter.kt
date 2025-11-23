package ir.noori.taskmanager.presentation.ui.tasklist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.noori.taskmanager.databinding.ItemTaskBinding
import ir.noori.taskmanager.domain.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(
    private val onCheckChanged: (Task, Boolean) -> Unit,
    private val onDeleteClicked: (Task) -> Unit,
    private val onTaskClicked: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) = with(binding) {

            txtTaskTitle.text = task.title
            txtTaskTitle.paintFlags = txtTaskTitle.paintFlags
                .let { flags ->
                    if (task.isDone) flags or Paint.STRIKE_THRU_TEXT_FLAG
                    else flags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            txtTaskDescription.apply {
                isVisible = !task.description.isNullOrEmpty()
                text = task.description
            }
            txtDueDate.text = formatDueDate(task.dueDate)
            chbDoneTask.apply {
                setOnCheckedChangeListener(null)
                isChecked = task.isDone
                setOnCheckedChangeListener { _, isChecked ->
                    onCheckChanged(task, isChecked)
                }
            }
//            chbFavorite.apply {
//                setOnCheckedChangeListener(null)
//                isChecked = task.isFavorite
//                setOnCheckedChangeListener { _, _ ->
//                    // Optional future behavior
//                }
//            }
            root.setOnClickListener { onTaskClicked(task) }
            btnDelete.setOnClickListener { onDeleteClicked(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }

    private fun formatDueDate(timestamp: Long): CharSequence {
        return buildSpannedString {
            append("Due: ")
            append(SimpleDateFormat("MMM dd HH:mm", Locale.getDefault()).format(Date(timestamp)))
        }
    }
}
