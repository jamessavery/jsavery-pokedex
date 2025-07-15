package com.example.jsavery_pokedex.presentation.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.domain.manager.PokemonFilterManager
import com.example.jsavery_pokedex.presentation.ui.components.Type
import com.example.jsavery_pokedex.presentation.ui.components.TypeIcon
import com.example.jsavery_pokedex.presentation.ui.theme.PokePurple
import com.example.jsavery_pokedex.presentation.ui.theme.SelectedGreen

@Composable
fun FilterScreen(filterManager: PokemonFilterManager) {
    val currentFilter by filterManager.filterState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(R.dimen.details_horizontal_padding)),
    ) {
        HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(bottom = 16.dp))

        // Sort by Section
        Text(
            text = stringResource(R.string.filter_sort_by_title),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                vertical = dimensionResource(R.dimen.filters_vertical_padding),
            ),
        )

        SortOptions(
            selectedOption = currentFilter.sortOption,
            onOptionSelected = {
                filterManager.updateSortOption(it)
            },
        )

        HorizontalDivider(
            color = Color.LightGray,
            modifier = Modifier.padding(
                vertical = dimensionResource(R.dimen.filters_vertical_padding),
            ),
        )

        // Filter by Type Section
        Text(
            text = stringResource(R.string.filter_by_type_title),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                vertical = dimensionResource(R.dimen.filters_vertical_padding),
            ),
        )

        TypeFilterGrid(
            selectedTypes = currentFilter.selectedTypes,
            onTypeToggle = { type ->
                filterManager.toggleTypeFilter(type)
            },
        )

        // Apply Button
        Button(
            onClick = {
                filterManager.hideFilterSheet()
            },
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PokePurple,
            ),
        ) {
            Text(
                text = stringResource(R.string.apply),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun SortOptions(selectedOption: SortOption, onOptionSelected: (SortOption) -> Unit) {
    Column {
        SortOption.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = option.getDisplayName(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedOption == option) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
                    modifier = Modifier.weight(1f),
                )

                if (selectedOption == option) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.a11y_selected),
                        tint = SelectedGreen,
                    )
                }
            }
        }
    }
}

@Composable
fun TypeFilterGrid(selectedTypes: List<String>, onTypeToggle: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
    ) {
        items(Type.entries.toTypedArray()) {
            val type = it.toString().uppercase()
            TypeFilterItem(
                type = type.lowercase().replaceFirstChar { it.uppercase() },
                isSelected = selectedTypes.contains(type),
                onToggle = { onTypeToggle(type) },
            )
        }
    }
}

@Composable
fun TypeFilterItem(
    type: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onToggle() }
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape,
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            TypeIcon(
                type = type,
                isSelected = isSelected,
                size = 60.dp,
            )
        }

        Text(
            text = type.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

enum class SortOption(
    @StringRes val displayNameRes: Int,
    val previewDisplayName: String,
) {
    POKEDEX_NUMBER(R.string.sort_option_pokedex_number, "Pokedex Number"),
    ALPHABETICAL_A_Z(R.string.sort_option_alphabetical_a_z, "Alphabetical (A-Z)"),
    ALPHABETICAL_Z_A(R.string.sort_option_alphabetical_z_a, "Alphabetical (Z-A)"),
    ;

    @Composable
    fun getDisplayName(): String =
        if (LocalInspectionMode.current) {
            previewDisplayName // Return constants if @Preview
        } else {
            stringResource(displayNameRes)
        }
}

@Preview(showBackground = true)
@Composable
fun FilterContentPreview() {
    val filterManager = PokemonFilterManager()
    filterManager.toggleTypeFilter(Type.FIRE.name)

    FilterScreen(
        filterManager = filterManager,
    )
}
