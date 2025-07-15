package com.example.jsavery_pokedex.services

import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("page") page: Int,
        @Query("type") type: List<String>,
    ): Response<PokemonResponse>

    @GET("id")
    suspend fun getPokemonDetail(@Query("id") id: Int): Response<Pokemon>
}
