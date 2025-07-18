package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonDataSource,
) : PokemonRepository {
    override suspend fun getPokemonList(
        nextPage: Int,
        selectedTypes: List<String>,
    ): Result<PokemonResponse> =
        runCatching {
            val response = remoteDataSource.getPokemonList(nextPage, selectedTypes)
            response ?: throw NullPointerException("Response is null")
        }

    override suspend fun getPokemonDetail(id: Int): Result<Pokemon> =
        runCatching {
            val response = remoteDataSource.getPokemonDetail(id)
            response ?: throw NullPointerException("Response is null")
        }
}

interface PokemonRepository {
    suspend fun getPokemonList(
        nextPage: Int,
        selectedTypes: List<String> = emptyList<String>(),
    ): Result<PokemonResponse>

    suspend fun getPokemonDetail(id: Int): Result<Pokemon>
}
