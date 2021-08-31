package com.zhangteng.base.widget

/**
 * Created by Zach on 5/27/2017.
 */
interface IEmoRatingListener {
    /**
     * Notice when the user has selected a final rating.
     *
     * @param rating
     */
    fun onRatingFinal(rating: Int)

    /**
     * Notice when the user has canceled the rating.
     */
    fun onRatingCancel()

    /**
     * Notice when the user is being rated you.
     *
     * @param rating
     */
    fun onRatingPending(rating: Int)
}