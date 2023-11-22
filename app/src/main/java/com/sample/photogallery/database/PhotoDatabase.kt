package com.sample.photogallery.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.photogallery.api.GalleryItem

@Database(entities = [ GalleryItem::class ], version=1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

}
