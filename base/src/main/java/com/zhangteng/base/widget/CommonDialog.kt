package com.zhangteng.base.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import com.zhangteng.base.R
import com.zhangteng.base.base.BaseDialog
import com.zhangteng.base.utils.AntiShakeUtils

open class CommonDialog : BaseDialog, View.OnClickListener {
    private var contentTxt: TextView? = null
    private var titleTxt: TextView? = null
    private var submitTxt: TextView? = null
    private var cancelTxt: TextView? = null
    private var devider: View? = null
    private var titleDevider: View? = null
    private var mContext: Context?
    private var content: String? = null
    private var positiveName: String? = null
    private var negativeName: String? = null
    private var title: String? = null
    private var listener: OnCloseListener? = null

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, content: String?) : super(context) {
        mContext = context
        this.content = content
        if (!TextUtils.isEmpty(content)) contentTxt?.text = content
    }

    open fun setPositiveButton(name: String?): CommonDialog {
        positiveName = name
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt?.text = positiveName
        }
        return this
    }

    open fun setNegativeButton(name: String?): CommonDialog {
        negativeName = name
        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt?.text = negativeName
            cancelTxt?.visibility = View.VISIBLE
            devider?.visibility = View.VISIBLE
        }
        return this
    }

    open fun setTitle(title: String?): CommonDialog {
        this.title = title
        if (!TextUtils.isEmpty(title)) {
            titleTxt?.text = title
            titleTxt?.visibility = View.VISIBLE
            titleDevider?.visibility = View.VISIBLE
        }
        return this
    }

    open fun setContent(content: String?): CommonDialog {
        this.content = content
        if (!TextUtils.isEmpty(content)) {
            contentTxt?.text = content
        }
        return this
    }

    open fun setContent(content: SpannableString?): CommonDialog {
        this.content = content.toString()
        if (!TextUtils.isEmpty(content)) {
            contentTxt?.highlightColor = Color.TRANSPARENT
            contentTxt?.movementMethod = LinkMovementMethod.getInstance()
            contentTxt?.text = content
        }
        return this
    }

    open fun setDialogCancelable(flag: Boolean): CommonDialog {
        setCancelable(flag)
        return this
    }

    open fun setDialogCanceledOnTouchOutside(cancel: Boolean): CommonDialog {
        setCanceledOnTouchOutside(cancel)
        return this
    }

    override fun getSelfTitleView(): Int {
        return 0
    }

    override fun getSelfContentView(): Int {
        return R.layout.dialog_common
    }

    override fun getSelfButtonView(): Int {
        return R.layout.dialog_common_button
    }

    override fun initView(view: View?) {
        view?.let {
            contentTxt = view.findViewById(R.id.content)
            titleTxt = view.findViewById(R.id.title)
            titleDevider = view.findViewById(R.id.titleDevider)
            devider = view.findViewById(R.id.devider)
            submitTxt = view.findViewById(R.id.submit)
            submitTxt?.setOnClickListener(this)
            cancelTxt = view.findViewById(R.id.cancel)
            cancelTxt?.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        if (AntiShakeUtils.isInvalidClick(v)) return
        if (v.id == R.id.cancel) {
            listener?.onClick(this, false)
            dismiss()
        } else if (v.id == R.id.submit) {
            listener?.onClick(this, true)
            dismiss()
        }
    }

    open fun setListener(listener: OnCloseListener): CommonDialog {
        this.listener = listener
        return this
    }

    interface OnCloseListener {
        open fun onClick(dialog: Dialog?, confirm: Boolean)
    }
}