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
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import com.zhangteng.base.R

/**
 * A rounded rectangle drawable which also includes a shadow around.
 */
internal class RoundRectDrawableWithShadow(
    resources: Resources,
    backgroundColor: ColorStateList?,
    radius: Float,
    shadowSize: Float,
    maxShadowSize: Float,
    shadowColorStart: ColorStateList?,
    shadowColorEnd: ColorStateList?
) : Drawable() {
    private val mInsetShadow // extra shadow to avoid gaps between card and shadow
            : Int
    private val mPaint: Paint
    private val mCornerShadowPaint: Paint
    private val mEdgeShadowPaint: Paint
    private val mCardBounds: RectF
    private var mCornerRadius: Float
    private var mCornerShadowPath: Path? = null

    // actual value set by developer
    private var mRawMaxShadowSize = 0f

    // multiplied value to account for shadow offset
    private var mShadowSize = 0f

    // actual value set by developer
    private var mRawShadowSize = 0f
    private var mBackground: ColorStateList? = null
    private var mDirty = true
    private var mShadowStartColor: ColorStateList? = null
    private var mShadowEndColor: ColorStateList? = null
    private var mAddPaddingForCorners = true

    /**
     * If shadow size is set to a value above max shadow, we print a warning
     */
    private var mPrintedShadowClipWarning = false

    init {
        // 如果没有设置阴影颜色，使用默认颜色
        mShadowStartColor = shadowColorStart
            ?: ColorStateList.valueOf(resources.getColor(R.color.cardview_shadow_start_color))
        mShadowEndColor = shadowColorEnd
            ?: ColorStateList.valueOf(resources.getColor(R.color.cardview_shadow_end_color))
        mInsetShadow = resources.getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        setBackground(backgroundColor)
        mCornerShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        mCornerShadowPaint.style = Paint.Style.FILL
        mCornerRadius = (radius + .5f).toInt().toFloat()
        mCardBounds = RectF()
        mEdgeShadowPaint = Paint(mCornerShadowPaint)
        mEdgeShadowPaint.isAntiAlias = false
        setShadowSize(shadowSize, maxShadowSize)
    }

    private fun setBackground(color: ColorStateList?) {
        mBackground = color ?: ColorStateList.valueOf(Color.TRANSPARENT)
        mPaint.color = mBackground!!.getColorForState(state, mBackground!!.defaultColor)
    }

    /**
     * Casts the value to an even integer.
     */
    private fun toEven(value: Float): Int {
        val i = (value + .5f).toInt()
        return if (i % 2 == 1) {
            i - 1
        } else i
    }

    fun setAddPaddingForCorners(addPaddingForCorners: Boolean) {
        mAddPaddingForCorners = addPaddingForCorners
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
        mCornerShadowPaint.alpha = alpha
        mEdgeShadowPaint.alpha = alpha
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mDirty = true
    }

    fun setShadowSize(shadowSize: Float, maxShadowSize: Float) {
        var shadowSize = shadowSize
        var maxShadowSize = maxShadowSize
        require(shadowSize >= 0f) {
            ("Invalid shadow size " + shadowSize
                    + ". Must be >= 0")
        }
        require(maxShadowSize >= 0f) {
            ("Invalid max shadow size " + maxShadowSize
                    + ". Must be >= 0")
        }
        shadowSize = toEven(shadowSize).toFloat()
        maxShadowSize = toEven(maxShadowSize).toFloat()
        if (shadowSize > maxShadowSize) {
            shadowSize = maxShadowSize
            if (!mPrintedShadowClipWarning) {
                mPrintedShadowClipWarning = true
            }
        }
        if (mRawShadowSize == shadowSize && mRawMaxShadowSize == maxShadowSize) {
            return
        }
        mRawShadowSize = shadowSize
        mRawMaxShadowSize = maxShadowSize
        mShadowSize = (shadowSize * SHADOW_MULTIPLIER + mInsetShadow + .5f).toInt().toFloat()
        mDirty = true
        invalidateSelf()
    }

    override fun getPadding(padding: Rect): Boolean {
        val vOffset = Math.ceil(
            calculateVerticalPadding(
                mRawMaxShadowSize, mCornerRadius,
                mAddPaddingForCorners
            ).toDouble()
        ).toInt()
        val hOffset = Math.ceil(
            calculateHorizontalPadding(
                mRawMaxShadowSize, mCornerRadius,
                mAddPaddingForCorners
            ).toDouble()
        ).toInt()
        padding[hOffset, vOffset, hOffset] = vOffset
        return true
    }

    override fun onStateChange(stateSet: IntArray): Boolean {
        val newColor = mBackground!!.getColorForState(stateSet, mBackground!!.defaultColor)
        if (mPaint.color == newColor) {
            return false
        }
        mPaint.color = newColor
        mDirty = true
        invalidateSelf()
        return true
    }

    override fun isStateful(): Boolean {
        return mBackground != null && mBackground!!.isStateful || super.isStateful()
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun draw(canvas: Canvas) {
        if (mDirty) {
            buildComponents(bounds)
            mDirty = false
        }
        canvas.translate(0f, mRawShadowSize / 2)
        drawShadow(canvas)
        canvas.translate(0f, -mRawShadowSize / 2)
        sRoundRectHelper!!.drawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint)
    }

    private fun drawShadow(canvas: Canvas) {
        val edgeShadowTop = -mCornerRadius - mShadowSize
        val inset = mCornerRadius + mInsetShadow + mRawShadowSize / 2
        val drawHorizontalEdges = mCardBounds.width() - 2 * inset > 0
        val drawVerticalEdges = mCardBounds.height() - 2 * inset > 0
        // LT
        var saved = canvas.save()
        canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawHorizontalEdges) {
            canvas.drawRect(
                0f, edgeShadowTop,
                mCardBounds.width() - 2 * inset, -mCornerRadius,
                mEdgeShadowPaint
            )
        }
        canvas.restoreToCount(saved)
        // RB
        saved = canvas.save()
        canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - inset)
        canvas.rotate(180f)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawHorizontalEdges) {
            canvas.drawRect(
                0f, edgeShadowTop,
                mCardBounds.width() - 2 * inset, -mCornerRadius + mShadowSize,
                mEdgeShadowPaint
            )
        }
        canvas.restoreToCount(saved)
        // LB
        saved = canvas.save()
        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - inset)
        canvas.rotate(270f)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawVerticalEdges) {
            canvas.drawRect(
                0f, edgeShadowTop,
                mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint
            )
        }
        canvas.restoreToCount(saved)
        // RT
        saved = canvas.save()
        canvas.translate(mCardBounds.right - inset, mCardBounds.top + inset)
        canvas.rotate(90f)
        canvas.drawPath(mCornerShadowPath!!, mCornerShadowPaint)
        if (drawVerticalEdges) {
            canvas.drawRect(
                0f, edgeShadowTop,
                mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint
            )
        }
        canvas.restoreToCount(saved)
    }

    private fun buildShadowCorners() {
        val innerBounds = RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius)
        val outerBounds = RectF(innerBounds)
        outerBounds.inset(-mShadowSize, -mShadowSize)
        if (mCornerShadowPath == null) {
            mCornerShadowPath = Path()
        } else {
            mCornerShadowPath!!.reset()
        }
        mCornerShadowPath!!.fillType = Path.FillType.EVEN_ODD
        mCornerShadowPath!!.moveTo(-mCornerRadius, 0f)
        mCornerShadowPath!!.rLineTo(-mShadowSize, 0f)
        // outer arc
        mCornerShadowPath!!.arcTo(outerBounds, 180f, 90f, false)
        // inner arc
        mCornerShadowPath!!.arcTo(innerBounds, 270f, -90f, false)
        mCornerShadowPath!!.close()
        val startRatio = mCornerRadius / (mCornerRadius + mShadowSize)

        // 获取当前状态下的阴影颜色
        val starColor =
            mShadowStartColor!!.getColorForState(state, mShadowStartColor!!.defaultColor)
        val endColor = mShadowEndColor!!.getColorForState(state, mShadowEndColor!!.defaultColor)
        // 设置阴影颜色
        mCornerShadowPaint.shader = RadialGradient(
            0f,
            0f,
            mCornerRadius + mShadowSize,
            intArrayOf(starColor, starColor, endColor),
            floatArrayOf(0f, startRatio, 1f),
            Shader.TileMode.CLAMP
        )

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.shader = LinearGradient(
            0f,
            -mCornerRadius + mShadowSize,
            0f,
            -mCornerRadius - mShadowSize,
            intArrayOf(starColor, starColor, endColor),
            floatArrayOf(0f, .5f, 1f),
            Shader.TileMode.CLAMP
        )
        mEdgeShadowPaint.isAntiAlias = false
    }

    private fun buildComponents(bounds: Rect) {
        // Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        val verticalOffset = mRawMaxShadowSize * SHADOW_MULTIPLIER
        mCardBounds[bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset, bounds.right - mRawMaxShadowSize] =
            bounds.bottom - verticalOffset
        buildShadowCorners()
    }

    var cornerRadius: Float
        get() = mCornerRadius
        set(radius) {
            var radius = radius
            require(radius >= 0f) { "Invalid radius $radius. Must be >= 0" }
            radius = (radius + .5f).toInt().toFloat()
            if (mCornerRadius == radius) {
                return
            }
            mCornerRadius = radius
            mDirty = true
            invalidateSelf()
        }

    fun getMaxShadowAndCornerPadding(into: Rect) {
        getPadding(into)
    }

    var shadowSize: Float
        get() = mRawShadowSize
        set(size) {
            setShadowSize(size, mRawMaxShadowSize)
        }
    var maxShadowSize: Float
        get() = mRawMaxShadowSize
        set(size) {
            setShadowSize(mRawShadowSize, size)
        }
    val minWidth: Float
        get() {
            val content = (2
                    * Math.max(
                mRawMaxShadowSize,
                mCornerRadius + mInsetShadow + mRawMaxShadowSize / 2
            ))
            return content + (mRawMaxShadowSize + mInsetShadow) * 2
        }
    val minHeight: Float
        get() {
            val content = 2 * Math.max(
                mRawMaxShadowSize,
                mCornerRadius + mInsetShadow + mRawMaxShadowSize * SHADOW_MULTIPLIER / 2
            )
            return content + (mRawMaxShadowSize * SHADOW_MULTIPLIER + mInsetShadow) * 2
        }
    var color: ColorStateList?
        get() = mBackground
        set(color) {
            setBackground(color)
            invalidateSelf()
        }

    internal interface RoundRectHelper {
        fun drawRoundRect(canvas: Canvas, bounds: RectF, cornerRadius: Float, paint: Paint?)
    }

    companion object {
        // used to calculate content padding
        private val COS_45 = Math.cos(Math.toRadians(45.0))
        private const val SHADOW_MULTIPLIER = 1.5f

        /*
     * This helper is set by CardView implementations.
     * <p>
     * Prior to API 17, canvas.drawRoundRect is expensive; which is why we need this interface
     * to draw efficient rounded rectangles before 17.
     * */
        var sRoundRectHelper: RoundRectHelper? = null
        fun calculateVerticalPadding(
            maxShadowSize: Float, cornerRadius: Float,
            addPaddingForCorners: Boolean
        ): Float {
            return if (addPaddingForCorners) {
                (maxShadowSize * SHADOW_MULTIPLIER + (1 - COS_45) * cornerRadius).toFloat()
            } else {
                maxShadowSize * SHADOW_MULTIPLIER
            }
        }

        fun calculateHorizontalPadding(
            maxShadowSize: Float, cornerRadius: Float,
            addPaddingForCorners: Boolean
        ): Float {
            return if (addPaddingForCorners) {
                (maxShadowSize + (1 - COS_45) * cornerRadius).toFloat()
            } else {
                maxShadowSize
            }
        }
    }
}