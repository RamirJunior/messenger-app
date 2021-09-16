package com.ramirjr.pigeon.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ramirjr.pigeon.databinding.ActivityNewMessageBinding
import com.ramirjr.pigeon.models.User
import com.ramirjr.pigeon.models.UserItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class NewMessageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewMessageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarNewMessage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Escolha um usuário"
        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()

                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid != FirebaseAuth.getInstance().uid) {
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    Log.d("New Message", "Usuário enviado = ${userItem}")

                    val intent = Intent(view.context, ChatLogActivity::class.java).apply {
                        putExtra(USER_KEY, userItem.user)
                    }
                    startActivity(intent)

                    finish()
                }
                binding.recyclerviewNewMessages.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }
}
