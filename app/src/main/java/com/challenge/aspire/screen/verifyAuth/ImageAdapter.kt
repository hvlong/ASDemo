package com.challenge.aspire.screen.verifyAuth

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.challenge.aspire.R
import com.challenge.aspire.base.adapter.BaseRecyclerViewAdapter
import com.challenge.aspire.base.adapter.OnItemClickListener
import com.challenge.aspire.data.model.Image
import com.challenge.aspire.databinding.ItemImageBinding

class ImageAdapter(
        context: Context) : BaseRecyclerViewAdapter<Image, RecyclerView.ViewHolder>(
        context) {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
                DataBindingUtil.inflate<ItemImageBinding>(layoutInflater,
                        R.layout.item_image, parent,
                        false)
        return ItemViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(getItem(position), position)
    }

    internal class ItemViewHolder(
            private val binding: ItemImageBinding,
            private val itemClickListener: OnItemClickListener<Image>?,
            private val itemViewModel: ItemImageViewModel =
                    ItemImageViewModel(itemClickListener)) : RecyclerView.ViewHolder(
            binding.root) {

        init {
            binding.viewModel = itemViewModel
        }

        fun bind(imagePath: Image?, position: Int) {
            itemViewModel.position = position
            itemViewModel.setData(imagePath)
            binding.executePendingBindings()
        }
    }
}