package com.example.jsavery_pokedex.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonResponse(
    @Json(name = "data")
    val data: List<Pokemon>
)

@JsonClass(generateAdapter = true)
data class Pokemon(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "abilities")
    val abilities: List<String>,

    @Json(name = "weight")
    val weightInKg: Double,

    @Json(name = "height")
    val heightInM: Double,

    @Json(name = "types")
    val types: List<String>,

    @Json(name = "evolutions")
    val evolutions: List<Int>,

    @Json(name = "description")
    val description: String,

    @Json(name = "images")
    val images: PokemonImages
)

@JsonClass(generateAdapter = true)
data class PokemonImages(
    @Json(name = "full")
    val full: String,

    @Json(name = "sprite_front")
    val spriteFront: String,

    @Json(name = "sprite_back")
    val spriteBack: String
)