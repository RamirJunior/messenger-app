package com.ramirjr.pigeon.messages

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.databinding.ActivityChatLogBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatLogActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatLogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
//        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = username

        sendTestMessages()
    }

    private fun sendTestMessages() {
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatItemReceived())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemReceived())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemReceived())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemReceived())
        adapter.add(ChatItemSent())
        adapter.add(ChatItemReceived())
        adapter.add(ChatItemSent())

        binding.recyclerviewChatLog.adapter = adapter
    }
}

class ChatItemReceived : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_msg_received).text =
            "Mensagem recebida..."
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_received
    }
}

class ChatItemSent : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_msg_sent).text =
            "Está é uma mensagem enviada que irá passar de uma linha para exibição..."
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_sent
    }
}