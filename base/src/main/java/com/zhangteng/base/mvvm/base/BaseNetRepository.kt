package com.zhangteng.base.mvvm.base

open class BaseNetRepository : BaseRepository() {

    suspend fun <T : Any> executeRequest(
        block: suspend () -> IResponse<T>
    ): IResponse<T> {
        return block.invoke()
    }
}