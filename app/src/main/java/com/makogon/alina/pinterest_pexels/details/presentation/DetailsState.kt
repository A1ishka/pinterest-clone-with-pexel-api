package com.makogon.alina.pinterest_pexels.details.presentation

import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo

data class DetailsState(
    val isLoading:Boolean=true,
    val photo: Photo?=null,
    val isFavorite:Boolean?=false
)