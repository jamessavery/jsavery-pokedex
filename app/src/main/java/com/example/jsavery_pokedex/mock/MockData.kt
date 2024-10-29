package com.example.jsavery_pokedex.mock

import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.data.model.PokemonImages

class MockData {
    companion object {
        private val MOCK_POKEMON_BULBASAUR = Pokemon(
            id = 1,
            name = "Bulbasaur",
            abilities = listOf("Overgrow", "Chlorophyll"),
            weightInKg = 6.9,
            heightInM = 0.7,
            types = listOf("Grass", "Poison"),
            evolutions = listOf(2, 3),
            description = "A strange seed was planted on its back at birth. The plant sprouts and grows with this Pokémon.",
            images = PokemonImages(
                full = "https://example.com/bulbasaur_full.png",
                spriteFront = "https://example.com/bulbasaur_front.png",
                spriteBack = "https://example.com/bulbasaur_back.png"
            )
        )
        private val MOCK_POKEMON_CHARMANDER = Pokemon(
            id = 4,
            name = "Charmander",
            abilities = listOf("Blaze", "Solar Power"),
            weightInKg = 8.5,
            heightInM = 0.6,
            types = listOf("Fire"),
            evolutions = listOf(5, 6),
            description = "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.",
            images = PokemonImages(
                full = "https://example.com/charmander_full.png",
                spriteFront = "https://example.com/charmander_front.png",
                spriteBack = "https://example.com/charmander_back.png"
            )
        )
        val MOCK_POKEMON_SQUIRTLE = Pokemon(
            id = 7,
            name = "Squirtle",
            abilities = listOf("Torrent", "Rain Dish"),
            weightInKg = 9.0,
            heightInM = 0.5,
            types = listOf("Water"),
            evolutions = listOf(8, 9),
            description = "After birth, its back swells and hardens into a shell. It powerfully sprays foam from its mouth.",
            images = PokemonImages(
                full = "https://example.com/squirtle_full.png",
                spriteFront = "https://example.com/squirtle_front.png",
                spriteBack = "https://example.com/squirtle_back.png"
            )
        )

        val MOCK_POKEMON_RESPONSE = listOf(
            MOCK_POKEMON_BULBASAUR,
            MOCK_POKEMON_CHARMANDER,
            MOCK_POKEMON_SQUIRTLE
        )
    }
}