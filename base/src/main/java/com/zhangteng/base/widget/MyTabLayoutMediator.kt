package com.zhangteng.base.widget

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
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
    private var onPageChangeCallback: MyTabLayout.TabLayoutOnPageChangeListener? = null
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
        onPageChangeCallback = MyTabLayout.TabLayoutOnPageChangeListener(
            tabLayout
        )
        viewPager.addOnPageChangeListener(onPageChangeCallback!!)

        // Now we'll add a tab selected listener to set ViewPager's current item
        onTabSelectedListener = MyTabLayout.ViewPagerOnTabSelectedListener(viewPager, smoothScroll)
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

    private fun populateTabsFromPagerAdapter() {
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
}