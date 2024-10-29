package com.example.jsavery_pokedex.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CoreNetworkService private constructor() {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    val pokemonApi: PokemonService = retrofit.create(PokemonService::class.java)

    companion object {
        private const val BASE_URL = "http://localhost:9000/"

        @Volatile
        private var instance: CoreNetworkService? = null
        fun getInstance(): CoreNetworkService {
            return instance ?: synchronized(this) {
                instance ?: CoreNetworkService().also { instance = it }
            }
        }
    }
}
