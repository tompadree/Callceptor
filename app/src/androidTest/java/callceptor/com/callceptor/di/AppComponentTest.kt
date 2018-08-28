package callceptor.com.callceptor.di

import callceptor.com.callceptor.di.component.AppComponent
import callceptor.com.callceptor.di.module.AppModule
import callceptor.com.callceptor.di.module.DataModule
import callceptor.com.callceptor.di.module.ThreadModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Tom on 28.8.2018..
 */
@Singleton
@Component(modules = [AppModule::class, DataModule::class, ThreadModule::class])
interface AppComponentTest : AppComponent {

//    fun inject(GitResultsActivityTest : GitResultsActivityTest)
}