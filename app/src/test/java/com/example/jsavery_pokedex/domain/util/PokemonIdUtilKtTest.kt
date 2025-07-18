package com.example.jsavery_pokedex.domain.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PokemonIdUtilKtTest {

    @ParameterizedTest
    @CsvSource(
        "1, '#0001'",
        "10, '#0010'",
        "100, '#0100'",
        "1000, '#1000'",
        "25, '#0025'",
        "150, '#0150'",
        "9999, '#9999'",
        "0, '#0000'",
    )
    fun `should format various Pokemon IDs correctly`(input: Int, expected: String) {
        assertEquals(expected, input.processPokedexId())
    }
}
