package com.fernando.aulafirebase

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fernando.aulafirebase.databinding.ActivityCadastroBinding
import com.fernando.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class CadastroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }
    private  val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCadastrar.setOnClickListener {
            cadastrarUsuario()
        }
    }

    private fun cadastrarUsuario() {
        val email = binding.editEmailCadastro.text.toString()
        val senha = binding.editSenhaCadastro.text.toString()


        //Passar os paramentros para criação do usuario $email e $senha
        autenticacao.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { authResult ->
                AlertDialog.Builder(this)
                    .setTitle("SUCESSO")
                    .setMessage("Sucesso ao criar conta.")
                    .setPositiveButton("OK"){dialog,posicao->
                        startActivity(Intent(this,MainActivity::class.java))
                    }
                    .setCancelable(false).create().show()

            }.addOnFailureListener { exception ->
                val mensagemErro = exception.message
                AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("Error ao criar conta")
                    .setPositiveButton("Fechar"){dialog,posicao->}
                    .create().show()

            }
    }
}