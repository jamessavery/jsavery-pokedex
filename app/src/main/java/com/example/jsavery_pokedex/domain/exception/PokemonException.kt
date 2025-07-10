package com.example.jsavery_pokedex.domain.exception

sealed class PokemonException(
    message: String,
) : Exception(message) {
    class NotFound(
        id: Int,
    ) : PokemonException("Pokemon $id not found")
}
