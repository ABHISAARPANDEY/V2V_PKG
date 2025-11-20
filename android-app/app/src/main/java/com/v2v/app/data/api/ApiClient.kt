package com.v2v.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // For Android Emulator: use 10.0.2.2 (maps to host's localhost)
    // For Physical Device: use your computer's IP address (e.g., 192.168.1.100)
    // Find your IP: Windows (ipconfig) or Mac/Linux (ifconfig)
    // 
    // Current IP: 192.168.105.232 - Verify this matches your computer's IP
    // To verify: Run 'ipconfig' (Windows) or 'ifconfig' (Mac/Linux) and check IPv4 Address
    private const val BASE_URL = "http://192.168.105.232:3001/"
    
    // Make sure:
    // 1. Backend is running on port 3001
    // 2. Your computer and phone are on the same WiFi network
    // 3. Windows Firewall allows connections on port 3001

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val token = TokenManager.getToken()
            if (token != null) {
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

