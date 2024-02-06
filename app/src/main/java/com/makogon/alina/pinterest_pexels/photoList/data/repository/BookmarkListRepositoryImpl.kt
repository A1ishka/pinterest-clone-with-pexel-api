package com.makogon.alina.pinterest_pexels.photoList.data.repository

import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.PhotoDB
import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.BookmarkedPhotoEntity
import com.makogon.alina.pinterest_pexels.photoList.data.mappers.toPhoto
import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.BookmarkListRepository
import com.makogon.alina.pinterest_pexels.photoList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkListRepositoryImpl @Inject constructor(
    private val photoDB: PhotoDB
) : BookmarkListRepository {
    override suspend fun getBookmarksList(): Flow<Resource<List<Photo>>> {
        return flow {
            emit(Resource.Loading(true))
            val localPhotoList = photoDB.photoDao.getBookmarkedPhotoList()
            val shouldLoadLocalMovie = localPhotoList.isNotEmpty()
            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localPhotoList.map { movieEntity ->
                        movieEntity.toPhoto()
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }else{
                emit(Resource.Error(message = "You haven't saved anything yet"))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    override suspend fun upsertBookmarkedPhotoList(bookmarkedPhotoList: List<BookmarkedPhotoEntity>){}

    override suspend fun getBookmarkedPhoto(id: Int): Flow<Resource<Photo>> {
        return flow {
            emit(Resource.Loading(true))
            val photoBookmarkedEntity = photoDB.photoDao.getBookmarkedPhotoById(id)
            if (photoBookmarkedEntity != null) {
                emit(Resource.Success(photoBookmarkedEntity.toPhoto()))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such photo"))
            emit(Resource.Loading(false))
        }
    }
}