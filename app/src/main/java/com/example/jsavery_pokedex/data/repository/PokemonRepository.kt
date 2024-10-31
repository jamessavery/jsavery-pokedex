package com.example.jsavery_pokedex.data.repository

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.datasource.PokemonDataSourceImpl
import com.example.jsavery_pokedex.data.model.PokemonResponse

import com.example.jsavery_pokedex.domain.util.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonRepositoryImpl(
    private val remoteDataSource: PokemonDataSource = PokemonDataSourceImpl()
) : PokemonRepository {

    override fun getPokemonList(nextPage: Int): Flow<Result<PokemonResponse>> = flow {
        try {
            remoteDataSource.getPokemonList(nextPage)?.let { response ->
                emit(Result.Success(response))
            } ?: emit(Result.Error(NullPointerException()))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(Result.Error(e))
        }
    }
}

interface PokemonRepository {
    fun getPokemonList(nextPage: Int): Flow<Result<PokemonResponse>>
}