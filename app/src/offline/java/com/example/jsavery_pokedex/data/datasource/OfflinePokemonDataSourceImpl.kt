package com.example.jsavery_pokedex.data.datasource

import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonResponse
import com.example.jsavery_pokedex.domain.util.JsonUtils
import com.example.jsavery_pokedex.mock.MockData
import javax.inject.Inject

class OfflinePokemonDataSourceImpl @Inject constructor(
    private val jsonUtils: JsonUtils,
) : PokemonDataSource {
    override suspend fun getPokemonList(nextPage: Int, type: List<String>): PokemonResponse? {
        val parsedJson: List<Pokemon>? = jsonUtils.parsePokemonListJson("pokemon.json")
        return PokemonResponse(
            count = 50,
            next = null,
            previous = null,
            data = parsedJson ?: emptyList(),
        )
    }

    override suspend fun getPokemonDetail(id: Int): Pokemon? =
        MockData.MOCK_POKEMON_BULBASAUR
}
