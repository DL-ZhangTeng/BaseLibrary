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

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.zhangteng.base.R

/**
 * A FrameLayout with a rounded corner background and shadow.
 *
 *
 * CardView uses `elevation` property on Lollipop for shadows and falls back to a
 * custom emulated shadow implementation on older platforms.
 *
 *
 * Due to expensive nature of rounded corner clipping, on platforms before Lollipop, CardView does
 * not clip its children that intersect with rounded corners. Instead, it adds padding to avoid such
 * intersection (See [.setPreventCornerOverlap] to change this behavior).
 *
 *
 * Before Lollipop, CardView adds padding to its content and draws shadows to that area. This
 * padding amount is equal to `maxCardElevation + (1 - cos45) * cornerRadius` on the
 * sides and `maxCardElevation * 1.5 + (1 - cos45) * cornerRadius` on top and bottom.
 *
 *
 * Since padding is used to offset content for shadows, you cannot set padding on CardView.
 * Instead, you can use content padding attributes in XML or
 * [.setContentPadding] in code to set the padding between the edges of
 * the CardView and children of CardView.
 *
 *
 * Note that, if you specify exact dimensions for the CardView, because of the shadows, its content
 * area will be different between platforms before Lollipop and after Lollipop. By using api version
 * specific resource values, you can avoid these changes. Alternatively, If you want CardView to add
 * inner padding on platforms Lollipop and after as well, you can call
 * [.setUseCompatPadding] and pass `true`.
 *
 *
 * To change CardView's elevation in a backward compatible way, use
 * [.setCardElevation]. CardView will use elevation API on Lollipop and before
 * Lollipop, it will change the shadow size. To avoid moving the View while shadow size is changing,
 * shadow size is clamped by [.getMaxCardElevation]. If you want to change elevation
 * dynamically, you should call [.setMaxCardElevation] when CardView is initialized.
 *
 * @attr ref androidx.cardview.R.styleable#CardView_cardBackgroundColor
 * @attr ref androidx.cardview.R.styleable#CardView_cardCornerRadius
 * @attr ref androidx.cardview.R.styleable#CardView_cardElevation
 * @attr ref androidx.cardview.R.styleable#CardView_cardMaxElevation
 * @attr ref androidx.cardview.R.styleable#CardView_cardUseCompatPadding
 * @attr ref androidx.cardview.R.styleable#CardView_cardPreventCornerOverlap
 * @attr ref androidx.cardview.R.styleable#CardView_contentPadding
 * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingLeft
 * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingTop
 * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingRight
 * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingBottom
 */
class CardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mCompatPadding: Boolean
    private var mPreventCornerOverlap: Boolean

    /**
     * CardView requires to have a particular minimum size to draw shadows before API 21. If
     * developer also sets min width/height, they might be overridden.
     *
     *
     * CardView works around this issue by recording user given parameters and using an internal
     * method to set them.
     */
    var mUserSetMinWidth: Int = 0
    var mUserSetMinHeight: Int = 0
    val mContentPadding = Rect()
    val mShadowBounds = Rect()
    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        // NO OP
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        // NO OP
    }
    /**
     * Returns whether CardView will add inner padding on platforms Lollipop and after.
     *
     * @return `true` if CardView adds inner padding on platforms Lollipop and after to
     * have same dimensions with platforms before Lollipop.
     */
    /**
     * CardView adds additional padding to draw shadows on platforms before Lollipop.
     *
     *
     * This may cause Cards to have different sizes between Lollipop and before Lollipop. If you
     * need to align CardView with other Views, you may need api version specific dimension
     * resources to account for the changes.
     * As an alternative, you can set this flag to `true` and CardView will add the same
     * padding values on platforms Lollipop and after.
     *
     *
     * Since setting this flag to true adds unnecessary gaps in the UI, default value is
     * `false`.
     *
     * @param useCompatPadding `true>` if CardView should add padding for the shadows on
     * platforms Lollipop and above.
     * @attr ref androidx.cardview.R.styleable#CardView_cardUseCompatPadding
     */
    var useCompatPadding: Boolean
        get() = mCompatPadding
        set(useCompatPadding) {
            if (mCompatPadding != useCompatPadding) {
                mCompatPadding = useCompatPadding
                IMPL.onCompatPaddingChanged(mCardViewDelegate)
            }
        }

    /**
     * Sets the padding between the Card's edges and the children of CardView.
     *
     *
     * Depending on platform version or [.getUseCompatPadding] settings, CardView may
     * update these values before calling [View.setPadding].
     *
     * @param left   The left padding in pixels
     * @param top    The top padding in pixels
     * @param right  The right padding in pixels
     * @param bottom The bottom padding in pixels
     * @attr ref androidx.cardview.R.styleable#CardView_contentPadding
     * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingLeft
     * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingTop
     * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingRight
     * @attr ref androidx.cardview.R.styleable#CardView_contentPaddingBottom
     */
    fun setContentPadding(@Px left: Int, @Px top: Int, @Px right: Int, @Px bottom: Int) {
        mContentPadding[left, top, right] = bottom
        IMPL.updatePadding(mCardViewDelegate)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpecLocal = widthMeasureSpec
        var heightMeasureSpecLocal = heightMeasureSpec
        if (IMPL !is CardViewApi21Impl) {
            val widthMode = MeasureSpec.getMode(widthMeasureSpecLocal)
            when (widthMode) {
                MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> {
                    val minWidth =
                        Math.ceil(IMPL.getMinWidth(mCardViewDelegate).toDouble()).toInt()
                    widthMeasureSpecLocal = MeasureSpec.makeMeasureSpec(
                        Math.max(
                            minWidth,
                            MeasureSpec.getSize(widthMeasureSpecLocal)
                        ), widthMode
                    )
                }

                MeasureSpec.UNSPECIFIED -> {}
            }
            val heightMode = MeasureSpec.getMode(heightMeasureSpecLocal)
            when (heightMode) {
                MeasureSpec.EXACTLY, MeasureSpec.AT_MOST -> {
                    val minHeight =
                        Math.ceil(IMPL.getMinHeight(mCardViewDelegate).toDouble()).toInt()
                    heightMeasureSpecLocal = MeasureSpec.makeMeasureSpec(
                        Math.max(
                            minHeight,
                            MeasureSpec.getSize(heightMeasureSpecLocal)
                        ), heightMode
                    )
                }

                MeasureSpec.UNSPECIFIED -> {}
            }
            super.onMeasure(widthMeasureSpecLocal, heightMeasureSpecLocal)
        } else {
            super.onMeasure(widthMeasureSpecLocal, heightMeasureSpecLocal)
        }
    }

    override fun setMinimumWidth(minWidth: Int) {
        mUserSetMinWidth = minWidth
        super.setMinimumWidth(minWidth)
    }

    override fun setMinimumHeight(minHeight: Int) {
        mUserSetMinHeight = minHeight
        super.setMinimumHeight(minHeight)
    }

    /**
     * Updates the background color of the CardView
     *
     * @param color The new color to set for the card background
     * @attr ref androidx.cardview.R.styleable#CardView_cardBackgroundColor
     */
    fun setCardBackgroundColor(@ColorInt color: Int) {
        IMPL.setBackgroundColor(mCardViewDelegate, ColorStateList.valueOf(color))
    }

    /**
     * Updates the background ColorStateList of the CardView
     *
     * @param color The new ColorStateList to set for the card background
     * @attr ref androidx.cardview.R.styleable#CardView_cardBackgroundColor
     */
    fun setCardBackgroundColor(color: ColorStateList?) {
        IMPL.setBackgroundColor(mCardViewDelegate, color)
    }

    /**
     * Returns the background color state list of the CardView.
     *
     * @return The background color state list of the CardView.
     */
    val cardBackgroundColor: ColorStateList
        get() = IMPL.getBackgroundColor(mCardViewDelegate)!!

    /**
     * Returns the inner padding after the Card's left edge
     *
     * @return the inner padding after the Card's left edge
     */
    @get:Px
    val contentPaddingLeft: Int
        get() = mContentPadding.left

    /**
     * Returns the inner padding before the Card's right edge
     *
     * @return the inner padding before the Card's right edge
     */
    @get:Px
    val contentPaddingRight: Int
        get() = mContentPadding.right

    /**
     * Returns the inner padding after the Card's top edge
     *
     * @return the inner padding after the Card's top edge
     */
    @get:Px
    val contentPaddingTop: Int
        get() = mContentPadding.top

    /**
     * Returns the inner padding before the Card's bottom edge
     *
     * @return the inner padding before the Card's bottom edge
     */
    @get:Px
    val contentPaddingBottom: Int
        get() = mContentPadding.bottom
    /**
     * Returns the corner radius of the CardView.
     *
     * @return Corner radius of the CardView
     * @see .getRadius
     */
    /**
     * Updates the corner radius of the CardView.
     *
     * @param radius The radius in pixels of the corners of the rectangle shape
     * @attr ref androidx.cardview.R.styleable#CardView_cardCornerRadius
     * @see .setRadius
     */
    var radius: Float
        get() = IMPL.getRadius(mCardViewDelegate)
        set(radius) {
            IMPL.setRadius(mCardViewDelegate, radius)
        }
    /**
     * Returns the backward compatible elevation of the CardView.
     *
     * @return Elevation of the CardView
     * @see .setCardElevation
     * @see .getMaxCardElevation
     */
    /**
     * Updates the backward compatible elevation of the CardView.
     *
     * @param elevation The backward compatible elevation in pixels.
     * @attr ref androidx.cardview.R.styleable#CardView_cardElevation
     * @see .getCardElevation
     * @see .setMaxCardElevation
     */
    var cardElevation: Float
        get() = IMPL.getElevation(mCardViewDelegate)
        set(elevation) {
            IMPL.setElevation(mCardViewDelegate, elevation)
        }
    /**
     * Returns the backward compatible maximum elevation of the CardView.
     *
     * @return Maximum elevation of the CardView
     * @see .setMaxCardElevation
     * @see .getCardElevation
     */
    /**
     * Updates the backward compatible maximum elevation of the CardView.
     *
     *
     * Calling this method has no effect if device OS version is Lollipop or newer and
     * [.getUseCompatPadding] is `false`.
     *
     * @param maxElevation The backward compatible maximum elevation in pixels.
     * @attr ref androidx.cardview.R.styleable#CardView_cardMaxElevation
     * @see .setCardElevation
     * @see .getMaxCardElevation
     */
    var maxCardElevation: Float
        get() = IMPL.getMaxElevation(mCardViewDelegate)
        set(maxElevation) {
            IMPL.setMaxElevation(mCardViewDelegate, maxElevation)
        }
    /**
     * Returns whether CardView should add extra padding to content to avoid overlaps with rounded
     * corners on pre-Lollipop platforms.
     *
     * @return True if CardView prevents overlaps with rounded corners on platforms before Lollipop.
     * Default value is `true`.
     */
    /**
     * On pre-Lollipop platforms, CardView does not clip the bounds of the Card for the rounded
     * corners. Instead, it adds padding to content so that it won't overlap with the rounded
     * corners. You can disable this behavior by setting this field to `false`.
     *
     *
     * Setting this value on Lollipop and above does not have any effect unless you have enabled
     * compatibility padding.
     *
     * @param preventCornerOverlap Whether CardView should add extra padding to content to avoid
     * overlaps with the CardView corners.
     * @attr ref androidx.cardview.R.styleable#CardView_cardPreventCornerOverlap
     * @see .setUseCompatPadding
     */
    var preventCornerOverlap: Boolean
        get() = mPreventCornerOverlap
        set(preventCornerOverlap) {
            if (preventCornerOverlap != mPreventCornerOverlap) {
                mPreventCornerOverlap = preventCornerOverlap
                IMPL.onPreventCornerOverlapChanged(mCardViewDelegate)
            }
        }
    private val mCardViewDelegate: CardViewDelegate = object : CardViewDelegate {
        private var mCardBackground: Drawable? = null
        override val useCompatPadding: Boolean
            get() = this@CardView.useCompatPadding
        override val preventCornerOverlap: Boolean
            get() = this@CardView.preventCornerOverlap

        override fun setShadowPadding(left: Int, top: Int, right: Int, bottom: Int) {
            mShadowBounds[left, top, right] = bottom
            super@CardView.setPadding(
                left + mContentPadding.left, top + mContentPadding.top,
                right + mContentPadding.right, bottom + mContentPadding.bottom
            )
        }

        override fun setMinWidthHeightInternal(width: Int, height: Int) {
            if (width > mUserSetMinWidth) {
                super@CardView.setMinimumWidth(width)
            }
            if (height > mUserSetMinHeight) {
                super@CardView.setMinimumHeight(height)
            }
        }

        override var cardBackground: Drawable?
            get() = mCardBackground
            set(drawable) {
                mCardBackground = drawable
                setBackgroundDrawable(drawable)
            }
        override val cardView: View
            get() = this@CardView
    }

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.CardView, defStyleAttr,
            R.style.CardView
        )
        val backgroundColor: ColorStateList? = if (a.hasValue(R.styleable.CardView_cardBackgroundColor)) {
            a.getColorStateList(R.styleable.CardView_cardBackgroundColor)
        } else {
            // There isn't one set, so we'll compute one based on the theme
            @SuppressLint("ResourceType") val aa =
                getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR)
            val themeColorBackground = aa.getColor(0, 0)
            aa.recycle()

            // If the theme colorBackground is light, use our own light color, otherwise dark
            val hsv = FloatArray(3)
            Color.colorToHSV(themeColorBackground, hsv)
            ColorStateList.valueOf(
                if (hsv[2] > 0.5f) resources.getColor(R.color.cardview_light_background) else resources.getColor(
                    R.color.cardview_dark_background
                )
            )
        }
        val radius = a.getDimension(R.styleable.CardView_cardCornerRadius, 0f)
        val elevation = a.getDimension(R.styleable.CardView_cardElevation, 0f)
        var maxElevation = a.getDimension(R.styleable.CardView_cardMaxElevation, 0f)
        mCompatPadding = a.getBoolean(R.styleable.CardView_cardUseCompatPadding, false)
        mPreventCornerOverlap = a.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true)
        val defaultPadding = a.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0)
        mContentPadding.left = a.getDimensionPixelSize(
            R.styleable.CardView_contentPaddingLeft,
            defaultPadding
        )
        mContentPadding.top = a.getDimensionPixelSize(
            R.styleable.CardView_contentPaddingTop,
            defaultPadding
        )
        mContentPadding.right = a.getDimensionPixelSize(
            R.styleable.CardView_contentPaddingRight,
            defaultPadding
        )
        mContentPadding.bottom = a.getDimensionPixelSize(
            R.styleable.CardView_contentPaddingBottom,
            defaultPadding
        )
        if (elevation > maxElevation) {
            maxElevation = elevation
        }
        mUserSetMinWidth = a.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0)
        mUserSetMinHeight = a.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0)
        val shadowColorStart = a.getColorStateList(R.styleable.CardView_cardShadowColorStart)
        val shadowColorEnd = a.getColorStateList(R.styleable.CardView_cardShadowColorEnd)
        a.recycle()
        IMPL.initialize(
            mCardViewDelegate, context, backgroundColor, radius,
            elevation, maxElevation, shadowColorStart, shadowColorEnd
        )
    }

    companion object {
        private val COLOR_BACKGROUND_ATTR = intArrayOf(android.R.attr.colorBackground)
        private val IMPL: CardViewImpl =
            if (Build.VERSION.SDK_INT >= 21) {
                CardViewApi21Impl()
            } else if (Build.VERSION.SDK_INT >= 17) {
                CardViewApi17Impl()
            } else {
                CardViewBaseImpl()
            }

        init {
            IMPL.initStatic()
        }
    }
}