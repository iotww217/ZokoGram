package com.zokogram.data.repository

import com.zokogram.data.local.dao.LastSeenDao
import com.zokogram.data.local.entity.LastSeenEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastSeenRepository @Inject constructor(
    private val lastSeenDao: LastSeenDao
) {
    suspend fun updateLastSeen(userId: Long, timestamp: Long) {
        lastSeenDao.insertOrUpdate(LastSeenEntity(userId = userId, lastSeenAt = timestamp))
    }

    fun observeLastSeen(userId: Long): Flow<Long?> {
        return lastSeenDao.observeLastSeen(userId)
    }

    suspend fun delete(userId: Long) {
        lastSeenDao.delete(userId)
    }
}
