package com.example.jsavery_pokedex.data.mapper

import com.example.jsavery_pokedex.data.model.EvolutionDetail
import com.example.jsavery_pokedex.data.model.Pokemon

fun Pokemon.mapToEvolutionItem(): EvolutionDetail = EvolutionDetail(id = this.id, name = this.name, fullImage = images.full)
