package com.ramirjr.pigeon.views

import android.widget.ImageView
import android.widget.TextView
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatItemReceived(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_msg_received).text = text

        //carregando imagem do contato na conversa
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.findViewById<ImageView>(R.id.imageViewReceived)
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_message_received
    }
}