package com.example.jsavery_pokedex.domain.di

import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.datasource.PokemonDataSourceImpl
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.data.repository.PokemonRepositoryImpl
import com.example.jsavery_pokedex.domain.manager.PokemonListManager
import com.example.jsavery_pokedex.domain.manager.PokemonListManagerImpl
import com.example.jsavery_pokedex.services.PokemonService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePokemonDataSource(pokeService: PokemonService): PokemonDataSource =
        PokemonDataSourceImpl(pokeService)

    @Provides
    @Singleton
    fun providePokemonRepository(remoteDataSource: PokemonDataSource): PokemonRepository =
        PokemonRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providePokemonListManager(): PokemonListManager =
        PokemonListManagerImpl()
}
