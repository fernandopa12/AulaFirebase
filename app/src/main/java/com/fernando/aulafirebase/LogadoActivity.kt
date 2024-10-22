package com.fernando.aulafirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fernando.aulafirebase.databinding.ActivityLogadoBinding
import com.fernando.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogadoActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityLogadoBinding.inflate(layoutInflater)
    }

    private  val autenticacao by lazy {
        FirebaseAuth.getInstance()
    }

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
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

        binding.btnDeslogar.setOnClickListener{
            autenticacao.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.btnSalvar.setOnClickListener{
            salvarInfo()
        }
    }
    private fun salvarInfo(){
        val dados = mapOf(
            "nomeCompleto" to binding.editNomeCompleto.text.toString(),
            "telefone" to binding.editTelefone.text.toString()
        )

        val idUsuarioAtual = autenticacao.currentUser?.uid

        if(idUsuarioAtual!=null){
            bancoDados
                .collection("usuarios")
                .document(idUsuarioAtual)
                .set(dados)
                .addOnSuccessListener {
                    Log.i("db_info","Dados salvo com sucesso")
                }
                .addOnFailureListener{
                    Log.i("db_info","Erro ao salvar dados.")
                }
        }

        //Adicionando na coleção
        //referenciaColecao.add(dados)

    }
}