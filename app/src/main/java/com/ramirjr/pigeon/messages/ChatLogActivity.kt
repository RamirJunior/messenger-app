package com.ramirjr.pigeon.messages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.ramirjr.pigeon.databinding.ActivityChatLogBinding
import com.ramirjr.pigeon.models.ChatItemReceived
import com.ramirjr.pigeon.models.ChatItemSent
import com.ramirjr.pigeon.models.ChatMessage
import com.ramirjr.pigeon.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatLogActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatLogBinding.inflate(layoutInflater) }
    val adapter = GroupAdapter<GroupieViewHolder>()
    val user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerviewChatLog.adapter = adapter

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username

        binding.sendButtonChatLog.setOnClickListener {
            performSendMessages()
        }

        listenFirebaseMessages()
    }

    private fun listenFirebaseMessages() {
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val fromId = FirebaseAuth.getInstance().uid
        val toId = user?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestMessagesActivity.currentUser ?: return
                        Log.d("ChatLog", "msg enviada: ${chatMessage.message}")
                        adapter.add(ChatItemSent(chatMessage.message, currentUser))
                    } else {
                        Log.d("ChatLog", "Msg recebida: ${chatMessage.message}")
                        val userReceived =
                            intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
                        adapter.add(ChatItemReceived(chatMessage.message, userReceived!!))
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

//        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toRef =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage =
            ChatMessage(ref.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("ChatLog", "Mensagem salva ${ref.key}")
                binding.edittextChatLog.text.clear()
                binding.recyclerviewChatLog.scrollToPosition(adapter.itemCount - 1)
            }
        toRef.setValue(chatMessage)

        //registrando ultima msg do remetente
        val latestMessageRefSender =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRefSender.setValue(chatMessage)

        //registrando ultima msg do receptor
        val latestMessageRefReceiver =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageRefReceiver.setValue(chatMessage)
    }
}
