package com.example.jsavery_pokedex.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jsavery_pokedex.domain.util.Result
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.mock.MockData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private var mockRepository = mockk<PokemonRepository>(relaxed = true)
    private val mockResponse = mockk<PokemonResponse>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN initial state THEN should be Loading`() = runTest {
        // then
        assertTrue(viewModel.pokemonListUiState.value is PokemonListUiState.Loading)
    }

    @Test
    fun `WHEN fetchPokemon succeeds THEN uiState should be Success`() = runTest {
        // given
        val mockPokemonList = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
        coEvery { mockResponse.data } returns mockPokemonList
        coEvery { mockRepository.getPokemonList() } returns flowOf(
            Result.Success(
                mockResponse
            )
        )

        // when
        viewModel.fetchPokemon()
        advanceUntilIdle()

        // then
        coVerify { mockRepository.getPokemonList() }
        val currentState = viewModel.pokemonListUiState.value
        assertTrue(currentState is PokemonListUiState.Success)
        assertEquals(mockPokemonList, (currentState as PokemonListUiState.Success).pokemonList)
    }

    @Test
    fun `WHEN fetchPokemon fails, THEN uiState should be Error`() = runTest {
        // given
        val errorMessage = "Network error"
        coEvery { mockRepository.getPokemonList() } returns flowOf(
            Result.Error(
                Throwable(errorMessage)
            )
        )

        // when
        viewModel.fetchPokemon()
        advanceUntilIdle()

        // then
        coVerify { mockRepository.getPokemonList() }
        val currentState = viewModel.pokemonListUiState.value
        assertTrue(currentState is PokemonListUiState.Error)
    }

    @Test
    fun `WHEN repository returns empty list, THEN uiState should be Success with empty list`() =
        runTest {
            // given
            coEvery { mockRepository.getPokemonList() } returns flowOf(
                Result.Success(
                    PokemonResponse(listOf())
                )
            )

            // when
            viewModel.fetchPokemon()
            advanceUntilIdle()

            // then
            val currentState = viewModel.pokemonListUiState.value
            assertTrue(currentState is PokemonListUiState.Success)
            assertTrue((currentState as PokemonListUiState.Success).pokemonList.isEmpty())
        }

    @Test
    fun `WHEN repository throws exception, THEN error message should match exception message`() =
        runTest {
            // given
            val expectedException = IOException("Network error")
            coEvery { mockRepository.getPokemonList() } returns flowOf(Result.Error(expectedException))

            // when
            viewModel.fetchPokemon()
            advanceUntilIdle()

            // then
            val currentState = viewModel.pokemonListUiState.value
            assertTrue(currentState is PokemonListUiState.Error)
            assertTrue((currentState as PokemonListUiState.Error).throwable is IOException)
        }
}