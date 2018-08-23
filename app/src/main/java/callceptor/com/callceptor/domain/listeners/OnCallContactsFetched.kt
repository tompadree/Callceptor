package callceptor.com.callceptor.domain.listeners

import callceptor.com.callceptor.data.models.Call
import java.util.ArrayList

/**
 * Created by Tom on 22.8.2018..
 */
interface OnCallContactsFetched {

    fun callLogsFetched(list : ArrayList<Call>)

    fun contactsFetched(list : ArrayList<String>)

    fun onFetchingError(e : Throwable)

}