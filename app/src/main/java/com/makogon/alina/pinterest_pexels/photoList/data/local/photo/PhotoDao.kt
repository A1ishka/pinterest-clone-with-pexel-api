package com.makogon.alina.pinterest_pexels.photoList.data.local.photo

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.BookmarkedPhotoEntity
import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.PhotoEntity

@Dao
interface PhotoDao {
    @Upsert
    suspend fun upsertPhotoList(photoList: List<PhotoEntity>)

    @Query("SELECT * FROM PhotoEntity WHERE id=:id")
    suspend fun getPhotoById(id: Int): PhotoEntity

    @Query("SELECT * FROM PhotoEntity")
    suspend fun getPhotoList(): List<PhotoEntity>


    @Upsert
    suspend fun upsertBookmarkedPhotoList(bookmarkedPhotoList: List<BookmarkedPhotoEntity>)

    @Query("SELECT * FROM BookmarkedPhotoEntity WHERE id=:id")
    suspend fun getBookmarkedPhotoById(id: Int): BookmarkedPhotoEntity

    @Query("SELECT * FROM BookmarkedPhotoEntity")
    suspend fun getBookmarkedPhotoList(): List<BookmarkedPhotoEntity>
}