package com.app.realtimechat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.realtimechat.model.Conversation
import com.app.realtimechat.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<Message>>

    @Query("DELETE FROM messages")
    suspend fun clearAllMessages()

    @Query("""
    SELECT chatId, 
           MAX(timestamp) as timestamp,
           (SELECT content FROM messages m2 WHERE m2.chatId = m.chatId ORDER BY timestamp DESC LIMIT 1) as latestMessage,
           SUM(CASE WHEN isRead = 0 THEN 1 ELSE 0 END) as unreadCount
    FROM messages m
    GROUP BY chatId
    ORDER BY timestamp DESC
""")
    fun getConversations(): Flow<List<Conversation>>
}