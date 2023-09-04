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

/**
 * Interface for platform specific CardView implementations.
 */
internal interface CardViewImpl {
    fun initialize(
        cardView: CardViewDelegate,
        context: Context,
        backgroundColor: ColorStateList?,
        radius: Float,
        elevation: Float,
        maxElevation: Float,
        shadowColorStart: ColorStateList?,
        shadowColorEnd: ColorStateList?
    )

    fun setRadius(cardView: CardViewDelegate, radius: Float)
    fun getRadius(cardView: CardViewDelegate): Float
    fun setElevation(cardView: CardViewDelegate, elevation: Float)
    fun getElevation(cardView: CardViewDelegate): Float
    fun initStatic()
    fun setMaxElevation(cardView: CardViewDelegate, maxElevation: Float)
    fun getMaxElevation(cardView: CardViewDelegate): Float
    fun getMinWidth(cardView: CardViewDelegate): Float
    fun getMinHeight(cardView: CardViewDelegate): Float
    fun updatePadding(cardView: CardViewDelegate)
    fun onCompatPaddingChanged(cardView: CardViewDelegate)
    fun onPreventCornerOverlapChanged(cardView: CardViewDelegate)
    fun setBackgroundColor(cardView: CardViewDelegate, color: ColorStateList?)
    fun getBackgroundColor(cardView: CardViewDelegate): ColorStateList?
}