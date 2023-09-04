/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhangteng.base.cardview

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.RequiresApi

/**
 * Very simple drawable that draws a rounded rectangle background with arbitrary corners and also
 * reports proper outline for Lollipop.
 *
 *
 * Simpler and uses less resources compared to GradientDrawable or ShapeDrawable.
 */
@RequiresApi(21)
internal class RoundRectDrawable(backgroundColor: ColorStateList?, private var mRadius: Float) :
    Drawable() {
    private val mPaint: Paint
    private val mBoundsF: RectF
    private val mBoundsI: Rect
    var padding = 0f
        private set
    private var mInsetForPadding = false
    private var mInsetForRadius = true
    private var mBackground: ColorStateList? = null
    private var mTintFilter: PorterDuffColorFilter? = null
    private var mTint: ColorStateList? = null
    private var mTintMode: PorterDuff.Mode? = PorterDuff.Mode.SRC_IN

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        setBackground(backgroundColor)
        mBoundsF = RectF()
        mBoundsI = Rect()
    }

    private fun setBackground(color: ColorStateList?) {
        mBackground = color ?: ColorStateList.valueOf(Color.TRANSPARENT)
        mPaint.color = mBackground!!.getColorForState(state, mBackground!!.defaultColor)
    }

    fun setPadding(padding: Float, insetForPadding: Boolean, insetForRadius: Boolean) {
        if (padding == this.padding && mInsetForPadding == insetForPadding && mInsetForRadius == insetForRadius) {
            return
        }
        this.padding = padding
        mInsetForPadding = insetForPadding
        mInsetForRadius = insetForRadius
        updateBounds(null)
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        val paint = mPaint
        val clearColorFilter: Boolean
        if (mTintFilter != null && paint.colorFilter == null) {
            paint.colorFilter = mTintFilter
            clearColorFilter = true
        } else {
            clearColorFilter = false
        }
        canvas.drawRoundRect(mBoundsF, mRadius, mRadius, paint)
        if (clearColorFilter) {
            paint.colorFilter = null
        }
    }

    private fun updateBounds(bounds: Rect?) {
        var bounds = bounds
        if (bounds == null) {
            bounds = getBounds()
        }
        mBoundsF[bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat()] =
            bounds.bottom.toFloat()
        mBoundsI.set(bounds)
        if (mInsetForPadding) {
            val vInset: Float = RoundRectDrawableWithShadow.Companion.calculateVerticalPadding(
                padding, mRadius, mInsetForRadius
            )
            val hInset: Float = RoundRectDrawableWithShadow.Companion.calculateHorizontalPadding(
                padding, mRadius, mInsetForRadius
            )
            mBoundsI.inset(
                Math.ceil(hInset.toDouble()).toInt(),
                Math.ceil(vInset.toDouble()).toInt()
            )
            // to make sure they have same bounds.
            mBoundsF.set(mBoundsI)
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        updateBounds(bounds)
    }

    override fun getOutline(outline: Outline) {
        outline.setRoundRect(mBoundsI, mRadius)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    var radius: Float
        get() = mRadius
        set(radius) {
            if (radius == mRadius) {
                return
            }
            mRadius = radius
            updateBounds(null)
            invalidateSelf()
        }
    var color: ColorStateList?
        get() = mBackground
        set(color) {
            setBackground(color)
            invalidateSelf()
        }

    override fun setTintList(tint: ColorStateList?) {
        mTint = tint
        mTintFilter = createTintFilter(mTint, mTintMode)
        invalidateSelf()
    }

    override fun setTintMode(tintMode: PorterDuff.Mode?) {
        mTintMode = tintMode
        mTintFilter = createTintFilter(mTint, mTintMode)
        invalidateSelf()
    }

    override fun onStateChange(stateSet: IntArray): Boolean {
        val newColor = mBackground!!.getColorForState(stateSet, mBackground!!.defaultColor)
        val colorChanged = newColor != mPaint.color
        if (colorChanged) {
            mPaint.color = newColor
        }
        if (mTint != null && mTintMode != null) {
            mTintFilter = createTintFilter(mTint, mTintMode)
            return true
        }
        return colorChanged
    }

    override fun isStateful(): Boolean {
        return mTint != null && mTint!!.isStateful || mBackground != null && mBackground!!.isStateful || super.isStateful()
    }

    /**
     * Ensures the tint filter is consistent with the current tint color and
     * mode.
     */
    private fun createTintFilter(
        tint: ColorStateList?,
        tintMode: PorterDuff.Mode?
    ): PorterDuffColorFilter? {
        if (tint == null || tintMode == null) {
            return null
        }
        val color = tint.getColorForState(state, Color.TRANSPARENT)
        return PorterDuffColorFilter(color, tintMode)
    }
}