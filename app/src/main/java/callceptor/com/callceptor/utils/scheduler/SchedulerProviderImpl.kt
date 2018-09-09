package callceptor.com.callceptor.utils.scheduler

import callceptor.com.callceptor.di.module.ThreadModule
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Tom on 8.9.2018..
 */
class SchedulerProviderImpl : SchedulerProvider {

    override fun uiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    override fun ioScheduler(): Scheduler = Schedulers.io()
}