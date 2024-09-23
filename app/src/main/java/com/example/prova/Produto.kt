package com.example.prova

data class Produto(

    val nome: String,
    val categoria: String,
    val preco: Double,
    val quantidade: Int
) {

    companion object {
        fun criarProduto(nome: String, categoria: String, preco: Double, quantidade: Int): Produto {
            return Produto(nome, categoria, preco, quantidade)
        }
    }
}