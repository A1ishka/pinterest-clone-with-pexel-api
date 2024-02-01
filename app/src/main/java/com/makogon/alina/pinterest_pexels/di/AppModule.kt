package com.makogon.alina.pinterest_pexels.di

import android.app.Application
import androidx.room.Room
import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.PhotoDB
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesPhotoApi(): PhotoApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PhotoApi.BASE_URL)
            .client(client)
            .build()
            .create(PhotoApi::class.java)
    }

    @Provides
    @Singleton
    fun providesPhotoDB(app: Application): PhotoDB {
        return Room.databaseBuilder(
            app,
            PhotoDB::class.java,
            "photodb.db"
        ).build()
    }

}