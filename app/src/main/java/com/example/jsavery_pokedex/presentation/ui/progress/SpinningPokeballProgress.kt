package com.example.jsavery_pokedex.presentation.ui.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jsavery_pokedex.R

@Composable
fun SpinningPokeballProgress(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pokeball_spin")

    // Rotation animation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "pokeball_rotation"
    )

    Image(
        painter = painterResource(id = R.drawable.pokeball_progress),
        contentDescription = "Loading",
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = 1f
                scaleY = 1f
                rotationZ = rotation
            },
        contentScale = ContentScale.Fit
    )
}

@Composable
@Preview(showBackground = true)
fun SpinningPokeballPreview() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        SpinningPokeballProgress()
    }
}