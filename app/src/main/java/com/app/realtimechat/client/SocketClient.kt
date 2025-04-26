package com.app.realtimechat.client

import android.content.Context
import android.util.Log
import com.app.realtimechat.MainActivity
import com.app.realtimechat.model.Message
import com.app.realtimechat.utils.Constants
import com.app.realtimechat.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI

class SocketClient(private val viewModel: ChatViewModel,val context: Context) {
    private lateinit var socket: WebSocketClient


    private var currentChatId: String = ""
    fun connect(chatId: String) {
        currentChatId = chatId
        socket = object : WebSocketClient(URI(Constants.BASE_URL)) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("TAG", "onOpen: ")
                flushQueue()

            }

            override fun connect() {
                super.connect()
                Log.d("TAG", "connect: called")
            }

            override fun reconnect() {
                super.reconnect()
                Log.d("TAG", "reconnect: ")
            }

            override fun getConnection(): WebSocket {
                return super.getConnection()
                Log.d("TAG", "getConnection: ")
            }

            override fun onMessage(message: String?) {
                Log.d("TAG", "onMessage: $message")
                message?.let {
                    val msg = Message(
                        chatId = chatId,
                        content = it,
                        isSent = true,
                        isRead = true,
                        timestamp = System.currentTimeMillis()
                    )
                    viewModel.receiveMessage(msg)
                }


            }


            override fun onClose(code: Int, reason: String?, remote: Boolean) {

            }
            override fun onError(ex: Exception?) {
                CoroutineScope(Dispatchers.Main).launch {
                    Constants.showAlert(context)
                }
            }
        }
        socket.connect()
    }


    fun sendMessage(message: String) {
        if (this::socket.isInitialized && socket.isOpen) {
            Log.d("TAG", "sendMessage: $message")
            socket.send(message)
        } else {
            Log.d("TAG", "else sendMessage: $message")
            MainActivity.failedMessages.add(message)
        }
    }

    private fun flushQueue() {
        Log.d("TAG", "flushQueue: ${MainActivity.failedMessages.size}")

        for(i in MainActivity.failedMessages){
            socket.send(i)
        }
    }

    fun close() {
        if (this::socket.isInitialized) {
            socket.close()
        }
    }
}