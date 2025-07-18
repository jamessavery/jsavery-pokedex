package com.example.jsavery_pokedex.domain.di

import com.example.jsavery_pokedex.data.datasource.OfflinePokemonDataSourceImpl
import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindPokemonDataSource(impl: OfflinePokemonDataSourceImpl): PokemonDataSource
}
