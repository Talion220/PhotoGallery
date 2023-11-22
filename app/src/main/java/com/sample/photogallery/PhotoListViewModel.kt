package com.sample.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.photogallery.api.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "PhotoListViewModel"

class PhotoListViewModel : ViewModel() {

    private val photoRepository = PhotoRepository.get()

    private val _photos: MutableStateFlow<List<GalleryItem>> = MutableStateFlow(emptyList())
    val photos: StateFlow<List<GalleryItem>>
        get() = _photos.asStateFlow()

    init {
        viewModelScope.launch {
            photoRepository.getPhotos().collect {
                _photos.value = it
            }
        }
    }
}