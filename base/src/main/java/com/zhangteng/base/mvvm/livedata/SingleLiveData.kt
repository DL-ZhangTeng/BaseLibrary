package com.zhangteng.base.mvvm.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 使 observe#LiveData时只相应一次onChanged操作
 * 可避免共享作用域 VM 下 liveData 被 observe 时旧数据倒灌的情况
 */
class SingleLiveData<T> : MutableLiveData<T>() {
    private val mPending = AtomicBoolean(false)


    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(
                "SingleLiveEvent",
                "Multiple observers registered but only one will be notified of changes."
            )
        }
        super.observe(owner, { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }


    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }


    @MainThread
    fun call() {
        value = null
    }
}