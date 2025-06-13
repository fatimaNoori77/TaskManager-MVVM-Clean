package ir.noori.taskmanager.data.model

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("is_done")
    val isDone: Boolean,

    @SerializedName("dueDate")
    val dueDate: Long,

    @SerializedName("reminderTime")
    val reminderTime: Long
)
