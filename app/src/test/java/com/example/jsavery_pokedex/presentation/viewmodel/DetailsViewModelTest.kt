package com.example.jsavery_pokedex.presentation.viewmodel

import app.cash.turbine.test
import com.example.jsavery_pokedex.BaseTest
import com.example.jsavery_pokedex.MainDispatcherExtension
import com.example.jsavery_pokedex.data.model.EvolutionDetail
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.domain.PokemonListManager
import com.example.jsavery_pokedex.mock.MockData
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest : BaseTest() {

    private var mockRepository = mockk<PokemonRepository>(relaxed = true)
    private var mockPokemonListManager = mockk<PokemonListManager>(relaxed = true)

    @InjectMockKs
    private lateinit var sut: DetailsViewModel

    @BeforeEach
    fun setup() {
        coEvery { mockPokemonListManager.updatePokemonList(any()) } returns Unit
    }

    @Test
    fun `getPokemonDetail() pokemon already exists in PokemonListManager success`() = runTest {
        coEvery { mockPokemonListManager.getPokemonById(any()) } returns MockData.MOCK_POKEMON_BULBASAUR

        sut.getPokemonDetail(1)

        sut.detailsUiState.test {
            assertEquals(
                PokemonDetailsUiState(
                    isLoading = false,
                    pokemon = MockData.MOCK_POKEMON_BULBASAUR,
                    error = null
                ),
                awaitItem()
            )
        }
    }

    @Test
    fun `getPokemonDetail() pokemon doesnt exist in PokemonListManager so fetchPokemonDetails success`() =
        runTest {
            coEvery { mockPokemonListManager.getPokemonById(any()) } returns null
            coEvery { mockRepository.getPokemonDetail(any()) } returns Result.success(MockData.MOCK_POKEMON_BULBASAUR)

            sut.getPokemonDetail(1)

            sut.detailsUiState.test {
                assertEquals(
                    PokemonDetailsUiState(
                        isLoading = false,
                        pokemon = MockData.MOCK_POKEMON_BULBASAUR,
                        error = null
                    ),
                    awaitItem()
                )
            }
        }

    @Test
    fun `getPokemonDetail() pokemon doesnt exist in PokemonListManager so fetchPokemonDetails failure`() =
        runTest {
            coEvery { mockRepository.getPokemonDetail(any()) } returns Result.failure(
                NullPointerException()
            )
            coEvery { mockPokemonListManager.getPokemonById(any()) } returns null

            sut.getPokemonDetail(1)

            sut.detailsUiState.test {
                assertEquals(
                    PokemonDetailsUiState(
                        isLoading = false,
                        pokemon = null,
                        error = NullPointerException().toString()
                    ),
                    awaitItem()
                )
            }
        }

    @Test
    fun `getPokemonEvolutionDetail() functions appropriately`() {
        coEvery { mockPokemonListManager.getPokemonById(1) } returns MockData.MOCK_POKEMON_BULBASAUR

        val result = sut.getPokemonEvolutionDetail(1)

        assertEquals(
            EvolutionDetail(
                id = 1,
                name = "Bulbasaur",
                fullImage = "https://example.com/bulbasaur_full.png"
            ), result
        )
    }

}
