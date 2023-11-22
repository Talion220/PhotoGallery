package com.sample.photogallery

import android.content.Context
import androidx.room.Room
import com.sample.photogallery.api.FlickrApi
import com.sample.photogallery.api.GalleryItem
import com.sample.photogallery.api.PhotoInterceptor
import com.sample.photogallery.database.PhotoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

private const val DATABASE_NAME = "photo-database"
class PhotoRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
){
    private val flickrApi: FlickrApi

    private val database: PhotoDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            DATABASE_NAME
        )
        .build()
    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
        flickrApi = retrofit.create()
    }

    suspend fun fetchPhotos(): List<GalleryItem> =
        flickrApi.fetchPhotos().photos.galleryItems
    suspend fun searchPhotos(query: String): List<GalleryItem> =
        flickrApi.searchPhotos(query).photos.galleryItems

    fun getPhotos(): Flow<List<GalleryItem>> = database.photoDao().getPhotos()
    suspend fun addPhoto(photo: GalleryItem) {
        database.photoDao().addPhoto(photo)
    }

    suspend fun deletePhotos() = database.photoDao().deletePhotos()



    companion object {
        private var INSTANCE: PhotoRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PhotoRepository(context)
            }
        }
        fun get(): PhotoRepository {
            return INSTANCE ?:
            throw IllegalStateException("PhotoRepository must be initialized")
        }
    }
}
