package com.example.jsavery_pokedex.presentation.ui.components.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.data.model.EvolutionDetail
import com.example.jsavery_pokedex.domain.util.processPokedexId
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.presentation.navigation.GlobalNavigation
import com.example.jsavery_pokedex.presentation.ui.theme.PokePurple

@Composable
fun EvolutionTimeline(
    evolutions: List<EvolutionDetail>,
    currentPokemonId: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Text(
            text = stringResource(R.string.pokemon_details_evolutions_title),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.details_horizontal_padding),
            ),
        )

        evolutions.forEachIndexed { index, evolutionDetail ->
            val isCurrent = currentPokemonId == evolutionDetail.id
            EvolutionItem(
                isCurrent = isCurrent,
                currentIndex = index,
                totalEvolutions = evolutions.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(R.dimen.details_horizontal_padding))
                    .clickable {
                        if (!isCurrent) {
                            GlobalNavigation.launchPokemonDetailsScreen(evolutionDetail.id)
                        }
                    },
                evolutionDetail = evolutionDetail,
            )
        }
    }
}

@Composable
private fun EvolutionItem(
    isCurrent: Boolean,
    currentIndex: Int,
    totalEvolutions: Int,
    modifier: Modifier = Modifier,
    evolutionDetail: EvolutionDetail,
) {
    val itemState = remember(currentIndex, totalEvolutions) {
        EvolutionItemState(
            isFirst = currentIndex == 0,
            isLast = currentIndex == totalEvolutions - 1,
            evolutionNumber = currentIndex + 1,
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top,
    ) {
        TimelineIndicator(
            isSelected = isCurrent,
            number = currentIndex + 1,
            isFirst = itemState.isFirst,
            isLast = itemState.isLast,
            modifier = Modifier.fillMaxHeight(),
        )

        // Right side image & Pokedex entry
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = evolutionDetail.fullImage,
                contentDescription = evolutionDetail.name,
                modifier = Modifier
                    .size(120.dp)
                    .padding(vertical = 8.dp),
                error = painterResource(id = R.drawable.ic_launcher_background),
            )
        }

        Column(
            modifier = Modifier
                .weight(2f)
                .padding(start = 10.dp, top = 10.dp)
                .height(80.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = evolutionDetail.name.replaceFirstChar { char -> char.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp),
            )

            Text(
                text = evolutionDetail.id.processPokedexId(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Illustration on the left side, depicting evolution stages in the form of a line & numbered circles
 */
@Composable
private fun TimelineIndicator(
    isSelected: Boolean,
    number: Int,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .width(40.dp)
            .height(120.dp),
    ) {
        val centerY = maxHeight / 2

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawTimelineElements(
                        isSelected = isSelected,
                        isFirst = isFirst,
                        isLast = isLast,
                        canvasSize = size,
                    )
                },
        ) {
            // Number within drawn circle
            Box(
                modifier = Modifier
                    .offset(x = 3.dp, y = centerY - 12.dp)
                    .size(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number.toString(),
                    color = if (isSelected) Color.White else DarkGray,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

private fun DrawScope.drawTimelineElements(
    isSelected: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    canvasSize: Size,
) {
    val circleRadius = 12.dp.toPx()
    val circleX = 15.dp.toPx()
    val circleY = canvasSize.height / 2
    val lineStrokeWidth = 2.dp.toPx()
    val circleStrokeWidth = 2.dp.toPx()

    val minRequiredHeight = circleY + circleRadius + 20.dp.toPx()
    val actualHeight = maxOf(canvasSize.height, minRequiredHeight)

    // Connecting lines
    if (!isFirst) {
        drawLine(
            color = Color.LightGray,
            start = Offset(circleX, 0f),
            end = Offset(circleX, circleY - circleRadius),
            strokeWidth = lineStrokeWidth,
        )
    }
    if (!isLast) {
        drawLine(
            color = Color.LightGray,
            start = Offset(circleX, circleY + circleRadius),
            end = Offset(circleX, actualHeight),
            strokeWidth = lineStrokeWidth,
        )
    }

    // Circle fill
    drawCircle(
        color = if (isSelected) PokePurple else Color.White,
        radius = circleRadius,
        center = Offset(circleX, circleY),
    )

    // Circle stroke
    drawCircle(
        color = if (isSelected) PokePurple else Color.LightGray,
        radius = circleRadius,
        center = Offset(circleX, circleY),
        style = Stroke(width = circleStrokeWidth),
    )
}

@Stable
data class EvolutionItemState(
    val isFirst: Boolean,
    val isLast: Boolean,
    val evolutionNumber: Int,
)

@Preview(showBackground = true)
@Composable
fun PreviewEvolutions() {
    EvolutionTimeline(
        evolutions = listOf(
            MockData.MOCK_EVOLUTION_DETAIL,
            MockData.MOCK_EVOLUTION_DETAIL.copy(id = 2),
            MockData.MOCK_EVOLUTION_DETAIL.copy(id = 3),
        ),
        currentPokemonId = 1,
        modifier = Modifier,
    )
}
