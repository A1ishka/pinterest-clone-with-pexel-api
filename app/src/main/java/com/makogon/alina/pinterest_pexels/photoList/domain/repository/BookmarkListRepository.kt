package com.makogon.alina.pinterest_pexels.photoList.domain.repository

import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.BookmarkedPhotoEntity
import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo
import com.makogon.alina.pinterest_pexels.photoList.util.Resource
import kotlinx.coroutines.flow.Flow

interface BookmarkListRepository {
    suspend fun getBookmarksList(): Flow<Resource<List<Photo>>>
    suspend fun getBookmarkedPhoto(id: Int): Flow<Resource<Photo>>
    suspend fun upsertBookmarkedPhotoList(bookmarkedPhotoList: List<BookmarkedPhotoEntity>)
}
