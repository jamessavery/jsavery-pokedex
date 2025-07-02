package com.example.jsavery_pokedex.presentation.viewmodel

import app.cash.turbine.test
import com.example.jsavery_pokedex.MainDispatcherExtension
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.domain.PokemonListManager
import com.example.jsavery_pokedex.mock.MockData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherExtension::class)
class MainViewModelTest {

    private val mockPokemonList = listOf(MockData.Companion.MOCK_POKEMON_SQUIRTLE)
    private val errorMessage = "Network error"

    private val testDispatcher = StandardTestDispatcher()
    private var mockRepository = mockk<PokemonRepository>(relaxed = true)
    private var mockPokemonListManager = mockk<PokemonListManager>(relaxed = true)
    private val mockResponse = mockk<PokemonResponse>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) } returns Result.success(
            PokemonResponse(
                10,
                next = MainViewModel.Companion.FIRST_PAGE,
                previous = null,
                data = listOf(),
            ),
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

            coEvery { mockRepository.getPokemonList(any()) } returns Result.success(
                mockResponse,
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            viewModel.pokemonListUiState.test {
                Assertions.assertEquals( // Loads first page
                    PokemonListUiState.Success(
                        pokemonList = mockPokemonList,
                        nextPage = nextPageTwo,
                        isLoadingMore = false,
                    ),
                    awaitItem(),
                )
                coVerify(exactly = 1) { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }

                // and when
                viewModel.onLoadMore(nextPageTwo)

                // and then
                Assertions.assertEquals(
                    PokemonListUiState.Success(
                        pokemonList = mockPokemonList,
                        nextPage = nextPageTwo,
                        isLoadingMore = true,
                    ),
                    awaitItem(),
                )
                Assertions.assertEquals( // Loads second page & concatenates
                    PokemonListUiState.Success(
                        pokemonList = mockPokemonList + mockPokemonList,
                        nextPage = nextPageTwo,
                        isLoadingMore = false,
                    ),
                    awaitItem(),
                )

                coVerify(exactly = 1) { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }
                coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
            }
        }

    @Test
    fun `WHEN fetchPokemon succeeds THEN uiState should be Success and WHEN onLoadMore THEN error is handled appropriately`() =
        runTest {
            // given
            val nextPageTwo = 2
            val exception = Throwable(errorMessage)
            coEvery { mockResponse.data } returns mockPokemonList
            coEvery { mockResponse.next } returns nextPageTwo
            coEvery { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) } returns Result.success(
                mockResponse,
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            viewModel.pokemonListUiState.test {
                // then
                coVerify(exactly = 1) { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }
                Assertions.assertEquals(
                    PokemonListUiState.Success(
                        pokemonList = mockPokemonList,
                        nextPage = nextPageTwo,
                        isLoadingMore = false,
                    ),
                    awaitItem(),
                )

                // and given
                coEvery { mockRepository.getPokemonList(nextPageTwo) } returns Result.failure(
                    exception,
                )

                // and when
                viewModel.onLoadMore(nextPageTwo)

                // and then
                Assertions.assertEquals(
                    PokemonListUiState.Success(
                        pokemonList = mockPokemonList,
                        nextPage = nextPageTwo,
                        isLoadingMore = true,
                    ),
                    awaitItem(),
                )

                Assertions.assertEquals(PokemonListUiState.Error(exception), awaitItem())

                coVerify(exactly = 1) { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }
                coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
            }
        }

    @Test
    fun `WHEN fetchPokemon succeeds but nextPage is null THEN avoid error by defaulting to 1`() =
        runTest {
            // given
            coEvery { mockResponse.data } returns mockPokemonList
            coEvery { mockResponse.next } returns null
            coEvery { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) } returns Result.success(
                mockResponse,
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            viewModel.pokemonListUiState.test {
                Assertions.assertEquals(
                    PokemonListUiState.Success(
                        pokemonList = mockPokemonList,
                        nextPage = MainViewModel.Companion.FIRST_PAGE,
                        isLoadingMore = false,
                    ),
                    awaitItem(),
                )
                coVerify(exactly = 1) { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }
            }
        }

    @Test
    fun `GIVEN isLoading is true THEN onLoadMore is blocked from loading more`() = runTest {
        // given
        coEvery { mockRepository.getPokemonList(any()) } returns Result.success(
            PokemonResponse(
                10,
                next = MainViewModel.Companion.FIRST_PAGE,
                previous = null,
                data = listOf(),
            ),
        )
        coEvery { mockPokemonListManager.updatePokemonList(listOf()) } returns Unit

        val nextPageTwo = 2
        coEvery { mockResponse.data } returns mockPokemonList
        coEvery { mockResponse.next } returns nextPageTwo
        coEvery { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) } returns Result.success(
            mockResponse,
        )

        // when
        viewModel = MainViewModel(mockRepository, mockPokemonListManager)
        advanceUntilIdle()

        // then
        coVerify(exactly = 1) { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }

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
        coVerify(exactly = 1) { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }
        coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
    }

    @Test
    fun `WHEN fetchPokemon fails, THEN uiState should be Error`() = runTest {
        // given
        val exception = Throwable(errorMessage)
        coEvery { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) } returns Result.failure(
            exception,
        )

        // when
        viewModel = MainViewModel(mockRepository, mockPokemonListManager)
        advanceUntilIdle()

        // then
        coVerify { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) }

        viewModel.pokemonListUiState.test {
            Assertions.assertEquals(PokemonListUiState.Error(exception), awaitItem())
        }
    }

    @Test
    fun `WHEN repository returns empty list, THEN uiState should be Success with empty list`() =
        runTest {
            // given
            coEvery { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) } returns Result.success(
                PokemonResponse(
                    10,
                    next = MainViewModel.Companion.FIRST_PAGE,
                    previous = null,
                    data = listOf(),
                ),
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            viewModel.pokemonListUiState.test {
                Assertions.assertEquals(
                    PokemonListUiState.Success(
                        pokemonList = emptyList(),
                        nextPage = MainViewModel.Companion.FIRST_PAGE,
                        isLoadingMore = false,
                    ),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `WHEN repository throws exception, THEN error message should match exception message`() =
        runTest {
            // given
            val expectedException = IOException("Network error")
            coEvery { mockRepository.getPokemonList(MainViewModel.Companion.FIRST_PAGE) } returns Result.failure(
                expectedException,
            )

            // when
            viewModel = MainViewModel(mockRepository, mockPokemonListManager)
            advanceUntilIdle()

            // then
            viewModel.pokemonListUiState.test {
                Assertions.assertEquals(
                    PokemonListUiState.Error(
                        expectedException,
                    ),
                    awaitItem(),
                )
            }
        }
}
