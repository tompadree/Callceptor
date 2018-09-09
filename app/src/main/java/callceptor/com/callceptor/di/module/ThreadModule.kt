package callceptor.com.callceptor.di.module

import callceptor.com.callceptor.utils.scheduler.SchedulerProvider
import callceptor.com.callceptor.utils.scheduler.SchedulerProviderImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Tom on 21.8.2018..
 */
@Module
class ThreadModule {

//    @Provides
//    @Singleton
//    @Named(OBSERVE_SCHEDULER)
//    fun provideObservableScheduler(): Scheduler = AndroidSchedulers.mainThread()
//
//    @Provides
//    @Singleton
//    @Named(SUBSCRIBE_SCHEDULER)
//    fun provideSubscribeScheduler(): Scheduler = Schedulers.io()


    @Provides
    @Singleton
    fun providesSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

//    companion object {
//
//        const val OBSERVE_SCHEDULER = "ObserveScheduler"
//
//        const val SUBSCRIBE_SCHEDULER = "SubscribeScheduler"
//    }

}