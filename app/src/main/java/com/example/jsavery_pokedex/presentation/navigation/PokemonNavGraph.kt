package com.example.jsavery_pokedex.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jsavery_pokedex.presentation.navigation.NavRoutes.POKEMON_DETAILS
import com.example.jsavery_pokedex.presentation.navigation.NavRoutes.POKEMON_DETAILS_ARG
import com.example.jsavery_pokedex.presentation.navigation.NavRoutes.POKEMON_LIST
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel
import com.example.jsavery_pokedex.presentation.screens.PokemonDetailsScreen
import com.example.jsavery_pokedex.presentation.screens.PokemonListScreen
import com.example.jsavery_pokedex.presentation.viewmodel.DetailsViewModel

object NavRoutes {
    const val POKEMON_LIST = "pokemon_list"
    const val POKEMON_DETAILS = "pokemon_details"
    const val POKEMON_DETAILS_ARG = "pokemonId"
}

@Composable
fun PokemonNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = POKEMON_LIST
    ) {
        composable(POKEMON_LIST) {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.pokemonListUiState.collectAsState()

            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                PokemonListScreen(
                    uiState = uiState,
                    modifier = Modifier.padding(innerPadding),
                    onLoadMore = { viewModel.onLoadMore(it) },
                    onPokemonClick = { pokemonId ->
                        navController.navigate("$POKEMON_DETAILS$pokemonId")
                    }
                )
            }
        }

        composable(
            route = "$POKEMON_DETAILS{$POKEMON_DETAILS_ARG}",
            arguments = listOf(
                navArgument(POKEMON_DETAILS_ARG) { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val pokemonId = backStackEntry.arguments?.getInt(POKEMON_DETAILS_ARG) ?: return@composable
            val detailsViewModel: DetailsViewModel = hiltViewModel()
            val uiState by detailsViewModel.detailsUiState.collectAsStateWithLifecycle()

            LaunchedEffect(pokemonId) {
                detailsViewModel.getPokemonDetail(pokemonId)
            }

            PokemonDetailsScreen(
                uiState,
                onBackClick = { navController.popBackStack() },
                getEvolutionDetail = { detailsViewModel.getPokemonEvolutionDetail(it) }
            )
        }
    }
}