package com.ramirjr.pigeon.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.databinding.ActivityLatestMessagesBinding
import com.ramirjr.pigeon.models.ChatMessage
import com.ramirjr.pigeon.models.User
import com.ramirjr.pigeon.registerlogin.LoginActivity
import com.ramirjr.pigeon.views.LatestMessageRow
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class LatestMessagesActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLatestMessagesBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()

        verifyUserIsLoggedIn()

        binding.recyclerviewLatestMessages.adapter = adapter
        binding.recyclerviewLatestMessages.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)

            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)

            startActivity(intent)
        }

        fetchCurrentUser()

        listenLatestMessages()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbarLatestMessages)
        supportActionBar?.title = null
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun listenLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                adapter.add(LatestMessageRow(chatMessage))

                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessage()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                adapter.add(LatestMessageRow(chatMessage))

                // convertendo as ultimas msgs em hash e recarregando no recyclerview
                latestMessagesMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessage()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
//                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }

        })
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    private fun refreshRecyclerViewMessage() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("LatestMessages", "Usuario atual: ${currentUser}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.option_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        var currentUser: User? = null
    }
}