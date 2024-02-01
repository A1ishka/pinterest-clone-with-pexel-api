package com.makogon.alina.pinterest_pexels.photoList.presentation

import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo

data class PhotoListState(
    val isLoading: Boolean=false,
    val photoListPage: Int = 1,
    val bookmarksListPage: Int = 1,
    val isCurrentPhotoScreen: Boolean=true,
    val photoList: List<Photo> = emptyList(),
    val bookmarksList: List<Photo> = emptyList()
) {
}