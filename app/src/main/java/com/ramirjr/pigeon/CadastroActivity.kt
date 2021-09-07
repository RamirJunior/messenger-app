package com.ramirjr.pigeon

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.ramirjr.pigeon.databinding.ActivityMainBinding

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val getContent = registerForActivityResult(
        ActivityResultContracts
            .GetContent()
    ) { uriFotoEscolhida: Uri? ->
        if (uriFotoEscolhida != null) {
            configuraImageViewComImagemEscolhida(uriFotoEscolhida)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun configuraImageViewComImagemEscolhida(uriFotoEscolhida: Uri?) {
        val source = ImageDecoder.createSource(this.contentResolver, uriFotoEscolhida!!)
        val bitmap = ImageDecoder.decodeBitmap(source)
        binding.ivFoto.visibility = VISIBLE
        binding.ivFoto.setImageBitmap(bitmap)
        binding.btnEscolherFoto.visibility = INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            cadastrarUsuario()
        }

        binding.btnEscolherFoto.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.ivFoto.setOnClickListener {
            Log.d("ImageView", "clicou na imageview")
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

        if (binding.ivFoto.background == null) {
            binding.ivFoto.setBackgroundResource(R.drawable.std_profile)
        }
        return false
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_image_profile, menu)
    }


}
