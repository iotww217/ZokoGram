package com.zokogram.data.repository

import com.zokogram.data.local.dao.EditedMessagesDao
import com.zokogram.data.local.entity.EditedMessageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditedMessagesRepository @Inject constructor(
    private val editedMessagesDao: EditedMessagesDao
) {
    fun observeEditedIds(chatId: Long): Flow<List<Long>> {
        return editedMessagesDao.observeEditedIds(chatId)
    }

    fun observeVersions(chatId: Long, messageId: Long): Flow<List<EditedMessageEntity>> {
        return editedMessagesDao.observeVersions(chatId, messageId)
    }

    suspend fun clearChat(chatId: Long) {
        editedMessagesDao.clearChat(chatId)
    }

    suspend fun clearAll() {
        editedMessagesDao.clearAll()
    }
}
