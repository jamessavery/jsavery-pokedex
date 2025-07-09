package com.example.jsavery_pokedex.domain.manager

import com.example.jsavery_pokedex.presentation.screens.FilterState
import com.example.jsavery_pokedex.presentation.screens.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PokemonFilterManager {

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

//    private val _showFilterSheet = MutableStateFlow(false)
//    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    fun updateFilter(newFilter: FilterState) {
        _filterState.value = newFilter
    }

//    fun showFilterSheet() {
//        _showFilterSheet.value = true
//    }
//
//    fun hideFilterSheet() {
//        _showFilterSheet.value = false
//    }

    fun clearFilters() {
        _filterState.value = FilterState()
    }

    fun updateSortOption(sortOption: SortOption) {
        _filterState.value = _filterState.value.copy(sortOption = sortOption)
    }

    fun toggleTypeFilter(type: String) {
        val currentTypes = _filterState.value.selectedTypes
        val newTypes = if (currentTypes.contains(type)) {
            currentTypes - type
        } else {
            currentTypes + type
        }
        _filterState.value = _filterState.value.copy(selectedTypes = newTypes)
    }
}
