package com.ramirjr.pigeon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.ramirjr.pigeon.databinding.ActivityNewMessageBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NewMessageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewMessageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Escolha um usuário"

        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())

        binding.recyclerviewNewMessages.adapter = adapter

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent()
    }

}

class UserItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // sera chamada dentro da lista pra cada usuario carregado por ultimo
    }


    override fun getLayout(): Int {
        return R.layout.user_row_new_new_message
    }

}
