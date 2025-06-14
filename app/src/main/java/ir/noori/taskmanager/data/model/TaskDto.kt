package ir.noori.taskmanager.data.model

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("completed")
    val isDone: Boolean,

    @SerializedName("userId")
    val userId: Int
)

