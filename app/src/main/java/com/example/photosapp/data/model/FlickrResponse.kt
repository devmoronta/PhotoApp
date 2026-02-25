package com.example.photosapp.data.model

import com.google.gson.annotations.SerializedName

data class FlickrResponse(
    @SerializedName("photos") val photos: FlickrPhotosResult,
    @SerializedName("stat") val stat: String
)

data class FlickrPhotosResult(
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("perpage") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("photo") val photo: List<FlickrPhoto>
)

data class FlickrPhoto(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("secret") val secret: String,
    @SerializedName("server") val server: String,
    @SerializedName("farm") val farm: Int,
    @SerializedName("title") val title: String,
    @SerializedName("ispublic") val isPublic: Int,
    @SerializedName("isfriend") val isFriend: Int,
    @SerializedName("isfamily") val isFamily: Int
)