package com.example.jsavery_pokedex.domain.util

fun Int.processPokedexId() = "#${this.toString().padStart(4, '0')}"
