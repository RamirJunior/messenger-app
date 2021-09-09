package com.ramirjr.pigeon.registerlogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ramirjr.pigeon.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.textviewRegistreSe.setOnClickListener {
            finish()
        }
    }
}