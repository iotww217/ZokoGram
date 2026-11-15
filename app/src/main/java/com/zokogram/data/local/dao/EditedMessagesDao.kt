package com.zokogram.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zokogram.data.local.entity.EditedMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EditedMessagesDao {
    @Insert
    suspend fun insert(item: EditedMessageEntity)

    @Query("SELECT DISTINCT messageId FROM edited_messages WHERE chatId = :chatId")
    fun observeEditedIds(chatId: Long): Flow<List<Long>>

    @Query("SELECT * FROM edited_messages WHERE chatId = :chatId AND messageId = :messageId ORDER BY editedAt ASC")
    fun observeVersions(chatId: Long, messageId: Long): Flow<List<EditedMessageEntity>>

    @Query("DELETE FROM edited_messages WHERE chatId = :chatId")
    suspend fun clearChat(chatId: Long)

    @Query("DELETE FROM edited_messages")
    suspend fun clearAll()
}
