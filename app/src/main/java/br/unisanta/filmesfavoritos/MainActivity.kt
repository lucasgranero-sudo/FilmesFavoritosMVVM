package br.unisanta.filmesfavoritos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.unisanta.filmesfavoritos.adapter.FilmeAdapter
import br.unisanta.filmesfavoritos.databinding.ActivityMainBinding
import br.unisanta.filmesfavoritos.databinding.DialogEditarFilmeBinding
import br.unisanta.filmesfavoritos.model.Filme
import br.unisanta.filmesfavoritos.viewmodel.FilmeViewModel
import coil3.load
import coil3.request.error
import coil3.request.placeholder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FilmeViewModel by viewModels()
    private lateinit var adapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = FilmeAdapter(
            aoClicar = { filme -> abrirDialog(filme) },
            aoSegurar = { filme -> confirmarExclusao(filme) }
        )
        binding.rvFilmes.layoutManager = LinearLayoutManager(this)
        binding.rvFilmes.adapter = adapter

        // observa a lista do ViewModel, quando mudar atualiza a tela
        viewModel.filmes.observe(this) { lista ->
            adapter.atualizarLista(lista)
            binding.txtTotal.text = "Total de filmes: " + lista.size
        }

        binding.fabAdicionar.setOnClickListener {
            abrirDialog(null)
        }
    }

    // se filme for null adiciona um novo, se nao edita o filme
    private fun abrirDialog(filme: Filme?) {
        val dialogBinding = DialogEditarFilmeBinding.inflate(layoutInflater)

        if (filme != null) {
            dialogBinding.edtNome.setText(filme.nome)
            dialogBinding.edtNota.setText(filme.nota.toString())
            dialogBinding.edtFoto.setText(filme.fotoUrl)
            carregarPreview(dialogBinding, filme.fotoUrl)
        }

        dialogBinding.btnPreview.setOnClickListener {
            carregarPreview(dialogBinding, dialogBinding.edtFoto.text.toString())
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (filme == null) "Adicionar filme" else "Editar filme")
            .setView(dialogBinding.root)
            .setPositiveButton("Salvar", null)
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val nome = dialogBinding.edtNome.text.toString().trim()
                val notaTexto = dialogBinding.edtNota.text.toString().trim()
                val foto = dialogBinding.edtFoto.text.toString().trim()

                val erro = viewModel.validar(nome, notaTexto)
                if (erro != null) {
                    Toast.makeText(this, erro, Toast.LENGTH_SHORT).show()
                } else {
                    val nota = notaTexto.replace(",", ".").toFloat()
                    if (filme == null) {
                        viewModel.adicionarFilme(nome, nota, foto)
                        Toast.makeText(this, "Filme adicionado", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.editarFilme(filme.id, nome, nota, foto)
                        Toast.makeText(this, "Filme atualizado", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun carregarPreview(dialogBinding: DialogEditarFilmeBinding, url: String) {
        dialogBinding.imgPreview.load(url) {
            placeholder(R.drawable.ic_filme)
            error(R.drawable.ic_filme)
        }
    }

    private fun confirmarExclusao(filme: Filme) {
        AlertDialog.Builder(this)
            .setTitle("Excluir filme")
            .setMessage("Deseja excluir " + filme.nome + "?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.excluirFilme(filme.id)
                Toast.makeText(this, "Filme excluído", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Não", null)
            .show()
    }
}
