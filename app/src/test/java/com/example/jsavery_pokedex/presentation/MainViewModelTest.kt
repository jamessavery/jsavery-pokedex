package com.example.jsavery_pokedex.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    // TODO Fix broken tests Issue-16
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockPokemonList = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
    private val errorMessage = "Network error"

    private val testDispatcher = StandardTestDispatcher()
    private var mockRepository = mockk<PokemonRepository>(relaxed = true)
    private val mockResponse = mockk<PokemonResponse>(relaxed = true)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

//    @Test
//    fun `GIVEN initial state THEN should be Loading`() = runTest {
//        // when
//        viewModel = MainViewModel(mockRepository)
//
//        // then
//        assertTrue(viewModel.pokemonListUiState.value is PokemonListUiState.Loading)
//    }
//
//    @Test
//    fun `WHEN fetchPokemon succeeds THEN uiState should be Success and WHEN onLoadMore THEN concatenates next batch`() = runTest {
//        // given
//        val nextPageTwo = 2
//        coEvery { mockResponse.data } returns mockPokemonList
//        coEvery { mockResponse.next } returns nextPageTwo
//        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns flowOf(
//            Result.Success(
//                mockResponse
//            )
//        )
//
//        // when
//        viewModel = MainViewModel(mockRepository)
//        advanceUntilIdle()
//
//        // then
//        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//        var currentState = viewModel.pokemonListUiState.value
//        assertTrue(currentState is PokemonListUiState.Success)
//        assertEquals(mockPokemonList, (currentState as PokemonListUiState.Success).pokemonList)
//
//        // and given
//        val concatenatedPokemonList = mockPokemonList + mockPokemonList
//        coEvery { mockRepository.getPokemonList(nextPageTwo) } returns flowOf(
//            Result.Success(
//                mockResponse
//            )
//        )
//
//        // and when
//        viewModel.onLoadMore(nextPageTwo)
//        advanceUntilIdle()
//
//        // and then
//        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//        coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
//        currentState = viewModel.pokemonListUiState.value
//        assertTrue(currentState is PokemonListUiState.Success)
//        assertEquals(concatenatedPokemonList, (currentState as PokemonListUiState.Success).pokemonList)
//    }
//
//    @Test
//    fun `WHEN fetchPokemon succeeds THEN uiState should be Success and WHEN onLoadMore THEN error is handled appropriately`() = runTest {
//        // given
//        val nextPageTwo = 2
//        coEvery { mockResponse.data } returns mockPokemonList
//        coEvery { mockResponse.next } returns nextPageTwo
//        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns flowOf(
//            Result.Success(
//                mockResponse
//            )
//        )
//
//        // when
//        viewModel = MainViewModel(mockRepository)
//        advanceUntilIdle()
//
//        // then
//        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//        var currentState = viewModel.pokemonListUiState.value
//        assertTrue(currentState is PokemonListUiState.Success)
//        assertEquals(mockPokemonList, (currentState as PokemonListUiState.Success).pokemonList)
//
//        // and given
//        coEvery { mockRepository.getPokemonList(nextPageTwo) } returns flowOf(
//            Result.Error(
//                Throwable(errorMessage)
//            )
//        )
//
//        // and when
//        viewModel.onLoadMore(nextPageTwo)
//        advanceUntilIdle()
//
//        // and then
//        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//        coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
//        currentState = viewModel.pokemonListUiState.value
//        assertTrue(currentState is PokemonListUiState.Error)
//        assertEquals(errorMessage, (currentState as PokemonListUiState.Error).throwable.message)
//    }
//
//    @Test
//    fun `WHEN fetchPokemon succeeds but nextPage is null THEN avoid error by defaulting to 1`() = runTest {
//        // given
//        coEvery { mockResponse.data } returns mockPokemonList
//        coEvery { mockResponse.next } returns null
//        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns flowOf(
//            Result.Success(
//                mockResponse
//            )
//        )
//
//        // when
//        viewModel = MainViewModel(mockRepository)
//        advanceUntilIdle()
//
//        // then
//        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//        val currentState = viewModel.pokemonListUiState.value
//        assertEquals(FIRST_PAGE, (currentState as PokemonListUiState.Success).nextPage)
//    }
//
//    @Test
//    fun `GIVEN isLoading is true THEN onLoadMore is blocked from loading more`() = runTest {
//        // given
//        val nextPageTwo = 2
//        coEvery { mockResponse.data } returns mockPokemonList
//        coEvery { mockResponse.next } returns nextPageTwo
//        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns flowOf(
//            Result.Success(
//                mockResponse
//            )
//        )
//
//        // when
//        viewModel = MainViewModel(mockRepository)
//        advanceUntilIdle()
//
//        // then
//        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//
//        // and when
//        viewModel.onLoadMore(nextPageTwo)
//        advanceUntilIdle()
//
//        // onLoadMore() is locked, these calls will not register
//        viewModel.onLoadMore(nextPageTwo)
//        viewModel.onLoadMore(nextPageTwo)
//        viewModel.onLoadMore(nextPageTwo)
//        viewModel.onLoadMore(nextPageTwo)
//        viewModel.onLoadMore(nextPageTwo)
//
//        // and then
//        coVerify(exactly = 1) { mockRepository.getPokemonList(FIRST_PAGE) }
//        coVerify(exactly = 1) { mockRepository.getPokemonList(nextPageTwo) }
//    }
//
//    @Test
//    fun `WHEN fetchPokemon fails, THEN uiState should be Error`() = runTest {
//        // given
//        coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns flowOf(
//            Result.Error(
//                Throwable(errorMessage)
//            )
//        )
//
//        // when
//        viewModel = MainViewModel(mockRepository)
//        advanceUntilIdle()
//
//        // then
//        coVerify { mockRepository.getPokemonList(FIRST_PAGE) }
//        val currentState = viewModel.pokemonListUiState.value
//        assertTrue(currentState is PokemonListUiState.Error)
//    }
//
//    @Test
//    fun `WHEN repository returns empty list, THEN uiState should be Success with empty list`() =
//        runTest {
//            // given
//            coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns flowOf(
//                Result.Success(
//                    PokemonResponse(10, next = FIRST_PAGE, previous = null, data = listOf())
//                )
//            )
//
//            // when
//            viewModel = MainViewModel(mockRepository)
//            advanceUntilIdle()
//
//            // then
//            val currentState = viewModel.pokemonListUiState.value
//            assertTrue(currentState is PokemonListUiState.Success)
//            assertTrue((currentState as PokemonListUiState.Success).pokemonList.isEmpty())
//        }
//
//    @Test
//    fun `WHEN repository throws exception, THEN error message should match exception message`() =
//        runTest {
//            // given
//            val expectedException = IOException("Network error")
//            coEvery { mockRepository.getPokemonList(FIRST_PAGE) } returns flowOf(Result.Error(expectedException))
//
//            // when
//            viewModel = MainViewModel(mockRepository)
//            advanceUntilIdle()
//
//            // then
//            val currentState = viewModel.pokemonListUiState.value
//            assertTrue(currentState is PokemonListUiState.Error)
//            assertTrue((currentState as PokemonListUiState.Error).throwable is IOException)
//        }
}
