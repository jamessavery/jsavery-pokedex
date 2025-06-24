package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.domain.util.Result
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonDataSource,
) : PokemonRepository {
    override suspend fun getPokemonList(nextPage: Int): Result<PokemonResponse> =
        try {
            val response = remoteDataSource.getPokemonList(nextPage)
            if (response != null) {
                Result.Success(response)
            } else {
                Result.Error(NullPointerException())
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(e)
        }

    override suspend fun getPokemonDetail(id: Int): Result<Pokemon> =
        try {
            val response = remoteDataSource.getPokemonDetail(id)
            if (response != null) {
                Result.Success(response)
            } else {
                Result.Error(NullPointerException())
            }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(e)
        }
}

interface PokemonRepository {
    suspend fun getPokemonList(nextPage: Int): Result<PokemonResponse>

    suspend fun getPokemonDetail(id: Int): Result<Pokemon>
}
