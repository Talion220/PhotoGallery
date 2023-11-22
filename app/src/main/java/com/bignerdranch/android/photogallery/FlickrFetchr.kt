package com.bignerdranch.android.photogallery

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.api.FlickrResponse
import com.bignerdranch.android.photogallery.api.PhotoInterceptor
import com.bignerdranch.android.photogallery.api.PhotoResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.bignerdranch.android.photogallery.database.PhotoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import androidx.room.Room
import com.bignerdranch.android.photogallery.GalleryItem
import com.bignerdranch.android.photogallery.api.SampleGalleryItem

private const val TAG = "FlickrFetchr"
private const val DATABASE_NAME = "photo-database"
class FlickrFetchr private constructor(
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
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()
        val retrofit: Retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        flickrApi =
            retrofit.create(FlickrApi::class.java)
    }
    fun fetchPhotosRequest():
            Call<FlickrResponse> {
        return flickrApi.fetchPhotos()
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(fetchPhotosRequest())
    }
    fun searchPhotosRequest(query: String):
            Call<FlickrResponse> {
        return flickrApi.searchPhotos(query)
    }
    fun searchPhotos(query: String):
            LiveData<List<GalleryItem>> {
        return fetchPhotoMetadata(searchPhotosRequest(query))
    }
    private fun fetchPhotoMetadata(flickrRequest:
                               Call<FlickrResponse>)
            : LiveData<List<GalleryItem>> {
        val responseLiveData:
                MutableLiveData<List<GalleryItem>> =  MutableLiveData()
        flickrRequest.enqueue(object :
            Callback<FlickrResponse> {
            override fun onFailure(call:
                                   Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }
            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                Log.d(TAG, "Response received")
                val flickrResponse:
                        FlickrResponse? = response.body()
                val photoResponse:
                        PhotoResponse? = flickrResponse?.photos
                var galleryItems:
                        List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                galleryItems =
                    galleryItems.filterNot {
                        it.url.isBlank()
                    }
                responseLiveData.value =
                    galleryItems
            }
        })
        return responseLiveData
    }

    fun getPhotos(): Flow<List<SampleGalleryItem>> = database.photoDao().getPhotos()
    suspend fun addPhoto(photo: SampleGalleryItem) {
        database.photoDao().addPhoto(photo)
    }

    suspend fun deletePhotos() = database.photoDao().deletePhotos()



    companion object {
        private var INSTANCE: FlickrFetchr? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = FlickrFetchr(context)
            }
        }
        fun get(): FlickrFetchr {
            return INSTANCE ?:
            throw IllegalStateException("PhotoRepository must be initialized")
        }
    }
}