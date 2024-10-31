package com.example.jsavery_pokedex.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.presentation.PokemonListUiState
import com.example.jsavery_pokedex.presentation.ui.components.PokemonItem
import com.example.jsavery_pokedex.presentation.ui.progress.SpinningPokeballProgress
import kotlinx.coroutines.flow.distinctUntilChanged

const val PAGINATE_SCROLL_RATIO = 0.6 // User scrolls 60% of page before triggering next load

@Composable
fun PokemonScreen(
    uiState: PokemonListUiState,
    modifier: Modifier = Modifier,
    onLoadMore: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true
        )

        when (uiState) {
            is PokemonListUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SpinningPokeballProgress()
                }
            }

            is PokemonListUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.generic_error_message),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is PokemonListUiState.Success -> {
                val filteredList = uiState.pokemonList.filter { pokemon ->
                    pokemon.name.contains(searchQuery, ignoreCase = true)
                }
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    items(filteredList) { pokemon ->
                        PokemonItem(pokemon)
                    }
                }

                // Pagination
                LaunchedEffect(listState, uiState.nextPage) {
                    snapshotFlow { listState.layoutInfo } // Snapshot of visible items & their positions
                        .distinctUntilChanged { old, new -> // Filters out consecutive duplicates
                            old.visibleItemsInfo.lastOrNull()?.index == new.visibleItemsInfo.lastOrNull()?.index
                        }
                        .collect { layoutInfo ->
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
}