package com.example.jsavery_pokedex.presentation.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.jsavery_pokedex.presentation.screens.PokemonDetailPlaceholder
import com.example.jsavery_pokedex.presentation.screens.PokemonDetailsScreen
import com.example.jsavery_pokedex.presentation.screens.PokemonListScreen
import kotlinx.serialization.Serializable

@Serializable
private object PokemonList : NavKey

@Serializable
data class PokemonDetails(val id: Int) : NavKey

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
                PokemonListScreen(backStack)
            }
            entry<PokemonDetails>(
                metadata = ListDetailSceneStrategy.detailPane(),
            ) { pokemonId ->
                PokemonDetailsScreen(pokemonId, backStack)
            }
        },
    )
}
