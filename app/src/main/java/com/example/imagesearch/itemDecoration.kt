package com.example.imagesearch

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class itemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12F,view.resources.displayMetrics)
        val outerMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50F,view.resources.displayMetrics)
        val spancount = 3
        val maxCount = parent.adapter?.itemCount
        val position = parent.getChildAdapterPosition(view)
        val column = position % spancount
        val row = position / spancount
        val lastRow = (maxCount!! -1) / spancount

        outRect.left = (column * spacing / spancount).toInt()
        outRect.right = (spacing - (column+1) * spacing/spancount).toInt()
        outRect.top = (spacing*2).toInt()

        if(row == lastRow){
            outRect.bottom = outerMargin.toInt()
        }
    }
}