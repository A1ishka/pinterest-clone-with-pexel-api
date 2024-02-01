package com.makogon.alina.pinterest_pexels.photoList.data.repository

import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.PhotoDB
import com.makogon.alina.pinterest_pexels.photoList.data.mappers.toPhoto
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi
import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.PhotoListRepository
import com.makogon.alina.pinterest_pexels.photoList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class PhotoListRepositoryImpl @Inject constructor(
    private val photoApi: PhotoApi,
    private val photoDB: PhotoDB
) : PhotoListRepository {
    override suspend fun getPhotoList(
        forceFetchFromRemote: Boolean,
        page: Int
    ): Flow<Resource<List<Photo>>> {
        return flow {
            emit(Resource.Loading(true))
//
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getPhoto(id: Int): Flow<Resource<Photo>> {
        return flow {
            emit(Resource.Loading(true))
            val photoEntity = photoDB.photoDao.getPhotoById(id)
            if (photoEntity != null) {
                emit(
                    Resource.Success(photoEntity.toPhoto())
                )
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such photo"))
            emit(Resource.Loading(false))
        }
    }
}