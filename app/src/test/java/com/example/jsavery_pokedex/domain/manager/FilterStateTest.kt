package com.example.jsavery_pokedex.domain.manager

import com.example.jsavery_pokedex.presentation.ui.components.Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FilterStateTest {

    private val sut = PokemonFilterManager()

    @Test
    fun `toggleTypeFilter follows FIFO structure & re-selecting a type removes it`() {
        sut.toggleTypeFilter(Type.WATER.name)

        assertEquals(listOf("WATER"), sut.filterState.value.selectedTypes)

        sut.toggleTypeFilter(Type.FIRE.name)

        assertEquals(listOf("WATER", "FIRE"), sut.filterState.value.selectedTypes)

        sut.toggleTypeFilter(Type.DRAGON.name)

        assertEquals(listOf("FIRE", "DRAGON"), sut.filterState.value.selectedTypes)

        sut.toggleTypeFilter(Type.DRAGON.name)

        assertEquals(listOf("FIRE"), sut.filterState.value.selectedTypes)

        sut.toggleTypeFilter(Type.FIRE.name)

        assertEquals(emptyList<String>(), sut.filterState.value.selectedTypes)
    }
}
