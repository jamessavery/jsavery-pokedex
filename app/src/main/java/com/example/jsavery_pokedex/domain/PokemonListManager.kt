package com.example.jsavery_pokedex.domain

import com.example.jsavery_pokedex.data.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonListManagerImpl @Inject constructor() : PokemonListManager {

    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    override val pokemonList = _pokemonList.asStateFlow()

    override fun updatePokemonList(newList: List<Pokemon>) {
        val currentList = _pokemonList.value.toMutableList()
        newList.forEach { pokemon ->
            if (!currentList.any { it.id == pokemon.id }) {
                currentList.add(pokemon)
            }
        }
        _pokemonList.value = currentList
    }

    override fun getPokemonById(id: Int): Pokemon? {
        return _pokemonList.value.find { it.id == id }
    }
}

interface PokemonListManager {
    val pokemonList: StateFlow<List<Pokemon>>
    fun updatePokemonList(newList: List<Pokemon>)
    fun getPokemonById(id: Int): Pokemon?
}
