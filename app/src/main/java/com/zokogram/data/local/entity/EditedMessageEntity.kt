package com.zokogram.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "edited_messages",
    indices = [Index(value = ["chatId", "messageId"])],
)
data class EditedMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val chatId: Long,
    val messageId: Long,
    val oldText: String,
    val contentType: String,
    val editedAt: Long,
)
