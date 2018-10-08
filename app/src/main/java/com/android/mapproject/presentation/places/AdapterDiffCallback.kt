package com.android.mapproject.presentation.places

import androidx.recyclerview.widget.DiffUtil
import com.android.mapproject.domain.model.ParkingPlace

/**
 * Created by JasonYang.
 */
class AdapterDiffCallback(
        private val oldList: List<ParkingPlace>,
        private val newList: List<ParkingPlace>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem::class.java == newItem::class.java
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val newer = newList[newItemPosition]
        return old == newer
    }

}