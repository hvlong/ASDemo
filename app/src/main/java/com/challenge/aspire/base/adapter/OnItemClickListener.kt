package com.challenge.aspire.base.adapter

/**
 * OnItemClickListener
 *
 * @param <T> Data from item click
</T> */

interface OnItemClickListener<T> {
    fun onItemViewClick(item: T, position: Int = 0)
}