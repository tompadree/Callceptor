package callceptor.com.callceptor.di.test

import callceptor.com.callceptor.di.component.AppComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Tom on 29.8.2018..
 */
@Singleton
@Component(modules = [TestModule::class])
interface TestComponent