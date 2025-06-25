package com.example.jsavery_pokedex.data.datasource

import com.example.jsavery_pokedex.data.model.PokemonResponse
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDataSourceTest {
    // TODO Issue-16 test dependencies are a mess, address that too
    private lateinit var pokemonDataSource: PokemonDataSource
    private val mockResponse = mockk<PokemonResponse>(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher())
        //        pokemonDataSource = PokemonDataSourceImpl()
    }

    private fun testDispatcher() = testDispatcher

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    //    @Test
    //    fun `WHEN getPokemonList returns success THEN API call is successful and WHEN loads next
    // page THEN success`() =
    //        runTest {
    //            // given
    //            val data = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
    //            every { mockResponse.data } returns data
    //            val mockCall: Response<PokemonResponse> = Response.success(mockResponse)
    //            coEvery { mockPokemonService.getPokemonList(FIRST_PAGE) } returns mockCall
    //
    //            // when
    //            var result = pokemonDataSource.getPokemonList(FIRST_PAGE)
    //
    //            // then
    //            coVerify { mockPokemonService.getPokemonList(FIRST_PAGE) }
    //            assertEquals(mockCall.body()?.data, result?.data)
    //
    //            // and given
    //            coEvery { mockPokemonService.getPokemonList(2) } returns mockCall
    //
    //            // and when
    //            result = pokemonDataSource.getPokemonList(2)
    //
    //            // and then
    //            coVerify(exactly = 1) { mockPokemonService.getPokemonList(FIRST_PAGE) }
    //            coVerify(exactly = 1) { mockPokemonService.getPokemonList(2) }
    //            assertEquals(mockCall.body()?.data, result?.data)
    //        }
    //
    //    @Test(expected = HttpException::class)
    //    fun `WHEN getPokemonList returns error THEN API call fails`() = runTest {
    //        // given
    //        val responseBody = Response.error<PokemonResponse>(
    //            HTTP_INTERNAL_ERROR, "".toResponseBody()
    //        ).errorBody()
    //        val mockCall: Response<PokemonResponse> =
    //            Response.error(HTTP_INTERNAL_ERROR, responseBody!!)
    //        coEvery { mockPokemonService.getPokemonList(FIRST_PAGE) } returns mockCall
    //
    //        // when
    //        val result = pokemonDataSource.getPokemonList(FIRST_PAGE)
    //
    //        // then
    //        coVerify { mockPokemonService.getPokemonList(FIRST_PAGE) }
    //        assertEquals(result, HttpException(mockCall))
    //    }
}
