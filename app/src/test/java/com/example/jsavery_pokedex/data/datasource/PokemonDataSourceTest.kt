package com.example.jsavery_pokedex.data.datasource

import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.services.PokemonService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDataSourceTest {

    private lateinit var pokemonDataSource: PokemonDataSource
    private val mockPokemonService = mockk<PokemonService>(relaxed = true)
    private val mockResponse = mockk<PokemonResponse>(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher())
        pokemonDataSource = PokemonDataSourceImpl(mockPokemonService)
    }

    private fun testDispatcher() = testDispatcher

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN getPokemonList returns success THEN API call is successful`() = runTest {
        // given
        val data = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
        every { mockResponse.data } returns data
        val mockCall: Response<PokemonResponse> = Response.success(mockResponse)
        coEvery { mockPokemonService.getPokemonList() } returns mockCall

        // when
        val result = pokemonDataSource.getPokemonList()

        // then
        coVerify { mockPokemonService.getPokemonList() }
        assertEquals(mockCall.body()?.data, result?.data)
    }

    @Test(expected = HttpException::class)
    fun `WHEN getPokemonList returns error THEN API call fails`() = runTest {
        // given
        val responseBody = Response.error<PokemonResponse>(
            HTTP_INTERNAL_ERROR, "".toResponseBody()
        ).errorBody()
        val mockCall: Response<PokemonResponse> =
            Response.error(HTTP_INTERNAL_ERROR, responseBody!!)
        coEvery { mockPokemonService.getPokemonList() } returns mockCall

        // when
        val result = pokemonDataSource.getPokemonList()

        // then
        coVerify { mockPokemonService.getPokemonList() }
        assertEquals(result, HttpException(mockCall))
    }

}