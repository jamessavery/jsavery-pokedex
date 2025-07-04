package com.example.jsavery_pokedex.data.datasource

import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.services.PokemonService
import javax.inject.Inject
import retrofit2.HttpException

class PokemonDataSourceImpl @Inject constructor(
    private val pokeService: PokemonService,
) : PokemonDataSource {
    override suspend fun getPokemonList(nextPage: Int): PokemonResponse? {
        val response = pokeService.getPokemonList(nextPage)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getPokemonDetail(id: Int): Pokemon? {
        val response = pokeService.getPokemonDetail(id)
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw HttpException(response)
        }
    }
}

interface PokemonDataSource {
    suspend fun getPokemonList(nextPage: Int): PokemonResponse?

    suspend fun getPokemonDetail(id: Int): Pokemon?
}
