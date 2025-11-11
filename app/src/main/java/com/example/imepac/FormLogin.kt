package com.example.imepac

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FormLogin : AppCompatActivity() {

    private lateinit var edit_email: EditText
    private lateinit var edit_senha: EditText
    private lateinit var bt_entrar: Button
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)

        edit_email = findViewById(R.id.edit_email_login)
        edit_senha = findViewById(R.id.edit_senha)
        bt_entrar = findViewById(R.id.bt_entrada)
        progressbar = findViewById(R.id.progressbar)


        bt_entrar.setOnClickListener { view ->
            val email = edit_email.text.toString().trim()
            val senha = edit_senha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.show()
            } else {
                autenticarUsuario(view)
            }
        }

        val linkFormCadastro = findViewById<TextView>(R.id.text_tela_cadastro)
        linkFormCadastro.setOnClickListener {
            val telaCadastro = Intent(this, FormCadastro::class.java)
            startActivity(telaCadastro)
        }
    }

    private fun autenticarUsuario(view: View) {
        val email = edit_email.text.toString().trim()
        val senha = edit_senha.text.toString().trim()

        progressbar.visibility = View.VISIBLE
        bt_entrar.isEnabled = false

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                progressbar.visibility = View.INVISIBLE
                bt_entrar.isEnabled = true
                if (task.isSuccessful) {
                    val intent = Intent(this, Tela_Perfil::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val snackbar = Snackbar.make(view, "Erro ao fazer login. Verifique seus dados.", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
    }
}
