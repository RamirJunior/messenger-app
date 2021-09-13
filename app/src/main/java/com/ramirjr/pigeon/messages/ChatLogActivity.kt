package com.ramirjr.pigeon.messages

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.databinding.ActivityChatLogBinding
import com.ramirjr.pigeon.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatLogActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatLogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        Log.d("ChatLog", "Usuário recebido = ${user}")
        supportActionBar?.title = user?.username

        sendTestMessages()

        binding.sendButtonChatLog.setOnClickListener {
            Log.d("ChatLog", "Pronto para enviar sua mensagem?")
            performSendMessages()
        }
    }

    private fun performSendMessages() {
        val text = binding.edittextChatLog.text.toString()

    }

    private fun sendTestMessages() {
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatItemReceived("hi there"))
        adapter.add(ChatItemSent("helo who are you\nAre you ok?"))

        binding.recyclerviewChatLog.adapter = adapter
    }
}

class ChatItemReceived(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_msg_received).text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_received
    }
}

class ChatItemSent(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_msg_sent).text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_sent
    }
}