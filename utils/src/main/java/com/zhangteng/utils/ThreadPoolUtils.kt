package com.zhangteng.utils

import android.util.Log
import java.lang.Runnable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.*

/**
 * description: 线程池工具
 * author: Swing
 * date: 2021/11/24
 */
class ThreadPoolUtils private constructor() {
    /**
     * 线程池
     */
    val threadPool: ThreadPoolExecutor

    /**
     * 延时执行线程池
     */
    val appSchedule: ScheduledExecutorService

    /**
     * 任务队列
     */
    private val taskQueue = ArrayBlockingQueue<Runnable>(10000)

    /**
     * 线程池正在执行线程数量
     */
    val activeCount: Int
        get() = threadPool.activeCount

    /**
     * 任务添加到线程池中
     *
     * @param paramRunnable
     */
    fun addExecuteTask(paramRunnable: Runnable?): Future<*>? {
        return if (paramRunnable == null) {
            null
        } else threadPool.submit(paramRunnable)
    }

    /**
     * 任务添加到线程池中
     *
     * @param paramRunnable
     */
    fun addExecuteTask(paramRunnable: Callable<*>?): Future<*>? {
        return if (paramRunnable == null) {
            null
        } else threadPool.submit(paramRunnable)
    }

    /**
     * 任务从线程池中移除
     *
     * @param paramRunnable
     */
    fun removeExecuteTask(paramRunnable: Runnable?): Boolean {
        return if (paramRunnable == null) {
            false
        } else threadPool.remove(paramRunnable)
    }

    /**
     * 延时任务添加到线程池中
     *
     * @param task
     * @param delayTime
     */
    fun addDelayExecuteTask(task: Runnable?, delayTime: Int) {
        appSchedule.schedule(
            DelayTask(task), delayTime.toLong(),
            TimeUnit.MILLISECONDS
        )
    }

    /**
     * 延时固定周期执行
     *
     * @param task
     * @param delay
     * @param period
     */
    fun addPeriodDelayExecuteTask(task: Runnable?, delay: Long?, period: Long?) {
        appSchedule.scheduleWithFixedDelay(
            DelayTask(task),
            delay!!,
            period!!,
            TimeUnit.MILLISECONDS
        )
    }

    /**
     * 延时任务
     */
    private class DelayTask(private val task: Runnable?) : Runnable {
        override fun run() {
            instance.addExecuteTask(task)
        }
    }

    companion object {
        /**
         * 得到线程池的实例
         *
         * @return
         */
        /**
         * 线程池实例化
         */
        val instance = ThreadPoolUtils()

        /**
         * 核心线程个数
         */
        val corePoolCount = Runtime.getRuntime().availableProcessors() * 2

        /**
         * 最大线程个数
         */
        val maximumPoolSize = Runtime.getRuntime().availableProcessors() * 5

        /**
         * 保持心跳时间
         */
        const val keepAliveTime = 1

        /**
         * 定时执行线程个数
         */
        const val minSchedule = 2

        private fun printException(r: Runnable?, throwable: Throwable?) {
            var t: Throwable? = throwable
            if (t == null && r is Future<*>) {
                try {
                    val future = r as Future<*>
                    if (future.isDone) {
                        future.get()
                    }
                } catch (ce: CancellationException) {
                    t = ce
                } catch (ee: ExecutionException) {
                    t = ee.cause
                } catch (ie: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
            if (t != null) {
                val cause = t.cause
                if (isTimeoutThrowable(cause)) {
                    Log.e(
                        instance.javaClass.simpleName,
                        "系统自有线程池任务调用超时异常,error_msg==" + cause!!.message
                    )
                } else {
                    Log.e(instance.javaClass.simpleName, "系统自有线程池任务异常,error_msg==" + t.message)
                }
            }
        }

        private fun isTimeoutThrowable(cause: Throwable?): Boolean {
            return (cause is TimeoutException
                    || cause is SocketTimeoutException
                    || cause is ConnectException)
        }
    }

    init {
        val myHandler = RejectedExecutionHandler { r: Runnable, _: ThreadPoolExecutor? ->
            taskQueue.offer(r)
        }
        threadPool = object : ThreadPoolExecutor(
            if (corePoolCount <= 0) {
                2
            } else {
                corePoolCount
            },
            if (maximumPoolSize <= 0) {
                5
            } else {
                maximumPoolSize
            },
            keepAliveTime.toLong(),
            TimeUnit.SECONDS,
            ArrayBlockingQueue(20),
            Executors.defaultThreadFactory(), myHandler
        ) {
            public override fun afterExecute(r: Runnable?, t: Throwable?) {
                super.afterExecute(r, t)
                printException(r, t)
            }
        }
        appSchedule = ScheduledThreadPoolExecutor(minSchedule, Executors.defaultThreadFactory())

        val command = Runnable {
            threadPool.execute(
                try {
                    //使用具备阻塞特性的方法
                    taskQueue.take()
                } catch (e: InterruptedException) {
                    return@Runnable
                }
            )
        }
        val scheduledPool: ScheduledExecutorService =
            ScheduledThreadPoolExecutor(1, Executors.defaultThreadFactory())
        //每一次执行终止和下一次执行开始之间都存在给定的延迟 16毫秒
        scheduledPool.scheduleWithFixedDelay(command, 0L, 16L, TimeUnit.MILLISECONDS)
    }
}