package com.example.jsavery_pokedex.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.domain.PokemonListManager
import com.example.jsavery_pokedex.domain.usecase.EvolutionDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val pokemonListManager: PokemonListManager,
    private val pokemonRepository: PokemonRepository,
    private val evolutionDetails: EvolutionDetails,
) : ViewModel() {

    private val _detailsUiState = MutableStateFlow(PokemonDetailsUiState())
    val detailsUiState = _detailsUiState.asStateFlow()

    fun getPokemonDetail(id: Int) {
        viewModelScope.launch {
            pokemonListManager.getPokemonById(id)?.let { localPokemon ->
                onPokemonDetailSuccess(localPokemon)
            } ?: fetchPokemonDetails(id)
        }
    }

    private fun fetchPokemonDetails(id: Int) {
        viewModelScope.launch {
            pokemonRepository.getPokemonDetail(id)
                .onSuccess {
                    onPokemonDetailSuccess(it)
                }.onFailure {
                    onPokemonDetailFailure(it)
                }
        }
    }

    private fun onPokemonDetailSuccess(pokemon: Pokemon) {
        pokemonListManager.updatePokemonList(listOf(pokemon))

        _detailsUiState.update { currentState ->
            currentState.copy(isLoading = false, pokemon = pokemon, error = null)
        }
    }

    private fun onPokemonDetailFailure(error: Throwable) {
        _detailsUiState.update { currentState ->
            currentState.copy(isLoading = false, pokemon = null, error = error.toString())
        }
    }

    fun getPokemonEvolutionDetails(id: Int) =
        evolutionDetails(id)
}

data class PokemonDetailsUiState(
    val isLoading: Boolean = true,
    val pokemon: Pokemon? = null,
    val error: String? = null,
)
