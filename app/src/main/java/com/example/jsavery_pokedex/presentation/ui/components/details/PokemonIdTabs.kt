package com.example.jsavery_pokedex.presentation.ui.components.details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jsavery_pokedex.domain.util.processPokedexId

@Composable
fun PokemonIdTabs(currentPokemonId: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        val numbers =
            when (currentPokemonId) {
                1 -> listOf(1, 2, 3)
                1025 -> listOf(1023, 1024, 1025)
                2 -> listOf(1, 2, 3)
                else -> listOf(currentPokemonId - 1, currentPokemonId, currentPokemonId + 1)
            }.filter { it in 1..1025 }

        numbers.forEach { number ->
            Tab(
                // TODO complete impl issue-10
                selected = number == currentPokemonId,
                onClick = {},
                text = { Text(text = number.processPokedexId()) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}
