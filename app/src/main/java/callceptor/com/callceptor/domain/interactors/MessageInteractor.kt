package callceptor.com.callceptor.domain.interactors

import callceptor.com.callceptor.domain.listeners.OnMessagesFetched


/**
 * Created by Tom on 24.8.2018..
 */
interface MessageInteractor{

    fun getMessages(onMessagesFetched: OnMessagesFetched)
}
