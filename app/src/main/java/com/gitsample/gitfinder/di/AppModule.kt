package com.gitsample.gitfinder.di

import com.gitsample.gitfinder.data.remote.GitFinderApiService
import com.gitsample.gitfinder.data.remote.RemoteDataSource
import com.gitsample.gitfinder.data.repository.Repository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * This app module provides the dependencies for
 * for Remote, local, api data sources also the
 * repositories
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    @Provides
    @Singleton
    fun provideGitFinderAPiService(retrofit: Retrofit): GitFinderApiService =
        retrofit.create(GitFinderApiService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: GitFinderApiService): RemoteDataSource =
        RemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository =
        Repository(remoteDataSource)

}