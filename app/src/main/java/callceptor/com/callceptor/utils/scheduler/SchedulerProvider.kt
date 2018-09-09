package callceptor.com.callceptor.utils.scheduler

import io.reactivex.Scheduler

/**
 * Created by Tom on 8.9.2018..
 */

interface SchedulerProvider {
    fun uiScheduler(): Scheduler
    fun ioScheduler(): Scheduler
}