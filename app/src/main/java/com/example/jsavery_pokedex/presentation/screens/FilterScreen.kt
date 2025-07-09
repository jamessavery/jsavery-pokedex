package com.example.jsavery_pokedex.presentation.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.presentation.ui.components.Type
import com.example.jsavery_pokedex.presentation.ui.components.TypeIcon
import com.example.jsavery_pokedex.presentation.ui.theme.PokePurple
import com.example.jsavery_pokedex.presentation.ui.theme.SelectedGreen

@Composable
fun FilterScreen(
    onDismiss: () -> Unit,
    onApply: (FilterState) -> Unit,
    currentFilter: FilterState = FilterState(),
) {
    var sortOption by remember { mutableStateOf(currentFilter.sortOption) }
    var selectedTypes by remember { mutableStateOf(currentFilter.selectedTypes) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(R.dimen.details_horizontal_padding)),
    ) {
        // Sort By Section
        Text(
            text = "Sort By",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        SortOptions(
            selectedOption = sortOption,
            onOptionSelected = { sortOption = it },
        )

        HorizontalDivider(color = Color.LightGray)

        // Filter by Type Section
        Text(
            text = "Filter by Type",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(bottom = 16.dp)
        )

        TypeFilterGrid(
            selectedTypes = selectedTypes,
            onTypeToggle = { type ->
                selectedTypes = if (selectedTypes.contains(type)) {
                    selectedTypes - type
                } else {
                    selectedTypes + type
                }
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Apply Button
        Button(
            onClick = {
                onApply(FilterState(sortOption, selectedTypes))
                onDismiss()
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
                text = "Apply",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun SortOptions(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit,
) {
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
                    text = option.displayName,
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
                        contentDescription = "Selected",
                        tint = SelectedGreen,
                    )
                }
            }
        }
    }
}

@Composable
fun TypeFilterGrid(
    selectedTypes: Set<String>,
    onTypeToggle: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp),
    ) {
        items(Type.entries.toTypedArray()) {
            val type = it.toString()
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
                .size(80.dp)
                .background(
                    color = if (isSelected)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else
                        Color.Transparent,
                    shape = CircleShape,
                )
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            TypeIcon(
                type = type,
                modifier = Modifier.size(80.dp),
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

// TODO JIMMY move these
data class FilterState(
    val sortOption: SortOption = SortOption.POKEDEX_NUMBER,
    val selectedTypes: Set<String> = emptySet(),
)

enum class SortOption(val displayName: String) {
    POKEDEX_NUMBER("Pokédex Number (default)"),
    ALPHABETICAL_A_Z("Alphabetical (A-Z)"),
    ALPHABETICAL_Z_A("Alphabetical (Z-A)")
}

@Preview(showBackground = true)
@Composable
fun FilterContentPreview() {
    FilterScreen(
        onDismiss = {},
        onApply = { },
        currentFilter = FilterState(),
    )
}

