package com.sample.photogallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sample.photogallery.api.GalleryItem
import com.sample.photogallery.databinding.ListItemGalleryBinding
import com.sample.photogallery.databinding.ListItemPhotoBinding
import java.util.UUID

class PhotoViewHolder(
    private val binding: ListItemGalleryBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(galleryItem: GalleryItem, onPhotoClicked: (photo:GalleryItem) ->Unit) {
        binding.itemImageView.load(galleryItem.url){
            placeholder(R.drawable.bill_up_close)

        }
        binding.root.setOnClickListener {
            onPhotoClicked(galleryItem)
        }

    }
}

class PhotoListViewHolder(
    private val binding: ListItemPhotoBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(galleryItem: GalleryItem, onPhotoClicked: (photo:GalleryItem) ->Unit) {
        binding.photoView.load(galleryItem.url){
            placeholder(R.drawable.bill_up_close)

        }
        binding.photoTitle.text = galleryItem.title
//        binding.root.setOnClickListener {
//            onPhotoClicked(galleryItem)
//        }

    }
}

class PhotoListAdapter(
    private val galleryItems: List<GalleryItem>,
    private val onPhotoClicked: (photo:GalleryItem) -> Unit
) : RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ListItemGalleryBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = galleryItems[position]
        holder.bind(item,onPhotoClicked)
    }
    override fun getItemCount() = galleryItems.size
}
class PhotoListViewAdapter(
    private val galleryItems: List<GalleryItem>,
    private val onPhotoClicked: (photo:GalleryItem) -> Unit
) : RecyclerView.Adapter<PhotoListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPhotoBinding.inflate(inflater,parent, false)
        return PhotoListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        val item = galleryItems[position]
        holder.bind(item,onPhotoClicked)
    }
    override fun getItemCount() = galleryItems.size
}


