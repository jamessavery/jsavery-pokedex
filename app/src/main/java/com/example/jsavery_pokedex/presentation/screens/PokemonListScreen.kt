package com.example.jsavery_pokedex.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.domain.manager.PokemonFilterManager
import com.example.jsavery_pokedex.domain.util.PokemonFilterUtils.applyPokemonFilter
import com.example.jsavery_pokedex.mock.MockData.Companion.MOCK_POKEMON_RESPONSE
import com.example.jsavery_pokedex.presentation.navigation.PokemonDetails
import com.example.jsavery_pokedex.presentation.ui.components.PokedexSearchBar
import com.example.jsavery_pokedex.presentation.ui.components.PokemonItem
import com.example.jsavery_pokedex.presentation.ui.components.progress.SpinningPokeballProgress
import com.example.jsavery_pokedex.presentation.ui.dismissKeyboardOnTouch
import com.example.jsavery_pokedex.presentation.ui.theme.PokedexTheme
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel
import com.example.jsavery_pokedex.presentation.viewmodel.PokemonListUiState
import kotlinx.coroutines.flow.distinctUntilChanged

const val PAGINATE_SCROLL_RATIO = 0.6 // User scrolls 60% of page before triggering next load

@Composable
fun PokemonListScreen(
    backStack: NavBackStack,
    filterManager: PokemonFilterManager,
    viewModel: MainViewModel,
) {
    val uiState by viewModel.pokemonListUiState.collectAsState()
    val selectedTypes = filterManager.filterState.collectAsState().value.selectedTypes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->
        PokemonListScreenContent(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding),
            onLoadMore = { viewModel.onLoadMore(it, selectedTypes) },
            onPokemonClick = { pokemonId -> backStack.add(PokemonDetails(pokemonId)) },
            filterManager = filterManager,
        )
    }
}

@Composable
fun PokemonListScreenContent(
    uiState: PokemonListUiState,
    modifier: Modifier = Modifier,
    onLoadMore: (Int?) -> Unit,
    onPokemonClick: (Int) -> Unit,
    filterManager: PokemonFilterManager,
) {
    when (uiState) {
        is PokemonListUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                SpinningPokeballProgress()
            }
        }

        is PokemonListUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .dismissKeyboardOnTouch(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.generic_error_message),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
        }

        is PokemonListUiState.Success -> {
            PokemonListSuccess(
                uiState = uiState,
                modifier = modifier,
                onLoadMore = onLoadMore,
                onPokemonClick = onPokemonClick,
                filterManager = filterManager,
            )
        }
    }
}

@Composable
fun PokemonListSuccess(
    uiState: PokemonListUiState.Success,
    modifier: Modifier,
    onLoadMore: (Int?) -> Unit,
    onPokemonClick: (Int) -> Unit,
    filterManager: PokemonFilterManager,
) {
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val currentFilter by filterManager.filterState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        PokedexSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            modifier = modifier,
            filterManager = filterManager,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            val filteredPokemonList =
                remember(uiState.pokemonList, searchQuery, currentFilter) {
                    applyPokemonFilter(uiState.pokemonList, searchQuery, currentFilter)
                }

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(25.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .dismissKeyboardOnTouch(),
            ) {
                items(filteredPokemonList) { pokemon ->
                    PokemonItem(pokemon = pokemon, onPokemonClick = onPokemonClick)
                }
            }

            // Pagination LaunchedEffect
            LaunchedEffect(listState, uiState.nextPage) {
                snapshotFlow { listState.layoutInfo }
                    .distinctUntilChanged { old, new ->
                        old.visibleItemsInfo.lastOrNull()?.index ==
                            new.visibleItemsInfo.lastOrNull()?.index
                    }.collect { layoutInfo ->
                        val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        val totalItems = layoutInfo.totalItemsCount

                        if (lastVisibleIndex != null && totalItems > 0) {
                            val threshold = (totalItems * PAGINATE_SCROLL_RATIO).toInt()
                            if (lastVisibleIndex >= threshold) {
                                onLoadMore(uiState.nextPage)
                            }
                        }
                    }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonPreview() {
    PokedexTheme {
        PokemonListScreenContent(
            uiState = PokemonListUiState.Success(
                MOCK_POKEMON_RESPONSE,
                null,
                isLoadingMore = false,
            ),
            onLoadMore = {},
            onPokemonClick = {},
            filterManager = PokemonFilterManager(),
        )
    }
}
