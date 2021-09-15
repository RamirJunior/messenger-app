package com.ramirjr.pigeon.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramirjr.pigeon.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener {
            val email = binding.txtInputEmail.editText?.text.toString()
            val password = binding.txtInputPassword.editText.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //condição verdadeira
                    Log.d("Login", "Usuario logado com sucesso: ${it.result?.user?.uid}")
                }
                .addOnFailureListener {
                    Log.d("Login", "Falha ao logar usuario: ${it.message}")
                    Toast.makeText(this, "Falha no login: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        binding.textviewRegisterNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}