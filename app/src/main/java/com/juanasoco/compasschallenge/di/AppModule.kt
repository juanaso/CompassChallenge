package com.juanasoco.compasschallenge.di

import android.app.Application
import androidx.room.Room
import com.juanasoco.compasschallenge.data.data_source.local.CompassDatabase
import com.juanasoco.compasschallenge.data.data_source.remote.CompassAPI
import com.juanasoco.compasschallenge.data.repository.CompassRepositoryImpl
import com.juanasoco.compasschallenge.domain.repository.CompassRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCompassAPI(): CompassAPI {
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(CompassAPI.BASE_URL)
            .build()
            .create(CompassAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCompassDatabase(app: Application): CompassDatabase {
        return Room.databaseBuilder(
            app,
            CompassDatabase::class.java,
            CompassDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCompassRepository(api: CompassAPI,compassDatabase: CompassDatabase): CompassRepository {
        return CompassRepositoryImpl(api,compassDatabase.compassDao())
    }

}