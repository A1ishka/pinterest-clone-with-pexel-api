package com.makogon.alina.pinterest_pexels.photoList.domain.model

data class Photo(
    val alt: String,
    val avg_color: String,
    val height: Int,
    val width: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val url: String,
    val id: Int
)