package com.zokogram.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_seen")
data class LastSeenEntity(
    @PrimaryKey val userId: Long,
    val lastSeenAt: Long,
)
