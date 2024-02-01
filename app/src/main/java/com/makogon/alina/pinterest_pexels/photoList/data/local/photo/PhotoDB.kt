package com.makogon.alina.pinterest_pexels.photoList.data.local.photo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.BookmarkedPhotoEntity
import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.PhotoEntity

@Database(entities = [PhotoEntity::class, BookmarkedPhotoEntity::class,], version = 1)
abstract class PhotoDB: RoomDatabase() {
    abstract val photoDao: PhotoDao
}