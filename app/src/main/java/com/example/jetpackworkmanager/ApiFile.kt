package com.example.jetpackworkmanager

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

interface ApiFile {

    @GET(
        "/img/a/AVvXsEhQ7R2ySipHb8y5jNJeiIj3pE8dZfWAV7EF0wQZ4rQ65lB4MsZroAT4R_7rSfznMZ30xBMLx9" +
                "_dwnt05V6I0Du0EfI7mvLicK6LwdkuZsF_Gc3sPqrZGxkojTJpHCXFI3Kvr3bLyoSjElldtt1NUpGSBzHgG3O1pvS9BR02L9R2_" +
                "FYTUgPLfUoNLWYQ"
    )
    suspend fun downloadImage(): Response<ResponseBody>

    companion object {

        val instance by lazy {
            Retrofit.Builder()
                .baseUrl("https://blogger.googleusercontent.com")
                .build()
                .create(ApiFile::class.java)
        }
    }

}


