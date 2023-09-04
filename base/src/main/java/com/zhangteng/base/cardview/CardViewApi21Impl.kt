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

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.RequiresApi

@RequiresApi(21)
internal class CardViewApi21Impl : CardViewApi17Impl() {
    // 标记 - 是否使用低版本实现
    private var useLower = false
    override fun initialize(
        cardView: CardViewDelegate, context: Context,
        backgroundColor: ColorStateList?, radius: Float, elevation: Float, maxElevation: Float,
        shadowColorStart: ColorStateList?, shadowColorEnd: ColorStateList?
    ) {

        // 没有自定义阴影颜色，不使用低版本实现
        if (shadowColorStart == null && shadowColorEnd == null) {
            useLower = false
            val background = RoundRectDrawable(backgroundColor, radius)
            cardView.cardBackground = background
            val view = cardView.cardView
            view.clipToOutline = true
            view.elevation = elevation
            setMaxElevation(cardView, maxElevation)
        } else {
            // 配置了自定义颜色，使用低版本实现
            useLower = true
            super.initialize(
                cardView,
                context,
                backgroundColor,
                radius,
                elevation,
                maxElevation,
                shadowColorStart,
                shadowColorEnd
            )
        }
    }

    override fun setRadius(cardView: CardViewDelegate, radius: Float) {
        if (useLower) {
            super.setRadius(cardView, radius)
        } else {
            getCardBackground(cardView)?.radius = radius
        }
    }

    override fun setMaxElevation(cardView: CardViewDelegate, maxElevation: Float) {
        if (useLower) {
            super.setMaxElevation(cardView, maxElevation)
        } else {
            getCardBackground(cardView)!!.setPadding(
                maxElevation,
                cardView.useCompatPadding, cardView.preventCornerOverlap
            )
            updatePadding(cardView)
        }
    }

    override fun getMaxElevation(cardView: CardViewDelegate): Float {
        return if (useLower) {
            super.getMaxElevation(cardView)
        } else {
            getCardBackground(cardView)!!.padding
        }
    }

    override fun getMinWidth(cardView: CardViewDelegate): Float {
        return if (useLower) {
            super.getMinWidth(cardView)
        } else {
            getRadius(cardView) * 2
        }
    }

    override fun getMinHeight(cardView: CardViewDelegate): Float {
        return if (useLower) {
            super.getMinHeight(cardView)
        } else {
            getRadius(cardView) * 2
        }
    }

    override fun getRadius(cardView: CardViewDelegate): Float {
        return if (useLower) {
            super.getRadius(cardView)
        } else {
            getCardBackground(cardView)?.radius ?: 0f
        }
    }

    override fun setElevation(cardView: CardViewDelegate, elevation: Float) {
        if (useLower) {
            super.setElevation(cardView, elevation)
        } else {
            cardView.cardView.elevation = elevation
        }
    }

    override fun getElevation(cardView: CardViewDelegate): Float {
        return if (useLower) {
            super.getElevation(cardView)
        } else {
            cardView.cardView.elevation
        }
    }

    override fun updatePadding(cardView: CardViewDelegate) {
        if (useLower) {
            super.updatePadding(cardView)
        } else {
            if (!cardView.useCompatPadding) {
                cardView.setShadowPadding(0, 0, 0, 0)
                return
            }
            val elevation = getMaxElevation(cardView)
            val radius = getRadius(cardView)
            val hPadding = Math.ceil(
                RoundRectDrawableWithShadow.Companion.calculateHorizontalPadding(
                    elevation,
                    radius,
                    cardView.preventCornerOverlap
                ).toDouble()
            ).toInt()
            val vPadding = Math.ceil(
                RoundRectDrawableWithShadow.Companion.calculateVerticalPadding(
                    elevation,
                    radius,
                    cardView.preventCornerOverlap
                ).toDouble()
            ).toInt()
            cardView.setShadowPadding(hPadding, vPadding, hPadding, vPadding)
        }
    }

    override fun onCompatPaddingChanged(cardView: CardViewDelegate) {
        if (useLower) {
            super.onCompatPaddingChanged(cardView)
        } else {
            setMaxElevation(cardView, getMaxElevation(cardView))
        }
    }

    override fun onPreventCornerOverlapChanged(cardView: CardViewDelegate) {
        if (useLower) {
            super.onPreventCornerOverlapChanged(cardView)
        } else {
            setMaxElevation(cardView, getMaxElevation(cardView))
        }
    }

    override fun setBackgroundColor(cardView: CardViewDelegate, color: ColorStateList?) {
        if (useLower) {
            super.setBackgroundColor(cardView, color)
        } else {
            getCardBackground(cardView)?.color = color
        }
    }

    override fun getBackgroundColor(cardView: CardViewDelegate): ColorStateList? {
        return if (useLower) {
            super.getBackgroundColor(cardView)
        } else {
            getCardBackground(cardView)?.color
        }
    }

    private fun getCardBackground(cardView: CardViewDelegate): RoundRectDrawable? {
        return cardView.cardBackground as RoundRectDrawable
    }
}