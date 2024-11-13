package br.unisanta.firebaseauth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unisanta.firebaseauth.databinding.ActivityMain2Binding
import br.unisanta.firebaseauth.databinding.ActivityMain3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        var email = ""

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            binding.txtNome.text = "Olá, " + currentUser.email.toString() + "!"
            email = currentUser.email.toString()
        }
        else {
            finish()
        }

        db.collection("permissoes").document(email).get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Documento encontrado, você pode acessar os dados aqui
                val permission = documentSnapshot.getString("permission").toString() // Exemplo de campo 'permission'
                if (permission == "cliente") {
                    binding.btnAgendar.text = "Fazer pedido"
                    binding.btnAgendar.setOnClickListener {
                        val intent = Intent(this, CadastroAgendar::class.java)
                        startActivity(intent)
                    }
                }
                else if(permission == "restaurante") {
                    binding.btnAgendar.text = "Ver pedidos"
                    binding.btnAgendar.setOnClickListener {
                        val intent = Intent(this, ListaAgendaActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                // Documento não encontrado
                Toast.makeText(this, "Documento não encontrado", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { exception ->
            // Em caso de erro
            Toast.makeText(this, "Erro ao capturar o documento: ${exception.message}", Toast.LENGTH_SHORT).show()
        }

        binding.btnExclusao.setOnClickListener {
            val user = Firebase.auth.currentUser!!

            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
        }
    }
}