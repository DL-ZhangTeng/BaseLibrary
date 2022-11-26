package com.zhangteng.base.recyclerview.layout.banner

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import java.lang.ref.WeakReference

/**
 * description: BannerLayoutManager
 * author: Swing
 * date: 2022/11/26
 */
class BannerLayoutManager : LinearLayoutManager {
    private var mLinearSnapHelper: LinearSnapHelper?
    var recyclerView: RecyclerView
        private set
    private var mOnSelectedViewListener: OnSelectedViewListener? = null
    private var mRealCount: Int
    private var mCurrentPosition = 0
    private var mHandler: TaskHandler
    private var mTimeDelayed: Long = 1000
    private var mOrientation: Int
    private var mTimeSmooth = 150f

    constructor(context: Context?, recyclerView: RecyclerView, realCount: Int) : super(context) {
        mLinearSnapHelper = LinearSnapHelper()
        mRealCount = realCount
        mHandler = TaskHandler(this)
        this.recyclerView = recyclerView
        orientation = HORIZONTAL
        this.mOrientation = HORIZONTAL
    }

    constructor(
        context: Context?,
        recyclerView: RecyclerView,
        realCount: Int,
        orientation: Int
    ) : super(context) {
        mLinearSnapHelper = LinearSnapHelper()
        mRealCount = realCount
        mHandler = TaskHandler(this)
        this.recyclerView = recyclerView
        setOrientation(orientation)
        this.mOrientation = orientation
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        mLinearSnapHelper!!.attachToRecyclerView(view)
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val smoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.context) {
                // 返回：滑过1px时经历的时间(ms)。
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return mTimeSmooth / displayMetrics.densityDpi
                }
            }
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) { //滑动停止
            if (mLinearSnapHelper != null) {
                val view = mLinearSnapHelper!!.findSnapView(this)
                mCurrentPosition = getPosition(view!!)
                if (mOnSelectedViewListener != null) mOnSelectedViewListener!!.onSelectedView(
                    view,
                    mCurrentPosition % mRealCount
                )
                mHandler.setSendMsg(true)
                val msg = Message.obtain()
                mCurrentPosition++
                msg.what = mCurrentPosition
                mHandler.sendMessageDelayed(msg, mTimeDelayed)
            }
        } else if (state == RecyclerView.SCROLL_STATE_DRAGGING) { //拖动
            mHandler.setSendMsg(false)
        }
    }

    fun setTimeDelayed(timeDelayed: Long) {
        mTimeDelayed = timeDelayed
    }

    fun setTimeSmooth(timeSmooth: Float) {
        mTimeSmooth = timeSmooth
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        mHandler.setSendMsg(true)
        val msg = Message.obtain()
        msg.what = mCurrentPosition + 1
        mHandler.sendMessageDelayed(msg, mTimeDelayed)
    }

    fun setOnSelectedViewListener(listener: OnSelectedViewListener?) {
        mOnSelectedViewListener = listener
    }

    private class TaskHandler(bannerLayoutManager: BannerLayoutManager) : Handler() {
        private val mWeakBanner: WeakReference<BannerLayoutManager>
        private var mSendMsg = false

        init {
            mWeakBanner = WeakReference(bannerLayoutManager)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg != null && mSendMsg) {
                val position = msg.what
                val bannerLayoutManager = mWeakBanner.get()
                bannerLayoutManager?.recyclerView?.smoothScrollToPosition(position)
            }
        }

        fun setSendMsg(sendMsg: Boolean) {
            mSendMsg = sendMsg
        }
    }
}