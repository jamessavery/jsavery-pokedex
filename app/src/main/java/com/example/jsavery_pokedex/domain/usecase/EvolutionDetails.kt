package com.example.jsavery_pokedex.domain.usecase

import com.example.jsavery_pokedex.data.model.EvolutionDetail
import com.example.jsavery_pokedex.domain.exception.PokemonException
import com.example.jsavery_pokedex.domain.manager.PokemonListManager
import javax.inject.Inject

class EvolutionDetails @Inject constructor(
    private val pokemonListManager: PokemonListManager,
) {

    operator fun invoke(id: Int): Result<List<EvolutionDetail>> {
        val pokemon = pokemonListManager.getPokemonById(id)
            ?: return Result.failure(PokemonException.NotFound(id))

        val evolutionDetails = pokemon.evolutions.mapNotNull { evolutionId ->
            pokemonListManager.getPokemonById(evolutionId)?.let { evolution ->
                EvolutionDetail(
                    id = evolution.id,
                    name = evolution.name,
                    fullImage = evolution.images.full,
                )
            }
        }

        return Result.success(evolutionDetails)
    }
}
