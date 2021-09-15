package com.ramirjr.pigeon.models

import android.widget.TextView
import com.ramirjr.pigeon.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class LatestMessageRow(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_text_latest_message).text =
            chatMessage.message
    }

    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }
}