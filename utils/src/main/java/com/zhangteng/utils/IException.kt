package com.zhangteng.utils

import android.os.Build
import androidx.annotation.RequiresApi

open class IException : Exception {
    open var code: Int? = null

    constructor() : super()

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)

    @RequiresApi(api = Build.VERSION_CODES.N)
    constructor(
        message: String?, cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace)

    open fun handleException(): IException {
        return this
    }
}