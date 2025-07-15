package com.example.jsavery_pokedex.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.jsavery_pokedex.presentation.screens.FilterScreen
import com.example.jsavery_pokedex.presentation.screens.PokemonDetailPlaceholder
import com.example.jsavery_pokedex.presentation.screens.PokemonDetailsScreen
import com.example.jsavery_pokedex.presentation.screens.PokemonListScreen
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private object PokemonList : NavKey

@Serializable
data class PokemonDetails(
    val id: Int,
) : NavKey

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun PokemonNavGraph(viewModel: MainViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val filterManager = viewModel.filterManager

    val backStack = rememberNavBackStack(PokemonList)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    val showFilterSheet by filterManager.showFilterSheet.collectAsState()
    var isSheetVisible by remember { mutableStateOf(false) }

    LaunchedEffect(showFilterSheet) {
        isSheetVisible = showFilterSheet
    }

    NavDisplay(
        backStack = backStack,
        onBack = { keysToRemove ->
            repeat(keysToRemove) {
                backStack.removeLastOrNull()
            }
        },
        transitionSpec = {
            NavigationTransitions.slideInHorizontally
        },
        popTransitionSpec = {
            NavigationTransitions.slideOutHorizontally
        },
        predictivePopTransitionSpec = {
            NavigationTransitions.slideOutHorizontally
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
                PokemonListScreen(
                    backStack = backStack,
                    filterManager = filterManager,
                    viewModel = viewModel,
                )
            }
            entry<PokemonDetails>(
                metadata = ListDetailSceneStrategy.detailPane(),
            ) { pokemonId ->
                PokemonDetailsScreen(pokemonId, backStack)
            }
        },
    )

    if (isSheetVisible) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    filterManager.hideFilterSheet()
                }
            },
        ) {
            FilterScreen(
                filterManager = filterManager,
            )
        }
    }
}
