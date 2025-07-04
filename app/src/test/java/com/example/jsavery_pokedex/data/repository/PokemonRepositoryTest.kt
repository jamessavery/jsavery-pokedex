package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.BaseTest
import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.presentation.viewmodel.MainViewModel.Companion.FIRST_PAGE
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryTest : BaseTest() {

    @MockK
    private lateinit var remoteDataSource: PokemonDataSource

    private val mockResponse = mockk<PokemonResponse>(relaxed = true)

    @InjectMockKs
    private lateinit var sut: PokemonRepositoryImpl

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN getPokemonList() returns success THEN API call is successful and WHEN loads next page THEN success`() =
        runTest {
            // given
            val data = listOf(MockData.MOCK_POKEMON_SQUIRTLE)
            every { mockResponse.data } returns data
            coEvery { remoteDataSource.getPokemonList(any()) } returns mockResponse

            // when
            var result = sut.getPokemonList(FIRST_PAGE)

            // then
            coVerify { remoteDataSource.getPokemonList(FIRST_PAGE) }
            assertEquals(mockResponse, (result.getOrNull()))

            // and when
            result = sut.getPokemonList(2)

            // and then
            coVerify(exactly = 1) { remoteDataSource.getPokemonList(FIRST_PAGE) }
            coVerify(exactly = 1) { remoteDataSource.getPokemonList(2) }
            assertEquals(mockResponse, result.getOrNull())
        }

    @Test
    fun `WHEN getPokemonList() returns null THEN return error`() =
        runTest {
            // given
            coEvery { remoteDataSource.getPokemonList(FIRST_PAGE) } returns null

            // when
            val result = sut.getPokemonList(FIRST_PAGE)

            // then
            coVerify { remoteDataSource.getPokemonList(FIRST_PAGE) }
            assertThrows<NullPointerException> {
                result.getOrThrow()
            }
        }

    @Test
    fun `WHEN getPokemonList() fails THEN return error`() =
        runTest {
            // given
            val expectedException = IOException("Network error")
            coEvery { remoteDataSource.getPokemonList(FIRST_PAGE) } throws expectedException

            // when
            val result = sut.getPokemonList(FIRST_PAGE)

            // then
            coVerify { remoteDataSource.getPokemonList(FIRST_PAGE) }
            assertThrows<IOException> {
                result.getOrThrow()
            }
        }

    @Test
    fun `WHEN getPokemonDetail() returns success THEN API call is successful and WHEN loads next page THEN success`() =
        runTest {
            // given
            coEvery { remoteDataSource.getPokemonDetail(any()) } returns
                MockData.MOCK_POKEMON_SQUIRTLE

            // when
            var result = sut.getPokemonDetail(1)

            // then
            assertEquals(MockData.MOCK_POKEMON_SQUIRTLE, (result.getOrNull()))

            // and when
            result = sut.getPokemonDetail(2)

            // and then
            coVerify(exactly = 1) { remoteDataSource.getPokemonDetail(1) }
            coVerify(exactly = 1) { remoteDataSource.getPokemonDetail(2) }
            assertEquals(MockData.MOCK_POKEMON_SQUIRTLE, result.getOrNull())
        }

    @Test
    fun `WHEN getPokemonDetail() returns null THEN return error`() =
        runTest {
            // given
            coEvery { remoteDataSource.getPokemonDetail(1) } returns null

            // when
            val result = sut.getPokemonDetail(1)

            // then
            coVerify { remoteDataSource.getPokemonDetail(1) }
            assertThrows<NullPointerException> {
                result.getOrThrow()
            }
        }

    @Test
    fun `WHEN getPokemonDetail() fails THEN return error`() =
        runTest {
            // given
            val expectedException = IOException("Network error")
            coEvery { remoteDataSource.getPokemonDetail(1) } throws expectedException

            // when
            val result = sut.getPokemonDetail(1)

            // then
            coVerify { remoteDataSource.getPokemonDetail(FIRST_PAGE) }
            assertThrows<IOException> {
                result.getOrThrow()
            }
        }
}
