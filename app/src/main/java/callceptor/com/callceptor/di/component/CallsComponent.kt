package callceptor.com.callceptor.di.component

import callceptor.com.callceptor.di.HomeScope
import callceptor.com.callceptor.di.module.CallsModule
import callceptor.com.callceptor.view.fragments.CallsFragment
import dagger.Component

/**
 * Created by Tom on 22.8.2018..
 */
@HomeScope
@Component(dependencies = [AppComponent::class], modules = [CallsModule::class])
interface CallsComponent {

    fun inject (callsFragment: CallsFragment)
}