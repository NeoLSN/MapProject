package com.android.mapproject.presentation.places

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created by JasonYang.
 */
class ScrollChildSwipeRefreshLayout : SwipeRefreshLayout {

    private var mScrollUpChild: View? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun canChildScrollUp(): Boolean {
        return if (mScrollUpChild != null) {
            ViewCompat.canScrollVertically(mScrollUpChild!!, -1)
        } else super.canChildScrollUp()
    }

    fun setScrollUpChild(view: View) {
        mScrollUpChild = view
    }
}