package com.zhangteng.base.widget

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import java.lang.ref.WeakReference
import kotlin.math.min

/**
 * A mediator to link a MyTabLayout with a ViewPager. The mediator will synchronize the ViewPager's
 * position with the selected tab when a tab is selected, and the MyTabLayout's scroll position when
 * the user drags the ViewPager. TabLayoutMediator will listen to ViewPager's OnPageChangeCallback
 * to adjust tab when ViewPager moves. TabLayoutMediator listens to MyTabLayout's
 * OnTabSelectedListener to adjust VP2 when tab moves. TabLayoutMediator listens to RecyclerView's
 * AdapterDataObserver to recreate tab content when dataset changes.
 *
 *
 * Establish the link by creating an instance of this class, make sure the ViewPager has an
 * adapter and then call [.attach] on it. Instantiating a TabLayoutMediator will only create
 * the mediator object, [.attach] will link the MyTabLayout and the ViewPager together. When
 * creating an instance of this class, you must supply an implementation of [ ] in which you set the text of the tab, and/or perform any styling of the
 * tabs that you require. Changing ViewPager's adapter will require a [.detach] followed by
 * [.attach] call. Changing the ViewPager or MyTabLayout will require a new instantiation of
 * TabLayoutMediator.
 */
class MyTabLayoutMediator(
    private val tabLayout: MyTabLayout,
    private val viewPager: ViewPager,
    private val smoothScroll: Boolean,
    private val tabConfigurationStrategy: TabConfigurationStrategy
) {
    private var adapter: PagerAdapter? = null

    /**
     * Returns whether the [MyTabLayout] and the [ViewPager] are linked together.
     */
    var isAttached = false
        private set
    private var onPageChangeCallback: TabLayoutOnPageChangeListener? = null
    private var onTabSelectedListener: MyTabLayout.OnTabSelectedListener? = null

    /**
     * A callback interface that must be implemented to set the text and styling of newly created
     * tabs.
     */
    interface TabConfigurationStrategy {
        /**
         * Called to configure the tab for the page at the specified position. Typically calls [ ][MyTabLayout.Tab.setText], but any form of styling can be applied.
         *
         * @param tab      The Tab which should be configured to represent the title of the item at the given
         * position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        fun onConfigureTab(tab: MyTabLayout.Tab, position: Int)
    }

    constructor(
        tabLayout: MyTabLayout,
        viewPager: ViewPager,
        tabConfigurationStrategy: TabConfigurationStrategy
    ) : this(
        tabLayout,
        viewPager,
        true,
        tabConfigurationStrategy
    )

    /**
     * Link the MyTabLayout and the ViewPager together. Must be called after ViewPager has an adapter
     * set. To be called on a new instance of TabLayoutMediator or if the ViewPager's adapter
     * changes.
     *
     * @throws IllegalStateException If the mediator is already attached, or the ViewPager has no
     * adapter.
     */
    fun attach() {
        check(!isAttached) { "TabLayoutMediator is already attached" }
        adapter = viewPager.adapter
        isAttached = true

        // Add our custom OnPageChangeCallback to the ViewPager
        onPageChangeCallback = TabLayoutOnPageChangeListener(
            tabLayout
        )
        viewPager.addOnPageChangeListener(onPageChangeCallback!!)

        // Now we'll add a tab selected listener to set ViewPager's current item
        onTabSelectedListener = ViewPagerOnTabSelectedListener(
            viewPager, smoothScroll
        )
        tabLayout.addOnTabSelectedListener(onTabSelectedListener!!)

        populateTabsFromPagerAdapter()

        // Now update the scroll position to match the ViewPager's current item
        tabLayout.setScrollPosition(viewPager.currentItem, 0f, true)
    }

    /**
     * Unlink the MyTabLayout and the ViewPager. To be called on a stale TabLayoutMediator if a new one
     * is instantiated, to prevent holding on to a view that should be garbage collected. Also to be
     * called before [.attach] when a ViewPager's adapter is changed.
     */
    fun detach() {
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener!!)
        viewPager.removeOnPageChangeListener(onPageChangeCallback!!)
        onTabSelectedListener = null
        onPageChangeCallback = null
        adapter = null
        isAttached = false
    }

    fun populateTabsFromPagerAdapter() {
        tabLayout.removeAllTabs()
        if (adapter != null) {
            val adapterCount = adapter!!.count
            for (i in 0 until adapterCount) {
                val tab = tabLayout.newTab()
                tabConfigurationStrategy.onConfigureTab(tab, i)
                tabLayout.addTab(tab, false)
            }
            // Make sure we reflect the currently set ViewPager item
            if (adapterCount > 0) {
                val lastItem = tabLayout.getTabCount() - 1
                val currItem = min(viewPager.currentItem, lastItem)
                if (currItem != tabLayout.getSelectedTabPosition()) {
                    tabLayout.selectTab(tabLayout.getTabAt(currItem))
                }
            }
        }
    }

    /**
     * A [ViewPager.OnPageChangeListener] class which contains the necessary calls back to the
     * provided [MyTabLayout] so that the tab position is kept in sync.
     *
     *
     * This class stores the provided MyTabLayout weakly, meaning that you can use [ ][] without removing the
     * callback and not cause a leak.
     */
    private class TabLayoutOnPageChangeListener constructor(tabLayout: MyTabLayout) :
        OnPageChangeListener {
        private val tabLayoutRef: WeakReference<MyTabLayout>
        private var previousScrollState = 0
        private var scrollState = 0

        init {
            tabLayoutRef = WeakReference(tabLayout)
            reset()
        }

        override fun onPageScrollStateChanged(state: Int) {
            previousScrollState = scrollState
            scrollState = state
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                val updateText =
                    scrollState != ViewPager.SCROLL_STATE_SETTLING || previousScrollState == ViewPager.SCROLL_STATE_DRAGGING
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                val updateIndicator =
                    !(scrollState == ViewPager.SCROLL_STATE_SETTLING && previousScrollState == ViewPager.SCROLL_STATE_IDLE)
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator)
            }
        }

        override fun onPageSelected(position: Int) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position && position < tabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                val updateIndicator = (scrollState == ViewPager.SCROLL_STATE_IDLE
                        || (scrollState == ViewPager.SCROLL_STATE_SETTLING
                        && previousScrollState == ViewPager.SCROLL_STATE_IDLE))
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator)
            }
        }

        fun reset() {
            scrollState = ViewPager.SCROLL_STATE_IDLE
            previousScrollState = scrollState
        }
    }

    /**
     * A [MyTabLayout.OnTabSelectedListener] class which contains the necessary calls back to the
     * provided [ViewPager] so that the tab position is kept in sync.
     */
    private class ViewPagerOnTabSelectedListener constructor(
        private val viewPager: ViewPager,
        private val smoothScroll: Boolean
    ) : MyTabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: MyTabLayout.Tab?) {
            tab?.let {
                viewPager.setCurrentItem(tab.getPosition(), smoothScroll)
            }
        }

        override fun onTabUnselected(tab: MyTabLayout.Tab?) {
            // No-op
        }

        override fun onTabReselected(tab: MyTabLayout.Tab?) {
            // No-op
        }
    }
}