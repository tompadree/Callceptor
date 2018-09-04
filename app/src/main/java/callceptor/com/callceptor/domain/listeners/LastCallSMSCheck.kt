package callceptor.com.callceptor.domain.listeners

import callceptor.com.callceptor.data.models.Call

/**
 * Created by Tom on 26.8.2018..
 */
interface LastCallSMSCheck {

    fun refreshCallList(lastNumber : String)

    fun refreshSMSList(lastNumber : String)
}