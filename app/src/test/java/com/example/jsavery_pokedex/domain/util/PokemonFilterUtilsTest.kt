package com.example.jsavery_pokedex.domain.util

import com.example.jsavery_pokedex.domain.manager.FilterState
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.presentation.screens.SortOption
import com.example.jsavery_pokedex.presentation.ui.components.Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PokemonFilterUtilsTest {

    private val samplePokemon = listOf(
        MockData.MOCK_POKEMON_SQUIRTLE,
        MockData.MOCK_POKEMON_BULBASAUR,
        MockData.MOCK_POKEMON_CHARMANDER,
    )

    @Test
    fun `should filter by name case insensitive`() {
        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "bulba",
            currentFilter = FilterState(),
        )

        assertEquals(1, result.size)
        assertEquals("Bulbasaur", result.first().name)
    }

    @Test
    fun `should return empty list when no matches found`() {
        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "nonexistent",
            currentFilter = FilterState(),
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `should filter by type`() {
        val filterState = FilterState(
            selectedTypes = listOf(Type.FIRE.name),
        )

        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "",
            currentFilter = filterState,
        )

        assertEquals(1, result.size)
        assertEquals("Charmander", result.first().name)
    }

    @Test
    fun `should sort by Pokedex number`() {
        val filterState = FilterState(
            sortOption = SortOption.POKEDEX_NUMBER,
        )

        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "",
            currentFilter = filterState,
        )

        assertEquals(listOf(1, 4, 7), result.map { it.id })
    }

    @Test
    fun `should sort alphabetically A-Z`() {
        val filterState = FilterState(
            sortOption = SortOption.ALPHABETICAL_A_Z,
        )

        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "",
            currentFilter = filterState,
        )

        assertEquals(
            listOf("Bulbasaur", "Charmander", "Squirtle"),
            result.map { it.name },
        )
    }

    @Test
    fun `should sort alphabetically Z-A`() {
        val filterState = FilterState(
            sortOption = SortOption.ALPHABETICAL_Z_A,
        )

        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "",
            currentFilter = filterState,
        )

        assertEquals(
            listOf("Squirtle", "Charmander", "Bulbasaur"),
            result.map { it.name },
        )
    }

    @Test
    fun `should combine search and type filters`() {
        val filterState = FilterState(
            selectedTypes = listOf("GRASS"),
        )

        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "bulba",
            currentFilter = filterState,
        )

        assertEquals(1, result.size)
        assertEquals("Bulbasaur", result.first().name)
    }

    @Test
    fun `should filter by multiple types`() {
        val filterState = FilterState(
            selectedTypes = listOf(Type.FIRE.name, Type.WATER.name),
        )

        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "",
            currentFilter = filterState,
        )

        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Charmander" })
        assertTrue(result.any { it.name == "Squirtle" })
    }

    @Test
    fun `should handle pokemon with multiple types`() {
        val filterState = FilterState(
            selectedTypes = listOf(Type.POISON.name, Type.GRASS.name),
        )

        val result = PokemonFilterUtils.applyPokemonFilter(
            pokemonList = samplePokemon,
            searchQuery = "",
            currentFilter = filterState,
        )

        assertEquals(1, result.size)
        assertEquals("Bulbasaur", result.first().name)
    }
}
