package com.zokogram.data.spy

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

data class CachedMessage(
    val chatId: Long,
    val messageId: Long,
    val text: String,
    val contentType: String
)

@Singleton
class MessageCache @Inject constructor() {
    private val cache = ConcurrentHashMap<String, CachedMessage>()

    private fun getKey(chatId: Long, messageId: Long) = "$chatId_$messageId"

    fun put(chatId: Long, messageId: Long, text: String, contentType: String) {
        cache[getKey(chatId, messageId)] = CachedMessage(chatId, messageId, text, contentType)
    }

    fun get(chatId: Long, messageId: Long): CachedMessage? {
        return cache[getKey(chatId, messageId)]
    }

    fun remove(chatId: Long, messageId: Long) {
        cache.remove(getKey(chatId, messageId))
    }
}
