package com.example.jsavery_pokedex.data.datasource

import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.services.CoreNetworkService
import com.example.jsavery_pokedex.services.PokemonService
import retrofit2.HttpException

class PokemonDataSourceImpl(
    private val pokeService: PokemonService = CoreNetworkService.getInstance().pokemonApi
) : PokemonDataSource {

    override suspend fun getPokemonList(): PokemonResponse? {
        val response = pokeService.getPokemonList()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw HttpException(response)
        }
    }
}

interface PokemonDataSource {
    suspend fun getPokemonList(): PokemonResponse?
}