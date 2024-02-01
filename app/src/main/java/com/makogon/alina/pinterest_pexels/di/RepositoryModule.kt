package com.makogon.alina.pinterest_pexels.di

import com.makogon.alina.pinterest_pexels.photoList.domain.repository.BookmarkListRepository
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.PhotoListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        photoListRepositoryImpl: PhotoListRepository
    ): PhotoListRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarksRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindBookmarksRepository(
        bookmarkListRepositoryImpl: BookmarkListRepository
    ): BookmarkListRepository
}