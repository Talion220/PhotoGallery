package com.sample.photogallery.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity
data class GalleryItem(
    val title: String,
    @PrimaryKey val id: String,
    @Json(name = "url_s") val url: String,
    )

