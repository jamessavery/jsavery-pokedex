package com.example.jsavery_pokedex.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.data.model.EvolutionDetail
import com.example.jsavery_pokedex.data.model.Pokemon
import com.example.jsavery_pokedex.domain.util.processPokedexId
import com.example.jsavery_pokedex.mock.MockData
import com.example.jsavery_pokedex.presentation.navigation.GlobalNavigation
import com.example.jsavery_pokedex.presentation.navigation.PokemonDetails
import com.example.jsavery_pokedex.presentation.ui.components.TypesItem
import com.example.jsavery_pokedex.presentation.ui.components.details.AnthropometryItem
import com.example.jsavery_pokedex.presentation.ui.components.details.EvolutionTimeline
import com.example.jsavery_pokedex.presentation.ui.components.details.PokemonIdTabs
import com.example.jsavery_pokedex.presentation.ui.components.progress.SpinningPokeballProgress
import com.example.jsavery_pokedex.presentation.viewmodel.DetailsViewModel
import com.example.jsavery_pokedex.presentation.viewmodel.PokemonDetailsUiState

@Composable
fun PokemonDetailsScreen(pokemonId: PokemonDetails) {
    val detailsViewModel: DetailsViewModel = hiltViewModel(
        key = pokemonId.id.toString(),
    )
    val uiState by detailsViewModel.detailsUiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val currentPokemonId by rememberUpdatedState(pokemonId)

    LaunchedEffect(currentPokemonId, lifecycleOwner) {
        detailsViewModel.getPokemonDetail(pokemonId.id)
    }

    PokemonDetailsScreenState(
        uiState = uiState,
        getEvolutionDetails = {
            detailsViewModel.getPokemonEvolutionDetails(it).getOrNull()
        },
    )
}

@Composable
fun PokemonDetailsScreenState(
    uiState: PokemonDetailsUiState,
    getEvolutionDetails: (Int) -> List<EvolutionDetail>?,
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SpinningPokeballProgress()
        }
    } else if (uiState.pokemon != null) {
        PokemonDetailsContent(
            uiState.pokemon,
            getEvolutionDetails = getEvolutionDetails,
        )
    } else {
        // TODO errors Issue-14
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailsContent(pokemon: Pokemon, getEvolutionDetails: (Int) -> List<EvolutionDetail>?) {
    val horizontalPadding = dimensionResource(R.dimen.details_horizontal_padding)
    val statStartPadding = dimensionResource(R.dimen.details_stat_start_padding)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.pokemon_details)) },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (GlobalNavigation.canGoBack()) GlobalNavigation.goBack()
                    },
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        null,
                    )
                }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            // PokemonId tabs
            PokemonIdTabs(pokemon.id)

            // Pokemon image
            AsyncImage(
                model = pokemon.images.full,
                contentDescription = pokemon.name,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontalPadding)
                    .background(Color.LightGray),
                contentScale = ContentScale.Fit,
            )

            // Pokemon name & id
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
            ) {
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = pokemon.id.processPokedexId(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            }

            // Overview Section
            Text(
                text = stringResource(R.string.pokemon_details_overview_title),
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp),
            )
            Text(
                text = pokemon.description,
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )

            // Weight, height, typing
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                AnthropometryItem(
                    title = stringResource(R.string.pokemon_details_height_title),
                    value = "${pokemon.heightInM}${
                        stringResource(
                            R.string.pokemon_details_m_label,
                        )
                    }",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(80.dp)
                        .width(2.dp),
                    color = Color.LightGray,
                )
                AnthropometryItem(
                    title = stringResource(R.string.pokemon_details_weight_title),
                    value = "${pokemon.weightInKg}${
                        stringResource(
                            R.string.pokemon_details_kg_label,
                        )
                    }",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = statStartPadding),
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(80.dp)
                        .width(2.dp),
                    color = Color.LightGray,
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = statStartPadding),
                ) {
                    Text(
                        text = stringResource(R.string.pokemon_details_type_title),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray,
                    )
                    pokemon.types.forEach { type -> TypesItem(type) }
                }
            }

            // Only show evolutions for evolvable Pokemon
            getEvolutionDetails(pokemon.id)?.takeIf {
                it.isNotEmpty()
            }?.let {
                EvolutionTimeline(
                    evolutions = it,
                    currentPokemonId = pokemon.id,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonDetailsScreenPreview() {
    PokemonDetailsScreenState(
        uiState = PokemonDetailsUiState(
            isLoading = false,
            pokemon = MockData.MOCK_POKEMON_BULBASAUR,
        ),
        {
            listOf(
                EvolutionDetail(1, "", ""),
                EvolutionDetail(2, "", ""),
                EvolutionDetail(3, "", ""),
            )
        },
    )
}
