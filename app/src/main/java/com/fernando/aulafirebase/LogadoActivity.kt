package com.fernando.aulafirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fernando.aulafirebase.databinding.ActivityLogadoBinding
import com.fernando.aulafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogadoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLogadoBinding.inflate(layoutInflater)
    }

    private val autenticacao by lazy {
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

        binding.btnDeslogar.setOnClickListener {
            autenticacao.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnSalvar.setOnClickListener {
            salvarInfo()
        }
        binding.btnAtualizar.setOnClickListener {
            atualizarUsuario()
        }
        binding.btnRemover.setOnClickListener {
            removerUsuario()
        }
        binding.btnListar.setOnClickListener {
            listarDados()
        }
    }

    private fun salvarInfo() {
        val dados = mapOf(
            "nomeCompleto" to binding.editNomeCompleto.text.toString(),
            "telefone" to binding.editTelefone.text.toString()
        )

        val idUsuarioAtual = autenticacao.currentUser?.uid

        if (idUsuarioAtual != null) {
            bancoDados
                .collection("usuarios")
                .document(idUsuarioAtual)
                .set(dados)
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("SUCESSO AO CADASTRAR")
                        .setMessage("Registro com salvo com sucesso.")
                        .setPositiveButton("Fechar") { dialog, posicao -> }
                        .create().show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setTitle("ERROR AO CADASTRAR REGISTRO")
                        .setMessage("Registro não salvo.")
                        .setPositiveButton("Fechar") { dialog, posicao -> }
                        .create().show()
                }
        }

    }

    private fun atualizarUsuario() {

        val idUsuarioLogado = autenticacao.currentUser?.uid

        if (idUsuarioLogado != null) {
            bancoDados
                .collection("usuarios")
                .document(idUsuarioLogado)
                .update("telefone", binding.editTelefone.text.toString())
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("SUCESSO AO ATUALIZAR")
                        .setMessage("Registro atualizar com sucesso.")
                        .setPositiveButton("Fechar") { dialog, posicao -> }
                        .create().show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setTitle("ERROR AO ATUALIZAR")
                        .setMessage("Registro não atualizado.")
                        .setPositiveButton("Fechar") { dialog, posicao -> }
                        .create().show()
                }

        }

    }

    private fun removerUsuario() {
        val idUsuarioLogado = autenticacao.currentUser?.uid

        if (idUsuarioLogado != null) {
            bancoDados
                .collection("usuarios")
                .document(idUsuarioLogado)
                .delete()
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("SUCESSO AO DELETAR")
                        .setMessage("Dados deletados com sucesso.")
                        .setPositiveButton("Fechar") { dialog, posicao -> }
                        .create().show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setTitle("DADOS NÃO DELETADO")
                        .setMessage("Dados não deletados.")
                        .setPositiveButton("Fechar") { dialog, posicao -> }
                        .create().show()
                }
        }
    }

    private fun listarDados() {
        val idUsuarioLogado = autenticacao.currentUser?.uid
        if (idUsuarioLogado != null) {
            bancoDados
                .collection("usuarios")
                .addSnapshotListener { querySnapshot, erro ->
                    val listaDocs = querySnapshot?.documents

                    var listaResultado = ""

                    listaDocs?.forEach { documentSnaphot ->
                        val dados = documentSnaphot.data

                        if (dados != null) {
                            val nomeCompleto = dados["nomeCompleto"]
                            val telefone = dados["telefone"]
                            listaResultado += "Nome:$nomeCompleto - Telefone:$telefone \n"
                        }
                    }

                    binding.textResultado.text = listaResultado
                }
        }
    }
}