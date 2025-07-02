package com.example.jsavery_pokedex.data.datasource

import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.services.PokemonService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class PokemonDataSourceTest {

    private val mockPokeService = mockk<PokemonService>()

    @InjectMockKs
    private lateinit var sut: PokemonDataSourceImpl

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPokemonList() success`() = runTest {
        // given
        val response = Response.success(
            PokemonResponse(
                count = 2,
                next = 1,
                previous = 0,
                data = MockData.MOCK_POKEMON_RESPONSE,
            ),
        )
        coEvery { mockPokeService.getPokemonList(any()) } returns response

        // when
        var result = sut.getPokemonList(1)

        // then
        assertEquals(response.body(), result)
        coVerify { mockPokeService.getPokemonList(1) }

        // and given
        coEvery { mockPokeService.getPokemonList(any()) } returns Response.success(null)

        // and when & then
        result = sut.getPokemonList(2)
        assertEquals(null, result)
        coVerify { mockPokeService.getPokemonList(2) }
    }

    @ParameterizedTest
    @MethodSource("httpErrorCodes")
    fun `getPokemonList() throws HttpException for various error codes`(
        errorCode: Int,
        errorMessage: String,
    ) = runTest {
        // given
        val mockResponse = mockk<Response<PokemonResponse>> {
            every { isSuccessful } returns false
            every { code() } returns errorCode
            every { message() } returns errorMessage
        }
        coEvery { mockPokeService.getPokemonList(any()) } returns mockResponse

        // when & then
        val exception = assertThrows<HttpException> {
            sut.getPokemonList(1)
        }
        assertEquals(mockResponse, exception.response())
        assertEquals(errorCode, exception.code())
    }

    @Test
    fun `getPokemonDetail() success`() = runTest {
        // given
        val response = Response.success(
            MockData.MOCK_POKEMON_BULBASAUR,
        )
        coEvery { mockPokeService.getPokemonDetail(any()) } returns response

        // when
        var result = sut.getPokemonDetail(1)

        // then
        assertEquals(response.body(), result)
        coVerify { mockPokeService.getPokemonDetail(1) }

        // and given
        coEvery { mockPokeService.getPokemonDetail(any()) } returns Response.success(null)

        // and when & then
        result = sut.getPokemonDetail(2)
        assertEquals(null, result)
        coVerify { mockPokeService.getPokemonDetail(2) }
    }

    @ParameterizedTest
    @MethodSource("httpErrorCodes")
    fun `getPokemonDetail() throws HttpException for various error codes`(
        errorCode: Int,
        errorMessage: String,
    ) = runTest {
        // given
        val mockResponse = mockk<Response<Pokemon>> {
            every { isSuccessful } returns false
            every { code() } returns errorCode
            every { message() } returns errorMessage
        }
        coEvery { mockPokeService.getPokemonDetail(any()) } returns mockResponse

        // when & then
        val exception = assertThrows<HttpException> {
            sut.getPokemonDetail(1)
        }
        assertEquals(mockResponse, exception.response())
        assertEquals(errorCode, exception.code())
    }

    companion object {
        @JvmStatic
        fun httpErrorCodes() = listOf(
            Arguments.of(400, "Bad Request"),
            Arguments.of(401, "Unauthorized"),
            Arguments.of(404, "Not Found"),
            Arguments.of(500, "Internal Server Error"),
            Arguments.of(503, "Service Unavailable"),
        )
    }
}
