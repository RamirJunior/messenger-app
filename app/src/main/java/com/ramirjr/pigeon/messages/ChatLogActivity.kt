package com.ramirjr.pigeon.messages

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.databinding.ActivityChatLogBinding
import com.ramirjr.pigeon.models.ChatMessage
import com.ramirjr.pigeon.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatLogActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatLogBinding.inflate(layoutInflater) }
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerviewChatLog.adapter = adapter

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        Log.d("ChatLog", "Usuário recebido = ${user}")
        supportActionBar?.title = user?.username

//        sendTestMessages()
        listenFirebaseMessages()
        binding.sendButtonChatLog.setOnClickListener {
            performSendMessages()
        }
    }

    private fun listenFirebaseMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        Log.d("ChatLog", "Mensagem enviada: ${chatMessage.message}")
                        adapter.add(ChatItemSent(chatMessage.message))
                    } else {
                        Log.d("ChatLog", "Mensagem recebida: ${chatMessage.message}")
                        adapter.add(ChatItemReceived(chatMessage.message))
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun performSendMessages() {
        val text = binding.edittextChatLog.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user?.uid
        if (fromId == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage =
            ChatMessage(ref.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("ChatLog", "Mensagem salva ${ref.key}")

            }
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