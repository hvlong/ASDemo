package com.challenge.aspire.screen.verifyAuth

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.challenge.aspire.BR
import com.challenge.aspire.base.adapter.OnItemClickListener
import com.challenge.aspire.data.model.Image
import com.challenge.aspire.utils.extension.notNull

class ItemImageViewModel(
        private val itemClickListener: OnItemClickListener<Image>? = null,
        var position: Int = 0,
        @Bindable
        var image: Image? = null) : BaseObservable() {


    fun setData(data: Image?) {
        data.notNull {
            image = it
            notifyPropertyChanged(BR.image)
        }
    }

    fun onItemClicked() {
//        itemClickListener.notNull { listener ->
//            image.notNull { listener.onItemViewClick(it, position) }
//        }
    }
}