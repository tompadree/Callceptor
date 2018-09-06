package callceptor.com.callceptor;

import callceptor.com.callceptor.di.module.ThreadModule
import io.reactivex.schedulers.TestScheduler

/**
 * Created by Tomislav on 06,September,2018
 */
class TestSchedulerProvider(val testScheduler: TestScheduler)  {

     fun uiScheduler() = ThreadModule.OBSERVE_SCHEDULER

     fun ioScheduler() = ThreadModule.SUBSCRIBE_SCHEDULER
}