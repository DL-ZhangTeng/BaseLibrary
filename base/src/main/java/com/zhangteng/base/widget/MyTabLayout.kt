package com.zhangteng.base.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.*
import android.content.res.ColorStateList
import android.database.DataSetObserver
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.util.*
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.TooltipCompat
import androidx.core.util.Pools
import androidx.core.util.Pools.SynchronizedPool
import androidx.core.view.GravityCompat
import androidx.core.view.PointerIconCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.*
import com.zhangteng.base.R
import com.zhangteng.base.utils.TextUtil
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Created by swing on 2018/7/19.
 */
@DecorView
open class MyTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {
    var mTabBackgroundResId = 0
    private val mTabs: ArrayList<Tab?> = ArrayList()
    private val mTabStrip: SlidingTabStrip?
    private var mRequestedTabMinWidth: Int
    private val mRequestedTabMaxWidth: Int
    private val mScrollableTabMinWidth: Int
    private val mSelectedListeners: ArrayList<OnTabSelectedListener?> = ArrayList()

    // Pool we use as a simple RecyclerBin
    private val mTabViewPool: Pools.Pool<TabView?> = Pools.SimplePool(12)
    var mTabPaddingStart: Int
    var mTabPaddingTop: Int
    var mTabPaddingEnd: Int
    var mTabPaddingBottom: Int
    var mTabViewSelf = false
    var mTabTextAppearance: Int
    var mTabTypeface: Int
    var mTabSelectedTypeface: Int
    var mTabTextColors: ColorStateList? = null
    var mTabTextSize: Float
    var mTabSelectedTextSize: Float
    var mTabTextMultiLineSize: Float
    var mTabMaxWidth = Int.MAX_VALUE
    var mTabGravity: Int
    var mMode: Int
    var mViewPager: ViewPager? = null
    private var mSelectedTab: Tab? = null
    private val mContentInsetStart: Int
    private var mSelectedListener: OnTabSelectedListener? = null
    private var mCurrentVpSelectedListener: OnTabSelectedListener? = null
    private var mScrollAnimator: ValueAnimator? = null
    private var mPagerAdapter: PagerAdapter? = null
    private var mPagerAdapterObserver: DataSetObserver? = null
    private var mPageChangeListener: TabLayoutOnPageChangeListener? = null
    private var mAdapterChangeListener: AdapterChangeListener? = null
    private var mSetupViewPagerImplicitly = false

    /**
     * Sets the tab indicator's color for the currently selected tab.
     *
     * @param color color to use for the indicator
     * @attr ref android.support.design.R.styleable#TabLayout_tabIndicatorColor
     */
    open fun setSelectedTabIndicatorColor(@ColorInt color: Int) {
        mTabStrip?.setSelectedIndicatorColor(color)
    }

    /**
     * Sets the tab indicator's height for the currently selected tab.
     *
     * @param height height to use for the indicator in pixels
     * @attr ref android.support.design.R.styleable#TabLayout_tabIndicatorHeight
     */
    open fun setSelectedTabIndicatorHeight(height: Int) {
        mTabStrip?.setSelectedIndicatorHeight(height)
    }

    open fun setSelectedIndicatorPaddingRight(right: Int) {
        mTabStrip?.setSelectedIndicatorPaddingRight(right)
    }

    open fun setSelectedIndicatorPaddingLeft(left: Int) {
        mTabStrip?.setSelectedIndicatorPaddingLeft(left)
    }

    /**
     * Set the scroll position of the tabs. This is useful for when the tabs are being displayed as
     * part of a scrolling container such as [ViewPager].
     *
     *
     * Calling this method does not update the selected tab, it is only used for drawing purposes.
     *
     * @param position           current scroll position
     * @param positionOffset     Value from [0, 1) indicating the offset from `position`.
     * @param updateSelectedText Whether to update the text's selected state.
     */
    open fun setScrollPosition(position: Int, positionOffset: Float, updateSelectedText: Boolean) {
        setScrollPosition(position, positionOffset, updateSelectedText, true)
    }

    open fun setScrollPosition(
        position: Int, positionOffset: Float, updateSelectedText: Boolean,
        updateIndicatorPosition: Boolean,
    ) {
        val roundedPosition = (position + positionOffset).roundToInt()
        if (roundedPosition < 0 || mTabStrip == null || roundedPosition >= mTabStrip.childCount) {
            return
        }

        // Set the indicator position, if enabled
        if (updateIndicatorPosition) {
            mTabStrip.setIndicatorPositionFromTabPosition(position, positionOffset)
        }

        // Now update the scroll position, canceling any running animation
        if (mScrollAnimator != null && mScrollAnimator!!.isRunning) {
            mScrollAnimator!!.cancel()
        }
        scrollTo(calculateScrollXForTab(position, positionOffset), 0)

        // Update the 'selected state' view as we scroll, if enabled
        if (updateSelectedText) {
            setSelectedTabView(roundedPosition)
        }
    }

    private fun getScrollPosition(): Float? {
        return mTabStrip?.getIndicatorPosition()
    }
    /**
     * Add a tab to this layout. The tab will be added at the end of the list.
     *
     * @param tab         Tab to add
     * @param setSelected True if the added tab should become the selected tab.
     */
    /**
     * Add a tab to this layout. The tab will be added at the end of the list.
     * If this is the first tab to be added it will become the selected tab.
     *
     * @param tab Tab to add
     */
    @JvmOverloads
    open fun addTab(tab: Tab, setSelected: Boolean = mTabs.isEmpty()) {
        addTab(tab, mTabs.size, setSelected)
    }
    /**
     * Add a tab to this layout. The tab will be inserted at `position`.
     *
     * @param tab         The tab to add
     * @param position    The new position of the tab
     * @param setSelected True if the added tab should become the selected tab.
     */
    /**
     * Add a tab to this layout. The tab will be inserted at `position`.
     * If this is the first tab to be added it will become the selected tab.
     *
     * @param tab      The tab to add
     * @param position The new position of the tab
     */
    @JvmOverloads
    open fun addTab(tab: Tab, position: Int, setSelected: Boolean = mTabs.isEmpty()) {
        require(!(tab.mParent !== this)) { "Tab belongs to a different TabLayout." }
        configureTab(tab, position)
        addTabView(tab)
        if (setSelected) {
            tab.select()
        }
    }

    private fun addTabFromItemView(item: MyTabItem) {
        val tab = newTab()
        if (item.mText != null) {
            tab.setText(item.mText)
        }
        if (item.mIcon != null) {
            tab.setIcon(item.mIcon)
        }
        if (item.mCustomLayout != 0) {
            tab.setCustomView(item.mCustomLayout)
        }
        if (!TextUtils.isEmpty(item.contentDescription)) {
            tab.setContentDescription(item.contentDescription)
        }
        addTab(tab)
    }

    @Deprecated(
        """Use {@link #addOnTabSelectedListener(MyTabLayout.OnTabSelectedListener)} and
      {@link #removeOnTabSelectedListener(MyTabLayout.OnTabSelectedListener)}."""
    )
    open fun setOnTabSelectedListener(listener: OnTabSelectedListener?) {
        // The logic in this method emulates what we had before support for multiple
        // registered listeners.
        mSelectedListener?.let { removeOnTabSelectedListener(it) }
        // Update the deprecated field so that we can remove the passed listener the next
        // time we're called
        mSelectedListener = listener
        listener?.let { addOnTabSelectedListener(it) }
    }

    /**
     * Add a [MyTabLayout.OnTabSelectedListener] that will be invoked when tab selection
     * changes.
     *
     *
     *
     * Components that add a listener should take care to remove it when finished via
     * [.removeOnTabSelectedListener].
     *
     * @param listener listener to add
     */
    open fun addOnTabSelectedListener(listener: OnTabSelectedListener) {
        if (!mSelectedListeners.contains(listener)) {
            mSelectedListeners.add(listener)
        }
    }

    /**
     * Remove the given [MyTabLayout.OnTabSelectedListener] that was previously added via
     * [.addOnTabSelectedListener].
     *
     * @param listener listener to remove
     */
    open fun removeOnTabSelectedListener(listener: OnTabSelectedListener) {
        mSelectedListeners.remove(listener)
    }

    /**
     * Remove all previously added [MyTabLayout.OnTabSelectedListener]s.
     */
    open fun clearOnTabSelectedListeners() {
        mSelectedListeners.clear()
    }

    /**
     * Create and return a new [MyTabLayout.Tab]. You need to manually add this using
     * [.addTab] or a related method.
     *
     * @return A new Tab
     * @see .addTab
     */
    open fun newTab(): Tab {
        var tab = sTabPool?.acquire()
        if (tab == null) {
            tab = Tab()
        }
        tab.mParent = this
        tab.mView = createTabView(tab)
        return tab
    }

    /**
     * Returns the number of tabs currently registered with the action bar.
     *
     * @return Tab count
     */
    open fun getTabCount(): Int {
        return mTabs.size
    }

    /**
     * Returns the tab at the specified index.
     */
    open fun getTabAt(index: Int): Tab? {
        return if (index < 0 || index >= getTabCount()) null else mTabs.get(index)
    }

    /**
     * Returns the position of the current selected tab.
     *
     * @return selected tab position, or `-1` if there isn't a selected tab.
     */
    open fun getSelectedTabPosition(): Int {
        return mSelectedTab?.getPosition() ?: -1
    }

    /**
     * Remove a tab from the layout. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param tab The tab to remove
     */
    open fun removeTab(tab: Tab?) {
        require(!(tab?.mParent !== this)) { "Tab does not belong to this TabLayout." }
        if (tab != null) {
            removeTabAt(tab.getPosition())
        }
    }

    /**
     * Remove a tab from the layout. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param position Position of the tab to remove
     */
    open fun removeTabAt(position: Int) {
        val selectedTabPosition = mSelectedTab?.getPosition() ?: 0
        removeTabViewAt(position)
        val removedTab = mTabs.removeAt(position)
        if (removedTab != null) {
            removedTab.reset()
            sTabPool?.release(removedTab)
        }
        val newTabCount = mTabs.size
        for (i in position until newTabCount) {
            mTabs[i]?.setPosition(i)
        }
        if (selectedTabPosition == position) {
            selectTab(if (mTabs.isEmpty()) null else mTabs[max(0, position - 1)])
        }
    }

    /**
     * Remove all tabs from the action bar and deselect the current tab.
     */
    open fun removeAllTabs() {
        // Remove all the views
        if (mTabStrip != null)
            for (i in mTabStrip.childCount - 1 downTo 0) {
                removeTabViewAt(i)
            }
        val i = mTabs.iterator()
        while (i.hasNext()) {
            val tab = i.next()
            i.remove()
            tab?.let {
                it.reset()
                sTabPool?.release(it)
            }
        }
        mSelectedTab = null
    }

    /**
     * Returns the current mode used by this [MyTabLayout].
     *
     * @see .setTabMode
     */
    @Mode
    open fun getTabMode(): Int {
        return mMode
    }

    /**
     * Set the behavior mode for the Tabs in this layout. The valid input options are:
     *
     *  * [.MODE_FIXED]: Fixed tabs display all tabs concurrently and are best used
     * with content that benefits from quick pivots between tabs.
     *  * [.MODE_SCROLLABLE]: Scrollable tabs display a subset of tabs at any given moment,
     * and can contain longer tab labels and a larger number of tabs. They are best used for
     * browsing contexts in touch interfaces when users don’t need to directly compare the tab
     * labels. This mode is commonly used with a [ViewPager].
     *
     *
     * @param mode one of [.MODE_FIXED] or [.MODE_SCROLLABLE].
     * @attr ref android.support.design.R.styleable#TabLayout_tabMode
     */
    open fun setTabMode(@Mode mode: Int) {
        if (mode != mMode) {
            mMode = mode
            applyModeAndGravity()
        }
    }

    /**
     * The current gravity used for laying out tabs.
     *
     * @return one of [.GRAVITY_CENTER] or [.GRAVITY_FILL].
     */
    @TabGravity
    open fun getTabGravity(): Int {
        return mTabGravity
    }

    /**
     * Set the gravity to use when laying out the tabs.
     *
     * @param gravity one of [.GRAVITY_CENTER] or [.GRAVITY_FILL].
     * @attr ref android.support.design.R.styleable#TabLayout_tabGravity
     */
    open fun setTabGravity(@TabGravity gravity: Int) {
        if (mTabGravity != gravity) {
            mTabGravity = gravity
            applyModeAndGravity()
        }
    }

    /**
     * Gets the text colors for the different states (normal, selected) used for the tabs.
     */
    open fun getTabTextColors(): ColorStateList? {
        return mTabTextColors
    }

    /**
     * Sets the text colors for the different states (normal, selected) used for the tabs.
     *
     * @see .getTabTextColors
     */
    open fun setTabTextColors(textColor: ColorStateList?) {
        if (mTabTextColors !== textColor) {
            mTabTextColors = textColor
            updateAllTabs()
        }
    }

    /**
     * Sets the text colors for the different states (normal, selected) used for the tabs.
     *
     * @attr ref android.support.design.R.styleable#TabLayout_tabTextColor
     * @attr ref android.support.design.R.styleable#TabLayout_tabSelectedTextColor
     */
    open fun setTabTextColors(normalColor: Int, selectedColor: Int) {
        setTabTextColors(createColorStateList(normalColor, selectedColor))
    }
    /**
     * The one-stop shop for setting up this [MyTabLayout] with a [ViewPager].
     *
     *
     *
     * This method will link the given ViewPager and this TabLayout together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks. The tabs displayed in this layout will be populated
     * from the ViewPager adapter's page titles.
     *
     *
     *
     * If `autoRefresh` is `true`, any changes in the [PagerAdapter] will
     * trigger this layout to re-populate itself from the adapter's titles.
     *
     *
     *
     * If the given ViewPager is non-null, it needs to already have a
     * [PagerAdapter] set.
     *
     * @param viewPager   the ViewPager to link to, or `null` to clear any previous link
     * @param autoRefresh whether this layout should refresh its contents if the given ViewPager's
     * content changes
     */
    /**
     * The one-stop shop for setting up this [MyTabLayout] with a [ViewPager].
     *
     *
     *
     * This is the same as calling [.setupWithViewPager] with
     * auto-refresh enabled.
     *
     * @param viewPager the ViewPager to link to, or `null` to clear any previous link
     */
    @JvmOverloads
    open fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean = true) {
        setupWithViewPager(viewPager, autoRefresh, false)
    }

    private fun setupWithViewPager(
        viewPager: ViewPager?, autoRefresh: Boolean,
        implicitSetup: Boolean,
    ) {
        if (mViewPager != null) {
            // If we've already been setup with a ViewPager, remove us from it
            if (mPageChangeListener != null) {
                mViewPager!!.removeOnPageChangeListener(mPageChangeListener!!)
            }
            if (mAdapterChangeListener != null) {
                mViewPager!!.removeOnAdapterChangeListener(mAdapterChangeListener!!)
            }
        }
        if (mCurrentVpSelectedListener != null) {
            // If we already have a tab selected listener for the ViewPager, remove it
            removeOnTabSelectedListener(mCurrentVpSelectedListener!!)
            mCurrentVpSelectedListener = null
        }
        if (viewPager != null) {
            mViewPager = viewPager

            // Add our custom OnPageChangeListener to the ViewPager
            if (mPageChangeListener == null) {
                mPageChangeListener = TabLayoutOnPageChangeListener(this)
            }
            mPageChangeListener!!.reset()
            viewPager.addOnPageChangeListener(mPageChangeListener!!)

            // Now we'll add a tab selected listener to set ViewPager's current item
            mCurrentVpSelectedListener = ViewPagerOnTabSelectedListener(viewPager)
            addOnTabSelectedListener(mCurrentVpSelectedListener as ViewPagerOnTabSelectedListener)
            val adapter = viewPager.adapter
            adapter?.let { setPagerAdapter(it, autoRefresh) }

            // Add a listener so that we're notified of any adapter changes
            if (mAdapterChangeListener == null) {
                mAdapterChangeListener = AdapterChangeListener()
            }
            mAdapterChangeListener!!.setAutoRefresh(autoRefresh)
            viewPager.addOnAdapterChangeListener(mAdapterChangeListener!!)

            // Now update the scroll position to match the ViewPager's current item
            setScrollPosition(viewPager.currentItem, 0f, true)
        } else {
            // We've been given a null ViewPager so we need to clear out the internal state,
            // listeners and observers
            mViewPager = null
            setPagerAdapter(null, false)
        }
        mSetupViewPagerImplicitly = implicitSetup
    }

    @Deprecated(
        """Use {@link #setupWithViewPager(ViewPager)} to link a TabLayout with a ViewPager
      together. When that method is used, the TabLayout will be automatically updated
      when the {@link PagerAdapter} is changed."""
    )
    open fun setTabsFromPagerAdapter(adapter: PagerAdapter?) {
        setPagerAdapter(adapter, false)
    }

    override fun shouldDelayChildPressedState(): Boolean {
        // Only delay the pressed state if the tabs can scroll
        return getTabScrollRange() > 0
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mViewPager == null) {
            // If we don't have a ViewPager already, check if our parent is a ViewPager to
            // setup with it automatically
            val vp = parent
            if (vp is ViewPager) {
                // If we have a ViewPager parent and we've been added as part of its decor, let's
                // assume that we should automatically setup to display any titles
                setupWithViewPager(vp, true, true)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mSetupViewPagerImplicitly) {
            // If we've been setup with a ViewPager implicitly, let's clear out any listeners, etc
            setupWithViewPager(null)
            mSetupViewPagerImplicitly = false
        }
    }

    private fun getTabScrollRange(): Int {
        return Math.max(
            0, (mTabStrip?.width ?: 0) - width - paddingLeft
                    - paddingRight
        )
    }

    open fun setPagerAdapter(adapter: PagerAdapter?, addObserver: Boolean) {
        if (mPagerAdapter != null && mPagerAdapterObserver != null) {
            // If we already have a PagerAdapter, unregister our observer
            mPagerAdapter!!.unregisterDataSetObserver(mPagerAdapterObserver!!)
        }
        mPagerAdapter = adapter
        if (addObserver && adapter != null) {
            // Register our observer on the new adapter
            if (mPagerAdapterObserver == null) {
                mPagerAdapterObserver = PagerAdapterObserver()
            }
            adapter.registerDataSetObserver(mPagerAdapterObserver!!)
        }

        // Finally make sure we reflect the new adapter
        populateFromPagerAdapter()
    }

    open fun populateFromPagerAdapter() {
        removeAllTabs()
        if (mPagerAdapter != null) {
            val adapterCount = mPagerAdapter!!.count
            for (i in 0 until adapterCount) {
                addTab(newTab().setText(mPagerAdapter!!.getPageTitle(i)), false)
            }

            // Make sure we reflect the currently set ViewPager item
            if (mViewPager != null && adapterCount > 0) {
                val curItem = mViewPager!!.currentItem
                if (curItem != getSelectedTabPosition() && curItem < getTabCount()) {
                    selectTab(getTabAt(curItem))
                }
            }
        }
    }

    private fun updateAllTabs() {
        var i = 0
        val z = mTabs.size
        while (i < z) {
            mTabs[i]?.updateView()
            i++
        }
    }

    private fun createTabView(tab: Tab): TabView {
        var tabView = mTabViewPool.acquire()
        if (tabView == null) {
            tabView = TabView(context)
        }
        tabView.setTab(tab)
        tabView.isFocusable = true
        tabView.minimumWidth = getTabMinWidth()
        return tabView
    }

    private fun configureTab(tab: Tab?, position: Int) {
        tab?.setPosition(position)
        mTabs.add(position, tab)
        val count = mTabs.size
        for (i in position + 1 until count) {
            mTabs[i]?.setPosition(i)
        }
    }

    private fun addTabView(tab: Tab?) {
        tab?.let {
            val tabView = it.mView
            mTabStrip?.addView(tabView, it.getPosition(), createLayoutParamsForTabs())
        }
    }

    override fun addView(child: View?) {
        addViewInternal(child)
    }

    override fun addView(child: View?, index: Int) {
        addViewInternal(child)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        addViewInternal(child)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        addViewInternal(child)
    }

    private fun addViewInternal(child: View?) {
        if (child is MyTabItem) {
            addTabFromItemView(child)
        } else {
            throw IllegalArgumentException("Only MyTabItem instances can be added to TabLayout")
        }
    }

    private fun createLayoutParamsForTabs(): LinearLayout.LayoutParams? {
        val lp = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        updateTabViewLayoutParams(lp)
        return lp
    }

    private fun updateTabViewLayoutParams(lp: LinearLayout.LayoutParams?) {
        if (lp != null) {
            if (mMode == MODE_FIXED && mTabGravity == GRAVITY_FILL) {
                lp.width = 0
                lp.weight = 1f
            } else {
                lp.width = LinearLayout.LayoutParams.WRAP_CONTENT
                lp.weight = 0f
            }
        }
    }

    open fun dpToPx(dps: Int): Int {
        return Math.round(resources.displayMetrics.density * dps)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // If we have a MeasureSpec which allows us to decide our height, try and use the default
        // height
        var heightMeasureSpec = heightMeasureSpec
        val idealHeight = dpToPx(getDefaultHeight()) + paddingTop + paddingBottom
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST -> heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)),
                MeasureSpec.EXACTLY
            )
            MeasureSpec.UNSPECIFIED -> heightMeasureSpec =
                MeasureSpec.makeMeasureSpec(idealHeight, MeasureSpec.EXACTLY)
        }
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            // If we don't have an unspecified width spec, use the given size to calculate
            // the max tab width
            mTabMaxWidth =
                if (mRequestedTabMaxWidth > 0) mRequestedTabMaxWidth else specWidth - dpToPx(
                    TAB_MIN_WIDTH_MARGIN
                )
        }

        // Now super measure itself using the (possibly) modified height spec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (childCount == 1) {
            // If we're in fixed mode then we need to make the tab strip is the same width as us
            // so we don't scroll
            val child = getChildAt(0)
            var remeasure = false
            when (mMode) {
                MODE_SCROLLABLE ->                     // We only need to resize the child if it's smaller than us. This is similar
                    // to fillViewport
                    remeasure = child.measuredWidth < measuredWidth
                MODE_FIXED ->                     // Resize the child so that it doesn't scroll
                    remeasure = child.measuredWidth != measuredWidth
            }
            if (remeasure) {
                // Re-measure the child with a widthSpec set to be exactly our measure width
                val childHeightMeasureSpec = getChildMeasureSpec(
                    heightMeasureSpec, paddingTop
                            + paddingBottom, child.layoutParams.height
                )
                val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    measuredWidth, MeasureSpec.EXACTLY
                )
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }
    }

    private fun removeTabViewAt(position: Int) {
        val view = mTabStrip?.getChildAt(position)
        mTabStrip?.removeViewAt(position)
        if (view != null) {
            view as TabView
            view.reset()
            mTabViewPool.release(view)
        }
        requestLayout()
    }

    private fun animateToTab(newPosition: Int) {
        if (newPosition == Tab.INVALID_POSITION) {
            return
        }
        if (windowToken == null || !ViewCompat.isLaidOut(this)
            || mTabStrip == null || mTabStrip.childrenNeedLayout()
        ) {
            // If we don't have a window token, or we haven't been laid out yet just draw the new
            // position now
            setScrollPosition(newPosition, 0f, true)
            return
        }
        val startScrollX = scrollX
        val targetScrollX = calculateScrollXForTab(newPosition, 0f)
        if (startScrollX != targetScrollX) {
            ensureScrollAnimator()
            mScrollAnimator?.setIntValues(startScrollX, targetScrollX)
            mScrollAnimator?.start()
        }

        // Now animate the indicator
        mTabStrip.animateIndicatorToPosition(newPosition, ANIMATION_DURATION)
    }

    private fun ensureScrollAnimator() {
        if (mScrollAnimator == null) {
            mScrollAnimator = ValueAnimator()
            mScrollAnimator!!.interpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
            mScrollAnimator!!.duration = ANIMATION_DURATION.toLong()
            mScrollAnimator!!.addUpdateListener { animator ->
                scrollTo(
                    animator.animatedValue as Int,
                    0
                )
            }
        }
    }

    open fun setScrollAnimatorListener(listener: Animator.AnimatorListener?) {
        ensureScrollAnimator()
        mScrollAnimator?.addListener(listener)
    }

    private fun setSelectedTabView(position: Int) {
        val tabCount = mTabStrip?.childCount ?: 0
        if (position < tabCount) {
            for (i in 0 until tabCount) {
                val child = mTabStrip?.getChildAt(i)
                child?.isSelected = i == position
            }
        }
    }

    @JvmOverloads
    open fun selectTab(tab: Tab?, updateIndicator: Boolean = true) {
        val currentTab = mSelectedTab
        if (currentTab == tab) {
            if (currentTab != null && tab != null) {
                dispatchTabReselected(tab)
                animateToTab(tab.getPosition())
            }
        } else {
            val newPosition = tab?.getPosition() ?: Tab.INVALID_POSITION
            if (updateIndicator) {
                if ((currentTab == null || currentTab.getPosition() == Tab.INVALID_POSITION)
                    && newPosition != Tab.INVALID_POSITION
                ) {
                    // If we don't currently have a tab, just draw the indicator
                    setScrollPosition(newPosition, 0f, true)
                } else {
                    animateToTab(newPosition)
                }
            }
            if (newPosition != Tab.INVALID_POSITION) {
                setSelectedTabView(newPosition)
            }

            if (currentTab != null) {
                val currentTabTextView = currentTab.mView?.mTextView
                if (mTabTypeface != mTabSelectedTypeface && currentTabTextView != null && currentTabTextView.typeface.style != mTabTypeface) {
                    currentTabTextView.typeface = Typeface.defaultFromStyle(mTabTypeface)
                }
                if (mTabTextSize != mTabSelectedTextSize && currentTabTextView != null && currentTabTextView.textSize != mTabTextSize) {
                    currentTabTextView.textSize = mTabTextSize
                }
                dispatchTabUnselected(currentTab)
            }
            mSelectedTab = tab
            if (tab != null) {
                val tabTextView = tab.mView?.mTextView
                if (mTabTypeface != mTabSelectedTypeface && tabTextView != null && tabTextView.typeface.style != mTabSelectedTypeface) {
                    tabTextView.typeface = Typeface.defaultFromStyle(mTabSelectedTypeface)
                }
                if (mTabTextSize != mTabSelectedTextSize && tabTextView != null && tabTextView.textSize != mTabSelectedTextSize) {
                    tabTextView.textSize = mTabSelectedTextSize
                }
                dispatchTabSelected(tab)
            }
        }
    }

    private fun dispatchTabSelected(tab: Tab) {
        for (i in mSelectedListeners.indices.reversed()) {
            mSelectedListeners[i]?.onTabSelected(tab)
        }
    }

    private fun dispatchTabUnselected(tab: Tab) {
        for (i in mSelectedListeners.indices.reversed()) {
            mSelectedListeners[i]?.onTabUnselected(tab)
        }
    }

    private fun dispatchTabReselected(tab: Tab) {
        for (i in mSelectedListeners.indices.reversed()) {
            mSelectedListeners[i]?.onTabReselected(tab)
        }
    }

    private fun calculateScrollXForTab(position: Int, positionOffset: Float): Int {
        if (mMode == MODE_SCROLLABLE) {
            val selectedChild = mTabStrip?.getChildAt(position)
            val nextChild =
                if (position + 1 < mTabStrip?.childCount ?: 0) mTabStrip?.getChildAt(position + 1) else null
            val selectedWidth = selectedChild?.width ?: 0
            val nextWidth = nextChild?.width ?: 0

            // base scroll amount: places center of tab in center of parent
            val scrollBase = selectedChild?.left ?: 0 + selectedWidth / 2 - width / 2
            // offset amount: fraction of the distance between centers of tabs
            val scrollOffset = ((selectedWidth + nextWidth) * 0.5f * positionOffset).toInt()
            return if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR) scrollBase + scrollOffset else scrollBase - scrollOffset
        }
        return 0
    }

    private fun applyModeAndGravity() {
        var paddingStart = 0
        if (mMode == MODE_SCROLLABLE) {
            // If we're scrollable, or fixed at start, inset using padding
            paddingStart = max(0, mContentInsetStart - mTabPaddingStart)
        }
        if (mTabStrip != null)
            ViewCompat.setPaddingRelative(mTabStrip, paddingStart, 0, 0, 0)
        when (mMode) {
            MODE_FIXED -> mTabStrip?.gravity = Gravity.CENTER_HORIZONTAL
            MODE_SCROLLABLE -> mTabStrip?.gravity = GravityCompat.START
        }
        updateTabViews(true)
    }

    open fun updateTabViews(requestLayout: Boolean) {
        for (i in 0 until (mTabStrip?.childCount ?: 0)) {
            val child = mTabStrip?.getChildAt(i)
            child?.let {
                it.minimumWidth = getTabMinWidth()
                updateTabViewLayoutParams(it.layoutParams as LinearLayout.LayoutParams)
                if (requestLayout) {
                    it.requestLayout()
                }
            }
        }
    }

    private fun getDefaultHeight(): Int {
        var hasIconAndText = false
        var i = 0
        val count = mTabs.size
        while (i < count) {
            val tab = mTabs[i]
            if (tab?.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                hasIconAndText = true
                break
            }
            i++
        }
        return if (hasIconAndText) DEFAULT_HEIGHT_WITH_TEXT_ICON else DEFAULT_HEIGHT
    }

    private fun getTabMinWidth(): Int {
        if (mRequestedTabMinWidth != INVALID_WIDTH) {
            // If we have been given a min width, use it
            return mRequestedTabMinWidth
        }
        // Else, we'll use the default value
        return if (mMode == MODE_SCROLLABLE) mScrollableTabMinWidth else 0
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        // We don't care about the layout params of any views added to us, since we don't actually
        // add them. The only view we add is the SlidingTabStrip, which is done manually.
        // We return the default layout params so that we don't blow up if we're given a MyTabItem
        // without android:layout_* values.
        return generateDefaultLayoutParams()
    }

    open fun getTabMaxWidth(): Int {
        return mTabMaxWidth
    }

    /**
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @IntDef(value = [MODE_SCROLLABLE, MODE_FIXED])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Mode

    /**
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @IntDef(flag = true, value = [GRAVITY_FILL, GRAVITY_CENTER])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class TabGravity

    /**
     * Callback interface invoked when a tab's selection state changes.
     */
    interface OnTabSelectedListener {
        /**
         * Called when a tab enters the selected state.
         *
         * @param tab The tab that was selected
         */
        fun onTabSelected(tab: Tab?)

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab The tab that was unselected
         */
        fun onTabUnselected(tab: Tab?)

        /**
         * Called when a tab that is already selected is chosen again by the user. Some applications
         * may use this action to return to the top level of a category.
         *
         * @param tab The tab that was reselected.
         */
        fun onTabReselected(tab: Tab?)
    }

    /**
     * A tab in this layout. Instances can be created via [.newTab].
     */
    class Tab internal constructor() {
        internal var mParent: MyTabLayout? = null
        internal var mView: TabView? = null
        private var mTag: Any? = null
        private var mIcon: Drawable? = null
        private var mText: CharSequence? = null
        private var mContentDesc: CharSequence? = null
        private var mPosition = INVALID_POSITION
        private var mCustomView: View? = null

        /**
         * @return This Tab's tag object.
         */
        open fun getTag(): Any? {
            return mTag
        }

        /**
         * Give this Tab an arbitrary object to hold for later use.
         *
         * @param tag Object to store
         * @return The current instance for call chaining
         */
        open fun setTag(tag: Any?): Tab {
            mTag = tag
            return this
        }

        /**
         * Returns the custom view used for this tab.
         *
         * @see .setCustomView
         * @see .setCustomView
         */
        open fun getCustomView(): View? {
            return mCustomView
        }

        /**
         * Set a custom view to be used for this tab.
         *
         *
         * If the inflated layout contains a [TextView] with an ID of
         * [android.R.id.text1] then that will be updated with the value given
         * to [.setText]. Similarly, if this layout contains an
         * [ImageView] with ID [android.R.id.icon] then it will be updated with
         * the value given to [.setIcon].
         *
         *
         * @param resId A layout resource to inflate and use as a custom tab view
         * @return The current instance for call chaining
         */
        open fun setCustomView(@LayoutRes resId: Int): Tab {
            val inflater = LayoutInflater.from(mView?.context)
            return setCustomView(inflater.inflate(resId, mView, false))
        }

        /**
         * Set a custom view to be used for this tab.
         *
         *
         * If the provided view contains a [TextView] with an ID of
         * [android.R.id.text1] then that will be updated with the value given
         * to [.setText]. Similarly, if this layout contains an
         * [ImageView] with ID [android.R.id.icon] then it will be updated with
         * the value given to [.setIcon].
         *
         *
         * @param view Custom view to be used as a tab.
         * @return The current instance for call chaining
         */
        open fun setCustomView(view: View?): Tab {
            mCustomView = view
            updateView()
            return this
        }

        /**
         * Return the icon associated with this tab.
         *
         * @return The tab's icon
         */
        open fun getIcon(): Drawable? {
            return mIcon
        }

        /**
         * Set the icon displayed on this tab.
         *
         * @param resId A resource ID referring to the icon that should be displayed
         * @return The current instance for call chaining
         */
        open fun setIcon(@DrawableRes resId: Int): Tab {
            requireNotNull(mParent) { "Tab not attached to a TabLayout" }
            return setIcon(AppCompatResources.getDrawable(mParent!!.context, resId))
        }

        /**
         * Set the icon displayed on this tab.
         *
         * @param icon The drawable to use as an icon
         * @return The current instance for call chaining
         */
        open fun setIcon(icon: Drawable?): Tab {
            mIcon = icon
            updateView()
            return this
        }

        /**
         * Return the current position of this tab in the action bar.
         *
         * @return Current position, or [.INVALID_POSITION] if this tab is not currently in
         * the action bar.
         */
        open fun getPosition(): Int {
            return mPosition
        }

        open fun setPosition(position: Int) {
            mPosition = position
        }

        /**
         * Return the text of this tab.
         *
         * @return The tab's text
         */
        open fun getText(): CharSequence? {
            return mText
        }

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not room to display
         * the entire string.
         *
         * @param resId A resource ID referring to the text that should be displayed
         * @return The current instance for call chaining
         */
        open fun setText(@StringRes resId: Int): Tab {
            requireNotNull(mParent) { "Tab not attached to a TabLayout" }
            return setText(mParent!!.resources.getText(resId))
        }

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not room to display
         * the entire string.
         *
         * @param text The text to display
         * @return The current instance for call chaining
         */
        open fun setText(text: CharSequence?): Tab {
            mText = text
            updateView()
            return this
        }

        /**
         * Select this tab. Only valid if the tab has been added to the action bar.
         */
        open fun select() {
            requireNotNull(mParent) { "Tab not attached to a TabLayout" }
            mParent!!.selectTab(this)
        }

        /**
         * Returns true if this tab is currently selected.
         */
        open fun isSelected(): Boolean {
            requireNotNull(mParent) { "Tab not attached to a TabLayout" }
            return mParent!!.getSelectedTabPosition() == mPosition
        }

        /**
         * Gets a brief description of this tab's content for use in accessibility support.
         *
         * @return Description of this tab's content
         * @see .setContentDescription
         * @see .setContentDescription
         */
        open fun getContentDescription(): CharSequence? {
            return mContentDesc
        }

        /**
         * Set a description of this tab's content for use in accessibility support. If no content
         * description is provided the title will be used.
         *
         * @param resId A resource ID referring to the description text
         * @return The current instance for call chaining
         * @see .setContentDescription
         * @see .getContentDescription
         */
        open fun setContentDescription(@StringRes resId: Int): Tab {
            requireNotNull(mParent) { "Tab not attached to a TabLayout" }
            return setContentDescription(mParent!!.resources.getText(resId))
        }

        /**
         * Set a description of this tab's content for use in accessibility support. If no content
         * description is provided the title will be used.
         *
         * @param contentDesc Description of this tab's content
         * @return The current instance for call chaining
         * @see .setContentDescription
         * @see .getContentDescription
         */
        open fun setContentDescription(contentDesc: CharSequence?): Tab {
            mContentDesc = contentDesc
            updateView()
            return this
        }

        open fun updateView() {
            mView?.update()
        }

        open fun reset() {
            mParent = null
            mView = null
            mTag = null
            mIcon = null
            mText = null
            mContentDesc = null
            mPosition = INVALID_POSITION
            mCustomView = null
        }

        companion object {
            /**
             * An invalid position for a tab.
             *
             * @see .getPosition
             */
            const val INVALID_POSITION = -1
        }
    }

    /**
     * A [ViewPager.OnPageChangeListener] class which contains the
     * necessary calls back to the provided [MyTabLayout] so that the tab position is
     * kept in sync.
     *
     *
     *
     * This class stores the provided TabLayout weakly, meaning that you can use
     * [ addOnPageChangeListener(OnPageChangeListener)][ViewPager.addOnPageChangeListener] without removing the listener and
     * not cause a leak.
     */
    class TabLayoutOnPageChangeListener(myTabLayout: MyTabLayout?) : OnPageChangeListener {
        private val mTabLayoutRef: WeakReference<MyTabLayout?>?
        private var mPreviousScrollState = 0
        private var mScrollState = 0
        override fun onPageScrollStateChanged(state: Int) {
            mPreviousScrollState = mScrollState
            mScrollState = state
        }

        override fun onPageScrolled(
            position: Int, positionOffset: Float,
            positionOffsetPixels: Int,
        ) {
            val myTabLayout = mTabLayoutRef?.get()
            if (myTabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                val updateText = mScrollState != ViewPager.SCROLL_STATE_SETTLING ||
                        mPreviousScrollState == ViewPager.SCROLL_STATE_DRAGGING
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                val updateIndicator = !(mScrollState == ViewPager.SCROLL_STATE_SETTLING
                        && mPreviousScrollState == ViewPager.SCROLL_STATE_IDLE)
                myTabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator)
            }
        }

        override fun onPageSelected(position: Int) {
            val myTabLayout = mTabLayoutRef?.get()
            if (myTabLayout != null && myTabLayout.getSelectedTabPosition() != position && position < myTabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                val updateIndicator = (mScrollState == ViewPager.SCROLL_STATE_IDLE
                        || (mScrollState == ViewPager.SCROLL_STATE_SETTLING
                        && mPreviousScrollState == ViewPager.SCROLL_STATE_IDLE))
                myTabLayout.selectTab(myTabLayout.getTabAt(position), updateIndicator)
            }
        }

        open fun reset() {
            mScrollState = ViewPager.SCROLL_STATE_IDLE
            mPreviousScrollState = mScrollState
        }

        init {
            mTabLayoutRef = WeakReference(myTabLayout)
        }
    }

    /**
     * A [MyTabLayout.OnTabSelectedListener] class which contains the necessary calls back
     * to the provided [ViewPager] so that the tab position is kept in sync.
     */
    class ViewPagerOnTabSelectedListener(private val mViewPager: ViewPager?) :
        OnTabSelectedListener {
        override fun onTabSelected(tab: Tab?) {
            tab?.getPosition()?.let { mViewPager?.setCurrentItem(it, false) }
        }

        override fun onTabUnselected(tab: Tab?) {
            // No-op
        }

        override fun onTabReselected(tab: Tab?) {
            // No-op
        }
    }

    internal object AnimationUtils {
        val LINEAR_INTERPOLATOR: Interpolator = LinearInterpolator()
        val FAST_OUT_SLOW_IN_INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()
        val FAST_OUT_LINEAR_IN_INTERPOLATOR: Interpolator = FastOutLinearInInterpolator()
        val LINEAR_OUT_SLOW_IN_INTERPOLATOR: Interpolator = LinearOutSlowInInterpolator()
        val DECELERATE_INTERPOLATOR: Interpolator = DecelerateInterpolator()

        /**
         * Linear interpolation between `startValue` and `endValue` by `fraction`.
         */
        open fun lerp(startValue: Float, endValue: Float, fraction: Float): Float {
            return startValue + fraction * (endValue - startValue)
        }

        open fun lerp(startValue: Int, endValue: Int, fraction: Float): Int {
            return startValue + Math.round(fraction * (endValue - startValue))
        }
    }

    internal object ThemeUtils {
        private val APPCOMPAT_CHECK_ATTRS: IntArray = intArrayOf(
            androidx.appcompat.R.attr.colorPrimary
        )

        open fun checkAppCompatTheme(context: Context?) {
            context?.let {
                val a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS)
                val failed = !a.hasValue(0)
                a.recycle()
                require(!failed) {
                    ("You need to use a Theme.AppCompat theme "
                            + "(or descendant) with the design library.")
                }
            }
        }
    }

    internal inner class TabView(context: Context) : LinearLayout(context) {
        private var mTab: Tab? = null
        internal var mTextView: TextView? = null
        private var mIconView: ImageView? = null
        private var mCustomView: View? = null
        private var mCustomTextView: TextView? = null
        private var mCustomIconView: ImageView? = null
        private var mDefaultMaxLines = 2
        override fun performClick(): Boolean {
            val handled = super.performClick()
            return if (mTab != null) {
                if (!handled) {
                    playSoundEffect(SoundEffectConstants.CLICK)
                }
                mTab!!.select()
                true
            } else {
                handled
            }
        }

        override fun setSelected(selected: Boolean) {
            val changed = isSelected != selected
            super.setSelected(selected)
            if (changed && selected && Build.VERSION.SDK_INT < 16) {
                // Pre-JB we need to manually send the TYPE_VIEW_SELECTED event
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
            }

            // Always dispatch this to the child views, regardless of whether the value has
            // changed
            mTextView?.isSelected = selected
            mIconView?.isSelected = selected
            mCustomView?.isSelected = selected
        }

        override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
            super.onInitializeAccessibilityEvent(event)
            // This view masquerades as an action bar tab.
            event?.className = ActionBar.Tab::class.java.name
        }

        override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
            super.onInitializeAccessibilityNodeInfo(info)
            // This view masquerades as an action bar tab.
            info?.className = ActionBar.Tab::class.java.name
        }

        public override fun onMeasure(origWidthMeasureSpec: Int, origHeightMeasureSpec: Int) {
            val specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec)
            val specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec)
            val maxWidth = getTabMaxWidth()
            var widthMeasureSpec = if (maxWidth > 0 && (specWidthMode == MeasureSpec.UNSPECIFIED
                        || specWidthSize > maxWidth)
            ) {
                // If we have a max width and a given spec which is either unspecified or
                // larger than the max width, update the width spec using the same mode
                MeasureSpec.makeMeasureSpec(mTabMaxWidth, MeasureSpec.AT_MOST)
            } else {
                // Else, use the original width spec
                origWidthMeasureSpec
            }

            // Now lets measure
            super.onMeasure(widthMeasureSpec, origHeightMeasureSpec)

            if (mTabViewSelf) {
                if (mCustomView != null) {
                    widthMeasureSpec = if (mCustomView is ViewGroup) {
                        val tab = (mCustomView as ViewGroup).getChildAt(0)
                        val width = tab.measuredWidth + mTabPaddingStart + mTabPaddingEnd
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
                    } else {
                        val width = mCustomView!!.measuredWidth + mTabPaddingStart + mTabPaddingEnd
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
                    }
                } else if (mCustomTextView != null) {
                    val width = mCustomTextView!!.measuredWidth + mTabPaddingStart + mTabPaddingEnd
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
                } else if (mTextView != null) {
                    val width =
                        mTextView!!.measuredWidth + mTabPaddingStart + mTabPaddingEnd
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
                } else if (mCustomIconView != null) {
                    val width = mCustomIconView!!.measuredWidth + mTabPaddingStart + mTabPaddingEnd
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
                } else if (mIconView != null) {
                    val width = mIconView!!.measuredWidth + mTabPaddingStart + mTabPaddingEnd
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
                }
                super.onMeasure(widthMeasureSpec, origHeightMeasureSpec)
            }

            // We need to switch the text size based on whether the text is spanning 2 lines or not
            if (mTextView != null) {
                var textSize = mTabTextSize
                val selectedTextSize = mTabSelectedTextSize
                var maxLines = mDefaultMaxLines
                if (mTabSelectedTypeface != mTabTypeface) {
                    mTextView!!.typeface =
                        Typeface.defaultFromStyle(if (mTextView!!.isSelected) mTabSelectedTypeface else mTabTypeface)
                } else if (mTextView!!.typeface.style != mTabTypeface) {
                    mTextView!!.typeface = Typeface.defaultFromStyle(mTabTypeface)
                }
                if (mTabSelectedTextSize != mTabTextSize) {
                    mTextView!!.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        if (mTextView!!.isSelected) mTabSelectedTextSize else mTabTextSize
                    )
                } else if (mTextView!!.textSize != mTabTextSize) {
                    mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize)
                }

                if (mIconView != null && mIconView != null && mIconView!!.visibility == VISIBLE) {
                    // If the icon view is being displayed, we limit the text to 1 line
                    maxLines = 1
                } else if (mTextView != null && mTextView!!.lineCount > 1) {
                    // Otherwise when we have text which wraps we reduce the text size
                    textSize = mTabTextMultiLineSize
                }
                val curTextSize = mTextView!!.textSize
                val curLineCount = mTextView!!.lineCount
                val curMaxLines = TextViewCompat.getMaxLines(mTextView!!)
                if (textSize != curTextSize || selectedTextSize != curTextSize || (curMaxLines >= 0 && maxLines != curMaxLines)) {
                    // We've got a new text size and/or max lines...
                    var updateTextView = true
                    if (mMode == MODE_FIXED && textSize > curTextSize && selectedTextSize > curTextSize && curLineCount == 1) {
                        // If we're in fixed mode, going up in text size and currently have 1 line
                        // then it's very easy to get into an infinite recursion.
                        // To combat that we check to see if the change in text size
                        // will cause a line count change. If so, abort the size change and stick
                        // to the smaller size.
                        val layout = mTextView!!.layout
                        if (layout == null || approximateLineWidth(layout, 0, textSize)
                            > measuredWidth - paddingLeft - paddingRight
                        ) {
                            updateTextView = false
                        }
                    }
                    if (updateTextView) {
                        mTextView!!.maxLines = maxLines
                        super.onMeasure(widthMeasureSpec, origHeightMeasureSpec)
                    }
                }
            }
        }

        open fun reset() {
            setTab(null)
            isSelected = false
        }

        open fun update() {
            val tab = mTab
            val custom = tab?.getCustomView()
            if (custom != null) {
                val customParent = custom.parent
                if (customParent !== this) {
                    if (customParent != null) {
                        (customParent as ViewGroup).removeView(custom)
                    }
                    addView(custom)
                }
                mCustomView = custom
                mTextView?.visibility = GONE
                if (mIconView != null) {
                    mIconView!!.visibility = GONE
                    mIconView!!.setImageDrawable(null)
                }
                mCustomTextView = custom.findViewById<View?>(android.R.id.text1) as TextView
                if (mCustomTextView != null) {
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mCustomTextView!!)
                }
                mCustomIconView = custom.findViewById<View?>(android.R.id.icon) as ImageView
            } else {
                // We do not have a custom view. Remove one if it already exists
                if (mCustomView != null) {
                    removeView(mCustomView)
                    mCustomView = null
                }
                mCustomTextView = null
                mCustomIconView = null
            }
            if (mCustomView == null) {
                // If there isn't a custom view, we'll us our own in-built layouts
                if (mIconView == null) {
                    val iconView = LayoutInflater.from(context)
                        .inflate(R.layout.design_layout_tab_icon, this, false) as ImageView
                    addView(iconView, 0)
                    mIconView = iconView
                }
                if (mTextView == null) {
                    val textView = LayoutInflater.from(context)
                        .inflate(R.layout.design_layout_tab_text, this, false) as TextView
                    addView(textView)
                    mTextView = textView
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mTextView!!)
                }
                TextViewCompat.setTextAppearance(mTextView!!, mTabTextAppearance)
                if (mTabTextColors != null) {
                    mTextView?.setTextColor(mTabTextColors)
                }
                updateTextAndIcon(mTextView, mIconView)
            } else {
                // Else, we'll see if there is a TextView or ImageView present and update them
                if (mCustomTextView != null || mCustomIconView != null) {
                    updateTextAndIcon(mCustomTextView, mCustomIconView)
                }
            }

            // Finally update our selected state
            isSelected = tab != null && tab.isSelected()
        }

        private fun updateTextAndIcon(
            textView: TextView?,
            iconView: ImageView?,
        ) {
            val icon = if (mTab != null) mTab?.getIcon() else null
            val text = if (mTab != null) mTab?.getText() else null
            val contentDesc = if (mTab != null) mTab?.getContentDescription() else null
            if (iconView != null) {
                if (icon != null) {
                    iconView.setImageDrawable(icon)
                    iconView.visibility = VISIBLE
                    visibility = VISIBLE
                } else {
                    iconView.visibility = GONE
                    iconView.setImageDrawable(null)
                }
                iconView.contentDescription = contentDesc
            }
            val hasText = !TextUtils.isEmpty(text)
            if (textView != null) {
                if (hasText) {
                    textView.text = text
                    textView.visibility = VISIBLE
                    visibility = VISIBLE
                } else {
                    textView.visibility = GONE
                    textView.text = null
                }
                textView.contentDescription = contentDesc
            }
            if (iconView != null) {
                val lp = iconView.layoutParams as MarginLayoutParams
                var bottomMargin = 0
                if (hasText && iconView.visibility == VISIBLE) {
                    // If we're showing both text and icon, add some margin bottom to the icon
                    bottomMargin = gapTextIcon ?: dpToPx(DEFAULT_GAP_TEXT_ICON)
                }
                if (bottomMargin != lp.bottomMargin) {
                    lp.bottomMargin = bottomMargin
                    iconView.requestLayout()
                }
            }
            TooltipCompat.setTooltipText(this, if (hasText) null else contentDesc)
        }

        open fun getTab(): Tab? {
            return mTab
        }

        open fun setTab(tab: Tab?) {
            if (tab != mTab) {
                mTab = tab
                update()
            }
        }

        /**
         * Approximates a given lines width with the new provided text size.
         */
        private fun approximateLineWidth(layout: Layout, line: Int, textSize: Float): Float {
            return layout.getLineWidth(line) * (textSize / layout.paint.textSize)
        }

        init {
            if (mTabBackgroundResId != 0) {
                ViewCompat.setBackground(
                    this, AppCompatResources.getDrawable(context, mTabBackgroundResId)
                )
            }
            ViewCompat.setPaddingRelative(
                this, mTabPaddingStart, mTabPaddingTop,
                mTabPaddingEnd, mTabPaddingBottom
            )
            gravity = Gravity.CENTER
            orientation = VERTICAL
            isClickable = true
            ViewCompat.setPointerIcon(
                this,
                PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND)
            )
        }
    }

    private inner class SlidingTabStrip(context: Context) :
        LinearLayout(context) {
        private val mSelectedIndicatorPaint: Paint?
        var mSelectedPosition = -1
        var mSelectionOffset = 0f
        private var mSelectedIndicatorHeight = 0
        private var mLayoutDirection = -1
        private var mIndicatorLeft = -1
        private var mIndicatorRight = -1
        private var mIndicatorPaddingLeft = -1
        private var mIndicatorPaddingRight = -1
        private var mIndicatorMarginTop = -1
        private var mIndicatorMarginBottom = -1
        private var mIndicatorRoundRadius = -1
        var mTabIndicatorBottomLayer = false
        var mTabIndicatorSelfFit = true
        var mTabIndicatorMarginBottomSelfFit = true
        private var mIndicatorAnimator: ValueAnimator? = null
        open fun setSelectedIndicatorColor(color: Int) {
            if (mSelectedIndicatorPaint?.color != color) {
                mSelectedIndicatorPaint?.color = color
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorHeight(height: Int) {
            if (mSelectedIndicatorHeight != height) {
                mSelectedIndicatorHeight = height
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorPaddingRight(right: Int) {
            if (mIndicatorPaddingRight != right) {
                mIndicatorPaddingRight = right
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorPaddingLeft(left: Int) {
            if (mIndicatorPaddingLeft != left) {
                mIndicatorPaddingLeft = left
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorMarginBottom(bottom: Int) {
            if (mIndicatorMarginBottom != bottom) {
                mIndicatorMarginBottom = bottom
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorMarginTop(top: Int) {
            if (mIndicatorMarginTop != top) {
                mIndicatorMarginTop = top
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorRoundRadius(roundRadius: Int) {
            if (mIndicatorRoundRadius != roundRadius) {
                mIndicatorRoundRadius = roundRadius
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorBottomLayer(bottomLayer: Boolean) {
            if (mTabIndicatorBottomLayer != bottomLayer) {
                mTabIndicatorBottomLayer = bottomLayer
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorSelfFit(selfFit: Boolean) {
            if (mTabIndicatorSelfFit != selfFit) {
                mTabIndicatorSelfFit = selfFit
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun setSelectedIndicatorMarginBottomSelfFit(marginBottomSelfFit: Boolean) {
            if (mTabIndicatorMarginBottomSelfFit != marginBottomSelfFit) {
                mTabIndicatorMarginBottomSelfFit = marginBottomSelfFit
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun childrenNeedLayout(): Boolean {
            var i = 0
            val z = childCount
            while (i < z) {
                val child = getChildAt(i)
                if (child.width <= 0) {
                    return true
                }
                i++
            }
            return false
        }

        open fun setIndicatorPositionFromTabPosition(position: Int, positionOffset: Float) {
            if (mIndicatorAnimator != null && mIndicatorAnimator!!.isRunning) {
                mIndicatorAnimator!!.cancel()
            }
            mSelectedPosition = position
            mSelectionOffset = positionOffset
            updateIndicatorPosition()
        }

        open fun getIndicatorPosition(): Float {
            return mSelectedPosition + mSelectionOffset
        }

        override fun onRtlPropertiesChanged(layoutDirection: Int) {
            super.onRtlPropertiesChanged(layoutDirection)

            // Workaround for a bug before Android M where LinearLayout did not relayout itself when
            // layout direction changed.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (mLayoutDirection != layoutDirection) {
                    requestLayout()
                    mLayoutDirection = layoutDirection
                }
            }
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
                // HorizontalScrollView will first measure use with UNSPECIFIED, and then with
                // EXACTLY. Ignore the first call since anything we do will be overwritten anyway
                return
            }
            if (mMode == MODE_FIXED && mTabGravity == GRAVITY_CENTER) {
                val count = childCount

                // First we'll find the widest tab
                var largestTabWidth = 0
                var i = 0
                while (i < count) {
                    val child = getChildAt(i)
                    if (child.visibility == VISIBLE) {
                        largestTabWidth = Math.max(largestTabWidth, child.measuredWidth)
                    }
                    i++
                }
                if (largestTabWidth <= 0) {
                    // If we don't have a largest child yet, skip until the next measure pass
                    return
                }
                val gutter = dpToPx(FIXED_WRAP_GUTTER_MIN)
                var remeasure = false
                if (largestTabWidth * count <= measuredWidth - gutter * 2) {
                    // If the tabs fit within our width minus gutters, we will set all tabs to have
                    // the same width
                    for (i in 0 until count) {
                        val lp = getChildAt(i).layoutParams as LayoutParams
                        if (lp.width != largestTabWidth || lp.weight != 0f) {
                            lp.width = largestTabWidth
                            lp.weight = 0f
                            remeasure = true
                        }
                    }
                } else {
                    // If the tabs will wrap to be larger than the width minus gutters, we need
                    // to switch to GRAVITY_FILL
                    mTabGravity = GRAVITY_FILL
                    updateTabViews(false)
                    remeasure = true
                }
                if (remeasure) {
                    // Now re-measure after our changes
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                }
            }
        }

        override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
            super.onLayout(changed, l, t, r, b)
            if (mIndicatorAnimator != null && mIndicatorAnimator!!.isRunning) {
                // If we're currently running an animation, lets cancel it and start a
                // new animation with the remaining duration
                mIndicatorAnimator!!.cancel()
                val duration = mIndicatorAnimator!!.duration
                animateIndicatorToPosition(
                    mSelectedPosition,
                    ((1f - mIndicatorAnimator!!.animatedFraction) * duration).roundToInt()
                )
            } else {
                // If we've been layed out, update the indicator position
                updateIndicatorPosition()
            }
        }

        private fun updateIndicatorPosition() {
            val selectedTitle = getChildAt(mSelectedPosition)
            var left: Int
            var right: Int
            if (selectedTitle != null && selectedTitle.width > 0) {
                left = selectedTitle.left
                right = selectedTitle.right
                if (mSelectionOffset > 0f && mSelectedPosition < childCount - 1) {
                    // Draw the selection partway between the tabs
                    val nextTitle = getChildAt(mSelectedPosition + 1)
                    left = (mSelectionOffset * nextTitle.left +
                            (1.0f - mSelectionOffset) * left).toInt()
                    right = (mSelectionOffset * nextTitle.right +
                            (1.0f - mSelectionOffset) * right).toInt()
                }
            } else {
                right = -1
                left = right
            }
            setIndicatorPosition(left, right)
        }

        open fun setIndicatorPosition(left: Int, right: Int) {
            if (left != mIndicatorLeft || right != mIndicatorRight) {
                // If the indicator's left/right has changed, invalidate
                mIndicatorLeft = left
                mIndicatorRight = right
                ViewCompat.postInvalidateOnAnimation(this)
            }
        }

        open fun animateIndicatorToPosition(position: Int, duration: Int) {
            if (mIndicatorAnimator != null && mIndicatorAnimator!!.isRunning) {
                mIndicatorAnimator!!.cancel()
            }
            val isRtl = (ViewCompat.getLayoutDirection(this)
                    == ViewCompat.LAYOUT_DIRECTION_RTL)
            val targetView = getChildAt(position)
            if (targetView == null) {
                // If we don't have a view, just update the position now and return
                updateIndicatorPosition()
                return
            }
            val targetLeft = targetView.left
            val targetRight = targetView.right
            val startLeft: Int
            val startRight: Int
            if (Math.abs(position - mSelectedPosition) <= 1) {
                // If the views are adjacent, we'll animate from edge-to-edge
                startLeft = mIndicatorLeft
                startRight = mIndicatorRight
            } else {
                // Else, we'll just grow from the nearest edge
                val offset = dpToPx(MOTION_NON_ADJACENT_OFFSET)
                if (position < mSelectedPosition) {
                    // We're going end-to-start
                    if (isRtl) {
                        startRight = targetLeft - offset
                        startLeft = startRight
                    } else {
                        startRight = targetRight + offset
                        startLeft = startRight
                    }
                } else {
                    // We're going start-to-end
                    if (isRtl) {
                        startRight = targetRight + offset
                        startLeft = startRight
                    } else {
                        startRight = targetLeft - offset
                        startLeft = startRight
                    }
                }
            }
            if (startLeft != targetLeft || startRight != targetRight) {
                mIndicatorAnimator = ValueAnimator()
                val animator = mIndicatorAnimator
                animator?.let {
                    it.interpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
                    it.duration = duration.toLong()
                    it.setFloatValues(0f, 1f)
                    it.addUpdateListener(AnimatorUpdateListener { animator ->
                        val fraction = animator.animatedFraction
                        setIndicatorPosition(
                            AnimationUtils.lerp(startLeft, targetLeft, fraction),
                            AnimationUtils.lerp(startRight, targetRight, fraction)
                        )
                    })
                    it.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animator: Animator?) {
                            mSelectedPosition = position
                            mSelectionOffset = 0f
                        }
                    })
                    it.start()
                }
            }
        }

        override fun draw(canvas: Canvas?) {
            super.draw(canvas)
            if (!mTabIndicatorBottomLayer) {
                drawIndicator(canvas)
            }
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            if (mTabIndicatorBottomLayer) {
                drawIndicator(canvas)
            }
        }

        private fun drawIndicator(canvas: Canvas?) {
            // Thick colored underline below the current selection
            if (mTabIndicatorSelfFit) {
                if (getTabAt(getSelectedTabPosition()) != null) {
                    val textView = getTabAt(getSelectedTabPosition())!!.mView?.mTextView
                    val textWidth = TextUtil.getTextWidth(textView)
                    val width = width / getTabCount()
                    mIndicatorPaddingRight = (width - (textWidth ?: 0)) / 2
                    mIndicatorPaddingLeft = (width - (textWidth ?: 0)) / 2
                    if (mTabIndicatorMarginBottomSelfFit) {
                        val textHeight = TextUtil.getTextHeight(textView)
                        val height = height
                        mIndicatorMarginBottom = (height - (textHeight
                            ?: 0)) / 2 - mSelectedIndicatorHeight / 2
                    }
                }
            }
            if (mIndicatorLeft >= 0 && mIndicatorRight > mIndicatorLeft) {
                if (mIndicatorPaddingLeft >= 0 && mIndicatorPaddingRight >= 0 && mIndicatorRight - mIndicatorPaddingRight > mIndicatorLeft + mIndicatorPaddingLeft) {
                    if (mIndicatorMarginBottom >= 0) {
                        mSelectedIndicatorPaint?.let {
                            canvas?.drawRoundRect(
                                (mIndicatorLeft + mIndicatorPaddingLeft).toFloat(),
                                (height - mSelectedIndicatorHeight - mIndicatorMarginBottom).toFloat(),
                                (
                                        mIndicatorRight - mIndicatorPaddingRight).toFloat(),
                                (height - mIndicatorMarginBottom).toFloat(),
                                mIndicatorRoundRadius.toFloat(),
                                mIndicatorRoundRadius.toFloat(),
                                it
                            )
                        }
                    } else if (mIndicatorMarginTop >= 0) {
                        mSelectedIndicatorPaint?.let {
                            canvas?.drawRoundRect(
                                (mIndicatorLeft + mIndicatorPaddingLeft).toFloat(),
                                mIndicatorMarginTop.toFloat(),
                                (
                                        mIndicatorRight - mIndicatorPaddingRight).toFloat(),
                                mSelectedIndicatorHeight.toFloat(),
                                mIndicatorRoundRadius.toFloat(),
                                mIndicatorRoundRadius.toFloat(),
                                it
                            )
                        }
                    } else {
                        mSelectedIndicatorPaint?.let {
                            canvas?.drawRoundRect(
                                (mIndicatorLeft + mIndicatorPaddingLeft).toFloat(),
                                (height - mSelectedIndicatorHeight).toFloat(),
                                (
                                        mIndicatorRight - mIndicatorPaddingRight).toFloat(),
                                height.toFloat(),
                                mIndicatorRoundRadius.toFloat(),
                                mIndicatorRoundRadius.toFloat(),
                                it
                            )
                        }
                    }
                } else {
                    mSelectedIndicatorPaint?.let {
                        canvas?.drawRoundRect(
                            mIndicatorLeft.toFloat(),
                            (height - mSelectedIndicatorHeight).toFloat(),
                            mIndicatorRight.toFloat(),
                            height.toFloat(),
                            mIndicatorRoundRadius.toFloat(),
                            mIndicatorRoundRadius.toFloat(),
                            it
                        )
                    }
                }
            }
        }

        init {
            setWillNotDraw(false)
            mSelectedIndicatorPaint = Paint()
        }
    }

    private inner class PagerAdapterObserver() : DataSetObserver() {
        override fun onChanged() {
            populateFromPagerAdapter()
        }

        override fun onInvalidated() {
            populateFromPagerAdapter()
        }
    }

    private inner class AdapterChangeListener() : OnAdapterChangeListener {
        private var mAutoRefresh = false
        override fun onAdapterChanged(
            viewPager: ViewPager,
            oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?,
        ) {
            if (mViewPager === viewPager) {
                setPagerAdapter(newAdapter, mAutoRefresh)
            }
        }

        open fun setAutoRefresh(autoRefresh: Boolean) {
            mAutoRefresh = autoRefresh
        }
    }

    companion object {
        /**
         * Scrollable tabs display a subset of tabs at any given moment, and can contain longer tab
         * labels and a larger number of tabs. They are best used for browsing contexts in touch
         * interfaces when users don’t need to directly compare the tab labels.
         *
         * @see .setTabMode
         * @see .getTabMode
         */
        const val MODE_SCROLLABLE = 0

        /**
         * Fixed tabs display all tabs concurrently and are best used with content that benefits from
         * quick pivots between tabs. The maximum number of tabs is limited by the view’s width.
         * Fixed tabs have equal width, based on the widest tab label.
         *
         * @see .setTabMode
         * @see .getTabMode
         */
        const val MODE_FIXED = 1

        /**
         * Gravity used to fill the [MyTabLayout] as much as possible. This option only takes effect
         * when used with [.MODE_FIXED].
         *
         * @see .setTabGravity
         * @see .getTabGravity
         */
        const val GRAVITY_FILL = 0

        /**
         * Gravity used to lay out the tabs in the center of the [MyTabLayout].
         *
         * @see .setTabGravity
         * @see .getTabGravity
         */
        const val GRAVITY_CENTER = 1
        const val DEFAULT_GAP_TEXT_ICON = 8 // dps
        const val FIXED_WRAP_GUTTER_MIN = 16 //dps
        const val MOTION_NON_ADJACENT_OFFSET = 24
        private const val DEFAULT_HEIGHT_WITH_TEXT_ICON = 72 // dps
        private const val INVALID_WIDTH = -1
        private const val DEFAULT_HEIGHT = 48 // dps
        private const val TAB_MIN_WIDTH_MARGIN = 56 //dps
        private const val ANIMATION_DURATION = 300
        private val sTabPool: Pools.Pool<Tab?>? = SynchronizedPool(16)
        private var gapTextIcon: Int? = null
        private fun createColorStateList(defaultColor: Int, selectedColor: Int): ColorStateList? {
            val states = arrayOfNulls<IntArray?>(2)
            val colors = IntArray(2)
            var i = 0
            states[i] = SELECTED_STATE_SET
            colors[i] = selectedColor
            i++

            // Default enabled state
            states[i] = EMPTY_STATE_SET
            colors[i] = defaultColor
            i++
            return ColorStateList(states, colors)
        }
    }

    init {
        ThemeUtils.checkAppCompatTheme(context)

        // Disable the Scroll Bar
        isHorizontalScrollBarEnabled = false

        // Add the TabStrip
        mTabStrip = SlidingTabStrip(context)
        super.addView(
            mTabStrip, 0, LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
            )
        )
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.MyTabLayout,
            defStyleAttr, R.style.MyTabLayout
        )
        mTabStrip.setSelectedIndicatorHeight(
            a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyIndicatorHeight, 0)
        )
        mTabStrip.setSelectedIndicatorPaddingLeft(
            a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyIndicatorPaddingLeft, 0)
        )
        mTabStrip.setSelectedIndicatorPaddingRight(
            a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyIndicatorPaddingRight, 0)
        )
        mTabStrip.setSelectedIndicatorMarginBottom(
            a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyIndicatorMarginBottom, 0)
        )
        mTabStrip.setSelectedIndicatorMarginTop(
            a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyIndicatorMarginTop, 0)
        )
        mTabStrip.setSelectedIndicatorRoundRadius(
            a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyIndicatorRoundRadius, 0)
        )
        mTabStrip.setSelectedIndicatorBottomLayer(
            a.getBoolean(R.styleable.MyTabLayout_tabMyIndicatorBottomLayer, false)
        )
        mTabStrip.setSelectedIndicatorSelfFit(
            a.getBoolean(R.styleable.MyTabLayout_tabMyIndicatorSelfFit, true)
        )
        mTabStrip.setSelectedIndicatorMarginBottomSelfFit(
            a.getBoolean(R.styleable.MyTabLayout_tabMyIndicatorMarginBottomSelfFit, true)
        )
        mTabStrip.setSelectedIndicatorColor(
            a.getColor(
                R.styleable.MyTabLayout_tabMyIndicatorColor,
                0
            )
        )
        mTabPaddingBottom = a
            .getDimensionPixelSize(R.styleable.MyTabLayout_tabMyPadding, 0)
        mTabPaddingEnd = mTabPaddingBottom
        mTabPaddingTop = mTabPaddingEnd
        mTabPaddingStart = mTabPaddingTop
        mTabPaddingStart = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMyPaddingStart,
            mTabPaddingStart
        )
        mTabPaddingTop = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMyPaddingTop,
            mTabPaddingTop
        )
        mTabPaddingEnd = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMyPaddingEnd,
            mTabPaddingEnd
        )
        mTabPaddingBottom = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMyPaddingBottom,
            mTabPaddingBottom
        )
        mTabTextAppearance = a.getResourceId(
            R.styleable.MyTabLayout_tabMyTextAppearance,
            R.style.TextAppearance_Design_Tab
        )
        gapTextIcon = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMyTextIconGap,
            dpToPx(DEFAULT_GAP_TEXT_ICON)
        )

        // Text colors/sizes come from the text appearance first
        val ta = context.obtainStyledAttributes(
            mTabTextAppearance,
            androidx.appcompat.R.styleable.TextAppearance
        )
        try {
            mTabTextSize = ta.getDimensionPixelSize(
                androidx.appcompat.R.styleable.TextAppearance_android_textSize, 0
            ).toFloat()
            mTabTextColors = ta.getColorStateList(
                androidx.appcompat.R.styleable.TextAppearance_android_textColor
            )
        } finally {
            ta.recycle()
        }
        mTabTypeface = a.getInt(R.styleable.MyTabLayout_tabMyTypeface, 1)
        mTabSelectedTypeface = a.getInt(R.styleable.MyTabLayout_tabMySelectedTypeface, mTabTypeface)
        mTabTextSize =
            a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyTextSize, mTabTextSize.toInt())
                .toFloat()
        mTabSelectedTextSize = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMySelectedTextSize,
            mTabTextSize.toInt()
        ).toFloat()
        if (a.hasValue(R.styleable.MyTabLayout_tabMyTextColor)) {
            // If we have an explicit text color set, use it instead
            mTabTextColors = a.getColorStateList(R.styleable.MyTabLayout_tabMyTextColor)
        }
        if (a.hasValue(R.styleable.MyTabLayout_tabMySelectedTextColor)) {
            // We have an explicit selected text color set, so we need to make merge it with the
            // current colors. This is exposed so that developers can use theme attributes to set
            // this (theme attrs in ColorStateLists are Lollipop+)
            val selected = a.getColor(R.styleable.MyTabLayout_tabMySelectedTextColor, 0)
            mTabTextColors =
                mTabTextColors?.defaultColor?.let { createColorStateList(it, selected) }
        }
        mRequestedTabMinWidth = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMyMinWidth,
            INVALID_WIDTH
        )
        mRequestedTabMaxWidth = a.getDimensionPixelSize(
            R.styleable.MyTabLayout_tabMyMaxWidth,
            INVALID_WIDTH
        )
        if (mTabStrip.mTabIndicatorBottomLayer) {
            mTabBackgroundResId = 0
            ViewCompat.setBackground(
                this,
                AppCompatResources.getDrawable(
                    context,
                    a.getResourceId(R.styleable.MyTabLayout_tabMyBackground, 0)
                )
            )
        } else {
            mTabBackgroundResId = a.getResourceId(R.styleable.MyTabLayout_tabMyBackground, 0)
        }
        mContentInsetStart = a.getDimensionPixelSize(R.styleable.MyTabLayout_tabMyContentStart, 0)
        mMode = a.getInt(R.styleable.MyTabLayout_tabMyMode, MODE_FIXED)
        mTabGravity = a.getInt(R.styleable.MyTabLayout_tabMyGravity, GRAVITY_FILL)
        val tabViewNumber = a.getInt(R.styleable.MyTabLayout_tabMyViewNumber, -1)
        if (tabViewNumber != -1) {
            val screenWidth = resources.displayMetrics.widthPixels
            mRequestedTabMinWidth = screenWidth / tabViewNumber
        }
        mTabViewSelf = a.getBoolean(
            R.styleable.MyTabLayout_tabMyTabViewSelf,
            false
        )
        a.recycle()
        val res = resources
        mTabTextMultiLineSize =
            res.getDimensionPixelSize(R.dimen.design_tab_text_size_2line).toFloat()
        mScrollableTabMinWidth = res.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width)

        // Now apply the tab mode and gravity
        applyModeAndGravity()
    }
}