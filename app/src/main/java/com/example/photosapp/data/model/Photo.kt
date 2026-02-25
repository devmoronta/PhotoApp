package com.example.photosapp.data.model

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val isPublic: Boolean,
    val isFriend: Boolean,
    val isFamily: Boolean,
    val imageUrl: String
)