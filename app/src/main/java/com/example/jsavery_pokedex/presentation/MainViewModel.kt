package com.example.jsavery_pokedex.presentation

import androidx.annotation.OpenForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.data.repository.PokemonRepositoryImpl
import com.example.jsavery_pokedex.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonListUiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val pokemonListUiState: StateFlow<PokemonListUiState> = _pokemonListUiState.asStateFlow()

    init {
        fetchPokemon()
    }

    @OpenForTesting
    fun fetchPokemon() {
        viewModelScope.launch {
            repository.getPokemonList().collect {
                when(it) {
                    is Result.Success -> onPokemonSuccess(it.data)
                    is Result.Error -> onPokemonFailure(it.exception)
                }
            }
        }
    }

    private fun onPokemonSuccess(pokemonResponse: PokemonResponse) {
        _pokemonListUiState.value = PokemonListUiState.Success(
            pokemonList = pokemonResponse.data
        )
    }

    private fun onPokemonFailure(error: Throwable) {
        _pokemonListUiState.value = PokemonListUiState.Error(
            throwable = error
        )
    }

}

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(PokemonRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState
    data class Success(val pokemonList: List<Pokemon>) : PokemonListUiState
    data class Error(val throwable: Throwable) : PokemonListUiState
}
