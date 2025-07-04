package com.example.jsavery_pokedex.presentation.ui.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.data.model.EvolutionDetail
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.domain.util.processPokedexId
import com.example.jsavery_pokedex.presentation.ui.theme.LightGray

@Composable
fun EvolutionChainItem(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
    getEvolutionDetail: (Int) -> EvolutionDetail?,
) {
    Column(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = stringResource(R.string.pokemon_details_evolutions_title),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO Issue-35: Is in a bugged state
            // TODO Rather than returning an Object, perhaps make reactive..?
            pokemon.evolutions.forEachIndexed { index, evolvedPokemonId ->
                var evolutionPokemon by remember {
                    mutableStateOf<EvolutionDetail>(EvolutionDetail(1, "Error", "fullImage"))
                }

                LaunchedEffect(evolvedPokemonId) {
                    evolutionPokemon =
                        getEvolutionDetail(evolvedPokemonId)
                            ?: EvolutionDetail(1, "Error2", "fullImage2")
                }

                EvolutionItem(
                    number = index + 1,
                    modifier = Modifier.fillMaxWidth(),
                    evolutionDetail = evolutionPokemon,
                )

                if (index < pokemon.evolutions.size - 1) {
                    VerticalDivider(
                        modifier = Modifier.height(40.dp).width(2.dp),
                        color = Color.LightGray,
                    )
                }
            }
        }
    }
}

@Composable
private fun EvolutionItem(
    number: Int,
    modifier: Modifier = Modifier,
    evolutionDetail: EvolutionDetail,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(24.dp).background(color = LightGray, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
            )
        }

        AsyncImage(
            model = evolutionDetail,
            contentDescription = evolutionDetail.name,
            modifier = Modifier.size(80.dp).padding(vertical = 8.dp),
            error = painterResource(id = R.drawable.ic_launcher_background),
        )

        evolutionDetail.let {
            Text(
                text = it.name.replaceFirstChar { char -> char.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = it.id.processPokedexId(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
            )
        }
    }
}
