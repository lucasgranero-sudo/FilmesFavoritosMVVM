package br.unisanta.filmesfavoritos.model

// Classe que representa um filme da lista
data class Filme(
    val id: Int,
    var nome: String,
    var nota: Float,
    var fotoUrl: String
)
