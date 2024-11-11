package com.example.jsavery_pokedex.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jsavery_pokedex.R

@Composable
fun TypeIcon(
    type: String,
    modifier: Modifier = Modifier
) {
    val iconRes = when (type.lowercase()) {
        "normal" -> R.drawable.type_normal
        "grass" -> R.drawable.type_grass
        "fire" -> R.drawable.type_fire
        "water" -> R.drawable.type_water
        "electric" -> R.drawable.type_electric
        "ice" -> R.drawable.type_ice
        "fighting" -> R.drawable.type_fighting
        "poison" -> R.drawable.type_poison
        "ground" -> R.drawable.type_ground
        "flying" -> R.drawable.type_flying
        "psychic" -> R.drawable.type_psychic
        "bug" -> R.drawable.type_bug
        "rock" -> R.drawable.type_rock
        "ghost" -> R.drawable.type_ghost
        "dragon" -> R.drawable.type_dragon
        "dark" -> R.drawable.type_dark
        "steel" -> R.drawable.type_steel
        "fairy" -> R.drawable.type_fairy
        else -> R.drawable.type_normal
    }

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = "$type type",
        modifier = modifier
            .size(24.dp)
            .padding(4.dp)
    )
}

@Preview
@Composable
fun TypeIconPreview() {
    TypeIcon(
        type = "Grass"
    )
}