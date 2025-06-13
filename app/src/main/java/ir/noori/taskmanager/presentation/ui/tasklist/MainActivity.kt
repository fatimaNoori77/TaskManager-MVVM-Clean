package ir.noori.taskmanager.presentation.ui.tasklist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.noori.taskmanager.databinding.ActivityMainBinding
import ir.noori.taskmanager.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            Toast.makeText(this, "Add task clicked (todo)", Toast.LENGTH_SHORT).show()
            // یا رفتن به صفحه افزودن تسک
        }
    }
}
