import com.example.prova.Produto

class Estoque {
    companion object {
        private val listaProdutos = mutableListOf<Produto>()

        fun adicionarProduto(produto: Produto) {
            listaProdutos.add(produto)
        }

        fun getListaProdutos(): List<Produto> = listaProdutos

        fun calcularValorTotalEstoque(): Double {
            return listaProdutos.sumOf { it.preco * it.quantidade }
        }
    }
}
