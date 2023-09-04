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

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.RequiresApi

@RequiresApi(17)
internal open class CardViewApi17Impl : CardViewBaseImpl() {
    override fun initStatic() {
        RoundRectDrawableWithShadow.sRoundRectHelper =
            object : RoundRectDrawableWithShadow.RoundRectHelper {
                override fun drawRoundRect(
                    canvas: Canvas,
                    bounds: RectF,
                    cornerRadius: Float,
                    paint: Paint?
                ) {
                    canvas.drawRoundRect(
                        bounds,
                        cornerRadius,
                        cornerRadius,
                        paint!!
                    )
                }

            }
    }
}