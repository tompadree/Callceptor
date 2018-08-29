package callceptor.com.callceptor

import callceptor.com.callceptor.di.component.AppComponent
import callceptor.com.callceptor.di.test.TestModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Tom on 29.8.2018..
 */
@Singleton
@Component(modules = [TestModule::class])
interface TstCOmponent : AppComponent {

}