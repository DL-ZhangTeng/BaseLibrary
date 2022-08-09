package com.zhangteng.mvvm.base

/**
 * 带统一响应的数据仓库（建议定义*RemoteDataSource和*LocalDataSource区分网络数据源和本地数据源）
 *
 * class NewsRepository(
 * private val newsRemoteDataSource: NewsRemoteDataSource
 * private val newsLocalDataSource: newsLocalDataSource
 * ) {
 *  suspend fun fetchLatestNews(): List<ArticleHeadline> =
 *      newsRemoteDataSource.fetchLatestNews()
 * }
 *
 * class NewsRemoteDataSource(
 *  private val newsApi: NewsApi,
 *  private val ioDispatcher: CoroutineDispatcher
 * ) {
 *  suspend fun fetchLatestNews(): List<ArticleHeadline> =
 *      withContext(ioDispatcher) {
 *          newsApi.fetchLatestNews()
 *      }
 * }
 *
 * interface NewsApi {
 *  fun fetchLatestNews(): List<ArticleHeadline>
 * }
 */
open class BaseNetRepository : BaseRepository() {

    suspend fun <T : Any> executeRequest(
        block: suspend () -> IResponse<T>
    ): IResponse<T> {
        return block.invoke()
    }
}