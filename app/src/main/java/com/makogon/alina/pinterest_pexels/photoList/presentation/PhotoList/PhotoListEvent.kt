package com.makogon.alina.pinterest_pexels.photoList.presentation.PhotoList

sealed interface PhotoListEvent {
    data class ChangeSearchText(val searchText: String): PhotoListEvent
    data class ChangeSelectedTitle(val selectedTitle: String): PhotoListEvent
    data class ChangeIsSearchActive(val isSearchActive: Boolean): PhotoListEvent

    data class PerformPhotoSearch(val searchText: String): PhotoListEvent
    object PerformFCollectionsSearch: PhotoListEvent
    object PerformBookmarkedPhotoSearch: PhotoListEvent
    object PerformCuratedPhotoSearch: PhotoListEvent

    data class Paginate(val category: String): PhotoListEvent
    object Navigate: PhotoListEvent
}