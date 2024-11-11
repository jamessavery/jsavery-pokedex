package com.example.jsavery_pokedex.data.mapper


import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.EvolutionDetail

fun Pokemon.mapToEvolutionItem(): EvolutionDetail {
    return EvolutionDetail(
        id = this.id,
        name = this.name,
        fullImage = images.full
    )
}