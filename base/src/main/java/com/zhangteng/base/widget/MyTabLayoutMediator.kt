package com.zhangteng.base.widget

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.min

/**
 * A mediator to link a MyTabLayout with a ViewPager. The mediator will synchronize the ViewPager's
 * position with the selected tab when a tab is selected, and the MyTabLayout's scroll position when
 * the user drags the ViewPager. MyTabLayoutMediator will listen to ViewPager's OnPageChangeCallback
 * to adjust tab when ViewPager moves. MyTabLayoutMediator listens to MyTabLayout's
 * OnTabSelectedListener to adjust VP2 when tab moves. MyTabLayoutMediator listens to RecyclerView's
 * AdapterDataObserver to recreate tab content when dataset changes.
 *
 *
 * Establish the link by creating an instance of this class, make sure the ViewPager has an
 * adapter and then call [.attach] on it. Instantiating a MyTabLayoutMediator will only create
 * the mediator object, [.attach] will link the MyTabLayout and the ViewPager together. When
 * creating an instance of this class, you must supply an implementation of [ ] in which you set the text of the tab, and/or perform any styling of the
 * tabs that you require. Changing ViewPager's adapter will require a [.detach] followed by
 * [.attach] call. Changing the ViewPager or MyTabLayout will require a new instantiation of
 * MyTabLayoutMediator.
 */
class MyTabLayoutMediator {
    private val tabLayout: MyTabLayout
    private val smoothScroll: Boolean
    private val tabConfigurationStrategy: TabConfigurationStrategy

    /**
     * Returns whether the [MyTabLayout] and the ViewPager are linked together.
     */
    private var isAttached = false

    private var viewPager: ViewPager? = null
    private var adapter: PagerAdapter? = null
    private var onPageChangeCallback: MyTabLayout.TabLayoutOnPageChangeListener? = null
    private var onTabSelectedListener: MyTabLayout.OnTabSelectedListener? = null

    private var viewPager2: ViewPager2? = null
    private var adapter2: RecyclerView.Adapter<*>? = null
    private var onPageChangeCallback2: MyTabLayout.TabLayoutOnPageChangeCallback? = null
    private var onTabSelectedListener2: MyTabLayout.ViewPager2OnTabSelectedListener? = null
    private var autoRefresh: Boolean? = null
    private var pagerAdapterObserver: RecyclerView.AdapterDataObserver? = null

    constructor(
        tabLayout: MyTabLayout,
        viewPager: ViewPager,
        smoothScroll: Boolean,
        tabConfigurationStrategy: TabConfigurationStrategy
    ) {
        this.tabLayout = tabLayout
        this.viewPager = viewPager
        this.smoothScroll = smoothScroll
        this.tabConfigurationStrategy = tabConfigurationStrategy
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

    constructor(
        tabLayout: MyTabLayout,
        viewPager: ViewPager2,
        autoRefresh: Boolean,
        smoothScroll: Boolean,
        tabConfigurationStrategy: TabConfigurationStrategy
    ) {
        this.tabLayout = tabLayout
        this.viewPager2 = viewPager
        this.autoRefresh = autoRefresh
        this.smoothScroll = smoothScroll
        this.tabConfigurationStrategy = tabConfigurationStrategy
    }

    constructor(
        tabLayout: MyTabLayout,
        viewPager: ViewPager2,
        tabConfigurationStrategy: TabConfigurationStrategy
    ) : this(tabLayout, viewPager,  /* autoRefresh= */true, tabConfigurationStrategy)

    constructor(
        tabLayout: MyTabLayout,
        viewPager: ViewPager2,
        autoRefresh: Boolean,
        tabConfigurationStrategy: TabConfigurationStrategy
    ) : this(
        tabLayout,
        viewPager,
        autoRefresh,  /* smoothScroll= */
        true,
        tabConfigurationStrategy
    )

    /**
     * Returns whether the [MyTabLayout] and the ViewPager are linked together.
     */
    fun isAttached(): Boolean {
        return isAttached
    }

    /**
     * Link the MyTabLayout and the ViewPager together. Must be called after ViewPager has an adapter
     * set. To be called on a new instance of MyTabLayoutMediator or if the ViewPager's adapter
     * changes.
     *
     * @throws IllegalStateException If the mediator is already attached, or the ViewPager has no
     * adapter.
     */
    fun attach() {
        check(!isAttached) { "MyTabLayoutMediator is already attached" }
        adapter = viewPager?.adapter
        isAttached = true

        // Add our custom OnPageChangeCallback to the ViewPager
        onPageChangeCallback = MyTabLayout.TabLayoutOnPageChangeListener(
            tabLayout
        )
        viewPager?.addOnPageChangeListener(onPageChangeCallback!!)

        // Now we'll add a tab selected listener to set ViewPager's current item
        onTabSelectedListener = MyTabLayout.ViewPagerOnTabSelectedListener(viewPager, smoothScroll)
        tabLayout.addOnTabSelectedListener(onTabSelectedListener!!)

        populateTabsFromPagerAdapter()

        // Now update the scroll position to match the ViewPager's current item
        tabLayout.setScrollPosition(viewPager?.currentItem ?: 0, 0f, true)
    }

    /**
     * Unlink the MyTabLayout and the ViewPager. To be called on a stale MyTabLayoutMediator if a new one
     * is instantiated, to prevent holding on to a view that should be garbage collected. Also to be
     * called before [.attach] when a ViewPager's adapter is changed.
     */
    fun detach() {
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener!!)
        viewPager?.removeOnPageChangeListener(onPageChangeCallback!!)
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
                val currItem = min(viewPager?.currentItem ?: 0, lastItem)
                if (currItem != tabLayout.getSelectedTabPosition()) {
                    tabLayout.selectTab(tabLayout.getTabAt(currItem))
                }
            }
        }
    }

    /**
     * Link the MyTabLayout and the ViewPager2 together. Must be called after ViewPager2 has an adapter
     * set. To be called on a new instance of MyTabLayoutMediator or if the ViewPager2's adapter
     * changes.
     *
     * @throws IllegalStateException If the mediator is already attached, or the ViewPager2 has no
     * adapter.
     */
    fun attach2() {
        check(!isAttached) { "MyTabLayoutMediator is already attached" }
        adapter2 = viewPager2?.adapter
        checkNotNull(adapter2) { "MyTabLayoutMediator attached before ViewPager2 has an " + "adapter" }
        isAttached = true

        // Add our custom OnPageChangeCallback to the ViewPager
        onPageChangeCallback2 = MyTabLayout.TabLayoutOnPageChangeCallback(tabLayout)
        viewPager2?.registerOnPageChangeCallback(onPageChangeCallback2!!)

        // Now we'll add a tab selected listener to set ViewPager's current item
        onTabSelectedListener2 = MyTabLayout.ViewPager2OnTabSelectedListener(
            viewPager2, smoothScroll
        )
        tabLayout.addOnTabSelectedListener(onTabSelectedListener2!!)

        // Now we'll populate ourselves from the pager adapter, adding an observer if
        // autoRefresh is enabled
        if (autoRefresh == true) {
            // Register our observer on the new adapter
            pagerAdapterObserver = PagerAdapterObserver()
            adapter2?.registerAdapterDataObserver(pagerAdapterObserver!!)
        }
        populateTabsFromPagerAdapter2()

        // Now update the scroll position to match the ViewPager's current item
        tabLayout.setScrollPosition(viewPager2?.currentItem ?: 0, 0f, true)
    }

    /**
     * Unlink the MyTabLayout and the ViewPager. To be called on a stale MyTabLayoutMediator if a new one
     * is instantiated, to prevent holding on to a view that should be garbage collected. Also to be
     * called before [.attach] when a ViewPager2's adapter is changed.
     */
    fun detach2() {
        if (autoRefresh == true && adapter2 != null) {
            adapter2!!.unregisterAdapterDataObserver(pagerAdapterObserver!!)
            pagerAdapterObserver = null
        }
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener2!!)
        viewPager2?.unregisterOnPageChangeCallback(onPageChangeCallback2!!)
        onTabSelectedListener2 = null
        onPageChangeCallback2 = null
        adapter2 = null
        isAttached = false
    }

    fun populateTabsFromPagerAdapter2() {
        tabLayout.removeAllTabs()
        if (adapter2 != null) {
            val adapterCount = adapter2!!.itemCount
            for (i in 0 until adapterCount) {
                val tab = tabLayout.newTab()
                tabConfigurationStrategy.onConfigureTab(tab, i)
                tabLayout.addTab(tab, false)
            }
            // Make sure we reflect the currently set ViewPager item
            if (adapterCount > 0) {
                val lastItem = tabLayout.getTabCount() - 1
                val currItem = Math.min(viewPager2?.currentItem ?: 0, lastItem)
                if (currItem != tabLayout.getSelectedTabPosition()) {
                    tabLayout.selectTab(tabLayout.getTabAt(currItem))
                }
            }
        }
    }

    inner class PagerAdapterObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            populateTabsFromPagerAdapter2()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter2()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            populateTabsFromPagerAdapter2()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter2()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter2()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            populateTabsFromPagerAdapter2()
        }
    }

    /**
     * A callback interface that must be implemented to set the text and styling of newly created
     * tabs.
     */
    interface TabConfigurationStrategy {
        /**
         * Called to configure the tab for the page at the specified position. Typically calls [MyTabLayout.Tab.setText], but any form of styling can be applied.
         *
         * @param tab      The Tab which should be configured to represent the title of the item at the given
         * position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        fun onConfigureTab(tab: MyTabLayout.Tab, position: Int)
    }
}