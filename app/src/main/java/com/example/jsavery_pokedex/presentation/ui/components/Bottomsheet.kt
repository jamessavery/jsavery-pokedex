package com.example.jsavery_pokedex.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApply: (FilterState) -> Unit,
    currentFilter: FilterState = FilterState()
) {
    var sortOption by remember { mutableStateOf(currentFilter.sortOption) }
    var selectedTypes by remember { mutableStateOf(currentFilter.selectedTypes) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Sort By Section
        Text(
            text = "Sort By",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SortOptions(
            selectedOption = sortOption,
            onOptionSelected = { sortOption = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Filter by Type Section
        Text(
            text = "Filter by Type",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TypeFilterGrid(
            selectedTypes = selectedTypes,
            onTypeToggle = { type ->
                selectedTypes = if (selectedTypes.contains(type)) {
                    selectedTypes - type
                } else {
                    selectedTypes + type
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Apply Button
        Button(
            onClick = {
                onApply(FilterState(sortOption, selectedTypes))
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6B46C1) // Purple color matching design
            )
        ) {
            Text(
                text = "Apply",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun SortOptions(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit
) {
    Column {
        SortOption.values().forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedOption == option) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier.weight(1f)
                )

                if (selectedOption == option) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color(0xFF22C55E) // Green checkmark
                    )
                }
            }
        }
    }
}

@Composable
fun TypeFilterGrid(
    selectedTypes: Set<String>,
    onTypeToggle: (String) -> Unit
) {
    val pokemonTypes = listOf(
        "bug", "dark", "dragon", "electric",
        "fairy", "fighting", "fire", "flying",
        "ghost", "grass", "ground", "ice",
        "normal", "poison", "psychic", "rock",
        "steel", "water"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(pokemonTypes) { type ->
            TypeFilterItem(
                type = type,
                isSelected = selectedTypes.contains(type),
                onToggle = { onTypeToggle(type) }
            )
        }
    }
}

@Composable
fun TypeFilterItem(
    type: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onToggle() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            TypeIcon(
                type = type,
                modifier = Modifier.size(32.dp)
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = Color(0xFF22C55E),
                            shape = CircleShape
                        )
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Text(
            text = type.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// TODO JIMMY move these
data class FilterState(
    val sortOption: SortOption = SortOption.MOST_RECENTLY_ADDED,
    val selectedTypes: Set<String> = emptySet()
)

enum class SortOption(val displayName: String) {
    MOST_RECENTLY_ADDED("Most Recently Added (default)"),
    POKEDEX_NUMBER("Pokédex Number"),
    ALPHABETICAL_A_Z("Alphabetical (A-Z)"),
    ALPHABETICAL_Z_A("Alphabetical (Z-A)")
}

@Preview
@Composable
fun Botasfasf() {
    FilterBottomSheet(
        onDismiss = {},
        onApply = { },
        currentFilter = { FilterState() },
    )
}

