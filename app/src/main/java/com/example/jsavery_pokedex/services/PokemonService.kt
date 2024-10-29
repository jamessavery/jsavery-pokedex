package com.example.jsavery_pokedex.services

import com.example.jsavery_pokedex.data.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET

interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemonList(): Response<PokemonResponse>
}
