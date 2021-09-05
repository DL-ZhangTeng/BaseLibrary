package com.zhangteng.base.adapter

import android.app.Service
import android.os.Vibrator
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.zhangteng.base.R
import com.zhangteng.base.base.BaseAdapter
import com.zhangteng.base.base.BaseAdapter.DefaultViewHolder
import com.zhangteng.base.utils.AntiShakeUtils.isInvalidClick
import com.zhangteng.base.utils.FileUtils.isVideoFile
import com.zhangteng.imagepicker.bean.ImageInfo
import com.zhangteng.imagepicker.callback.HandlerCallBack
import com.zhangteng.imagepicker.callback.IHandlerCallBack
import com.zhangteng.imagepicker.config.ImagePickerConfig
import com.zhangteng.imagepicker.config.ImagePickerEnum
import com.zhangteng.imagepicker.config.ImagePickerOpen
import com.zhangteng.imagepicker.imageloader.GlideImageLoader
import com.zhangteng.imagepicker.utils.NullUtill
import java.util.*

/**
 * 发布九宫格选择图片视频文件；已实现拖曳；已实现的默认图片视频逻辑，需要配合ImagePicker使用
 * 请配合 [com.zhangteng.base.widget.GridSpacingItemDecoration][com.zhangteng.base.widget.LinearSpacingItemDecoration]调整间距
 * Created by swing on 2018/5/7.
 */
class PublishAdapter : BaseAdapter<ImageInfo?, DefaultViewHolder> {
    private var onAddItemClickListener: OnAddItemClickListener? = null
    private var onAddVideoItemClickListener: OnAddVideoItemClickListener? = null
    private var onAddFileItemClickListener: OnAddFileItemClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onImageItemClickListener: OnImageItemClickListener? = null
    private var onVideoItemClickListener: OnVideoItemClickListener? = null
    private var activity: FragmentActivity
    private var recyclerView: RecyclerView? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    /**
     * 剩余可选图片数
     */
    var maxSelectable = 9

    /**
     * 是否只选择图片
     */
    private var onlyImage: Boolean

    /**
     * 添加按钮个数1~3(添加图片，添加视频，添加文件；添加文件无默认实现)
     */
    private var addButtonNum = 1

    /**
     * 是否有删除按钮
     */
    var isHaveDeleteBtn = true

    //<editor-fold desc="不可拖曳九宫格适配器初始化">
    @JvmOverloads
    constructor(
        activity: FragmentActivity,
        data: MutableList<ImageInfo?>?,
        @IntRange(from = 1, to = 3) addButtonNum: Int = 1,
        onlyImage: Boolean = false
    ) : super(data) {
        this.activity = activity
        this.addButtonNum = addButtonNum
        this.onlyImage = onlyImage
    }
    //</editor-fold>

    //<editor-fold desc="可拖曳九宫格适配器初始化">
    @JvmOverloads
    constructor(
        activity: FragmentActivity,
        recyclerView: RecyclerView,
        data: MutableList<ImageInfo?>?,
        @IntRange(from = 1, to = 3) addButtonNum: Int = 1,
        onlyImage: Boolean = false
    ) : super(data) {
        this.activity = activity
        this.recyclerView = recyclerView
        itemTouchHelper = ItemTouchHelper(PublishItemTouchHelperCallback())
        itemTouchHelper!!.attachToRecyclerView(recyclerView)
        this.recyclerView!!.addOnItemTouchListener(PublishItemClickListener())
        this.addButtonNum = addButtonNum
        this.onlyImage = onlyImage
        val itemAnimator = recyclerView.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }
    //</editor-fold>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        when (viewType) {
            IMAGE -> {
                // 实例化展示的view
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.publish_item_img, parent, false)
                // 实例化viewholder
                return PublishViewHolder(v)
            }
            ADD_IMAGE -> {
                // 实例化展示的view
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.publish_item_img_add_, parent, false)
                // 实例化viewholder
                return PublishAddViewHolder(v)
            }
            ADD_VIDEO -> {
                // 实例化展示的view
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.publish_item_img_addvideo_, parent, false)
                // 实例化viewholder
                return PublishAddVideoViewHolder(v)
            }
            ADD_FILE -> {
                // 实例化展示的view
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.publish_item_img_addfile_, parent, false)
                // 实例化viewholder
                return PublishAddFileViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_default, parent, false)
                return DefaultViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        if (holder is PublishViewHolder) {
            GlideImageLoader().loadImage(
                holder.imageView.context, holder.imageView, data!![position]!!.path
            )
        }
        setHolderViewClickListener(holder)
    }

    override fun getItemCount(): Int {
        return if (addButtonNum == 3) {
            getAddButtonNumLesserItemCount(addButtonNum - 1) + 1
        } else {
            getAddButtonNumLesserItemCount(addButtonNum)
        }
    }

    /**
     * 添加按钮小于3时的条目数
     *
     * @param addButtonNum 不包含上传文件按钮之外的按钮数量
     */
    protected fun getAddButtonNumLesserItemCount(addButtonNum: Int): Int {
        return if (super.getItemCount() >= maxSelectable) {
            maxSelectable
        } else if (super.getItemCount() + addButtonNum > maxSelectable) {
            if (NullUtill.getNotNull(data).size > 0 && isVideoFile(
                    data!![0]!!.path
                )
            ) {
                maxSelectable
            } else {
                maxSelectable + addButtonNum - 1
            }
        } else {
            if (NullUtill.getNotNull(data).size > 0 && isVideoFile(
                    data!![0]!!.path
                )
            ) {
                super.getItemCount() + addButtonNum - 1
            } else {
                super.getItemCount() + addButtonNum
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (addButtonNum == 3) {
            if (position == itemCount - 1) {
                ADD_FILE
            } else {
                getAddButtonNumLesserItemViewType(position, addButtonNum - 1)
            }
        } else {
            getAddButtonNumLesserItemViewType(position, addButtonNum)
        }
    }

    /**
     * 添加按钮小于3时的条目类型
     *
     * @param position     item 标识
     * @param addButtonNum 不包含上传文件按钮之外的按钮数量
     */
    protected fun getAddButtonNumLesserItemViewType(position: Int, addButtonNum: Int): Int {
        return if (position == getAddButtonNumLesserItemCount(addButtonNum) - 1 && NullUtill.getNotNull(
                data
            ).size < maxSelectable
        ) {
            ADD_IMAGE
        } else if (addButtonNum == 2 && position == getAddButtonNumLesserItemCount(addButtonNum) - 2 && NullUtill.getNotNull(
                data
            ).size < maxSelectable
        ) {
            if (NullUtill.getNotNull(data).size > 0 && isVideoFile(
                    data!![0]!!.path
                )
            ) {
                IMAGE
            } else ADD_VIDEO
        } else {
            IMAGE
        }
    }

    class PublishViewHolder(itemView: View) : DefaultViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.discover_publish_iv)
        val delete: ImageView = itemView.findViewById(R.id.discover_pulish_delete)
    }

    class PublishAddViewHolder(itemView: View) : DefaultViewHolder(itemView) {
        val add: ImageView = itemView.findViewById(R.id.discover_publish_add_)
    }

    class PublishAddVideoViewHolder(itemView: View) : DefaultViewHolder(itemView) {
        val add: ImageView = itemView.findViewById(R.id.discover_publish_add_)
    }

    class PublishAddFileViewHolder(itemView: View) : DefaultViewHolder(itemView) {
        val add: ImageView = itemView.findViewById(R.id.discover_publish_add_)
    }

    //<editor-fold desc="设置九宫格点击事件">
    fun setOnAddItemClickListener(onAddItemClickListener: OnAddItemClickListener?) {
        this.onAddItemClickListener = onAddItemClickListener
    }

    fun setOnAddVideoItemClickListener(onAddVideoItemClickListener: OnAddVideoItemClickListener?) {
        this.onAddVideoItemClickListener = onAddVideoItemClickListener
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener?) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun setOnImageItemClickListener(onImageItemClickListener: OnImageItemClickListener?) {
        this.onImageItemClickListener = onImageItemClickListener
    }

    fun setOnVideoItemClickListener(onVideoItemClickListener: OnVideoItemClickListener?) {
        this.onVideoItemClickListener = onVideoItemClickListener
    }

    fun setOnAddFileItemClickListener(onAddFileItemClickListener: OnAddFileItemClickListener?) {
        this.onAddFileItemClickListener = onAddFileItemClickListener
    }

    protected fun setHolderViewClickListener(holder: RecyclerView.ViewHolder) {
        if (holder is PublishViewHolder) {
            holder.imageView.setOnClickListener { v: View? ->
                if (isInvalidClick(v!!)) return@setOnClickListener
                val p = holder.getAdapterPosition()
                if (isVideoFile(data!![p]!!.path)) {
                    if (onVideoItemClickListener != null) {
                        onVideoItemClickListener!!.onVideoItemClick(v)
                    } else {
                        previewVideo(holder.imageView, p)
                    }
                } else {
                    if (onImageItemClickListener != null) {
                        onImageItemClickListener!!.onImageItemClick(v)
                    } else {
                        previewImage(holder.imageView, p)
                    }
                }
            }
            if (isHaveDeleteBtn) {
                holder.delete.visibility = View.VISIBLE
                holder.delete.setOnClickListener { v: View? ->
                    if (isInvalidClick(v!!)) return@setOnClickListener
                    val p = holder.getAdapterPosition()
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener!!.onDeleteClick(v, p, data!![p]!!.path)
                    } else {
                        if (isVideoFile(
                                data!![p]!!.path
                            ) && data!!.size == 1
                        ) {
                            maxSelectable = 9
                        }
                        data!!.removeAt(p)
                        notifyItemRemoved(p)
                        notifyItemRangeChanged(p, itemCount)
                    }
                }
            } else {
                holder.delete.visibility = View.GONE
            }
        } else if (holder is PublishAddViewHolder) {
            holder.add.setOnClickListener { v: View? ->
                if (isInvalidClick(v!!)) return@setOnClickListener
                if (onAddItemClickListener != null) {
                    onAddItemClickListener!!.onAddItemClick(v)
                } else {
                    val iHandlerCallBack: IHandlerCallBack = object : HandlerCallBack() {

                        override fun onSuccess(photoList: List<ImageInfo>) {
                            super.onSuccess(photoList)
                        }

                        override fun onFinish(photoList: List<ImageInfo>) {
                            super.onFinish(photoList)
                            if (addButtonNum == 1 && photoList.size > 0) {
                                for (path in photoList) {
                                    if (isVideoFile(path.path)) {
                                        maxSelectable = 1
                                    }
                                }
                            }
                            data!!.addAll(photoList)
                            notifyDataSetChanged()
                        }

                    }
                    initPicker(
                        activity,
                        iHandlerCallBack,
                        maxSelectable - data!!.size,
                        !onlyImage && addButtonNum == 1,
                        true
                    )
                }
            }
        } else if (holder is PublishAddVideoViewHolder) {
            holder.add.setOnClickListener { v: View? ->
                if (isInvalidClick(v!!)) return@setOnClickListener
                if (onAddVideoItemClickListener != null) {
                    onAddVideoItemClickListener!!.onAddVideoItemClick(v)
                } else {
                    val iHandlerCallBack: IHandlerCallBack = object : HandlerCallBack() {

                        override fun onSuccess(photoList: List<ImageInfo>) {
                            super.onSuccess(photoList)
                        }

                        override fun onFinish(photoList: List<ImageInfo>) {
                            super.onFinish(photoList)
                            if (!NullUtill.isEmpty(photoList)) {
                                data!!.add(0, photoList[0])
                                notifyDataSetChanged()
                            }
                        }

                    }
                    initPicker(
                        activity,
                        iHandlerCallBack,
                        maxSelectable - data!!.size - 1,
                        true,
                        false
                    )
                }
            }
        } else if (holder is PublishAddFileViewHolder) {
            holder.add.setOnClickListener { v: View? ->
                if (isInvalidClick(v!!)) return@setOnClickListener
                if (onAddFileItemClickListener != null) {
                    onAddFileItemClickListener!!.onAddFileItemClick(v)
                }
            }
        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(view: View?, position: Int, path: String?)
    }

    interface OnVideoItemClickListener {
        fun onVideoItemClick(view: View?)
    }

    interface OnImageItemClickListener {
        fun onImageItemClick(view: View?)
    }

    interface OnAddItemClickListener {
        fun onAddItemClick(view: View?)
    }

    interface OnAddVideoItemClickListener {
        fun onAddVideoItemClick(view: View?)
    }

    interface OnAddFileItemClickListener {
        fun onAddFileItemClick(view: View?)
    }

    /**
     * description 预览图片
     */
    protected fun previewImage(view: ImageView?, position: Int) {}

    /**
     * description 预览视频
     */
    protected fun previewVideo(view: ImageView?, position: Int) {}
    //</editor-fold>

    //<editor-fold desc="设置九宫格拖曳事件">
    /**
     * recyclerview触摸监听器
     */
    inner class PublishItemClickListener : OnItemTouchListener {
        private val mGestureDetector: GestureDetectorCompat
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            mGestureDetector.onTouchEvent(e)
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            mGestureDetector.onTouchEvent(e)
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        init {
            mGestureDetector =
                GestureDetectorCompat(recyclerView!!.context, ItemTouchHelperGestureListener())
        }
    }

    /**
     * 拖曳手势监听器
     */
    inner class ItemTouchHelperGestureListener : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val child = recyclerView!!.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val vh = recyclerView!!.getChildViewHolder(child)
                if (NullUtill.isEmpty(data)) {
                    return
                } else {
                    if (vh.layoutPosition == 0 && isVideoFile(
                            data!![0]!!.path
                        )
                    ) {
                        return
                    }
                    val buttonNum = itemCount - data!!.size
                    if (buttonNum == 1 && vh.layoutPosition == itemCount - 1) {
                        return
                    } else if (buttonNum >= 2) {
                        if (vh.layoutPosition == itemCount - 1 || vh.layoutPosition == itemCount - 2) {
                            return
                        }
                    }
                }
                itemTouchHelper!!.startDrag(vh)
                //获取系统震动服务
                val vib = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator //震动70毫秒
                vib.vibrate(70)
            }
        }
    }

    /**
     * recyclerview触摸回调
     */
    inner class PublishItemTouchHelperCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return if (recyclerView.layoutManager is GridLayoutManager) {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags = 0
                makeMovementFlags(
                    dragFlags,
                    swipeFlags
                )
            } else if (recyclerView.layoutManager is LinearLayoutManager) {
                val dragFlags: Int
                dragFlags =
                    if ((recyclerView.layoutManager as LinearLayoutManager?)!!.orientation == LinearLayoutManager.HORIZONTAL) {
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    } else {
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    }
                val swipeFlags = 0
                makeMovementFlags(
                    dragFlags,
                    swipeFlags
                )
            } else {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags = 0
                makeMovementFlags(
                    dragFlags,
                    swipeFlags
                )
            }
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            //得到当拖拽的viewHolder的Position
            val fromPosition = viewHolder.adapterPosition
            //拿到当前拖拽到的item的viewHolder
            val toPosition = target.adapterPosition
            if (toPosition >= data!!.size) return false
            if (toPosition == 0 && isVideoFile(data!![0]!!.path)) return false
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(data, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(data, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }
    }
    //</editor-fold>

    companion object {
        private const val ADD_FILE = 3
        private const val ADD_VIDEO = 2
        private const val ADD_IMAGE = 1
        private const val IMAGE = 0

        //<editor-fold desc="初始化图片选择器">
        @JvmOverloads
        fun initPicker(
            activity: FragmentActivity?,
            iHandlerCallBack: IHandlerCallBack?,
            maxImage: Int,
            isSelectVideo: Boolean = true,
            isSelectImage: Boolean = true,
            isCrop: Boolean = false,
            cropAspectRatio: Float = 0f
        ) {
            val imagePickerConfig = ImagePickerConfig.Builder()
                .pickerThemeColorRes(R.color.titlebar_bg)
                .pickerTitleColorRes(R.color.titlebar_text_color)
                .pickerBackRes(R.mipmap.image_picker_back_black)
                .pickerFolderRes(R.mipmap.image_picker_folder_black)
                .cropThemeColorRes(R.color.base_theme_color)
                .cropTitleColorRes(R.color.titlebar_text_light_color)
                .imageLoader(GlideImageLoader()) //图片加载器
                .iHandlerCallBack(iHandlerCallBack) //图片选择器生命周期监听（直接打开摄像头时无效）
                .multiSelect(true) //是否开启多选
                .isVideoPicker(isSelectVideo) //是否选择视频 默认false
                .isShowCamera(!isSelectVideo)
                .isImagePicker(isSelectImage)
                .imagePickerType(ImagePickerEnum.PHOTO_PICKER) //选择器打开类型
                .isMirror(false) //是否旋转镜头
                .maxImageSelectable(maxImage) //图片可选择数
                .maxVideoSelectable(1)
                .maxHeight(1920) //图片最大高度
                .maxWidth(1080) //图片最大宽度
                .maxImageSize(15) //图片最大大小Mb
                .maxVideoLength(10 * 60 * 1000)
                .maxVideoSize(50)
                .isCrop(isCrop, cropAspectRatio)
                .build()
            ImagePickerOpen.getInstance().setImagePickerConfig(imagePickerConfig).open(activity)
        }
        //</editor-fold>
    }
}