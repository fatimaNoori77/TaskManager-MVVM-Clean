package ir.noori.taskmanager.data.dto

import com.google.gson.annotations.SerializedName

data class TaskDto(
    val id: Int,

    val title: String,

    @SerializedName("completed")
    val isDone: Boolean,

    val userId: Int
)

