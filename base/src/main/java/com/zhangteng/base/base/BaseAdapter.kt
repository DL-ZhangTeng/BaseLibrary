package com.zhangteng.base.base

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.util.Linkify
import android.util.SparseArray
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.zhangteng.base.base.BaseAdapter.DefaultViewHolder

/**
 * RecyclerView 自定义的适配器
 * Created by Swing on 2018/5/7.
 */
abstract class BaseAdapter<T, VH : DefaultViewHolder> : RecyclerView.Adapter<VH> {
    constructor()
    constructor(data: MutableList<T?>?) : super() {
        this.data = data
    }

    var data: MutableList<T?>? = null
    var hasHeaderOrFooter = false
    var mOnItemClickListener: OnItemClickListener? = null
    var mOnItemLongClickListener: OnItemLongClickListener? = null
    var mOnItemChildClickListener: OnItemChildClickListener? = null
    var mOnItemChildLongClickListener: OnItemChildLongClickListener? = null

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (position < 0) return
        holder.setAdapter(this)
        holder.itemView.setOnClickListener { v: View? ->
            mOnItemClickListener?.onItemClick(
                v,
                holder.bindingAdapterPosition
            )
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.isLongClickable = true
            holder.itemView.setOnLongClickListener { v: View? ->
                mOnItemLongClickListener?.onItemLongClick(v, holder.bindingAdapterPosition)
                return@setOnLongClickListener true
            }
        }
        if (data == null || position >= data!!.size) {
            onBindViewHolder(holder, null, position)
        } else {
            onBindViewHolder(holder, data!![position], position)
        }
    }

    abstract fun onBindViewHolder(holder: VH, item: T?, position: Int)

    override fun getItemCount(): Int {
        return if (data == null || data!!.isEmpty()) 0 else data!!.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View?, position: Int): Boolean
    }

    interface OnItemChildClickListener {
        fun onItemChildClick(view: View?, position: Int)
    }

    interface OnItemChildLongClickListener {
        fun onItemChildLongClick(view: View?, position: Int): Boolean
    }

    open class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Views indexed with their IDs
         */
        private val views: SparseArray<View?>?
        fun getNestViews(): MutableSet<Int?>? {
            return nestViews
        }

        private val nestViews: HashSet<Int?>?
        private val childClickViewIds: LinkedHashSet<Int?>?
        private val itemChildLongClickViewIds: LinkedHashSet<Int?>?
        private var adapter: BaseAdapter<*, *>? = null

        /**
         * Package private field to retain the associated user object and detect a change
         */
        var associatedObject: Any? = null
        fun getItemChildLongClickViewIds(): HashSet<Int?>? {
            return itemChildLongClickViewIds
        }

        fun getChildClickViewIds(): HashSet<Int?>? {
            return childClickViewIds
        }

        /**
         * Will set the text of a TextView.
         *
         * @param viewId The view id.
         * @param value  The text to put in the text view.
         * @return The DefaultViewHolder for chaining.
         */
        fun setText(@IdRes viewId: Int, value: CharSequence?): DefaultViewHolder? {
            val view = getView<TextView?>(viewId)
            view?.text = value
            return this
        }

        fun setText(@IdRes viewId: Int, @StringRes strId: Int): DefaultViewHolder? {
            val view = getView<TextView?>(viewId)
            view?.setText(strId)
            return this
        }

        /**
         * Will set the image of an ImageView from a resource id.
         *
         * @param viewId     The view id.
         * @param imageResId The image resource id.
         * @return The DefaultViewHolder for chaining.
         */
        fun setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): DefaultViewHolder? {
            val view = getView<ImageView?>(viewId)
            view?.setImageResource(imageResId)
            return this
        }

        /**
         * Will set background color of a view.
         *
         * @param viewId The view id.
         * @param color  A color, not a resource id.
         * @return The DefaultViewHolder for chaining.
         */
        fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): DefaultViewHolder? {
            val view = getView<View?>(viewId)
            view?.setBackgroundColor(color)
            return this
        }

        /**
         * Will set background of a view.
         *
         * @param viewId        The view id.
         * @param backgroundRes A resource to use as a background.
         * @return The DefaultViewHolder for chaining.
         */
        fun setBackgroundRes(
            @IdRes viewId: Int,
            @DrawableRes backgroundRes: Int
        ): DefaultViewHolder? {
            val view = getView<View?>(viewId)
            view?.setBackgroundResource(backgroundRes)
            return this
        }

        /**
         * Will set text color of a TextView.
         *
         * @param viewId    The view id.
         * @param textColor The text color (not a resource id).
         * @return The DefaultViewHolder for chaining.
         */
        fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): DefaultViewHolder? {
            val view = getView<TextView?>(viewId)
            view?.setTextColor(textColor)
            return this
        }

        /**
         * Will set the image of an ImageView from a drawable.
         *
         * @param viewId   The view id.
         * @param drawable The image drawable.
         * @return The DefaultViewHolder for chaining.
         */
        fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): DefaultViewHolder? {
            val view = getView<ImageView?>(viewId)
            view?.setImageDrawable(drawable)
            return this
        }

        /**
         * Add an action to set the image of an image view. Can be called multiple times.
         */
        fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): DefaultViewHolder? {
            val view = getView<ImageView?>(viewId)
            view?.setImageBitmap(bitmap)
            return this
        }

        /**
         * Add an action to set the alpha of a view. Can be called multiple times.
         * Alpha between 0-1.
         */
        fun setAlpha(@IdRes viewId: Int, value: Float): DefaultViewHolder? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getView<View?>(viewId)?.alpha = value
            } else {
                // Pre-honeycomb hack to set Alpha value
                val alpha = AlphaAnimation(value, value)
                alpha.duration = 0
                alpha.fillAfter = true
                getView<View?>(viewId)?.startAnimation(alpha)
            }
            return this
        }

        /**
         * Set a view visibility to VISIBLE (true) or GONE (false).
         *
         * @param viewId  The view id.
         * @param visible True for VISIBLE, false for GONE.
         * @return The DefaultViewHolder for chaining.
         */
        fun setGone(@IdRes viewId: Int, visible: Boolean): DefaultViewHolder? {
            val view = getView<View?>(viewId)
            view?.setVisibility(if (visible) View.VISIBLE else View.GONE)
            return this
        }

        /**
         * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
         *
         * @param viewId  The view id.
         * @param visible True for VISIBLE, false for INVISIBLE.
         * @return The DefaultViewHolder for chaining.
         */
        fun setVisible(@IdRes viewId: Int, visible: Boolean): DefaultViewHolder? {
            val view = getView<View?>(viewId)
            view?.setVisibility(if (visible) View.VISIBLE else View.INVISIBLE)
            return this
        }

        /**
         * Add links into a TextView.
         *
         * @param viewId The id of the TextView to linkify.
         * @return The DefaultViewHolder for chaining.
         */
        fun linkify(@IdRes viewId: Int): DefaultViewHolder? {
            val view = getView<TextView?>(viewId)
            view?.let { Linkify.addLinks(it, Linkify.ALL) }
            return this
        }

        /**
         * Apply the typeface to the given viewId, and enable subpixel rendering.
         */
        fun setTypeface(@IdRes viewId: Int, typeface: Typeface?): DefaultViewHolder? {
            val view = getView<TextView?>(viewId)
            view?.setTypeface(typeface)
            view?.paintFlags = view!!.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
            return this
        }

        /**
         * Apply the typeface to all the given viewIds, and enable subpixel rendering.
         */
        fun setTypeface(typeface: Typeface?, vararg viewIds: Int): DefaultViewHolder? {
            for (viewId in viewIds) {
                val view = getView<TextView?>(viewId)
                view?.setTypeface(typeface)
                view?.paintFlags = view!!.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
            }
            return this
        }

        /**
         * Sets the progress of a ProgressBar.
         *
         * @param viewId   The view id.
         * @param progress The progress.
         * @return The DefaultViewHolder for chaining.
         */
        fun setProgress(@IdRes viewId: Int, progress: Int): DefaultViewHolder? {
            val view = getView<ProgressBar?>(viewId)
            view?.progress = progress
            return this
        }

        /**
         * Sets the progress and max of a ProgressBar.
         *
         * @param viewId   The view id.
         * @param progress The progress.
         * @param max      The max value of a ProgressBar.
         * @return The DefaultViewHolder for chaining.
         */
        fun setProgress(@IdRes viewId: Int, progress: Int, max: Int): DefaultViewHolder? {
            val view = getView<ProgressBar?>(viewId)
            view?.max = max
            view?.progress = progress
            return this
        }

        /**
         * Sets the range of a ProgressBar to 0...max.
         *
         * @param viewId The view id.
         * @param max    The max value of a ProgressBar.
         * @return The DefaultViewHolder for chaining.
         */
        fun setMax(@IdRes viewId: Int, max: Int): DefaultViewHolder? {
            val view = getView<ProgressBar?>(viewId)
            view?.max = max
            return this
        }

        /**
         * Sets the rating (the number of stars filled) of a RatingBar.
         *
         * @param viewId The view id.
         * @param rating The rating.
         * @return The DefaultViewHolder for chaining.
         */
        fun setRating(@IdRes viewId: Int, rating: Float): DefaultViewHolder? {
            val view = getView<RatingBar?>(viewId)
            view?.rating = rating
            return this
        }

        /**
         * Sets the rating (the number of stars filled) and max of a RatingBar.
         *
         * @param viewId The view id.
         * @param rating The rating.
         * @param max    The range of the RatingBar to 0...max.
         * @return The DefaultViewHolder for chaining.
         */
        fun setRating(@IdRes viewId: Int, rating: Float, max: Int): DefaultViewHolder? {
            val view = getView<RatingBar?>(viewId)
            view?.max = max
            view?.rating = rating
            return this
        }

        /**
         * add childView id
         *
         * @param viewId add the child view id   can support childview click
         * @return if you use adapter bind listener
         * @link {(adapter.setOnItemChildClickListener(listener))}
         *
         *
         * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
         */
        fun addOnClickListener(@IdRes viewId: Int): DefaultViewHolder? {
            childClickViewIds?.add(viewId)
            val view = getView<View?>(viewId)
            if (view != null) {
                if (!view.isClickable) {
                    view.isClickable = true
                }
                view.setOnClickListener(View.OnClickListener { v: View? ->
                    if (adapter?.mOnItemChildClickListener != null) {
                        adapter?.mOnItemChildClickListener!!.onItemChildClick(v, layoutPosition)
                    }
                })
            }
            return this
        }

        /**
         * set nestview id
         *
         * @param viewId add the child view id   can support childview click
         * @return
         */
        fun setNestView(@IdRes viewId: Int): DefaultViewHolder? {
            addOnClickListener(viewId)
            addOnLongClickListener(viewId)
            nestViews?.add(viewId)
            return this
        }

        /**
         * add long click view id
         *
         * @param viewId
         * @return if you use adapter bind listener
         * @link {(adapter.setOnItemChildLongClickListener(listener))}
         *
         *
         * or if you can use  recyclerView.addOnItemTouch(listerer)  wo also support this menthod
         */
        fun addOnLongClickListener(@IdRes viewId: Int): DefaultViewHolder? {
            itemChildLongClickViewIds?.add(viewId)
            val view = getView<View?>(viewId)
            if (view != null) {
                if (!view.isLongClickable) {
                    view.isLongClickable = true
                }
                view.setOnLongClickListener(OnLongClickListener { v ->
                    adapter?.mOnItemChildLongClickListener != null &&
                            adapter?.mOnItemChildLongClickListener!!.onItemChildLongClick(
                                v,
                                layoutPosition
                            )
                })
            }
            return this
        }

        /**
         * Sets the listview or gridview's item long click listener of the view
         *
         * @param viewId   The view id.
         * @param listener The item long click listener;
         * @return The DefaultViewHolder for chaining.
         */
        fun setOnItemLongClickListener(
            @IdRes viewId: Int,
            listener: AdapterView.OnItemLongClickListener?
        ): DefaultViewHolder? {
            val view = getView<AdapterView<*>?>(viewId)
            view?.onItemLongClickListener = listener
            return this
        }

        /**
         * Sets the listview or gridview's item selected click listener of the view
         *
         * @param viewId   The view id.
         * @param listener The item selected click listener;
         * @return The DefaultViewHolder for chaining.
         */
        fun setOnItemSelectedClickListener(
            @IdRes viewId: Int,
            listener: OnItemSelectedListener?
        ): DefaultViewHolder? {
            val view = getView<AdapterView<*>?>(viewId)
            view?.onItemSelectedListener = listener
            return this
        }

        /**
         * Sets the on checked change listener of the view.
         *
         * @param viewId   The view id.
         * @param listener The checked change listener of compound button.
         * @return The DefaultViewHolder for chaining.
         */
        fun setOnCheckedChangeListener(
            @IdRes viewId: Int,
            listener: CompoundButton.OnCheckedChangeListener?
        ): DefaultViewHolder? {
            val view = getView<CompoundButton?>(viewId)
            view?.setOnCheckedChangeListener(listener)
            return this
        }

        /**
         * Sets the tag of the view.
         *
         * @param viewId The view id.
         * @param tag    The tag;
         * @return The DefaultViewHolder for chaining.
         */
        fun setTag(@IdRes viewId: Int, tag: Any?): DefaultViewHolder? {
            val view = getView<View?>(viewId)
            view?.tag = tag
            return this
        }

        /**
         * Sets the tag of the view.
         *
         * @param viewId The view id.
         * @param key    The key of tag;
         * @param tag    The tag;
         * @return The DefaultViewHolder for chaining.
         */
        fun setTag(@IdRes viewId: Int, key: Int, tag: Any?): DefaultViewHolder? {
            val view = getView<View?>(viewId)
            view?.setTag(key, tag)
            return this
        }

        /**
         * Sets the checked status of a checkable.
         *
         * @param viewId  The view id.
         * @param checked The checked status;
         * @return The DefaultViewHolder for chaining.
         */
        fun setChecked(@IdRes viewId: Int, checked: Boolean): DefaultViewHolder? {
            val view = getView<View?>(viewId)
            // View unable cast to Checkable
            if (view is Checkable) {
                (view as Checkable?)?.isChecked = checked
            }
            return this
        }

        /**
         * Sets the adapter of a adapter view.
         *
         * @param viewId  The view id.
         * @param adapter The adapter;
         * @return The DefaultViewHolder for chaining.
         */
        fun setAdapter(@IdRes viewId: Int, adapter: Adapter?): DefaultViewHolder? {
            val view = getView<AdapterView<*>?>(viewId)
            adapter?.let {
                view?.setAdapter(adapter as Nothing?)
            }
            return this
        }

        /**
         * Sets the adapter of a adapter view.
         *
         * @param adapter The adapter;
         * @return The DefaultViewHolder for chaining.
         */
        fun setAdapter(adapter: BaseAdapter<*, *>?): DefaultViewHolder? {
            this.adapter = adapter
            return this
        }

        fun <T : View?> getView(@IdRes viewId: Int): T? {
            var view = views?.get(viewId)
            if (view == null) {
                view = itemView.findViewById(viewId)
                views?.put(viewId, view)
            }
            return view as T?
        }

        init {
            views = SparseArray()
            childClickViewIds = LinkedHashSet()
            itemChildLongClickViewIds = LinkedHashSet()
            nestViews = HashSet()
        }
    }

    companion object {
        protected const val DEFAULT_VIEW = 10000
    }
}