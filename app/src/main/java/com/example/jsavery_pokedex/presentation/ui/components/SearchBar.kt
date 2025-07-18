package com.example.jsavery_pokedex.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jsavery_pokedex.R
import com.example.jsavery_pokedex.domain.manager.PokemonFilterManager
import com.example.jsavery_pokedex.presentation.ui.theme.PokePurple
import com.example.jsavery_pokedex.presentation.ui.theme.PokedexTheme

@Composable
fun PokedexSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    filterManager: PokemonFilterManager,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .statusBarsPadding(),
    ) {
        Text(
            text = stringResource(R.string.search_bar_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 25.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val talkback = stringResource(R.string.a11y_search_field)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(0.85f)
                    .semantics {
                        contentDescription = talkback
                    },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_bar_text),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = PokePurple,
                        modifier = Modifier.size(25.dp),
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedBorderColor = PokePurple,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
            )

            OutlinedIconButton(
                onClick = {
                    filterManager.showFilterSheet()
                },
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                ),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = PokePurple,
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = stringResource(
                        R.string.a11y_active_filters,
                        filterManager.getActiveFilters(),
                    ),
                    tint = PokePurple,
                    modifier = Modifier.size(45.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    PokedexTheme(darkTheme = false) {
        PokedexSearchBar(
            searchQuery = "",
            onSearchQueryChange = {},
            filterManager = PokemonFilterManager(),
        )
    }
}
