package com.zokogram.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zokogram.data.local.entity.LastSeenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LastSeenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: LastSeenEntity)

    @Query("SELECT lastSeenAt FROM last_seen WHERE userId = :userId")
    fun observeLastSeen(userId: Long): Flow<Long?>

    @Query("DELETE FROM last_seen WHERE userId = :userId")
    suspend fun delete(userId: Long)
}
