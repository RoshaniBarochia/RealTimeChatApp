package com.app.realtimechat.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.realtimechat.model.Message

@Database(entities = [Message::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}