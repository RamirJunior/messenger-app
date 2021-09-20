package com.ramirjr.pigeon.registerlogin

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.databinding.ActivityRegisterBinding
import com.ramirjr.pigeon.messages.LatestMessagesActivity
import com.ramirjr.pigeon.models.User
import com.ramirjr.pigeon.views.LoadingDialogRegister
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    var uriPhotoSelected: Uri? = null
    val loadingDialogRegister: LoadingDialogRegister = LoadingDialogRegister(this)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnUserPhoto.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.imageviewUserPhoto.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.textviewSignHere.setOnClickListener {
            startLoginActivity()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
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

    private fun validateEmailAddress(email: String?): Boolean {
        val emailInput = email
        if (!emailInput.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.txtInputEmail.error = null
            return true
        } else {
            binding.txtInputEmail.error = getString(R.string.invalid_email)
            return false
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun registerUser(): Boolean {
        val email = binding.txtInputEmail.editText?.text.toString()
        val password = binding.txtInputPassword.editText?.text.toString()

        if ((email.isEmpty() || password.isEmpty())) {
            Toast.makeText(this, "Por favor, digite E-mail/Senha.", Toast.LENGTH_SHORT).show()
            return true
        }

        if (validateEmailAddress(email)) {
            registerOnFirebase(email, password)
        }

        return false
    }

    private fun registerOnFirebase(email: String, password: String) {

        loadingDialogRegister.startLoadingDialog()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Register", "Usuario criado com sucesso: ${it.result?.user?.uid}")
                sendImageToFirebase()
            }
            .addOnFailureListener {
                Log.d("Register", "Falha ao criar usuario: ${it.message}")
                Toast.makeText(this, "Falha ao criar usuario: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
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
                    Log.d("Register", "Falha ao enviar a imagem: ${it.message}")
                }
        } else return
    }

    private fun saveUserToFirebase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, binding.txtInputName.editText?.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Registro", "Usuario registrado no Firebase")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}
