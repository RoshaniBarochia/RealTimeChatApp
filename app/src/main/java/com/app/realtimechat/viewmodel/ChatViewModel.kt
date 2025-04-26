package com.app.realtimechat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.realtimechat.client.SocketClient
import com.app.realtimechat.model.Conversation
import com.app.realtimechat.repository.ChatRepository
import com.app.realtimechat.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _currentChatId = MutableLiveData<String>()
    val currentChatId: LiveData<String> = _currentChatId

    private val _conversations = repository.getConversations().asLiveData()
    val conversations: LiveData<List<Conversation>> = _conversations

    fun setChatId(chatId: String) {
        _currentChatId.postValue(chatId)
    }

    fun messages(chatId: String): LiveData<List<Message>> =
        repository.getMessages(chatId).asLiveData()

    fun sendMessage(message: Message) = viewModelScope.launch {
        //repository.saveMessage(message)
        socketClient?.sendMessage(message.content)
    }
    fun receiveMessage(message: Message) = viewModelScope.launch {
        repository.saveMessage(message)
    }


    fun clearMessages() = viewModelScope.launch {
        repository.clearMessages()
    }

    var socketClient: SocketClient? = null
}
