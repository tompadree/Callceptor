package callceptor.com.callceptor.di.component

import callceptor.com.callceptor.di.HomeScope
import callceptor.com.callceptor.di.module.MessageModule
import callceptor.com.callceptor.view.fragments.MessagesFragment
import dagger.Component

/**
 * Created by Tom on 24.8.2018..
 */
@HomeScope
@Component(dependencies = [AppComponent::class], modules = [MessageModule::class])
interface MessagesComponent {

    fun inject(messagesFragment: MessagesFragment)
}