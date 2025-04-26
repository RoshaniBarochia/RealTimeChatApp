package com.app.realtimechat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.realtimechat.databinding.ItemConversationBinding
import com.app.realtimechat.model.Conversation

class ConversationAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<Conversation, ConversationAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemConversationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Conversation) {
            binding.tvChatId.text = item.chatId
            binding.tvPreview.text = item.latestMessage
            binding.tvUnreadCount.text = if (item.unreadCount > 0) "${item.unreadCount} unread" else ""
            binding.root.setOnClickListener { onClick(item.chatId) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Conversation>() {
        override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation) = oldItem.chatId == newItem.chatId
        override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation) = oldItem == newItem
    }
}
