package com.example.jsavery_pokedex.presentation

import app.cash.turbine.test
import com.example.jsavery_pokedex.MainDispatcherExtension
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.domain.PokemonListManager
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel.Companion.FIRST_PAGE
import com.example.jsavery_pokedex.presentation.viewmodel.PokemonListUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherExtension::class)
class MainViewModelTest {

    private val mockPokemonList = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
    private val errorMessage = "Network error"

    private val testDispatcher = StandardTestDispatcher()
    private var mockRepository = mockk<PokemonRepository>(relaxed = true)
    private var mockPokemonListManager = mockk<PokemonListManager>(relaxed = true)
    private val mockResponse = mockk<PokemonResponse>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.success(
            PokemonResponse(10, next = FIRST_PAGE, previous = null, data = listOf())
        )
        coEvery { mockPokemonListManager.updatePokemonList(listOf()) } returns Unit
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN fetchPokemon succeeds THEN uiState should be Success and WHEN onLoadMore THEN concatenates next batch`() =
        runTest {
            // given
            val nextPageTwo = 2
            coEvery { mockResponse.data } returns mockPokemonList
            coEvery { mockResponse.next } returns nextPageTwo
            coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.success(
                mockResponse,
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
            var currentState = viewModel.pokemonListUiState.value
            assertTrue(currentState is PokemonListUiState.Success)
            assertEquals(mockPokemonList, (currentState as PokemonListUiState.Success).pokemonList)

            // and given
            val concatenatedPokemonList = mockPokemonList + mockPokemonList
            coEvery { mockRepository.getPokemonList(nextPageTwo) } returns Result.success(
                mockResponse,
            )

            // and when
            viewModel.onLoadMore(nextPageTwo)
            advanceUntilIdle()

            // and then
            coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
            coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
            currentState = viewModel.pokemonListUiState.value
            assertTrue(currentState is PokemonListUiState.Success)
            assertEquals(
                concatenatedPokemonList,
                (
                        currentState as
                                PokemonListUiState.Success
                        ).pokemonList,
            )
        }

    @Test
    fun `WHEN fetchPokemon succeeds THEN uiState should be Success and WHEN onLoadMore THEN error is handled appropriately`() =
        runTest {
            // given
            val nextPageTwo = 2
            coEvery { mockResponse.data } returns mockPokemonList
            coEvery { mockResponse.next } returns nextPageTwo
            coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.success(
                mockResponse
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
            var currentState = viewModel.pokemonListUiState.value
            assertTrue(currentState is PokemonListUiState.Success)
            assertEquals(mockPokemonList, (currentState as PokemonListUiState.Success).pokemonList)

            // and given
            coEvery { mockRepository.getPokemonList(nextPageTwo) } returns Result.failure(
                Throwable(errorMessage)
            )

            // and when
            viewModel.onLoadMore(nextPageTwo)
            advanceUntilIdle()

            // and then
            coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
            coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
            currentState = viewModel.pokemonListUiState.value
            assertTrue(currentState is PokemonListUiState.Error)
            assertEquals(errorMessage, (currentState as PokemonListUiState.Error).throwable.message)
        }

//    @Test
//    fun `WHEN fetchPokemon succeeds but nextPage is null THEN avoid error basdy defaulting to 1`() = runTest {
//        // given
//        coEvery { mockResponse.data } returns mockPokemonList
//        coEvery { mockResponse.next } returns null
//        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.success(mockResponse)
//
//        // when & then
//        viewModel = MainViewModel(mockRepository, mockPokemonListManager)
//
//        viewModel.pokemonListUiState.test {
//            // Should start with Loading state
//            assertEquals(PokemonListUiState.Loading, awaitItem())
//
//            // Wait for async operations to complete
//            advanceUntilIdle()
//
//            // Should emit Success state with nextPage defaulted to 1
//            val successState = awaitItem()
//            assertTrue(successState is PokemonListUiState.Success)
//            assertEquals(FIRST_PAGE, successState.nextPage)
//            assertEquals(mockPokemonList, successState.pokemonList)
//            assertFalse(successState.isLoadingMore)
//
//            // Verify repository was called
//            coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//
//            // Clean up
//            cancelAndConsumeRemainingEvents()
//        }
//    }


    @Test // jimmy
    fun `WHEN fetchPokemon succeeds but nextPage is null THEN avoid error by defaulting to 1`() =
        runTest {
            // given
            coEvery { mockResponse.data } returns mockPokemonList
            coEvery { mockResponse.next } returns null
            coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.success(
                mockResponse
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            viewModel.pokemonListUiState.test {
                val successState = awaitItem()
                successState as PokemonListUiState.Success
                assertEquals(FIRST_PAGE, successState.nextPage)
                assertEquals(mockPokemonList, successState.pokemonList)
                assertFalse(successState.isLoadingMore)
                coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
            }
        }

    @Test
    fun `GIVEN isLoading is true THEN onLoadMore is blocked from loading more`() = runTest {
        // given
        coEvery { mockRepository.getPokemonList(any()) } returns Result.success(
            PokemonResponse(10, next = FIRST_PAGE, previous = null, data = listOf())
        )
        coEvery { mockPokemonListManager.updatePokemonList(listOf()) } returns Unit

        val nextPageTwo = 2
        coEvery { mockResponse.data } returns mockPokemonList
        coEvery { mockResponse.next } returns nextPageTwo
        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.success(
            mockResponse
        )

        // when
        viewModel = MainViewModel(mockRepository, mockPokemonListManager)
        advanceUntilIdle()

        // then
        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }

        // and when
        viewModel.onLoadMore(nextPageTwo)
        advanceUntilIdle()

        // onLoadMore() is locked, these calls will not register
        viewModel.onLoadMore(nextPageTwo)
        viewModel.onLoadMore(nextPageTwo)
        viewModel.onLoadMore(nextPageTwo)
        viewModel.onLoadMore(nextPageTwo)
        viewModel.onLoadMore(nextPageTwo)

        // and then
        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
        coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
    }

    @Test
    fun `WHEN fetchPokemon fails, THEN uiState should be Error`() = runTest {
        // given
        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.failure(
            Throwable(errorMessage)
        )

        // when
        viewModel = MainViewModel(mockRepository, mockPokemonListManager)
        advanceUntilIdle()

        // then
        coVerify { mockRepository.getPokemonList(FIRST_PAGE) }
        val currentState = viewModel.pokemonListUiState.value
        assertTrue(currentState is PokemonListUiState.Error)
    }

    @Test
    fun `WHEN repository returns empty list, THEN uiState should be Success with empty list`() =
        runTest {
            // given
            coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.success(
                PokemonResponse(10, next = FIRST_PAGE, previous = null, data = listOf())
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
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
            coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns Result.failure(
                expectedException
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            val currentState = viewModel.pokemonListUiState.value
            assertTrue(currentState is PokemonListUiState.Error)
            assertTrue((currentState as PokemonListUiState.Error).throwable is IOException)
        }
}
