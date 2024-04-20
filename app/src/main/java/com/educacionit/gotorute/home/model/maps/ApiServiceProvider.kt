package com.educacionit.gotorute.home.model.maps

import android.util.Log
import com.educacionit.gotorute.home.model.maps.api.RoutesServiceAPI
import com.educacionit.gotorute.home.model.maps.api.SearchServiceAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {
    private const val SEARCH_SERVICE_BASE_URL = "https://nominatim.openstreetmap.org"
    private const val ROUTES_SERVICE_BASE_URL = "https://trueway-directions2.p.rapidapi.com"

    private val defaultInterceptor = getDefaultInterceptor()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(defaultInterceptor)
        .build()

    val searchServiceAPI: SearchServiceAPI =
        getRetrofitInstance(SEARCH_SERVICE_BASE_URL).create(SearchServiceAPI::class.java)

    val routesServiceAPI: RoutesServiceAPI =
        getRetrofitInstance(ROUTES_SERVICE_BASE_URL).create(RoutesServiceAPI::class.java)

    private fun getRetrofitInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getDefaultInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()

            val requestBodyString = request.body()?.let { body ->
                val buffer = Buffer()
                body.writeTo(buffer)
                buffer.readUtf8()
            } ?: ""

            val requestLog = """
            URL: ${request.url()}
            Method: ${request.method()}
            Headers: ${request.headers()}
            Body: $requestBodyString
        """.trimIndent()

            Log.d("InterceptorLogger", requestLog)

            chain.proceed(request)
        }
    }
}