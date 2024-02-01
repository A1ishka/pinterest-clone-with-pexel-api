package com.makogon.alina.pinterest_pexels.photoList.presentation

sealed interface PhotoListEvent {
    data class Paginate(val category: String):PhotoListEvent
    object Navigate: PhotoListEvent
}