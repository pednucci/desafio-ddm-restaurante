package br.unisanta.firebaseauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaAgendaActivity : AppCompatActivity() {
    val dao = AgendaDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda_lista)
        val rvUsuarios = findViewById<RecyclerView>(R.id.recyclerViewAgendas)
        dao.sincronizarComFirestore {
            val agendas = dao.obterPedidos()
            rvUsuarios.layoutManager = LinearLayoutManager(this)
            rvUsuarios.adapter = PedidoAdapter(agendas)
        }
    }
}