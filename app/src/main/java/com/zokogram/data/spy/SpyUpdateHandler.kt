package com.zokogram.data.spy

import com.zokogram.data.local.dao.EditedMessagesDao
import com.zokogram.data.local.dao.LastSeenDao
import com.zokogram.data.local.entity.EditedMessageEntity
import com.zokogram.data.local.entity.LastSeenEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpyUpdateHandler @Inject constructor(
    private val messageCache: MessageCache,
    private val editedMessagesDao: EditedMessagesDao,
    private val lastSeenDao: LastSeenDao
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun handleUpdate(update: TdApi.Update) {
        when (update) {
            is TdApi.UpdateNewMessage -> {
                val msg = update.message
                val contentText = extractText(msg.content)
                val contentType = msg.content.constructor.toString()
                messageCache.put(msg.chatId, msg.id, contentText, contentType)
            }
            is TdApi.UpdateMessageContent -> {
                val cached = messageCache.get(update.chatId, update.messageId)
                val newText = extractText(update.newContent)
                if (cached != null && cached.text != newText) {
                    scope.launch {
                        editedMessagesDao.insert(
                            EditedMessageEntity(
                                chatId = update.chatId,
                                messageId = update.messageId,
                                oldText = cached.text,
                                contentType = cached.contentType,
                                editedAt = System.currentTimeMillis()
                            )
                        )
                    }
                }
                if (cached != null) {
                    messageCache.put(update.chatId, update.messageId, newText, cached.contentType)
                }
            }
            is TdApi.UpdateUserStatus -> {
                val userId = update.userId
                val status = update.status
                if (status is TdApi.UserStatusOnline) {
                    scope.launch {
                        lastSeenDao.insertOrUpdate(
                            LastSeenEntity(userId = userId, lastSeenAt = System.currentTimeMillis())
                        )
                    }
                } else if (status is TdApi.UserStatusOffline) {
                    scope.launch {
                        lastSeenDao.insertOrUpdate(
                            LastSeenEntity(userId = userId, lastSeenAt = status.wasOnline.toLong() * 1000)
                        )
                    }
                }
            }
        }
    }

    private fun extractText(content: TdApi.MessageContent): String {
        return when (content) {
            is TdApi.MessageText -> content.text.text
            is TdApi.MessagePhoto -> content.caption.text
            is TdApi.MessageVideo -> content.caption.text
            is TdApi.MessageDocument -> content.caption.text
            else -> ""
        }
    }
}
