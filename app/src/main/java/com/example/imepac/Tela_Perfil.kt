package com.example.imepac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Tela_Perfil : AppCompatActivity() {

    private lateinit var textNomeUser: TextView
    private lateinit var textEmailUser: TextView
    private lateinit var bt_sair: Button

    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_perfil)

        textNomeUser = findViewById(R.id.textNomeUser)
        textEmailUser = findViewById(R.id.textEmailUser)
        bt_sair = findViewById(R.id.bt_sair) // Certifique-se de que este ID está no seu XML

        bt_sair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val email = user?.email
        val usuarioID = user?.uid

        if (email != null && usuarioID != null) {
            db.collection("Usuarios").whereEqualTo("uid", usuarioID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        textNomeUser.text = document.getString("nome")
                        textEmailUser.text = email
                    }
                }
        }
    }
}
