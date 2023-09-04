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

import android.graphics.drawable.Drawable
import android.view.View

/**
 * Interface provided by CardView to implementations.
 *
 *
 * Necessary to resolve circular dependency between base CardView and platform implementations.
 */
internal interface CardViewDelegate {
    var cardBackground: Drawable?
    val useCompatPadding: Boolean
    val preventCornerOverlap: Boolean
    fun setShadowPadding(left: Int, top: Int, right: Int, bottom: Int)
    fun setMinWidthHeightInternal(width: Int, height: Int)
    val cardView: View
}