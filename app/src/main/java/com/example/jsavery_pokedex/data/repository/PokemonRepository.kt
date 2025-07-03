package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class PokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonDataSource,
) : PokemonRepository {
    override suspend fun getPokemonList(nextPage: Int): Result<PokemonResponse> =
        try {
            val response = remoteDataSource.getPokemonList(nextPage)
            if (response != null) {
                Result.success(response)
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }

    override suspend fun getPokemonDetail(id: Int): Result<Pokemon> =
        try {
            val response = remoteDataSource.getPokemonDetail(id)
            if (response != null) {
                Result.success(response)
            } else {
                Result.failure(NullPointerException())
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
}

interface PokemonRepository {
    suspend fun getPokemonList(nextPage: Int): Result<PokemonResponse>

    suspend fun getPokemonDetail(id: Int): Result<Pokemon>
}
