package br.unisanta.firebaseauth

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.unisanta.firebaseauth.Pedido
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class PedidoAdapter(private val pedidos:List<Pedido>):
    RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txvEmail:TextView = itemView.findViewById(R.id.txtEmailClienteItem)
        val txvRestaurante:TextView = itemView.findViewById(R.id.txtRestauranteItem)
        val txvProduto:TextView = itemView.findViewById(R.id.txtProdutoItem)
        val txvEndereco:TextView = itemView.findViewById(R.id.txtEnderecoClienteItem)
        val txvTimestamp:TextView = itemView.findViewById(R.id.txtTimestampItem)
        val txvStatus:TextView = itemView.findViewById(R.id.txtStatusItem)

        val buttonEmPreparo: Button = itemView.findViewById(R.id.btnEmPreparo)
        val buttonProntoEntrega: Button = itemView.findViewById(R.id.btnProntoEntrega)
        val buttonEntregue: Button = itemView.findViewById(R.id.btnEntregue)
        val buttonPendente: Button = itemView.findViewById(R.id.btnPendente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agenda,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.txvEmail.text = pedido.email
        holder.txvRestaurante.text = pedido.restaurante
        holder.txvProduto.text = pedido.pedido
        holder.txvTimestamp.text = pedido.timestamp
        holder.txvEndereco.text = pedido.endereco
        holder.txvStatus.text = pedido.status

        holder.buttonEmPreparo.setOnClickListener{
            val id = "${pedido.email}-${pedido.timestamp}-${pedido.pedido}"
            holder.txvStatus.text = "em preparo"
            Toast.makeText(holder.itemView.context, "Pedido está agora em preparo!", Toast.LENGTH_SHORT).show()
            editarStatusPedido(id, "em preparo")
        }
        holder.buttonProntoEntrega.setOnClickListener{
            val id = "${pedido.email}-${pedido.timestamp}-${pedido.pedido}"
            holder.txvStatus.text = "pronto para entrega"
            Toast.makeText(holder.itemView.context, "Pedido está agora está pronto para entrega!", Toast.LENGTH_SHORT).show()
            editarStatusPedido(id, "pronto para entrega")
        }
        holder.buttonEntregue.setOnClickListener{
            val id = "${pedido.email}-${pedido.timestamp}-${pedido.pedido}"
            holder.txvStatus.text = "entregue"
            Toast.makeText(holder.itemView.context, "Pedido está agora está entregue!", Toast.LENGTH_SHORT).show()
            editarStatusPedido(id, "entregue")
        }
        holder.buttonPendente.setOnClickListener{
            val id = "${pedido.email}-${pedido.timestamp}-${pedido.pedido}"
            holder.txvStatus.text = "pendente"
            Toast.makeText(holder.itemView.context, "Pedido está agora está pendente!", Toast.LENGTH_SHORT).show()
            editarStatusPedido(id, "pendente")
        }
    }
    override fun getItemCount(): Int {
        return pedidos.size
    }

    fun editarStatusPedido(pedidoId: String, novoStatus: String) {
        // Obter a instância do Firestore
        val db = FirebaseFirestore.getInstance()

        // Referência para o documento específico do pedido
        val pedidoRef: DocumentReference = db.collection("pedidos").document(pedidoId)

        // Obter o documento
        pedidoRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Se o documento existir, editar o campo 'status'
                    pedidoRef.update("status", novoStatus)
                        .addOnSuccessListener {
                            // Ação de sucesso

                        }
                        .addOnFailureListener { e ->
                            // Caso ocorra algum erro ao salvar

                        }
                } else {
                    // Se o documento não existir

                }
            }
            .addOnFailureListener { e ->
                // Caso haja erro ao buscar o documento
            }
    }



}