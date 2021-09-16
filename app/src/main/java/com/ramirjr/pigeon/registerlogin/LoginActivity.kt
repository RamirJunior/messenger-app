package com.ramirjr.pigeon.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramirjr.pigeon.databinding.ActivityLoginBinding
import com.ramirjr.pigeon.messages.LatestMessagesActivity
import com.ramirjr.pigeon.messages.LoadingDialogLogin

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val loadingDialog: LoadingDialogLogin = LoadingDialogLogin(this)

        binding.btnEntrar.setOnClickListener {
            loadingDialog.startLoadingDialog()

            val email = binding.txtInputEmail.editText?.text.toString()
            val password = binding.txtInputPassword.editText?.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //condição verdadeira
                    Log.d("Login", "Usuario logado com sucesso: ${it.result?.user?.uid}")

                    startLatestMessagesActivity()

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

    private fun startLatestMessagesActivity() {
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}