package com.ramirjr.pigeon.messages

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.ramirjr.pigeon.R

class LoadingDialog internal constructor(private val activity: Activity) {
    private var dialog: AlertDialog? = null
    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_register_user, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog!!.show()
    }
}