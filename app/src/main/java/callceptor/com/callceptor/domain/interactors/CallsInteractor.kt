package callceptor.com.callceptor.domain.interactors

import callceptor.com.callceptor.domain.listeners.OnCallLogsFetched

/**
 * Created by Tom on 22.8.2018..
 */
interface CallsInteractor {

    fun getCallLogs(onCallLogsFetched: OnCallLogsFetched)

//    fun fetchNextPage()
}