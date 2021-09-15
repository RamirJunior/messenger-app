package com.ramirjr.pigeon.registerlogin

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.ramirjr.pigeon.databinding.ActivityRegisterBinding
import com.ramirjr.pigeon.messages.LatestMessagesActivity
import com.ramirjr.pigeon.models.User
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    var uriPhotoSelected: Uri? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.btnUserPhoto.setOnClickListener {
            Log.d("botao", "clicou no botao")
            getContent.launch("image/*")
        }

        binding.imageviewUserPhoto.setOnClickListener {
            Log.d("ImageView", "clicou na imageview")
            getContent.launch("image/*")
        }

        binding.textviewSignHere.setOnClickListener {
            startLoginActivity()
        }
    }

    // configurando imagem recebida no imageview
    @RequiresApi(Build.VERSION_CODES.P)
    private val getContent = registerForActivityResult(
        ActivityResultContracts
            .GetContent()
    ) { photoSelected: Uri? ->
        if (photoSelected != null) {
            uriPhotoSelected = photoSelected
            val source = ImageDecoder.createSource(this.contentResolver, photoSelected)
            val bitmapImage = ImageDecoder.decodeBitmap(source)
            binding.imageviewUserPhoto.visibility = VISIBLE
            binding.imageviewUserPhoto.setImageBitmap(bitmapImage)
            binding.btnUserPhoto.visibility = INVISIBLE
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerUser(): Boolean {
        var email = binding.txtInputEmail.editText?.text.toString()
        var password = binding.txtInputPassword.editText?.text.toString()

        if ((email.isEmpty() || password.isEmpty())) {
            Toast.makeText(this, "Por favor, digite E-mail/Senha.", Toast.LENGTH_SHORT).show()
            return true
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Cadastro", "Usuario criado com sucesso: ${it.result?.user?.uid}")

                sendImageToFirebase()
            }
            .addOnFailureListener {
                Log.d("Main", "Falha ao criar usuario: ${it.message}")
                Toast.makeText(this, "Falha ao criar usuario: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        return false
    }

    private fun sendImageToFirebase() {
        if (uriPhotoSelected != null) {
            val fileName = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

            ref.putFile(uriPhotoSelected!!)
                .addOnSuccessListener {
                    Log.d("Cadastro", "Imagem enviada com sucesso: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("Registro", "local do arquivo: $it")
                        saveUserToFirebase(it.toString())
                    }
                }
                .addOnFailureListener {
                    //TODO do some logging here
                }
        } else return
    }

    private fun saveUserToFirebase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, binding.txtInputName.editText?.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Registro", "usuario salvo no BD do Firebase")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}
