package com.example.jsavery_pokedex.domain.util

import android.content.Context
import com.example.jsavery_pokedex.data.model.Pokemon
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

class JsonUtils @Inject constructor(
    private val context: Context,
    moshi: Moshi,
) {

    private val pokemonListAdapter: JsonAdapter<List<Pokemon>> = moshi.adapter(
        Types.newParameterizedType(List::class.java, Pokemon::class.java),
    )

    fun parsePokemonListJson(fileName: String) =
        try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            pokemonListAdapter.fromJson(jsonString)
        } catch (e: Exception) {
            println("Failed to parse $e")
            null
        }
}
