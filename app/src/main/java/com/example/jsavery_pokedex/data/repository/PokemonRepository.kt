package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonDataSource,
) : PokemonRepository {
    override suspend fun getPokemonList(nextPage: Int): Result<PokemonResponse> =
        runCatching {
            val response = remoteDataSource.getPokemonList(nextPage)
            response ?: throw NullPointerException("Response is null")
        }

    override suspend fun getPokemonDetail(id: Int): Result<Pokemon> =
        runCatching {
            val response = remoteDataSource.getPokemonDetail(id)
            response ?: throw NullPointerException("Response is null")
        }
}

interface PokemonRepository {
    suspend fun getPokemonList(nextPage: Int): Result<PokemonResponse>

    suspend fun getPokemonDetail(id: Int): Result<Pokemon>
}
