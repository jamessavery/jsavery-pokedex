package com.example.jsavery_pokedex.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jsavery_pokedex.mock.MockData.Companion.MOCK_POKEMON_RESPONSE
import com.example.jsavery_pokedex.presentation.screens.PokemonScreen
import com.example.jsavery_pokedex.presentation.ui.theme.PokedexTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexApp(viewModel)
        }
    }
}

@Composable
fun PokedexApp(viewModel: MainViewModel) {
    val uiState by viewModel.pokemonListUiState.collectAsState()

    PokedexTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            PokemonScreen(
                uiState = uiState, modifier = Modifier.padding(innerPadding)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PokemonPreview() {
    PokedexTheme {
        PokemonScreen(uiState = PokemonListUiState.Success(MOCK_POKEMON_RESPONSE))
    }
}