package com.ramirjr.pigeon.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.databinding.ActivityLoginBinding
import com.ramirjr.pigeon.messages.LatestMessagesActivity
import com.ramirjr.pigeon.views.LoadingDialogLogin

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    val loadingDialog: LoadingDialogLogin = LoadingDialogLogin(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener {
            val email = binding.txtInputEmail.editText?.text.toString()
            val password = binding.txtInputPassword.editText?.text.toString()

            if (validateEmail(email) && validatePassword(password)) {
                logOnFirebase(email, password)
            }
        }

        binding.textviewRegisterNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logOnFirebase(email: String, password: String) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Login", "Usuario logado com sucesso: ${it.result?.user?.uid}")
                startLatestMessagesActivity()
                loadingDialog.startLoadingDialog()
            }
            .addOnFailureListener {
                Log.d("Login", "Falha ao logar usuario: ${it.message}")
                Toast.makeText(
                    this,
                    "Usuário não encontrado.\n\nPor favor registre-se.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
    }

    private fun validateEmail(email: String?): Boolean {
        val emailInput = email
        if (!emailInput.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.txtInputEmail.error = null
            return true
        } else {
            binding.txtInputEmail.error = getString(R.string.invalid_email)
            return false
        }
    }

    private fun validatePassword(password: String?): Boolean {
        val passwordInput = password
        if (!passwordInput.isNullOrEmpty() && passwordInput.length >= 6) {
            binding.txtInputPassword.error = null
            return true
        } else {
            binding.txtInputPassword.error = getString(R.string.invalid_password)
            return false
        }
    }

    private fun startLatestMessagesActivity() {
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}