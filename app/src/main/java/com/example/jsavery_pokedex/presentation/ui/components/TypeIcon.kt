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

enum class Type {
    BUG, DARK, DRAGON, ELECTRIC,
    FAIRY, FIGHTING, FIRE, FLYING,
    GHOST, GRASS, GROUND, ICE,
    NORMAL, POISON, PSYCHIC, ROCK,
    STEEL, WATER
}

@Composable
fun TypeIcon(type: String, modifier: Modifier = Modifier) {
    val iconRes =
        when (type.lowercase()) {
            Type.NORMAL.name.lowercase() -> R.drawable.type_normal
            Type.GRASS.name.lowercase() -> R.drawable.type_grass
            Type.FIRE.name.lowercase() -> R.drawable.type_fire
            Type.WATER.name.lowercase() -> R.drawable.type_water
            Type.ELECTRIC.name.lowercase() -> R.drawable.type_electric
            Type.ICE.name.lowercase() -> R.drawable.type_ice
            Type.FIGHTING.name.lowercase() -> R.drawable.type_fighting
            Type.POISON.name.lowercase() -> R.drawable.type_poison
            Type.GROUND.name.lowercase() -> R.drawable.type_ground
            Type.FLYING.name.lowercase() -> R.drawable.type_flying
            Type.PSYCHIC.name.lowercase() -> R.drawable.type_psychic
            Type.BUG.name.lowercase() -> R.drawable.type_bug
            Type.ROCK.name.lowercase() -> R.drawable.type_rock
            Type.GHOST.name.lowercase() -> R.drawable.type_ghost
            Type.DRAGON.name.lowercase() -> R.drawable.type_dragon
            Type.DARK.name.lowercase() -> R.drawable.type_dark
            Type.STEEL.name.lowercase() -> R.drawable.type_steel
            Type.FAIRY.name.lowercase() -> R.drawable.type_fairy
            else -> R.drawable.type_normal
        }

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = "$type type",
        modifier = modifier
            .size(24.dp)
            .padding(4.dp),
    )
}

@Preview
@Composable
fun TypeIconPreview() {
    TypeIcon(type = "Grass")
}
