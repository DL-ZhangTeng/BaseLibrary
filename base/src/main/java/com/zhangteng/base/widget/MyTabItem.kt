package com.zhangteng.base.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.TintTypedArray
import com.zhangteng.base.R

/**
 * Created by swing on 2018/9/3.
 */
class MyTabItem @SuppressLint("RestrictedApi") constructor(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val mText: CharSequence?
    val mIcon: Drawable?
    val mCustomLayout: Int

    constructor(context: Context?) : this(context, null) {}

    init {
        val a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.MyTabItem)
        mText = a.getText(R.styleable.MyTabItem_myText)
        mIcon = a.getDrawable(R.styleable.MyTabItem_myIcon)
        mCustomLayout = a.getResourceId(R.styleable.MyTabItem_myLayout, 0)
        a.recycle()
    }
}