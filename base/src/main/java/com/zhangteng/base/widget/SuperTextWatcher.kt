package com.zhangteng.base.widget

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
import com.zhangteng.base.R
import com.zhangteng.base.widget.SuperTextWatcher.AtUser
import com.zhangteng.utils.e
import com.zhangteng.utils.isInvalidClick
import java.lang.ref.WeakReference
import java.util.regex.Pattern

/**
 * EditText的@处理 未处理剪裁复制时的逻辑
 * 1、需要实现toAtActivity（输入@后跳转目标）、createBrandNewAUser（创建一个全新艾特用户对象用于输入框显示）
 * 2、需要跳转个人主页使用回调AClickableSpan.onAtClickListener
 * 3、使用时必须在当前Activity调用onActivityResult，如下
 * override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
 * super.onActivityResult(requestCode, resultCode, data)
 * if (resultCode == RESULT_OK) {
 * superTextWatcher.onActivityResult(requestCode, data)
 * }
 * }
 * 4、可使用SuperTextWatcher.getAtSpan*等方法为字符串添加特效（艾特变色、点击事件）
 * 5、为了避免用户名重复，SuperTextWatcher支持@Id的字符串添加特效，可使用getAtIdString将@Name的字符串替换为@Id
 * 6、修改艾特特效颜色可添加资源<color name="aite_name_color">#1289F3</color>
 * 7、使用完成后建议清理EditText文本时调用atUsers.clear
 * Created by swing on 2019/6/26 0026.
 */
abstract class SuperTextWatcher<T : AtUser?>(
    activity: Activity,
    editText: EditText,
    atUsers: MutableList<T>
) : TextWatcher {
    /**
     * description: 被艾特的人员列表
     */
    var atUsers: MutableList<T>
    private val activity: WeakReference<Activity>
    private val editText: EditText

    /**
     * @param start: 字符串中即将发生修改的位置。
     * @param count: 字符串中即将被修改的文字的长度。如果是新增的话则为0。
     * @param after: 被修改的文字修改之后的长度。如果是删除的话则为0。
     */
    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        //将用户名中的结束符替换为*
        var afterBackup = charSequence.toString().replace(superAt, at)
        if (!isEmpty(atUsers)) {
            for (aUser in atUsers) {
                val name = aUser!!.atUserName
                afterBackup = afterBackup.replace(name!!, symbolReplace(name))
            }
            afterBackup = afterBackup.replace(superAt, at)
        }
        val previousString = afterBackup.substring(0, start)
        val nextString = afterBackup.substring(start)
        //如果光标在用户名中间输入时将光标移动至结尾
        val previousStartIndex = previousString.lastIndexOf(startDivider)
        val nextEndIndex = nextString.indexOf(endDivider)
        if (previousStartIndex != -1 && nextEndIndex != -1) {
            val futureStr =
                previousString.substring(previousStartIndex) + nextString.substring(0, nextEndIndex)
            if (!isEmpty(atUsers)) {
                for (aUser in atUsers) {
                    if (aUser!!.atUserName != null && futureStr.contains(
                            aUser.atUserName!!.replace(endDivider, backupSymbol)
                        )
                    ) {
                        if (count > 0 && after == 0) {
                            editText.removeTextChangedListener(this)
                            val content: CharSequence = charSequence.toString().replace(
                                startDivider + aUser.atUserName + endDivider, ""
                            )
                            editText.setText(content)
                            editText.setSelection(content.length)
                            editText.addTextChangedListener(this)
                            atUsers.remove(aUser)
                            break
                        } else {
                            editText.removeTextChangedListener(this)
                            editText.setText(charSequence)
                            editText.setSelection(charSequence.length)
                            editText.addTextChangedListener(this)
                        }
                    }
                }
            }
        }
    }

    /**
     * @param start:  有变动的字符串的序号
     * @param before: 被改变的字符串长度，如果是新增则为0。
     * @param count:  添加的字符串长度，如果是删除则为0。
     */
    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        if (count == 1 && before == 0) {
            val subString = charSequence.toString().substring(start, start + count)
            if (subString.contains(at) || subString.contains(superAt)) {
                toAtActivity(activity.get(), 1100)
            }
        }
    }

    override fun afterTextChanged(editable: Editable) {}

    /**
     * description 重新创建一个艾特用户对象
     */
    abstract fun createBrandNewAUser(): T

    /**
     * 输入@后跳转目标
     *
     * @param activity    源
     * @param requestCode 请求code
     */
    abstract fun toAtActivity(activity: Activity?, requestCode: Int)

    /**
     * 输入@后回退响应结果
     */
    open fun onActivityResult(requestCode: Int, data: Intent?) {
        if (1100 == requestCode) {
            //艾特人员选择回调
            onAtActivityResult(data, true)
        } else if (requestCode == 1101) {
            //跳转主页后光标至于结尾
            editText.setSelection(editText.text.toString().length)
        }
    }

    /**
     * 艾特列表页回退响应结果
     *
     * @param data         回调艾特的人名与id
     * @param isRepetition 是否可重复艾特同一个人
     */
    protected open fun onAtActivityResult(data: Intent?, isRepetition: Boolean) {
        if (data == null) return
        var name: String? = ""
        var id: String? = ""
        if (data.hasExtra(NAME)) name = data.getStringExtra(NAME)
        if (data.hasExtra(ID)) id = data.getStringExtra(ID)
        var content = editText!!.text.toString()
        //避免同人多选
        if (!isRepetition) {
            for (aUser in atUsers) {
                if (TextUtils.equals(aUser!!.atUserId, id)) {
                    if (content.endsWith(at) || content.endsWith(superAt)) {
                        content = content.substring(0, content.length - 1)
                    }
                    editText.removeTextChangedListener(this)
                    editText.setText(content)
                    editText.setSelection(content.length)
                    editText.addTextChangedListener(this)
                    return
                }
            }
        }
        val userInfo = createBrandNewAUser()
        userInfo!!.atUserName = name
        userInfo.atUserId = id
        atUsers.add(userInfo)
        editText.removeTextChangedListener(this)
        val selectionStart = editText.selectionStart
        if (selectionStart >= 0) {
            var pContent = content.substring(0, selectionStart)
            val nContent = content.substring(selectionStart)
            if (pContent.endsWith(at) || pContent.endsWith(superAt)) {
                pContent = pContent.substring(0, pContent.length - 1)
            }
            val nameA = startDivider + name + endDivider
            content = pContent + nameA + nContent
        } else {
            if (content.endsWith(at) || content.endsWith(superAt)) {
                content = content.substring(0, content.length - 1)
            }
            val nameA = startDivider + name + endDivider
            content = content + nameA
        }
        editText.setText(content)
        editText.setSelection(content.length)
        editText.addTextChangedListener(this)
    }

    class ContentClickableSpan(private val onClickListener: View.OnClickListener?) :
        ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }

        override fun onClick(widget: View) {
            if (widget.isInvalidClick()) return
            onClickListener?.onClick(widget)
        }
    }

    class AClickableSpan<T : AtUser?>(
        activity: Activity,
        aUser: T,
        onAtClickListener: OnAtClickListener? = null
    ) :
        ClickableSpan() {
        private val aUser: T?
        private val onAtClickListener: OnAtClickListener?
        private val activity: WeakReference<Activity>
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }

        override fun onClick(widget: View) {
            if (widget.isInvalidClick()) return
            if (aUser?.atUserId != null) {
                onAtClickListener?.onClick(widget, aUser.atUserId)
            }
        }

        init {
            this.aUser = aUser
            this.activity = WeakReference(activity)
            this.onAtClickListener = onAtClickListener
        }

        interface OnAtClickListener {
            fun onClick(v: View?, userId: String?)
        }
    }

    interface AtUser {
        var atUserName: String?
        var atUserId: String?
    }

    companion object {
        /**
         * description: name数据Intent传递的key
         */
        const val NAME = "name"

        /**
         * description: id数据Intent传递的key
         */
        const val ID = "id"

        /**
         * description: 空格
         */
        private const val blank = " "

        /**
         * description: 英文艾特
         */
        private const val at = "@"

        /**
         * description: 中文艾特
         */
        private const val superAt = "＠"

        /**
         * description: 艾特开始符
         */
        private const val startDivider = at

        /**
         * description: 艾特结束符
         */
        private const val endDivider = blank

        /**
         * description: 备用字符（用于替代名字中的关键字符）
         */
        private const val backupSymbol = "*"

        /**
         * 对字符串添加@特效：注如果使用getANicknameString()将会失去原始特效
         *
         * @param spannableInfo 原始文本
         * @param atUsers       人员列表
         * @param contentColor  内容颜色
         */
        fun <T : AtUser?> getAtSpan(
            activity: Activity?,
            spannableInfo: SpannableString,
            atUsers: List<T>,
            start: Int = 0,
            end: Int = spannableInfo.length,
            onClickListener: View.OnClickListener? = null,
            onAtClickListener: AClickableSpan.OnAtClickListener? = null,
            @ColorInt contentColor: Int = Color.parseColor("#333333")
        ): SpannableString {
            var spannableInfoV = spannableInfo
            var endV = end
            val beforeLength = spannableInfoV.length
            spannableInfoV = getAtNameString(spannableInfoV, atUsers)
            var backup = spannableInfoV.toString()
            //替换昵称中的关键符，避免定位出错
            if (!isEmpty(atUsers)) {
                for (aUser in atUsers) {
                    val name = aUser!!.atUserName
                    if (name != null) {
                        backup = backup.replace(name, symbolReplace(name))
                    }
                }
            }
            val afterLength = spannableInfoV.length
            endV = endV + afterLength - beforeLength
            return getAtSpanToRaw(
                activity,
                spannableInfoV,
                backup,
                atUsers,
                start,
                endV,
                onClickListener,
                onAtClickListener,
                contentColor
            )
        }

        /**
         * 对字符串添加@特效——未处理中文@
         *
         * @param spannableInfo 原始文本
         * @param backup        文本替换昵称中的关键符，避免定位出错
         * @param atUsers       人员列表
         * @param contentColor  内容颜色
         */
        protected fun <T : AtUser?> getAtSpanToRaw(
            activity: Activity?,
            spannableInfo: SpannableString,
            backup: String,
            atUsers: List<T>,
            start: Int = 0,
            end: Int = spannableInfo.length,
            onClickListener: View.OnClickListener? = null,
            onAtClickListener: AClickableSpan.OnAtClickListener? = null,
            @ColorInt contentColor: Int = Color.parseColor("#333333")
        ): SpannableString {
            if (activity == null) return spannableInfo
            try {
                var content = backup.substring(start, end)
                if (content.endsWith(at)) content = content.substring(0, content.lastIndexOf(at))
                var cursor = start
                var dividerStart = content.indexOf(startDivider)
                var dividerEnd = -1
                if (dividerStart != -1) {
                    if (content.substring(dividerStart).contains(endDivider)) {
                        dividerEnd =
                            content.substring(dividerStart).indexOf(endDivider) + dividerStart
                    }
                }
                if (dividerStart == -1 || dividerEnd == -1 || isEmpty(atUsers)) {
                    if (cursor < spannableInfo.length) {
                        spannableInfo.setSpan(
                            ContentClickableSpan(onClickListener),
                            cursor,
                            spannableInfo.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannableInfo.setSpan(
                            ForegroundColorSpan(contentColor),
                            cursor,
                            spannableInfo.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    return spannableInfo
                } else {
                    dividerStart += cursor
                    dividerEnd += cursor
                }
                var misplace = 0
                for (i in 0 until Int.MAX_VALUE) {
                    if (cursor < dividerStart) {
                        spannableInfo.setSpan(
                            ContentClickableSpan(onClickListener),
                            cursor,
                            dividerStart,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        spannableInfo.setSpan(
                            ForegroundColorSpan(contentColor),
                            cursor,
                            dividerStart,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    val targetName = spannableInfo.toString().substring(dividerStart, dividerEnd)
                    if (atUsers.size > i - misplace
                        && (targetName.contains(atUsers[i - misplace]!!.atUserName!!)
                                || targetName.contains(atUsers[i - misplace]!!.atUserId!!))
                    ) {
                        if (dividerStart < dividerStart + startDivider.length) {
                            spannableInfo.setSpan(
                                null,
                                dividerStart,
                                dividerStart + startDivider.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannableInfo.setSpan(
                                ForegroundColorSpan(activity.resources.getColor(R.color.aite_name_color)),
                                dividerStart,
                                dividerStart + startDivider.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        if (dividerStart + startDivider.length < dividerEnd) {
                            spannableInfo.setSpan(
                                AClickableSpan(
                                    activity,
                                    if (atUsers.size > i - misplace) atUsers[i - misplace] else null,
                                    onAtClickListener
                                ),
                                dividerStart + startDivider.length,
                                dividerEnd,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannableInfo.setSpan(
                                ForegroundColorSpan(activity.resources.getColor(R.color.aite_name_color)),
                                dividerStart + startDivider.length,
                                dividerEnd,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        if (dividerEnd < dividerEnd + endDivider.length) {
                            spannableInfo.setSpan(
                                null,
                                dividerEnd,
                                dividerEnd + endDivider.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannableInfo.setSpan(
                                ForegroundColorSpan(activity.resources.getColor(R.color.aite_name_color)),
                                dividerEnd,
                                dividerEnd + endDivider.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    } else {
                        misplace++
                        if (dividerStart < dividerEnd + endDivider.length) {
                            spannableInfo.setSpan(
                                ContentClickableSpan(onClickListener),
                                dividerStart,
                                dividerEnd + endDivider.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannableInfo.setSpan(
                                ForegroundColorSpan(contentColor),
                                dividerStart,
                                dividerEnd + endDivider.length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    }
                    cursor = dividerEnd + endDivider.length
                    if (cursor >= spannableInfo.length) {
                        break
                    }
                    content = spannableInfo.toString().substring(cursor)
                    dividerStart = content.indexOf(startDivider)
                    if (dividerStart == -1) {
                        if (cursor < end) {
                            spannableInfo.setSpan(
                                ContentClickableSpan(onClickListener),
                                cursor,
                                end,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannableInfo.setSpan(
                                ForegroundColorSpan(contentColor),
                                cursor,
                                end,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                        break
                    }
                    dividerEnd = content.substring(dividerStart).indexOf(endDivider) + dividerStart
                    dividerStart += cursor
                    dividerEnd += cursor
                }
            } catch (e: Exception) {
                "SuperTextWatcher".e(e.message)
            }
            return spannableInfo
        }

        /**
         * 将文本中的@id替换为@昵称——已处理中文@
         *
         * @param spannableInfo 原始文本
         * @param atUsers       人员列表
         */
        fun <T : AtUser?> getAtNameString(
            spannableInfo: SpannableString,
            atUsers: List<T>
        ): SpannableString {
            if (isEmpty(atUsers)) return spannableInfo
            var content = spannableInfo.toString().replace(superAt, at)
            for (aUser in atUsers) {
                content = content.replace(
                    startDivider + aUser!!.atUserId + endDivider, startDivider + (if (isEmpty(
                            aUser.atUserName
                        )
                    ) aUser.atUserId else aUser.atUserName) + endDivider
                )
            }
            return SpannableString(content)
        }

        /**
         * 将文本中的@昵称替换为@id——已处理中文@
         *
         * @param content 原始文本
         * @param atUsers 人员列表
         */
        fun <T : AtUser?> getAtIdString(content: String, atUsers: List<T>): String {
            var contentV = content
            if (isEmpty(atUsers)) return contentV
            contentV = contentV.replace(superAt, at)
            for (aUser in atUsers) {
                contentV = contentV.replace(
                    startDivider + aUser!!.atUserName + endDivider,
                    startDivider + aUser.atUserId + endDivider
                )
            }
            return contentV
        }

        /**
         * 替换用户名中的关键字符
         */
        protected fun symbolReplace(string: String?): String {
            if (string.isNullOrEmpty()) return ""
            val regEx = "[$at$superAt$blank]"
            val p = Pattern.compile(regEx)
            val m = p.matcher(string)
            return m.replaceAll(backupSymbol).trim { it <= ' ' }
        }

        /**
         * 用户名中是否有关键字符
         *
         * @return true 有 false 无
         */
        fun isContainsSymbol(string: String?): Boolean {
            if (string.isNullOrEmpty()) return false
            val regEx = "[$at$superAt$blank]"
            val p = Pattern.compile(regEx)
            val m = p.matcher(string)
            return m.matches()
        }

        fun isEmpty(list: List<*>?): Boolean {
            return null == list || list.isEmpty()
        }

        fun isEmpty(string: String?): Boolean {
            return null == string || string == ""
        }
    }

    init {
        this.activity = WeakReference(activity)
        this.editText = editText
        this.atUsers = atUsers
    }
}