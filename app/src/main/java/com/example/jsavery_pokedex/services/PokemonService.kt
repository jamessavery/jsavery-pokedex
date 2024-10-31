package com.example.jsavery_pokedex.services

import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.presentation.MainViewModel.Companion.FIRST_PAGE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("page") page: Int
    ): Response<PokemonResponse>
}