package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.domain.util.Result
import com.example.jsavery_pokedex.mock.MockData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class PokemonRepositoryImplTest {

    private lateinit var pokemonRepository: PokemonRepository
    private val mockPokeDataSource = mockk<PokemonDataSource>(relaxed = true)
    private val mockResponse = mockk<PokemonResponse>(relaxed = true)

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

    @Test
    fun `WHEN getPokemonList returns success THEN API call is successful`() = runTest {
        // given
        val data = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
        every { mockResponse.data } returns data
        coEvery { mockPokeDataSource.getPokemonList() } returns mockResponse

        // when
        val result = pokemonRepository.getPokemonList().first()

        // then
        coVerify { mockPokeDataSource.getPokemonList() }
        assertTrue(result is Result.Success)
        assertEquals(Result.Success(mockResponse), (result as Result.Success))
    }

    @Test
    fun `WHEN getPokemonList returns null THEN return error`() = runTest {
        // given
        coEvery { mockPokeDataSource.getPokemonList() } returns null

        // when
        val result = pokemonRepository.getPokemonList().first()

        // then
        coVerify { mockPokeDataSource.getPokemonList() }
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assert(errorResult.exception is NullPointerException)
    }

    @Test
    fun `WHEN getPokemonList fails THEN return error`() = runTest {
        // given
        val expectedException = IOException("Network error")
        coEvery { mockPokeDataSource.getPokemonList() } throws expectedException

        // when
        val result = pokemonRepository.getPokemonList().first()

        // then
        coVerify { mockPokeDataSource.getPokemonList() }
        assertTrue(result is Result.Error)
        val errorResult = result as Result.Error
        assert(errorResult.exception == expectedException)
    }
}