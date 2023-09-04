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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

internal open class CardViewBaseImpl : CardViewImpl {
    /* synthetic access */ val mCornerRect = RectF()
    override fun initStatic() {
        // Draws a round rect using 7 draw operations. This is faster than using
        // canvas.drawRoundRect before JBMR1 because API 11-16 used alpha mask textures to draw
        // shapes.
        RoundRectDrawableWithShadow.sRoundRectHelper =
            object : RoundRectDrawableWithShadow.RoundRectHelper {
                override fun drawRoundRect(
                    canvas: Canvas,
                    bounds: RectF,
                    cornerRadius: Float,
                    paint: Paint?
                ) {
                    val twoRadius = cornerRadius * 2
                    val innerWidth = bounds.width() - twoRadius - 1
                    val innerHeight = bounds.height() - twoRadius - 1
                    if (cornerRadius >= 1f) {
                        // increment corner radius to account for half pixels.
                        val roundedCornerRadius = cornerRadius + .5f
                        mCornerRect[-roundedCornerRadius, -roundedCornerRadius, roundedCornerRadius] =
                            roundedCornerRadius
                        val saved = canvas.save()
                        canvas.translate(
                            bounds.left + roundedCornerRadius,
                            bounds.top + roundedCornerRadius
                        )
                        canvas.drawArc(mCornerRect, 180f, 90f, true, paint!!)
                        canvas.translate(innerWidth, 0f)
                        canvas.rotate(90f)
                        canvas.drawArc(mCornerRect, 180f, 90f, true, paint!!)
                        canvas.translate(innerHeight, 0f)
                        canvas.rotate(90f)
                        canvas.drawArc(mCornerRect, 180f, 90f, true, paint!!)
                        canvas.translate(innerWidth, 0f)
                        canvas.rotate(90f)
                        canvas.drawArc(mCornerRect, 180f, 90f, true, paint!!)
                        canvas.restoreToCount(saved)
                        //draw top and bottom pieces
                        canvas.drawRect(
                            bounds.left + roundedCornerRadius - 1f, bounds.top,
                            bounds.right - roundedCornerRadius + 1f,
                            bounds.top + roundedCornerRadius, paint!!
                        )
                        canvas.drawRect(
                            bounds.left + roundedCornerRadius - 1f,
                            bounds.bottom - roundedCornerRadius,
                            bounds.right - roundedCornerRadius + 1f, bounds.bottom, paint!!
                        )
                    }
                    // center
                    canvas.drawRect(
                        bounds.left, bounds.top + cornerRadius,
                        bounds.right, bounds.bottom - cornerRadius, paint!!
                    )
                }
            }
    }

    override fun initialize(
        cardView: CardViewDelegate,
        context: Context,
        backgroundColor: ColorStateList?,
        radius: Float,
        elevation: Float,
        maxElevation: Float,
        shadowColorStart: ColorStateList?,
        shadowColorEnd: ColorStateList?
    ) {
        val background = createBackground(
            context, backgroundColor, radius,
            elevation, maxElevation, shadowColorStart, shadowColorEnd
        )
        background.setAddPaddingForCorners(cardView.preventCornerOverlap)
        cardView.cardBackground = background
        updatePadding(cardView)
    }

    private fun createBackground(
        context: Context, backgroundColor: ColorStateList?, radius: Float, elevation: Float,
        maxElevation: Float, shadowColorStart: ColorStateList?, shadowColorEnd: ColorStateList?
    ): RoundRectDrawableWithShadow {
        return RoundRectDrawableWithShadow(
            context.resources, backgroundColor, radius,
            elevation, maxElevation, shadowColorStart, shadowColorEnd
        )
    }

    override fun updatePadding(cardView: CardViewDelegate) {
        val shadowPadding = Rect()
        getShadowBackground(cardView)!!.getMaxShadowAndCornerPadding(shadowPadding)
        cardView.setMinWidthHeightInternal(
            Math.ceil(getMinWidth(cardView).toDouble()).toInt(),
            Math.ceil(getMinHeight(cardView).toDouble()).toInt()
        )
        cardView.setShadowPadding(
            shadowPadding.left, shadowPadding.top,
            shadowPadding.right, shadowPadding.bottom
        )
    }

    override fun onCompatPaddingChanged(cardView: CardViewDelegate) {
        // NO OP
    }

    override fun onPreventCornerOverlapChanged(cardView: CardViewDelegate) {
        getShadowBackground(cardView)!!.setAddPaddingForCorners(cardView.preventCornerOverlap)
        updatePadding(cardView)
    }

    override fun setBackgroundColor(cardView: CardViewDelegate, color: ColorStateList?) {
        getShadowBackground(cardView)?.color = color
    }

    override fun getBackgroundColor(cardView: CardViewDelegate): ColorStateList? {
        return getShadowBackground(cardView)?.color
    }

    override fun setRadius(cardView: CardViewDelegate, radius: Float) {
        getShadowBackground(cardView)?.cornerRadius = radius
        updatePadding(cardView)
    }

    override fun getRadius(cardView: CardViewDelegate): Float {
        return getShadowBackground(cardView)?.cornerRadius ?: 0f
    }

    override fun setElevation(cardView: CardViewDelegate, elevation: Float) {
        getShadowBackground(cardView)!!.shadowSize = elevation
    }

    override fun getElevation(cardView: CardViewDelegate): Float {
        return getShadowBackground(cardView)?.shadowSize ?: 0f
    }

    override fun setMaxElevation(cardView: CardViewDelegate, maxElevation: Float) {
        getShadowBackground(cardView)?.maxShadowSize = maxElevation
        updatePadding(cardView)
    }

    override fun getMaxElevation(cardView: CardViewDelegate): Float {
        return getShadowBackground(cardView)?.maxShadowSize ?: 0f
    }

    override fun getMinWidth(cardView: CardViewDelegate): Float {
        return getShadowBackground(cardView)?.minWidth ?: 0f
    }

    override fun getMinHeight(cardView: CardViewDelegate): Float {
        return getShadowBackground(cardView)?.minHeight ?: 0f
    }

    private fun getShadowBackground(cardView: CardViewDelegate): RoundRectDrawableWithShadow? {
        return cardView.cardBackground as RoundRectDrawableWithShadow?
    }
}