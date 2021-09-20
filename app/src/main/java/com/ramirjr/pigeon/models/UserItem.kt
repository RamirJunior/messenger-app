package com.ramirjr.pigeon.models

import android.widget.ImageView
import android.widget.TextView
import com.ramirjr.pigeon.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class UserItem(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_username).text = user.username

        val targetImage = viewHolder.itemView.findViewById<ImageView>(R.id.imageview_new_message)
        Picasso.get().load(user.profileImageUrl).into(targetImage)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_new_message
    }
}