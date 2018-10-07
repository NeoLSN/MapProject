package com.android.mapproject.presentation.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mapproject.BR
import com.android.mapproject.R
import com.android.mapproject.domain.ParkingPlace
import com.android.mapproject.util.recyclerview.viewholder.BaseViewHolder

/**
 * Created by JasonYang.
 */
class ParkingPlacesAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var onItemClick: ((obj: Any) -> Unit)? = null

    var items: List<ParkingPlace> = ArrayList()
        set(newItems) {
            val cb = AdapterDiffCallback(field, newItems)
            val result = DiffUtil.calculateDiff(cb, false)
            field = newItems
            result.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.place_item, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val place = items[position]
        holder.bindVariable(BR.place, place)
        onItemClick?.let { listener ->
            holder.itemView.setOnClickListener {
                listener(place)
            }
        }
    }
}