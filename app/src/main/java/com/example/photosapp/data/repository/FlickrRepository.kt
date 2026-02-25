package com.example.photosapp.data.repository

import com.example.photosapp.BuildConfig
import com.example.photosapp.data.api.FlickrApiService
import com.example.photosapp.data.model.Photo
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlickrRepository @Inject constructor(
    private val apiService: FlickrApiService
) {

    suspend fun getPhotos(query: String, page: Int): Result<List<Photo>> {
        return try {
            val response = if (query.isEmpty()) {
                apiService.getRecentPhotos(
                    apiKey = BuildConfig.API_KEY,
                    page = page
                )
            } else {
                apiService.searchPhotos(
                    apiKey = BuildConfig.API_KEY,
                    text = query,
                    page = page
                )
            }

            val photos = response.photos.photo.map { dto ->
                Photo(
                    id = dto.id,
                    owner = dto.owner,
                    secret = dto.secret,
                    server = dto.server,
                    farm = dto.farm,
                    title = dto.title,
                    isPublic = dto.isPublic == 1,
                    isFriend = dto.isFriend == 1,
                    isFamily = dto.isFamily == 1,
                    imageUrl = "https://live.staticflickr.com/${dto.server}/${dto.id}_${dto.secret}.jpg"
                )
            }

            Result.success(photos)
        } catch (e: Exception) {
            Result.failure(
                when (e) {
                    is UnknownHostException -> Exception("No internet connection")
                    is SocketTimeoutException -> Exception("Request timed out")
                    is HttpException -> Exception("Server error: ${e.code()}")
                    else -> Exception("Something went wrong")
                }
            )
        }
    }
}