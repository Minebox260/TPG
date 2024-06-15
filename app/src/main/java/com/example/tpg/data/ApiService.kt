package com.example.tpg.data

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val BASE_URL = "https://ijydhtkpaonezetazldv.supabase.co/rest/v1/"
private const val API_KEY = "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlqeWRodGtwYW9uZXpldGF6bGR2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTgwMjQ0NTIsImV4cCI6MjAzMzYwMDQ1Mn0.Ej1RscPvoMfMbYtxjalapsKHpl6O-UZ6l-5F8PLBvu0"

val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @Headers(API_KEY)
    @GET("machines")
    suspend fun getMachine(
        @Query("select") select: String = "*",
        @Query("serial_number") serial_number: String,
    ): String

}