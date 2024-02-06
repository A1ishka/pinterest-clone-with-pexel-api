package com.makogon.alina.pinterest_pexels.photoList.data.repository

import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.PhotoDB
import com.makogon.alina.pinterest_pexels.photoList.data.mappers.toPhoto
import com.makogon.alina.pinterest_pexels.photoList.data.mappers.toPhotoEntity
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi
import com.makogon.alina.pinterest_pexels.photoList.data.remote.PhotoApi.Companion.PER_PAGE
import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo
import com.makogon.alina.pinterest_pexels.photoList.domain.repository.PhotoListRepository
import com.makogon.alina.pinterest_pexels.photoList.presentation.components.FeaturedCollection
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
        page: Int,
        query: String
    ): Flow<Resource<List<Photo>>> {
        return flow {
            emit(Resource.Loading(true))
            val localPhotoList = photoDB.photoDao.getPhotoList()
            val shouldLoadLocalMovie = localPhotoList.isNotEmpty() && !forceFetchFromRemote
            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localPhotoList.map { movieEntity ->
                        movieEntity.toPhoto()
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }
            val photoListFromApi = try {
                photoApi.getPhotoList(PER_PAGE, page, query)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading"))
                return@flow
            }

            val photoEntities = photoListFromApi.photos.let {
                it.map { photoDto ->
                    photoDto.toPhotoEntity()
                }
            }
            photoDB.photoDao.upsertPhotoList(photoEntities)
            emit(Resource.Success(
                photoEntities.map { it.toPhoto() }
            ))

            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getPhoto(id: Int): Flow<Resource<Photo>> {
        return flow {
            emit(Resource.Loading(true))
            val photoFromApi = photoApi.getPhoto(id)
            if (photoFromApi != null) {
                emit(Resource.Success(photoFromApi.let { photoDto -> photoDto.toPhoto() }))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such photo"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getFeaturedCollections(): Flow<Resource<List<FeaturedCollection>>> {
        return flow {
            emit(Resource.Loading(true))

            val featuredCollectionFromApi = try {
                photoApi.getFeaturedCollections()
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading"))
                return@flow
            }
            emit(Resource.Success(featuredCollectionFromApi))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getCuratedPhotoList(page: Int): Flow<Resource<List<Photo>>> {
        return flow {
            emit(Resource.Loading(true))

            val curatedPhotoFromApi = try {
                photoApi.getCuratedPhotoList(PER_PAGE, page)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading"))
                return@flow
            }

            emit(Resource.Success(
                curatedPhotoFromApi.photos.let {
                    it.map() { photoDto ->
                        photoDto.toPhoto()
                    }
                }
            ))
            emit(Resource.Loading(false))
        }
    }
}