package com.example.imepac

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FormCadastro : AppCompatActivity() {


    private lateinit var edit_nome: EditText
    private lateinit var edit_email: EditText
    private lateinit var edit_senha: EditText
    private lateinit var btnCadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cadastro)
        //No comando abaixo mando esconder o Toolbar
        getSupportActionBar()?.hide();

        // o que estiver no elemento será enviado para a variável
        edit_nome = findViewById(R.id.edit_nome)
        edit_email = findViewById(R.id.edit_email)
        edit_senha = findViewById(R.id.edit_senha)
        btnCadastrar = findViewById(R.id.bt_cadastrar)

        btnCadastrar.setOnClickListener {
            val nome = edit_nome.text.toString().trim()
            val email = edit_email.text.toString().trim()
            val senha = edit_senha.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                val mensagemErro = "Campos não preenchidos, tente novamente"
                val snackbar = Snackbar.make(it, mensagemErro, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                cadastrarUsuario(it);
            }
        }
    }

    fun cadastrarUsuario(it: View){

        val email = edit_email.text.toString().trim()
        val senha = edit_senha.text.toString().trim()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener{task-> //task é o objeto do resultado vindo o firebase
                if (task.isSuccessful){
                    salvarDadosUsuario() //metodo para salvar o cadastro
                    val mensagemOk = "Cadastro realizado com sucesso"
                    val snackbar = Snackbar.make(it, mensagemOk, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }else{
                    val mensagemErro = "Erro ao cadastrar usuário"
                    val snackbar = Snackbar.make(it, mensagemErro, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
    }
    fun salvarDadosUsuario(){
        val db = FirebaseFirestore.getInstance()
        val nome = edit_nome.text.toString().trim()
        val usuarioID = FirebaseAuth.getInstance().currentUser?.uid
        val email = FirebaseAuth.getInstance().currentUser?.email
        if (usuarioID != null && email != null) {
            val usuarios = hashMapOf(
                "nome" to nome,
                "email" to email,
                "uid" to usuarioID
            )

            db.collection("Usuarios")
                .add(usuarios)
                .addOnSuccessListener { documentReference -> // Add com sucesso
                    println("Documento adicionado com ID: ${documentReference.id}")
                }
                .addOnFailureListener { e -> // Ocorreu um erro ao adicionar
                    println("Erro ao adicionar documento: $e")
                }
        } else { // O usuário não está autenticado
            println("Erro na autenticação")
        }
    }

    }
