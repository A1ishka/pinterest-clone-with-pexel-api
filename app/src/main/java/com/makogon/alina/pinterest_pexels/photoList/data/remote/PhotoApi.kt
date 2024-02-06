package com.makogon.alina.pinterest_pexels.photoList.data.remote

import com.makogon.alina.pinterest_pexels.photoList.data.remote.response.PhotoDto
import com.makogon.alina.pinterest_pexels.photoList.data.remote.response.PhotoListDto
import com.makogon.alina.pinterest_pexels.photoList.presentation.components.FeaturedCollection
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApi {
    @GET("search?query={query}")
    suspend fun getPhotoList(
        @Query("per_page") perpage: Int = 30,
        @Query("page") page: Int,
        @Path("query") query: String,
        @Header("Authorization") authorization: String = API_KEY
    ): PhotoListDto

    @GET("photos/{id}")
    suspend fun getPhoto(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String = API_KEY
    ): PhotoDto

    @GET("curated?")
    suspend fun getCuratedPhotoList(
        @Query("per_page") perpage: Int = PER_PAGE,
        @Query("page") page: Int,
        @Header("Authorization") authorization: String = API_KEY
    ): PhotoListDto


    @GET("collections/featured?")
    suspend fun getFeaturedCollections(
        @Query("per_page") perpage: Int = FEATURED_PER_PAGE,
        //@Query("page") page: Int,
        @Header("Authorization") authorization: String = API_KEY
    ): List<FeaturedCollection>?


    companion object {
        const val BASE_URL = "https://api.pexels.com/v1/"
        const val IMAGE_BASE_URL = "https://api.pexels.com/v1/photos/:"
        const val API_KEY = "v3BC1D9FqIgaeodvGtoGxgNajLLXJ23SYgVXUhxs2CNM5bAm4wvOOez7"
        const val PER_PAGE = 30
        const val FEATURED_PER_PAGE = 7
        const val PAGE = 1
    }
}