package callceptor.com.callceptor;

import io.reactivex.schedulers.TestScheduler

/**
 * Created by Tomislav on 07,September,2018
 */
class TestSchedulersProvider (val testScheduler: TestScheduler) { // : SchedulerProvider {

         fun uiScheduler() = testScheduler

         fun ioScheduler() = testScheduler

}