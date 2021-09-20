package com.ramirjr.pigeon.views

import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.models.ChatMessage
import com.ramirjr.pigeon.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageRow(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {

    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // enviando ultima mensagem para textview
        viewHolder.itemView.findViewById<TextView>(R.id.textview_text_latest_message).text =
            chatMessage.message

        // verificando se é sender ou receptor da msg
        val chatPartnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = chatMessage.toId.toString()
        } else {
            chatPartnerId = chatMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                // carregando o nome correto do usuario no recycler
                viewHolder.itemView.findViewById<TextView>(R.id.textview_username_latest_messages).text =
                    chatPartnerUser?.username

                // carregando foto
                val targetImageView =
                    viewHolder.itemView.findViewById<ImageView>(R.id.imageview_user_image_latest_message)
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }
}