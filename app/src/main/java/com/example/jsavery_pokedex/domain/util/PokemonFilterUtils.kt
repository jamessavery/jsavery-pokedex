package com.example.jsavery_pokedex.domain.util

import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.domain.manager.FilterState
import com.example.jsavery_pokedex.presentation.screens.SortOption

object PokemonFilterUtils {
    fun applyPokemonFilter(
        pokemonList: List<Pokemon>,
        searchQuery: String,
        currentFilter: FilterState,
    ) =
        pokemonList.filter { pokemon ->
            val matchesSearch = searchQuery.isEmpty() ||
                pokemon.name.contains(searchQuery, ignoreCase = true) ||
                pokemon.id.toString() == searchQuery

            val matchesType = currentFilter.selectedTypes.isEmpty() ||
                pokemon.types.any { pokemonType ->
                    currentFilter.selectedTypes.contains(pokemonType.uppercase())
                }

            matchesSearch && matchesType
        }.let { filteredList ->
            when (currentFilter.sortOption) {
                SortOption.POKEDEX_NUMBER -> filteredList.sortedBy { it.id }
                SortOption.ALPHABETICAL_A_Z -> filteredList.sortedBy { it.name }
                SortOption.ALPHABETICAL_Z_A -> filteredList.sortedByDescending { it.name }
            }
        }
}
