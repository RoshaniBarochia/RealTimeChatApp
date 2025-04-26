package com.app.realtimechat.model

data class Conversation(
    val chatId: String,
    val latestMessage: String,
    val timestamp: Long,
    val unreadCount: Int
)