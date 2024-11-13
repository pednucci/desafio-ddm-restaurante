package br.unisanta.firebaseauth

import com.google.firebase.firestore.FirebaseFirestore

class AgendaDao {
    companion object{
        private val pedidos = mutableListOf<Pedido>()
    }

    private val db = FirebaseFirestore.getInstance()

    fun adicionarPedido(pedido:Pedido){
        Companion.pedidos.add(pedido)
    }
    fun obterPedidos():List<Pedido>{
        return Companion.pedidos
    }

    fun sincronizarComFirestore(onComplete: () -> Unit) {
        db.collection("pedidos")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Limpa a lista local antes de adicionar os novos dados
                pedidos.clear()

                for (document in querySnapshot) {
                    val email = document.getString("email") ?: ""
                    val restaurante = document.getString("restaurante") ?: ""
                    val produto = document.getString("produto") ?: ""
                    val endereco = document.getString("endereco") ?: ""
                    val status = document.getString("status") ?: ""
                    val timestamp = document.getString("timestamp") ?: ""
                    val pedido = Pedido(email, restaurante, produto, status, timestamp, endereco)
                    adicionarPedido(pedido)
                }

                // Chama a função de callback para sinalizar que a sincronização terminou
                onComplete()
            }
            .addOnFailureListener { exception ->
                // Trate a falha, exiba um log ou mensagem de erro se necessário
                println("Erro ao sincronizar pedidos: ${exception.message}")
            }
    }
}