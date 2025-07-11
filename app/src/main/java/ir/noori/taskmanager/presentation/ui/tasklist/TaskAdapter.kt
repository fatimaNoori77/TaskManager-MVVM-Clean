package ir.noori.taskmanager.presentation.ui.tasklist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
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
    val onDeleteClicked: (Task) -> Unit,
    val onTaskClicked: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) = with(binding) {
            txtTaskTitle.text = task.title
            if(task.description?.isEmpty() == true){
                txtTaskDescription.visibility = View.GONE
            }else{
                txtTaskDescription.visibility = View.VISIBLE
                txtTaskDescription.text = task.description
            }
            txtDueDate.text = buildSpannedString {
                append("Due: ")
                append(SimpleDateFormat("MMM dd HH:mm", Locale.getDefault()).format(Date(task.dueDate)))
            }
            chbDoneTask.isChecked = task.isDone

            txtTaskTitle.paintFlags = if (task.isDone) {
                txtTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                txtTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            chbFavorite.setOnCheckedChangeListener { _, isChecked ->
            }


            chbDoneTask.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(task, isChecked)
            }

            itemView.setOnClickListener {
                onTaskClicked.invoke(task)
            }

            btnDelete.setOnClickListener {
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
