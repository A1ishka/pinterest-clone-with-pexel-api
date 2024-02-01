package com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makogon.alina.pinterest_pexels.photoList.data.remote.response.Src

@Entity
data class PhotoEntity(
    val alt: String,
    val avg_color: String,
    val height: Int,
    val width: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val url: String,

    @PrimaryKey
    val id: Int
)