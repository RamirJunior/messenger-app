package com.ramirjr.pigeon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramirjr.pigeon.databinding.ActivityMainBinding

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            cadastrarUsuario()
        }

        binding.btnEscolherFoto.setOnClickListener {
            Log.d("photo:", "hora de escolher uma")
        }

        binding.textviewFacaLogin.setOnClickListener {
            iniciarNovaActivity()
        }
    }

    private fun iniciarNovaActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun cadastrarUsuario(): Boolean {
        var email = binding.inputEmail.editText?.text.toString()
        var senha = binding.inputSenha.editText?.text.toString()

        if ((email.isEmpty() || senha.isEmpty())) {
            Toast.makeText(this, "Por favor, digite E-mail/Senha.", Toast.LENGTH_SHORT).show()
            return true
        }
        Log.d("main", "email: ${email}")
        Log.d("main", "senha: ${senha}")

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor digite e-mail/senha.", Toast.LENGTH_SHORT).show()
            return true
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //condição verdadeira
                Log.d("Main", "funcionou ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Main", "Falha ao criar usuario: ${it.message}")
                Toast.makeText(this, "Falha ao criar usuario: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        return false
    }
}