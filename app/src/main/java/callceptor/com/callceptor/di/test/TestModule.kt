package callceptor.com.callceptor.di.test

import callceptor.com.callceptor.di.module.ThreadModule
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Tom on 29.8.2018..
 */
@Module
class TestModule {

    @Provides
    @Singleton
    @Named(ThreadModule.OBSERVE_SCHEDULER)
    fun provideAndroidSchedulersMainThread(): Scheduler = AndroidSchedulers.mainThread()


    @Provides
    @Singleton
    @Named(ThreadModule.SUBSCRIBE_SCHEDULER)
    fun provideSchedulersIo(): Scheduler = Schedulers.io()

}