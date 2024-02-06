package com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList

import com.makogon.alina.pinterest_pexels.photoList.domain.model.Photo
import com.makogon.alina.pinterest_pexels.photoList.presentation.components.FeaturedCollection

data class PhotoListState(
    val isLoading: Boolean = false,
    val isCurrentPhotoScreen: Boolean = true,
    val isSearchActive: Boolean = false,
    val photoListPage: Int = 1,
    val bookmarksListPage: Int = 1,
    val searchText: String = "",
    val selectedTitle: String = "",
    val featuredCollections: List<FeaturedCollection> = emptyList(),
    val photoList: List<Photo> = emptyList(),
    val bookmarksList: List<Photo> = emptyList()
) {
}