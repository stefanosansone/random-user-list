package dev.stefanosansone.randomuserlist.data.model

import androidx.room.Entity

@Entity(tableName = "users", primaryKeys = ["firstName","lastName"])
data class UserEntity(
    val firstName: String,
    val lastName: String,
    val email: String,
    val page: Int
)
