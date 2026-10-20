package br.unisanta.filmesfavoritos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.unisanta.filmesfavoritos.R
import br.unisanta.filmesfavoritos.databinding.ItemFilmeBinding
import br.unisanta.filmesfavoritos.model.Filme
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder

class FilmeAdapter(
    private val aoClicar: (Filme) -> Unit,
    private val aoSegurar: (Filme) -> Unit
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    private var lista: List<Filme> = emptyList()

    fun atualizarLista(novaLista: List<Filme>) {
        lista = novaLista
        notifyDataSetChanged()
    }

    class FilmeViewHolder(val binding: ItemFilmeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val binding = ItemFilmeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        val filme = lista[position]
        holder.binding.txtNome.text = filme.nome
        holder.binding.txtNota.text = "Nota: " + filme.nota
        holder.binding.ratingNota.rating = filme.nota / 2

        // carrega a foto com o Coil
        holder.binding.imgFoto.load(filme.fotoUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_filme)
            error(R.drawable.ic_filme)
        }

        holder.binding.root.setOnClickListener { aoClicar(filme) }
        holder.binding.root.setOnLongClickListener {
            aoSegurar(filme)
            true
        }
    }

    override fun getItemCount(): Int = lista.size
}
