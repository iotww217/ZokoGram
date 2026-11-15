package com.zokogram.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zokogram.data.local.entity.EditedMessageEntity
import com.zokogram.data.repository.EditedMessagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val editedMessagesRepository: EditedMessagesRepository
) : ViewModel() {

    private val _currentChatId = MutableStateFlow<Long>(0L)

    fun setChatId(chatId: Long) {
        _currentChatId.value = chatId
    }

    // Наблюдение за ID сообщений, которые были изменены в текущем чате
    val editedMessageIds: StateFlow<List<Long>> = _currentChatId
        .flatMapLatest { chatId ->
            if (chatId != 0L) editedMessagesRepository.observeEditedIds(chatId)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Состояние для выбранного сообщения, чью историю нужно показать в диалоге
    private val _selectedMessageForHistory = MutableStateFlow<Pair<Long, Long>?>(null) // Pair<ChatId, MessageId>
    
    val messageVersions: StateFlow<List<EditedMessageEntity>> = _selectedMessageForHistory
        .flatMapLatest { pair ->
            if (pair != null) {
                editedMessagesRepository.observeVersions(pair.first, pair.second)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun showHistory(chatId: Long, messageId: Long) {
        _selectedMessageForHistory.value = Pair(chatId, messageId)
    }

    fun dismissHistory() {
        _selectedMessageForHistory.value = null
    }
}
