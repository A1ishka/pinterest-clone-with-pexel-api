package com.makogon.alina.pinterest_pexels.photoList.domain.repository

import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo
import com.makogon.alina.pinterest_pexels.photoList.util.Resource
import kotlinx.coroutines.flow.Flow

interface BookmarkListRepository {
    suspend fun getBookmarksList(): Flow<Resource<List<Photo>>>

    suspend fun getPhoto(id: Int): Flow<Resource<Photo>>
}