package com.gitsample.gitfinder.di

import com.gitsample.gitfinder.BuildConfig
import com.gitsample.gitfinder.data.remote.GitFinderApiService
import com.gitsample.gitfinder.data.remote.RemoteDataSource
import com.gitsample.gitfinder.data.repository.GitFinderRepository
import com.gitsample.gitfinder.data.repository.RepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        return logging
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            // Add the logging interceptor (prints to Logcat)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
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
    fun provideRepository(remoteDataSource: RemoteDataSource): GitFinderRepository =
        RepositoryImpl(remoteDataSource)

}