package br.unisanta.firebaseauth

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unisanta.firebaseauth.databinding.AgendarActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import com.google.firebase.firestore.FieldValue
import java.text.SimpleDateFormat

class CadastroAgendar : AppCompatActivity() {
    private lateinit var binding: AgendarActivityBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        binding = AgendarActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fbBack.setOnClickListener {
            finish()
        }

        binding.btnConfirmarAgendamento.setOnClickListener {
            if (binding.edtNomeProduto.text.length != 0 && binding.edtNomeRestaurante.text.length != 0 && binding.edtEndereco.text.length != 0) {
                val agendamento = "Pedido enviado com sucesso!"
                Toast.makeText(this, agendamento, Toast.LENGTH_LONG).show()
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    AddPedido(currentUser.email.toString(), binding.edtNomeRestaurante.text.toString(), binding.edtNomeProduto.text.toString(), binding.edtEndereco.text.toString())
                }
                else {
                    Toast.makeText(this, "Você não está logado!", Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(this, "Por favor, escolha o produto e o restaurante.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun AddPedido(email :String, restaurante: String, produto: String, endereco: String) {
        val dataHoraAtual = Date()
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dataHoraString = formato.format(dataHoraAtual)

        val agenda = hashMapOf(
            "email" to email,
            "restaurante" to restaurante,
            "produto" to produto,
            "endereco" to endereco,
            "status" to "pendente",
            "timestamp" to dataHoraString
        )

        db.collection("pedidos").document("$email-$dataHoraString-$produto")
            .set(agenda).addOnSuccessListener {
                Log.d("Firestore", "Pedido adicionado com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Erro ao adicionar pedido", e)
            }
    }
}