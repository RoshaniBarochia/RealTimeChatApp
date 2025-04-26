package com.app.realtimechat

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.realtimechat.adapter.ConversationAdapter
import com.app.realtimechat.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversationListActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)

        adapter = ConversationAdapter { chatId ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("chatId", chatId)
            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.rvConversations).apply {
            layoutManager = LinearLayoutManager(this@ConversationListActivity)
            this.adapter = this@ConversationListActivity.adapter
        }

        viewModel.conversations.observe(this) {
            adapter.submitList(it)
        }
    }
}
