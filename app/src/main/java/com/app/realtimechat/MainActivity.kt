package com.app.realtimechat

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.realtimechat.adapter.ChatAdapter
import com.app.realtimechat.client.SocketClient
import com.app.realtimechat.model.Message
import com.app.realtimechat.utils.Constants.isNetworkAvailable
import com.app.realtimechat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter
    private lateinit var etMessage: EditText
    private lateinit var emptyStateTextView: TextView
    private lateinit var btnSend: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var connectivityManager: ConnectivityManager
    var isInternetAvailable = false
    val chatId = "message"
    companion object {
        val failedMessages: MutableList<String> = mutableListOf()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        recyclerView = findViewById(R.id.chatRecyclerView)
        etMessage = findViewById(R.id.messageEditText)
        btnSend = findViewById(R.id.sendButton)
        emptyStateTextView = findViewById(R.id.emptyStateTextView)
        adapter = ChatAdapter()
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter = adapter
        btnSend.setOnClickListener {
            val content = etMessage.text.toString()
            if (content.isNotBlank()) {
                val message = Message(
                    chatId = chatId,
                    content = content,
                    isSent = true,
                    isRead = false,
                    timestamp = System.currentTimeMillis()
                )
                viewModel.sendMessage(message)
                etMessage.text.clear()
            }
        }

        viewModel.messages(chatId).observe(this) {
            adapter.submitList(it)
            if (it.isEmpty()) {
                emptyStateTextView.visibility = View.VISIBLE
                emptyStateTextView.text=getString(R.string.no_chats_available)
                recyclerView.visibility = View.GONE
            } else {
                emptyStateTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

    }
    private fun checkForInternetAndSync() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        Log.d("TAG", "checkForInternetAndSync: syncOfflineAPI")
        if (!isInternetAvailable) {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback )
        }


    }
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("TAG", "checkForInternetAndSync: Network Available $isInternetAvailable")

            if (!isInternetAvailable) {
                isInternetAvailable = true
                //if(viewModel.socketClient == null || viewModel.socketClient!!.socket.isClosed) {
                    viewModel.setChatId(chatId)
                    viewModel.socketClient = SocketClient(viewModel, this@MainActivity)
                    viewModel.socketClient?.connect(chatId)
                //}
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isInternetAvailable = false
            viewModel.socketClient?.close()


        }
    }

    override fun onResume() {
        super.onResume()
        if (!isNetworkAvailable(this)) {
            emptyStateTextView.visibility = View.VISIBLE
            emptyStateTextView.text=getString(R.string.no_internet_connection)
        }else {
            checkForInternetAndSync()


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearMessages() // Clear on app close
        viewModel.socketClient?.close()
        connectivityManager.unregisterNetworkCallback( networkCallback )

    }
}