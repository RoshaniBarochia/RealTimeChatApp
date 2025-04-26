package com.app.realtimechat.repository

import com.app.realtimechat.db.MessageDao
import com.app.realtimechat.model.Conversation
import com.app.realtimechat.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val dao: MessageDao
) {
    fun getMessages(chatId: String) = dao.getMessagesForChat(chatId)

    suspend fun saveMessage(message: Message) = dao.insertMessage(message)

    suspend fun clearMessages() = dao.clearAllMessages()

    fun getConversations(): Flow<List<Conversation>> = dao.getConversations()

}