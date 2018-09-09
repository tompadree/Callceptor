package callceptor.com.callceptor.domain.interactors

import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.domain.listeners.OnMessagesFetched
import java.util.ArrayList


/**
 * Created by Tom on 24.8.2018..
 */
interface MessageInteractor{

    fun getMessages(onMessagesFetched: OnMessagesFetched)

    fun idLastNumber(onMessagesFetched: OnMessagesFetched, lastNumber : String)

    fun saveLocalResults(messages: ArrayList<Message>, onMessagesFetched: OnMessagesFetched)

    fun saveLastMessage(message: Message)

    fun fetchNextPage()

    fun destroyDisposable()

}
