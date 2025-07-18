package com.example.jsavery_pokedex.data.datasource

import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse

interface PokemonDataSource {
    suspend fun getPokemonList(nextPage: Int, type: List<String>): PokemonResponse?

    suspend fun getPokemonDetail(id: Int): Pokemon?
}
