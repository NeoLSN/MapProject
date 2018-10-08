package com.android.mapproject.util.recyclerview

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by JasonYang.
 */
fun RecyclerView.setLinearDivider(
        @DrawableRes drawableResId: Int,
        linearLayoutManager: LinearLayoutManager
) {
    val context = this.context
    this.addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation).apply {
        setDrawable(ContextCompat.getDrawable(context, drawableResId)!!)
    })
}