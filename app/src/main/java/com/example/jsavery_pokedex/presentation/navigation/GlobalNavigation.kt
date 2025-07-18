package com.example.jsavery_pokedex.presentation.navigation

import androidx.navigation3.runtime.NavBackStack

object GlobalNavigation {

    private var backStackReference: NavBackStack? = null
    private var isInit = false

    fun initialize(backStack: NavBackStack) {
        backStackReference = backStack
        isInit = true
    }

    fun launchPokemonDetailsScreen(id: Int) {
        if (!isInit) return
        backStackReference?.add(PokemonDetails(id))
    }

    fun canGoBack(): Boolean =
        backStackReference?.size?.let { it > 1 } == true

    fun goBack() {
        if (!isInit || !canGoBack()) return
        backStackReference?.removeLastOrNull()
    }
}
