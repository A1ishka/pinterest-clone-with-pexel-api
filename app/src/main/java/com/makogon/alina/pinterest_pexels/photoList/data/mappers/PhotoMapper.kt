package com.makogon.alina.pinterest_pexels.photoList.data.mappers

import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.BookmarkedPhotoEntity
import com.makogon.alina.pinterest_pexels.photoList.data.local.photo.entities.PhotoEntity
import com.makogon.alina.pinterest_pexels.photoList.data.remote.response.PhotoDto
import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo

fun PhotoEntity.toPhoto(): Photo {
    return Photo(
        alt=alt,
        avg_color=avg_color,
        height=height,
        width=width,
        liked=liked,
        photographer=photographer,
        photographer_id=photographer_id,
        photographer_url=photographer_url,
        url=url,
        id=id
    )
}


fun PhotoDto.toPhotoEntity(): PhotoEntity {
    return PhotoEntity(
        alt=alt?: "",
        avg_color=avg_color?: "",
        height=height?: 0,
        width=width?:0,
        liked=liked?:false,
        photographer=photographer?: "",
        photographer_id=photographer_id?:0,
        photographer_url=photographer_url?: "",
        url=url?: "",
        id=id?:-1
    )
}

fun PhotoDto.toPhoto(): Photo {
    return Photo(
        alt=alt?: "",
        avg_color=avg_color?: "",
        height=height?: 0,
        width=width?:0,
        liked=liked?:false,
        photographer=photographer?: "",
        photographer_id=photographer_id?:0,
        photographer_url=photographer_url?: "",
        url=url?: "",
        id=id?:-1
    )
}

fun BookmarkedPhotoEntity.toPhoto(): Photo {
    return Photo(
        alt=alt,
        avg_color=avg_color,
        height=height,
        width=width,
        liked=liked,
        photographer=photographer,
        photographer_id=photographer_id,
        photographer_url=photographer_url,
        url=url,
        id=id
    )
}


fun PhotoDto.toBookmarkedPhotoEntity(): BookmarkedPhotoEntity {
    return BookmarkedPhotoEntity(
        alt=alt?: "",
        avg_color=avg_color?: "",
        height=height?: 0,
        width=width?:0,
        liked=liked?:false,
        photographer=photographer?: "",
        photographer_id=photographer_id?:0,
        photographer_url=photographer_url?: "",
        url=url?: "",
        id=id?:-1
    )
}