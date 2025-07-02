package com.example.jsavery_pokedex.presentation.viewmodel

import app.cash.turbine.test
import com.example.jsavery_pokedex.BaseTest
import com.example.jsavery_pokedex.data.model.EvolutionDetail
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.domain.PokemonListManager
import com.example.jsavery_pokedex.mock.MockData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        // given
        coEvery { mockPokemonListManager.getPokemonById(any()) } returns MockData.MOCK_POKEMON_BULBASAUR

        // when
        sut.getPokemonDetail(1)

        // then
        sut.detailsUiState.test {
            assertEquals(
                PokemonDetailsUiState(
                    isLoading = false,
                    pokemon = MockData.MOCK_POKEMON_BULBASAUR,
                    error = null,
                ),
                awaitItem(),
            )

            coVerify(exactly = 0) { mockRepository.getPokemonDetail(any()) }
            coVerify(exactly = 1) { mockPokemonListManager.getPokemonById(1) }
        }
    }

    @Test
    fun `getPokemonDetail() pokemon doesnt exist in PokemonListManager so fetchPokemonDetails success`() =
        runTest {
            // given
            coEvery { mockPokemonListManager.getPokemonById(any()) } returns null
            coEvery { mockRepository.getPokemonDetail(any()) } returns Result.success(MockData.MOCK_POKEMON_BULBASAUR)

            // when
            sut.getPokemonDetail(1)

            sut.detailsUiState.test {
                // then
                assertEquals(
                    PokemonDetailsUiState(
                        isLoading = false,
                        pokemon = MockData.MOCK_POKEMON_BULBASAUR,
                        error = null,
                    ),
                    awaitItem(),
                )

                coVerify(exactly = 1) { mockRepository.getPokemonDetail(any()) }
                coVerify(exactly = 1) { mockPokemonListManager.getPokemonById(1) }
            }
        }

    @Test
    fun `getPokemonDetail() pokemon doesnt exist in PokemonListManager so fetchPokemonDetails failure`() =
        runTest {
            // given
            coEvery { mockRepository.getPokemonDetail(any()) } returns Result.failure(
                NullPointerException(),
            )
            coEvery { mockPokemonListManager.getPokemonById(any()) } returns null

            // when
            sut.getPokemonDetail(1)

            sut.detailsUiState.test {
                // then
                assertEquals(
                    PokemonDetailsUiState(
                        isLoading = false,
                        pokemon = null,
                        error = NullPointerException().toString(),
                    ),
                    awaitItem(),
                )

                coVerify(exactly = 1) { mockRepository.getPokemonDetail(any()) }
                coVerify(exactly = 1) { mockPokemonListManager.getPokemonById(1) }
            }
        }

    @Test
    fun `getPokemonEvolutionDetail() functions appropriately`() {
        // given
        coEvery { mockPokemonListManager.getPokemonById(1) } returns MockData.MOCK_POKEMON_BULBASAUR

        // when
        val result = sut.getPokemonEvolutionDetail(1)

        // then
        assertEquals(
            EvolutionDetail(
                id = 1,
                name = "Bulbasaur",
                fullImage = "https://example.com/bulbasaur_full.png",
            ),
            result,
        )
        verify { mockPokemonListManager.getPokemonById(1) }
    }
}
