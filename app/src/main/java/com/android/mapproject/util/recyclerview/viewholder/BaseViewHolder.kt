package com.android.mapproject.util.recyclerview.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by JasonYang on 2018/9/27.
 */
class BaseViewHolder(private val binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindVariable(variableId: Int, obj: Any) {
        binding.setVariable(variableId, obj)
        binding.executePendingBindings()
    }
}