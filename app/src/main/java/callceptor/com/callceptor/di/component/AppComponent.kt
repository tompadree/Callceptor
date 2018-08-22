package callceptor.com.callceptor.di.component

import android.content.Context
import callceptor.com.callceptor.data.db.CallceptorDatabase
import callceptor.com.callceptor.di.module.AppModule
import callceptor.com.callceptor.di.module.DataModule
import callceptor.com.callceptor.di.module.ThreadModule
import callceptor.com.callceptor.di.module.ThreadModule.Companion.OBSERVE_SCHEDULER
import callceptor.com.callceptor.di.module.ThreadModule.Companion.SUBSCRIBE_SCHEDULER
import callceptor.com.callceptor.view.BaseActivity
import callceptor.com.callceptor.view.BaseFragment
import dagger.Component
import io.reactivex.Scheduler
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Tom on 21.8.2018..
 */
@Singleton
@Component(modules = [AppModule::class, ThreadModule::class, DataModule::class]) // //, NetModule::class])
interface AppComponent {

    fun context(): Context

//    fun networkApi(): NetworkApi

    fun callceptorDatabase(): CallceptorDatabase

    @Named(OBSERVE_SCHEDULER)
    fun provideAndroidSchedulersMainThread(): Scheduler

    @Named(SUBSCRIBE_SCHEDULER)
    fun provideSchedulersIo(): Scheduler

    fun inject(baseActivity: BaseActivity)

    fun inject(baseFragment: BaseFragment)


}