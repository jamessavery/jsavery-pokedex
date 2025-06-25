package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

// TODO Issue-16 fix unit tests + add missing
@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryTest {
    private lateinit var pokemonRepository: PokemonRepository
    private val mockPokeDataSource = mockk<PokemonDataSource>(relaxed = true)
//    private val mockResponse = mockk<PokemonResponse>(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        pokemonRepository = PokemonRepositoryImpl(mockPokeDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

//    @Test
//    fun `WHEN getPokemonList returns success THEN API call is successful and WHEN loads next page THEN success`() =
//        runTest {
//            // given
//            val data = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
//            every { mockResponse.data } returns data
//            coEvery { mockPokeDataSource.getPokemonList(FIRST_PAGE) } returns mockResponse
//
//            // when
//            var result = pokemonRepository.getPokemonList(FIRST_PAGE).first()
//
//            // then
//            coVerify { mockPokeDataSource.getPokemonList(FIRST_PAGE) }
//            assertTrue(result is Result.Success)
//            assertEquals(Result.Success(mockResponse), (result as Result.Success))
//
//            // and given
//            coEvery { mockPokeDataSource.getPokemonList(2) } returns mockResponse
//
//            // and when
//            result = pokemonRepository.getPokemonList(2).first()
//
//            // and then
//            coVerify(exactly = 1) { mockPokeDataSource.getPokemonList(FIRST_PAGE) }
//            coVerify(exactly = 1) { mockPokeDataSource.getPokemonList(2) }
//            assertTrue(result is Result.Success)
//            assertEquals(Result.Success(mockResponse), (result as Result.Success))
//        }
//
//    @Test
//    fun `WHEN getPokemonList returns null THEN return error`() =
//        runTest {
//            // given
//            coEvery { mockPokeDataSource.getPokemonList(FIRST_PAGE) } returns null
//
//            // when
//            val result = pokemonRepository.getPokemonList(FIRST_PAGE).first()
//
//            // then
//            coVerify { mockPokeDataSource.getPokemonList(FIRST_PAGE) }
//            assertTrue(result is Result.Error)
//            val errorResult = result as Result.Error
//            assert(errorResult.exception is NullPointerException)
//        }
//
//    @Test
//    fun `WHEN getPokemonList fails THEN return error`() =
//        runTest {
//            // given
//            val expectedException = IOException("Network error")
//            coEvery { mockPokeDataSource.getPokemonList(FIRST_PAGE) } throws expectedException
//
//            // when
//            val result = pokemonRepository.getPokemonList(FIRST_PAGE).first()
//
//            // then
//            coVerify { mockPokeDataSource.getPokemonList(FIRST_PAGE) }
//            assertTrue(result is Result.Error)
//            val errorResult = result as Result.Error
//            assert(errorResult.exception == expectedException)
//        }
}
