package com.juanasoco.compasschallenge.data.data_source.remote

import retrofit2.http.GET

interface CompassAPI {
    @GET("/about")
    suspend fun fetchContent(): String
        companion object {
        const val BASE_URL = " https://www.compass.com"
    }
}