package com.thiagoseiji.movieapp.retrofit

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.thiagoseiji.movieapp.data.Movie
import com.thiagoseiji.movieapp.data.api.CastResponse
import com.thiagoseiji.movieapp.data.api.MovieResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movie/popular")
    fun getPopularMoviesAsync(): Deferred<Response<MovieResponse>>

    @GET("movie/upcoming")
    fun getUpcomingMoviesAsync(): Deferred<Response<MovieResponse>>

    @GET("movie/{id}")
    fun getMovieById(@Path("id") id: Int): Deferred<Response<Movie>>

    @GET("movie/{id}/credits")
    fun getMovieCast(@Path("id") id: Int): Deferred<Response<CastResponse>>


    companion object {
        fun create(): MovieService {

            val apiKeyInterceptor = Interceptor{ chain ->
                val original = chain.request()
                val httpUrl = original.url()

                val newHttpUrl = httpUrl.newBuilder()
                    .addQueryParameter("api_key", "31243b71dfc271e73433b9f97d059e7f")
                    .addQueryParameter("language", "en-US").build()

                val requestBuilder = original.newBuilder().url(newHttpUrl)

                val request = requestBuilder.build()

                chain.proceed(request)
            }

            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()

            return Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(OkHttpClient.Builder()
                    .addInterceptor(apiKeyInterceptor)
                    .build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(MovieService::class.java)
        }
    }
}