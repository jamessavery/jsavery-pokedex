package com.example.jsavery_pokedex.domain.di

import android.content.Context
import com.example.jsavery_pokedex.data.datasource.PokemonDataSource
import com.example.jsavery_pokedex.data.repository.PokemonRepository
import com.example.jsavery_pokedex.data.repository.PokemonRepositoryImpl
import com.example.jsavery_pokedex.domain.manager.PokemonListManager
import com.example.jsavery_pokedex.domain.manager.PokemonListManagerImpl
import com.example.jsavery_pokedex.domain.util.JsonUtils
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonRepository(remoteDataSource: PokemonDataSource): PokemonRepository =
        PokemonRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun providePokemonListManager(): PokemonListManager =
        PokemonListManagerImpl()

    @Provides
    @Singleton
    fun provideJsonUtils(@ApplicationContext context: Context, moshi: Moshi) =
        JsonUtils(context, moshi)
}
