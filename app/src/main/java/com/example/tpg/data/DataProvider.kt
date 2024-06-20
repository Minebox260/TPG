package com.example.tpg.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object DataProvider {

    val BASE_URL = "https://ijydhtkpaonezetazldv.supabase.co/"
    val API_PUBLIC_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlqeWRodGtwYW9uZXpldGF6bGR2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTgwMjQ0NTIsImV4cCI6MjAzMzYwMDQ1Mn0.Ej1RscPvoMfMbYtxjalapsKHpl6O-UZ6l-5F8PLBvu0"

    val supabase = createSupabaseClient(
        supabaseUrl = BASE_URL,
        supabaseKey = API_PUBLIC_KEY
    ) {
        install(Postgrest)
        install(Auth)
    }
}