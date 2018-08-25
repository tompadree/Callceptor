package callceptor.com.callceptor.domain.interactors

import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.domain.listeners.OnCallContactsFetched


/**
 * Created by Tom on 22.8.2018..
 */
interface CallsInteractor {

    fun getCallLogs(initonCallContactsFetched: OnCallContactsFetched)

    fun getContacts(onCallContactsFetched: OnCallContactsFetched)

    fun saveLastCall(calls: ArrayList<Call>)

    fun fetchNextPage()

    fun destroyDisposable()
}