package com.example.prova

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object ProdutoRepository {
    val produtos = mutableListOf<Produto>()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutMain()
        }
    }
}

@Composable
fun LayoutMain() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "telaprincipal") {

        composable("telaprincipal") { TelaMain(navController) }

        composable("cadastrar") { CadastrarProdutos(navController) }

        composable("listar"){ ListaProdutos(navController) }

        composable("detalhes/{nome}/{categoria}/{preco}/{quantidade}") {

                backStackEntry ->

            val nome = backStackEntry.arguments?.getString("nome") ?: ""

            val categoria = backStackEntry.arguments?.getString("categorai") ?: ""

            val preco = backStackEntry.arguments?.getString("preco")?.toDouble() ?: 0.0

            val quantidade = backStackEntry.arguments?.getString("quantidade")?.toInt() ?: 0

            DetalhesProduto(nome, categoria, preco, quantidade, navController)
        }
    }
}

@Composable
fun TelaMain(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Tela Inicial", fontSize = 25.sp)

        Spacer(modifier = Modifier.height(25.dp))

        Button(onClick = { navController.navigate("cadastrar") }) {
            Text(text = "Cadastrar Produto")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(onClick = { navController.navigate("listar") }) {
            Text(text = "Ver Lista de Produtos")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastrarProdutos(navController: NavHostController) {

    var nome by remember {
        mutableStateOf("")
    }
    var preco by remember {
        mutableStateOf("")
    }
    var quantidade by remember {
        mutableStateOf("")
    }

    var categoria by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Cadastrar Produtos", fontSize = 25.sp)

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text(text = "Nome do Produto") }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text(text = "Categoria") }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text(text = "Preço") }
        )

        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text(text = "Quantidade") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            when { nome.isBlank() || categoria.isBlank() || preco.isBlank() || quantidade.isBlank() -> {
                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                }
                preco.toDouble() < 0 -> {
                    Toast.makeText(context, "O Preço deve ser um positivo!", Toast.LENGTH_SHORT).show()
                }
                quantidade.toInt() < 0 -> {
                    Toast.makeText(context, "A quantidade não pode ser menor que 0!", Toast.LENGTH_SHORT).show()
                }

                else -> {
                val novoProduto = Produto.criarProduto(nome, categoria, preco.toDouble(), quantidade.toInt())
                ProdutoRepository.produtos.add(novoProduto)

                Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                navController.popBackStack()
                }
            }

        }) {
            Text(text = "Cadastrar")
        }
    }
}

@Composable
fun ListaProdutos(navController: NavHostController){

    val produtos = ProdutoRepository.produtos

    LazyColumn(){
        items(produtos){
            produto ->
            ProdutoItem(produto){
                navController.navigate("detalhes/${produto.nome}" +
                                                    "/${produto.categoria}" +
                                                    "/${produto.preco}" +
                                                    "/${produto.quantidade}")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("telaprincipal") }
        ) {
            Text(text = "Voltar à Tela Inicial")
        }
    }
}

@Composable
fun ProdutoItem(produto: Produto, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(onClick = onClick)
    ) {

        Text(text = "${produto.nome} (${produto.quantidade})",
            fontSize = (20.sp))

        Button(onClick = onClick)
        {
            Text(text = "Detalhes")
        }
    }
}

@Composable
fun DetalhesProduto(nome: String,categoria: String, preco: Double, quantidade: Int, navController: NavHostController) {

    val produtos = ProdutoRepository.produtos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = "Nome: $nome", fontSize = 24.sp)
        Text(text = "Categoria: $categoria", fontSize = 20.sp)
        Text(text = "Preço: R$$preco", fontSize = 20.sp)
        Text(text = "Quantidade em Estoque: $quantidade", fontSize = 20.sp)

        Button(
            onClick = { navController.navigate("listar") }
        ) {
            Text(text = "Lista de Produtos")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { navController.navigate("telaprincipal") }
        ) {
            Text(text = "Voltar a Tela Inicial")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    LayoutMain()
}
