package com.example.jsavery_pokedex.domain.usecase

import com.example.jsavery_pokedex.BaseTest
import com.example.jsavery_pokedex.domain.PokemonListManager
import com.example.jsavery_pokedex.domain.exception.PokemonException
import com.example.jsavery_pokedex.mock.MockData
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EvolutionDetailsTest : BaseTest() {

    @MockK
    private lateinit var pokemonListManager: PokemonListManager

    private lateinit var sut: EvolutionDetails

    @BeforeEach
    fun setup() {
        sut = EvolutionDetails(pokemonListManager)
    }

    @Test
    fun `Happy path returns success with evolution details`() =
        runTest {
            every { pokemonListManager.getPokemonById(1) } returns MockData.MOCK_POKEMON_BULBASAUR
            every { pokemonListManager.getPokemonById(2) } returns
                MockData.MOCK_POKEMON_BULBASAUR.copy(
                    name = "Ivysaur",
                )
            every { pokemonListManager.getPokemonById(3) } returns
                MockData.MOCK_POKEMON_BULBASAUR.copy(
                    name = "Venasaur",
                )

            val result = sut.invoke(1).getOrNull()

            assertEquals(result!!.size, 3)
            assertEquals(result[0].name, "Bulbasaur")
            assertEquals(result[1].name, "Ivysaur")
            assertEquals(result[2].name, "Venasaur")

            verify { pokemonListManager.getPokemonById(1) }
            verify { pokemonListManager.getPokemonById(2) }
            verify { pokemonListManager.getPokemonById(3) }
        }

    @Test
    fun `unhappy path returns failure when pokemon not found`() {
        every { pokemonListManager.getPokemonById(1) } returns null

        val result = sut.invoke(1)

        assertEquals(result.exceptionOrNull()?.cause, PokemonException.NotFound(1).cause)
        verify(exactly = 1) { pokemonListManager.getPokemonById(1) }
    }

    @Test
    fun `invoke returns success with empty list when pokemon has no evolutions`() {
        every { pokemonListManager.getPokemonById(1) } returns MockData.MOCK_POKEMON_CHARMANDER

        val result = sut.invoke(1)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty()!!)

        verify(exactly = 1) { pokemonListManager.getPokemonById(1) }
    }

    @Test
    fun `happy path skips null evolutions and returns only valid ones`() {
        every { pokemonListManager.getPokemonById(1) } returns MockData.MOCK_POKEMON_BULBASAUR
        every { pokemonListManager.getPokemonById(2) } returns null
        every { pokemonListManager.getPokemonById(3) } returns
            MockData.MOCK_POKEMON_BULBASAUR.copy(
                name = "Venasaur",
            )

        val result = sut.invoke(1).getOrNull()

        assertEquals(result!!.size, 2)
        assertEquals(result[0].name, "Bulbasaur")
        // Skips middle evolution Ivysaur, as it returned null
        assertEquals(result[1].name, "Venasaur")

        verify { pokemonListManager.getPokemonById(1) }
        verify { pokemonListManager.getPokemonById(2) }
        verify { pokemonListManager.getPokemonById(3) }
    }
}
