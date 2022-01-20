package com.zhangteng.mvvm.base

/**
 * description: 网络数据仓库
 * author: Swing
 * date: 2021/12/28
 */
open class BaseNetRepository : BaseRepository() {

    suspend fun <T : Any> executeRequest(
        block: suspend () -> IResponse<T>
    ): IResponse<T> {
        return block.invoke()
    }
}