package com.example.tpg.data

object DataProvider {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}