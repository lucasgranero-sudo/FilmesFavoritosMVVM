# Filmes Favoritos (MVVM)

App Android em Kotlin onde o usuário pode visualizar e editar a lista dos seus filmes favoritos (nome, nota e foto).

## O que foi usado
- **Kotlin**
- **View Binding** para acessar os elementos dos layouts (`ActivityMainBinding`, `ItemFilmeBinding`, `DialogEditarFilmeBinding`)
- **Arquitetura MVVM**
  - **Model:** `model/Filme.kt`
  - **View:** `MainActivity.kt` + layouts XML
  - **ViewModel:** `viewmodel/FilmeViewModel.kt` (lista em `LiveData` e lógica de adicionar, editar, excluir e validar)
- **RecyclerView** com `adapter/FilmeAdapter.kt`
- **Coil** para carregar as fotos pela URL (https://github.com/coil-kt/coil)

## Funcionalidades
- Ver a lista de filmes com foto, nome e nota
- Botão **+** para adicionar um filme
- **Tocar** em um filme para editar nome, nota e link da foto (botão "Ver foto" mostra a prévia)
- **Segurar** em um filme para excluir

## Como rodar
Abrir a pasta no Android Studio, esperar o Gradle sincronizar e rodar no emulador ou celular (precisa de internet para carregar as fotos).
