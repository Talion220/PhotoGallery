package com.bignerdranch.android.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.photogallery.FlickrFetchr
import com.bignerdranch.android.photogallery.api.SampleGalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "PhotoListViewModel"

class PhotoListViewModel : ViewModel() {

    private val photoRepository = FlickrFetchr.get()

    private val _photos: MutableStateFlow<List<SampleGalleryItem>> = MutableStateFlow(emptyList())
    val photos: StateFlow<List<SampleGalleryItem>>
        get() = _photos.asStateFlow()

    init {
        viewModelScope.launch {
            photoRepository.getPhotos().collect {
                _photos.value = it
            }
        }
    }
}