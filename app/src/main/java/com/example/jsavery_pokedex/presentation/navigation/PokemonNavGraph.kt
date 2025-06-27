package com.example.jsavery_pokedex.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.presentation.screens.PokemonDetailsScreen
import com.example.jsavery_pokedex.presentation.screens.PokemonListScreen
import com.example.jsavery_pokedex.presentation.viewmodel.DetailsViewModel
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel
import kotlinx.serialization.Serializable

@Serializable
private object PokemonList : NavKey

@Serializable
private data class PokemonDetails(val id: Int) : NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PokemonNavGraph() {
    val backStack = rememberNavBackStack(PokemonList)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    NavDisplay(
        backStack = backStack,
        onBack = { keysToRemove ->
            repeat(keysToRemove) {
                backStack.removeLastOrNull()
            }
        },
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<PokemonList>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        PokemonDetailPlaceholder()
                    },
                ),
            ) {
                val viewModel: MainViewModel = hiltViewModel()
                val uiState by viewModel.pokemonListUiState.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0.dp),
                ) { innerPadding ->
                    PokemonListScreen(
                        uiState = uiState,
                        modifier = Modifier.padding(innerPadding),
                        onLoadMore = { viewModel.onLoadMore(it) },
                        onPokemonClick = { pokemonId -> backStack.add(PokemonDetails(pokemonId)) },
                    )
                }
            }
            entry<PokemonDetails>(
                metadata = ListDetailSceneStrategy.detailPane(),
            ) { pokemonId ->
                val detailsViewModel: DetailsViewModel = hiltViewModel()
                val uiState by detailsViewModel.detailsUiState.collectAsStateWithLifecycle()

                val lifecycleOwner = LocalLifecycleOwner.current
                val currentPokemonId by rememberUpdatedState(pokemonId)

                LaunchedEffect(currentPokemonId, lifecycleOwner) {
                    detailsViewModel.getPokemonDetail(pokemonId.id)
                }

                PokemonDetailsScreen(
                    uiState = uiState,
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    getEvolutionDetail = { detailsViewModel.getPokemonEvolutionDetail(it) },
                )
            }
        },
    )
}

@Composable
private fun PokemonDetailPlaceholder() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.pokeball_progress),
            contentDescription = "Select a Pokemon",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .rotate(90f),
        )
    }
}
