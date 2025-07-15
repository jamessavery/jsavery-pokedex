package com.example.jsavery_pokedex.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jsavery_pokedex.R

enum class Type {
    BUG,
    DARK,
    DRAGON,
    ELECTRIC,
    FAIRY,
    FIGHTING,
    FIRE,
    FLYING,
    GHOST,
    GRASS,
    GROUND,
    ICE,
    NORMAL,
    POISON,
    PSYCHIC,
    ROCK,
    STEEL,
    WATER,
}

@Composable
fun TypeIcon(
    type: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    size: Dp,
) {
    val iconRes = when (type.lowercase()) {
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

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "$type type",
            modifier = Modifier
                .size(size)
                .align(Alignment.Center),
        )

        // Selection indicator overlay
        if (isSelected) {
            val overlaySize = size * 0.7f
            val iconSize = size * 0.6f

            Box(
                modifier = Modifier
                    .size(overlaySize)
                    .background(
                        color = Color.White,
                        shape = CircleShape,
                    )
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    colorFilter = ColorFilter.tint(getTypeColor(type)),
                    modifier = Modifier.size(iconSize),
                )
            }
        }
    }
}

@Composable
fun getTypeColor(type: String): Color =
    when (type.lowercase()) {
        Type.NORMAL.name.lowercase() -> Color(0xFFA8A878)
        Type.GRASS.name.lowercase() -> Color(0xFF8EA68D)
        Type.FIRE.name.lowercase() -> Color(0xFFFA9F61)
        Type.WATER.name.lowercase() -> Color(0xFF89F3FA)
        Type.ELECTRIC.name.lowercase() -> Color(0xFFF7DA76)
        Type.ICE.name.lowercase() -> Color(0xFF33D5D5)
        Type.FIGHTING.name.lowercase() -> Color(0xFFD26E31)
        Type.POISON.name.lowercase() -> Color(0xFFCDA0CA)
        Type.GROUND.name.lowercase() -> Color(0xFFD6C67A)
        Type.FLYING.name.lowercase() -> Color(0xFFC1B7DE)
        Type.PSYCHIC.name.lowercase() -> Color(0xFFE9708F)
        Type.BUG.name.lowercase() -> Color(0xFF7E9F58)
        Type.ROCK.name.lowercase() -> Color(0xFFA28C21)
        Type.GHOST.name.lowercase() -> Color(0xFF887A9F)
        Type.DRAGON.name.lowercase() -> Color(0xFFA0494E)
        Type.DARK.name.lowercase() -> Color(0xFF57493F)
        Type.STEEL.name.lowercase() -> Color(0xFFC4C4CC)
        Type.FAIRY.name.lowercase() -> Color(0xFFFFC5D4)
        else -> Color(0xFFA8A878)
    }

@Preview
@Composable
fun TypeIconPreview() {
    TypeIcon(type = "Grass", isSelected = true, size = 30.dp)
}
