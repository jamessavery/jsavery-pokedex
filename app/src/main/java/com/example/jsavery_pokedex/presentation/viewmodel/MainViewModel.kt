package com.example.jsavery_pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.domain.PokemonListManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val repository: PokemonRepository,
    private val pokemonListManager: PokemonListManager,
) : ViewModel() {
    private val _pokemonListUiState =
        MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val pokemonListUiState: StateFlow<PokemonListUiState> = _pokemonListUiState.asStateFlow()

    init {
        fetchPokemon()
    }

    private fun fetchPokemon() {
        viewModelScope.launch {
            repository.getPokemonList(FIRST_PAGE)
                .onSuccess {
                    onPokemonListSuccess(it)
                }.onFailure {
                    onPokemonListFailure(it)
                }
        }
    }

    private fun onPokemonListSuccess(pokemonResponse: PokemonResponse) {
        pokemonListManager.updatePokemonList(pokemonResponse.data)

        _pokemonListUiState.value =
            PokemonListUiState.Success(
                pokemonList = processPokemonList(pokemonResponse.data),
                nextPage = pokemonResponse.next ?: FIRST_PAGE,
                isLoadingMore = false,
            )
    }

    private fun processPokemonList(data: List<Pokemon>): List<Pokemon> {
        val currentState = _pokemonListUiState.value
        val pokemonList = (currentState as? PokemonListUiState.Success)?.pokemonList

        return if (pokemonList.isNullOrEmpty()) {
            data
        } else {
            pokemonList.plus(data)
        }
    }

    private fun onPokemonListFailure(error: Throwable) {
        _pokemonListUiState.value = PokemonListUiState.Error(throwable = error)
    }

    fun onLoadMore(nextPage: Int) {
        val currentState = _pokemonListUiState.value
        if (currentState !is PokemonListUiState.Success || currentState.isLoadingMore) {
            return // Don't load more if currently already loading
        }

        viewModelScope.launch {
            _pokemonListUiState.update {
                (it as PokemonListUiState.Success).copy(isLoadingMore = true)
            }

            repository.getPokemonList(nextPage)
                .onSuccess {
                    onPokemonListSuccess(it)
                }
                .onFailure {
                    onPokemonListFailure(it)
                }
        }
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}

sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState

    data class Success(
        val pokemonList: List<Pokemon> = emptyList(),
        val nextPage: Int = 1,
        val isLoadingMore: Boolean = false,
    ) : PokemonListUiState

    data class Error(
        val throwable: Throwable,
    ) : PokemonListUiState
}
