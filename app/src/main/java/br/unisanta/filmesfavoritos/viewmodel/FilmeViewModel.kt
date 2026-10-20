package br.unisanta.filmesfavoritos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.unisanta.filmesfavoritos.model.Filme

// ViewModel: guarda a lista de filmes e faz a logica de adicionar, editar e excluir
class FilmeViewModel : ViewModel() {

    private val _filmes = MutableLiveData<List<Filme>>()
    val filmes: LiveData<List<Filme>> = _filmes

    private var proximoId = 1

    init {
        // filmes iniciais da lista
        val lista = mutableListOf(
            Filme(proximoId++, "Interestelar", 9.5f, "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"),
            Filme(proximoId++, "O Poderoso Chefão", 9.2f, "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg"),
            Filme(proximoId++, "Matrix", 8.7f, "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg")
        )
        _filmes.value = lista
    }

    fun adicionarFilme(nome: String, nota: Float, fotoUrl: String) {
        val lista = _filmes.value.orEmpty().toMutableList()
        lista.add(Filme(proximoId++, nome, nota, fotoUrl))
        _filmes.value = lista
    }

    fun editarFilme(id: Int, nome: String, nota: Float, fotoUrl: String) {
        val lista = _filmes.value.orEmpty().map { filme ->
            if (filme.id == id) {
                filme.copy(nome = nome, nota = nota, fotoUrl = fotoUrl)
            } else {
                filme
            }
        }
        _filmes.value = lista
    }

    fun excluirFilme(id: Int) {
        val lista = _filmes.value.orEmpty().filter { it.id != id }
        _filmes.value = lista
    }

    // valida os dados antes de salvar, retorna a mensagem de erro ou null se estiver ok
    fun validar(nome: String, notaTexto: String): String? {
        if (nome.isBlank()) {
            return "Digite o nome do filme"
        }
        val nota = notaTexto.replace(",", ".").toFloatOrNull()
        if (nota == null || nota < 0 || nota > 10) {
            return "A nota deve ser de 0 a 10"
        }
        return null
    }
}
