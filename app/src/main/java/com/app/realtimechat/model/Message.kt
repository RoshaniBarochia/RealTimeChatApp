package com.app.realtimechat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chatId: String,
    val content: String,
    val isSent: Boolean,
    val isRead: Boolean,
    val timestamp: Long
)