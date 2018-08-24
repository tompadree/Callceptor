package callceptor.com.callceptor.presenter

/**
 * Created by Tom on 24.8.2018..
 */
interface MessagesPresenter : BasePresenter{

    fun fetchMessages()

    fun fetchNextPage()
}