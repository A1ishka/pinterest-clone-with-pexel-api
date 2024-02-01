package com.makogon.alina.pinterest_pexels.photoList.data.remote.response

data class PhotoListDto(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<PhotoDto>,
    val total_results: Int
)