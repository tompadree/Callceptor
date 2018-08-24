package callceptor.com.callceptor.presenter

/**
 * Created by Tom on 22.8.2018..
 */
interface CallsPresenter : BasePresenter {

    fun fetchCallLogs()

    fun fetchNextPage()
}