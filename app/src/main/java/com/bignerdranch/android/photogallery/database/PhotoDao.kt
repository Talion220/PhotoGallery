package com.bignerdranch.android.photogallery.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bignerdranch.android.photogallery.api.SampleGalleryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM samplegalleryitem")
    fun getPhotos(): Flow<List<SampleGalleryItem>>
    @Insert
    suspend fun addPhoto(photo: SampleGalleryItem)
    @Query("DELETE FROM samplegalleryitem")
    suspend fun deletePhotos()


}