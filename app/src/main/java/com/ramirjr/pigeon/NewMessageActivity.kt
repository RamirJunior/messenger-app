package com.ramirjr.pigeon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ramirjr.pigeon.databinding.ActivityNewMessageBinding
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class NewMessageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewMessageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Escolha um usuário"

//        val adapter = GroupAdapter<GroupieViewHolder>()
//        adapter.add()
//        binding.recyclerviewNewMessages.adapter = adapter
    }
}

class UserItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // sera chamada dentro da lista pra cada usuario carregado por ultimo
    }


    override fun getLayout(): Int {
        TODO("Not yet implemented")
    }

}
