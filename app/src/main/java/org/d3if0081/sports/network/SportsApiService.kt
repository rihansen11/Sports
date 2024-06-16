package org.d3if0081.sports.network

import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if0081.sports.model.OpStatus
import org.d3if0081.sports.model.Sports
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://unspoken.my.id/"

private val moshi = Moshi.Builder()
    .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
interface SportsApiService {

    @GET("api_rihansen.php")
    suspend fun getSports(
        @Header("Authorization") userId: String
    ):List<Sports>

    @Multipart
    @POST("api_rihansen.php")
    suspend fun postSports(
        @Header("Authorization") userId: String,
        @Part("nama") nama: RequestBody,
        @Part("posisi") posisi: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("api_rihansen.php")
    suspend fun deleteSports(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ) : OpStatus
}

object SportsApi{
    val service: SportsApiService by lazy {
        retrofit.create(SportsApiService::class.java)
    }
    fun getSportsUrl(imageId: String): String{
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus{
    LOADING, SUCCESS, FAILED
}