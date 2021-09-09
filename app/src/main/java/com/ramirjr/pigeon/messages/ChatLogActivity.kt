package com.ramirjr.pigeon.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ramirjr.pigeon.databinding.ActivityChatLogBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class ChatLogActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatLogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Conversa"

        val adapter = GroupAdapter<GroupieViewHolder>()
        binding.recyclerviewChatLog.adapter = adapter
    }
}